package com.example.caloric.network;

import android.content.Context;
import android.util.Log;

import com.example.caloric.model.Category;
import com.example.caloric.model.CategoryResponse;
import com.example.caloric.model.Country;
import com.example.caloric.model.CountryResponse;
import com.example.caloric.model.Ingredient;
import com.example.caloric.model.IngredientResponse;
import com.example.caloric.model.Meal;
import com.example.caloric.model.MealResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteDataSource implements RemoteSource {
    private static final String TAG = "TAG";
    private static RemoteDataSource instance = null;
    private MealApiInterface mealApiInterface;
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";

    public RemoteDataSource(Context context) {
        File cacheDirectory = new File(context.getCacheDir(), "offline_cache_directory");
        Cache cache = new Cache(cacheDirectory, 80 * 1024 * 1024);
//You are setting up an OkHttpClient with caching by creating a cache directory (offline_cache_directory) and allocating 80 MB of space for it.
// This can help improve performance and allow for offline capabilities when using Retrofit.
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder().cache(cache).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        mealApiInterface = retrofit.create(MealApiInterface.class);
    }

    public static synchronized RemoteDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new RemoteDataSource(context);
        }
        return instance;
    }

    public MealApiInterface getMyApi() {
        return mealApiInterface;
    }

    @Override
    public Observable<MealResponse> getMealByName(String name) {
        return mealApiInterface.getMealByName(name);
    }

    @Override
    public Observable<MealResponse> getMealByFirstChar(String firstChar) {
        return mealApiInterface.getMealByFirstChar(firstChar);
    }

    @Override
    public Observable<MealResponse> getMealById(String id) {
        return mealApiInterface.getMealById(id);
    }

    @Override
    public Observable<MealResponse> getRandomMeal() {
        return mealApiInterface.getRandomMeal();

    }

    @Override
    public Observable<CategoryResponse> getAllCategories() {
        return mealApiInterface.getAllCategories();
    }

    @Override
    public Observable<CountryResponse> getAllCountries() {
        return mealApiInterface.getAllCountries();
    }

    @Override
    public Observable<IngredientResponse> getAllIngredients() {
        return mealApiInterface.getAllIngredient();
    }

    @Override
    public Observable<MealResponse> getMealsByIngredient(String ingredient) {
        return mealApiInterface.getMealsByIngredient(ingredient);
    }

    @Override
    public Observable<MealResponse> getMealsByCategory(String category) {
        return mealApiInterface.getMealsByCategory(category);
    }

    @Override
    public Observable<MealResponse> getMealsByCountry(String country) {
        return mealApiInterface.getMealsByCountry(country);
    }

    public Observable<MealResponse> getRandomMeals() {
        return Observable.just(0) // Start with a single item to initiate the chain
                .repeat(5) // Repeat the chain 5 times
                .flatMap(i -> mealApiInterface.getRandomMeal() // Fetch a random meal for each item
                        .onErrorReturn(throwable -> null) // Handle errors by returning null
                )
                .filter(meal -> meal != null) // Filter out any null results
                .toList() // Collect items into a List<Meal>
                .map(meals -> new MealResponse(meals.stream()
                        .flatMap(mealResponse -> mealResponse.getMeals().stream())
                        .collect(Collectors.toList()))) // Wrap the list of all meals in a MealResponse
                .toObservable(); // Convert List<MealResponse> to Observable<MealResponse>
    }

}