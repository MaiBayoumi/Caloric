package com.example.caloric.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.caloric.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class LocalDataSource implements LocalSource {
    private static LocalDataSource instance = null;
    private MealsDao mealsDao;

    private LocalDataSource(Context context){
        mealsDao = MealsDatabase.getInstance(context).getMealsDao();
    }

    public static synchronized LocalDataSource getInstance(Context context){
        if(instance == null){
            instance = new LocalDataSource(context);
        }
        return instance;
    }


    @Override
    public Completable insertMeal(Meal meal) {
        return mealsDao.insertMeal(meal);
    }

    @Override
    public Completable insertAllFav(List<Meal> meals) {
        return mealsDao.insertAllFav(meals);
    }

    @Override
    public Completable deleteMeal(Meal meal) {
        return mealsDao.deleteMeal(meal);
    }

    @Override
    public Completable deleteAllMeals() {
        return mealsDao.deleteAllMeals();
    }

    @Override
    public Flowable<List<Meal>> getAllMeals() {
        return mealsDao.getAllMeals();
    }

    @Override
    public Flowable<List<Meal>> getMealsOfDay(String day) {
        return mealsDao.getMealsOfDay(day);
    }

    @Override
    public Completable updateDayOfMeal(String id, String day) {
        return mealsDao.updateColumnDay(id,day);
    }

    @Override
    public Completable insertMealToCalendar(Meal meal, String day) {
        // Set the day of the week in the meal object
        meal.setDay(day);

        // Ensure the meal is not marked as a favorite
        meal.setIsFavorite(false);

        // Insert the meal into the database, associating it with the selected day
        return mealsDao.insertMeal(meal);
    }

    public Meal getMealById(String id) {
        return mealsDao.getMealById(id); // Assuming MealDao has this method
    }

}