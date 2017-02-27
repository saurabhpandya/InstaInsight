package com.instainsight.whoviewedprofile;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.instainsight.db.DBManager;
import com.instainsight.db.DBQueries;
import com.instainsight.db.tables.PROFILEVIEWER;
import com.instainsight.media.RecentMediaDBQueries;
import com.instainsight.whoviewedprofile.model.WhoViewedProfileBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by SONY on 26-02-2017.
 */

public class ProfileViewerDBQueries {
    private String TAG = RecentMediaDBQueries.class.getSimpleName();
    private Context mContext;
    private DBManager manager;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public ProfileViewerDBQueries(Context context) {
        mContext = context;
        getDBInstance();
    }

    private void getDBInstance() {
        manager = DBManager.getInstance(mContext);
        manager.open();
    }

    public void deleteProfileViewersFromDB() {
        DBQueries dbQueries = new DBQueries(mContext);
        long deletedCount = dbQueries.deleteTable(PROFILEVIEWER.TABLE_NAME);
        Log.d(TAG, "deleteProfileViewersFromDB::deletedCount:" + deletedCount);

    }

    public ArrayList<WhoViewedProfileBean> saveProfileViewersToDb(ArrayList<WhoViewedProfileBean> arylstWhoViewedProfile) {
        readWriteLock.readLock().lock();
        ArrayList<Map<String, Object>> columnDetails = new ArrayList<>();

        for (WhoViewedProfileBean whoViewedProfileBean : arylstWhoViewedProfile) {
            Map<String, Object> valuesMap = new HashMap<>();

            valuesMap.put(PROFILEVIEWER.PROFILEVIEWER_ID, whoViewedProfileBean.getId());
            valuesMap.put(PROFILEVIEWER.PROFILEVIEWER_FULLNAME, whoViewedProfileBean.getFull_name());
            valuesMap.put(PROFILEVIEWER.PROFILEVIEWER_POINTS, whoViewedProfileBean.getPoints());
            valuesMap.put(PROFILEVIEWER.PROFILEVIEWER_PROFILEPICTURE, whoViewedProfileBean.getProfile_picture());
            valuesMap.put(PROFILEVIEWER.PROFILEVIEWER_TYPE, whoViewedProfileBean.getType());
            valuesMap.put(PROFILEVIEWER.PROFILEVIEWER_USERNAME, whoViewedProfileBean.getUsername());
            columnDetails.add(valuesMap);
        }
        DBQueries dbQueries = new DBQueries(mContext);
        long insertedRows = dbQueries.insertValues(PROFILEVIEWER.TABLE_NAME, columnDetails);
        Log.d(TAG, "saveProfileViewersToDb:insertValues::" + insertedRows);
        readWriteLock.readLock().unlock();
        return getWhoViewedProfile();
    }

    public ArrayList<WhoViewedProfileBean> getWhoViewedProfileByUser(String userId) {
        ArrayList<WhoViewedProfileBean> arylstWhoViewedProfile = new ArrayList<WhoViewedProfileBean>();
        readWriteLock.readLock().lock();

        Cursor cursor = manager.getDB().query(
//                true, // distinct
                PROFILEVIEWER.TABLE_NAME, // table name
                null, // projection
                PROFILEVIEWER.PROFILEVIEWER_ID + " = ?", // selection
                new String[]{userId}, // selection args
                null, // group by
                null, // having
                null,
//                PROFILEVIEWER.PROFILEVIEWER_ID + " desc", // order by
                null // limit
        );
        /*query(RECENTMEDIA.TABLE_NAME, null, null, null, null, null, RECENTMEDIA.LIKEDBYUSER_CREATEDTIME + " desc");*/

        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                WhoViewedProfileBean data = new WhoViewedProfileBean();

                data.setId(cursor.getString(cursor.getColumnIndex(PROFILEVIEWER.PROFILEVIEWER_ID)));
                data.setUsername(cursor.getString(cursor.getColumnIndex(PROFILEVIEWER.PROFILEVIEWER_USERNAME)));
                data.setFull_name(cursor.getString(cursor.getColumnIndex(PROFILEVIEWER.PROFILEVIEWER_FULLNAME)));
                data.setPoints(cursor.getInt(cursor.getColumnIndex(PROFILEVIEWER.PROFILEVIEWER_POINTS)));
                data.setProfile_picture(cursor.getString(cursor.getColumnIndex(PROFILEVIEWER.PROFILEVIEWER_PROFILEPICTURE)));
                data.setType(cursor.getString(cursor.getColumnIndex(PROFILEVIEWER.PROFILEVIEWER_TYPE)));
                arylstWhoViewedProfile.add(data);
                cursor.moveToNext();
            }
        }
        cursor.close();
        readWriteLock.readLock().unlock();
        return arylstWhoViewedProfile;
    }

    private ArrayList<WhoViewedProfileBean> getWhoViewedProfile() {
        ArrayList<WhoViewedProfileBean> arylstWhoViewedProfile = new ArrayList<WhoViewedProfileBean>();
        readWriteLock.readLock().lock();

        Cursor cursor = manager.getDB().query(
//                true, // distinct
                PROFILEVIEWER.TABLE_NAME, // table name
                null, // projection
                null, // selection
                null, // selection args
                PROFILEVIEWER.PROFILEVIEWER_ID, // group by
                null, // having
                null,
//                PROFILEVIEWER.PROFILEVIEWER_ID + " desc", // order by
                null // limit
        );
        /*query(RECENTMEDIA.TABLE_NAME, null, null, null, null, null, RECENTMEDIA.LIKEDBYUSER_CREATEDTIME + " desc");*/

        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                WhoViewedProfileBean data = new WhoViewedProfileBean();

                data.setId(cursor.getString(cursor.getColumnIndex(PROFILEVIEWER.PROFILEVIEWER_ID)));
                data.setUsername(cursor.getString(cursor.getColumnIndex(PROFILEVIEWER.PROFILEVIEWER_USERNAME)));
                data.setFull_name(cursor.getString(cursor.getColumnIndex(PROFILEVIEWER.PROFILEVIEWER_FULLNAME)));
                data.setPoints(cursor.getInt(cursor.getColumnIndex(PROFILEVIEWER.PROFILEVIEWER_POINTS)));
                data.setProfile_picture(cursor.getString(cursor.getColumnIndex(PROFILEVIEWER.PROFILEVIEWER_PROFILEPICTURE)));
                data.setType(cursor.getString(cursor.getColumnIndex(PROFILEVIEWER.PROFILEVIEWER_TYPE)));
                arylstWhoViewedProfile.add(data);
                cursor.moveToNext();
            }
        }
        cursor.close();
        readWriteLock.readLock().unlock();
        return arylstWhoViewedProfile;
    }

}
