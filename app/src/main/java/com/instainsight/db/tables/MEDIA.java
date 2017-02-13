package com.instainsight.db.tables;

import android.util.Log;

/**
 * Created by SONY on 13-02-2017.
 */

public class MEDIA {

    public static final String TABLE_NAME = "MEDIA";
    public static final String ATTRIBUTION = "attribution";
    public static final String LIKES_COUNT = "likes_count";
    public static final String USERHASLIKED = "user_has_liked";
    public static final String LOCATIONNAME = "location_name";
    public static final String LOCATIONLAT = "location_latitude";
    public static final String LOCATIONLONG = "location_longitude";
    public static final String LOCATIONID = "location_id";
    public static final String FILTER = "filter";
    public static final String CREATEDTIME = "created_time";
    public static final String MEDIAOWNER_ID = "mediaowner_id";
    public static final String MEDIAOWNER_USERNAME = "mediaowner_username";
    public static final String MEDIAOWNER_PROFILEPIC = "mediaowner_profilepic";
    public static final String MEDIAOWNER_FULLNAME = "mediaowner_fullname";
    public static final String TAGS = "tags";
    public static final String MEDIAID = "media_id";
    public static final String COMMENTS_COUNT = "comments_count";
    public static final String USERS_IN_PHOTO = "users_in_photo";
    public static final String IMAGEURL = "imageurl";
    public static final String VIDEOURL = "videourl";
    public static final String CAPTION_TEXT = "caption_text";
    public static final String CAPTION_ID = "caption_id";
    public static final String LINK = "link";
    public static final String MEDIA_TYPE = "media_type";
    private static final String MEDIA_TBL_CREATE = (new StringBuffer())
            .append("CREATE TABLE ").append(TABLE_NAME).append(" ( ")
            .append(ATTRIBUTION).append(" TEXT, ")
            .append(LIKES_COUNT).append(" TEXT, ")
            .append(USERHASLIKED).append(" TEXT, ")
            .append(LOCATIONNAME).append(" TEXT, ")
            .append(LOCATIONLAT).append(" TEXT, ")
            .append(LOCATIONLONG).append(" TEXT, ")
            .append(LOCATIONID).append(" TEXT, ")
            .append(FILTER).append(" TEXT, ")
            .append(CREATEDTIME).append(" TEXT, ")
            .append(MEDIAOWNER_ID).append(" TEXT, ")
            .append(MEDIAOWNER_USERNAME).append(" TEXT, ")
            .append(MEDIAOWNER_PROFILEPIC).append(" TEXT, ")
            .append(MEDIAOWNER_FULLNAME).append(" TEXT, ")
            .append(TAGS).append(" TEXT, ")
            .append(MEDIAID).append(" TEXT, ")
            .append(COMMENTS_COUNT).append(" TEXT, ")
            .append(USERS_IN_PHOTO).append(" TEXT, ")
            .append(IMAGEURL).append(" TEXT, ")
            .append(VIDEOURL).append(" TEXT, ")
            .append(CAPTION_TEXT).append(" TEXT, ")
            .append(CAPTION_ID).append(" TEXT, ")
            .append(LINK).append(" TEXT, ")
            .append(MEDIA_TYPE).append(" TEXT ")
            .append(");").toString();
    private static final String MEDIA_TBL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static String TAG = MEDIA.class.getSimpleName();

    public static String createTable() {
        Log.d(TAG, TABLE_NAME + " TABLE CREATED");
        return MEDIA_TBL_CREATE;
    }

    public static String dropTable() {
        return MEDIA_TBL_DROP;
    }

}
