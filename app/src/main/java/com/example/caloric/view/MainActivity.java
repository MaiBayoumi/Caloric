package com.example.caloric.view;


import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.caloric.R;
import com.example.caloric.register.LogIn;


public class MainActivity extends AppCompatActivity {
    Animation scaleChanger;
    TextView appNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        scaleChanger = AnimationUtils.loadAnimation(this, R.anim.scale_changer);

        appNameTextView = findViewById(R.id.appNameTextView);

        appNameTextView.startAnimation(scaleChanger);
        appNameTextView.postOnAnimationDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LogIn.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
    }
}
