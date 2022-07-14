package com.example.alergenko.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.alergenko.R;
import com.example.alergenko.entities.Product;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Product> {
    public ListAdapter(Context context, ArrayList<Product> products) {
        super(context, R.layout.product_card, products);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Product product = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_card, parent, false);
        }

        // INICIALIZATION OF COMPONENTS
        ImageView imgVProductImg = convertView.findViewById(R.id.imgVProductImg);
        TextView txtVProductName = convertView.findViewById(R.id.txtVProductName);
        TextView txtVAllergens = convertView.findViewById(R.id.txtVAllergens);
        TextView txtVIngredients = convertView.findViewById(R.id.txtVIngredients);

        // setting values od components
        //imgVProductImg.setImageResource(product.getMainImageSrc());
        txtVProductName.setText(product.getName());
        txtVAllergens.setText(product.getAllergens());
        txtVIngredients.setText(product.getIngredients());

        return convertView;
    }
}
