package com.instainsight.profile;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.instainsight.BaseActivity;
import com.instainsight.InstaInsightApp;
import com.instainsight.R;
import com.instainsight.Utils.ConnectivityReceiver;

public class LandingActivity extends BaseActivity implements
        ConnectivityReceiver.ConnectivityReceiverListener,
        View.OnClickListener/*, Observer*/ {

    //    private InstaInsightApp instaInsightApp;
    private String TAG = LandingActivity.class.getSimpleName();
    //    private BottomNavigationView bottomNavigationView;
    private ImageView imgvw_paid_user, imgvw_free_user;
    private int mSelectedItem;
    private boolean isConnected;
    private LinearLayout lnrlyt_free_paid_user;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//Do what you need for this SDK
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getWindow().setStatusBarColor(getResources().getColor(R.color.actionbar));

        }
        ;
        getIds();
        regListner();
//        instaInsightApp = (InstaInsightApp) getApplication();
//        instaInsightApp.getUserBeanObserver().addObserver(this);
//        selectFragment(bottomNavigationView.getMenu().getItem(0));
        selectFragment(0);
        initAd();
    }

    private void getIds() {
//        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        imgvw_free_user = (ImageView) findViewById(R.id.imgvw_free_user);
        imgvw_paid_user = (ImageView) findViewById(R.id.imgvw_paid_user);
        lnrlyt_free_paid_user = (LinearLayout) findViewById(R.id.lnrlyt_free_paid_user);
    }

    private void initAd() {
        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId("KnXhHKOr6tHcxTriZlifm3D1YrpsajKLGDop3fdHGcyl525NDzNgRqEC8oXNe98U");

//        AdRequest adRequest = new AdRequest.Builder()
//                .build();

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("F26D9F2D292FFCC31770FE3853CFE277").build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        InstaInsightApp.getInstance().setConnectivityListener(this);
    }

    private void regListner() {

        imgvw_free_user.setOnClickListener(this);
        imgvw_paid_user.setOnClickListener(this);


//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                selectFragment(item);
////                switch (item.getItemId()) {
////                    case R.id.menu_freeuser:
////                        Utility.showToast(getApplicationContext(), "Free User");
////                        break;
////                    case R.id.menu_paiduser:
////                        Utility.showToast(getApplicationContext(), "Paid User");
////                        break;
////                }
//                return true;
//            }
//        });
    }

//    private void selectFragment(MenuItem item) {
//        Fragment frag = null;
//        // init corresponding fragment
//        switch (item.getItemId()) {
//            case R.id.menu_freeuser:
//                frag = new ProfileFragment();
//                break;
//            case R.id.menu_paiduser:
//                frag = new PaidProfileFragment();
//                break;
//        }
//
//        // update selected item
//        mSelectedItem = item.getItemId();
//
//        // uncheck the other items.
//        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
//            MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
//            menuItem.setChecked(menuItem.getItemId() == item.getItemId());
//        }
//
//        updateToolbarText(item.getTitle());
//
//        if (frag != null) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.container, frag, frag.getTag());
//            ft.commit();
//        }
//    }

    private void selectFragment(int fragment) {
        Fragment frag = null;
        // init corresponding fragment
        switch (fragment) {
            case 0:
                imgvw_free_user.setBackgroundResource(R.drawable.freemag);
                imgvw_paid_user.setBackgroundResource(R.drawable.paidgray);
                frag = new ProfileFragment();
                break;
            case 1:
                imgvw_free_user.setBackgroundResource(R.drawable.freegray);
                imgvw_paid_user.setBackgroundResource(R.drawable.paidmag);
                frag = new PaidProfileFragment();
                break;
        }

        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, frag, frag.getTag());
            ft.commit();
        }
    }

//    private void updateToolbarText(CharSequence text) {
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setTitle(text);
//        }
//    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgvw_free_user:
                selectFragment(0);
                break;
            case R.id.imgvw_paid_user:
                selectFragment(1);
                break;
            default:
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    //    @Override
//    public void update(Observable observable, Object o) {
//        if (observable instanceof UsersBean) {
//            UsersBean usersBean = (UsersBean) observable;
//            if (usersBean.isUpdate()) {
//                imgvw_free_user.setEnabled(true);
//                imgvw_paid_user.setEnabled(true);
//                lnrlyt_free_paid_user.setEnabled(true);
//                imgvw_free_user.setAlpha(1.0f);
//                imgvw_paid_user.setAlpha(1.0f);
//            } else {
//                imgvw_free_user.setEnabled(false);
//                imgvw_paid_user.setEnabled(true);
//                lnrlyt_free_paid_user.setEnabled(false);
//                imgvw_free_user.setAlpha(0.5f);
//                imgvw_paid_user.setAlpha(0.5f);
//
//            }
//        }
//    }
}
