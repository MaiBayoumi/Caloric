package com.example.caloric.calender.view;

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


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloric.R;
import com.example.caloric.calender.presenter.PlannerPresenter;
import com.example.caloric.calender.presenter.PlannerPresenterInterface;
import com.example.caloric.database.LocalDataSource;
import com.example.caloric.database.LocalSource;

import com.example.caloric.model.Meal;
import com.example.caloric.model.Repo;
import com.example.caloric.model.RepoInterface;
import com.example.caloric.network.RemoteDataSource;
import com.example.caloric.network.RemoteSource;
import com.example.caloric.view.HostedActivity;
import java.util.ArrayList;
import java.util.List;


public class PlannerFrag extends Fragment implements PlannerViewInterface, OnDayClickInterface {


    String day;
    TextView nameDay;
    RecyclerView mealsRecyclerPlan;
    PlannerPresenterInterface detailsPresenter;
    DayAdapter dayAdapter;


    @Override
    public void onStart() {
        super.onStart();
        ((HostedActivity) requireActivity()).bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_planner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameDay = view.findViewById(R.id.day);
        mealsRecyclerPlan = view.findViewById(R.id.recyclerView);
        dayAdapter = new DayAdapter(view.getContext(), this);
        mealsRecyclerPlan.setAdapter(dayAdapter);


        if (getArguments() != null) {
            day = getArguments().getString("day");
        }

        RemoteSource remoteSource = RemoteDataSource.getInstance(getContext());
        LocalSource localSource = LocalDataSource.getInstance(getContext());
        RepoInterface repo = Repo.getInstance(remoteSource, localSource);
        detailsPresenter = new PlannerPresenter(repo, this);

        detailsPresenter.getMealsForDay(day);
        nameDay.setText(day);
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
            Bundle bundle = new Bundle();
            bundle.putString("mealId", id);

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
    }

    private boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }
}