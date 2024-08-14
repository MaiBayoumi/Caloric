package com.example.caloric;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HostedActivity extends AppCompatActivity {
    public BottomNavigationView bottomNavigationView;
    public NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosted);

        initialiseViews();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        if (!checkConnection()) {
            FavouritelistFrag fragment = new FavouritelistFrag();
            fragment.setArguments(new Bundle());
            navController.navigate(R.id.favorite);
            Menu menu = bottomNavigationView.getMenu();
            MenuItem menuItem = menu.findItem(R.id.recommendationFrag);
            MenuItem menuItem2 =menu.findItem(R.id.search);
            MenuItem menuItem3 =menu.findItem(R.id.planner);
            menuItem.setEnabled(false);
            menuItem2.setEnabled(false);
            menuItem3.setEnabled(false);
        }

    }


    private void initialiseViews() {

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        bottomNavigationView = findViewById(R.id.bottomNavigator);

    }

    private boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        boolean isConnected = networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        return isConnected;
    }
}