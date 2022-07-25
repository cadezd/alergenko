package com.example.alergenko;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
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
import com.example.alergenko.notifications.Notification;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    // for checking internet connection
    boolean isConnected;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO: naredi tako da boš podatke o izdelik dobil iz PB

        checkConnectivity();

        View contentView = inflater.inflate(R.layout.history_fragement, container, false);
        ListView listView = contentView.findViewById(R.id.lvHistory);
        ListAdapter adapter = new ListAdapter(getContext(), User.getHistory());
        listView.setAdapter(adapter);

        // opens ProductInfoActivity when user clicks on one element in the list view if it has connection
        listView.setClickable(true);
        listView.setOnItemClickListener((adapterView, view, position, l) -> openProductInfoActivity(User.getHistory(), position));

        return contentView;
    }

    // Method to check network connectivity in Main Activity
    private void checkConnectivity() {
        // here we are getting the connectivity service from connectivity manager
        final ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Getting network Info
        // give Network Access Permission in Manifest
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        // isConnected is a boolean variable
        // here we check if network is connected or is getting connected
        isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

        // network callback to  monitor network
        connectivityManager.registerNetworkCallback(
                new NetworkRequest.Builder()
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        .build(), callbackConnectivity);
    }

    // callback function for checking internet connection
    private final ConnectivityManager.NetworkCallback callbackConnectivity = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            isConnected = true;
        }

        @Override
        public void onLost(Network network) {
            isConnected = false;
        }
    };

    private String getStringResourceByName(String aString) {
        String packageName = getContext().getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }

    private void openProductInfoActivity(ArrayList<Product> products, int position) {
        if (isConnected) {
            Intent intent = new Intent(getActivity(), ProductInfoActivity.class);
            // gives ProductInfoActivity data obout which fragment to open after it closes
            intent.putExtra("fromFragment", R.id.nav_history);
            // gives ProductInfoActivity data about product to display
            intent.putExtra("barcode", products.get(position).getBarcode());
            // start's ProductInfoActivity
            startActivity(intent);
        } else {
            Notification problemNotification = new Notification(getStringResourceByName("exception"), getStringResourceByName("no_internet_connection"), getContext());
            problemNotification.show();
        }
    }
}
