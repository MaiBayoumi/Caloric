package com.example.caloric.recipe.presenter;


import com.example.caloric.model.Meal;

public interface DetailsPresenterInterface {
    void getMealById(String id);
    void insertMealToFavourite(Meal meal);
    void updateDayOfMeal(String id, String day);


}
