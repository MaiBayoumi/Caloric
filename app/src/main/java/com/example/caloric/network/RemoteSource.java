package com.example.caloric.network;

public interface RemoteSource {
    void getMealByName(String name, NetworkDelegate networkDelegate);

    void getMealByFirstChar(String firstChar, NetworkDelegate networkDelegate);

    void getMealById(String id, NetworkDelegate networkDelegate);

    void getRandomMeal(NetworkDelegate networkDelegate);

    void getAllCategories(NetworkDelegate networkDelegate);

    void getAllCountries(NetworkDelegate networkDelegate);

    void getAllIngredient(NetworkDelegate networkDelegate);

    void getMealsByIngredient(String ingredient, NetworkDelegate networkDelegate);

    void getMealsByCategory(String category, NetworkDelegate networkDelegate);

    void getMealsByCountry(String country, NetworkDelegate networkDelegate);

    void getRandomMeals(NetworkDelegate networkDelegate);
}
