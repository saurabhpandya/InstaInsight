package com.instainsight.upgradetopro.viewmodel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import com.instainsight.Utils.Utility;
import com.instainsight.instagram.InstagramSession;
import com.instainsight.util.Inventory;
import com.instainsight.util.Purchase;
import com.instainsight.viewmodels.BaseViewModel;
import com.instainsight.viewmodels.IViewModel;

import static com.instainsight.constants.Constants.INAPP_REMOVE_ADS;
import static com.instainsight.constants.Constants.INAPP_REMOVE_ADS_HALLFYEARLY;
import static com.instainsight.constants.Constants.INAPP_REMOVE_ADS_MONTHLY;
import static com.instainsight.constants.Constants.INAPP_REMOVE_ADS_YEARLY;
import static com.instainsight.constants.Constants.INAPP_UPGRADE_TO_PRO;
import static com.instainsight.constants.Constants.INAPP_UPGRADE_TO_PRO_HALLFYEARLY;
import static com.instainsight.constants.Constants.INAPP_UPGRADE_TO_PRO_MONTHLY;
import static com.instainsight.constants.Constants.INAPP_UPGRADE_TO_PRO_YEARLY;

/**
 * Created by SONY on 31-03-2017.
 */

public class UpgradeToProViewModel extends BaseViewModel implements IViewModel {

    public boolean hasRemoveAdsMonthlySub;
    public boolean hasRemoveAdsAutoRenewMonthlySub;

    public boolean hasRemoveAdsHalfYearlySub;
    public boolean hasRemoveAdsAutoRenewHalfYearlySub;

    public boolean hasRemoveAdsYearlySub;
    public boolean hasRemoveAdsAutoRenewYearlySub;

    public boolean hasUpgradeToProMonthlySub;
    public boolean hasUpgradeToProAutoRenewMonthlySub;

    public boolean hasUpgradeToProHalfYearlySub;
    public boolean hasUpgradeToProAutoRenewHalfYearlySub;

    public boolean hasUpgradeToProYearlySub;
    public boolean hasUpgradeToProAutoRenewYearlySub;

    private String TAG = UpgradeToProViewModel.class.getSimpleName();
    private Context mContext;
    private InstagramSession mInstagramSession;
    private Activity mActivity;

    // Does the user have an active subscription 1 month ads free plan?
    private boolean mSubscribedTo1MonthAdFree = false;
    // Will the subscription auto-renew?
    private boolean mAutoRenewEnabled = false;
    // Tracks the currently owned 1 month ads free SKU, and the options in the Manage dialog
    private String m1MonthAdsFree = "";
    private String mRemoveAdsMonthly;


