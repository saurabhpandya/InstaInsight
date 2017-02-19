package com.instainsight.instagram;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

/**
 * Manage access token and user data. Token and user data data are stored in shared preferences.
 *
 * @author Lorensius W. L T <lorenz@londatiga.net>
 */
public class InstagramSession {
    private static final String SHARED = "Instagram_Preferences";
    private static final String USERID = "userid";
    private static final String USERNAME = "username";
    private static final String FULLNAME = "fullname";
    private static final String PROFILPIC = "profilpic";
    private static final String ACCESS_TOKEN = "access_token";
    private Context mContext;
    private SharedPreferences mSharedPref;

    public InstagramSession(Context context) {
        mContext = context;
        mSharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
    }

    /**
     * Save user data
     *
     * @param user User data
     */
    public void store(InstagramUser user) {
        Editor editor = mSharedPref.edit();

        editor.putString(ACCESS_TOKEN, user.accessToken);
        editor.putString(USERID, user.getUserBean().getId());
        editor.putString(USERNAME, user.getUserBean().getUsername());
        editor.putString(FULLNAME, user.getUserBean().getFullName());
        editor.putString(PROFILPIC, user.getUserBean().getProfilPicture());

        editor.commit();
    }

    /**
     * Reset user data
     */
    public void reset() {
        Editor editor = mSharedPref.edit();

        editor.putString(ACCESS_TOKEN, "");
        editor.putString(USERID, "");
        editor.putString(USERNAME, "");
        editor.putString(FULLNAME, "");
        editor.putString(PROFILPIC, "");

        editor.commit();

        CookieSyncManager.createInstance(mContext);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    /**
     * Get user data
     *
     * @return User data
     */
    public InstagramUser getUser() {
        if (mSharedPref.getString(ACCESS_TOKEN, "").equals("")) {
            return null;
        }

        InstagramUser user = new InstagramUser();
        UserBean userBean = new UserBean();

        userBean.setId(mSharedPref.getString(USERID, ""));
        userBean.setUsername(mSharedPref.getString(USERNAME, ""));
        userBean.setFullName(mSharedPref.getString(FULLNAME, ""));
        userBean.setProfilPicture(mSharedPref.getString(PROFILPIC, ""));
        user.setAccessToken(mSharedPref.getString(ACCESS_TOKEN, ""));
        user.setUserBean(userBean);
        return user;
    }

    /**
     * Get access token
     *
     * @return Access token
     */
    public String getAccessToken() {
        return mSharedPref.getString(ACCESS_TOKEN, "");
    }

    /**
     * Check if ther is an active session.
     *
     * @return true if active and vice versa
     */
    public boolean isActive() {
        return (mSharedPref.getString(ACCESS_TOKEN, "").equals("")) ? false : true;
    }
}