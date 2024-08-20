package com.example.caloric.meals.view;


import com.example.caloric.model.Meal;

public interface OnCommonClickInterface {
    void onSaveBtnClicked(Meal meal);

    void onMealItemClicked(String id);
}
