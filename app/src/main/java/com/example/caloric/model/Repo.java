package com.example.caloric.model;

import com.example.caloric.Planner.model.PlannerModel;
import com.example.caloric.database.LocalSource;
import com.example.caloric.network.RemoteDataSource;
import com.example.caloric.network.RemoteSource;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class Repo implements RepoInterface {
    private static Repo instance;
    private final RemoteSource remoteSource;
    private final LocalSource localSource;

    private Repo(RemoteSource remoteSource, LocalSource localSource) {
        this.remoteSource = remoteSource;
        this.localSource = localSource;
    }

    public static synchronized Repo getInstance(RemoteSource remoteSource, LocalSource localSource) {
        if (instance == null) {
            instance = new Repo(remoteSource, localSource);
        }
        return instance;
    }

    @Override
    public Completable insertMealToFavourite(Meal meal) {
        return  localSource.insertMeal(meal);
    }

    @Override
    public Completable insertAllFav(List<Meal> meals) {
        return localSource.insertAllFav(meals);

    }

    @Override
    public Completable deleteMealFromFavourite(Meal meal) {
        return  localSource.deleteMeal(meal);
    }

    @Override
    public Completable deleteAllFavouriteMeals() {
        return localSource.deleteAllMeals();

    }

    @Override
    public Flowable<List<Meal>> getAllFavouriteMeals(String userId) {
        return localSource.getAllMeals(userId);
    }

    @Override
    public Flowable<List<PlannerModel>> getMealsOfDay(String day,String userId) {
        return localSource.getMealsOfDay(day,userId);
    }



    @Override
    public Completable insertMealToCalendar(PlannerModel meal, String day) {
        return localSource.insertMealToCalendar(meal, day);

    }


    @Override
    public Completable deletePlannedMeal(PlannerModel meal) {
        return localSource.deletePlannedMeal(meal);
    }


    @Override
    public Observable<MealResponse> getMealByName(String name) {
        return remoteSource.getMealByName(name);
    }

    @Override
    public Observable<MealResponse> getMealByFirstChar(String firstChar) {
        return remoteSource.getMealByFirstChar(firstChar);
    }

    @Override
    public Observable<MealResponse> getMealById(String id) {
        return remoteSource.getMealById(id);
    }


    @Override
    public Observable<MealResponse> getRandomMeal() {
        return remoteSource.getRandomMeal();
    }

    @Override
    public Observable<CategoryResponse> getAllCategories() {
        return remoteSource.getAllCategories();
    }

    @Override
    public Observable<CountryResponse> getAllCountries() {
        return remoteSource.getAllCountries();
    }

    @Override
    public Observable<IngredientResponse> getAllIngredients() {
        return remoteSource.getAllIngredients();
    }

    @Override
    public Observable<MealResponse> getMealsByIngredient(String ingredient) {
        return remoteSource.getMealsByIngredient(ingredient);
    }

    @Override
    public Observable<MealResponse> getMealsByCategory(String category) {
        return remoteSource.getMealsByCategory(category);
    }

    @Override
    public Observable<MealResponse> getMealsByCountry(String country) {
        return remoteSource.getMealsByCountry(country);
    }

    @Override
    public Observable<MealResponse> getRandomMeals() {
        return remoteSource.getRandomMeals();
    }



}