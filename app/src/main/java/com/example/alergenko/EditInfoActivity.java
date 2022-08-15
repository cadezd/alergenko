package com.example.alergenko;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alergenko.entities.User;
import com.example.alergenko.networking.NetworkConfig;
import com.example.alergenko.notifications.Notification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set's the layout
        setContentView(R.layout.edit_info_activity);
    }

    // DECLARATION OF COMPONENTS
    ImageButton btnBack;
    EditText txtInFirstName;
    EditText txtInLastName;
    Button btnUpdate;

    // for checking internet connection
    boolean isConnected;

    @Override
    protected void onStart() {
        super.onStart();

        checkConnectivity();

        // INICIALIZATION OF COMPONENTS
        btnBack = findViewById(R.id.btnBack);
        txtInFirstName = findViewById(R.id.txtInFirstName);
        txtInLastName = findViewById(R.id.txtInLastName);
        btnUpdate = findViewById(R.id.btnUpdate);

        // displaying user data
        txtInFirstName.setText(User.getFirstName());
        txtInLastName.setText(User.getLastName());

        // CLICK LISTENERS
        // opens main activty
        btnBack.setOnClickListener(view -> openMainActivty(null));
        btnUpdate.setOnClickListener(view -> updateUserData());
    }

    private void updateUserData() {

        if (!isConnected){
            Notification problemNotification = new Notification(getStringResourceByName("exception"), getStringResourceByName("no_internet_connection"), EditInfoActivity.this);
            problemNotification.show();
            return;
        }

        // checking if entered data is valid
        if (!isValidFirstName() || !isValidLastName())
            return;

        // collecting entered data
        String firstName = txtInFirstName.getText().toString().trim();
        String lastName = txtInLastName.getText().toString().trim();

        User.setFirstName(firstName);
        User.setLastName(lastName);

        // sets display name
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(User.getFirstName() + " " + User.getLastName())
                .build();
        assert user != null;
        user.updateProfile(profileUpdates);

        // updates all other data
        DatabaseReference databaseReference = FirebaseDatabase.getInstance(NetworkConfig.URL_DATABASE).getReference("users");
        databaseReference.child(user.getUid()).child("firstName").setValue(User.getFirstName());
        databaseReference.child(user.getUid()).child("lastName").setValue(User.getLastName());

        // opens MainActivty and notifies user about changed data
        openMainActivty(getStringResourceByName("notification_data_updated"));
    }

    protected boolean isValidFirstName() {
        TextInputLayout textInputLayout = findViewById(R.id.editTextTextFirstName);
        String firstName = txtInFirstName.getText().toString().trim();
        if (!firstName.isEmpty()) {
            textInputLayout.setErrorEnabled(false);
            return true;
        }
        txtInFirstName.getText().clear();
        txtInFirstName.requestFocus();
        textInputLayout.setError("To polje je obvezno");
        return false;
    }

    protected boolean isValidLastName() {
        TextInputLayout textInputLayout = findViewById(R.id.editTextTextLastName);
        String lastName = txtInLastName.getText().toString().trim();
        if (!lastName.isEmpty()) {
            textInputLayout.setErrorEnabled(false);
            return true;
        }
        txtInLastName.getText().clear();
        txtInLastName.requestFocus();
        textInputLayout.setError("To polje je obvezno");
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

    private void openMainActivty(String message) {
        Intent intent = new Intent(EditInfoActivity.this, MainActivity.class);
        intent.putExtra("fragmentToOpen", R.id.nav_settings);
        intent.putExtra("message", message);
        startActivity(intent);
    }

    private String getStringResourceByName(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }
}