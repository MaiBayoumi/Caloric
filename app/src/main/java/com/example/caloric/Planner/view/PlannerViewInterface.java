package com.example.caloric.Planner.view;


import com.example.caloric.model.Meal;

import java.util.List;

public interface PlannerViewInterface {
    void onGetMealOfDay(List<Meal> favouriteMeals);

    void onError(String s);

    void onMealDeleted();

    void onMealUpdated();
}