package com.example.alergenko.networking;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.alergenko.R;

import java.io.InputStream;
import java.net.URL;

public class GetImage extends AsyncTask<String, Void, Drawable> {

    private Context context;

    public GetImage(Context context) {
        this.context = context;
    }

    protected Drawable doInBackground(String... urls) {
        try {
            InputStream in = new URL(urls[0]).openStream();
            Drawable drawable = Drawable.createFromStream(in, "img");
            if (drawable == null)
                throw new Exception("Cannot access image of product on server");

            return drawable;
        } catch (Exception e) {
            Log.i("bala1", e.toString());
            return ContextCompat.getDrawable(context, R.drawable.ic_image_not_supported);
        }
    }

}