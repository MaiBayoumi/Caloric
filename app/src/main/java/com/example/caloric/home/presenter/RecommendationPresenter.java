package com.example.caloric.home.presenter;


import com.example.caloric.home.view.RecommendationViewInterface;
import com.example.caloric.model.Category;
import com.example.caloric.model.Country;
import com.example.caloric.model.Ingredient;
import com.example.caloric.model.Meal;
import com.example.caloric.model.MealResponse;
import com.example.caloric.model.RepoInterface;

import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class RecommendationPresenter implements RecommendationPresenterInterface {
    private final RepoInterface repo;
    private final RecommendationViewInterface view;

    public RecommendationPresenter(RepoInterface repo, RecommendationViewInterface view) {
        this.repo = repo;
        this.view = view;
    }

    @Override
    public void getRandomMeal() {
        repo.getRandomMeal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> view.setDailyInspirationData(meals.getMeals()),
                        throwable -> view.onFailureResult(throwable.getMessage())
                );
    }

    @Override
    public void getAllCategories() {
        repo.getAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categories -> view.setListToCategoriesAdapter(categories.getCategories()),
                        throwable -> view.onFailureResult(throwable.getMessage())
                );
    }

    @Override
    public void getAllCountries() {
        repo.getAllCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        countries -> view.setListToCountriesAdapter(countries.getCountries()),
                        throwable -> view.onFailureResult(throwable.getMessage())
                );
    }

    @Override
    public void getMealsByCategory(String category) {
        repo.getMealsByCategory(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> view.onGetMeals(meals.getMeals()),
                        throwable -> view.onFailureResult(throwable.getMessage())
                );
    }

    @Override
    public void getMealsByCountry(String country) {
        repo.getMealsByCountry(country)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> view.onGetMeals(meals.getMeals()),
                        throwable -> view.onFailureResult(throwable.getMessage())
                );
    }

    @Override
    public void getRandomMeals() {
        repo.getRandomMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> view.onGetMeals(meals.getMeals()),
                        throwable -> view.onFailureResult(throwable.getMessage())
                );
    }

    @Override
    public void insertMeal(Meal meal) {
        repo.insertMealToFavourite(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> view.onInsertMealSuccess(),
                        throwable -> view.onFailureResult(throwable.getMessage())
                );
    }

    @Override
    public void insertAllFav(List<Meal> meals) {
        repo.insertAllFav(meals)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> view.onInsertAllFavSuccess(),
                        throwable -> view.onFailureResult(throwable.getMessage())
                );
    }

    @Override
    public void deleteMeal(Meal meal) {
        repo.deleteMealFromFavourite(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> view.onDeleteMealSuccess(),
                        throwable -> view.onFailureResult(throwable.getMessage())
                );
    }

//    @Override
//    public void deleteAllFavMeals() {
//        repo.deleteAllFavouriteMeals()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        () -> view.onDeleteAllFavMealsSuccess(),
//                        throwable -> view.onFailureResult(throwable.getMessage())
//                );
//    }
}