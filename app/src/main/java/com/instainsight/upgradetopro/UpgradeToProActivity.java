package com.instainsight.upgradetopro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.instainsight.InstaInsightApp;
import com.instainsight.R;
import com.instainsight.Utils.Utility;
import com.instainsight.ViewModelActivity;
import com.instainsight.databinding.ActivityUpgradeToProBinding;
import com.instainsight.upgradetopro.viewmodel.UpgradeToProViewModel;
import com.instainsight.util.IabBroadcastReceiver;
import com.instainsight.util.IabHelper;
import com.instainsight.util.IabResult;
import com.instainsight.util.Inventory;
import com.instainsight.util.Purchase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.instainsight.constants.Constants.INAPP_BASE64KEY;
import static com.instainsight.constants.Constants.INAPP_REMOVE_ADS;
import static com.instainsight.constants.Constants.INAPP_REMOVE_ADS_HALLFYEARLY;
import static com.instainsight.constants.Constants.INAPP_REMOVE_ADS_MONTHLY;
import static com.instainsight.constants.Constants.INAPP_REMOVE_ADS_YEARLY;
import static com.instainsight.constants.Constants.INAPP_UPGRADE_TO_PRO;
import static com.instainsight.constants.Constants.INAPP_UPGRADE_TO_PRO_HALLFYEARLY;
import static com.instainsight.constants.Constants.INAPP_UPGRADE_TO_PRO_MONTHLY;
import static com.instainsight.constants.Constants.INAPP_UPGRADE_TO_PRO_YEARLY;
import static com.instainsight.constants.Constants.RC_REQUEST;

