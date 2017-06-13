package com.instainsight.db.tables;

import android.util.Log;

/**
 * Created by SONY on 13-02-2017.
 */

public class PAID_MEDIA {

    public static final String TABLE_NAME = "PAID_MEDIA";

    public static final String MEDIAID = "media_id";

    public static final String MEDIAOWNER_ID = "mediaowner_id";
    public static final String MEDIAOWNER_USERNAME = "mediaowner_username";
    public static final String MEDIAOWNER_PROFILEPIC = "mediaowner_profilepic";
    public static final String MEDIAOWNER_FULLNAME = "mediaowner_fullname";

    public static final String IMAGEURL = "imageurl";

    public static final String CREATEDTIME = "created_time";

    public static final String LIKES_COUNT = "likes_count";

    public static final String COMMENTS_COUNT = "comments_count";

    public static final String LINK = "link";

    public static final String USERS_IN_PHOTO = "users_in_photo";

    public static final String VIDEOURL = "videourl";

    public static final String MEDIA_TYPE = "media_type";

    private static final String MEDIA_TBL_CREATE = (new StringBuffer())
            .append("CREATE TABLE ").append(TABLE_NAME).append(" ( ")
            .append(MEDIAID).append(" TEXT, ")
            .append(MEDIAOWNER_ID).append(" TEXT, ")
            .append(MEDIAOWNER_USERNAME).append(" TEXT, ")
            .append(MEDIAOWNER_PROFILEPIC).append(" TEXT, ")
            .append(MEDIAOWNER_FULLNAME).append(" TEXT, ")
            .append(IMAGEURL).append(" TEXT, ")
            .append(CREATEDTIME).append(" TEXT, ")
            .append(LIKES_COUNT).append(" TEXT, ")
            .append(COMMENTS_COUNT).append(" TEXT, ")
            .append(LINK).append(" TEXT, ")
            .append(USERS_IN_PHOTO).append(" TEXT, ")
            .append(VIDEOURL).append(" TEXT, ")
            .append(MEDIA_TYPE).append(" TEXT ")
            .append(");").toString();

    private static final String MEDIA_TBL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static String TAG = PAID_MEDIA.class.getSimpleName();

    public static String createTable() {
        Log.d(TAG, TABLE_NAME + " TABLE CREATED");
        return MEDIA_TBL_CREATE;
    }

    public static String dropTable() {
        return MEDIA_TBL_DROP;
    }

}
