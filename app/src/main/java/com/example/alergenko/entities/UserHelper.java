package com.example.alergenko.entities;

import java.util.ArrayList;
import java.util.HashMap;

public class UserHelper {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private ArrayList<Boolean> settings = new ArrayList<>();
    private ArrayList<Product> history = new ArrayList<>();

    public UserHelper() {
    }

    public UserHelper(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.settings.add(Boolean.TRUE);
        this.settings.add(Boolean.TRUE);
        this.settings.add(Boolean.TRUE);
    }

    public UserHelper(String firstName, String lastName, String email, String phoneNumber, ArrayList<Boolean> settings) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.settings = settings;
    }

    public UserHelper(String firstName, String lastName, String email, String phoneNumber, ArrayList<Boolean> settings, HashMap<String, ArrayList<Product>> history) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.settings = settings;
        this.history = history.get(0);
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
                " firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", settings=" + settings +
                ", history=" + history +
                '}';
    }
}
