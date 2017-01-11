package com.instainsight;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.instainsight.Utils.ConnectivityReceiver;
import com.instainsight.instagram.Instagram;
import com.instainsight.instagram.InstagramSession;

/**
 * Created by SONY on 25-12-2016.
 */

public class BaseFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    public Instagram mInstagram;
    public InstagramSession mInstagramSession;
    public boolean isConnected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInstagram();
    }

    @Override
    public void onResume() {
        super.onResume();
        InstaInsightApp.getInstance().setConnectivityListener(BaseFragment.this);
    }

    public void initInstagram() {
        mInstagram = InstaInsightApp.getInstagramInstance(getActivity());
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
