package com.example.alergenko;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.alergenko.entities.User;
import com.example.alergenko.networking.NetworkConfig;
import com.example.alergenko.notifications.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {

    // DECLARATION OF COMPONENTS
    Button btnChangeData;
    Button btnDeleteAccount;
    SwitchCompat swShowAllergens;
    SwitchCompat swShowIngredients;
    SwitchCompat swShowNutritionValues;
    Button btnLogOut;

    ArrayList<Boolean> originalSettings = new ArrayList<>();

    // for automatic login
    public static final String SHARED_PREFS = "sharedPrefs";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // set's the layout
        View contentView = inflater.inflate(R.layout.settings_fragment, container, false);

        // INICIALIZATION OF COMPONENTS
        btnChangeData = contentView.findViewById(R.id.btnChangeData);
        btnDeleteAccount = contentView.findViewById(R.id.btnDeleteAccount);
        swShowAllergens = contentView.findViewById(R.id.swShowAllergens);
        swShowIngredients = contentView.findViewById(R.id.swShowIngredients);
        swShowNutritionValues = contentView.findViewById(R.id.swShowNutritionValues);
        btnLogOut = contentView.findViewById(R.id.btnLogOut);

        swShowAllergens.setChecked(User.getSettings().get(0));
        swShowIngredients.setChecked(User.getSettings().get(1));
        swShowNutritionValues.setChecked(User.getSettings().get(2));

        originalSettings = copySettings();

        // CLICK LISTENERS
        // opens edit info activity
        btnChangeData.setOnClickListener(view -> openEditInfoActivity());
        btnDeleteAccount.setOnClickListener(view -> deleteAccount());
        btnLogOut.setOnClickListener(view -> logOut());
        swShowAllergens.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b)
                User.getSettings().set(0, Boolean.TRUE);
            else
                User.getSettings().set(0, Boolean.FALSE);
        });
        swShowIngredients.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b)
                User.getSettings().set(1, Boolean.TRUE);
            else
                User.getSettings().set(1, Boolean.FALSE);
        });
        swShowNutritionValues.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b)
                User.getSettings().set(2, Boolean.TRUE);
            else
                User.getSettings().set(2, Boolean.FALSE);
        });

        // displays a message from intent
        if (getActivity().getIntent().getStringExtra("message") != null) {
            String tile = getStringResourceByName("notification");
            String message = getActivity().getIntent().getStringExtra("message");
            getActivity().getIntent().removeExtra("message");
            Notification problemNotification = new Notification(tile, message, getContext());
            problemNotification.show();
        }

        return contentView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!originalSettings.equals(User.getSettings()))
            saveSettingsToDatabase();
    }

    // ADDITIONAL METHODS
    private void deleteAccount() {
        // delets user from Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // deletes user extra data from Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance(NetworkConfig.URL_DATABASE).getReference("users");
        databaseReference.child(user.getUid()).removeValue();

        // clear fields for automatic login
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", null);
        editor.putString("password", null);
        editor.apply();

        user.delete();

        openLoginActivity(getStringResourceByName("notification_account_deleted"));
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        // clear fields for automatic login
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", null);
        editor.putString("password", null);
        editor.apply();
        openLoginActivity(null);
    }

    private void saveSettingsToDatabase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance(NetworkConfig.URL_DATABASE).getReference("users");
        databaseReference.child(user.getUid()).child("settings").setValue(User.getSettings());
    }

    private ArrayList<Boolean> copySettings() {
        ArrayList<Boolean> tmp = new ArrayList<>();
        for (Boolean setting : User.getSettings()) {
            tmp.add((Boolean) setting);
        }
        return tmp;
    }

    private void openEditInfoActivity() {
        Intent intent = new Intent(getActivity(), EditInfoActivity.class);
        startActivity(intent);
    }

    private void openLoginActivity(String message) {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.putExtra("message", message);
        startActivity(intent);
        getActivity().finishAffinity();
    }

    private String getStringResourceByName(String aString) {
        String packageName = getContext().getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }
}
