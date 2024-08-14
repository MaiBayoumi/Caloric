package com.example.caloric.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.caloric.model.Meal;

import java.util.List;

@Dao
public interface MealsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertMeal(Meal meal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllFav(List<Meal> meals);

    @Delete
    public void deleteMeal(Meal meal);

    @Query("DELETE FROM meals_table")
    public void deleteAllMeals() ;

    @Query("SELECT * FROM meals_table")
    LiveData<List<Meal>> getAllMeals();

    @Query("SELECT * From meals_table WHERE day = :day")
    LiveData<List<Meal>> getMealsOfDay(String day);

    @Query("UPDATE meals_table SET day = :day WHERE idMeal = :id")
    void updateColumnDay(String id, String day);
}
