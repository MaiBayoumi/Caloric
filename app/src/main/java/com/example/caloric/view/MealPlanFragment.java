package com.example.caloric.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yumfit.R;
import com.example.yumfit.authentication.register.RegisterActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MealPlanFragment extends Fragment implements OnPlanClickInterface {

    RecyclerView planRecyclerView;
    PlanRecyclerAdapter planAdapter;

    FirebaseUser currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);


    }

    private void initializeViews(View view) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        planRecyclerView = view.findViewById(R.id.planRecyclerView);
        planAdapter = new PlanRecyclerAdapter(this);
        planRecyclerView.setAdapter(planAdapter);

        if (currentUser != null) {
            planAdapter.setList();
        } else {
            showMaterialDialog(view.getContext());
        }
    }

    @Override
    public void onShowBtnClicked(String day) {
        //we should navigate to fragment show plan for these day
        MealPlanFragmentDirections.ActionMealPlanFragmentToDayDetailFragment action =
                MealPlanFragmentDirections.actionMealPlanFragmentToDayDetailFragment(day);
        Navigation.findNavController(getView()).navigate(action);
    }

    private void showMaterialDialog(Context context) {

        new MaterialAlertDialogBuilder(context)
                .setTitle(getResources().getString(R.string.yumfit))
                .setMessage(getResources().getString(R.string.messagePlan))
                .setNegativeButton(getResources().getString(R.string.signIn), (dialog, which) -> {

                    Intent intent = new Intent();
                    intent.setClass(getContext(), RegisterActivity.class);
                    startActivity(intent);
                })
                .setPositiveButton(getResources().getString(R.string.cancel), (dialog, which) -> {


                })
                .show();
    }
}