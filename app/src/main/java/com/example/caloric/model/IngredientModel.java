package com.example.caloric.model;

public class IngredientModel {
    private String ingredientName;
    private String ingredientMeasure;
    private String ingredientThumb;

    public IngredientModel(String ingredientName, String ingredientMeasure, String ingredientThumb) {
        this.ingredientName = ingredientName;
        this.ingredientMeasure = ingredientMeasure;
        this.ingredientThumb = ingredientThumb;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getIngredientMeasure() {
        return ingredientMeasure;
    }

    public void setIngredientMeasure(String ingredientMeasure) {
        this.ingredientMeasure = ingredientMeasure;
    }

    public String getIngredientThumb() {
        return ingredientThumb;
    }

    public void setIngredientThumb(String ingredientThumb) {
        this.ingredientThumb = ingredientThumb;
    }
}
