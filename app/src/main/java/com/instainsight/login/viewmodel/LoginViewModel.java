package com.instainsight.login.viewmodel;

import android.app.Activity;
import android.content.Context;

import com.instainsight.instagram.Instagram;
import com.instainsight.instagram.InstagramServices;
import com.instainsight.instagram.InstagramSession;
import com.instainsight.viewmodels.BaseViewModel;
import com.instainsight.viewmodels.IViewModel;

/**
 * Created by SONY on 18-02-2017.
 */

public class LoginViewModel extends BaseViewModel implements IViewModel {

    private InstagramServices instagramServices;
    private Context mContext;
    private InstagramSession mInstagramSession;

    public LoginViewModel(InstagramServices instagramServices, Context context, InstagramSession mInstagramSession) {
        this.instagramServices = instagramServices;
        this.mInstagramSession = mInstagramSession;
        mContext = context;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void setCurrentActivity(Activity value) {

    }

    public void authenticateUser(Instagram mInstagram, Instagram.InstagramAuthListener mAuthListener) {
        if (isConnected())
            mInstagram.authorize(mAuthListener, instagramServices);
//        else
//            Utility.showConnectivitySnack(rltv_connectwithinstagram, isConnected);
    }

}
