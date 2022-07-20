package com.example.alergenko;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alergenko.entities.User;
import com.example.alergenko.entities.UserHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_activity);
    }


    // DECLARATION OF COMPONENTS
    ImageButton btnBack;
    EditText txtInVerificationNumber1;
    EditText txtInVerificationNumber2;
    EditText txtInVerificationNumber3;
    EditText txtInVerificationNumber4;
    EditText txtInVerificationNumber5;
    EditText txtInVerificationNumber6;
    Button btnSend;

    // VERIFICATION
    String verificationCodeBySystem;

    // REGISTRATION
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onStart() {
        super.onStart();

        // INICIALIZATION OF COMPONENTS
        btnBack = findViewById(R.id.btnBack);
        txtInVerificationNumber1 = findViewById(R.id.txtInVerificationNumber1);
        txtInVerificationNumber2 = findViewById(R.id.txtInVerificationNumber2);
        txtInVerificationNumber3 = findViewById(R.id.txtInVerificationNumber3);
        txtInVerificationNumber4 = findViewById(R.id.txtInVerificationNumber4);
        txtInVerificationNumber5 = findViewById(R.id.txtInVerificationNumber5);
        txtInVerificationNumber6 = findViewById(R.id.txtInVerificationNumber6);
        btnSend = findViewById(R.id.btnSend);

        sendVerificationToUser(User.getPhoneNumber());

        // CLICK LISTENERS
        btnBack.setOnClickListener(view -> openRegisterActivity(null));
        btnSend.setOnClickListener(view -> {
            String verificationCode = "" +
                    txtInVerificationNumber1.getText().toString() +
                    txtInVerificationNumber2.getText().toString() +
                    txtInVerificationNumber3.getText().toString() +
                    txtInVerificationNumber4.getText().toString() +
                    txtInVerificationNumber5.getText().toString() +
                    txtInVerificationNumber6.getText().toString();
            verifyCode(verificationCode);
        });
        connectTextInputs();

    }

    // ADDITIONAL METHODS
    private void sendVerificationToUser(String phoneNumber) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(callbackVerification)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbackVerification = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                // puts verification code in input text fields
                txtInVerificationNumber1.setText(code.substring(0, 0));
                txtInVerificationNumber2.setText(code.substring(1, 1));
                txtInVerificationNumber3.setText(code.substring(2, 2));
                txtInVerificationNumber4.setText(code.substring(3, 3));
                txtInVerificationNumber5.setText(code.substring(4, 4));
                txtInVerificationNumber6.setText(code.substring(5, 5));
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            openRegisterActivity(getStringResourceByName("exception_firebase")); // opens register activty with exception message in intent
        }
    };

    private void verifyCode(String codeByUser) {
        try {
            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
            signTheUserByCredentials(phoneAuthCredential);
        } catch (IllegalArgumentException e) {
            openRegisterActivity(getStringResourceByName("exception_bad_verification_code"));
        } catch (Exception e) {
            openRegisterActivity(e.getMessage());
        }
    }

    private void signTheUserByCredentials(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(VerificationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            registerUser();
                            openLoginActivity(getStringResourceByName("notification_registration_success")); // opens login activity with message in intent
                        } else {
                            openRegisterActivity(task.getException().getMessage()); // opens register activity with exception message in intent
                        }
                    }
                });
    }

    public void registerUser() {
        firebaseDatabase = FirebaseDatabase.getInstance("https://alergenko-user-db-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("users");

        Object userHelper = new UserHelper(
                User.getUserId(),
                User.getFirstName(),
                User.getLastName(),
                User.getEmail(),
                User.getPhoneNumber(),
                User.getPassword()
        );
        databaseReference.child(User.getUserId()).setValue(userHelper);
    }

    private void openRegisterActivity(String exceptionMessage) {
        User.clearFields();
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("exceptionMessage", exceptionMessage);
        startActivity(intent);
    }

    private void openLoginActivity(String message) {
        User.clearFields();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("message", message);
        startActivity(intent);
    }

    private void connectTextInputs() { // makes sure that when you enter a number in one text input field another (next one) text input field is focused
        txtInVerificationNumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txtInVerificationNumber1.getText().toString().length() == 1)
                    txtInVerificationNumber2.requestFocus();
                else if (txtInVerificationNumber1.getText().toString().length() == 0)
                    txtInVerificationNumber1.requestFocus();
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        txtInVerificationNumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txtInVerificationNumber2.getText().toString().length() == 1)
                    txtInVerificationNumber3.requestFocus();
                else if (txtInVerificationNumber2.getText().toString().length() == 0)
                    txtInVerificationNumber1.requestFocus();
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        txtInVerificationNumber3.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txtInVerificationNumber3.getText().toString().length() == 1)
                    txtInVerificationNumber4.requestFocus();
                else if (txtInVerificationNumber3.getText().toString().length() == 0)
                    txtInVerificationNumber2.requestFocus();
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        txtInVerificationNumber4.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txtInVerificationNumber4.getText().toString().length() == 1)
                    txtInVerificationNumber5.requestFocus();
                else if (txtInVerificationNumber4.getText().toString().length() == 0)
                    txtInVerificationNumber3.requestFocus();
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        txtInVerificationNumber5.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txtInVerificationNumber5.getText().toString().length() == 1)
                    txtInVerificationNumber6.requestFocus();
                else if (txtInVerificationNumber5.getText().toString().length() == 0)
                    txtInVerificationNumber4.requestFocus();
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        txtInVerificationNumber6.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txtInVerificationNumber6.getText().toString().length() == 1)
                    txtInVerificationNumber6.requestFocus();
                else if (txtInVerificationNumber6.getText().toString().length() == 0)
                    txtInVerificationNumber5.requestFocus();
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) { // focuses on previous input if the user presses DEL KEY and current input does not have anything in it (onTextChaned  is not triggered)
            if (txtInVerificationNumber6.getText().length() == 0) {
                txtInVerificationNumber6.setText("");
                txtInVerificationNumber5.requestFocus();
            }
            if (txtInVerificationNumber5.getText().length() == 0) {
                txtInVerificationNumber5.setText("");
                txtInVerificationNumber4.requestFocus();
            }
            if (txtInVerificationNumber4.getText().length() == 0) {
                txtInVerificationNumber4.setText("");
                txtInVerificationNumber3.requestFocus();
            }
            if (txtInVerificationNumber3.getText().length() == 0) {
                txtInVerificationNumber3.setText("");
                txtInVerificationNumber2.requestFocus();
            }
            if (txtInVerificationNumber2.getText().length() == 0) {
                txtInVerificationNumber2.setText("");
                txtInVerificationNumber1.requestFocus();
            }
            return true;
        }
        if (txtInVerificationNumber5.getText().length() == 1) {         // focuses on next input if current input already has a number in it
            txtInVerificationNumber6.requestFocus();
            return true;
        }
        if (txtInVerificationNumber4.getText().length() == 1) {
            txtInVerificationNumber5.requestFocus();
            return true;
        }
        if (txtInVerificationNumber3.getText().length() == 1) {
            txtInVerificationNumber4.requestFocus();
            return true;
        }
        if (txtInVerificationNumber2.getText().length() == 1) {
            txtInVerificationNumber3.requestFocus();
            return true;
        }
        if (txtInVerificationNumber1.getText().length() == 1) {
            txtInVerificationNumber2.requestFocus();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private String getStringResourceByName(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }
}