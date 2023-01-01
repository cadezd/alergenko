package com.example.alergenko;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alergenko.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerificationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set's the layout
        setContentView(R.layout.verification_activity);

        // connection with FireBase
        mAuth = FirebaseAuth.getInstance();
    }


    // DECLARATION OF COMPONENTS
    Button btnOpenLoginActivity;
    TextView txtVResend;

    @Override
    protected void onStart() {
        super.onStart();

        // INICIALIZATION OF COMPONENTS
        btnOpenLoginActivity = findViewById(R.id.btnSendEmail);
        txtVResend = findViewById(R.id.txtVResend);

        // CLICK LISTENERS
        btnOpenLoginActivity.setOnClickListener(view -> openLoginActivity(null));
        txtVResend.setOnClickListener(view -> sendVerificationEmail());

        sendVerificationEmail();
    }

    // ADDITIONAL METHODS
    private void openLoginActivity(String message) {
        User.clearFields();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("message", message);
        startActivity(intent);
    }

    private void sendVerificationEmail() {
        User.clearFields();
        mAuth.setLanguageCode("sl");
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification();
    }
}