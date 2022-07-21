package com.example.alergenko;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.alergenko.adapters.ListAdapter;
import com.example.alergenko.entities.Product;
import com.example.alergenko.entities.User;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // ArrayList of test Products
        // TODO: naredi tako da boš podatke o izdelik dobil iz PB
        // TODO: dodaj še podatke o hranilnih vrednostih
        //ArrayList<Product> products = User.getHistory();

        View contentView = inflater.inflate(R.layout.history_fragement, container, false);
        ListView listView = contentView.findViewById(R.id.lvHistory);
        ListAdapter adapter = new ListAdapter(getContext(), User.getHistory());
        listView.setAdapter(adapter);

        // opens ProductInfoActivity when user clicks on one element in the list view
        listView.setClickable(true);
        listView.setOnItemClickListener((adapterView, view, position, l) -> openProductInfoActivity(User.getHistory(), position));

        return contentView;
    }

    private void openProductInfoActivity(ArrayList<Product> products, int position){
        Intent intent = new Intent(getActivity(), ProductInfoActivity.class);
        // gives ProductInfoActivity data obout which fragment to open after it closes
        intent.putExtra("fromFragment", R.id.nav_history);
        // gives ProductInfoActivity data about product to display
        intent.putExtra("productName", products.get(position).getName());
        intent.putExtra("allergens", products.get(position).getAllergens());
        intent.putExtra("ingredients", products.get(position).getIngredients());
        // start's ProductInfoActivity
        startActivity(intent);
    }
}
