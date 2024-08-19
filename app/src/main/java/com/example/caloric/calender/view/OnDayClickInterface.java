package com.example.caloric.calender.view;


import com.example.caloric.model.Meal;

public interface OnDayClickInterface {
    void onDeleteBtnClicked(Meal meal);

    void onFavItemClicked(String id);
}
