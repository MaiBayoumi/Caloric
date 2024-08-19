package com.example.caloric;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloric.database.LocalDataSource;
import com.example.caloric.database.LocalSource;
import com.example.caloric.model.Meal;
import com.example.caloric.model.Repo;
import com.example.caloric.model.RepoInterface;
import com.example.caloric.network.ClientService;
import com.example.caloric.network.RemoteSource;
import com.example.caloric.planner.presenter.DayDetailsPresenter;
import com.example.caloric.planner.presenter.DayPresenterInterface;
import com.example.caloric.planner.view.DayMealAdapter;
import com.example.caloric.planner.view.DayViewInterface;
import com.example.caloric.planner.view.OnDayClickInterface;
import com.example.caloric.planner.view.SharedViewModel;
import com.example.caloric.view.HostedActivity;

import java.util.ArrayList;
import java.util.List;

public class PlannerFrag extends Fragment implements DayViewInterface, OnDayClickInterface {

    private String day;
    private TextView nameDay;
    private RecyclerView mealsRecyclerPlan;
    private DayPresenterInterface detailsPresenter;
    private DayMealAdapter dayAdapter;

    private SharedViewModel sharedViewModel;

    @Override
    public void onStart() {
        super.onStart();
        ((HostedActivity) requireActivity()).bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_planner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);

        // Observe the selected day LiveData
        sharedViewModel.getSelectedDay().observe(getViewLifecycleOwner(), selectedDay -> {
            day = selectedDay;
            RemoteSource remoteSource = ClientService.getInstance(getContext());
            LocalSource localSource = LocalDataSource.getInstance(getContext());
            RepoInterface repo = Repo.getInstance(remoteSource, localSource);
            detailsPresenter = new DayDetailsPresenter(repo, this);
            detailsPresenter.getMealsForDay(day);
            nameDay.setText(day);
        });
    }

    private void initializeViews(View view) {
        nameDay = view.findViewById(R.id.day);
        mealsRecyclerPlan = view.findViewById(R.id.recyclerView);
        dayAdapter = new DayMealAdapter(view.getContext(), this);
        mealsRecyclerPlan.setAdapter(dayAdapter);
    }

    @Override
    public void onGetMealOfDay(List<Meal> favouriteMeals) {
        if (favouriteMeals != null) {
            dayAdapter.setList((ArrayList<Meal>) favouriteMeals);
            dayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDeleteBtnClicked(Meal meal) {
        detailsPresenter.updateDayOfMeal(meal.getIdMeal(), "no day");
    }

    @Override
    public void onFavItemClicked(String id) {
        if (checkConnection()) {
            sharedViewModel.setSelectedMealId(id);
            NavController navController = Navigation.findNavController(getView());
            navController.navigate(R.id.action_plannerFrag_to_mealRecipeFrag);
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((HostedActivity) requireActivity()).bottomNavigationView.setVisibility(View.VISIBLE);
    }

    private boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }
}
