package com.example.caloric.meals.view;

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

import com.example.caloric.R;
import com.example.caloric.database.LocalDataSource;
import com.example.caloric.database.LocalSource;
import com.example.caloric.meals.presenter.MealsPresenter;
import com.example.caloric.meals.presenter.MealsPresenterInterface;
import com.example.caloric.model.Meal;
import com.example.caloric.model.MealResponse;
import com.example.caloric.model.Repo;
import com.example.caloric.model.RepoInterface;
import com.example.caloric.network.RemoteDataSource;
import com.example.caloric.network.RemoteSource;

import java.util.ArrayList;


public class MealsFrag extends Fragment implements OnCommonClickInterface, MealsViewInterface {

    MealResponse meals;
    RecyclerView mealRecycler;
    MealsAdapter mealsAdapter;
    MealsPresenterInterface presenter;
    TextView caloric;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        caloric= view.findViewById(R.id.caloric);
        mealsAdapter = new MealsAdapter(view.getContext(), this);


        Bundle args = getArguments();
        if (args != null) {
            meals = (MealResponse) args.getSerializable("meals");
        }
        mealRecycler.setAdapter(mealsAdapter);

        mealsAdapter.setList((ArrayList<Meal>) meals.getMeals());
        mealsAdapter.notifyDataSetChanged();


        RemoteSource remoteSource = RemoteDataSource.getInstance(view.getContext());
        LocalSource localSource = LocalDataSource.getInstance(view.getContext());
        RepoInterface repo = Repo.getInstance(remoteSource, localSource);
        presenter = new MealsPresenter(repo, this);
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

}