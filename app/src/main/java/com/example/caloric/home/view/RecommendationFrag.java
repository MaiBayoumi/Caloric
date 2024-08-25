package com.example.caloric.home.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloric.R;
import com.example.caloric.database.LocalDataSource;
import com.example.caloric.database.LocalSource;
import com.example.caloric.home.presenter.RecommendationPresenter;
import com.example.caloric.home.presenter.RecommendationPresenterInterface;
import com.example.caloric.model.Category;
import com.example.caloric.model.Country;
import com.example.caloric.model.Meal;
import com.example.caloric.model.MealResponse;
import com.example.caloric.model.Repo;
import com.example.caloric.model.User;
import com.example.caloric.network.RemoteDataSource;
import com.example.caloric.network.RemoteSource;
import com.example.caloric.register.LogIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendationFrag extends Fragment implements RecommendationViewInterface, OnClickInterface {
    private static final String TAG = "TAG";

    private RecyclerView dailyRecyclerView, countryRecyclerView, categoryRecyclerView;
    private RecommendationPresenterInterface presenter;
    private DailyRecyclerAdapter dailyAdapter;
    private CountryRecyclerAdapter countryAdapter;
    private CategoryRecyclerAdapter categoryAdapter;
    private TextView dailyTV, countryTV, categoryTV, caloric;

    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private User userPojo;

    private ArrayList<Meal> mealList;
    private ArrayList<Country> countryList;
    private ArrayList<Category> categoryList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mealList = new ArrayList<>();
        categoryList = new ArrayList<>();
        countryList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommendation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dailyRecyclerView = view.findViewById(R.id.recommendRecycler);
        countryRecyclerView = view.findViewById(R.id.countriesRecyclerView);
        categoryRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        dailyTV = view.findViewById(R.id.dailyTV);
        countryTV = view.findViewById(R.id.countriesTextView);
        categoryTV = view.findViewById(R.id.categoriesTextView);
        caloric = view.findViewById(R.id.caloric);

        dailyAdapter = new DailyRecyclerAdapter(view.getContext(), this, mealList);
        countryAdapter = new CountryRecyclerAdapter(view.getContext(), this, countryList);
        categoryAdapter = new CategoryRecyclerAdapter(view.getContext(), this, categoryList);

        setupRecyclerView(dailyRecyclerView, dailyAdapter);
        setupRecyclerView(countryRecyclerView, countryAdapter);
        setupRecyclerView(categoryRecyclerView, categoryAdapter);

        RemoteSource remoteSource = RemoteDataSource.getInstance(view.getContext());
        LocalSource localSource = LocalDataSource.getInstance(view.getContext());
        Repo repo = Repo.getInstance(remoteSource, localSource);
        presenter = new RecommendationPresenter(repo, this);

        presenter.getRandomMeals();
        presenter.getAllCountries();
        presenter.getAllCategories();

        if (currentUser != null) {
            checkDataInFireStore();
        }
    }

    private void setupRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter<?> adapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setDailyInspirationData(List<Meal> meals) {
        dailyAdapter.setList((ArrayList<Meal>) meals);
        dailyAdapter.notifyDataSetChanged();
    }

    @Override
    public void setListToCategoriesAdapter(List<Category> categories) {
        categoryAdapter.setList((ArrayList<Category>) categories);
        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void setListToCountriesAdapter(List<Country> countries) {
        countryAdapter.setList((ArrayList<Country>) countries);
        countryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailureResult(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onInsertMealSuccess() {
        //Toast.makeText(getContext(), "Meal successfully added to favorites.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInsertAllFavSuccess() {
        //Toast.makeText(getContext(), "All favorite meals have been successfully added.", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onSaveBtnClick(Meal meal) {
        if (currentUser != null) {
            presenter.insertMeal(meal);
        } else {
            showMaterialDialog(getContext());
        }
    }

    @Override
    public void onDailyInspirationItemClicked(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        NavController navController = Navigation.findNavController(getView());
        navController.navigate(R.id.action_recommendationFrag_to_mealRecipeFrag, args);
    }


    @Override
    public void onGetMeals(List<Meal> meals) {
        if (meals != null && !meals.isEmpty()) {
            dailyRecyclerView.setVisibility(View.VISIBLE);
            dailyAdapter.setList(new ArrayList<>(meals)); // Set the data directly to the adapter
            dailyAdapter.notifyDataSetChanged();
        } else {
            onFailureResult("No meals found");
        }
    }

  @Override
    public void onCountryItemClicked(Country country) {
        Bundle args = new Bundle();
        args.putString("filter", country.getStrArea());
        args.putString("filterType", "country");
        NavController navController = Navigation.findNavController(getView());
        navController.navigate(R.id.action_recommendationFrag_to_mealsFrag, args);
    }

    @Override
    public void onCategoryItemClicked(Category category) {
        Bundle args = new Bundle();
        args.putString("filter", category.getStrCategory());
        args.putString("filterType", "category");
        NavController navController = Navigation.findNavController(getView());
        navController.navigate(R.id.action_recommendationFrag_to_mealsFrag, args);
    }

    private void checkDataInFireStore() {
        db.collection("users")
                .document(currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                userPojo = document.toObject(User.class);
                                if (userPojo != null && userPojo.getFavMeals() != null) {
                                    presenter.insertAllFav(userPojo.getFavMeals());
                                }
                            } else {
                                createNewUserInFireStore();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void createNewUserInFireStore() {
        User newUser = new User(currentUser.getDisplayName(), currentUser.getEmail());
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userPojo", newUser);

        db.collection("users")
                .document(currentUser.getUid())
                .set(userMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "New user added to Firestore");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error adding document", e);
                    }
                });
    }

    private void showMaterialDialog(Context context) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(getResources().getString(R.string.caloric))
                .setMessage(getResources().getString(R.string.messageAdd))
                .setNegativeButton(getResources().getString(R.string.signIn), (dialog, which) -> {
                    Intent intent = new Intent(getContext(), LogIn.class);
                    startActivity(intent);
                })
                .setPositiveButton(getResources().getString(R.string.cancel), (dialog, which) -> dialog.dismiss())
                .show();
    }
}