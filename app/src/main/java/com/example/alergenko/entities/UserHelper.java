package com.example.alergenko.entities;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class UserHelper {
    public final String firstName;
    public final String lastName;
    public final String email;
    public final String phoneNumber;
    public final ArrayList<Boolean> settings = new ArrayList<>();
    public final ArrayList<Product> history = new ArrayList<>();


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
