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
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alergenko.entities.Product;
import com.example.alergenko.entities.User;
import com.example.alergenko.networking.NetworkConfig;
import com.example.alergenko.notifications.Notification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set's the layout
        setContentView(R.layout.login_acitvity);

        // connection with FireBase
        mAuth = FirebaseAuth.getInstance();
    }


    // DECLARATION OF COMPONENTS
    Button btnLogin;
    TextView txtVRegister;
    EditText txtInEmail;
    EditText txtInPsswd;
    TextView txtVForgotPsswd;

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
        txtVForgotPsswd = findViewById(R.id.txtVForgotPsswd);

        // CLICK LISTENERS
        // login user
        btnLogin.setOnClickListener(view -> login());
        // opens RegisterActivty
        txtVRegister.setOnClickListener(view -> openRegisterActivity());
        // opens ResetPasswordActivty
        txtVForgotPsswd.setOnClickListener(view -> openResetPasswordActivty());

        // displays a message from intent
        if (getIntent().getStringExtra("message") != null) {
            String tile = getStringResourceByName("notification");
            String message = getIntent().getStringExtra("message");
            getIntent().removeExtra("message");
            Notification problemNotification = new Notification(tile, message, this);
            problemNotification.show();
        }
    }

    // ADDITIONAL METHODS
    private void openResetPasswordActivty(){
        Intent intent = new Intent(this, ResetPasswordActivty.class);
        startActivity(intent);
    }

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

        String email = txtInEmail.getText().toString().trim().toLowerCase();
        String password = txtInPsswd.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    assert user != null;
                    if (user.isEmailVerified()) {
                        // collects user data and open MainActivity
                        collectUserData(user);
                        openMainActivity();
                    } else {
                        // notifies user that gmail address has not been verified and sends verification email
                        Notification problemNotification = new Notification(getStringResourceByName("exception"), getStringResourceByName("exception_email_not_verified"), LoginActivity.this);
                        problemNotification.show();
                        clearInputFields();
                    }
                } else {
                    // notifies user about error
                    Notification problemNotification = new Notification(getStringResourceByName("exception"), getStringResourceByName("exception_username_password"), LoginActivity.this);
                    problemNotification.show();
                    clearInputFields();
                }
            }
        });

    }

    private void collectUserData(FirebaseUser user) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance(NetworkConfig.URL_DATABASE).getReference("users");
        databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("bala", snapshot.toString());
                if (snapshot.exists()) {
                    User.setFirstName(snapshot.child("firstName").getValue(String.class));
                    User.setLastName(snapshot.child("lastName").getValue(String.class));
                    User.setPhoneNumber(snapshot.child("phoneNumber").getValue(String.class));
                    User.setEmail(snapshot.child("email").getValue(String.class));

                    ArrayList<Boolean> settings = new ArrayList<>();
                    for (DataSnapshot dataSnapshotSettings : snapshot.child("settings").getChildren())
                        settings.add(dataSnapshotSettings.getValue(Boolean.class));
                    User.setSettings(settings);

                    ArrayList<Product> history = new ArrayList<>();
                    for (DataSnapshot dataSnapshotHistory : snapshot.child("history").getChildren())
                        history.add(dataSnapshotHistory.getValue(Product.class));
                    User.setHistory(history);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // notifies user about error
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