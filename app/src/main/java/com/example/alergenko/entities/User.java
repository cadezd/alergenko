package com.example.alergenko.entities;

import java.util.ArrayList;

public class User {

    private static String firstName;
    private static String lastName;
    private static String email;
    private static String phoneNumber;
    private static ArrayList<Boolean> settings = new ArrayList<>();
    private static ArrayList<Product> history = new ArrayList<>(0);


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

    public static ArrayList<Boolean> getSettings() {
        return settings;
    }

    public static void setSettings(ArrayList<Boolean> settings) {
        User.settings = settings;
    }

    public static ArrayList<Product> getHistory() {
        return history;
    }

    public static void setHistory(ArrayList<Product> history) {
        User.history = history;
    }

    public static void addProductToUserHistory(Product product){
        User.history.add(0, product);
    }

    public static void clearFields() {
        firstName = null;
        lastName = null;
        email = null;
        phoneNumber = null;
        settings = new ArrayList<>();
        settings.add(Boolean.TRUE);
        settings.add(Boolean.TRUE);
        settings.add(Boolean.TRUE);
        history = new ArrayList<>(0);
    }
}
