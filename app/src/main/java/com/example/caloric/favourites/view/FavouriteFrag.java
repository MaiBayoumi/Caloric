package com.example.caloric.favourites.view;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloric.R;
import com.example.caloric.database.LocalDataSource;
import com.example.caloric.database.LocalSource;
import com.example.caloric.favourites.presenter.FavouritePresenter;
import com.example.caloric.favourites.presenter.FavouritePresenterInterface;
import com.example.caloric.home.view.RecommendationFrag;
import com.example.caloric.model.Meal;
import com.example.caloric.model.Repo;
import com.example.caloric.model.RepoInterface;
import com.example.caloric.model.User;
import com.example.caloric.network.RemoteDataSource;
import com.example.caloric.network.RemoteSource;
import com.example.caloric.register.LogIn;
import com.example.caloric.view.HostedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavouriteFrag extends Fragment implements LifecycleOwner, FavouriteViewInterface, OnClickFavouriteInterface {

    FavouritePresenterInterface favouritePresenter;
    RecyclerView favouriteRecycler;
    FavouriteRecyclerAdapter favouriteAdapter;
    TextView nullText;
    Button refreshBtn;
    FirebaseUser currentUser;
    List<Meal> mealsFromRoom;

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


        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        RemoteSource remoteSource = RemoteDataSource.getInstance(view.getContext());
        LocalSource localSource = LocalDataSource.getInstance(view.getContext());
        RepoInterface repo = Repo.getInstance(remoteSource, localSource);
        favouritePresenter = new FavouritePresenter(repo, this);

        favouriteRecycler = view.findViewById(R.id.favouriteRecyclerView);
        favouriteAdapter = new FavouriteRecyclerAdapter(view.getContext(), this);
        favouriteRecycler.setAdapter(favouriteAdapter);
        favouriteRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        favouritePresenter.getAllMeals();

        if (currentUser == null) {
            showMaterialDialog(view.getContext());
            nullText.setText("Sign In and Comeback..");
        }

        if (!checkConnection()) {
            refreshBtn.setVisibility(View.VISIBLE);
        }

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection()) {
                    connectFragments();
                }
            }
        });

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
    public void deleteMealFromFavourite(Meal meal) {
        favouritePresenter.deleteMeal(meal);
    }


    @Override
    public void onDeleteBtnClicked(Meal meal) {
        favouritePresenter.deleteMeal(meal);
    }

    @Override
    public void onFavItemClicked(String id) {
        if (checkConnection()) {
            // Assuming you fetch the meal by ID and check its favorite status
            Meal meal = favouritePresenter.getMealById(id);
            favouritePresenter.saveMealIfFavourite(meal);

            if (meal.isFavorite() == true) {
                Bundle args = new Bundle();
                args.putString("id", id);
                NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.action_favouritelistFrag_to_mealRecipeFrag, args);
            } else {
                Toast.makeText(getContext(), "Meal not added to favorites", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateUserDataInFireStore() {
        User updatedUser = new User(currentUser.getDisplayName(), currentUser.getEmail(),
                mealsFromRoom);
        Map<String, Object> data = new HashMap<>();
        data.put("userPojo", updatedUser);
        FirebaseFirestore.getInstance().collection("users")
                .document(currentUser.getUid())
                .set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("hey", "User updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("hey", "Error updating user", e);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (currentUser != null) {
            updateUserDataInFireStore();
        }
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
                .setPositiveButton(getResources().getString(R.string.cancel), (dialog, which) -> {


                })
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
