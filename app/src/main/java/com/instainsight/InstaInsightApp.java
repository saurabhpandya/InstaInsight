package com.instainsight;

import android.app.Application;
import android.content.Context;

import com.instainsight.Utils.ConnectivityReceiver;
import com.instainsight.instagram.Instagram;
import com.instainsight.instagram.InstagramSession;

import static com.instainsight.constants.Constants.CLIENT_ID;
import static com.instainsight.constants.Constants.CLIENT_SECRET;
import static com.instainsight.constants.Constants.REDIRECT_URI;

/**
 * Created by SONY on 17-12-2016.
 */

public class InstaInsightApp extends Application {

    private static InstaInsightApp mInstance;

    private static InstagramSession mInstagramSession;
    private static Instagram mInstagram;

    public static synchronized InstaInsightApp getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    public static InstagramSession getmInstagramSession(Instagram mInstagram) {
        mInstagramSession = mInstagram.getSession();
        return mInstagramSession;
    }

    public static Instagram getInstagramInstance(Context context) {
        mInstagram = new Instagram(context, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI);
        return mInstagram;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

}
