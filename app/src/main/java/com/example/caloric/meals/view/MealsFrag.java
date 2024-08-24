package com.example.caloric.meals.view;

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

import com.example.caloric.R;
import com.example.caloric.database.LocalDataSource;
import com.example.caloric.database.LocalSource;
import com.example.caloric.meals.presenter.MealsPresenter;
import com.example.caloric.meals.presenter.MealsPresenterInterface;
import com.example.caloric.model.Country;
import com.example.caloric.model.Meal;
import com.example.caloric.model.MealResponse;
import com.example.caloric.model.Repo;
import com.example.caloric.model.RepoInterface;
import com.example.caloric.network.RemoteDataSource;
import com.example.caloric.network.RemoteSource;
import com.example.caloric.view.HostedActivity;

import java.util.ArrayList;
import java.util.List;


public class MealsFrag extends Fragment implements OnCommonClickInterface, MealsViewInterface {

    MealResponse meals;
    String filter, filterType;
    RecyclerView mealRecycler;
    MealsAdapter mealsAdapter;
    MealsPresenterInterface presenter;
    TextView caloric;
    ImageButton back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        ((HostedActivity) requireActivity()).bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mealRecycler = view.findViewById(R.id.mealRecycler);
        caloric = view.findViewById(R.id.caloric);
        back = view.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_mealsFrag_to_recommendationFrag);
            }
        });


        mealsAdapter = new MealsAdapter(view.getContext(), this);
        RemoteSource remoteSource = RemoteDataSource.getInstance(view.getContext());
        LocalSource localSource = LocalDataSource.getInstance(view.getContext());
        RepoInterface repo = Repo.getInstance(remoteSource, localSource);
        presenter = new MealsPresenter(repo, this);

        Bundle args = getArguments();
        if (args != null) {
            filter = args.getString("filter");
            filterType = args.getString("filterType");
            Log.d("MAI", "onViewCreated: " + filter + " " + filterType);

            if ("country".equals(filterType)) {
                presenter.getCountryMeals(filter);
            } else {
                presenter.getCategoryMeals(filter);
            }
        }

        //meals = (MealResponse) args.getSerializable("filter");
        mealRecycler.setAdapter(mealsAdapter);
        //mealsAdapter.setList((ArrayList<Meal>) meals.getMeals());
        mealsAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        ((HostedActivity) requireActivity()).bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onSaveBtnClicked(Meal meal) {
        presenter.insertMealToFavourite(meal);
    }

    @Override
    public void onMealItemClicked(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        NavController navController = Navigation.findNavController(getView());
        navController.navigate(R.id.action_mealsFrag_to_mealRecipeFrag, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((HostedActivity) requireActivity()).bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMealInserted() {
        Toast.makeText(getContext(), "meal has been successfully added.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onError(String s) {
        Toast.makeText(getContext(), "Error: " + s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetMealsByCountry(List<Meal> countryMeals) {
        //countryMeals = presenter.getCountryMeals(countryMeals);
        if (countryMeals != null && !countryMeals.isEmpty()) {
            Log.d("MAI", "No meals found for the selected country " + countryMeals.size());

            mealsAdapter.setList(countryMeals);
            mealsAdapter.notifyDataSetChanged();
        } else {
            Log.d("MAI", "No meals found for the selected country");
        }
    }

    @Override
    public void onGetMealsByCategory(List<Meal> categoryMeals) {
        if (categoryMeals != null && !categoryMeals.isEmpty()) {
            mealsAdapter.setList(categoryMeals);
            mealsAdapter.notifyDataSetChanged();
        } else {
            Log.d("MAI", "No meals found for the selected category");

        }

    }

}