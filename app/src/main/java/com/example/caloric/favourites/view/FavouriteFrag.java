package com.example.caloric.favourites.view;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caloric.R;
import com.example.caloric.database.LocalDataSource;
import com.example.caloric.database.LocalSource;
import com.example.caloric.favourites.presenter.FavouritePresenter;
import com.example.caloric.favourites.presenter.FavouritePresenterInterface;
import com.example.caloric.home.view.RecommendationFrag;
import com.example.caloric.model.Meal;
import com.example.caloric.model.Repo;
import com.example.caloric.model.RepoInterface;
import com.example.caloric.network.RemoteDataSource;
import com.example.caloric.network.RemoteSource;
import com.example.caloric.register.LogIn;
import com.example.caloric.view.HostedActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class FavouriteFrag extends Fragment implements LifecycleOwner, FavouriteViewInterface, OnClickFavouriteInterface {

    FavouritePresenterInterface favouritePresenter;
    RecyclerView favouriteRecycler;
    FavouriteRecyclerAdapter favouriteAdapter;
    TextView nullText;
    Button refreshBtn;
    FirebaseUser currentUser;
    List<Meal> mealsFromRoom = new ArrayList<>();  // Initialize with an empty list
    private final CompositeDisposable disposables = new CompositeDisposable();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favouritelist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nullText = view.findViewById(R.id.nullTextView);
        refreshBtn = view.findViewById(R.id.refresh);
        favouriteRecycler = view.findViewById(R.id.favouriteRecyclerView);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            // Hide the favorite list and show a message to sign in
            favouriteRecycler.setVisibility(View.GONE);
            nullText.setText("Sign In and Comeback..");
            nullText.setVisibility(View.VISIBLE);
            refreshBtn.setVisibility(View.GONE);
            showMaterialDialog(view.getContext());
        } else {
            // Show the favorite list if the user is signed in
            RemoteSource remoteSource = RemoteDataSource.getInstance(view.getContext());
            LocalSource localSource = LocalDataSource.getInstance(view.getContext());
            RepoInterface repo = Repo.getInstance(remoteSource, localSource);
            favouritePresenter = new FavouritePresenter(repo, this);

            favouriteAdapter = new FavouriteRecyclerAdapter(view.getContext(), this);
            favouriteRecycler.setAdapter(favouriteAdapter);
            favouriteRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

            if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                favouritePresenter.getAllMeals(FirebaseAuth.getInstance().getUid());
            }


            if (!checkConnection()) {
                refreshBtn.setVisibility(View.VISIBLE);
            }

            refreshBtn.setOnClickListener(v -> {
                if (checkConnection()) {
                    connectFragments();
                }
            });
        }
    }


    @Override
    public void onGetFavouriteMeals(List<Meal> favouriteMeals) {

        //Log.d("FavouritelistFrag", "Favourite meals received: " + favouriteMeals.size());
        if (favouriteMeals.isEmpty()) {
            nullText.setVisibility(View.VISIBLE);
        } else {
            nullText.setVisibility(View.GONE);
        }
        favouriteAdapter.setList((ArrayList<Meal>) favouriteMeals);
    }


    @Override
    public void onError(String message) {
        Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeleteFromFav() {
        Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_LONG).show();
    }

//    @Override
//    public void onGetMealDetails(String id) {
//        Bundle args = new Bundle();
//        args.putString("id", id);
//        NavController navController = Navigation.findNavController(getView());
//        navController.navigate(R.id.action_recommendationFrag_to_mealRecipeFrag, args);
//    }

    @Override
    public void onDeleteBtnClicked(Meal meal) {
        favouritePresenter.deleteMeal(meal);
    }

    @Override
    public void onFavItemClicked(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        NavController navController = Navigation.findNavController(getView());
        navController.navigate(R.id.action_favouritelistFrag_to_mealRecipeFrag, args);
    }

    private void updateUserDataInFireStore() {
        // Create a map for user data
        Map<String, Object> userData = new HashMap<>();
        userData.put("displayName", currentUser.getDisplayName());
        userData.put("email", currentUser.getEmail());
        userData.put("uid", currentUser.getUid());

        // Serialize mealsFromRoom if it's not null
        List<Map<String, Object>> mealsList = new ArrayList<>();
        if (mealsFromRoom != null) {
            for (Meal meal : mealsFromRoom) {
                Map<String, Object> mealMap = new HashMap<>();
                mealMap.put("idMeal", meal.getIdMeal());
                mealMap.put("strMeal", meal.getStrMeal());
                // Add other meal properties
                mealsList.add(mealMap);
            }
        }

        // Combine user data and meals data
        Map<String, Object> data = new HashMap<>();
        data.put("userData", userData);
        data.put("meals", mealsList);

        // Update Firestore document
        FirebaseFirestore.getInstance().collection("users")
                .document(currentUser.getUid())
                .set(data, SetOptions.merge())
                .addOnSuccessListener(unused -> Log.d("hey", "User updated successfully"))
                .addOnFailureListener(e -> Log.d("hey", "Error updating user", e));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (currentUser != null) {
            updateUserDataInFireStore();
        }
        disposables.clear();
    }

    private void showMaterialDialog(Context context) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(getResources().getString(R.string.caloric))
                .setMessage(getResources().getString(R.string.messageFav))
                .setNegativeButton(getResources().getString(R.string.signIn), (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), LogIn.class);
                    startActivity(intent);
                })
                .setPositiveButton(getResources().getString(R.string.cancel), (dialog, which) -> {})
                .show();
    }

    private boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        boolean isConnected = networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        return isConnected;
    }

    private void connectFragments() {
        HostedActivity activity = (HostedActivity) getActivity();
        RecommendationFrag fragment = new RecommendationFrag();
        fragment.setArguments(new Bundle());
        activity.navController.navigate(R.id.recommendationFrag);
        Menu menu = activity.bottomNavigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.recommendationFrag);
        MenuItem menuItem2 = menu.findItem(R.id.searchFrag);
        MenuItem menuItem3 = menu.findItem(R.id.profileFrag);
        menuItem.setEnabled(true);
        menuItem2.setEnabled(true);
        menuItem3.setEnabled(true);
    }
}