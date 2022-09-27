package com.example.alergenko.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.alergenko.R;
import com.example.alergenko.entities.Product;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter implements Filterable {

    Context context;
    ArrayList<Product> products, productsTmp;
    CustomFilter customFilter;

    public ListAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
        this.productsTmp = products;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Product product = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.product_card, parent, false);
        }

        //Log.i("bala", product.toString());

        // DECLARATION AND INICIALIZATION OF COMPONENTS
        ImageView imgVProductImg = convertView.findViewById(R.id.imgVProductImg);
        TextView txtVProductName = convertView.findViewById(R.id.txtVProductName);

        // setting values of components
        imgVProductImg.setImageDrawable(product.getImage());
        txtVProductName.setText(product.getName());

        return convertView;
    }

    @Override
    public int getCount() {
        return this.products.size();
    }

    @Override
    public Product getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public ArrayList<Product> getProducts(){
        return this.products;
    }

    @Override
    public Filter getFilter() {
        if (customFilter == null) {
            customFilter = new CustomFilter();
        }

        return customFilter;
    }

    class CustomFilter extends Filter { // Filter object that performs filtering on list of products
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                // performing search
                constraint = constraint.toString().toLowerCase();
                ArrayList<Product> filteredProducts = new ArrayList<>();
                Product product;
                for (int i = 0; i < ListAdapter.this.products.size(); i++) {
                    product = ListAdapter.this.products.get(i);
                    if (product.getName().toLowerCase().contains(constraint.toString())) {
                        filteredProducts.add(product);
                    }
                }
                results.count = filteredProducts.size();
                results.values = filteredProducts;
            } else {
                results.count = productsTmp.size();
                results.values = productsTmp;
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ListAdapter.this.products = (ArrayList<Product>) results.values;
            notifyDataSetChanged();
        }
    }
}


