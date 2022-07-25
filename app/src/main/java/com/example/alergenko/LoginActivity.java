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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alergenko.entities.Product;
import com.example.alergenko.entities.User;
import com.example.alergenko.networking.NetworkConfig;
import com.example.alergenko.notifications.Notification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;


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

    // for checking internet connection
    boolean isConnected;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();

        checkConnectivity();

        // INICIALIZATION OF COMPONENTS
        btnLogin = findViewById(R.id.btnClose);
        txtVRegister = findViewById(R.id.txtVRegister);
        txtInEmail = findViewById(R.id.txtInEmail);
        txtInPsswd = findViewById(R.id.txtInPsswd);

        txtInEmail.setText("david.cadez89@gmail.com");
        txtInEmail.clearFocus();
        txtInPsswd.setText("David123cadez_");
        txtInPsswd.clearFocus();


        // CLICK LISTENERS
        // login user
        btnLogin.setOnClickListener(view -> login());
        // opens register activty
        txtVRegister.setOnClickListener(view -> openRegisterActivity());

        if (getIntent().getStringExtra("message") != null) { // displays error message if it comes from verification activty
            String tile = getStringResourceByName("notification");
            String message = getIntent().getStringExtra("message");
            Notification problemNotification = new Notification(tile, message, this);
            problemNotification.show();
        }
    }

    // ADDITIONAL METHODS
    private void openRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void login() {
        if (!isConnected) { // checks for the internet connection
            Notification problemNotification = new Notification(getStringResourceByName("exception"), getStringResourceByName("no_internet_connection"), LoginActivity.this);
            problemNotification.show();
            clearInputFields();
            return;
        }

        // generates userId and checks if it's in database
        String userId = hashSha1(txtInEmail.getText().toString().trim() + txtInPsswd.getText().toString());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance(NetworkConfig.URL_DATABASE).getReference("users");
        Query userQuery = databaseReference.orderByChild("userId").equalTo(userId);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) { // if users exists in database retrieves user data and opens MainActivty
                    User.setUserId(userId);
                    User.setFirstName(snapshot.child(userId).child("firstName").getValue(String.class));
                    User.setLastName(snapshot.child(userId).child("lastName").getValue(String.class));
                    User.setEmail(snapshot.child(userId).child("email").getValue(String.class));
                    User.setPhoneNumber(snapshot.child(userId).child("phoneNumber").getValue(String.class));
                    User.setPassword(snapshot.child(userId).child("password").getValue(String.class));

                    ArrayList<Boolean> settings = new ArrayList<>();
                    for (DataSnapshot snapshotSettings : snapshot.child(userId).child("settings").getChildren()) {
                        settings.add(snapshotSettings.getValue(Boolean.class));
                    }
                    User.setSettings(settings);

                    ArrayList<Product> history = new ArrayList<>();
                    for (DataSnapshot snapshotSettings : snapshot.child(userId).child("history").getChildren()) {
                        history.add(snapshotSettings.getValue(Product.class));
                    }
                    User.setHistory(history);

                    openMainActivity();
                } else {
                    Notification problemNotification = new Notification(getStringResourceByName("exception"), getStringResourceByName("exception_username_password"), LoginActivity.this);
                    problemNotification.show();
                    clearInputFields();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { // handling database errors
                Notification problemNotification = new Notification(getStringResourceByName("exception"), error.getMessage(), LoginActivity.this);
                problemNotification.show();
                clearInputFields();
            }
        });
    }

    private String getStringResourceByName(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }

    public static String hashSha1(String clearString) { // for hasing passwords
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(clearString.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = messageDigest.digest();
            StringBuilder buffer = new StringBuilder();
            for (byte b : bytes) {
                buffer.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    public void clearInputFields() {
        txtInEmail.setText("");
        txtInEmail.clearFocus();
        txtInPsswd.setText("");
        txtInPsswd.clearFocus();
    }
}