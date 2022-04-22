package com.example.alergenko;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // set's the layout
        View contentView = inflater.inflate(R.layout.settings_fragement, container, false);

        // DECLARATION AND INICIALIZATION OF COMPONENTS
        Button btnChangeEmail = contentView.findViewById(R.id.btnChangeEmail);

        // CLICK LISTENERS
        // opens edit info activity
        btnChangeEmail.setOnClickListener(view -> openEditInfoActivity());

        return contentView;
    }

    // ADDITIONAL METHODS
    private void openEditInfoActivity(){
        Intent intent = new Intent(getActivity(), EditInfoActivity.class);
        startActivity(intent);
    }

}
