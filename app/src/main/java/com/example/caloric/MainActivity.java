package com.example.caloric;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.ui.NavigationUI;

import com.example.caloric.network.NetworkChecker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    public static NavController navController;
    public static BottomNavigationView bottomNavigationView;
    public static MainActivity mainActivity;
    private NavigationView navigationView;
    private NetworkChecker networkChecker;
    public static TextView tv_headerDrawer;
    private static final String TAG = "MainActivity";
    private Timer timer;
    private Boolean timerIsExists = false;
    public static Boolean Asaguest = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkChecker = NetworkChecker.getInstance(this);
        bottomNavigationView = findViewById(R.id.bottomnavigator);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        //navigationView = findViewById(R.id.n);


    }
}
