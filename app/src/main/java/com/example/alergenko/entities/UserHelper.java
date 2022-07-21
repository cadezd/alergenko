package com.example.alergenko.entities;

import java.util.ArrayList;

public class UserHelper {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private ArrayList<String> barcodes;
    private ArrayList<Boolean> settings;

    public UserHelper(String userId, String firstName, String lastName, String email, String phoneNumber, String password) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.settings = new ArrayList<>();
        settings.add(Boolean.TRUE);
        settings.add(Boolean.TRUE);
        settings.add(Boolean.TRUE);
    }

    public UserHelper(String userId, String firstName, String lastName, String email, String phoneNumber, String password, ArrayList<String> barcodes) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.barcodes = barcodes;
        this.settings = new ArrayList<>();
        settings.add(Boolean.TRUE);
        settings.add(Boolean.TRUE);
        settings.add(Boolean.TRUE);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getBarcodes() {
        return barcodes;
    }

    public void setBarcodes(ArrayList<String> barcodes) {
        this.barcodes = barcodes;
    }

    public ArrayList<Boolean> getSettings() {
        return settings;
    }

    public void setSettings(ArrayList<Boolean> settings) {
        this.settings = settings;
    }

    @Override
    public String toString() {
        return "UserHelper{" +
                "userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", barcodes=" + barcodes +
                ", settings=" + settings +
                '}';
    }
}
