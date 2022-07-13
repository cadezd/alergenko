package com.example.alergenko.entities;

public class User {

    private static String username;
    private static String password;
    private static String jwt;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        User.username = username;
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
}
