package com.example.caloric.recipe.presenter;


import com.example.caloric.Planner.model.PlannerModel;
import com.example.caloric.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface RecipePresenterInterface {
    void getMealById(String id);
    void insertMealToFavourite(Meal meal);
    void insertMealToCalendar(PlannerModel meal, String day);
    void deletePlannedMeal(PlannerModel meal);




}