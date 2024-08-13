package com.example.caloric.login;

import android.content.Context;

import com.example.caloric.RecommendationFrag;
import com.example.caloric.database.DAO;
import com.example.caloric.database.LocalDataSource;
import com.example.caloric.model.MealsItem;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class RepositoryLocal {
    private static final String TAG = "Repository";
    private Context context;
    private DAO mealDAO;
    private Flowable<List<MealsItem>> storedMealsItems;
    private List<MealsItem> mealsItemsFromFirestore = new ArrayList<>();
    private List<MealsItem> mealsWeekPlanSaturday = new ArrayList<>(), mealsWeekPlanSunday = new ArrayList<>(), mealsWeekPlanMonday = new ArrayList<>(), mealsWeekPlanTuesday = new ArrayList<>(), mealsWeekPlanWednesday = new ArrayList<>(), mealsWeekPlanThursday = new ArrayList<>(), mealsWeekPlanFriday = new ArrayList<>();
    //private RecommendationF interfaceDailyInspirations;

    public RepositoryLocal(Context context) {
        this.context = context;

        LocalDataSource db =LocalDataSource.getInstance(context);
        mealDAO = db.mealsDAO();

        storedMealsItems = mealDAO.getStoredMealsItems();
    }

}
