package com.example.caloric.search.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.caloric.R;
import com.example.caloric.database.LocalDataSource;
import com.example.caloric.database.LocalSource;
import com.example.caloric.model.Category;
import com.example.caloric.model.Country;
import com.example.caloric.model.Ingredient;
import com.example.caloric.model.Meal;
import com.example.caloric.model.Repo;
import com.example.caloric.model.RepoInterface;
import com.example.caloric.network.RemoteDataSource;
import com.example.caloric.network.RemoteSource;
import com.example.caloric.search.presenter.SearchPresenter;
import com.example.caloric.search.presenter.SearchPresenterInterface;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import com.jakewharton.rxbinding4.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import java.util.ArrayList;
import java.util.List;

public class SearchFrag extends Fragment implements SearchViewInterface, OnSearchClickInterface  {

    EditText searchET;
    RecyclerView searchRecycler;
    SearchPresenterInterface searchPresenter;
    SearchRecyclerAdapter searchRecyclerAdapter;
    RadioGroup radioGroup;
    TextView nullTextView;
    int selectedBtnId;
    ArrayList<Meal> emptyMeals = new ArrayList<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        radioGroup = view.findViewById(R.id.radioGroup);
        searchET = view.findViewById(R.id.etSearch);
        searchRecycler = view.findViewById(R.id.searchRecycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        searchRecyclerAdapter = new SearchRecyclerAdapter(view.getContext(), this);
        searchRecycler.setLayoutManager(linearLayoutManager);
        searchRecycler.setAdapter(searchRecyclerAdapter);

        selectedBtnId = R.id.country_radioBtn;
        nullTextView = view.findViewById(R.id.nullTextViewInsearch);


        RemoteSource remoteSource = RemoteDataSource.getInstance(view.getContext());
        LocalSource localSource = LocalDataSource.getInstance(view.getContext());
        RepoInterface repo = Repo.getInstance(remoteSource, localSource);
        searchPresenter = new SearchPresenter(repo, this);

        searchPresenter.getMealsByCountry("Egyptian");
        Log.d("SearchFrag", "Getting Canadian meals");

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.meal_radioBtn) {
                    searchPresenter.getRandomMeals();
                } else if (checkedId == R.id.country_radioBtn) {
                    searchPresenter.getMealsByCountry("Egyptian");
                } else if (checkedId == R.id.category_radioBtn) {
                    searchPresenter.getMealsByCategory("chicken");
                } else if (checkedId == R.id.ingredient_radioBtn) {
                    searchPresenter.getMealsByIngredient("butter");
                }
                selectedBtnId = checkedId;
            }
        });
//        searchET.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String query = s.toString();
//                if (selectedBtnId == R.id.meal_radioBtn) {
//                    searchPresenter.getMealByName(query);
//                } else if (selectedBtnId == R.id.country_radioBtn) {
//                    searchPresenter.getMealsByCountry(query);
//                } else if (selectedBtnId == R.id.category_radioBtn) {
//                    searchPresenter.getMealsByCategory(query);
//                } else if (selectedBtnId == R.id.ingredient_radioBtn) {
//                    searchPresenter.getMealsByIngredient(query);
//                }
//            }
//        });
//
//
//    }
        Disposable searchDisposable = RxTextView.textChanges(searchET)
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    String query = charSequence.toString();
                    if (selectedBtnId == R.id.meal_radioBtn) {
                        searchPresenter.getMealByName(query);
                    } else if (selectedBtnId == R.id.country_radioBtn) {
                        searchPresenter.getMealsByCountry(query);
                    } else if (selectedBtnId == R.id.category_radioBtn) {
                        searchPresenter.getMealsByCategory(query);
                    } else if (selectedBtnId == R.id.ingredient_radioBtn) {
                        searchPresenter.getMealsByIngredient(query);
                    }
                });

        compositeDisposable.add(searchDisposable);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.clear();
    }


    @Override
    public void onSaveBtnClicked(Meal meal) {
        searchPresenter.insertMeal(meal);

    }

    @Override
    public void onItemClicked(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        NavController navController = Navigation.findNavController(getView());
        navController.navigate(R.id.action_searchFrag_to_mealRecipeFrag, args);
    }

    @Override
    public void onGetMeals(List<Meal> meals) {
        if (meals != null && !meals.isEmpty()) {
            searchRecycler.setVisibility(View.VISIBLE);
            nullTextView.setVisibility(View.GONE);
            searchRecyclerAdapter.setList((ArrayList<Meal>) meals);
            searchRecyclerAdapter.notifyDataSetChanged();
        } else {
            onFailureResult("No meals found");
        }
    }



    @Override
    public void onGetAllCategories(List<Category> categories) {

    }

    @Override
    public void onGetAllIngredient(List<Ingredient> ingredients) {

    }

    @Override
    public void onGetAllCountries(List<Country> countries) {

    }

    @Override
    public void onFailureResult(String message) {
        nullTextView.setText(message);
        nullTextView.setVisibility(View.VISIBLE);
        searchRecycler.setVisibility(View.GONE);
    }

}