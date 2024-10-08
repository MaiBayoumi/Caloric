package com.example.caloric.Planner.view;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloric.Planner.model.PlannerModel;
import com.example.caloric.R;
import com.example.caloric.Planner.presenter.PlannerPresenter;
import com.example.caloric.Planner.presenter.PlannerPresenterInterface;
import com.example.caloric.database.LocalDataSource;
import com.example.caloric.database.LocalSource;
import com.example.caloric.model.Meal;
import com.example.caloric.model.Repo;
import com.example.caloric.model.RepoInterface;
import com.example.caloric.network.RemoteDataSource;
import com.example.caloric.network.RemoteSource;
import com.example.caloric.view.HostedActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class PlannerFrag extends Fragment implements PlannerViewInterface, OnDayClickInterface {

    private String day;
    private TextView nameDay;
    private RecyclerView mealsRecyclerPlan;
    private PlannerPresenterInterface detailsPresenter;
    private DayAdapter dayAdapter;
    private ImageButton back;
    private AuthStateListener authListener;
    private String userId;

    @Override
    public void onStart() {
        super.onStart();
        ((HostedActivity) requireActivity()).bottomNavigationView.setVisibility(View.GONE);
        FirebaseAuth.getInstance().addAuthStateListener(authListener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            day = savedInstanceState.getString("day");
        } else if (getArguments() != null) {
            day = getArguments().getString("day");
        }

        authListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                Log.e("PlannerFrag", "User signed out");
                // Optionally, redirect to login or show a message
                // navigateToLogin();
            } else {
                userId = user.getUid();
                Log.d("PlannerFrag", "User signed in with ID: " + userId);
                if (day != null) {
                    detailsPresenter.getMealsForDay(day, userId);
                }
            }
        };
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_planner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameDay = view.findViewById(R.id.day);
        mealsRecyclerPlan = view.findViewById(R.id.recyclerView);
        back = view.findViewById(R.id.back);
        dayAdapter = new DayAdapter(view.getContext(), this, new ArrayList<>());
        mealsRecyclerPlan.setAdapter(dayAdapter);

        nameDay.setText(day != null ? day : "No Day Selected");

        RemoteSource remoteDataSourceSource = RemoteDataSource.getInstance(getContext());
        LocalSource localSource = LocalDataSource.getInstance(getContext());
        RepoInterface repo = Repo.getInstance(remoteDataSourceSource, localSource);
        detailsPresenter = new PlannerPresenter(repo, this);

        if (day != null && userId != null) {
            Log.d("PlannerFrag", "Fetching meals for day: " + day);
            detailsPresenter.getMealsForDay(day, userId);
            nameDay.setText(day);
        }

        back.setOnClickListener(view1 -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_plannerFrag_to_mealPlanFrag);
        });
    }

    @Override
    public void onGetMealOfDay(List<PlannerModel> meals) {
        if (meals != null) {
            Log.d("PlannerFrag", meals.size() + " meals found for day " + day);
            dayAdapter.setList((ArrayList<PlannerModel>) meals);
        } else {
            Log.d("PlannerFrag", "No meals found for day " + day);
        }
    }

    @Override
    public void onError(String s) {
        Toast.makeText(getContext(), "Error: " + s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMealDeleted() {
        Toast.makeText(getContext(), "Meal removed successfully", Toast.LENGTH_SHORT).show();
        if (day != null && userId != null) {
            detailsPresenter.getMealsForDay(day, userId);
        }
    }

    @Override
    public void onDeleteBtnClicked(PlannerModel meal) {
        detailsPresenter.deletePlannedMeal(meal);
    }

    @Override
    public void onFavItemClicked(String id) {
        if (checkConnection()) {
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            NavController navController = Navigation.findNavController(getView());
            navController.navigate(R.id.action_plannerFrag_to_mealRecipeFrag, bundle);
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((HostedActivity) requireActivity()).bottomNavigationView.setVisibility(View.VISIBLE);
        if (detailsPresenter instanceof PlannerPresenter) {
            ((PlannerPresenter) detailsPresenter).clearDisposables();
        }
        FirebaseAuth.getInstance().removeAuthStateListener(authListener);
    }

    private boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }
}
