package com.example.alergenko;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alergenko.notifications.Notification;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivty extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set's the layout
        setContentView(R.layout.reset_password_activty);

        // connection with FireBase
        mAuth = FirebaseAuth.getInstance();
    }

    // DECLARATION OF COMPONENTS
    Button btnSendEmail;
    EditText txtInEmail;
    ImageButton btnBack;

    // for checking internet connection
    boolean isConnected;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();

        checkConnectivity();

        // INICIALIZATION OF COMPONENTS
        btnSendEmail = findViewById(R.id.btnSendEmail);
        txtInEmail = findViewById(R.id.txtInEmail);
        btnBack = findViewById(R.id.btnBack);

        // CLICK LISTENERS
        btnSendEmail.setOnClickListener(view -> sendPassowrdResetEmail());
        btnBack.setOnClickListener(view -> openLoginActivty(null));
    }

    private void sendPassowrdResetEmail() {
        if (!isConnected) {  // notifies user that there is no internet connection
            txtInEmail.setText("");
            Notification problemNotification = new Notification(getStringResourceByName("exception"), getStringResourceByName("no_internet_connection"), ResetPasswordActivty.this);
            problemNotification.show();
            return;
        }

        String email = txtInEmail.getText().toString();
        if (!isValidEmail(email)) return; // checks if enterd email is vaild
        mAuth.setLanguageCode("sl");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> { // sends password reset email and opens LoginActivty with a message
            if (task.isSuccessful()) {
                openLoginActivty(getStringResourceByName("notification_reset_password_email_send"));
            } else {
                txtInEmail.setText("");
                Notification problemNotification = new Notification(getStringResourceByName("exception"), getStringResourceByName("exception_general"), ResetPasswordActivty.this);
                problemNotification.show();
            }
        });
    }

    protected boolean isValidEmail(String email) {
        TextInputLayout textInputLayout = findViewById(R.id.editTextEmailAddress);
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (email.matches(regex)) {
            textInputLayout.setErrorEnabled(false);
            return true;
        }
        txtInEmail.getText().clear();
        txtInEmail.requestFocus();
        txtInEmail.setText("");
        textInputLayout.setError("Potrebno je vnesti vaš e-poštni naslov");
        return false;
    }

    // Method to check network connectivity in Main Activity
    private void checkConnectivity() {
        // here we are getting the connectivity service from connectivity manager
        final ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Getting network Info
        // give Network Access Permission in Manifest
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        // isConnected is a boolean variable
        // here we check if network is connected or is getting connected
        isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

        // network callback to  monitor network
        connectivityManager.registerNetworkCallback(
                new NetworkRequest.Builder()
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        .build(), callbackConnectivity);
    }

    // callback function for checking internet connection
    private final ConnectivityManager.NetworkCallback callbackConnectivity = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            isConnected = true;
        }

        @Override
        public void onLost(Network network) {
            isConnected = false;
        }
    };


    private String getStringResourceByName(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }

    private void openLoginActivty(String message) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("message", message);
        startActivity(intent);
    }
}
