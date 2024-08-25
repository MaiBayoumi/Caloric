package com.example.caloric.home.view;

import com.example.caloric.model.Category;
import com.example.caloric.model.Country;
import com.example.caloric.model.Ingredient;
import com.example.caloric.model.Meal;
import com.example.caloric.model.MealResponse;

import java.util.List;

public interface RecommendationViewInterface {
    void setDailyInspirationData(List<Meal> meals);
    void setListToCategoriesAdapter(List<Category> categories);
    void setListToCountriesAdapter(List<Country> countries);
    void onGetMeals(List<Meal> meals);
    void onFailureResult(String message);
    void onInsertMealSuccess();
    void onInsertAllFavSuccess();
}