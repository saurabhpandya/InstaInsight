package com.instainsight.db.tables;

import android.util.Log;

/**
 * Created by SONY on 13-02-2017.
 */

public class PAID_COMMENTS {

    public static final String TABLE_NAME = "PAID_COMMENTS";

    public static final String COMMENTED_MEDIA_ID = "media_id";

    public static final String COMMENT_ID = "comment_id";

    public static final String COMMENTED_TEXT = "text";

    public static final String COMMENTED_USERNAME = "commented_username";
    public static final String COMMENTED_USERPROFILEPIC = "commented_userprofilepic";
    public static final String COMMENTED_USERID = "commented_userid";
    public static final String COMMENTED_USERFULLNAME = "commented_fullname";

    public static final String CREATEDTIME = "created_time";


    private static final String PAID_COMMENTS_TBL_CREATE = (new StringBuffer())
            .append("CREATE TABLE ").append(TABLE_NAME).append(" ( ")
            .append(COMMENTED_MEDIA_ID).append(" TEXT, ")
            .append(COMMENT_ID).append(" TEXT, ")
            .append(COMMENTED_TEXT).append(" TEXT, ")
            .append(COMMENTED_USERNAME).append(" TEXT, ")
            .append(COMMENTED_USERPROFILEPIC).append(" TEXT, ")
            .append(COMMENTED_USERID).append(" TEXT, ")
            .append(COMMENTED_USERFULLNAME).append(" TEXT, ")
            .append(CREATEDTIME).append(" TEXT")
            .append(");").toString();

    private static final String PAID_COMMENTS_TBL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static String TAG = PAID_COMMENTS.class.getSimpleName();

    public static String createTable() {
        Log.d(TAG, TABLE_NAME + " TABLE CREATED");
        return PAID_COMMENTS_TBL_CREATE;
    }

    public static String dropTable() {
        return PAID_COMMENTS_TBL_DROP;
    }

}
