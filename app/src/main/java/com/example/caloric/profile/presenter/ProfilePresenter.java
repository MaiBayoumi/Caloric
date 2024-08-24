package com.example.caloric.profile.presenter;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;


import com.example.caloric.model.Meal;
import com.example.caloric.model.RepoInterface;
import com.example.caloric.profile.view.ProfileViewInterface;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProfilePresenter implements ProfilePresenterInterface {
    private RepoInterface repo;
    private ProfileViewInterface profileView;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ProfilePresenter(RepoInterface repo, ProfileViewInterface profileView) {
        this.repo = repo;
        this.profileView = profileView;
    }

    @Override
    public void deleteAllFavouriteMeals() {
        Disposable disposable = repo.deleteAllFavouriteMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> profileView.onMealsDeleted(),
                        throwable -> profileView.onError(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
    }

    @Override
    public void getAllFavouriteMeals() {
        Disposable disposable = repo.getAllFavouriteMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> profileView.onGetAllFavouriteList(meals),
                        throwable -> profileView.onError(throwable.getMessage())
                );
        compositeDisposable.add(disposable);
    }

    public void clearDisposables() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }
}