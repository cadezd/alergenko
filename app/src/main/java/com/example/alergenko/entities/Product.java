package com.example.alergenko.entities;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.example.alergenko.R;
import com.example.alergenko.networking.GetImage;

public class Product {
    private String itemId;
    private String brandName;
    private String nutritionValues;
    private String name;
    private String ingredients;
    private String barcode;
    private String mainImageSrc;
    private String url;
    private String allergens;
    private Drawable image;

    public Product() {
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public Product(String itemId, String brandName, String nutritionValues, String name, String ingredients, String barcode, String mainImageSrc, String url, String allergens) {
        this.barcode = barcode;
        this.itemId = itemId;
        this.name = name;
        this.brandName = brandName;
        this.mainImageSrc = mainImageSrc;
        this.allergens = allergens;
        this.ingredients = ingredients;
        this.nutritionValues = nutritionValues;
        this.url = url;
        this.image = Resources.getSystem().getDrawable(R.drawable.ic_image_not_supported);
        this.fetchImageFromInternet();
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getNutritionValues() {
        return nutritionValues;
    }

    public void setNutritionValues(String nutritionValues) {
        this.nutritionValues = nutritionValues;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getMainImageSrc() {
        return mainImageSrc;
    }

    public void setMainImageSrc(String mainImageSrc) {
        this.mainImageSrc = mainImageSrc;
        this.fetchImageFromInternet();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAllergens() {
        return allergens;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void fetchImageFromInternet() {
        // loads image of a product from web
        try {
            GetImage getImage = new GetImage();
            AsyncTask<String, Void, Drawable> response = getImage.execute(this.mainImageSrc);
            if (response.get() == null)
                throw new Exception("Cannot access image of product on server");
            this.image = response.get();
        } catch (Exception e) {
            // loads default (product image not supported)
            Log.e("napaka", e.toString());
            this.image = Resources.getSystem().getDrawable(R.drawable.ic_image_not_supported);
        }
    }

    public Drawable getImage(){
        return this.image;
    }

    @Override
    public String toString() {
        return "Product{" +
                "itemId='" + itemId + '\'' +
                ", brandName='" + brandName + '\'' +
                ", nutritionValues='" + nutritionValues + '\'' +
                ", name='" + name + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", barcode='" + barcode + '\'' +
                ", mainImageSrc='" + mainImageSrc + '\'' +
                ", url='" + url + '\'' +
                ", allergens='" + allergens + '\'' +
                ", image=" + image +
                '}';
    }
}
