package com.instainsight.db.tables;

import android.util.Log;

/**
 * Created by SONY on 26-02-2017.
 */

public class PROFILEVIEWER {

    public static final String TABLE_NAME = "PROFILEVIEWER";
    public static final String PROFILEVIEWER_USERNAME = "username";
    public static final String PROFILEVIEWER_FULLNAME = "full_name";
    public static final String PROFILEVIEWER_PROFILEPICTURE = "profile_picture";
    public static final String PROFILEVIEWER_ID = "id";
    public static final String PROFILEVIEWER_TYPE = "type";
    public static final String PROFILEVIEWER_POINTS = "points";
    private static final String PROFILEVIEWER_TBL_CREATE = (new StringBuffer())
            .append("CREATE TABLE ").append(TABLE_NAME).append(" ( ")
            .append(PROFILEVIEWER_USERNAME).append(" TEXT, ")
            .append(PROFILEVIEWER_FULLNAME).append(" TEXT, ")
            .append(PROFILEVIEWER_PROFILEPICTURE).append(" TEXT, ")
            .append(PROFILEVIEWER_ID).append(" TEXT, ")
            .append(PROFILEVIEWER_TYPE).append(" TEXT, ")
            .append(PROFILEVIEWER_POINTS).append(" INTEGER")
            .append(");").toString();
    private static final String PROFILEVIEWER_TBL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static String TAG = PROFILEVIEWER.class.getSimpleName();
    private String username;
    private String full_name;
    private String profile_picture;
    private String id;
    private String type;
    private int points;

    public static String createTable() {
        Log.d(TAG, TABLE_NAME + " TABLE CREATED");
        return PROFILEVIEWER_TBL_CREATE;
    }

    public static String dropTable() {
        return PROFILEVIEWER_TBL_DROP;
    }

}
