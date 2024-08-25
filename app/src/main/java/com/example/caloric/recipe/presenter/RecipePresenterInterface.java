package com.example.caloric.recipe.presenter;


import com.example.caloric.Planner.model.PlannerModel;
import com.example.caloric.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface RecipePresenterInterface {
    Completable getMealById(String id);
    Completable insertMealToFavourite(Meal meal);
    Completable updateDayOfMeal(String id, String day);
    Completable insertMealToCalendar(PlannerModel meal, String day);

    Flowable<List<Meal>> getAllPlannedMeal();
    Completable insertPLannedMeal(PlannerModel meal);
    Completable deletePlannedMeal(PlannerModel meal);




}