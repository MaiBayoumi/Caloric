package com.example.caloric.Planner.view;

import com.example.caloric.Planner.model.PlannerModel;
import com.example.caloric.model.Meal;

public interface OnDayClickInterface {
    void onDeleteBtnClicked(PlannerModel meal);

    void onFavItemClicked(String id);
}



