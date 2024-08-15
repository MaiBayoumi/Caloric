package com.example.caloric.network;

import com.example.caloric.model.Category;
import com.example.caloric.model.Country;
import com.example.caloric.model.Ingredient;
import com.example.caloric.model.Meal;
import com.example.caloric.model.MealResponse;

import java.util.List;

public interface NetworkDelegate {
    public void onSuccessResultMeal(List<Meal> meals);
    public void onSuccessFilter(MealResponse meals);
    public void onSuccessResultCategory(List<Category> categories);
    public void onSuccessResultIngredient(List<Ingredient> ingredients);
    public void onSuccessResultCountries(List<Country> countries);
    public void onFailureResult(String message);
}
