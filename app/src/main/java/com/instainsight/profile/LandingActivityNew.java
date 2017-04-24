package com.instainsight.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.instainsight.InstaInsightApp;
import com.instainsight.R;
import com.instainsight.Utils.Utility;
import com.instainsight.ViewModelActivity;
import com.instainsight.databinding.ActivityLandingNewBinding;
import com.instainsight.db.DBQueries;
import com.instainsight.db.tables.LIKEDBYUSER;
import com.instainsight.db.tables.MEDIA;
import com.instainsight.db.tables.PROFILEVIEWER;
import com.instainsight.db.tables.RECENTMEDIA;
import com.instainsight.db.tables.USERS;
import com.instainsight.db.tables.USERSELF;
import com.instainsight.login.LoginActivity;
import com.instainsight.models.ObjectResponseBean;
import com.instainsight.profile.bean.UsersBean;
import com.instainsight.profile.viewmodel.LandingViewModel;
import com.instainsight.upgradetopro.UpgradeToProActivity;
import com.instainsight.util.IabBroadcastReceiver;
import com.instainsight.util.IabHelper;
import com.instainsight.util.IabResult;
import com.instainsight.util.Inventory;
import com.instainsight.util.Purchase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import static com.instainsight.constants.Constants.SKU_REMOVE_ADS_MONTHLY;
import static com.instainsight.db.DatabaseHelper.TABLE_FOLLOWERS;
import static com.instainsight.db.DatabaseHelper.TABLE_FOLLOWING;
import static com.instainsight.db.DatabaseHelper.TABLE_LIKEDBYUSER;
import static com.instainsight.db.DatabaseHelper.TABLE_MEDIA;
import static com.instainsight.db.DatabaseHelper.TABLE_USERS;

public class LandingActivityNew extends ViewModelActivity implements IabBroadcastReceiver.IabBroadcastListener, DialogInterface.OnClickListener {

    @Inject
    LandingViewModel landingViewModel;
    private String TAG = LandingActivityNew.class.getSimpleName();
    private ActivityLandingNewBinding landingNewBinding;

