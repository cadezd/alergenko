package com.example.alergenko.entities;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class UserHelper {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;
    private final ArrayList<Boolean> settings = new ArrayList<>();
    private final ArrayList<Product> history = new ArrayList<>();


    public UserHelper(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.settings.add(Boolean.TRUE);
        this.settings.add(Boolean.TRUE);
        this.settings.add(Boolean.TRUE);
    }

    @NonNull
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
