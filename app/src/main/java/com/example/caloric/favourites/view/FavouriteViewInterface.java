package com.example.caloric.favourites.view;



import com.example.caloric.model.Meal;
import com.example.caloric.model.MealResponse;

import java.util.List;

public interface FavouriteViewInterface {

    void onGetFavouriteMeals(List<Meal> favouriteMeals);

    void deleteMealFromFavourite(Meal meal);

    void onError(String message);


    void onDeleteFromFav();

    void onGetMealDetails(String id);
}