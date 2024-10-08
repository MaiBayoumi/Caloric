package com.example.caloric.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.caloric.Planner.model.PlannerModel;
import com.example.caloric.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

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
    public Flowable<List<Meal>> getAllMeals(String userId) {
        return mealsDao.getAllMeals(userId);
    }

    @Override
    public Flowable<List<PlannerModel>> getMealsOfDay(String day,String userId) {
        return mealsDao.getMealsOfDay(day,userId);
    }



    @Override
    public Completable insertMealToCalendar(PlannerModel meal, String day) {
        // Set the day of the week in the meal object
        meal.setDayOfMeal(day);

        // Insert the meal into the database, associating it with the selected day
        return mealsDao.insertPLannedMeal(meal);
    }

    public Meal getMealById(String id) {
        return mealsDao.getMealById(id); // Assuming MealDao has this method
    }



    @Override
    public Completable deletePlannedMeal(PlannerModel meal) {
        return mealsDao.deletePlannedMeal(meal);
    }




}