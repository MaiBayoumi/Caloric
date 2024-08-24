package com.example.caloric.search.presenter;


import com.example.caloric.model.Meal;

public interface SearchPresenterInterface {
    void getMealsByIngredient(String ingredient);

    void getMealsByCategory(String category);

    void getMealsByCountry(String country);

    void getMealByName(String name);

    void getMealByFirstChar(String firstChar);

    void insertMeal(Meal meal);

    void getRandomMeals();
}
