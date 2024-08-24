package com.example.caloric.model;

import com.example.caloric.network.RemoteSource;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

public interface RepoInterface extends RemoteSource {


    // Database operations
    Completable insertMealToFavourite(Meal meal);

    Completable insertAllFav(List<Meal> meals);

    Completable deleteMealFromFavourite(Meal meal);

    Completable deleteAllFavouriteMeals();

    Flowable<List<Meal>> getAllFavouriteMeals();

    Flowable<List<Meal>> getMealsOfDay(String day);

    Completable updateDayOfMeal(String id, String day);

    Completable insertMealToCalendar(Meal meal, String day);


}