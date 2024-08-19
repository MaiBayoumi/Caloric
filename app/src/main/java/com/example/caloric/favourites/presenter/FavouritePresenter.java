package com.example.caloric.favourites.presenter;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;


import com.example.caloric.favourites.view.FavouriteViewInterface;
import com.example.caloric.model.Meal;
import com.example.caloric.model.RepoInterface;

import java.util.List;

public class FavouritePresenter implements FavouritePresenterInterface {
    private RepoInterface repo;
    private FavouriteViewInterface favouriteView;


    public FavouritePresenter(RepoInterface repo, FavouriteViewInterface favouriteView){
        this.repo = repo;
        this.favouriteView = favouriteView;
    }

    @Override
    public void getAllMeals() {
        repo.getAllFavouriteMeals().observe((LifecycleOwner) favouriteView, new Observer<List<Meal>>() {
            @Override
            public void onChanged(List<Meal> meals) {
                favouriteView.onGetFavouriteMeals(meals);
            }
        });
    }

    @Override
    public void deleteMeal(Meal meal) {
        repo.deleteMealFromFavourite(meal);
    }
}
