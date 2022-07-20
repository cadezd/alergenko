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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.alergenko.entities.Product;
import com.example.alergenko.networking.GetImage;
import com.example.alergenko.networking.GetProduct;

import org.json.JSONObject;

public class ProductInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_info_activity);
    }

    // DECLARATION OF COMPONENTS
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
        Product product = getProduct(intent.getStringExtra("barcode"));
        displayProductData(product);
    }

    // ADDITIONAL METHODS
    private void openMainActivity(int fragmentToOpen) {
        Intent intent = new Intent(ProductInfoActivity.this, MainActivity.class);
        intent.putExtra("fragmentToOpen", fragmentToOpen);
        startActivity(intent);
    }


    private void displayProductData(Product product) {
        Drawable productImg = getProductImg(product.getMainImageSrc());
        imgVProductImg.setImageDrawable(productImg);
        txtVProductName.setText(product.getName());
        txtVAllergens.setText(product.getAllergens());
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

    private Product getProduct(String barcode) {
        try {
            GetProduct getProduct = new GetProduct(barcode, this);
            AsyncTask<String, Void, JSONObject> response = getProduct.execute();
            JSONObject jsonObject = response.get();

            if (jsonObject == null)
                throw new Exception("Napaka pri povezavi");

            Product product = new Product(
                    (String) jsonObject.get("barcode"),
                    (String) jsonObject.get("itemId"),
                    (String) jsonObject.get("name"),
                    (String) jsonObject.get("brandName"),
                    (String) jsonObject.get("mainImageSrc"),
                    (String) jsonObject.get("allergens"),
                    (String) jsonObject.get("ingredients"),
                    (String) jsonObject.get("nutritionValues"),
                    (String) jsonObject.get("url")
            );

            return product;
        } catch (Exception e) {
            // in case of exceptions oppens MainActivity
            // TODO: dodaj da se odpre nov avitvity z gifom in napisom da produkta ni mogoče dobiti
            Log.i("bala1", e.toString());
            return null;
        }
    }

    public Drawable getProductImg(String url) {
        // loads image of a product from web
        try {
            GetImage getImage = new GetImage(this);
            AsyncTask<String, Void, Drawable> response = getImage.execute(url);
            return response.get();
        } catch (Exception e) {
            // loads default (product image not supported)
            Log.i("bala1", e.toString());
            return ContextCompat.getDrawable(this, R.drawable.ic_image_not_supported);
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
            nutrient = "   " + nutrient;
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
}