package com.example.caloric.mealplan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.caloric.R;
import com.example.caloric.register.LogIn;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class mealPlanFrag extends Fragment implements OnPlanClickInterface {

    RecyclerView planRecyclerView;
    mealPlanRecyclerAdapter planAdapter;

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
        planAdapter = new mealPlanRecyclerAdapter(this);
        planRecyclerView.setAdapter(planAdapter);

        if (currentUser != null) {
            planAdapter.setList();
        } else {
            showMaterialDialog(view.getContext());
        }
    }

    @Override
    public void onShowBtnClicked(String day) {
        Bundle args = new Bundle();
        args.putString("day", day);
        NavController navController = Navigation.findNavController(getView());
        navController.navigate(R.id.action_mealPlanFrag_to_plannerFrag, args);
    }

    private void showMaterialDialog(Context context) {

        new MaterialAlertDialogBuilder(context)
                .setTitle(getResources().getString(R.string.caloric))
                .setMessage(getResources().getString(R.string.messagePlan))
                .setNegativeButton(getResources().getString(R.string.signIn), (dialog, which) -> {

                    Intent intent = new Intent();
                    intent.setClass(getContext(), LogIn.class);
                    startActivity(intent);
                })
                .setPositiveButton(getResources().getString(R.string.cancel), (dialog, which) -> {


                })
                .show();
    }
}