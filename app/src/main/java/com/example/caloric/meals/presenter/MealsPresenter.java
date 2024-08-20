package com.example.caloric.meals.presenter;


import com.example.caloric.meals.view.MealsViewInterface;
import com.example.caloric.model.Category;
import com.example.caloric.model.Country;
import com.example.caloric.model.Ingredient;
import com.example.caloric.model.Meal;
import com.example.caloric.model.MealResponse;
import com.example.caloric.model.RepoInterface;
import com.example.caloric.network.NetworkDelegate;

import java.util.List;

public class MealsPresenter implements NetworkDelegate, MealsPresenterInterface {
    private RepoInterface repo;
    private MealsViewInterface mealsViewInterface;

    public MealsPresenter(RepoInterface repo, MealsViewInterface commonMealView) {
        this.repo = repo;
        this.mealsViewInterface = commonMealView;
    }

    @Override
    public void insertMealToFavourite(Meal meal) {
        repo.insertMealToFavourite(meal);
    }

    @Override
    public void onSuccessResultMeal(List<Meal> meals) {

    }

    @Override
    public void onSuccessFilter(MealResponse meals) {

    }

    @Override
    public void onSuccessResultCategory(List<Category> categories) {

    }

    @Override
    public void onSuccessResultIngredient(List<Ingredient> ingredients) {

    }

    @Override
    public void onSuccessResultCountries(List<Country> countries) {

    }

    @Override
    public void onFailureResult(String message) {

    }
}
