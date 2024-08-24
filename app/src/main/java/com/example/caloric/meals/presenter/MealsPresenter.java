package com.example.caloric.meals.presenter;


import android.util.Log;

import com.example.caloric.meals.view.MealsViewInterface;
import com.example.caloric.model.Category;
import com.example.caloric.model.Country;
import com.example.caloric.model.Ingredient;
import com.example.caloric.model.Meal;
import com.example.caloric.model.MealResponse;
import com.example.caloric.model.RepoInterface;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealsPresenter implements MealsPresenterInterface {
    private RepoInterface repo;
    private MealsViewInterface mealsViewInterface;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MealsPresenter(RepoInterface repo, MealsViewInterface commonMealView) {
        this.repo = repo;
        this.mealsViewInterface = commonMealView;
    }

    @Override
    public void insertMealToFavourite(Meal meal) {
        Disposable disposable = repo.insertMealToFavourite(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> mealsViewInterface.onMealInserted(),
                        throwable -> mealsViewInterface.onError(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
    }

    @Override
    public void getCountryMeals(String country) {
        Disposable disposable = repo.getMealsByCountry(country)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> {
                            mealsViewInterface.onGetMealsByCountry(meals.getMeals());
                        },
                        throwable -> {
                            mealsViewInterface.onError(throwable.getMessage());
                        }
                );
        compositeDisposable.add(disposable);
    }


    @Override
    public void getCategoryMeals(String category) {
        Disposable disposable =  repo.getMealsByCategory(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> {
                            mealsViewInterface.onGetMealsByCategory(meals.getMeals());
                        },
                        throwable -> mealsViewInterface.onError(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
    }

}