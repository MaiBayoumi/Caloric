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

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.caloric.R;
import com.example.caloric.database.LocalDataSource;
import com.example.caloric.database.LocalSource;
import com.example.caloric.home.presenter.HomePresenter;
import com.example.caloric.home.presenter.HomePresenterInterface;
import com.example.caloric.model.Category;
import com.example.caloric.model.Country;
import com.example.caloric.model.Meal;
import com.example.caloric.model.MealResponse;
import com.example.caloric.model.Repo;
import com.example.caloric.model.RepoInterface;
import com.example.caloric.model.User;
import com.example.caloric.network.ClientService;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendationFrag extends Fragment  implements HomeViewInterface, OnClickInterface{
    private static final String TAG = "TAG";
    RecyclerView dailyRecyclerView, countryRecyclerView, categoryRecyclerView;
    HomePresenterInterface presenter;
    DailyRecyclerAdapter dailyAdapter;
    CountryRecyclerAdapter countryAdapter;
    TextView dailyTV, countryTV, categoryTV,caloric;
    ImageButton exit;
    CategoryRecyclerAdapter categoryAdapter;
    FirebaseFirestore db;
    boolean isExist = false;
    FirebaseUser currentUser;
    User userPojo;
    ArrayList<Meal> myList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommendation, container, false);


    }

    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myList= new ArrayList<>();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        dailyRecyclerView = view.findViewById(R.id.recommendRecycler);
        countryRecyclerView = view.findViewById(R.id.countriesRecyclerView);
        categoryRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        dailyTV = view.findViewById(R.id.dailyTV);
        countryTV = view.findViewById(R.id.countriesTextView);
        categoryTV = view.findViewById(R.id.categoriesTextView);
        caloric= view.findViewById(R.id.caloric);
        exit=view.findViewById(R.id.logout);

        dailyAdapter = new DailyRecyclerAdapter(view.getContext(), this,myList);
        countryAdapter = new CountryRecyclerAdapter(view.getContext(), this);
        categoryAdapter = new CategoryRecyclerAdapter(view.getContext(), this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);

        dailyRecyclerView.setLayoutManager(linearLayoutManager);
        dailyRecyclerView.setAdapter(dailyAdapter);


        countryRecyclerView.setAdapter(countryAdapter);


        categoryRecyclerView.setAdapter(categoryAdapter);

        db = FirebaseFirestore.getInstance();
        userPojo = new User();

        RemoteSource remoteSource = ClientService.getInstance(view.getContext());
        LocalSource localSource = LocalDataSource.getInstance(view.getContext());
        RepoInterface repo = Repo.getInstance(remoteSource, localSource);
        presenter = new HomePresenter(repo, this);

        presenter.getRandomMeal();
        presenter.getAllCountries();
        presenter.getAllCategories();

        if (currentUser != null) {
            checkDataInFireStore();
        }else{
            presenter.deleteAllFavMeals();
        }
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
    public void onSuccessToFilter(MealResponse meals) {
  }

    @Override
    public void onCountryItemClicked(Country country) {
        presenter.getMealsByCountry(country.getStrArea());

    }


    @Override
    public void onCategoryItemClicked(Category category) {
        presenter.getMealsByCategory(category.getStrCategory());
    }


    private void checkDataInFireStore2() {
        db.collection("users")
                .document(currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Map<String, Object> data = document.getData();
                            userPojo = document.toObject(User.class);
                            if (!document.exists()) {
                                createNewUserInFireStore();
                            }
                            if (userPojo.getFavMeals() != null)
                                presenter.insertAllFav(userPojo.getFavMeals());
                            isExist = true;
                        } else {
                            Log.d("hey", "Error getting documents.", task.getException());

                        }
                    }
                });

    }

    private void checkDataInFireStore() {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(currentUser.getUid())) {
                                    Map<String, Object> data = document.getData();
                                    userPojo = new User((Map<String, Object>) data.get("userPojo"));
                                    if (userPojo.getFavMeals() != null)
                                        presenter.insertAllFav(userPojo.getFavMeals());
                                    isExist = true;
                                }
                            }
                            if (!isExist) {
                                createNewUserInFireStore();
                            }
                        } else {
                            Log.d("hey", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void createNewUserInFireStore() {
        Map<String, Object> user = new HashMap<>();
        User newUser = new User(currentUser.getDisplayName(), currentUser.getEmail());
        user.put("userPojo", newUser);

        db.collection("users")
                .document(currentUser.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("hey", "new User Added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("hey", "Error adding document", e);
                    }
                });
    }

    private void showMaterialDialog(Context context) {

        new MaterialAlertDialogBuilder(context)
                .setTitle(getResources().getString(R.string.caloric))
                .setMessage(getResources().getString(R.string.messageAdd))
                .setNegativeButton(getResources().getString(R.string.signIn), (dialog, which) -> {

                    Intent intent = new Intent();
                    intent.setClass(getContext(), LogIn.class);
                    startActivity(intent);
                })
                .setPositiveButton(getResources().getString(R.string.cancel), (dialog, which) -> {

                })
                .show();
    }

    private void hideAnimation(){
        dailyTV.setVisibility(View.VISIBLE);
        countryTV.setVisibility(View.VISIBLE);
        categoryTV.setVisibility(View.VISIBLE);
    }

}