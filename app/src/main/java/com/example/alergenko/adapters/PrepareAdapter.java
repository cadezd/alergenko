package com.example.alergenko.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.alergenko.entities.Product;
import com.example.alergenko.entities.User;

import java.util.ArrayList;

public class PrepareAdapter extends AsyncTask<Void,Void,ListAdapter> {
    ProgressBar progressBar;
    Context context;
    ArrayList<Product> products;
    ListView listView;

    public PrepareAdapter(Context ctx, ListView lstV, ArrayList<Product> prdcts, ProgressBar procb){
        context = ctx;
        listView = lstV;
        products = prdcts;
        progressBar = procb;
    }

    @Override
    public void onPreExecute() {
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public ListAdapter doInBackground(Void... params) {
        ListAdapter adapter = new ListAdapter (context, products);

        return adapter;
    }

    public void onPostExecute(ListAdapter result) {
        listView.setAdapter(result);
        progressBar.setVisibility(View.GONE);
    }
}