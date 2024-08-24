package com.example.caloric.network;



import com.example.caloric.model.CategoryResponse;
import com.example.caloric.model.CountryResponse;
import com.example.caloric.model.IngredientResponse;
import com.example.caloric.model.Meal;
import com.example.caloric.model.MealResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealApiInterface {
    @GET("search.php")
    Observable<MealResponse> getMealByName(@Query("s")String name);

    @GET("search.php")
    Observable<MealResponse> getMealByFirstChar(@Query("f") String firstChar);

    @GET("lookup.php")
    Observable<MealResponse> getMealById(@Query("i") String id);

    @GET("random.php")
    Observable<MealResponse> getRandomMeal();

    @GET("categories.php")
    Observable<CategoryResponse> getAllCategories();

    @GET("list.php?a=list")
    Observable<CountryResponse> getAllCountries();

    @GET("list.php?i=list")
    Observable<IngredientResponse> getAllIngredient();

    @GET("filter.php")//this return list of strMeal and strMealThumb and idMeal just
    Observable<MealResponse> getMealsByIngredient(@Query("i") String ingredient);

    @GET("filter.php")//this return list of strMeal and strMealThumb and idMeal just
    Observable<MealResponse> getMealsByCategory(@Query("c") String category);

    @GET("filter.php")//this return list of strMeal and strMealThumb and idMeal just
    Observable<MealResponse> getMealsByCountry(@Query("a") String country);



    //this to put your ingredient name instead of Cajun to get thumb of ingredient
    //https://www.themealdb.com/images/ingredients/Cajun.png

}