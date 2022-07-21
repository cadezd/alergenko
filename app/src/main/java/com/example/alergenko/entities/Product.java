package com.example.alergenko.entities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.alergenko.R;
import com.example.alergenko.networking.GetImage;

public class Product {

    private String barcode;
    private String itemId;
    private String name;
    private String brandName;
    private String mainImageSrc;
    private String allergens;
    private String ingredients;
    private String nutritionValues;
    private String url;

    public Product() {
    }

    public Product(String barcode, String itemId, String name, String brandName, String mainImageSrc, String allergens, String ingredients, String nutritionValues, String url) {
        this.barcode = barcode;
        this.itemId = itemId;
        this.name = name;
        this.brandName = brandName;
        this.mainImageSrc = mainImageSrc;
        this.allergens = allergens;
        this.ingredients = ingredients;
        this.nutritionValues = nutritionValues;
        this.url = url;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getMainImageSrc() {
        return mainImageSrc;
    }

    public void setMainImageSrc(String mainImageSrc) {
        this.mainImageSrc = mainImageSrc;
    }

    public String getAllergens() {
        return allergens;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getNutritionValues() {
        return nutritionValues;
    }

    public void setNutritionValues(String nutritionValues) {
        this.nutritionValues = nutritionValues;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Drawable getProductImg(Context context) {
        // loads image of a product from web
        try {
            GetImage getImage = new GetImage(context);
            AsyncTask<String, Void, Drawable> response = getImage.execute(this.mainImageSrc);
            return response.get();
        } catch (Exception e) {
            // loads default (product image not supported)
            Log.i("bala1", e.toString());
            return ContextCompat.getDrawable(context, R.drawable.ic_image_not_supported);
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "barcode='" + barcode + '\'' +
                ", itemId='" + itemId + '\'' +
                ", name='" + name + '\'' +
                ", brandName='" + brandName + '\'' +
                ", mainImageSrc='" + mainImageSrc + '\'' +
                ", allergens='" + allergens + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", nutritionValues='" + nutritionValues + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
