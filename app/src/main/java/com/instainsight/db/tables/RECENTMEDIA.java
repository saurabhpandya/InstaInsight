package com.instainsight.db.tables;

import android.util.Log;

/**
 * Created by SONY on 12-02-2017.
 */

public class RECENTMEDIA {

    public static final String TABLE_NAME = "RECENTMEDIA";

    public static final String CREATEDTIME = "created_time";
    public static final String LINK = "link";
    public static final String LOCATIONNAME = "location_name";
    public static final String LOCATIONLAT = "location_latitude";
    public static final String LOCATIONLONG = "location_longitude";
    public static final String LOCATIONID = "location_id";
    public static final String TAGS = "tags";
    public static final String USERHASLIKED = "user_has_liked";
    public static final String LIKES_COUNT = "likes_count";
    public static final String MEDIAID = "media_id";
    public static final String ATTRIBUTION = "attribution";
    public static final String IMAGEURL = "imageurl";
    public static final String MEDIA_TYPE = "media_type";
    public static final String FILTER = "filter";
    public static final String COMMENTS_COUNT = "comments_count";
    public static final String MEDIAOWNER_ID = "mediaowner_id";
    public static final String MEDIAOWNER_USERNAME = "mediaowner_username";
    public static final String MEDIAOWNER_PROFILEPIC = "mediaowner_profilepic";
    public static final String MEDIAOWNER_FULLNAME = "mediaowner_fullname";

    private static final String RECENTMEDIA_TBL_CREATE = (new StringBuffer())
            .append("CREATE TABLE ").append(TABLE_NAME).append(" ( ")
            .append(CREATEDTIME).append(" TEXT, ")
            .append(LINK).append(" TEXT, ")
            .append(LOCATIONNAME).append(" TEXT, ")
            .append(LOCATIONLAT).append(" TEXT, ")
            .append(LOCATIONLONG).append(" TEXT, ")
            .append(LOCATIONID).append(" TEXT, ")
            .append(TAGS).append(" TEXT, ")
            .append(USERHASLIKED).append(" TEXT, ")
            .append(LIKES_COUNT).append(" TEXT, ")
            .append(MEDIAID).append(" TEXT, ")
            .append(ATTRIBUTION).append(" TEXT, ")
            .append(IMAGEURL).append(" TEXT, ")
            .append(MEDIA_TYPE).append(" TEXT, ")
            .append(FILTER).append(" TEXT, ")
            .append(COMMENTS_COUNT).append(" TEXT, ")
            .append(MEDIAOWNER_ID).append(" TEXT, ")
            .append(MEDIAOWNER_USERNAME).append(" TEXT, ")
            .append(MEDIAOWNER_PROFILEPIC).append(" TEXT, ")
            .append(MEDIAOWNER_FULLNAME).append(" TEXT")
            .append(");").toString();
    private static final String RECENTMEDIA_TBL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private static String TAG = LIKEDBYUSER.class.getSimpleName();

    public static String createTable() {
        Log.d(TAG, TABLE_NAME + " TABLE CREATED");
        return RECENTMEDIA_TBL_CREATE;
    }

    public static String dropTable() {
        return RECENTMEDIA_TBL_DROP;
    }


}
