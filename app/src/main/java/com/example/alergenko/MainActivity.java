package com.example.alergenko;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set's the layout
        setContentView(R.layout.main_activity);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onStart() {
        super.onStart();

        // DECLARATION AND INICIALIZATION OF COMPONENTS
        BottomNavigationView navNavigation = findViewById(R.id.navNavigation);

        // EVENT LISTENERS
        navNavigation.setOnNavigationItemSelectedListener(navListener);

        // chooses wich fragment to open when MainActivity starts
        Intent intent = getIntent();
        int frgagmentToOpen = intent.getIntExtra("fragmentToOpen", R.id.nav_scan);
        switch (frgagmentToOpen) {
            case R.id.nav_settings:
                navNavigation.setSelectedItemId(R.id.nav_settings);
                getSupportFragmentManager().beginTransaction().replace(R.id.frmLayoutFragementContainer, new SettingsFragment()).commit();
                break;
            case R.id.nav_history:
                navNavigation.setSelectedItemId(R.id.nav_history);
                getSupportFragmentManager().beginTransaction().replace(R.id.frmLayoutFragementContainer, new HistoryFragment()).commit();
                break;
            default:
                // set's default highlighted tab to nav_scan tab in bottom navigation bar
                navNavigation.setSelectedItemId(R.id.nav_scan);
                // when MainActivity starts by default it opens ScanFragment
                getSupportFragmentManager().beginTransaction().replace(R.id.frmLayoutFragementContainer, new ScanFragment()).commit();
                break;
        }

    }

    // menu item selector
    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment;

                switch (item.getItemId()) {
                    case R.id.nav_settings:
                        selectedFragment = new SettingsFragment();
                        break;
                    case R.id.nav_history:
                        selectedFragment = new HistoryFragment();
                        break;
                    default:
                        selectedFragment = new ScanFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frmLayoutFragementContainer, selectedFragment).commit();

                return true;
            };
}