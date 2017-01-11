package com.instainsight.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_FOLLOWERS = "Followers";
    public static final String TABLE_FOLLOWING = "Following";

    // column names for TABLE_USERS
    public static final String KEY_BIO = "bio";
    public static final String KEY_FOLLOWERCOUNT = "followed_by";
    public static final String KEY_NEWFOLLOWERCOUNT = "new_followed_by";
    public static final String KEY_FOLLOWINGCOUNT = "follows";
    public static final String KEY_NEWFOLLOWINGCOUNT = "new_follows";


    // Common column names for TABLE_USERS, TABLE_FOLLOWERS & TABLE_FOLLOWING
    public static final String KEY_USERID = "id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PROFILEPIC = "profile_picture";
    public static final String KEY_FULLNAME = "full_name";
    public static final String KEY_CREATEDAT = "created_at";
    public static final String KEY_ISNEW = "isnew";

    public static final String CREATEUSERSTABLE = "CREATE TABLE " + TABLE_USERS + " ("
            + KEY_USERID + " TEXT,"
            + KEY_USERNAME + " TEXT,"
            + KEY_FULLNAME + " TEXT,"
            + KEY_PROFILEPIC + " TEXT,"
            + KEY_BIO + " TEXT,"
            + KEY_FOLLOWERCOUNT + " TEXT,"
            + KEY_FOLLOWINGCOUNT + " TEXT,"
            + KEY_NEWFOLLOWINGCOUNT + " TEXT,"
            + KEY_NEWFOLLOWERCOUNT + " TEXT)";
    ;
    public static final String CREATEFOLLOWERSTABLE = "CREATE TABLE " + TABLE_FOLLOWERS + " ("
            + KEY_USERID + " TEXT,"
            + KEY_USERNAME + " TEXT,"
            + KEY_PROFILEPIC + " TEXT,"
            + KEY_FULLNAME + " TEXT,"
            + KEY_ISNEW + " TEXT,"
            + KEY_CREATEDAT + " TEXT)";
    public static final String CREATEFOLLOWINGTABLE = "CREATE TABLE " + TABLE_FOLLOWING + " ("
            + KEY_USERID + " TEXT,"
            + KEY_USERNAME + " TEXT,"
            + KEY_PROFILEPIC + " TEXT,"
            + KEY_FULLNAME + " TEXT,"
            + KEY_ISNEW + " TEXT,"
            + KEY_CREATEDAT + " TEXT)";

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "InstaInsight.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATEFOLLOWERSTABLE);
        db.execSQL(CREATEFOLLOWINGTABLE);
        db.execSQL(CREATEUSERSTABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop the table when need to upgrade database version

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLLOWERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLLOWING);
        // create new tables
        onCreate(db);
    }
}
