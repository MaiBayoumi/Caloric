package com.example.caloric.recipe.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.caloric.R;
import com.example.caloric.database.LocalDataSource;
import com.example.caloric.model.IngredientModel;
import com.example.caloric.model.Meal;
import com.example.caloric.model.Repo;
import com.example.caloric.model.RepoInterface;
import com.example.caloric.network.RemoteDataSource;
import com.example.caloric.network.RemoteSource;
import com.example.caloric.recipe.presenter.RecipePresenter;
import com.example.caloric.recipe.presenter.RecipePresenterInterface;
import com.example.caloric.register.LogIn;
import com.example.caloric.view.HostedActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MealRecipeFrag extends Fragment implements RecipeViewInterface {

    private ImageView mealImg;
    private TextView mealNameTV, mealCountryTV, mealDescriptionTV;
    private ImageButton addToPlanBtn, addToFavourite;
    private RecyclerView ingredientRecyclerView;
    private YouTubePlayerView youTubePlayer;
    private IngredientRecyclerAdapter ingredientAdapter;
    private RecipePresenterInterface detailsPresenter;
    private FirebaseUser currentUser;
    private Meal currentMeal;
    private int mSelectedIndex;
    String[] videoArray;
    String videoString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((HostedActivity) requireActivity()).bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mealImg = view.findViewById(R.id.detailsImageView);
        mealNameTV = view.findViewById(R.id.detailsMealNameTextView);
        mealCountryTV = view.findViewById(R.id.detailsCountryName);
        mealDescriptionTV = view.findViewById(R.id.detailsDescriptionOfmeal);
        addToPlanBtn = view.findViewById(R.id.detailsAddToPlanBtn);
        addToFavourite = view.findViewById(R.id.detailsAddToFav);
        ingredientRecyclerView = view.findViewById(R.id.detailsIngredientRecycler);
        youTubePlayer = view.findViewById(R.id.youtubePlayer);

        ingredientAdapter = new IngredientRecyclerAdapter(view.getContext());
        ingredientRecyclerView.setAdapter(ingredientAdapter);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        RepoInterface repo = Repo.getInstance(RemoteDataSource.getInstance(view.getContext()), LocalDataSource.getInstance(view.getContext()));
        detailsPresenter = new RecipePresenter(repo, this);

        addToPlanBtn.setOnClickListener(v -> {
            if (currentUser != null) {
                showDaySelectionDialog();
            } else {
                showMaterialDialog(requireContext());
            }
        });

        addToFavourite.setOnClickListener(v -> {
            if (currentUser != null) {
                detailsPresenter.insertMealToFavourite(currentMeal);
                Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show();
            } else {
                showMaterialDialog(requireContext());
            }
        });

        if (getArguments() != null) {
            String id = getArguments().getString("id");
            if (id != null) {
                detailsPresenter.getMealById(id);
            }
        }
    }


    private void showDaySelectionDialog() {
        List<String> daysOfWeek = Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_single_choice, daysOfWeek);

        new AlertDialog.Builder(requireContext())
                .setTitle("Select a day of the week")
                .setSingleChoiceItems(adapter, mSelectedIndex, (dialog, which) -> mSelectedIndex = which)
                .setPositiveButton("OK", (dialog, which) -> {
                    if (mSelectedIndex >= 0) {
                        String selectedDay = daysOfWeek.get(mSelectedIndex);
                        Log.d("MealRecipeFrag", "Selected Day: " + selectedDay); // Log the selected day
                        detailsPresenter.updateDayOfMeal(currentMeal.getIdMeal(), selectedDay);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void showMaterialDialog(Context context) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(getString(R.string.caloric))
                .setMessage(getString(R.string.messageAdd))
                .setNegativeButton(getString(R.string.signIn), (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), LogIn.class);
                    startActivity(intent);
                })
                .setPositiveButton(getString(R.string.cancel), null)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HostedActivity) requireActivity()).bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onGetMealDetails(List<Meal> meals) {
        currentMeal = meals.get(0);
        Glide.with(getContext()).load(meals.get(0).getStrMealThumb())
                .apply(new RequestOptions().override(500, 500)
                        .error(R.drawable.meals)).into(mealImg);
        mealNameTV.setText(meals.get(0).getStrMeal());
        mealCountryTV.setText(meals.get(0).getStrArea());
        mealDescriptionTV.setText(meals.get(0).getStrInstructions());

        if (!meals.get(0).getStrYoutube().equals("")) {
            videoArray = meals.get(0).getStrYoutube().split("=");
            videoString = videoArray[1];
        } else {
            videoString = "";
        }
        youTubePlayer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                youTubePlayer.loadVideo(videoString, 0);
                youTubePlayer.pause();
            }
        });
        ArrayList<IngredientModel> ingredientPojos = getIngredientPojoList(meals.get(0));
        ingredientAdapter.setList(ingredientPojos);
        ingredientAdapter.notifyDataSetChanged();
    }

    private void setupYouTubePlayer(String youtubeUrl) {
        if (youtubeUrl != null && !youtubeUrl.isEmpty()) {
            String videoId = youtubeUrl.split("=")[1];
            youTubePlayer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    youTubePlayer.loadVideo(videoId, 0);
                    youTubePlayer.pause();
                }
            });
        }
    }

    private ArrayList<IngredientModel> getIngredientPojoList(Meal meal) {
        ArrayList<IngredientModel> ingredientList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            try {
                String ingredient = (String) meal.getClass().getMethod("getStrIngredient" + i).invoke(meal);
                String measure = (String) meal.getClass().getMethod("getStrMeasure" + i).invoke(meal);
                if (ingredient != null && !ingredient.isEmpty() && measure != null && !measure.isEmpty()) {
                    String imageUrl = "https://www.themealdb.com/images/ingredients/" + ingredient + ".png";
                    ingredientList.add(new IngredientModel(ingredient, measure, imageUrl));
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                // Handle exception if needed
            }
        }
        return ingredientList;
    }

    @Override
    public void insertMealToFavourite(Meal meal) {
        detailsPresenter.insertMealToFavourite(meal);
    }

    @Override
    public void onFailToGetMealDetails(String message) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((HostedActivity) requireActivity()).bottomNavigationView.setVisibility(View.VISIBLE);
    }
}
