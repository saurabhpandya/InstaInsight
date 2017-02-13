package com.instainsight.db.tables;

import android.util.Log;

/**
 * Created by SONY on 11-02-2017.
 */

public class LIKEDBYUSER {

    public static final String TABLE_NAME = "LIKEDBYUSER";
    public static final String LIKEDBYUSER_MEDIAID = "media_id";
    public static final String LIKEDBYUSER_TYPE = "type";
    public static final String LIKEDBYUSER_IMAGEURL = "imageurl";
    public static final String LIKEDBYUSER_LIKESCOUNT = "likes_count";
    public static final String LIKEDBYUSER_USERID = "user_id";
    public static final String LIKEDBYUSER_CREATEDTIME = "created_time";
    public static final String LIKEDBYUSER_COMMENTS = "comments_count";
    public static final String LIKEDBYUSER_LINK = "link";
    public static final String LIKEDBYUSER_LIKEDUSERID = "liked_userid";
    public static final String LIKEDBYUSER_LIKEDUSERNAME = "liked_username";
    public static final String LIKEDBYUSER_LIKEDUSERPROFILENAME = "liked_profilepic";
    public static final String LIKEDBYUSER_LIKEDUSERFULLNAME = "liked_fullname";

    private static final String LIKEDBYUSER_TBL_CREATE = (new StringBuffer())
            .append("CREATE TABLE ").append(TABLE_NAME).append(" ( ")
            .append(LIKEDBYUSER_MEDIAID).append(" TEXT, ")
            .append(LIKEDBYUSER_TYPE).append(" TEXT, ")
            .append(LIKEDBYUSER_IMAGEURL).append(" TEXT, ")
            .append(LIKEDBYUSER_LIKESCOUNT).append(" TEXT, ")
            .append(LIKEDBYUSER_USERID).append(" TEXT, ")
            .append(LIKEDBYUSER_CREATEDTIME).append(" TEXT, ")
            .append(LIKEDBYUSER_COMMENTS).append(" TEXT, ")
            .append(LIKEDBYUSER_LINK).append(" TEXT, ")
            .append(LIKEDBYUSER_LIKEDUSERID).append(" TEXT, ")
            .append(LIKEDBYUSER_LIKEDUSERNAME).append(" TEXT, ")
            .append(LIKEDBYUSER_LIKEDUSERPROFILENAME).append(" TEXT, ")
            .append(LIKEDBYUSER_LIKEDUSERFULLNAME).append(" TEXT")
            .append(");").toString();
    private static final String LIKEDBYUSER_TBL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private static String TAG = LIKEDBYUSER.class.getSimpleName();

    public static String createTable() {
        Log.d(TAG, TABLE_NAME + " TABLE CREATED");
        return LIKEDBYUSER_TBL_CREATE;
    }

    public static String dropTable() {
        return LIKEDBYUSER_TBL_DROP;
    }

}
