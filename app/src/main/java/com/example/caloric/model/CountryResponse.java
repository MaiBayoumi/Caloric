package com.example.caloric.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryResponse {
    @SerializedName("meals")
    private List<Country> countries;


    public CountryResponse(List<Country> meals) {
        this.countries = meals;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> meals) {
        this.countries = meals;
    }
}
