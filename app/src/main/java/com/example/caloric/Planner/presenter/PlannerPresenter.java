package com.example.caloric.Planner.presenter;

import android.util.Log;

import com.example.caloric.Planner.model.PlannerModel;
import com.example.caloric.Planner.view.PlannerViewInterface;
import com.example.caloric.model.Meal;
import com.example.caloric.model.RepoInterface;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlannerPresenter implements PlannerPresenterInterface {
    private RepoInterface repo;
    private PlannerViewInterface dayViewInterface;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public PlannerPresenter(RepoInterface repo, PlannerViewInterface dayViewInterface){
        this.repo = repo;
        this.dayViewInterface = dayViewInterface;
    }

    @Override
    public void getMealsForDay(String day, String userId) {
        disposables.add(
                repo.getMealsOfDay(day,userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                plannerModels -> dayViewInterface.onGetMealOfDay(plannerModels),
                                throwable -> dayViewInterface.onError("Error fetching meals: " + throwable.getMessage())
                        )
        );
    }

    @Override
    public void deletePlannedMeal(PlannerModel meal) {
        disposables.add(
                repo.deletePlannedMeal(meal)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> dayViewInterface.onMealDeleted(),  // Notify the view on successful deletion
                                throwable -> dayViewInterface.onError("Error deleting meal: " + throwable.getMessage())
                        )
        );
    }




    public void clearDisposables() {
        disposables.clear();
    }

}
