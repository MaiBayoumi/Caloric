package com.example.caloric.network;

import com.example.caloric.model.Meal;
import com.example.caloric.model.MealResponse;

import java.util.List;

public interface NetworkDelegate {
    public void onSuccessResultMeal(List<Meal> meals);
    public void onSuccessFilter(MealResponse meals);
    public void onFailureResult(String message);
}
