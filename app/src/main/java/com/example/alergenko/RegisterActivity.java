package com.example.alergenko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // DECLARATION AND INICIALIZATION OF COMPONENTS
        ImageButton btnBack = findViewById(R.id.btnBack);

        // CLICK LISTENERS
        btnBack.setOnClickListener(view -> openLoginActivity());
    }

    // ADDITIONAL METHODS
    protected void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    protected void register(){
        //TODO: add register mechanism
    }
}