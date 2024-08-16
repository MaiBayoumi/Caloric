package com.example.caloric.recipe.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.caloric.model.Meal;

public class SharedMealViewModel extends ViewModel {
    private final MutableLiveData<Meal> selectedMeal = new MutableLiveData<>();

    public void selectMeal(Meal meal) {
        selectedMeal.setValue(meal);
    }

    public LiveData<Meal> getSelectedMeal() {
        return selectedMeal;
    }
}
