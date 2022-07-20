package com.example.alergenko.networking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.alergenko.R;
import com.example.alergenko.RegisterActivity;
import com.example.alergenko.ScanFragment;
import com.example.alergenko.entities.User;
import com.example.alergenko.notifications.Notification;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@SuppressLint("StaticFieldLeak")
public class GetProduct extends AsyncTask<String, Void, JSONObject> {

    private Exception e;
    private final String barcode;
    private final Context context;

    public GetProduct(String barcode, Context context) {
        this.barcode = barcode;
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... urls) {
        JSONObject product = null;
        try {
            // Setting the connection parameters
            URL url = new URL(NetworkConfig.URL_GET_PRODUCT + this.barcode);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + User.getJwt());

            // checking response code
            if (con.getResponseCode() != 200) {
                throw new Exception("ERROR: " + con.getResponseCode());
            }

            // reading the response
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null)
                    response.append(responseLine.trim());
                product = new JSONObject(response.toString());
            }

            return product;
        } catch (Exception e) {
            this.e = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        // handling exceptions
        if (this.e != null || response == null) {
            Notification problemNotification = new Notification("Napaka", this.e.getMessage(), context);
            problemNotification.show();
            // TODO: dodaj da se odpre nov avitvity z gifom in napisom da produkta ni mogoče dobiti

        }
    }
}
