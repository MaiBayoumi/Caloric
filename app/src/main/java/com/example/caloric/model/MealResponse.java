package com.example.caloric.model;

import java.io.Serializable;
import java.util.List;

public class MealResponse implements Serializable {

    private List<Meal> meals;

    public MealResponse(List<Meal> meals) {
        this.meals = meals;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}
