package com.example.alergenko;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.alergenko.entities.Product;
import com.example.alergenko.entities.User;
import com.example.alergenko.networking.GetImage;
import com.example.alergenko.networking.NetworkConfig;
import com.example.alergenko.networking.OnGetDataListener;
import com.example.alergenko.notifications.Notification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_info_activity);
    }

    // DECLARATION OF COMPONENTS
    ScrollView scrollView;
    ProgressBar progressBar;
    TextView txtVProductName;
    ImageView imgVProductImg;
    TextView txtVAllergens;
    TextView txtVIngredients;
    TableLayout tblLyNutritionValues;
    Button btnClose;

    @Override
    protected void onStart() {
        super.onStart();

        // INICIALIZATION OF COMPONENTS
        scrollView = findViewById(R.id.scrollView);
        progressBar = findViewById(R.id.progressBar);
        txtVProductName = findViewById(R.id.txtVProductName);
        imgVProductImg = findViewById(R.id.imgVProductImg);
        txtVAllergens = findViewById(R.id.txtVAllergens);
        txtVIngredients = findViewById(R.id.txtVIngredients);
        tblLyNutritionValues = findViewById(R.id.tblLyNutritionValues);
        btnClose = findViewById(R.id.btnClose);

        // CLICK LISTENERS
        // opens main activty
        Intent intent = getIntent();
        int fragmentToOpen = intent.getIntExtra("fromFragment", R.id.nav_scan);
        btnClose.setOnClickListener(view -> openMainActivity(fragmentToOpen));

        // GETTING AND DISPLAYING PRODUCT DATA
        String barcode = getIntent().getStringExtra("barcode");
        if (fragmentToOpen == R.id.nav_history) {
            showLoadingScreen();    // searches for product in user history
            Product product = readDataFromHistory(barcode);
            clearLoadingScreen();
            displayProductData(product);
        } else {    // searches for product in database
            DatabaseReference databaseReference = FirebaseDatabase.getInstance(NetworkConfig.URL_DATABASE).getReference("products");
            readDataFromDatabse(databaseReference.child(barcode), new OnGetDataListener() {
                @Override
                public void onSuccess(Product dataSnapshotValue) {
                    clearLoadingScreen();
                    User.addProductToUserHistory(dataSnapshotValue);
                    displayProductData(dataSnapshotValue);
                }
            });
        }
    }

    // ADDITIONAL METHODS
    public void readDataFromDatabse(DatabaseReference ref, final OnGetDataListener listener) {
        showLoadingScreen();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // This is how you use the value once it is loaded! Make sure to pass the
                // value of the DataSnapshot object, not the object itself (this was the
                // original answerer's mistake!)
                listener.onSuccess(snapshot.getValue(Product.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Notification problemNotification = new Notification(getStringResourceByName("exception"), error.getMessage(), ProductInfoActivity.this);
                problemNotification.show();
            }
        });
    }

    public Product readDataFromHistory(String barcode) {
        for (Product product : User.getHistory()) {
            if (product.getBarcode().equals(barcode))
                return product;
        }
        return null;
    }

    public Drawable getProductImg(String url) {
        // loads image of a product from web
        try {
            GetImage getImage = new GetImage(this);
            AsyncTask<String, Void, Drawable> response = getImage.execute(url);
            return response.get();
        } catch (Exception e) {
            // loads default (product image not supported)
            return ContextCompat.getDrawable(this, R.drawable.ic_image_not_supported);
        }
    }

    private void displayProductData(Product product) {
        Drawable productImg = getProductImg(product.getMainImageSrc());
        imgVProductImg.setImageDrawable(productImg);
        txtVProductName.setText(product.getName());
        txtVAllergens.setText((product.getAllergens() != null) ? product.getAllergens() + "." : getStringResourceByName("product_does_not_contain_allergens"));
        txtVIngredients.setText(product.getIngredients());

        String[] rows = product.getNutritionValues().split("\\$");
        String[] cols;
        for (int i = 0; i < rows.length; i++) {
            cols = rows[i].split(":");
            tblLyNutritionValues.addView(createTableRow(
                    cols[0],
                    (cols.length - 1 >= 1) ? cols[1] : "",
                    (cols.length - 1 >= 2) ? cols[2] : "",
                    i));
        }
    }

    @SuppressLint("SetTextI18n")
    public TableRow createTableRow(String nutrient, String quantity, String unit, int index) {
        if (index == 0) { // to make header more pretty
            nutrient = quantity;
            quantity = "";
        }

        TableRow tableRow = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
        tableRow.setLayoutParams(lp);

        TableRow.LayoutParams lp1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        TextView txtVNutrient = new TextView(this); // nutrient column
        txtVNutrient.setLayoutParams(lp1);
        txtVNutrient.setPadding(25, 3, 0, 3);
        txtVNutrient.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_medium));
        if (nutrient.contains("—")) // indent
            nutrient = "  " + nutrient;
        if (index == 0) {
            txtVNutrient.setTextSize(16);
            txtVNutrient.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_bold));
        }
        txtVNutrient.setText(nutrient);
        tableRow.addView(txtVNutrient);

        TextView txtVQuantityUnit = new TextView(this); // quantity and unit column
        txtVQuantityUnit.setLayoutParams(lp1);
        txtVQuantityUnit.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        txtVQuantityUnit.setPadding(0, 3, 25, 3);
        txtVQuantityUnit.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_medium));
        txtVQuantityUnit.setText(quantity + " " + unit);
        tableRow.addView(txtVQuantityUnit);

        return tableRow;
    }

    public void showLoadingScreen() {
        scrollView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void clearLoadingScreen() {
        scrollView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private String getStringResourceByName(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }

    private void openMainActivity(int fragmentToOpen) {
        Intent intent = new Intent(ProductInfoActivity.this, MainActivity.class);
        intent.putExtra("fragmentToOpen", fragmentToOpen);
        startActivity(intent);
    }

}