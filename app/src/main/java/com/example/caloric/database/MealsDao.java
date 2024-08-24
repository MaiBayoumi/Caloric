package com.example.caloric.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.caloric.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface MealsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMeal(Meal meal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAllFav(List<Meal> meals);

    @Delete
    Completable deleteMeal(Meal meal);

    @Query("DELETE FROM meals_table")
    Completable deleteAllMeals() ;

    @Query("SELECT * FROM meals_table")
    Flowable<List<Meal>> getAllMeals();

    @Query("SELECT * From meals_table WHERE day = :day")
    Flowable<List<Meal>> getMealsOfDay(String day);

    @Query("UPDATE meals_table SET day = :day WHERE idMeal = :id")
    Completable updateColumnDay(String id, String day);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMealToCalendar(Meal meal);

    @Query("SELECT * FROM meals_table WHERE idMeal = :id LIMIT 1")
    Meal getMealById(String id);
}