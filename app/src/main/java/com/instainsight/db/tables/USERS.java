package com.instainsight.db.tables;

import android.util.Log;

/**
 * Created by SONY on 13-02-2017.
 */

public class USERS {
    public static String TABLE_NAME = "USERS";
    private static final String USERS_TBL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static String ID = "id";
    public static String USERNAME = "username";
    public static String FULLNAME = "full_name";
    public static String PROFILEPICTURE = "profile_picture";
    public static String BIO = "bio";
    public static String WEBSITE = "website";
    public static String MEDIA_COUNT = "media_count";
    public static String FOLLOWS_COUNT = "media_follows";
    public static String FOLLOWED_BY_COUNT = "media_followed_by";
    public static String FOLLOWS = "follows";
    public static String FOLLOWED_BY = "followed_by";

    private static final String USERS_TBL_CREATE = (new StringBuffer())
            .append("CREATE TABLE ").append(TABLE_NAME).append(" ( ")
            .append(ID).append(" TEXT, ")
            .append(USERNAME).append(" TEXT, ")
            .append(FULLNAME).append(" TEXT, ")
            .append(PROFILEPICTURE).append(" TEXT, ")
            .append(BIO).append(" TEXT, ")
            .append(WEBSITE).append(" TEXT, ")
            .append(MEDIA_COUNT).append(" TEXT, ")
            .append(FOLLOWS_COUNT).append(" TEXT, ")
            .append(FOLLOWED_BY_COUNT).append(" TEXT, ")
            .append(FOLLOWS).append(" TEXT, ")
            .append(FOLLOWED_BY).append(" TEXT")
            .append(");").toString();
    private static String TAG = USERSELF.class.getSimpleName();

    public static String createTable() {
        Log.d(TAG, TABLE_NAME + " TABLE CREATED");
        return USERS_TBL_CREATE;
    }

    public static String dropTable() {
        return USERS_TBL_DROP;
    }
}
