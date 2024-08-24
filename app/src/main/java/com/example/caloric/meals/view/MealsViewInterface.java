package com.example.caloric.meals.view;

import com.example.caloric.model.Meal;

import java.util.ArrayList;
import java.util.List;

public interface MealsViewInterface {
    void onMealInserted();

    void onError(String s);

    void onGetMealsByCountry(List<Meal> meals);

    void onGetMealsByCategory(List<Meal> categoryMeals);
}