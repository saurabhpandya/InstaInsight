package com.instainsight.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.instainsight.db.tables.LIKEDBYUSER;
import com.instainsight.db.tables.MEDIA;
import com.instainsight.db.tables.PROFILEVIEWER;
import com.instainsight.db.tables.RECENTMEDIA;
import com.instainsight.db.tables.USERS;
import com.instainsight.db.tables.USERSELF;
import com.instainsight.db.tables.USERS_NOT_FOLLOWING_BACK;
import com.instainsight.db.tables.USERS_UNFOLLOWERS;

public class DBManager {
    private static final String TAG = "DBManager";
    private static final String DB_NAME = "InstaInsight";
    private static int dbVersion = 4;

    private static DBManager adapter;

    //    private final Context context;
    private final DatabaseHelper dbHelper;
    private SQLiteDatabase dbObject;

    private DBManager(final Context ctx) {
//        this.context = ctx;
        dbHelper = new DatabaseHelper(ctx);
    }

    public static DBManager getInstance(final Context context) {
        try {
            synchronized (context) {
                if (adapter == null) {
                    adapter = new DBManager(context);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return adapter;
    }

    public SQLiteDatabase getDB() {
        return dbObject;
    }

    //---opens the database---
    public DBManager open() throws SQLException {
        dbObject = dbHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close() {
        dbHelper.close();
    }

    public void incrementCount() {
        dbVersion++;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(final Context context) {
            super(context, DB_NAME, null, dbVersion);
        }

        @Override
        public void onCreate(final SQLiteDatabase dbObj) {
            Log.d(TAG, "Inside onCreate");
            dbObj.execSQL(PROFILEVIEWER.createTable());
            dbObj.execSQL(LIKEDBYUSER.createTable());
            dbObj.execSQL(RECENTMEDIA.createTable());
            dbObj.execSQL(USERSELF.createTable());
            dbObj.execSQL(USERS.createTable());
            dbObj.execSQL(USERS_UNFOLLOWERS.createTable());
            dbObj.execSQL(USERS_NOT_FOLLOWING_BACK.createTable());
            dbObj.execSQL(MEDIA.createTable());
        }

        @Override
        public void onUpgrade(final SQLiteDatabase dbObj, final int oldVersion, final int newVersion) {
            dbVersion = newVersion;
            dbObj.execSQL(PROFILEVIEWER.dropTable());
            dbObj.execSQL(LIKEDBYUSER.dropTable());
            dbObj.execSQL(RECENTMEDIA.dropTable());
            dbObj.execSQL(USERSELF.dropTable());
            dbObj.execSQL(USERS.dropTable());
            dbObj.execSQL(USERS_UNFOLLOWERS.dropTable());
            dbObj.execSQL(USERS_NOT_FOLLOWING_BACK.dropTable());
            dbObj.execSQL(MEDIA.dropTable());
            onCreate(dbObj);
        }
    }
}

