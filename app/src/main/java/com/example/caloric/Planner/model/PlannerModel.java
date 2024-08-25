package com.example.caloric.Planner.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "plan_table")
public class PlannerModel {

    @PrimaryKey
    @NonNull
    private String idMeal;
    private String strMealThumb;
    private String strMeal;
    private String userId;
    private String dayOfMeal;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getIdMeal() { return idMeal; }
    public void setIdMeal(String value) { this.idMeal = value; }




    public String getStrMealThumb() { return strMealThumb; }
    public void setStrMealThumb(String value) { this.strMealThumb = value; }


    public String getStrMeal() { return strMeal; }
    public void setStrMeal(String value) { this.strMeal = value; }



    public String getDayOfMeal() {
        return dayOfMeal;
    }

    public void setDayOfMeal(String dayOfMeal) {
        this.dayOfMeal = dayOfMeal;
    }
}