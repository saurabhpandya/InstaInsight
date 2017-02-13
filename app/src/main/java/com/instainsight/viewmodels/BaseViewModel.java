package com.instainsight.viewmodels;

import android.os.Handler;

import com.instainsight.Utils.ConnectivityReceiver;

/**
 * Base class for all ViewModels. Biggest reason for this class is that all viewModels need a
 * reference
 * to the UI Thread.
 */
public class BaseViewModel implements ConnectivityReceiver.ConnectivityReceiverListener {
    public boolean isConnected;
    protected Handler mUiThreadHandler;

    public void onCreate(Handler handler) {
        mUiThreadHandler = handler;
    }

    public void onDestroy() {
        mUiThreadHandler = null;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isConnected() {
        return isConnected = ConnectivityReceiver.isConnected();
    }
}