    // Does the user have an active subscription 1 month ads free plan?
    private boolean mSubscribedTo1MonthAdFree = false;
    // The helper object
    private IabHelper mHelper;
    // Provides purchase notification while this app is running
    private IabBroadcastReceiver mBroadcastReceiver;
    // Will the subscription auto-renew?
    private boolean mAutoRenewEnabled = false;
    // Tracks the currently owned 1 month ads free SKU, and the options in the Manage dialog
    private String m1MonthAdsFree = "";
    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
//                setWaitScreen(false);
                landingNewBinding.imgvwBuy.setVisibility(View.VISIBLE);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
//                setWaitScreen(false);
                landingNewBinding.imgvwBuy.setVisibility(View.VISIBLE);
                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(SKU_REMOVE_ADS_MONTHLY)) {
                // bought the infinite gas subscription
                Log.d(TAG, "1 month ads free subscription purchased.");
                alert("Thank you for subscribing to 1 month ads free plan!");
                mSubscribedTo1MonthAdFree = true;
                mAutoRenewEnabled = purchase.isAutoRenewing();
                m1MonthAdsFree = purchase.getSku();
                landingNewBinding.imgvwBuy.setVisibility(View.GONE);
                Utility.setPurchaseData(LandingActivityNew.this, SKU_REMOVE_ADS_MONTHLY, mSubscribedTo1MonthAdFree);
//                mTank = TANK_MAX;
//                updateUi();
//                setWaitScreen(false);
            }
        }
    };
    private String mRemoveAdsMonthly;
    private int vpPos = 0;
    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // First find out which subscription is auto renewing
            Purchase gasMonthly = inventory.getPurchase(SKU_REMOVE_ADS_MONTHLY);
            if (gasMonthly != null && gasMonthly.isAutoRenewing()) {
                mRemoveAdsMonthly = SKU_REMOVE_ADS_MONTHLY;
                mAutoRenewEnabled = true;
            }

            // The user is subscribed if either subscription exists, even if neither is auto
            // renewing
            mSubscribedTo1MonthAdFree = (gasMonthly != null && verifyDeveloperPayload(gasMonthly));
            Log.d(TAG, "User " + (mSubscribedTo1MonthAdFree ? "HAS" : "DOES NOT HAVE")
                    + " one month subscription.");
            if (mSubscribedTo1MonthAdFree) {
                landingNewBinding.imgvwBuy.setVisibility(View.INVISIBLE);
                Utility.setPurchaseData(LandingActivityNew.this, SKU_REMOVE_ADS_MONTHLY, mSubscribedTo1MonthAdFree);
            } else {
                if (vpPos != 0)
                    landingNewBinding.imgvwBuy.setVisibility(View.VISIBLE);
                Utility.setPurchaseData(LandingActivityNew.this, SKU_REMOVE_ADS_MONTHLY, mSubscribedTo1MonthAdFree);
            }

            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }
    }

    void complain(String message) {
        Log.e(TAG, "**** : " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(LandingActivityNew.this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    /**
     * Verifies the developer payload of a purchase.
     */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((InstaInsightApp) getApplication()).getComponent().inject(LandingActivityNew.this);
        super.onCreate(savedInstanceState);
        landingNewBinding = DataBindingUtil.setContentView(this, R.layout.activity_landing_new);
        landingNewBinding.setLandingViewModel(landingViewModel);
        createViewPager(landingNewBinding.vpLanding);
        setViewPagerListner();
        landingNewBinding.imgvwBuy.setVisibility(View.GONE);
        initInAppPurchase();
        checkForAdSubscription();
        getSelfData();
    }

    private void setViewPagerListner() {
        landingNewBinding.vpLanding.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                vpPos = position;
                if (position == 1)
                    landingNewBinding.imgvwBuy.setVisibility(View.VISIBLE);
                else
                    landingNewBinding.imgvwBuy.setVisibility(View.GONE);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void checkForAdSubscription() {
        landingViewModel.checkForAdSubscription();
    }

    private void getSelfData() {

        landingViewModel.getSelfData()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        landingNewBinding.prgrsLanding.setVisibility(View.VISIBLE);
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        landingNewBinding.prgrsLanding.setVisibility(View.GONE);
                    }
                })
                .subscribe(new Consumer<ObjectResponseBean<UsersBean>>() {
                    @Override
                    public void accept(ObjectResponseBean<UsersBean> usersBean) throws Exception {
                        UsersBean userSelfBean = usersBean.getData();
                        landingNewBinding.txtvwProfilename.setText(userSelfBean.getUserFullName());
                        landingNewBinding.txtvwFollowercount.setText(userSelfBean.getUserCountBean().getFollowed_by());
                        landingNewBinding.txtvwFollowingcount.setText(userSelfBean.getUserCountBean().getFollows());
                        Glide.with(LandingActivityNew.this).load(userSelfBean.getProfilePic()).placeholder(R.drawable.defaultpic)
                                .dontAnimate().into(landingNewBinding.imgvwProfilepic);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    protected void createViewModel() {
        mViewModel = landingViewModel;
        landingViewModel.setCurrentActivity(LandingActivityNew.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        InstaInsightApp.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }

    private void createViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ProfileFragment(), "Profile");
        adapter.addFrag(new PaidProfileFragment(), "Pro Profile");
        viewPager.setAdapter(adapter);
        landingNewBinding.tabsLanding.setupWithViewPager(landingNewBinding.vpLanding);
        createTabIcons();
    }

    private void createTabIcons() {

        ImageView tabFree = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFree.setImageResource(R.drawable.free);
        landingNewBinding.tabsLanding.getTabAt(0).setCustomView(tabFree);

        ImageView tabPro = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabPro.setImageResource(R.drawable.pro);
        landingNewBinding.tabsLanding.getTabAt(1).setCustomView(tabPro);
    }

    private void initInAppPurchase() {
        /* base64EncodedPublicKey should be YOUR APPLICATION'S PUBLIC KEY
         * (that you got from the Google Play developer console). This is not your
         * developer public key, it's the *app-specific* public key.
         *
         * Instead of just storing the entire literal string here embedded in the
         * program,  construct the key at runtime from pieces or
         * use bit manipulation (for example, XOR with some other string) to hide
         * the actual key.  The key itself is not secret information, but we don't
         * want to make it easy for an attacker to replace the public key with one
         * of their own and then fake messages from the server.
         */
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjMBYres8lMU0" +
                "mHP6MvSpTp7cIr06c1yy+l2bjF0f48ZjntNkaLYSdaU1U8sri3eDL3pRi6Jz+4uQltw0p8Mu/v/NZ3ZpEU" +
                "8HZdqDf0V0qazmkNP8qHfrmP8kAcjjh5kE44Un1MfG3tLaSGQx6fLMETRvOLBkdj/n9TK397nDvx/2lqhi" +
                "Rd2i0An1mEkwvuEtIJ3jHjwyQVMrktd/8hOimLCPDJNLz15r+Lt4SMzntshLUafhNLQeMaVnAbMTwqxiZ" +
                "Er+eFribwU9FZ8oFArZhnpr37s6wyX3/uHfDM1P6aDNMeHmNNWGvbYcIksFCmVaD8SjBCy7RXCLrOIbjbwK/QIDAQAB";
        // Some sanity checks to see if the developer (that's you!) really followed the
        // instructions to run this sample (don't put these checks on your app!)
        if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) {
            throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
        }
        if (getPackageName().startsWith("com.example")) {
            throw new RuntimeException("Please change the sample's package name! See README.");
        }

        // Create the helper, passing it our context and the public key to verify signatures with
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Important: Dynamically register for broadcast messages about updated purchases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
                mBroadcastReceiver = new IabBroadcastReceiver(LandingActivityNew.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error querying inventory. Another async operation in progress.");
                }
            }
        });

    }

    public void openInAppPurchase(View v) {
        startActivity(new Intent(this, UpgradeToProActivity.class));
////        Utility.showToast(mContext, "Work in progress");
//
//        /* TODO: for security, generate your payload here for verification. See the comments on
//             *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
//             *        an empty string, but on a production app you should carefully generate
//             *        this. */
//        String payload = "";
//
//
//        List<String> oldSkus = null;
//        if (!TextUtils.isEmpty(m1MonthAdsFree)) {
//            // The user currently has a valid subscription, any purchase action is going to
//            // replace that subscription
//            oldSkus = new ArrayList<String>();
//            oldSkus.add(m1MonthAdsFree);
//        }
//
////        setWaitScreen(true);
//        Log.d(TAG, "Launching purchase flow for gas subscription.");
//        try {
//            mHelper.flagEndAsync();
//            mHelper.launchPurchaseFlow(LandingActivityNew.this, SKU_REMOVE_ADS_MONTHLY, IabHelper.ITEM_TYPE_SUBS,
//                    oldSkus, RC_REQUEST, mPurchaseFinishedListener, payload);
//        } catch (IabHelper.IabAsyncInProgressException e) {
//            complain("Error launching purchase flow. Another async operation in progress.");
////            setWaitScreen(false);
//        }
//        // Reset the dialog options
////        mSelectedSubscriptionPeriod = "";
////        mFirstChoiceSku = "";
////        mSecondChoiceSku = "";

    }

    private void launchLogoutProcess() {
        DBQueries dbQueries = new DBQueries(getApplicationContext());
        if (dbQueries.isTableExists(TABLE_MEDIA))
            dbQueries.deleteTable(TABLE_MEDIA);
        if (dbQueries.isTableExists(TABLE_FOLLOWERS))
            dbQueries.deleteTable(TABLE_FOLLOWERS);
        if (dbQueries.isTableExists(TABLE_FOLLOWING))
            dbQueries.deleteTable(TABLE_FOLLOWING);
        if (dbQueries.isTableExists(TABLE_LIKEDBYUSER))
            dbQueries.deleteTable(TABLE_LIKEDBYUSER);
        if (dbQueries.isTableExists(TABLE_USERS))
            dbQueries.deleteTable(TABLE_USERS);
        if (dbQueries.isTableExists(PROFILEVIEWER.TABLE_NAME))
            dbQueries.deleteTable(PROFILEVIEWER.TABLE_NAME);
        if (dbQueries.isTableExists(LIKEDBYUSER.TABLE_NAME))
            dbQueries.deleteTable(LIKEDBYUSER.TABLE_NAME);
        if (dbQueries.isTableExists(RECENTMEDIA.TABLE_NAME))
            dbQueries.deleteTable(RECENTMEDIA.TABLE_NAME);
        if (dbQueries.isTableExists(USERSELF.TABLE_NAME))
            dbQueries.deleteTable(USERSELF.TABLE_NAME);
        if (dbQueries.isTableExists(USERS.TABLE_NAME))
            dbQueries.deleteTable(USERS.TABLE_NAME);
        if (dbQueries.isTableExists(MEDIA.TABLE_NAME))
            dbQueries.deleteTable(MEDIA.TABLE_NAME);
        Utility.clearSharedPreference(getApplicationContext());
        mInstagramSession.reset();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void logOut(View v) {

        AlertDialog.Builder alrtDlgLogout = new AlertDialog.Builder(LandingActivityNew.this);
        alrtDlgLogout.setTitle("Log Out");
        alrtDlgLogout.setMessage("Are you sure, you want to logout?");
        alrtDlgLogout.setPositiveButton("Yes", this);
        alrtDlgLogout.setNegativeButton("Cancel", this);
        alrtDlgLogout.create().show();

    }

    @Override
    public void receivedBroadcast() {
// Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error querying inventory. Another async operation in progress.");
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int id) {
        switch (id) {
            case DialogInterface.BUTTON_POSITIVE:
                launchLogoutProcess();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                break;
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
