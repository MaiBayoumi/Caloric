package com.example.caloric.home.view;

import com.example.caloric.model.Category;
import com.example.caloric.model.Country;
import com.example.caloric.model.Meal;
import com.example.caloric.model.MealResponse;

import java.util.List;

public interface HomeViewInterface {
    void setDailyInspirationData(List<Meal> meals);

    void onSuccessToFilter(MealResponse meals);
    void setListToCategoriesAdapter(List<Category> categories);
    void setListToCountriesAdapter(List<Country> countries);
    void onFailureResult(String message);
}
