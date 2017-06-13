package com.instainsight.db.tables;

import android.util.Log;

/**
 * Created by SONY on 13-02-2017.
 */

public class PAID_FOLLOWEDBY {
    public static String TABLE_NAME = "PAID_FOLLOWEDBY";
    private static final String PAID_FOLLOWEDBY_TBL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static String ID = "id";
    public static String USERNAME = "username";
    public static String FULLNAME = "full_name";
    public static String PROFILEPICTURE = "profile_picture";
    public static String BIO = "bio";
    public static String WEBSITE = "website";
    public static String MEDIA_COUNT = "media_count";
    public static String FOLLOWS_COUNT = "media_follows";
    public static String FOLLOWED_BY_COUNT = "media_followed_by";
    public static String FOLLOWS = "follows";   // if our user follows this user then 1 else 0
    public static String FOLLOWED_BY = "followed_by"; // if this user follows our user then 1 else 0
    public static String ISNEW_FOLLOWS = "isnew_follows";   // if this user is loaded for first time then 1 else 0 for follows
    public static String ISNEW_FOLLOWED_BY = "isnew_followed_by";   // if this user is loaded for first time then 1 else 0 for followed by
    public static String CREATEDTIME = "created_time";
    public static String ISPRIVATE = "is_private";


    private static final String PAID_FOLLOWEDBY_TBL_CREATE = (new StringBuffer())
            .append("CREATE TABLE ").append(TABLE_NAME).append(" ( ")
            .append(ID).append(" TEXT, ")
            .append(USERNAME).append(" TEXT, ")
            .append(FULLNAME).append(" TEXT, ")
            .append(PROFILEPICTURE).append(" TEXT, ")
            .append(BIO).append(" TEXT, ")
            .append(WEBSITE).append(" TEXT, ")
            .append(MEDIA_COUNT).append(" INTEGER, ")
            .append(FOLLOWS_COUNT).append(" INTEGER, ")
            .append(FOLLOWED_BY_COUNT).append(" INTEGER, ")
            .append(FOLLOWS).append(" TEXT DEFAULT \"0\", ")
            .append(FOLLOWED_BY).append(" TEXT DEFAULT \"0\", ")
            .append(ISNEW_FOLLOWS).append(" TEXT, ")
            .append(ISNEW_FOLLOWED_BY).append(" TEXT, ")
            .append(ISPRIVATE).append(" TEXT, ")
            .append(CREATEDTIME).append(" TEXT")
            .append(");").toString();

    private static String TAG = PAID_FOLLOWEDBY.class.getSimpleName();

    public static String createTable() {
        Log.d(TAG, TABLE_NAME + " TABLE CREATED");
        return PAID_FOLLOWEDBY_TBL_CREATE;
    }

    public static String dropTable() {
        return PAID_FOLLOWEDBY_TBL_DROP;
    }
}
