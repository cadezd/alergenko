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
    private static ArrayList<Product> history;

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

    public static ArrayList<Product> getHistory() {
        return history;
    }

    public static void addToHistory(Product product) {
        history.add(0, product);
    }

    public static void clearFields() {
        firstName = "";
        lastName = "";
        email = "";
        phoneNumber = "";
        password = "";
        jwt = "";
        history = null;
    }
}
