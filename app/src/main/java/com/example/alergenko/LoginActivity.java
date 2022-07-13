package com.example.alergenko;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alergenko.entities.User;
import com.example.alergenko.networking.GetJWT;
import com.example.alergenko.networking.NetworkConfig;
import com.example.alergenko.notifications.ProblemNotification;

import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set's the layout
        setContentView(R.layout.login_acitvity);
    }


    // DECLARATION OF COMPONENTS
    Button btnLogin;
    TextView txtVRegister;
    EditText txtInEmail;
    EditText txtInPsswd;

    @Override
    protected void onStart() {
        super.onStart();

        // INICIALIZATION OF COMPONENTS
        btnLogin = findViewById(R.id.btnClose);
        txtVRegister = findViewById(R.id.txtVRegister);
        txtInEmail = findViewById(R.id.txtInEmail);
        txtInPsswd = findViewById(R.id.txtInPsswd);

        txtInEmail.setText("");
        txtInEmail.clearFocus();
        txtInPsswd.setText("");
        txtInPsswd.clearFocus();


        // CLICK LISTENERS
        // login user
        btnLogin.setOnClickListener(view -> login());
        // opens register activty
        txtVRegister.setOnClickListener(view -> openRegisterActivity());
    }

    // ADDITIONAL METHODS
    private void openRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void login() {
        try {
            // make a POST request to get JWT
            User.setUsername(txtInEmail.getText().toString());
            User.setPassword(txtInPsswd.getText().toString());
            GetJWT getJWT = new GetJWT(User.getUsername(), User.getPassword());
            AsyncTask<String, Void, JSONObject> response = getJWT.execute(NetworkConfig.URL_AUTH);

            // checks if response does not contain JWT and throws an Exception
            if (!response.get().has("jwt"))
                throw new Exception("Napačno uporabniško ime ali geslo");

            // saves JWT and opens MainActivity
            User.setJwt(response.get().get("jwt").toString());
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        } catch (Exception e) {
            handleException(e.getMessage(), this);
        }
    }

    private void handleException(String message, Context context) {
        ProblemNotification problemNotification = new ProblemNotification("Napaka", message, context);
        problemNotification.show();
        txtInEmail.setText("");
        txtInPsswd.setText("");
    }
}