package com.example.caloric.recipe.view;



import com.example.caloric.model.Meal;

import java.util.List;

public interface DetailsViewInterface {
    void onGetMealDetails(List<Meal> meals);

    void insertMealToFavourite(Meal meal);
    void onFailToGetMealDetails(String message);
}
