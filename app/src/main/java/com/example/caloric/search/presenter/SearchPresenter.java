package com.example.caloric.search.presenter;

import com.example.caloric.model.Category;
import com.example.caloric.model.Country;
import com.example.caloric.model.Ingredient;
import com.example.caloric.model.Meal;
import com.example.caloric.model.MealResponse;
import com.example.caloric.model.RepoInterface;
import com.example.caloric.search.view.SearchViewInterface;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class SearchPresenter implements SearchPresenterInterface {
    private final RepoInterface repo;
    private final SearchViewInterface searchView;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public SearchPresenter(RepoInterface repo, SearchViewInterface searchView) {
        this.repo = repo;
        this.searchView = searchView;
    }

    @Override
    public void getMealsByIngredient(String ingredient) {
        Disposable disposable = repo.getMealsByIngredient(ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> searchView.onGetMeals(meals.getMeals()),
                        throwable -> searchView.onFailureResult(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
    }

    @Override
    public void getMealsByCategory(String category) {
        Disposable disposable = repo.getMealsByCategory(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> searchView.onGetMeals(meals.getMeals()),
                        throwable -> searchView.onFailureResult(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
    }

    @Override
    public void getMealsByCountry(String country) {
        Disposable disposable = repo.getMealsByCountry(country)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> searchView.onGetMeals(meals.getMeals()),
                        throwable -> searchView.onFailureResult(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
    }

    @Override
    public void getMealByName(String name) {
        Disposable disposable = repo.getMealByName(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meal -> searchView.onGetMeals(meal.getMeals()),  // Assuming a single meal wrapped in a list
                        throwable -> searchView.onFailureResult(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
    }

    @Override
    public void getMealByFirstChar(String firstChar) {
        Disposable disposable = repo.getMealByFirstChar(firstChar)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> searchView.onGetMeals(meals.getMeals()),
                        throwable -> searchView.onFailureResult(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
    }

    @Override
    public void insertMeal(Meal meal) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            meal.setUserId(FirebaseAuth.getInstance().getUid());
            Disposable disposable = repo.insertMealToFavourite(meal)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> searchView.onInsertMealSuccess(),
                            throwable -> searchView.onFailureResult(throwable.getMessage())
                    );
            compositeDisposable.add(disposable);
        }
    }

    public void getRandomMeals() {
        Disposable disposable = repo.getRandomMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> searchView.onGetMeals(meals.getMeals()),
                        throwable -> searchView.onFailureResult(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
    }

    public void clearDisposables() {
        compositeDisposable.clear();
    }
}