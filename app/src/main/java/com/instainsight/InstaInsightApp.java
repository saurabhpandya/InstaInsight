package com.instainsight;

import android.app.Application;
import android.content.Context;

import com.instainsight.Utils.ConnectivityReceiver;
import com.instainsight.instagram.Instagram;
import com.instainsight.instagram.InstagramSession;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

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
//    public UsersBean usersBean;

    public static synchronized InstaInsightApp getInstance() {
        return mInstance;
    }

    public static InstagramSession getmInstagramSession(Instagram mInstagram) {
        mInstagramSession = mInstagram.getSession();
        return mInstagramSession;
    }

    public static Instagram getInstagramInstance(Context context) {
        mInstagram = new Instagram(context, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI);
        return mInstagram;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getResources().getString(R.string.font_roboto_regular))
//                .setDefaultFontPath("OLDENGL.TTF")
                .setFontAttrId(R.attr.fontPath)
                .build());

//        usersBean = new UsersBean();
    }

//    public UsersBean getUserBeanObserver() {
//        return usersBean;
//    }

}
