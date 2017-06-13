package com.instainsight.db.tables;

import android.util.Log;

/**
 * Created by SONY on 13-02-2017.
 */

public class PAID_LIKEDUSERS {

    public static final String TABLE_NAME = "PAID_LIKEDUSERS";

    public static final String LIKED_MEDIAID = "mediaid";
    public static final String LIKED_USERNAME = "liked_username";
    public static final String LIKED_USERID = "liked_userid";
    public static final String LIKED_USERFIRSTNAME = "liked_firstname";
    public static final String LIKED_USERLASTNAME = "liked_lastname";
    public static final String TYPE = "type";


    private static final String PAID_LIKEDUSERS_TBL_CREATE = (new StringBuffer())
            .append("CREATE TABLE ").append(TABLE_NAME).append(" ( ")
            .append(LIKED_MEDIAID).append(" TEXT, ")
            .append(LIKED_USERNAME).append(" TEXT, ")
            .append(LIKED_USERID).append(" TEXT, ")
            .append(LIKED_USERFIRSTNAME).append(" TEXT, ")
            .append(LIKED_USERLASTNAME).append(" TEXT, ")
            .append(TYPE).append(" TEXT")
            .append(");").toString();

    private static final String PAID_LIKEDUSERS_TBL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static String TAG = PAID_LIKEDUSERS.class.getSimpleName();

    public static String createTable() {
        Log.d(TAG, TABLE_NAME + " TABLE CREATED");
        return PAID_LIKEDUSERS_TBL_CREATE;
    }

    public static String dropTable() {
        return PAID_LIKEDUSERS_TBL_DROP;
    }

}
