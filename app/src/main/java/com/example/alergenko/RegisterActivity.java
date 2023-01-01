package com.example.alergenko;

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
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alergenko.entities.User;
import com.example.alergenko.entities.UserHelper;
import com.example.alergenko.networking.NetworkConfig;
import com.example.alergenko.notifications.Notification;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set's the layout
        setContentView(R.layout.register_activity);

        // connection with FireBase
        mAuth = FirebaseAuth.getInstance();
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
        txtInEmail = findViewById(R.id.txtInEmail);
        txtInPhoneNumber = findViewById(R.id.txtInPhoneNumber);
        txtInPsswd = findViewById(R.id.txtInPsswd);
        txtInPsswdConfirm = findViewById(R.id.txtInPsswdConfirm);
        btnRegister = findViewById(R.id.btnUpdate);

        // CLICK LISTENERS
        btnBack.setOnClickListener(view -> openLoginActivity(null));
        btnRegister.setOnClickListener(view -> register());

    }

    // ADDITIONAL METHODS
    protected void register() {
        // notifies user that there is no internet connection
        if (!isConnected) {
            Notification problemNotification = new Notification(getStringResourceByName("exception"), getStringResourceByName("no_internet_connection"), this);
            problemNotification.show();
            return;
        }

        // checking if entered data is valid
        if (!isValidFirstName() || !isValidLastName() || !isValidPhoneNumber() || isPhoneNumberDuplicate() || !isValidEmail() || isEmailDuplicate() || !isPasswordStrong() || !isValidPassword())
            return;

        // collecting enterd data
        User.setFirstName(txtInFirstName.getText().toString().trim());
        User.setLastName(txtInLastName.getText().toString().trim());
        User.setPhoneNumber("+386" + txtInPhoneNumber.getText().toString().trim());
        User.setEmail(txtInEmail.getText().toString().trim().toLowerCase());

        String password = txtInPsswd.getText().toString();
        String email = txtInEmail.getText().toString().toLowerCase();

        // registering user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful() && task.isComplete()) {
                        // Sign in success, update UI with the signed-in user's information
                        UserHelper userHelper = new UserHelper(
                                User.getFirstName(),
                                User.getLastName(),
                                User.getEmail(),
                                User.getPhoneNumber()
                        );

                        // sets display name
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(User.getFirstName() + " " + User.getLastName())
                                .build();
                        user.updateProfile(profileUpdates);

                        FirebaseDatabase.getInstance(NetworkConfig.URL_DATABASE).getReference("users")
                                .child(Objects.requireNonNull(user).getUid())
                                .setValue(userHelper).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful() && task1.isComplete()) {
                                        openVerificationActivity();
                                    } else if (task1.isComplete()) {
                                        Notification problemNotification = new Notification(getStringResourceByName("exception"), Objects.requireNonNull(task1.getException()).getMessage(), RegisterActivity.this);
                                        problemNotification.show();
                                    }
                                });
                    } else if (task.isComplete()){
                        // If sign in fails, display a message to the user.
                        Notification problemNotification = new Notification(getStringResourceByName("exception"), Objects.requireNonNull(task.getException()).getMessage(), RegisterActivity.this);
                        problemNotification.show();
                    }
                });
    }

    protected boolean isValidFirstName() {
        TextInputLayout textInputLayout = findViewById(R.id.editTextTextFirstName);
        if (!txtInFirstName.getText().toString().isEmpty()) {
            textInputLayout.setErrorEnabled(false);
            return true;
        }
        txtInFirstName.getText().clear();
        txtInFirstName.requestFocus();
        textInputLayout.setError("Potrebno je vnesti vaše ime");
        return false;
    }

    protected boolean isValidLastName() {
        TextInputLayout textInputLayout = findViewById(R.id.editTextTextLastName);
        if (!txtInLastName.getText().toString().isEmpty()) {
            textInputLayout.setErrorEnabled(false);
            return true;
        }
        txtInLastName.getText().clear();
        txtInLastName.requestFocus();
        textInputLayout.setError("Potrebno je vnesti vaš priimek");
        return false;
    }

    protected boolean isValidPhoneNumber() {
        TextInputLayout textInputLayout = findViewById(R.id.editTextPhoneNumber);
        String regex = "\\+386[1-9][0-9]{7}";
        String phoneNumber = "+386" + txtInPhoneNumber.getText().toString();
        if (phoneNumber.matches(regex)) {
            textInputLayout.setErrorEnabled(false);
            return true;
        }
        txtInPhoneNumber.getText().clear();
        txtInPhoneNumber.requestFocus();
        textInputLayout.setError("Potrebno je vnesti vašo telefonsko številko");
        return false;
    }

    protected boolean isPhoneNumberDuplicate() {
        TextInputLayout textInputLayout = findViewById(R.id.editTextPhoneNumber);
        final boolean[] isPhoneNumberDuplicate = {false};
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance(NetworkConfig.URL_DATABASE).getReference().child("users");
        ref.orderByChild("phoneNumber").equalTo("+386" + txtInPhoneNumber.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { // checks if phone number already exists in database
                if (dataSnapshot.exists()) {
                    isPhoneNumberDuplicate[0] = true;
                    txtInPhoneNumber.getText().clear();
                    txtInPhoneNumber.requestFocus();
                    textInputLayout.setError("Račun s to telefonsko številko že obstaja");
                } else {
                    textInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { // handling errors
                Notification problemNotification = new Notification(getStringResourceByName("exception"), error.getMessage(), RegisterActivity.this);
                problemNotification.show();
                isPhoneNumberDuplicate[0] = true;
            }
        });

        return isPhoneNumberDuplicate[0];
    }

    protected boolean isValidEmail() {
        TextInputLayout textInputLayout = findViewById(R.id.editTextEmailAddress);
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (txtInEmail.getText().toString().trim().toLowerCase().matches(regex)) {
            textInputLayout.setErrorEnabled(false);
            return true;
        }
        txtInEmail.getText().clear();
        txtInEmail.requestFocus();
        textInputLayout.setError("Potrebno je vnesti vaš e-poštni naslov");
        return false;
    }

    protected boolean isEmailDuplicate() {
        TextInputLayout textInputLayout = findViewById(R.id.editTextEmailAddress);
        final boolean[] isEmailDuplicate = {false};
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance(NetworkConfig.URL_DATABASE).getReference().child("users");
        ref.orderByChild("email").equalTo(txtInEmail.getText().toString().trim().toLowerCase()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { // checks if email already exists in database
                if (dataSnapshot.exists()) {
                    isEmailDuplicate[0] = true;
                    txtInEmail.getText().clear();
                    txtInEmail.requestFocus();
                    textInputLayout.setError("Račun s tem e-poštnim naslovom že obstaja");
                } else {
                    textInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { // handling errors
                Notification problemNotification = new Notification(getStringResourceByName("exception"), error.getMessage(), RegisterActivity.this);
                problemNotification.show();
                isEmailDuplicate[0] = true;
            }
        });

        return isEmailDuplicate[0];
    }

    protected boolean isPasswordStrong() {
        TextInputLayout textInputLayout = findViewById(R.id.editTextPassword);
        TextInputLayout textInputLayout1 = findViewById(R.id.editTextPasswordConfirm);
        if (txtInPsswd.getText().toString().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#*&()–\\[\\]{}_]).{8,20}$")) {
            textInputLayout.setErrorEnabled(false);
            textInputLayout1.setErrorEnabled(false);
            return true;
        }
        txtInPsswd.getText().clear();
        txtInPsswdConfirm.getText().clear();
        txtInPsswd.requestFocus();
        textInputLayout.setError("Geslo ne zadostuje kriterijem (dolžina 8 znakov, vsebuje vsaj 1 veliko črko, 1 številko in 1 poseben znak)");
        textInputLayout.setErrorIconDrawable(null);
        textInputLayout1.setError("Geslo ne zadostuje kriterijem (dolžina 8 znakov, vsebuje vsaj 1 veliko črko, 1 številko in 1 poseben znak)");
        textInputLayout1.setErrorIconDrawable(null);
        return false;
    }

    protected boolean isValidPassword() {
        TextInputLayout textInputLayout = findViewById(R.id.editTextPassword);
        TextInputLayout textInputLayout1 = findViewById(R.id.editTextPasswordConfirm);
        if (txtInPsswd.getText().toString().equals(txtInPsswdConfirm.getText().toString())) {
            textInputLayout.setErrorEnabled(false);
            textInputLayout1.setErrorEnabled(false);
            return true;
        }
        txtInPsswd.getText().clear();
        txtInPsswdConfirm.getText().clear();
        txtInPsswd.requestFocus();
        textInputLayout.setError("Gesli se je ujemata");
        textInputLayout.setErrorIconDrawable(null);
        textInputLayout1.setError("Gesli se ne ujemata");
        textInputLayout1.setErrorIconDrawable(null);
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

    protected void openLoginActivity(String message) {
        User.clearFields();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("message", message);
        startActivity(intent);
    }

    protected void openVerificationActivity() {
        Intent intent = new Intent(this, VerificationActivity.class);
        startActivity(intent);
    }

    private String getStringResourceByName(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }
}