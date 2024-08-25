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
                repo.getMealsOfDay(day)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                plannerModels -> dayViewInterface.onGetMealOfDay(plannerModels),
                                throwable -> dayViewInterface.onError("Error fetching meals: " + throwable.getMessage())
                        )
        );
    }




//    @Override
//    public void deleteMeal(Meal meal) {
//        disposables.add(
//                repo.deleteMealFromFavourite(meal)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(
//                                () -> dayViewInterface.onMealDeleted(),
//                                throwable -> dayViewInterface.onError("Error deleting meal: " + throwable.getMessage())
//                        )
//        );
//    }

    //    @Override
//    public void updateDayOfMeal(String id, String day) {
//        disposables.add(
//                repo.updateDayOfMeal(id, day)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(
//                                () -> dayViewInterface.onSuccess(),
//                                throwable -> dayViewInterface.onError("Error updating meal: " + throwable.getMessage())
//                        )
//        );
//    }
    @Override
    public void getAllPlannedMeal() {
        disposables.add(repo.getAllPlannedMeal() // This should return a Flowable<List<PlannerModel>>
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        plannedMeals -> {
                            // Pass the retrieved list to the view
                            dayViewInterface.onSuccess();
                        },
                        throwable -> {
                            // Handle errors
                            Log.e("PlannerPresenter", "Error fetching planned meals", throwable);
                            dayViewInterface.onError(throwable.getMessage());
                        }
                ));
    }


    @Override
    public void insertPlannedMeal(PlannerModel meal) {
        disposables.add(repo.insertPLannedMeal(meal) // This should return a Completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            // Notify the view that the meal was successfully inserted
                            dayViewInterface.onSuccess();
                        },
                        throwable -> {
                            // Handle errors
                            Log.e("PlannerPresenter", "Error inserting planned meal", throwable);
                            dayViewInterface.onError(throwable.getMessage());
                        }
                ));
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

//    @Override
//    public void saveMealToPlan(Meal meal, String day) {
//        if (meal != null && day != null) {
//            // Assume PlannerModel is your entity model for plan_table
//            PlannerModel plannerModel = new PlannerModel(meal.getIdMeal(), meal.getStrMeal(), day, meal.getStrMealThumb());
//            repo.saveMealToPlan(plannerModel);  // Insert the meal into plan_table through the repository
//        }
//    }
}
