package com.example.alergenko.networking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.alergenko.R;

import java.io.InputStream;
import java.net.ContentHandler;
import java.net.URL;

public class GetImage extends AsyncTask<String, Void, Drawable> {

    public GetImage(){}

    @SuppressLint("UseCompatLoadingForDrawables")
    protected Drawable doInBackground(String... urls) {
        try {
            // Getting image from internet
            InputStream in = new URL(urls[0]).openStream();
            Drawable drawable = Drawable.createFromStream(in, "img");
            if (drawable == null)
                throw new Exception("Cannot access image of product on server");
            return drawable;
        } catch (Exception e) {
            Log.e("napaka", e.toString());
            return Resources.getSystem().getDrawable(R.drawable.ic_image_not_supported);
        }
    }
}