package com.instainsight.profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.instainsight.BaseFragment;
import com.instainsight.R;
import com.instainsight.Utils.Utility;
import com.instainsight.constants.Constants;
import com.instainsight.followersing.followers.FollowersActivity;
import com.instainsight.followersing.following.FollowingActivity;
import com.instainsight.ghostfollowers.GhostFollowersActivity;
import com.instainsight.ilikedmost.ILikedMostActivity;
import com.instainsight.instagram.InstagramRequest;
import com.instainsight.instagram.InstagramUser;
import com.instainsight.login.LoginActivity;
import com.instainsight.mostpopularfollowers.MostPopularFollowersActivity;
import com.instainsight.mytoplikers.MyTopLikersActivity;
import com.instainsight.profile.bean.UsersBean;
import com.instainsight.profile.dao.UsersDao;
import com.instainsight.util.IabBroadcastReceiver;
import com.instainsight.util.IabHelper;
import com.instainsight.util.IabResult;
import com.instainsight.util.Inventory;
import com.instainsight.util.Purchase;
import com.instainsight.whoviewedprofile.WhoViewedProfileActivity;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

import static com.instainsight.constants.Constants.INAPP_BASE64KEY;
import static com.instainsight.constants.Constants.INAPP_GHOST_FOLLOWERS;
import static com.instainsight.constants.Constants.INAPP_GHOST_FOLLOWERS_HALLFYEARLY;
import static com.instainsight.constants.Constants.INAPP_GHOST_FOLLOWERS_MONTHLY;
import static com.instainsight.constants.Constants.INAPP_GHOST_FOLLOWERS_YEARLY;
import static com.instainsight.constants.Constants.INAPP_MOST_POPULAR_FOLLOWERS;
import static com.instainsight.constants.Constants.INAPP_MOST_POPULAR_FOLLOWERS_HALLFYEARLY;
import static com.instainsight.constants.Constants.INAPP_MOST_POPULAR_FOLLOWERS_MONTHLY;
import static com.instainsight.constants.Constants.INAPP_MOST_POPULAR_FOLLOWERS_YEARLY;
import static com.instainsight.constants.Constants.INAPP_MY_TOP_LIKERS;
import static com.instainsight.constants.Constants.INAPP_MY_TOP_LIKERS_HALLFYEARLY;
import static com.instainsight.constants.Constants.INAPP_MY_TOP_LIKERS_MONTHLY;
import static com.instainsight.constants.Constants.INAPP_MY_TOP_LIKERS_YEARLY;
import static com.instainsight.constants.Constants.INAPP_PROFILE_VIEWER;
import static com.instainsight.constants.Constants.INAPP_PROFILE_VIEWER_HALLFYEARLY;
import static com.instainsight.constants.Constants.INAPP_PROFILE_VIEWER_MONTHLY;
import static com.instainsight.constants.Constants.INAPP_PROFILE_VIEWER_YEARLY;
import static com.instainsight.constants.Constants.INAPP_REMOVE_ADS;
import static com.instainsight.constants.Constants.INAPP_REMOVE_ADS_HALLFYEARLY;
import static com.instainsight.constants.Constants.INAPP_REMOVE_ADS_MONTHLY;
import static com.instainsight.constants.Constants.INAPP_REMOVE_ADS_YEARLY;
import static com.instainsight.constants.Constants.INAPP_UPGRADE_TO_PRO;
import static com.instainsight.constants.Constants.INAPP_UPGRADE_TO_PRO_HALLFYEARLY;
import static com.instainsight.constants.Constants.INAPP_UPGRADE_TO_PRO_MONTHLY;
import static com.instainsight.constants.Constants.INAPP_UPGRADE_TO_PRO_YEARLY;
import static com.instainsight.constants.Constants.INAPP_WHO_I_LIKED_MOST;
import static com.instainsight.constants.Constants.INAPP_WHO_I_LIKED_MOST_HALLFYEARLY;
import static com.instainsight.constants.Constants.INAPP_WHO_I_LIKED_MOST_MONTHLY;
import static com.instainsight.constants.Constants.INAPP_WHO_I_LIKED_MOST_YEARLY;
import static com.instainsight.constants.Constants.RC_REQUEST;
import static com.instainsight.constants.Constants.SKU_REMOVE_ADS_MONTHLY;

/**
 * Created by SONY on 17-12-2016.
 */

