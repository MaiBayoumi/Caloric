package com.example.caloric.recipe.presenter;


import com.example.caloric.database.LocalDataSource;
import com.example.caloric.model.Category;
import com.example.caloric.model.Country;
import com.example.caloric.model.Ingredient;
import com.example.caloric.model.Meal;
import com.example.caloric.model.MealResponse;
import com.example.caloric.model.RepoInterface;
import com.example.caloric.network.NetworkDelegate;
import com.example.caloric.recipe.view.RecipeViewInterface;

import java.util.List;

public class RecipePresenter implements NetworkDelegate, RecipePresenterInterface {
    private RepoInterface repo;
    private RecipeViewInterface detailsView;
    //LocalDataSource localDataSource;

    public RecipePresenter(RepoInterface repo, RecipeViewInterface detailsView){
        this.repo = repo;
        this.detailsView = detailsView;
    }

    @Override
    public void getMealById(String id) {
        repo.getMealById(id,this);

    }

    @Override
    public void insertMealToFavourite(Meal meal) {

        repo.insertMealToFavourite(meal);
    }

    @Override
    public void updateDayOfMeal(String id, String day) {
        repo.updateDayOfMeal(id, day);
    }

    @Override
    public void insertMealToCalendar(Meal meal, String day) {
        // Logic to save the meal to the calendar in the database with the selected day
        repo.insertMealToCalendar(meal, day);
    }

    @Override
    public void onSuccessResultMeal(List<Meal> meals) {
        detailsView.onGetMealDetails(meals);
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
        detailsView.onFailToGetMealDetails(message);
    }


}
