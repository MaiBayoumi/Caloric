package com.example.caloric.favourites.presenter;

import android.view.View;

import com.example.caloric.favourites.view.FavouriteViewInterface;
import com.example.caloric.model.Meal;
import com.example.caloric.model.RepoInterface;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavouritePresenter implements FavouritePresenterInterface {
    private RepoInterface repo;
    private FavouriteViewInterface favouriteView;
    private CompositeDisposable compositeDisposable;
    View view;

    public FavouritePresenter(RepoInterface repo, FavouriteViewInterface favouriteView) {
        this.repo = repo;
        this.favouriteView = favouriteView;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getAllMeals(String userId) {
        Disposable disposable = repo.getAllFavouriteMeals(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> favouriteView.onGetFavouriteMeals(meals),
                        throwable -> favouriteView.onError(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
    }

    @Override
    public void deleteMeal(Meal meal) {
        Disposable disposable = repo.deleteMealFromFavourite(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            favouriteView.onDeleteFromFav();
                        },
                        throwable -> favouriteView.onError(throwable.getMessage())
                );
        compositeDisposable.add(disposable);

    }

    public void dispose() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}