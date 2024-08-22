package com.example.caloric.calender.presenter;


import com.example.caloric.model.Meal;

public interface PlannerPresenterInterface {
    void getMealsForDay(String day);
    void deleteMeal(Meal meal);

    void updateDayOfMeal(String id, String day);

}
