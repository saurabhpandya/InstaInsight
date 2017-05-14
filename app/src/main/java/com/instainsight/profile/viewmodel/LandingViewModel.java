package com.instainsight.profile.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.instainsight.Utils.Utility;
import com.instainsight.instagram.InstagramServices;
import com.instainsight.instagram.InstagramSession;
import com.instainsight.models.ObjectResponseBean;
import com.instainsight.profile.LandingServices;
import com.instainsight.profile.bean.UsersBean;
import com.instainsight.viewmodels.BaseViewModel;
import com.instainsight.viewmodels.IViewModel;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.instainsight.constants.Constants.ADS_DELAY;
import static com.instainsight.constants.Constants.WebFields.TOKEN_ADMOBADS;

/**
 * Created by SONY on 28-03-2017.
 */

public class LandingViewModel extends BaseViewModel implements IViewModel {

    private String TAG = LandingViewModel.class.getSimpleName();
    private Activity mActivity;
    private Context mContext;
    private InstagramSession mInstagramSession;
    private InterstitialAd mInterstitialAd;
    private LandingServices landingServices;
    private Handler handlerForAds;
    private Runnable runnableAds = new Runnable() {
        @Override
        public void run() {
            if (mInstagramSession.isActive())
                showInterstitial();
        }
    };

    public LandingViewModel(InstagramServices instagramServices, LandingServices landingServices,
                            Context context, InstagramSession mInstagramSession) {
        mContext = context;
        this.mInstagramSession = mInstagramSession;
        this.landingServices = landingServices;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void setCurrentActivity(Activity value) {
        mActivity = value;
    }

    public Observable<ObjectResponseBean<UsersBean>> getSelfData() {
        return landingServices.getSelfData(mInstagramSession.getAccessToken());
    }

    public void checkForAdSubscription() {
        if (!Utility.getPurchaseData(mActivity, "remove_ads_1_month_subscription")) {
            getAdMobIds();
        }

    }

    private void getAdMobIds() {
        landingServices.getAdMobAds(TOKEN_ADMOBADS)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String s = response.body();
                        if (null != s && s.contains(",")) {
                            String adUnitId = s.split("\\,")[0];
                            if (null != adUnitId)
                                initAd(adUnitId);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

    }

    private void initAd(String adUnitId) {
        mInterstitialAd = new InterstitialAd(mActivity);
//        App ID: ca-app-pub-3477009218980648~8796752818
//        Ad unit ID: ca-app-pub-3477009218980648/4226952413
        // set the ad unit ID
//        mInterstitialAd.setAdUnitId("ca-app-pub-3477009218980648/4226952413");
        mInterstitialAd.setAdUnitId(adUnitId);

//        AdRequest adRequest = new AdRequest.Builder()
//                .build();

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("F26D9F2D292FFCC31770FE3853CFE277").build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                handlerForAds = new Handler();
                handlerForAds.postDelayed(runnableAds, ADS_DELAY);
            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public void doNotShowAd() {
        if (handlerForAds != null) {
            handlerForAds.removeCallbacks(runnableAds);
            runnableAds = null;
            handlerForAds = null;
        }
        runnableAds = null;
        handlerForAds = null;
    }

}
