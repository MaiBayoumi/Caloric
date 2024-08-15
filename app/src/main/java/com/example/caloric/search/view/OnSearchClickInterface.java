package com.example.caloric.search.view;

import com.example.caloric.model.Meal;

public interface OnSearchClickInterface {
    void onSaveBtnClicked(Meal meal);

    void onItemClicked(String id);
}
