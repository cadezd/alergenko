package com.example.alergenko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
    }

    // DECLARATION OF COMPONENTS
    ImageButton btnBack;
    EditText txtInFirstName;
    EditText txtInLastName;
    EditText txtInEmail;
    EditText txtInPhoneNumber;
    EditText txtInPsswd;
    EditText txtInPsswdConfirm;
    Button btnRegister;

    @Override
    protected void onStart() {
        super.onStart();

        // INICIALIZATION OF COMPONENTS
        btnBack = findViewById(R.id.btnBack);
        btnRegister = findViewById(R.id.btnUpdate);

        // CLICK LISTENERS
        btnBack.setOnClickListener(view -> openLoginActivity());
        btnRegister.setOnClickListener(view -> register());
    }

    // ADDITIONAL METHODS
    protected void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    protected void register(){
        //TODO: add register mechanism
        Intent intent = new Intent(this, VerificationActivity.class);
        startActivity(intent);
    }

}