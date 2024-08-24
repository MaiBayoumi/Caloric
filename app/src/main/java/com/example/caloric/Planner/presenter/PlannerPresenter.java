package com.example.caloric.Planner.presenter;

import com.example.caloric.Planner.view.PlannerViewInterface;
import com.example.caloric.model.Meal;
import com.example.caloric.model.RepoInterface;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

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
    public void getMealsForDay(String day) {
        Observable<List<Meal>> mealsObservable = repo.getMealsOfDay(day).toObservable();
        disposables.add(
                mealsObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                meals -> dayViewInterface.onGetMealOfDay(meals),
                                throwable -> dayViewInterface.onError("Error fetching meals: " + throwable.getMessage())
                        )
        );
    }

    @Override
    public void deleteMeal(Meal meal) {
        disposables.add(
                repo.deleteMealFromFavourite(meal)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> dayViewInterface.onMealDeleted(),
                                throwable -> dayViewInterface.onError("Error deleting meal: " + throwable.getMessage())
                        )
        );
    }

    @Override
    public void updateDayOfMeal(String id, String day) {
        disposables.add(
                repo.updateDayOfMeal(id, day)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> dayViewInterface.onMealUpdated(),
                                throwable -> dayViewInterface.onError("Error updating meal: " + throwable.getMessage())
                        )
        );
    }

    public void clearDisposables() {
        disposables.clear();
    }
}
