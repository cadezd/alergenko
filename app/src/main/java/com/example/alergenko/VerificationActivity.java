package com.example.alergenko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class VerificationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_activity);
    }


    // DECLARATION OF COMPONENTS
    ImageButton btnBack;
    Button btnSend;

    @Override
    protected void onStart() {
        super.onStart();

        // INICIALIZATION OF COMPONENTS
        btnBack = findViewById(R.id.btnBack);
        btnSend = findViewById(R.id.btnSend);

        // CLICK LISTENERS
        btnBack.setOnClickListener(view -> openRegisterActivity());
        btnSend.setOnClickListener(view -> verify());

    }

    // ADDITIONAL METHODS
    private void openRegisterActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void verify(){
        // TODO: add verification mechanism

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}