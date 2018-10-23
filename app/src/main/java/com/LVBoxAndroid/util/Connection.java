package com.LVBoxAndroid.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connection {
    public static boolean verifyNet(Context contexto){

        ConnectivityManager cm = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if ( (netInfo != null) && (netInfo.isConnectedOrConnecting()) && (netInfo.isAvailable()) )
            return true;
        else {
            return false;
        }
    }
}
