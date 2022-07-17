package com.example.alergenko.networking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;

import com.example.alergenko.R;
import com.example.alergenko.notifications.ProblemNotification;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GetJWT extends AsyncTask<String, Void, JSONObject> {

    private Exception e;
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private final String username;
    private final String password;

    public GetJWT(String username, String password, Context context) {
        this.username = username;
        this.password = password;
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... urls) {
        JSONObject jwt;
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
            if (con.getResponseCode() != 200) {
                throw new NetworkException(con.getResponseCode());
            }

            // reading the response
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null)
                    response.append(responseLine.trim());
                jwt = new JSONObject(response.toString());
            }

            return jwt;
        } catch (ConnectException e) {
            this.e = new ConnectException(getStringResourceByName("no_internet_connection"));
            return null;
        } catch (Exception e) {
            this.e = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        // handling exceptions
        if (this.e != null) {
            ProblemNotification problemNotification = new ProblemNotification(getStringResourceByName("exception"), this.e.getMessage(), context);
            problemNotification.show();
            clearTextInputs(context);
        }
    }

    protected void clearTextInputs(Context context) {
        Activity activity = (Activity) context;
        EditText txtInEmail = activity.findViewById(R.id.txtInEmail);
        EditText txtInPsswd = activity.findViewById(R.id.txtInPsswd);

        txtInEmail.setText("");
        txtInEmail.clearFocus();
        txtInPsswd.setText("");
        txtInPsswd.clearFocus();
    }

    private String getStringResourceByName(String aString) {
        String packageName = ((Activity) context).getPackageName();
        int resId = ((Activity) context).getResources().getIdentifier(aString, "string", packageName);
        return ((Activity) context).getString(resId);
    }
}
