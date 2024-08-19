package com.example.caloric.favourites.view;



import com.example.caloric.model.Meal;

import java.util.List;

public interface FavouriteViewInterface {

    void onGetFavouriteMeals(List<Meal> favouriteMeals);

    void deleteMealFromFavourite(Meal meal);
}
