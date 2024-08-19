package com.example.caloric.profile.presenter;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;


import com.example.caloric.model.Meal;
import com.example.caloric.model.RepoInterface;
import com.example.caloric.profile.view.ProfileViewInterface;

import java.util.List;

public class ProfilePresenter implements ProfilePresenterInterface {
    private RepoInterface repo;
    private ProfileViewInterface profileView;

    public ProfilePresenter(RepoInterface repo, ProfileViewInterface profileView){
        this.repo = repo;
        this.profileView = profileView;
    }

    @Override
    public void deleteAllFavouriteMeals() {
        repo.deleteAllFavouriteMeals();
    }

    @Override
    public void getAllFavouriteMeals() {
        repo.getAllFavouriteMeals().observe((LifecycleOwner) profileView, new Observer<List<Meal>>() {
            @Override
            public void onChanged(List<Meal> meals) {
                profileView.onGetAllFavouriteList(meals);
            }
        });
    }
}
