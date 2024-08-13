package com.example.caloric.network;

import com.example.caloric.model.Meals;
import com.example.caloric.model.RootSingleMeal;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {
    // for recomendations
    String BASE_URL_DAILY_INSPIRATIONS = "https://www.themealdb.com/api/json/v1/1/";

    @GET("random.php")
    Observable<Meals> getRootRandom();

    @GET("search.php")
    Observable<RootSingleMeal> getRootSingleMeal(@Query("s") String mealName);


}
