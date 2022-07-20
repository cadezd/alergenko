package com.example.alergenko;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.List;
import java.util.Objects;

public class ScanFragment extends Fragment {

    // View that is used for scanning barcodes
    CompoundBarcodeView barcodeView;
    // Main view
    View view;
    // Notification, custom view for notification, text placeholder for notification
    Toast toastNotification;
    View toastView;
    TextView txtVMessage;
    // Checks if phone is connected to internet
    boolean isConnected = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container == null) {
            return null;
        }

        view = inflater.inflate(R.layout.scan_fragement, container, false);

        barcodeView = view.findViewById(R.id.barcode_scanner);
        barcodeView.setStatusText("");
        barcodeView.decodeContinuous(callbackBarcodeScanner);

        return view;
    }

    @Override
    public void onStart() {
        // starts monitoring network
        checkConnectivity();
        super.onStart();
    }

    @Override
    public void onResume() {
        barcodeView.resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        barcodeView.pause();
        super.onPause();
    }


    // callback function for scanning barcode
    private final BarcodeCallback callbackBarcodeScanner = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null && isConnected) {
                openProductInfoActivity(result.getText());
            } else if (result.getText() != null && !isConnected) {
                toastNotification(getStringResourceByName("no_internet_connection"));
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

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

    // Method to check network connectivity in Main Activity
    private void checkConnectivity() {
        // here we are getting the connectivity service from connectivity manager
        final ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getContext()).getSystemService(Context.CONNECTIVITY_SERVICE);

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

    private void openProductInfoActivity(String barcode) {
        Intent intent = new Intent(getActivity(), ProductInfoActivity.class);
        // gives ProductInfoActivity data obout which fragment to open after it closes
        intent.putExtra("fromFragment", R.id.nav_scan);
        // gives barcode of scanned product
        intent.putExtra("barcode", barcode);
        startActivity(intent);
    }

    @SuppressLint("InflateParams")
    public void toastNotification(String text) {
        if (toastNotification != null) { // makes sure that only one toast message is shown
            toastNotification.cancel();
        }
        toastView = LayoutInflater.from(getContext()).inflate(R.layout.toast_text, null);
        txtVMessage = toastView.findViewById(R.id.txtVMessage);
        txtVMessage.setText(text);
        txtVMessage.setTextColor(Color.BLACK);
        toastNotification = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
        toastNotification.setView(toastView);
        toastNotification.setGravity(Gravity.BOTTOM, 0, 300);
        toastNotification.show();
    }

    private String getStringResourceByName(String aString) {
        String packageName = getActivity().getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }
}
