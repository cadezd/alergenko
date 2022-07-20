package com.example.alergenko;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class EditInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set's the layout
        setContentView(R.layout.edit_info_activity);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // DECLARATION AND INICIALIZATION OF COMPONENTS
        ImageButton btnBack = findViewById(R.id.btnBack);

        // CLICK LISTENERS
        // opens main activty
        btnBack.setOnClickListener(view -> openMainActivty());

    }

    private void openMainActivty() {
        Intent intent = new Intent(EditInfoActivity.this, MainActivity.class);
        intent.putExtra("fragmentToOpen", R.id.nav_settings);
        startActivity(intent);
    }
}