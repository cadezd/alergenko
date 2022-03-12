package com.example.alergenko;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set's the layout
        setContentView(R.layout.main_activity);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // DECLARATION AND INICIALIZATION OF COMPONENTS
        BottomNavigationView navNavigation = findViewById(R.id.navNavigation);

        // EVENT LISTENERS
        navNavigation.setOnNavigationItemSelectedListener(navListener);

        // set's default highlighted tab to nav_scan tab in bottom navigation bar
        navNavigation.setSelectedItemId(R.id.nav_scan);
        // when MainActivity starts by default it opens ScanFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.frmLayoutFragementContainer, new ScanFragment()).commit();
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