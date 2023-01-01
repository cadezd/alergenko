package com.example.alergenko;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ProductNotFoundActivty extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set's the layout
        setContentView(R.layout.product_not_found_activty);
    }

    // DECLARATION OF COMPONENTS
    Button btnClose;

    @Override
    protected void onStart() {
        super.onStart();

        // INICIALIZATION OF COMPONENTS
        btnClose = findViewById(R.id.btnClose);

        // CLICK LISTENERS
        btnClose.setOnClickListener(view -> openMainActivity(R.id.nav_scan));
    }

    // ADDITIONAL METHODS
    private void openMainActivity(int fragmentToOpen) {
        Intent intent = new Intent(ProductNotFoundActivty.this, MainActivity.class);
        intent.putExtra("fragmentToOpen", fragmentToOpen);
        startActivity(intent);
        finishAffinity();
    }

}
