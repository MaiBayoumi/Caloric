package com.example.caloric.Planner.view;


import com.example.caloric.model.Meal;

public interface OnDayClickInterface {
    void onDeleteBtnClicked(Meal meal);

    void onFavItemClicked(String id);
}
