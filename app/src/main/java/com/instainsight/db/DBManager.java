package com.instainsight.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.instainsight.db.tables.LIKEDBYUSER;
import com.instainsight.db.tables.RECENTMEDIA;

public class DBManager {
    private static final String TAG = "DBManager";
    private static final String DB_NAME = "InstaInsight";
    private static int dbVersion = 2;

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
            dbObj.execSQL(LIKEDBYUSER.createTable());
            dbObj.execSQL(RECENTMEDIA.createTable());
        }

        @Override
        public void onUpgrade(final SQLiteDatabase dbObj, final int oldVersion, final int newVersion) {
            dbVersion = newVersion;
            dbObj.execSQL(LIKEDBYUSER.dropTable());
            dbObj.execSQL(RECENTMEDIA.dropTable());
            onCreate(dbObj);
        }
    }
}

