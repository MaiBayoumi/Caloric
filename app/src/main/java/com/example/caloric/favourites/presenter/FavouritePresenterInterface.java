package com.example.caloric.favourites.presenter;


import com.example.caloric.model.Meal;

import io.reactivex.rxjava3.core.Observable;

public interface FavouritePresenterInterface {
    void getAllMeals();

    void deleteMeal(Meal meal);

}