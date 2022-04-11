package com.example.alergenko;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // ArrayList of test Products
        // TODO: naredi tako da boš podatke o izdelik dobil iz PB
        // TODO: dodaj še podatke o hranilnih vrednostih
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("Neki", "Neki", "Neki", R.drawable.pic_brownies));
        products.add(new Product("Neki", "Neki", "Neki", R.drawable.pic_brownies));
        products.add(new Product("Neki", "Neki", "Neki", R.drawable.pic_brownies));
        products.add(new Product("Neki", "Neki", "Neki", R.drawable.pic_brownies));

        View contentView = inflater.inflate(R.layout.history_fragement, container, false);
        ListView listView = contentView.findViewById(R.id.lvHistory);
        ListAdapter adapter = new ListAdapter(getContext(), products);
        listView.setAdapter(adapter);

        // opens ProductInfoActivity when user clicks on one element in the list view
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                openProductInfoActivity(products, position);
            }
        });

        return contentView;
    }

    private void openProductInfoActivity(ArrayList<Product> products, int position){
        Intent intent = new Intent(getActivity(), ProductInfoActivity.class);
        // gives ProductInfoActivity data about product to display
        intent.putExtra("productName", products.get(position).getProductName());
        intent.putExtra("allergens", products.get(position).getAllergens());
        intent.putExtra("ingredients", products.get(position).getIngredients());
        // start's ProductInfoActivity
        startActivity(intent);
    }
}
