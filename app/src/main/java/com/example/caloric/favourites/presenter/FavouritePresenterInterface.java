package com.example.caloric.favourites.presenter;


import com.example.caloric.model.Meal;

public interface FavouritePresenterInterface {
    void getAllMeals();

    void deleteMeal(Meal meal);
}
