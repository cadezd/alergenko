package com.example.alergenko;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set's the layout
        setContentView(R.layout.login_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // DECLARATION AND INICIALIZATION OF COMPONENTS
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView txtVRegister = findViewById(R.id.txtVRegister);

        // CLICK LISTENERS
        // login user
        btnLogin.setOnClickListener(view -> login());
        // opens register activty
        txtVRegister.setOnClickListener(view -> openRegisterActivity());
    }

    // ADDITIONAL METHODS
    private void openRegisterActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void login(){
        // TODO: add login mechanism
    }
}