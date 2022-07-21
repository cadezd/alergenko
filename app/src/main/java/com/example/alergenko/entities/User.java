package com.example.alergenko.entities;

import java.util.ArrayList;

public class User {

    private static String userId;
    private static String firstName;
    private static String lastName;
    private static String email;
    private static String phoneNumber;
    private static String password;
    private static String jwt;
    private static Boolean[] settings = {true, true, true};
    private static ArrayList<String> barcodes = new ArrayList<>();
    private static ArrayList<Product> history = new ArrayList<>();


    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        User.userId = userId;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static void setFirstName(String firstName) {
        User.firstName = firstName;
    }

    public static String getLastName() {
        return lastName;
    }

    public static void setLastName(String lastName) {
        User.lastName = lastName;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        User.email = email;
    }

    public static String getPhoneNumber() {
        return phoneNumber;
    }

    public static void setPhoneNumber(String phoneNumber) {
        User.phoneNumber = phoneNumber;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        User.password = password;
    }

    public static String getJwt() {
        return jwt;
    }

    public static void setJwt(String jwt) {
        User.jwt = jwt;
    }

    public static Boolean[] getSettings() {
        return User.settings;
    }

    public static void setSettings(Boolean[] settings) {
        User.settings = settings;
    }

    public static ArrayList<Product> getHistory() {
        return history;
    }

    public static void setHistory(ArrayList<Product> history) {
        User.history = history;
    }

    public static void addProductToHistory(Product product) {
        User.history.add(0, product);
    }

    public static ArrayList<String> getBarcodes() {
        return barcodes;
    }

    public static void setBarcodes(ArrayList<String> barcodes) {
        User.barcodes = barcodes;
    }

    public static void addBarcodeToBarcodes(String barcode) {
        barcodes.add(0, barcode);
    }


    public static void clearFields() {
        userId = null;
        firstName = null;
        lastName = null;
        email = null;
        phoneNumber = null;
        password = null;
        jwt = null;
        settings = new Boolean[]{true, true, true};
        history = null;
    }
}
