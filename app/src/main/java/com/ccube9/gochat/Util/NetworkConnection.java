package com.ccube9.gochat.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnection {
    public boolean checkInternetConnection(Context mContext) {
        ConnectivityManager cn = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {
            return true;
        } else {
            return false;
        }
    }
}
