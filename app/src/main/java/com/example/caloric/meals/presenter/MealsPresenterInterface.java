package com.example.caloric.meals.presenter;


import com.example.caloric.model.Meal;

public interface MealsPresenterInterface {
    void insertMealToFavourite(Meal meal);
    void getCountryMeals(String country);

    void getCategoryMeals(String category);
}