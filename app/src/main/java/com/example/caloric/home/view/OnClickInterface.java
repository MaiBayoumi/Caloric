package com.example.caloric.home.view;


import com.example.caloric.model.Category;
import com.example.caloric.model.Country;
import com.example.caloric.model.Meal;

public interface OnClickInterface {
    void onSaveBtnClick(Meal meal);

    void onDailyInspirationItemClicked(String id);

    void onCategoryItemClicked(Category category);
    void onCountryItemClicked(Country country);

}
