package com.example.caloric.calender.presenter;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.caloric.calender.view.PlannerViewInterface;
import com.example.caloric.model.Meal;
import com.example.caloric.model.RepoInterface;

import java.util.List;

public class PlannerPresenter implements PlannerPresenterInterface {
    private RepoInterface repo;
    private PlannerViewInterface dayViewInterface;


    public PlannerPresenter(RepoInterface repo, PlannerViewInterface dayViewInterface){
        this.repo = repo;
        this.dayViewInterface = dayViewInterface;
    }

    @Override
    public void getMealsForDay(String day) {
        repo.getMealsOfDay(day).observe((LifecycleOwner) dayViewInterface, new Observer<List<Meal>>() {
            @Override
            public void onChanged(List<Meal> meals) {
                dayViewInterface.onGetMealOfDay(meals);
            }
        });
    }

    @Override
    public void deleteMeal(Meal meal) {
        repo.deleteMealFromFavourite(meal);
    }

    @Override
    public void updateDayOfMeal(String id, String day) {
        repo.updateDayOfMeal(id, day);
    }




}
