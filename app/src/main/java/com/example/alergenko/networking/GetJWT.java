package com.example.alergenko.networking;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.alergenko.LoginActivity;
import com.example.alergenko.RegisterActivity;
import com.example.alergenko.notifications.ProblemNotification;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GetJWT extends AsyncTask<String, Void, JSONObject> {

    private String username;
    private String password;
    private Exception exception;

    public GetJWT(String username, String password) {
        this.username = username;
        this.password = password;
    }

    protected JSONObject doInBackground(String... urls) {
        JSONObject jwt = null;
        try {
            // Setting the connection parameters
            URL url = new URL(urls[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json");

            // creating request body
            String jsonInputString = "{\"username\": \"" + this.username + "\", \"password\": \"" + this.password + "\"}";
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // checking response code
            if (con.getResponseCode() != 200){
                throw new Exception("ERROR: " + con.getResponseCode());
            }

            // reading the response
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null)
                    response.append(responseLine.trim());
                jwt = new JSONObject(response.toString());
            }
        } catch (Exception e) {
            // returns error json object
            try {
                jwt = new JSONObject("{\"error\":\"" + e.getMessage() + "\"}");
            } catch (JSONException je){
                Log.i("ntwerr", je.toString());
            }
        }

        return jwt;
    }
}
