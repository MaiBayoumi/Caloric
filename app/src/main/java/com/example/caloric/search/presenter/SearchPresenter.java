package com.example.caloric.search.presenter;


import com.example.caloric.model.Category;
import com.example.caloric.model.Country;
import com.example.caloric.model.Ingredient;
import com.example.caloric.model.Meal;
import com.example.caloric.model.MealResponse;
import com.example.caloric.model.RepoInterface;
import com.example.caloric.network.NetworkDelegate;

import java.util.List;

public class SearchPresenter implements NetworkDelegate, SearchPresenterInterface {
    private RepoInterface repo;
    private SearchViewInterface searchView;

    public SearchPresenter(RepoInterface repo, SearchViewInterface searchView) {
        this.repo = repo;
        this.searchView = searchView;
    }

    @Override
    public void getMealsByIngredient(String ingredient) {
        repo.getMealsByIngredient(ingredient, this);
    }

    @Override
    public void getMealsByCategory(String category) {
        repo.getMealsByCategory(category, this);
    }

    @Override
    public void getMealsByCountry(String country) {
        repo.getMealsByCountry(country, this);
    }

    @Override
    public void getMealByName(String name) {
        repo.getMealByName(name, this);
    }

    @Override
    public void getMealByFirstChar(String firstChar) {
        repo.getMealByFirstChar(firstChar, this);
    }

    @Override
    public void getRandomMeal() {
        repo.getRandomMeal(this);
    }

    @Override
    public void insertMeal(Meal meal) {
        repo.insertMealToFavourite(meal);
    }

    @Override
    public void onSuccessResultMeal(List<Meal> meals) {
        searchView.onGetMeals(meals);
    }

    @Override
    public void onSuccessFilter(MealResponse meals) {
        searchView.onGetMeals(meals.getMeals());
    }

    @Override
    public void onSuccessResultCategory(List<Category> categories) {
        searchView.onGetAllCategories(categories);
    }

    @Override
    public void onSuccessResultIngredient(List<Ingredient> ingredients) {
        searchView.onGetAllIngredient(ingredients);
    }

    @Override
    public void onSuccessResultCountries(List<Country> countries) {
        searchView.onGetAllCountries(countries);
    }

    @Override
    public void onFailureResult(String message) {
        searchView.onFailureResult(message);
    }


}
