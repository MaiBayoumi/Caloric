package com.example.caloric.Planner.presenter;
import com.example.caloric.model.Meal;
import com.example.caloric.Planner.model.PlannerModel;
import com.example.caloric.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface PlannerPresenterInterface {
    void getMealsForDay(String day, String userId);
    //void deleteMeal(Meal meal);

    //void updateDayOfMeal(String id, String day);
    //void saveMealToPlan(Meal meal, String day);

    void getAllPlannedMeal();

    void insertPlannedMeal(PlannerModel meal);

    void deletePlannedMeal(PlannerModel meal);
}



