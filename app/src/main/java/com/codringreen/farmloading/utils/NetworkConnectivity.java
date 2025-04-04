package com.codringreen.farmloading.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;

import javax.inject.Inject;

public class NetworkConnectivity {

    private final ConnectivityManager connectivityManager;

    @Inject
    public NetworkConnectivity(Context context) {
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public boolean isNetworkAvailable() {
        if (connectivityManager == null) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) {
                return false;
            }

            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            return capabilities != null &&
                    (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
        } else {
            android.net.NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
    }
}