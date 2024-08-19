package com.example.caloric.favourites.view;


import com.example.caloric.model.Meal;

public interface OnClickFavouriteInterface {
    void onDeleteBtnClicked(Meal meal);

    void onFavItemClicked(String id);
}
