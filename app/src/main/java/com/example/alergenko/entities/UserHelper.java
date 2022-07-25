package com.example.alergenko.entities;

import java.util.ArrayList;

public class UserHelper {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private ArrayList<Boolean> settings = new ArrayList<>();
    private ArrayList<Product> history = new ArrayList<>();

    public UserHelper(String userId, String firstName, String lastName, String email, String phoneNumber, String password) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.settings.add(Boolean.TRUE);
        this.settings.add(Boolean.TRUE);
        this.settings.add(Boolean.TRUE);
    }

    public UserHelper(String userId, String firstName, String lastName, String email, String phoneNumber, String password, ArrayList<Boolean> settings, ArrayList<Product> history) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.settings = settings;
        this.history = history;
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

    public ArrayList<Boolean> getSettings() {
        return settings;
    }

    public void setSettings(ArrayList<Boolean> settings) {
        this.settings = settings;
    }

    public ArrayList<Product> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<Product> history) {
        this.history = history;
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
                ", settings=" + settings +
                ", history=" + history +
                '}';
    }
}
