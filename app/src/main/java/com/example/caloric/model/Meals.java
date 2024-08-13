package com.example.caloric.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Meals {
    @SerializedName("meals")
    private List<MealsItem> meals;

    public List<MealsItem> getMeals() {
        return meals;
    }
}
