package com.example.caloric.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IngredientResponse {
    @SerializedName("meals")
    private List<Ingredient> Ingredients;

    public IngredientResponse(List<Ingredient> Ingredients) {
        this.Ingredients = Ingredients;
    }

    public List<Ingredient> getIngredient() {
        return Ingredients;
    }

    public void setIngredient(List<Ingredient> meals) {
        this.Ingredients = meals;
    }
}