public class PaidProfileFragment extends BaseFragment implements View.OnClickListener,
        IabBroadcastReceiver.IabBroadcastListener, DialogInterface.OnClickListener {


    private String TAG = PaidProfileFragment.class.getSimpleName();
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
    private String mRemoveAdsMonthly;

    private boolean hasPrflStlkrMonthlySub;
    private boolean hasPrflStlkrAutoRenewMonthlySub;
    private boolean hasPrflStlkrHalfYearlySub;
    private boolean hasPrflStlkrAutoRenewHalfYearlySub;
    private boolean hasPrflStlkrYearlySub;
    private boolean hasPrflStlkrAutoRenewYearlySub;

    private boolean hasMyTopLikersMonthlySub;
    private boolean hasMyTopLikersAutoRenewMonthlySub;
    private boolean hasMyTopLikersHalfYearlySub;
    private boolean hasMyTopLikersAutoRenewHalfYearlySub;
    private boolean hasMyTopLikersYearlySub;
    private boolean hasMyTopLikersAutoRenewYearlySub;

    private boolean hasWhoILikeMostMonthlySub;
    private boolean hasWhoILikeMostAutoRenewMonthlySub;
    private boolean hasWhoILikeMostHalfYearlySub;
    private boolean hasWhoILikeMostAutoRenewHalfYearlySub;
    private boolean hasWhoILikeMostYearlySub;
    private boolean hasWhoILikeMostAutoRenewYearlySub;

    private boolean hasMostPopularFollowersMonthlySub;
    private boolean hasMostPopularFollowersAutoRenewMonthlySub;
    private boolean hasMostPopularFollowersHalfYearlySub;
    private boolean hasMostPopularFollowersAutoRenewHalfYearlySub;
    private boolean hasMostPopularFollowersYearlySub;
    private boolean hasMostPopularFollowersAutoRenewYearlySub;

    private boolean hasGhostFollowersMonthlySub;
    private boolean hasGhostFollowersAutoRenewMonthlySub;
    private boolean hasGhostFollowersHalfYearlySub;
    private boolean hasGhostFollowersAutoRenewHalfYearlySub;
    private boolean hasGhostFollowersYearlySub;
    private boolean hasGhostFollowersAutoRenewYearlySub;
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

            checkForAutoRenewNOwnedForProduct(inventory, INAPP_PROFILE_VIEWER);
            checkForAutoRenewNOwnedForProduct(inventory, INAPP_MY_TOP_LIKERS);
            checkForAutoRenewNOwnedForProduct(inventory, INAPP_WHO_I_LIKED_MOST);
            checkForAutoRenewNOwnedForProduct(inventory, INAPP_MOST_POPULAR_FOLLOWERS);
            checkForAutoRenewNOwnedForProduct(inventory, INAPP_GHOST_FOLLOWERS);

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
//                imgvw_buy.setVisibility(View.INVISIBLE);
                Utility.setPurchaseData(getActivity(), SKU_REMOVE_ADS_MONTHLY, mSubscribedTo1MonthAdFree);
            } else {
//                imgvw_buy.setVisibility(View.VISIBLE);
                Utility.setPurchaseData(getActivity(), SKU_REMOVE_ADS_MONTHLY, mSubscribedTo1MonthAdFree);
            }

            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };
    // Callback for when a purchase is finished
    private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
//                setWaitScreen(false);
//                imgvw_buy.setVisibility(View.VISIBLE);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
//                setWaitScreen(false);
//                imgvw_buy.setVisibility(View.VISIBLE);
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
//                imgvw_buy.setVisibility(View.GONE);
//                mTank = TANK_MAX;
//                updateUi();
//                setWaitScreen(false);
            }
        }
    };
    private CircularImageView imgvw_pp_profilepic;
    private TextView txtvw_pp_profilename, txtvw_pp_followercount, txtvw_pp_followingcount;
    private LinearLayout lnrlyt_pp_followers, lnrlyt_pp_following, lnrlyt_pp_profileviewer,
            lnrlyt_pp_mytoplikes, lnrlyt_pp_whoilikemost, lnrlyt_pp_popularfollower,
            lnrlyt_pp_ghostfollower;
    private View vwPaidProfile;
    private Context mContext;
    private ProgressDialog pd;
    private UsersDao usersDao;
    private boolean showPd = true;
    private String SKU_SELECTED;
    private String SKU_MONTHLY;
    private String SKU_HALFYEARLY;
    private String SKU_YEARLY;
    private int inAppPurchaseOn = -1;
    private int inAppSubscription = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vwPaidProfile = inflater.inflate(R.layout.frgmnt_profilepaid, container, false);
        getIds();
        regListner();
