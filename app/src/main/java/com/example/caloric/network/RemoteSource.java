package com.example.caloric.network;

import com.example.caloric.model.Category;
import com.example.caloric.model.CategoryResponse;
import com.example.caloric.model.Country;
import com.example.caloric.model.CountryResponse;
import com.example.caloric.model.Ingredient;
import com.example.caloric.model.IngredientResponse;
import com.example.caloric.model.Meal;
import com.example.caloric.model.MealResponse;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

public interface RemoteSource {

    Observable<MealResponse> getMealByName(String name);

    Observable<MealResponse> getMealByFirstChar(String firstChar);

    Observable<MealResponse> getMealById(String id);

    Observable<MealResponse> getRandomMeal();

    Observable<CategoryResponse> getAllCategories();

    Observable<CountryResponse> getAllCountries();

    Observable<IngredientResponse> getAllIngredients();

    Observable<MealResponse> getMealsByIngredient(String ingredient);

    Observable<MealResponse> getMealsByCategory(String category);

    Observable<MealResponse> getMealsByCountry(String country);

    Observable<MealResponse> getRandomMeals();
}