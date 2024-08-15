package com.example.caloric.network;

import android.content.Context;
import android.util.Log;

import com.example.caloric.model.CategoryResponse;
import com.example.caloric.model.CountryResponse;
import com.example.caloric.model.IngredientResponse;
import com.example.caloric.model.MealResponse;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientService implements RemoteSource {
    private static final String TAG = "TAG";
    private static ClientService instance = null;
    private MealApiInterface mealApiInterface;
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";

    private ClientService(Context context) {
        File cacheDirectory = new File(context.getCacheDir(), "offline_cache_directory");
        Cache cache = new Cache(cacheDirectory, 80 * 1024 * 1024);

        OkHttpClient okHttpClient = new OkHttpClient
                .Builder().cache(cache).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mealApiInterface = retrofit.create(MealApiInterface.class);
    }

    public static synchronized ClientService getInstance(Context context) {
        if (instance == null) {
            instance = new ClientService(context);
        }
        return instance;
    }

    @Override
    public void getMealByName(String name, NetworkDelegate networkDelegate) {
        mealApiInterface.getMealByName(name).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.body().getMeals() != null) {
                    Log.d(TAG, "onResponse: done to get Meal by Name and size equal " + response.body().getMeals().size());
                    networkDelegate.onSuccessResultMeal(response.body().getMeals());
                } else {
                    networkDelegate.onFailureResult("no Result..");
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: failed to get Meal By Name");
                networkDelegate.onFailureResult(t.getMessage());
            }
        });
    }

    @Override
    public void getMealByFirstChar(String firstChar, NetworkDelegate networkDelegate) {
        mealApiInterface.getMealByFirstChar(firstChar).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                Log.d(TAG, "onResponse: done to get Meal by FirstChar and size equal " + response.body().getMeals().size());
                networkDelegate.onSuccessResultMeal(response.body().getMeals());
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: failed to get Meal By First Char");
                networkDelegate.onFailureResult(t.getMessage());
            }
        });
    }

    @Override
    public void getMealById(String id, NetworkDelegate networkDelegate) {
        mealApiInterface.getMealById(id).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                Log.d(TAG, "onResponse: done to get Meal by Id and size equal " + response.body().getMeals().size());
                networkDelegate.onSuccessResultMeal(response.body().getMeals());
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: failed to get Meal By Id");
                networkDelegate.onFailureResult(t.getMessage());
            }
        });
    }


    @Override
    public void getRandomMeal(NetworkDelegate networkDelegate) {
        mealApiInterface.getRandomMeal().enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                Log.d(TAG, "onResponse: done get RandomMeal and the name is : " + response.body().getMeals().get(0).getStrMeal());
                networkDelegate.onSuccessResultMeal(response.body().getMeals());
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: failed to get Random Meal");
                networkDelegate.onFailureResult(t.getMessage());
            }
        });
    }
    @Override
    public void getAllCategories(NetworkDelegate networkDelegate) {
        mealApiInterface.getAllCategories().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                Log.d(TAG, "onResponse: done to get All categories and size equal " + response.body().getCategories().size());
                networkDelegate.onSuccessResultCategory(response.body().getCategories());
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: failed to get All categories");
                networkDelegate.onFailureResult(t.getMessage());
            }
        });
    }

    @Override
    public void getAllCountries(NetworkDelegate networkDelegate) {
        mealApiInterface.getAllCountries().enqueue(new Callback<CountryResponse>() {
            @Override
            public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {
                Log.d(TAG, "onResponse: done to get All Countries and size equal " + response.body().getCountries().size());
                networkDelegate.onSuccessResultCountries(response.body().getCountries());
            }

            @Override
            public void onFailure(Call<CountryResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: failed to get All Countries");
                networkDelegate.onFailureResult(t.getMessage());
            }
        });
    }

    @Override
    public void getAllIngredient(NetworkDelegate networkDelegate) {
        mealApiInterface.getAllIngredient().enqueue(new Callback<IngredientResponse>() {
            @Override
            public void onResponse(Call<IngredientResponse> call, Response<IngredientResponse> response) {
                Log.d(TAG, "onResponse: done to get All Ingredient and size equal " + response.body().getIngredient().size());
                networkDelegate.onSuccessResultIngredient(response.body().getIngredient());
            }

            @Override
            public void onFailure(Call<IngredientResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: failed to get All Ingredient");
                networkDelegate.onFailureResult(t.getMessage());
            }
        });
    }

    @Override
    public void getMealsByIngredient(String ingredient, NetworkDelegate networkDelegate) {
        mealApiInterface.getMealsByIngredient(ingredient).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.body().getMeals() != null) {
                    Log.d(TAG, "onResponse: done to get Meals by Ingredient and size equal " + response.body().getMeals().size());
                    networkDelegate.onSuccessFilter(response.body());
                } else {
                    networkDelegate.onFailureResult("no Result..");
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: failed to get MealBy Ingredient");
                networkDelegate.onFailureResult(t.getMessage());
            }
        });
    }

    @Override
    public void getMealsByCategory(String category, NetworkDelegate networkDelegate) {
        mealApiInterface.getMealsByCategory(category).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.body().getMeals() != null) {
                    Log.d(TAG, "onResponse: done to get meals by Category and size equal " + response.body().getMeals().size());
                    networkDelegate.onSuccessFilter(response.body());
                } else {
                    networkDelegate.onFailureResult("no Result..");
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: failed to get Meals By Category");
                networkDelegate.onFailureResult(t.getMessage());
            }
        });
    }

    @Override
    public void getMealsByCountry(String country, NetworkDelegate networkDelegate) {
        mealApiInterface.getMealsByCountry(country).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.body().getMeals() != null) {
                    Log.d(TAG, "onResponse: done to get Meals By Countries and size equal " + response.body().getMeals().size());
                    networkDelegate.onSuccessFilter(response.body());
                } else {
                    networkDelegate.onFailureResult("no Result..");
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: failed to get Meals By Country");
                networkDelegate.onFailureResult(t.getMessage());
            }
        });
    }
}
