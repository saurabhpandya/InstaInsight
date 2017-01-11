package com.instainsight;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.instainsight.Utils.ConnectivityReceiver;
import com.instainsight.instagram.Instagram;
import com.instainsight.instagram.InstagramSession;

public class BaseActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    public Instagram mInstagram;
    public InstagramSession mInstagramSession;
    public boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInstagram();
    }

    @Override
    protected void onResume() {
        super.onResume();
        InstaInsightApp.getInstance().setConnectivityListener(BaseActivity.this);
    }

    public void initInstagram() {
        mInstagram = InstaInsightApp.getInstagramInstance(BaseActivity.this);
        mInstagramSession = InstaInsightApp.getmInstagramSession(mInstagram);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isConnected() {
        return isConnected = ConnectivityReceiver.isConnected();
    }
}