    public UpgradeToProViewModel(Context context, InstagramSession mInstagramSession) {
        mContext = context;
        this.mInstagramSession = mInstagramSession;
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

    public void complain(String message) {
        Log.e(TAG, "**** : " + message);
        alert("Error: " + message);
    }

    public void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(mActivity);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    /**
     * Verifies the developer payload of a purchase.
     */
    public boolean verifyDeveloperPayload(Purchase p) {
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

    public void checkForAutoRenewNOwnedForProduct(Inventory inventory, int inAppProduct) {
        switch (inAppProduct) {
            case INAPP_REMOVE_ADS:
                // Remove Ads
                Purchase removeAdsMonthly = inventory.getPurchase(INAPP_REMOVE_ADS_MONTHLY);
                if (removeAdsMonthly != null && removeAdsMonthly.isAutoRenewing()) {
                    hasRemoveAdsAutoRenewMonthlySub = true;
                }

                hasRemoveAdsMonthlySub = (removeAdsMonthly != null && verifyDeveloperPayload(removeAdsMonthly));
                Log.d(TAG, "User " + (hasRemoveAdsMonthlySub ? "HAS" : "DOES NOT HAVE")
                        + " one month Profile Stalker subscription.");
                Utility.setPurchaseData(mContext, INAPP_REMOVE_ADS_MONTHLY, hasRemoveAdsMonthlySub);

                Purchase removeAdsHalfYearly = inventory.getPurchase(INAPP_REMOVE_ADS_HALLFYEARLY);
                if (removeAdsHalfYearly != null && removeAdsHalfYearly.isAutoRenewing()) {
                    hasRemoveAdsAutoRenewHalfYearlySub = true;
                }

                hasRemoveAdsHalfYearlySub = (removeAdsHalfYearly != null && verifyDeveloperPayload(removeAdsHalfYearly));
                Log.d(TAG, "User " + (hasRemoveAdsHalfYearlySub ? "HAS" : "DOES NOT HAVE")
                        + " one month Profile Stalker subscription.");
                Utility.setPurchaseData(mContext, INAPP_REMOVE_ADS_HALLFYEARLY, hasRemoveAdsHalfYearlySub);

                Purchase removeAdsYearly = inventory.getPurchase(INAPP_REMOVE_ADS_YEARLY);
                if (removeAdsYearly != null && removeAdsYearly.isAutoRenewing()) {
                    hasRemoveAdsAutoRenewYearlySub = true;
                }

                hasRemoveAdsYearlySub = (removeAdsYearly != null && verifyDeveloperPayload(removeAdsYearly));
                Log.d(TAG, "User " + (hasRemoveAdsYearlySub ? "HAS" : "DOES NOT HAVE")
                        + " one month Profile Stalker subscription.");
                Utility.setPurchaseData(mContext, INAPP_REMOVE_ADS_YEARLY, hasRemoveAdsYearlySub);

                break;
            case INAPP_UPGRADE_TO_PRO:
                // Upgrade to Pro
                Purchase upgradeToProMonthly = inventory.getPurchase(INAPP_UPGRADE_TO_PRO_MONTHLY);
                if (upgradeToProMonthly != null && upgradeToProMonthly.isAutoRenewing()) {
                    hasUpgradeToProAutoRenewMonthlySub = true;
                }

                hasUpgradeToProMonthlySub = (upgradeToProMonthly != null && verifyDeveloperPayload(upgradeToProMonthly));
                Log.d(TAG, "User " + (hasUpgradeToProMonthlySub ? "HAS" : "DOES NOT HAVE")
                        + " one month Profile Stalker subscription.");
                Utility.setPurchaseData(mContext, INAPP_UPGRADE_TO_PRO_MONTHLY, hasUpgradeToProMonthlySub);

                Purchase upgradeToProHalfYearly = inventory.getPurchase(INAPP_UPGRADE_TO_PRO_HALLFYEARLY);
                if (upgradeToProHalfYearly != null && upgradeToProHalfYearly.isAutoRenewing()) {
                    hasUpgradeToProAutoRenewHalfYearlySub = true;
                }

                hasUpgradeToProHalfYearlySub = (upgradeToProHalfYearly != null && verifyDeveloperPayload(upgradeToProHalfYearly));
                Log.d(TAG, "User " + (hasUpgradeToProHalfYearlySub ? "HAS" : "DOES NOT HAVE")
                        + " one month Profile Stalker subscription.");
                Utility.setPurchaseData(mContext, INAPP_UPGRADE_TO_PRO_HALLFYEARLY, hasUpgradeToProHalfYearlySub);

                Purchase upgradeToProYearly = inventory.getPurchase(INAPP_UPGRADE_TO_PRO_YEARLY);
                if (upgradeToProYearly != null && upgradeToProYearly.isAutoRenewing()) {
                    hasUpgradeToProAutoRenewYearlySub = true;
                }

                hasUpgradeToProYearlySub = (upgradeToProYearly != null && verifyDeveloperPayload(upgradeToProYearly));
                Log.d(TAG, "User " + (hasUpgradeToProYearlySub ? "HAS" : "DOES NOT HAVE")
                        + " one month Profile Stalker subscription.");
                Utility.setPurchaseData(mContext, INAPP_UPGRADE_TO_PRO_YEARLY, hasUpgradeToProYearlySub);
                break;
        }
    }

    public String setPurchasedDataForProduct(Purchase purchase) {
        String purchaseSKU = "";
        switch (purchase.getSku()) {
            case INAPP_UPGRADE_TO_PRO_MONTHLY:
                Log.d(TAG, INAPP_UPGRADE_TO_PRO_MONTHLY + " subscription purchased.");
                alert("Thank you for subscribing to 1 month Pro Plan!");
                hasUpgradeToProMonthlySub = true;
                hasUpgradeToProAutoRenewMonthlySub = purchase.isAutoRenewing();
                purchaseSKU = INAPP_UPGRADE_TO_PRO_MONTHLY;
                Utility.setPurchaseData(mContext, INAPP_UPGRADE_TO_PRO_MONTHLY, hasUpgradeToProMonthlySub);
                break;
            case INAPP_UPGRADE_TO_PRO_HALLFYEARLY:
                Log.d(TAG, INAPP_UPGRADE_TO_PRO_HALLFYEARLY + " subscription purchased.");
                alert("Thank you for subscribing to 6 months Pro Plan!");
                hasUpgradeToProHalfYearlySub = true;
                hasUpgradeToProAutoRenewHalfYearlySub = purchase.isAutoRenewing();
                purchaseSKU = INAPP_UPGRADE_TO_PRO_HALLFYEARLY;
                Utility.setPurchaseData(mContext, INAPP_UPGRADE_TO_PRO_HALLFYEARLY, hasUpgradeToProHalfYearlySub);
                break;
            case INAPP_UPGRADE_TO_PRO_YEARLY:
                Log.d(TAG, INAPP_UPGRADE_TO_PRO_YEARLY + " subscription purchased.");
                alert("Thank you for subscribing to 1 year Pro Plan!");
                hasUpgradeToProYearlySub = true;
                hasUpgradeToProAutoRenewYearlySub = purchase.isAutoRenewing();
                purchaseSKU = INAPP_UPGRADE_TO_PRO_YEARLY;
                Utility.setPurchaseData(mContext, INAPP_UPGRADE_TO_PRO_YEARLY, hasUpgradeToProYearlySub);
                break;
            case INAPP_REMOVE_ADS_MONTHLY:
                Log.d(TAG, INAPP_REMOVE_ADS_MONTHLY + " subscription purchased.");
                alert("Thank you. Enjoy ads free experience for 1 month!");
                hasRemoveAdsMonthlySub = true;
                hasRemoveAdsAutoRenewMonthlySub = purchase.isAutoRenewing();
                purchaseSKU = INAPP_REMOVE_ADS_MONTHLY;
                Utility.setPurchaseData(mContext, INAPP_REMOVE_ADS_MONTHLY, hasRemoveAdsMonthlySub);
                break;
            case INAPP_REMOVE_ADS_HALLFYEARLY:
                Log.d(TAG, INAPP_REMOVE_ADS_HALLFYEARLY + " subscription purchased.");
                alert("Thank you. Enjoy ads free experience for 6 months!");
                hasRemoveAdsHalfYearlySub = true;
                hasRemoveAdsAutoRenewHalfYearlySub = purchase.isAutoRenewing();
                purchaseSKU = INAPP_REMOVE_ADS_HALLFYEARLY;
                Utility.setPurchaseData(mContext, INAPP_REMOVE_ADS_HALLFYEARLY, hasRemoveAdsHalfYearlySub);
                break;
            case INAPP_REMOVE_ADS_YEARLY:
                Log.d(TAG, INAPP_REMOVE_ADS_YEARLY + " subscription purchased.");
                alert("Thank you. Enjoy ads free experience for 1 year!");
                hasRemoveAdsYearlySub = true;
                hasRemoveAdsAutoRenewYearlySub = purchase.isAutoRenewing();
                purchaseSKU = INAPP_REMOVE_ADS_YEARLY;
                Utility.setPurchaseData(mContext, INAPP_REMOVE_ADS_YEARLY, hasRemoveAdsYearlySub);
                break;
        }
        return purchaseSKU;
    }

}