//        getUserData();

        initInAppPurchase();

        return vwPaidProfile;
    }

    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        if (mBroadcastReceiver != null) {
            getActivity().unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }
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
        String base64EncodedPublicKey = INAPP_BASE64KEY;
        // Some sanity checks to see if the developer (that's you!) really followed the
        // instructions to run this sample (don't put these checks on your app!)
        if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) {
            throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
        }
        if (getActivity().getPackageName().startsWith("com.example")) {
            throw new RuntimeException("Please change the sample's package name! See README.");
        }

        // Create the helper, passing it our context and the public key to verify signatures with
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(getActivity(), base64EncodedPublicKey);

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
                mBroadcastReceiver = new IabBroadcastReceiver(PaidProfileFragment.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                getActivity().registerReceiver(mBroadcastReceiver, broadcastFilter);

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

    @Override
    public void onAttach(Context context) {
        mContext = context;
//        instaInsightApp = (InstaInsightApp) context.getApplicationContext();
        super.onAttach(context);
    }

    private void getIds() {
        imgvw_pp_profilepic = (CircularImageView) vwPaidProfile.findViewById(R.id.imgvw_pp_profilepic);
        txtvw_pp_profilename = (TextView) vwPaidProfile.findViewById(R.id.txtvw_pp_profilename);
//        imgvw_buy = (ImageView) vwPaidProfile.findViewById(R.id.imgvw_buy);
        lnrlyt_pp_followers = (LinearLayout) vwPaidProfile.findViewById(R.id.lnrlyt_pp_followers);
        lnrlyt_pp_following = (LinearLayout) vwPaidProfile.findViewById(R.id.lnrlyt_pp_following);

        txtvw_pp_followercount = (TextView) vwPaidProfile.findViewById(R.id.txtvw_pp_followercount);
        txtvw_pp_followingcount = (TextView) vwPaidProfile.findViewById(R.id.txtvw_pp_followingcount);

        lnrlyt_pp_profileviewer = (LinearLayout) vwPaidProfile.findViewById(R.id.lnrlyt_pp_profileviewer);
        lnrlyt_pp_mytoplikes = (LinearLayout) vwPaidProfile.findViewById(R.id.lnrlyt_pp_mytoplikes);
        lnrlyt_pp_whoilikemost = (LinearLayout) vwPaidProfile.findViewById(R.id.lnrlyt_pp_whoilikemost);
        lnrlyt_pp_popularfollower = (LinearLayout) vwPaidProfile.findViewById(R.id.lnrlyt_pp_popularfollower);
        lnrlyt_pp_ghostfollower = (LinearLayout) vwPaidProfile.findViewById(R.id.lnrlyt_pp_ghostfollower);
    }

    private void regListner() {
        lnrlyt_pp_followers.setOnClickListener(this);
        lnrlyt_pp_following.setOnClickListener(this);

        lnrlyt_pp_profileviewer.setOnClickListener(this);
        lnrlyt_pp_mytoplikes.setOnClickListener(this);
        lnrlyt_pp_whoilikemost.setOnClickListener(this);
        lnrlyt_pp_popularfollower.setOnClickListener(this);
        lnrlyt_pp_ghostfollower.setOnClickListener(this);
//        imgvw_buy.setOnClickListener(this);
    }

    private void getUserData() {

        if (mInstagramSession.isActive()) {

            InstagramUser instagramUser = mInstagramSession.getUser();

            txtvw_pp_profilename.setText(instagramUser.getUserBean().getFullName());

            Glide.with(mContext).load(instagramUser.getUserBean().getProfilPicture()).placeholder(R.drawable.defaultpic)
                    .crossFade().into(imgvw_pp_profilepic);

            usersDao = new UsersDao(mContext);
            UsersBean usersBean = usersDao.getUserDetails(instagramUser.getUserBean().getId());
            if (usersBean != null) {
                txtvw_pp_followercount.setText(usersBean.getUserCountBean().getFollowed_by());
                txtvw_pp_followingcount.setText(usersBean.getUserCountBean().getFollows());
                showPd = false;
            }

            if (isConnected()) {
                pd = new ProgressDialog(mContext);
                pd.setMessage("Please wait...");
                pd.setIndeterminate(true);
                if (showPd)
                    pd.show();
//                instaInsightApp.getUserBeanObserver().setUpdate(false);
                InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
                request.createRequest("GET", Constants.WebFields.ENDPOINT_USERSELF, new ArrayList<NameValuePair>(),
                        new InstagramRequest.InstagramRequestListener() {
                            @Override
                            public void onSuccess(String response) {

                                UsersDao usersDao = new UsersDao(mContext);
                                UsersBean usersBean = usersDao.getUserDetailsFromJson(response);
                                usersBean = usersDao.saveUserDetails(usersBean);
//                                instaInsightApp.getUserBeanObserver().setUpdate(true);
                                txtvw_pp_followercount.setText(usersBean.getUserCountBean().getFollowed_by());
                                txtvw_pp_followingcount.setText(usersBean.getUserCountBean().getFollows());
                                if (showPd)
                                    pd.dismiss();

                            }

                            @Override
                            public void onError(String error) {
                                if (showPd)
                                    pd.dismiss();
                                Utility.showToast(mContext, error);
//                                instaInsightApp.getUserBeanObserver().setUpdate(true);
                            }
                        });
            } else {
                UsersDao usersDao = new UsersDao(mContext);
                usersBean = usersDao.getUserDetails(instagramUser.getUserBean().getId());
                txtvw_pp_followercount.setText(usersBean.getUserCountBean().getFollowed_by());
                txtvw_pp_followingcount.setText(usersBean.getUserCountBean().getFollows());

            }
        } else {
            Utility.showToast(mContext, "Could not authentication, need to log in again");
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lnrlyt_pp_followers:
//                openFollowers();
                break;
            case R.id.lnrlyt_pp_following:
//                openFollowing();
                break;
            case R.id.lnrlyt_pp_profileviewer:
                openProfileViewer();
                break;
            case R.id.lnrlyt_pp_mytoplikes:
                openMyTopLikes();
                break;
            case R.id.lnrlyt_pp_whoilikemost:
                openWhoILikeMost();
                break;
            case R.id.lnrlyt_pp_popularfollower:
                openPopularFollower();
                break;
            case R.id.lnrlyt_pp_ghostfollower:
                openGhostFollower();
                break;
            case R.id.imgvw_buy:
//                openInAppPurchase();
                break;
            default:
        }
    }

    private void openInAppPurchase() {
//        Utility.showToast(mContext, "Work in progress");

        /* TODO: for security, generate your payload here for verification. See the comments on
             *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
             *        an empty string, but on a production app you should carefully generate
             *        this. */
        String payload = "";


        List<String> oldSkus = null;
        if (!TextUtils.isEmpty(m1MonthAdsFree)) {
            // The user currently has a valid subscription, any purchase action is going to
            // replace that subscription
            oldSkus = new ArrayList<String>();
            oldSkus.add(m1MonthAdsFree);
        }

//        setWaitScreen(true);
        Log.d(TAG, "Launching purchase flow for gas subscription.");
        try {
            mHelper.flagEndAsync();
            mHelper.launchPurchaseFlow(getActivity(), SKU_REMOVE_ADS_MONTHLY, IabHelper.ITEM_TYPE_SUBS,
                    oldSkus, RC_REQUEST, mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
//            setWaitScreen(false);
        }
        // Reset the dialog options
//        mSelectedSubscriptionPeriod = "";
//        mFirstChoiceSku = "";
//        mSecondChoiceSku = "";

    }

    private void openFollowers() {
        startActivity(new Intent(getActivity(), FollowersActivity.class));
    }

    private void openFollowing() {
        startActivity(new Intent(getActivity(), FollowingActivity.class));
    }

    private void openProfileViewer() {
//        if (Utility.getPurchaseData(getActivity(), INAPP_PROFILE_VIEWER_MONTHLY) ||
//                Utility.getPurchaseData(getActivity(), INAPP_PROFILE_VIEWER_HALLFYEARLY) ||
//                Utility.getPurchaseData(getActivity(), INAPP_PROFILE_VIEWER_YEARLY))
        startActivity(WhoViewedProfileActivity.class);
//        else
//            showPurchaseDialog(INAPP_PROFILE_VIEWER);
    }

    private void openMyTopLikes() {
//        if (Utility.getPurchaseData(getActivity(), INAPP_MY_TOP_LIKERS_MONTHLY) ||
//                Utility.getPurchaseData(getActivity(), INAPP_MY_TOP_LIKERS_HALLFYEARLY) ||
//                Utility.getPurchaseData(getActivity(), INAPP_MY_TOP_LIKERS_YEARLY))
        startActivity(MyTopLikersActivity.class);
//        else
//            showPurchaseDialog(INAPP_MY_TOP_LIKERS);
    }

    private void openWhoILikeMost() {
//        if (Utility.getPurchaseData(getActivity(), INAPP_WHO_I_LIKED_MOST_MONTHLY) ||
//                Utility.getPurchaseData(getActivity(), INAPP_WHO_I_LIKED_MOST_HALLFYEARLY) ||
//                Utility.getPurchaseData(getActivity(), INAPP_WHO_I_LIKED_MOST_YEARLY))
        startActivity(ILikedMostActivity.class);
//        else
//            showPurchaseDialog(INAPP_WHO_I_LIKED_MOST);

    }

    private void openPopularFollower() {

//        if (Utility.getPurchaseData(getActivity(), INAPP_MOST_POPULAR_FOLLOWERS_MONTHLY) ||
//                Utility.getPurchaseData(getActivity(), INAPP_MOST_POPULAR_FOLLOWERS_HALLFYEARLY) ||
//                Utility.getPurchaseData(getActivity(), INAPP_MOST_POPULAR_FOLLOWERS_YEARLY))
        startActivity(MostPopularFollowersActivity.class);
//        else
//            showPurchaseDialog(INAPP_MOST_POPULAR_FOLLOWERS);
    }

    private void openGhostFollower() {
//        if (Utility.getPurchaseData(getActivity(), INAPP_GHOST_FOLLOWERS_MONTHLY) ||
//                Utility.getPurchaseData(getActivity(), INAPP_GHOST_FOLLOWERS_HALLFYEARLY) ||
//                Utility.getPurchaseData(getActivity(), INAPP_GHOST_FOLLOWERS_YEARLY))
        startActivity(GhostFollowersActivity.class);
//        else
//            showPurchaseDialog(INAPP_GHOST_FOLLOWERS);
    }

    private void startActivity(Class aClass) {
        startActivity(new Intent(getActivity(), aClass));
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

    private void showPurchaseDialog(int inAppType) {
        CharSequence[] options = new CharSequence[3];
        options[0] = getString(R.string.inapp_subscription_1month);
        options[1] = getString(R.string.inapp_subscription_6months);
        options[2] = getString(R.string.inapp_subscription_1year);
        String title = "";
        // inAppType will be on which feature user has tapped
        switch (inAppType) {
            case INAPP_PROFILE_VIEWER:
                SKU_MONTHLY = INAPP_PROFILE_VIEWER_MONTHLY;
                SKU_HALFYEARLY = INAPP_PROFILE_VIEWER_HALLFYEARLY;
                SKU_YEARLY = INAPP_PROFILE_VIEWER_YEARLY;
                title = getActivity().getResources().getString(R.string.lbl_prfl_vwrs);
                break;
            case INAPP_MY_TOP_LIKERS:
                SKU_MONTHLY = INAPP_MY_TOP_LIKERS_MONTHLY;
                SKU_HALFYEARLY = INAPP_MY_TOP_LIKERS_HALLFYEARLY;
                SKU_YEARLY = INAPP_MY_TOP_LIKERS_YEARLY;
                title = getActivity().getResources().getString(R.string.lbl_my_top_likers);
                break;
            case INAPP_WHO_I_LIKED_MOST:
                SKU_MONTHLY = INAPP_WHO_I_LIKED_MOST_MONTHLY;
                SKU_HALFYEARLY = INAPP_WHO_I_LIKED_MOST_HALLFYEARLY;
                SKU_YEARLY = INAPP_WHO_I_LIKED_MOST_YEARLY;
                title = getActivity().getResources().getString(R.string.lbl_who_i_like_most);
                break;
            case INAPP_MOST_POPULAR_FOLLOWERS:
                SKU_MONTHLY = INAPP_MOST_POPULAR_FOLLOWERS_MONTHLY;
                SKU_HALFYEARLY = INAPP_MOST_POPULAR_FOLLOWERS_HALLFYEARLY;
                SKU_YEARLY = INAPP_MOST_POPULAR_FOLLOWERS_YEARLY;
                title = getActivity().getResources().getString(R.string.lbl_popular_followers);
                break;
            case INAPP_GHOST_FOLLOWERS:
                SKU_MONTHLY = INAPP_GHOST_FOLLOWERS_MONTHLY;
                SKU_HALFYEARLY = INAPP_GHOST_FOLLOWERS_HALLFYEARLY;
                SKU_YEARLY = INAPP_GHOST_FOLLOWERS_YEARLY;
                title = getActivity().getResources().getString(R.string.lbl_ghost_followers);
                break;
            case INAPP_UPGRADE_TO_PRO:
                SKU_MONTHLY = INAPP_UPGRADE_TO_PRO_MONTHLY;
                SKU_HALFYEARLY = INAPP_UPGRADE_TO_PRO_HALLFYEARLY;
                SKU_YEARLY = INAPP_UPGRADE_TO_PRO_YEARLY;
                title = getActivity().getResources().getString(R.string.lbl_upgradetopro);
                break;
            case INAPP_REMOVE_ADS:
                SKU_MONTHLY = INAPP_REMOVE_ADS_MONTHLY;
                SKU_HALFYEARLY = INAPP_REMOVE_ADS_HALLFYEARLY;
                SKU_YEARLY = INAPP_REMOVE_ADS_YEARLY;
                title = getActivity().getResources().getString(R.string.lbl_removeads);
                break;
        }
        // inAppType & inAppPurchaseOn will be on which feature user has tapped
        inAppPurchaseOn = inAppType;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setSingleChoiceItems(options, 0 /* checkedItem */, this)
                .setPositiveButton(R.string.subscription_prompt_continue, this)
                .setNegativeButton(R.string.subscription_prompt_cancel, this);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onClick(DialogInterface dialogInterface, int id) {
        switch (id) {
            case 0:
                SKU_SELECTED = SKU_MONTHLY;
                break;
            case 1:
                SKU_SELECTED = SKU_HALFYEARLY;
                break;
            case 2:
                SKU_SELECTED = SKU_YEARLY;
                break;
            case DialogInterface.BUTTON_POSITIVE:
                launchPurchaseFlow();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                inAppPurchaseOn = -1;
                break;
        }
    }

    private void launchPurchaseFlow() {
        /* TODO: for security, generate your payload here for verification. See the comments on
             *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
             *        an empty string, but on a production app you should carefully generate
             *        this. */
        String payload = "";

        if (TextUtils.isEmpty(SKU_SELECTED)) {
            // The user has not changed from the default selection
            SKU_SELECTED = SKU_MONTHLY;
        }

        List<String> oldSkus = null;
        if (!TextUtils.isEmpty(SKU_MONTHLY)
                && !SKU_MONTHLY.equals(SKU_SELECTED)) {
            // The user currently has a valid subscription, any purchase action is going to
            // replace that subscription
            oldSkus = new ArrayList<String>();
            oldSkus.add(SKU_MONTHLY);
        } else if (!TextUtils.isEmpty(SKU_HALFYEARLY)
                && !SKU_HALFYEARLY.equals(SKU_SELECTED)) {
            // The user currently has a valid subscription, any purchase action is going to
            // replace that subscription
            oldSkus = new ArrayList<String>();
            oldSkus.add(SKU_HALFYEARLY);
        } else if (!TextUtils.isEmpty(SKU_YEARLY)
                && !SKU_YEARLY.equals(SKU_SELECTED)) {
            // The user currently has a valid subscription, any purchase action is going to
            // replace that subscription
            oldSkus = new ArrayList<String>();
            oldSkus.add(SKU_YEARLY);
        }
        oldSkus = new ArrayList<>();
        //TODO loading
//        setWaitScreen(true);
        Log.d(TAG, "Launching purchase flow for subscription.");
        try {
            Log.d(TAG, "Selected Subscription:" + SKU_SELECTED);
            mHelper.flagEndAsync();
            mHelper.launchPurchaseFlow(getActivity(), SKU_SELECTED, IabHelper.ITEM_TYPE_SUBS,
                    oldSkus, RC_REQUEST, mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
            //TODO loading
//            setWaitScreen(false);
        } catch (NullPointerException e1) {
            e1.printStackTrace();
        }
        // Reset the dialog options
        SKU_SELECTED = "";
    }

    private void checkForAutoRenewNOwnedForProduct(Inventory inventory, int inAppProduct) {
        switch (inAppProduct) {
            case INAPP_PROFILE_VIEWER:
                //Profile Viewers
                Purchase profileSTalkerMonthly = inventory.getPurchase(INAPP_PROFILE_VIEWER_MONTHLY);
                if (profileSTalkerMonthly != null && profileSTalkerMonthly.isAutoRenewing()) {
                    hasPrflStlkrAutoRenewMonthlySub = true;
                }

                // The user is subscribed if either subscription exists, even if neither is auto
                // renewing
                hasPrflStlkrMonthlySub = (profileSTalkerMonthly != null && verifyDeveloperPayload(profileSTalkerMonthly));
                Log.d(TAG, "User " + (hasPrflStlkrMonthlySub ? "HAS" : "DOES NOT HAVE")
                        + " one month Profile Stalker subscription.");
                Utility.setPurchaseData(getActivity(), INAPP_PROFILE_VIEWER_MONTHLY, hasPrflStlkrMonthlySub);


                Purchase profileSTalkerHalfYearly = inventory.getPurchase(INAPP_PROFILE_VIEWER_HALLFYEARLY);
                if (profileSTalkerHalfYearly != null && profileSTalkerHalfYearly.isAutoRenewing()) {
                    hasPrflStlkrAutoRenewHalfYearlySub = true;
                }

                hasPrflStlkrHalfYearlySub = (profileSTalkerHalfYearly != null && verifyDeveloperPayload(profileSTalkerHalfYearly));
                Log.d(TAG, "User " + (hasPrflStlkrHalfYearlySub ? "HAS" : "DOES NOT HAVE")
                        + " one month Profile Stalker subscription.");
                Utility.setPurchaseData(getActivity(), INAPP_PROFILE_VIEWER_HALLFYEARLY, hasPrflStlkrHalfYearlySub);


                Purchase profileSTalkerYearly = inventory.getPurchase(INAPP_PROFILE_VIEWER_YEARLY);
                if (profileSTalkerYearly != null && profileSTalkerYearly.isAutoRenewing()) {
                    hasPrflStlkrAutoRenewYearlySub = true;
                }

                hasPrflStlkrYearlySub = (profileSTalkerYearly != null && verifyDeveloperPayload(profileSTalkerYearly));
                Log.d(TAG, "User " + (hasPrflStlkrYearlySub ? "HAS" : "DOES NOT HAVE")
                        + " one month Profile Stalker subscription.");
                Utility.setPurchaseData(getActivity(), INAPP_PROFILE_VIEWER_YEARLY, hasPrflStlkrYearlySub);

                break;
            case INAPP_MY_TOP_LIKERS:
                // My Top Likers
                Purchase myTopLikersMonthly = inventory.getPurchase(INAPP_MY_TOP_LIKERS_MONTHLY);
                if (myTopLikersMonthly != null && myTopLikersMonthly.isAutoRenewing()) {
                    hasMyTopLikersAutoRenewMonthlySub = true;
                }

                hasMyTopLikersMonthlySub = (myTopLikersMonthly != null && verifyDeveloperPayload(myTopLikersMonthly));
                Log.d(TAG, "User " + (hasMyTopLikersMonthlySub ? "HAS" : "DOES NOT HAVE")
                        + " one month Profile Stalker subscription.");
                Utility.setPurchaseData(getActivity(), INAPP_MY_TOP_LIKERS_MONTHLY, hasMyTopLikersMonthlySub);

                Purchase myTopLikersHalfYearly = inventory.getPurchase(INAPP_MY_TOP_LIKERS_HALLFYEARLY);
                if (myTopLikersHalfYearly != null && myTopLikersHalfYearly.isAutoRenewing()) {
                    hasMyTopLikersAutoRenewHalfYearlySub = true;
                }

                hasMyTopLikersHalfYearlySub = (myTopLikersHalfYearly != null && verifyDeveloperPayload(myTopLikersHalfYearly));
                Log.d(TAG, "User " + (hasMyTopLikersHalfYearlySub ? "HAS" : "DOES NOT HAVE")
                        + " one month Profile Stalker subscription.");
                Utility.setPurchaseData(getActivity(), INAPP_MY_TOP_LIKERS_HALLFYEARLY, hasMyTopLikersHalfYearlySub);

                Purchase myTopLikersYearly = inventory.getPurchase(INAPP_MY_TOP_LIKERS_YEARLY);
                if (myTopLikersYearly != null && myTopLikersYearly.isAutoRenewing()) {
                    hasMyTopLikersAutoRenewYearlySub = true;
                }

                hasMyTopLikersYearlySub = (myTopLikersYearly != null && verifyDeveloperPayload(myTopLikersYearly));
                Log.d(TAG, "User " + (hasMyTopLikersYearlySub ? "HAS" : "DOES NOT HAVE")
                        + " one month Profile Stalker subscription.");
                Utility.setPurchaseData(getActivity(), INAPP_MY_TOP_LIKERS_YEARLY, hasMyTopLikersYearlySub);

                break;

            case INAPP_WHO_I_LIKED_MOST:
                // Who I Liked Most
                Purchase whoILikedMostMonthly = inventory.getPurchase(INAPP_WHO_I_LIKED_MOST_MONTHLY);
                if (whoILikedMostMonthly != null && whoILikedMostMonthly.isAutoRenewing()) {
                    hasWhoILikeMostAutoRenewMonthlySub = true;
                }

                hasWhoILikeMostMonthlySub = (whoILikedMostMonthly != null && verifyDeveloperPayload(whoILikedMostMonthly));
                Log.d(TAG, "User " + (hasWhoILikeMostMonthlySub ? "HAS" : "DOES NOT HAVE")
                        + " one month Profile Stalker subscription.");
                Utility.setPurchaseData(getActivity(), INAPP_WHO_I_LIKED_MOST_MONTHLY, hasWhoILikeMostMonthlySub);

                Purchase whoILikedMostHalfYearly = inventory.getPurchase(INAPP_WHO_I_LIKED_MOST_HALLFYEARLY);
                if (whoILikedMostHalfYearly != null && whoILikedMostHalfYearly.isAutoRenewing()) {
                    hasWhoILikeMostAutoRenewHalfYearlySub = true;
                }

                hasWhoILikeMostHalfYearlySub = (whoILikedMostHalfYearly != null && verifyDeveloperPayload(whoILikedMostHalfYearly));
                Log.d(TAG, "User " + (hasWhoILikeMostHalfYearlySub ? "HAS" : "DOES NOT HAVE")
                        + " one month Profile Stalker subscription.");
                Utility.setPurchaseData(getActivity(), INAPP_WHO_I_LIKED_MOST_HALLFYEARLY, hasWhoILikeMostHalfYearlySub);

                Purchase whoILikedMostYearly = inventory.getPurchase(INAPP_WHO_I_LIKED_MOST_YEARLY);
                if (whoILikedMostYearly != null && whoILikedMostYearly.isAutoRenewing()) {
                    hasWhoILikeMostAutoRenewYearlySub = true;
                }

                hasWhoILikeMostYearlySub = (whoILikedMostYearly != null && verifyDeveloperPayload(whoILikedMostYearly));
                Log.d(TAG, "User " + (hasWhoILikeMostYearlySub ? "HAS" : "DOES NOT HAVE")
                        + " one month Profile Stalker subscription.");
                Utility.setPurchaseData(getActivity(), INAPP_WHO_I_LIKED_MOST_YEARLY, hasWhoILikeMostYearlySub);

                break;
            case INAPP_MOST_POPULAR_FOLLOWERS:
                // Most Popular Followers
                Purchase mostPopularFollowersMonthly = inventory.getPurchase(INAPP_MOST_POPULAR_FOLLOWERS_MONTHLY);
                if (mostPopularFollowersMonthly != null && mostPopularFollowersMonthly.isAutoRenewing()) {
                    hasMostPopularFollowersAutoRenewMonthlySub = true;
                }

                hasMostPopularFollowersMonthlySub = (mostPopularFollowersMonthly != null && verifyDeveloperPayload(mostPopularFollowersMonthly));
                Log.d(TAG, "User " + (hasMostPopularFollowersMonthlySub ? "HAS" : "DOES NOT HAVE")
                        + " one month Profile Stalker subscription.");
                Utility.setPurchaseData(getActivity(), INAPP_MOST_POPULAR_FOLLOWERS_MONTHLY, hasMostPopularFollowersMonthlySub);

                Purchase mostPopularFollowersHalfYearly = inventory.getPurchase(INAPP_MOST_POPULAR_FOLLOWERS_HALLFYEARLY);
                if (mostPopularFollowersHalfYearly != null && mostPopularFollowersHalfYearly.isAutoRenewing()) {
                    hasMostPopularFollowersAutoRenewHalfYearlySub = true;
                }

                hasMostPopularFollowersHalfYearlySub = (mostPopularFollowersHalfYearly != null && verifyDeveloperPayload(mostPopularFollowersHalfYearly));
                Log.d(TAG, "User " + (hasMostPopularFollowersHalfYearlySub ? "HAS" : "DOES NOT HAVE")
                        + " one month Profile Stalker subscription.");
                Utility.setPurchaseData(getActivity(), INAPP_MOST_POPULAR_FOLLOWERS_HALLFYEARLY, hasMostPopularFollowersHalfYearlySub);

                Purchase mostPopularFollowersYearly = inventory.getPurchase(INAPP_MOST_POPULAR_FOLLOWERS_YEARLY);
                if (mostPopularFollowersYearly != null && mostPopularFollowersYearly.isAutoRenewing()) {
                    hasMostPopularFollowersAutoRenewYearlySub = true;
                }

                hasMostPopularFollowersYearlySub = (mostPopularFollowersYearly != null && verifyDeveloperPayload(mostPopularFollowersYearly));
                Log.d(TAG, "User " + (hasMostPopularFollowersYearlySub ? "HAS" : "DOES NOT HAVE")
                        + " one month Profile Stalker subscription.");
                Utility.setPurchaseData(getActivity(), INAPP_MOST_POPULAR_FOLLOWERS_YEARLY, hasMostPopularFollowersYearlySub);

                break;
            case INAPP_GHOST_FOLLOWERS:
                // Ghost Followers
                Purchase ghostFollowersMonthly = inventory.getPurchase(INAPP_GHOST_FOLLOWERS_MONTHLY);
                if (ghostFollowersMonthly != null && ghostFollowersMonthly.isAutoRenewing()) {
                    hasGhostFollowersAutoRenewMonthlySub = true;
                }

                hasGhostFollowersMonthlySub = (ghostFollowersMonthly != null && verifyDeveloperPayload(ghostFollowersMonthly));
                Log.d(TAG, "User " + (hasGhostFollowersMonthlySub ? "HAS" : "DOES NOT HAVE")
                        + " one month Profile Stalker subscription.");
                Utility.setPurchaseData(getActivity(), INAPP_GHOST_FOLLOWERS_MONTHLY, hasGhostFollowersMonthlySub);

                Purchase ghostFollowersHalfYearly = inventory.getPurchase(INAPP_GHOST_FOLLOWERS_HALLFYEARLY);
                if (ghostFollowersHalfYearly != null && ghostFollowersHalfYearly.isAutoRenewing()) {
                    hasGhostFollowersAutoRenewHalfYearlySub = true;
                }

                hasGhostFollowersHalfYearlySub = (ghostFollowersHalfYearly != null && verifyDeveloperPayload(ghostFollowersHalfYearly));
                Log.d(TAG, "User " + (hasGhostFollowersHalfYearlySub ? "HAS" : "DOES NOT HAVE")
                        + " one month Profile Stalker subscription.");
                Utility.setPurchaseData(getActivity(), INAPP_GHOST_FOLLOWERS_HALLFYEARLY, hasGhostFollowersHalfYearlySub);

                Purchase ghostFollowersYearly = inventory.getPurchase(INAPP_GHOST_FOLLOWERS_YEARLY);
                if (ghostFollowersYearly != null && ghostFollowersYearly.isAutoRenewing()) {
                    hasGhostFollowersAutoRenewYearlySub = true;
                }
                hasGhostFollowersYearlySub = (ghostFollowersYearly != null && verifyDeveloperPayload(ghostFollowersYearly));
                Log.d(TAG, "User " + (hasGhostFollowersYearlySub ? "HAS" : "DOES NOT HAVE")
                        + " one month Profile Stalker subscription.");
                Utility.setPurchaseData(getActivity(), INAPP_GHOST_FOLLOWERS_YEARLY, hasGhostFollowersYearlySub);

                break;
        }
    }

    private void complain(String message) {
        Log.e(TAG, "**** : " + message);
        alert("Error: " + message);
    }

    private void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(getActivity());
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


}