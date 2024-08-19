package com.example.caloric.home.presenter;


import com.example.caloric.home.view.RecommendationViewInterface;
import com.example.caloric.model.Category;
import com.example.caloric.model.Country;
import com.example.caloric.model.Ingredient;
import com.example.caloric.model.Meal;
import com.example.caloric.model.MealResponse;
import com.example.caloric.model.RepoInterface;
import com.example.caloric.network.NetworkDelegate;

import java.util.List;

public class RecommendationPresenter implements NetworkDelegate, RecommendationPresenterInterface {
    private RepoInterface repo;
    private RecommendationViewInterface homeView;

    public RecommendationPresenter(RepoInterface repo, RecommendationViewInterface home){
        this.repo = repo;
        this.homeView = home;
    }
    //presenter
    @Override
    public void getRandomMeal() {

        repo.getRandomMeal(this);
    }

    @Override
    public void getAllCategories() {
        repo.getAllCategories(this);
    }

    @Override
    public void getAllCountries() {
        repo.getAllCountries(this);
    }

    @Override
    public void getAllIngredient() {
        repo.getAllIngredient(this);
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
    public void deleteAllFavMeals() {
        repo.deleteAllFavouriteMeals();
    }

    @Override
    public void insertMeal(Meal meal) {
        repo.insertMealToFavourite(meal);
    }

    @Override
    public void insertAllFav(List<Meal> meals) {
        repo.insertAllFav(meals);
    }




    //Delegate
    @Override
    public void onSuccessResultMeal(List<Meal> meals) {
        homeView.setDailyInspirationData(meals);
    }

    @Override
    public void onSuccessFilter(MealResponse meals) {
        homeView.onSuccessToFilter(meals);
    }

    @Override
    public void onSuccessResultCategory(List<Category> categories) {
        homeView.setListToCategoriesAdapter(categories);
    }

    @Override
    public void onSuccessResultIngredient(List<Ingredient> ingredients) {

    }

    @Override
    public void onSuccessResultCountries(List<Country> countries) {
        homeView.setListToCountriesAdapter(countries);
    }

    @Override
    public void onFailureResult(String message) {
        homeView.onFailureResult(message);
    }
}
