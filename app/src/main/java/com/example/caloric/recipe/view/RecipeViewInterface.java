package com.example.caloric.recipe.view;



import com.example.caloric.model.Meal;
import com.example.caloric.model.MealResponse;

import java.util.List;

public interface RecipeViewInterface {
    void onGetMealDetails(List<Meal> meals);

    void insertMealToFavourite(Meal meal);
    void onFailToGetMealDetails(String message);

    void onMealLoaded(MealResponse meal);

    void onError(String s);

    void onMealInsertedToFavourite();

    void onMealUpdatedDay();

    void onMealInsertedToCalendar();


}