public class UpgradeToProActivity extends ViewModelActivity implements IabBroadcastReceiver.IabBroadcastListener
        , DialogInterface.OnClickListener {

    @Inject
    UpgradeToProViewModel upgradeToProViewModel;
    private String TAG = UpgradeToProActivity.class.getSimpleName();
    private ActivityUpgradeToProBinding upgradeToProBinding;

    // The helper object
    private IabHelper mHelper;
    // Provides purchase notification while this app is running
    private IabBroadcastReceiver mBroadcastReceiver;

    // Listener that's called when we finish querying the items and subscriptions we own
    private IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                upgradeToProViewModel.complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            upgradeToProViewModel.checkForAutoRenewNOwnedForProduct(inventory, INAPP_REMOVE_ADS);
            upgradeToProViewModel.checkForAutoRenewNOwnedForProduct(inventory, INAPP_UPGRADE_TO_PRO);
            setPurchasedView();
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
                upgradeToProViewModel.complain("Error purchasing: " + result);
//                setWaitScreen(false);
//                upgradeToProBinding.txtvwUpgradetopro.setAlpha(1.0f);
//                upgradeToProBinding.txtvwUpgradetopro.setEnabled(true);
                return;
            }
            if (!upgradeToProViewModel.verifyDeveloperPayload(purchase)) {
                upgradeToProViewModel.complain("Error purchasing. Authenticity verification failed.");
//                setWaitScreen(false);
//                upgradeToProBinding.txtvwUpgradetopro.setAlpha(1.0f);
//                upgradeToProBinding.txtvwUpgradetopro.setEnabled(true);
                return;
            }

            Log.d(TAG, "Purchase successful.");
            upgradeToProViewModel.setPurchasedDataForProduct(purchase);
            setPurchasedView();

        }
    };
    private String SKU_SELECTED;
    private String SKU_MONTHLY;
    private String SKU_HALFYEARLY;
    private String SKU_YEARLY;
    private int inAppPurchaseOn = -1;
    private int inAppSubscription = -1;

    private void setPurchasedView() {

        upgradeToProViewModel.hasUpgradeToProMonthlySub = Utility.getPurchaseData(getApplicationContext(), INAPP_UPGRADE_TO_PRO_MONTHLY);
        upgradeToProViewModel.hasUpgradeToProHalfYearlySub = Utility.getPurchaseData(getApplicationContext(), INAPP_UPGRADE_TO_PRO_HALLFYEARLY);
        upgradeToProViewModel.hasUpgradeToProYearlySub = Utility.getPurchaseData(getApplicationContext(), INAPP_UPGRADE_TO_PRO_YEARLY);

        upgradeToProViewModel.hasRemoveAdsMonthlySub = Utility.getPurchaseData(getApplicationContext(), INAPP_REMOVE_ADS_MONTHLY);
        upgradeToProViewModel.hasRemoveAdsHalfYearlySub = Utility.getPurchaseData(getApplicationContext(), INAPP_REMOVE_ADS_HALLFYEARLY);
        upgradeToProViewModel.hasRemoveAdsYearlySub = Utility.getPurchaseData(getApplicationContext(), INAPP_REMOVE_ADS_YEARLY);

        if (upgradeToProViewModel.hasUpgradeToProMonthlySub ||
                upgradeToProViewModel.hasUpgradeToProHalfYearlySub ||
                upgradeToProViewModel.hasUpgradeToProYearlySub) {
            upgradeToProBinding.txtvwUpgradetopro.setAlpha(0.5f);
            upgradeToProBinding.txtvwUpgradetopro.setEnabled(false);
        } else {
            upgradeToProBinding.txtvwUpgradetopro.setAlpha(1.0f);
            upgradeToProBinding.txtvwUpgradetopro.setEnabled(true);
        }
        if (upgradeToProViewModel.hasRemoveAdsMonthlySub ||
                upgradeToProViewModel.hasRemoveAdsHalfYearlySub ||
                upgradeToProViewModel.hasRemoveAdsYearlySub) {
            upgradeToProBinding.txtvwRemoveads.setAlpha(0.5f);
            upgradeToProBinding.txtvwRemoveads.setEnabled(false);
        } else {
            upgradeToProBinding.txtvwRemoveads.setAlpha(1.0f);
            upgradeToProBinding.txtvwRemoveads.setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((InstaInsightApp) getApplication()).getComponent().inject(UpgradeToProActivity.this);
        super.onCreate(savedInstanceState);
        upgradeToProBinding = DataBindingUtil.setContentView(this, R.layout.activity_upgrade_to_pro);
        upgradeToProBinding.setUpgradeToPro(upgradeToProViewModel);
        setTitle(R.string.lbl_go_pro);
        initActionbar();
        initInAppPurchase();
    }

    private void initActionbar() {
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

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
                    upgradeToProViewModel.complain("Problem setting up in-app billing: " + result);
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
                mBroadcastReceiver = new IabBroadcastReceiver(UpgradeToProActivity.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    upgradeToProViewModel.complain("Error querying inventory. Another async operation in progress.");
                }
            }
        });

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
            mHelper.launchPurchaseFlow(UpgradeToProActivity.this, SKU_SELECTED, IabHelper.ITEM_TYPE_SUBS,
                    oldSkus, RC_REQUEST, mPurchaseFinishedListener, payload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            upgradeToProViewModel.complain("Error launching purchase flow. Another async operation in progress.");
            //TODO loading
//            setWaitScreen(false);
        } catch (NullPointerException e1) {
            e1.printStackTrace();
        }
        // Reset the dialog options
        SKU_SELECTED = "";

    }

    public void checkPurchaseForPro(View v) {

        if (!Utility.getPurchaseData(getApplicationContext(), INAPP_UPGRADE_TO_PRO_MONTHLY) &&
                !Utility.getPurchaseData(getApplicationContext(), INAPP_UPGRADE_TO_PRO_HALLFYEARLY) &&
                !Utility.getPurchaseData(getApplicationContext(), INAPP_UPGRADE_TO_PRO_YEARLY))
            showPurchaseDialog(INAPP_UPGRADE_TO_PRO);
    }

    public void checkPurchaseForRemoveAds(View v) {

        if (!Utility.getPurchaseData(getApplicationContext(), INAPP_REMOVE_ADS_MONTHLY) &&
                !Utility.getPurchaseData(getApplicationContext(), INAPP_REMOVE_ADS_HALLFYEARLY) &&
                !Utility.getPurchaseData(getApplicationContext(), INAPP_REMOVE_ADS_YEARLY))
            showPurchaseDialog(INAPP_REMOVE_ADS);
    }

    private void showPurchaseDialog(int inAppType) {
        CharSequence[] options = new CharSequence[3];
        options[0] = getString(R.string.inapp_subscription_1month);
        options[1] = getString(R.string.inapp_subscription_6months);
        options[2] = getString(R.string.inapp_subscription_1year);
        String title = "";
        // inAppType will be on which feature user has tapped
        switch (inAppType) {
            case INAPP_UPGRADE_TO_PRO:
                SKU_MONTHLY = INAPP_UPGRADE_TO_PRO_MONTHLY;
                SKU_HALFYEARLY = INAPP_UPGRADE_TO_PRO_HALLFYEARLY;
                SKU_YEARLY = INAPP_UPGRADE_TO_PRO_YEARLY;
                title = getResources().getString(R.string.lbl_upgradetopro);
                break;
            case INAPP_REMOVE_ADS:
                SKU_MONTHLY = INAPP_REMOVE_ADS_MONTHLY;
                SKU_HALFYEARLY = INAPP_REMOVE_ADS_HALLFYEARLY;
                SKU_YEARLY = INAPP_REMOVE_ADS_YEARLY;
                title = getResources().getString(R.string.lbl_removeads);
                break;
        }
        // inAppType & inAppPurchaseOn will be on which feature user has tapped
        inAppPurchaseOn = inAppType;
        AlertDialog.Builder builder = new AlertDialog.Builder(UpgradeToProActivity.this);
        builder.setTitle(title)
                .setSingleChoiceItems(options, 0 /* checkedItem */, this)
                .setPositiveButton(R.string.subscription_prompt_continue, this)
                .setNegativeButton(R.string.subscription_prompt_cancel, this);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void receivedBroadcast() {
// Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            upgradeToProViewModel.complain("Error querying inventory. Another async operation in progress.");
        }
    }

    @Override
    protected void createViewModel() {
        mViewModel = upgradeToProViewModel;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
}
