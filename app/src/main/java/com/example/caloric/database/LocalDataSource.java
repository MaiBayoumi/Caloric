package com.example.caloric.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.caloric.model.Meal;

import java.util.List;

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
    public void insertMeal(Meal meal) {
        mealsDao.insertMeal(meal);
    }

    @Override
    public void insertAllFav(List<Meal> meals) {
        mealsDao.insertAllFav(meals);
    }

    @Override
    public void deleteMeal(Meal meal) {
        mealsDao.deleteMeal(meal);
    }

    @Override
    public void deleteAllMeals() {
        mealsDao.deleteAllMeals();
    }

    @Override
    public LiveData<List<Meal>> getAllMeals() {
        return mealsDao.getAllMeals();
    }

    @Override
    public LiveData<List<Meal>> getMealsOfDay(String day) {
        return mealsDao.getMealsOfDay(day);
    }

    @Override
    public void updateDayOfMeal(String id, String day) {
        mealsDao.updateColumnDay(id,day);
    }

    @Override
    public void insertMealToCalendar(Meal meal, String day) {
        // Set the day of the week in the meal object
        meal.setDay(day);

        // Ensure the meal is not marked as a favorite
        meal.setIsFavorite(false);

        // Insert the meal into the database, associating it with the selected day
        mealsDao.insertMeal(meal);
    }

    public Meal getMealById(String id) {
        return mealsDao.getMealById(id); // Assuming MealDao has this method
    }

}


