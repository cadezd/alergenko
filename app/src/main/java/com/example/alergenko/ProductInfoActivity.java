package com.example.alergenko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class ProductInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_info_activity);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // DECLARATION AND INICIALIZATION OF COMPONENTS
        Button btnClose = findViewById(R.id.btnClose);

        // CLICK LISTENERS
        // opens register activty
        Intent intent = getIntent();
        int fragmentToOpen = intent.getIntExtra("fromFragment", R.id.nav_scan);
        btnClose.setOnClickListener(view -> openMainActivity(fragmentToOpen));
    }

    // ADDITIONAL METHODS
    private void openMainActivity(int fragmentToOpen){
        Intent intent = new Intent(ProductInfoActivity.this, MainActivity.class);
        intent.putExtra("fragmentToOpen", fragmentToOpen);
        startActivity(intent);
    }
}