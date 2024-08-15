package com.example.caloric.home.presenter;

import com.example.caloric.model.Meal;

import java.util.List;

public interface CallBackMeal {
    void onSuccess(List<Meal> meals);
    void onFailure(String errorMessage);
}
