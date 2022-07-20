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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

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
        btnRegister = findViewById(R.id.btnRegister);

        // CLICK LISTENERS
        btnBack.setOnClickListener(view -> openLoginActivity());
        btnRegister.setOnClickListener(view -> register());

        if (getIntent().getStringExtra("exceptionMessage") != null) { // displays error message if it comes from verification activty
            String tile = getStringResourceByName("exception");
            String message = getIntent().getStringExtra("exceptionMessage");
            Notification problemNotification = new Notification(tile, message, this);
            problemNotification.show();
        }
    }

    // ADDITIONAL METHODS
    protected void register() {

        // notifies user that there is no internet connection
        if (!isConnected) {
            Notification problemNotification = new Notification(getStringResourceByName("exception"), getStringResourceByName("no_internet_connection"), this);
            problemNotification.show();
        }

        // checking if entered data is valid
        if (!isValidFirstName() || !isValidLastName() || !isValidPhoneNumber() || !isPhoneNumberDuplicate() || !isValidEmail() || !isEmailDuplicate() || !isPasswordStrong() || !isValidPassword())
            return;

        // collecting enterd data
        User.setFirstName(txtInFirstName.getText().toString());
        User.setLastName(txtInLastName.getText().toString());
        User.setPhoneNumber("+386" + txtInPhoneNumber.getText().toString());
        User.setEmail(txtInEmail.getText().toString());
        String password = txtInPsswd.getText().toString();
        User.setPassword(hashSha1(User.getEmail() + password));
        User.setUserId(hashSha1(User.getEmail() + password));

        // opening VerificationActivty
        openVerifivationActivity(User.getPhoneNumber());
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
        textInputLayout.setError("Potrebno je vnesti vašo telefonsko številko" + txtInPhoneNumber.getText().toString());
        return false;
    }

    protected boolean isPhoneNumberDuplicate() {
        TextInputLayout textInputLayout = findViewById(R.id.editTextPhoneNumber);
        final boolean[] isPhoneNumberDuplicate = {false};
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
                Notification problemNotification = new Notification(getStringResourceByName("excpetion"), error.getMessage(), RegisterActivity.this);
                problemNotification.show();
                isPhoneNumberDuplicate[0] = true;
            }
        });

        return isPhoneNumberDuplicate[0];
    }

    protected boolean isValidEmail() {
        TextInputLayout textInputLayout = findViewById(R.id.editTextEmailAddress);
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!txtInEmail.getText().toString().isEmpty() && txtInEmail.getText().toString().matches(regex)) {
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
        DatabaseReference ref = FirebaseDatabase.getInstance(NetworkConfig.URL_DATABASE).getReference().child("users");
        ref.orderByChild("email").equalTo(txtInEmail.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                Notification problemNotification = new Notification(getStringResourceByName("excpetion"), error.getMessage(), RegisterActivity.this);
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
        textInputLayout1.setError("Geslo ne zadostuje kriterijem (dolžina 8 znakov, vsebuje vsaj 1 veliko črko, 1 številko in 1 poseben znak)");
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
        textInputLayout1.setError("Gesli se ne ujemata");
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

    protected void openVerifivationActivity(String phoneNumber) {
        Intent intent = new Intent(this, VerificationActivity.class);
        intent.putExtra("phoneNumber", phoneNumber);
        startActivity(intent);
    }

    protected void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private String getStringResourceByName(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }
}