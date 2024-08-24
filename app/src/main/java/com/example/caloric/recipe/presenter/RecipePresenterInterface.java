package com.example.caloric.recipe.presenter;


import com.example.caloric.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

public interface RecipePresenterInterface {
    Completable getMealById(String id);
    Completable insertMealToFavourite(Meal meal);
    Completable updateDayOfMeal(String id, String day);
    Completable insertMealToCalendar(Meal meal, String day);


}