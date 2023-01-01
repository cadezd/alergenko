package com.example.alergenko;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    // for camera permission
    private int CAMERA_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set's the layout
        setContentView(R.layout.main_activity);
    }

    // DECLARATION OF COMPONENTS
    BottomNavigationView navNavigation;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onStart() {
        super.onStart();

        // INICIALIZATION OF COMPONENTS
        navNavigation = findViewById(R.id.navNavigation);

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

        // for granting permission to use camera
        askForPermissions();
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


    private void askForPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(this)
                    .setTitle(getStringResourceByName("notification_permission_needed"))
                    .setMessage(getStringResourceByName("notification_permission_needed_expleined"))
                    .setPositiveButton("VREDU", (dialogInterface, i) -> ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE))
                    .setNegativeButton("PREKLIČI", (dialogInterface, i) -> dialogInterface.dismiss())
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    private String getStringResourceByName(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }
}