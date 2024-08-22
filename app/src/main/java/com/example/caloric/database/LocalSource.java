package com.example.caloric.database;

import androidx.lifecycle.LiveData;

import com.example.caloric.model.Meal;

import java.util.List;

public interface LocalSource {
    void insertMeal(Meal meal);

    public void insertAllFav(List<Meal> meals);

    void deleteMeal(Meal meal);

    void deleteAllMeals() ;

    LiveData<List<Meal>> getAllMeals();
    LiveData<List<Meal>> getMealsOfDay(String day);
    void updateDayOfMeal(String id, String day);

    void insertMealToCalendar(Meal meal, String day);

    Meal getMealById(String id);
}
