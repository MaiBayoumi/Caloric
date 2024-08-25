package com.example.caloric.recipe.presenter;

import com.example.caloric.Planner.model.PlannerModel;
import com.example.caloric.model.Meal;
import com.example.caloric.model.RepoInterface;
import com.example.caloric.recipe.view.RecipeViewInterface;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RecipePresenter implements RecipePresenterInterface {
    private RepoInterface repo;
    private RecipeViewInterface detailsView;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public RecipePresenter(RepoInterface repo, RecipeViewInterface detailsView){
        this.repo = repo;
        this.detailsView = detailsView;
    }

    @Override
    public Completable getMealById(String id) {
        Disposable disposable = repo.getMealById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meal -> detailsView.onGetMealDetails(meal.getMeals()),
                        throwable -> detailsView.onError(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
        return null;
    }



    @Override
    public Completable  insertMealToFavourite(Meal meal) {
        Disposable disposable = repo.insertMealToFavourite(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> detailsView.onMealInsertedToFavourite(),
                        throwable -> detailsView.onError(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
        return null;
    }

    @Override
    public Completable  updateDayOfMeal(String id, String day) {
        Disposable disposable = repo.updateDayOfMeal(id, day)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> detailsView.onMealUpdatedDay(),
                        throwable -> detailsView.onError(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
        return null;
    }

    @Override
    public Completable  insertMealToCalendar(PlannerModel meal, String day) {
        Disposable disposable = repo.insertMealToCalendar(meal, day)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> detailsView.onMealInsertedToCalendar(),
                        throwable -> detailsView.onError(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
        return null;
    }

    @Override
    public Flowable<List<Meal>> getAllPlannedMeal() {
        Disposable disposable = repo.getAllPlannedMeal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (meals) -> detailsView.onMealInsertedToCalendar(),
                        throwable -> detailsView.onError(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
        return null;
    }

    @Override
    public Completable insertPLannedMeal(PlannerModel meal) {
        Disposable disposable = repo.insertPLannedMeal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> detailsView.onMealInsertedToCalendar(),
                        throwable -> detailsView.onError(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
        return null;
    }

    @Override
    public Completable deletePlannedMeal(PlannerModel meal) {
        Disposable disposable = repo.deletePlannedMeal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> detailsView.onMealInsertedToCalendar(),
                        throwable -> detailsView.onError(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
        return null;
    }

    public void clearDisposables() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }
}
