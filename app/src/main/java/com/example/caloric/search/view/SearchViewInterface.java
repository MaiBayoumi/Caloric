package com.example.caloric.search.view;
import com.example.caloric.model.Category;
import com.example.caloric.model.Country;
import com.example.caloric.model.Ingredient;
import com.example.caloric.model.Meal;

import java.util.List;
public interface SearchViewInterface {
    void onGetMeals(List<Meal> meals);
    void onGetAllCategories(List<Category> categories);

    void onGetAllIngredient(List<Ingredient> ingredients);

    void onGetAllCountries(List<Country> countries);

    void onFailureResult(String message);

    void onInsertMealSuccess();
}