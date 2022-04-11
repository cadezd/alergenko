package com.example.alergenko;

public class Product {

    private String productName;
    private String ingredients;
    private String allergens;
    private int image;

    public Product(String productName, String ingredients, String allergens, int image) {
        this.productName = productName;
        this.ingredients = ingredients;
        this.allergens = allergens;
        this.image = image;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getAllergens() {
        return allergens;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
