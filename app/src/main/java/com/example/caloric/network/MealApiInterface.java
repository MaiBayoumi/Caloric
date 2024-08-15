package com.example.caloric.network;


import com.example.caloric.model.CategoryResponse;
import com.example.caloric.model.CountryResponse;
import com.example.caloric.model.IngredientResponse;
import com.example.caloric.model.MealResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealApiInterface {
    @GET("search.php")
    Call<MealResponse> getMealByName(@Query("s")String name);

    @GET("search.php")
    Call<MealResponse> getMealByFirstChar(@Query("f") String firstChar);

    @GET("lookup.php")
    Call<MealResponse> getMealById(@Query("i") String id);

    @GET("random.php")
    Call<MealResponse> getRandomMeal();

    @GET("categories.php")
    Call<CategoryResponse> getAllCategories();

    @GET("list.php?a=list")
    Call<CountryResponse> getAllCountries();

    @GET("list.php?i=list")
    Call<IngredientResponse> getAllIngredient();

    @GET("filter.php")//this return list of strMeal and strMealThumb and idMeal just
    Call<MealResponse> getMealsByIngredient(@Query("i") String ingredient);

    @GET("filter.php")//this return list of strMeal and strMealThumb and idMeal just
    Call<MealResponse> getMealsByCategory(@Query("c") String category);

    @GET("filter.php")//this return list of strMeal and strMealThumb and idMeal just
    Call<MealResponse> getMealsByCountry(@Query("a") String country);



    //this to put your ingredient name instead of Cajun to get thumb of ingredient
    //https://www.themealdb.com/images/ingredients/Cajun.png

}
