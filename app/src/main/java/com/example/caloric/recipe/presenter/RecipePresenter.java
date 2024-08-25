package com.example.caloric.recipe.presenter;

import android.util.Log;

import com.example.caloric.Planner.model.PlannerModel;
import com.example.caloric.model.Meal;
import com.example.caloric.model.RepoInterface;
import com.example.caloric.recipe.view.RecipeViewInterface;
import com.google.firebase.auth.FirebaseAuth;

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
    public void getMealById(String id) {
        Disposable disposable = repo.getMealById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meal -> detailsView.onGetMealDetails(meal.getMeals()),
                        throwable -> detailsView.onError(throwable.getMessage())
                );
        compositeDisposable.add(disposable);

    }



    @Override
    public void  insertMealToFavourite(Meal meal) {
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            meal.setUserId(FirebaseAuth.getInstance().getUid());
            Disposable disposable = repo.insertMealToFavourite(meal)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> detailsView.onMealInsertedToFavourite(),
                            throwable -> detailsView.onError(throwable.getMessage())
                    );
            compositeDisposable.add(disposable);
        }
    }


    @Override
    public void  insertMealToCalendar(PlannerModel meal, String day) {
        Log.d("MAI", "Enter plan");

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            meal.setUserId(FirebaseAuth.getInstance().getUid());
            Disposable disposable = repo.insertMealToCalendar(meal, day)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> detailsView.onMealInsertedToCalendar(),
                            throwable -> detailsView.onError(throwable.getMessage())
                    );
            compositeDisposable.add(disposable);
        }
    }

    @Override
    public void deletePlannedMeal(PlannerModel meal) {
        Disposable disposable = repo.deletePlannedMeal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> detailsView.onMealInsertedToCalendar(),
                        throwable -> detailsView.onError(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
    }

    public void clearDisposables() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }
}
