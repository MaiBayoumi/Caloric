package com.example.caloric.database;

import androidx.lifecycle.LiveData;

import com.example.caloric.Planner.model.PlannerModel;
import com.example.caloric.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

public interface LocalSource {
    Completable insertMeal(Meal meal);

    Completable insertAllFav(List<Meal> meals);

    Completable deleteMeal(Meal meal);

    Completable deleteAllMeals() ;

    Flowable<List<Meal>> getAllMeals();
    Flowable<List<PlannerModel>> getMealsOfDay(String day);
    Completable updateDayOfMeal(String id, String day);

    Completable insertMealToCalendar(PlannerModel meal, String day);

    Meal getMealById(String id);

    //Completable saveMealToPlan(PlannerModel plannerModel);

    Flowable<List<PlannerModel>> getAllPlannedMeal();
    Completable insertPLannedMeal(PlannerModel meal);
    Completable deletePlannedMeal(PlannerModel meal);
}