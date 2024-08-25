package com.example.caloric.home.presenter;

import com.example.caloric.model.Meal;

import java.util.List;

public interface RecommendationPresenterInterface {

    void getAllCategories();

    void getAllCountries();

    void insertMeal(Meal meal);

    void insertAllFav(List<Meal> meals);

    void getRandomMeals();

}