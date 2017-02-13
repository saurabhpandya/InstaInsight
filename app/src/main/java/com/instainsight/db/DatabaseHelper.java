package com.instainsight.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_FOLLOWERS = "Followers";
    public static final String TABLE_FOLLOWING = "Following";
    public static final String TABLE_MEDIA = "Media";
    public static final String TABLE_LIKEDBYUSER = "LikedByUser";

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

    public static final String KEY_MEDIA_MEDIAID_ = "media_id";
    public static final String KEY_MEDIA_MEDIAID = "id";
    public static final String KEY_MEDIA_TYPE = "type";
    public static final String KEY_MEDIA_IMAGES = "images";
    public static final String KEY_MEDIA_STANDARDRESOLUTION = "standard_resolution";
    public static final String KEY_MEDIA_IMAGEURL = "url";
    public static final String KEY_MEDIA_IMAGEURL_ = "imageurl";
    public static final String KEY_MEDIA_LIKES = "likes";
    public static final String KEY_MEDIA_LIKESCOUNT = "count";
    public static final String KEY_MEDIA_LIKESCOUNT_ = "likes_count";
    public static final String KEY_MEDIA_USER = "user";
    public static final String KEY_MEDIA_USERID = "id";
    public static final String KEY_MEDIA_CREATEDTIME = "created_time";
    public static final String KEY_MEDIA_COMMENTS = "comments";
    public static final String KEY_MEDIA_COMMENTSCOUNT = "count";
    public static final String KEY_MEDIA_COMMENTSCOUNT_ = "comments_count";
    public static final String KEY_MEDIA_LINK = "link";

    public static final String KEY_LIKEDBYUSER_MEDIAID = "id";
    public static final String KEY_LIKEDBYUSER_MEDIAID_ = "link";

    public static final String CREATEMEDIA = "CREATE TABLE " + TABLE_MEDIA + " ("
            + KEY_MEDIA_MEDIAID_ + " TEXT,"
            + KEY_MEDIA_USERID + " TEXT,"
            + KEY_MEDIA_TYPE + " TEXT,"
            + KEY_MEDIA_IMAGEURL_ + " TEXT,"
            + KEY_MEDIA_LIKESCOUNT_ + " TEXT,"
            + KEY_MEDIA_CREATEDTIME + " TEXT,"
            + KEY_MEDIA_COMMENTSCOUNT_ + " TEXT,"
            + KEY_MEDIA_LINK + " TEXT);";

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
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "InstaInsight.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATEMEDIA);
        db.execSQL(CREATEFOLLOWERSTABLE);
        db.execSQL(CREATEFOLLOWINGTABLE);
        db.execSQL(CREATEUSERSTABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop the table when need to upgrade database version
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDIA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLLOWERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLLOWING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        // create new tables
        onCreate(db);
    }
}
