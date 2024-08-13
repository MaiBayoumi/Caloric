package com.example.caloric.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.caloric.model.MealsItem;

@Database(entities = MealsItem.class, exportSchema = false, version = 1)
public abstract class LocalDataSource extends RoomDatabase {

    private static LocalDataSource instance = null;

    public abstract DAO mealsDAO();

    public static synchronized LocalDataSource getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), LocalDataSource.class, "Meal").build();
        }
        return instance;
    }

}