package com.instainsight.followersing.followers;

import android.content.Context;
import android.database.Cursor;
import android.os.SystemClock;
import android.util.Log;

import com.instainsight.db.DBManager;
import com.instainsight.db.DBQueries;
import com.instainsight.db.tables.USERS;
import com.instainsight.followersing.followers.bean.FollowerBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import io.reactivex.Observable;

/**
 * Created by SONY on 11-02-2017.
 */

public class FollowedByDBQueries {
    private String TAG = FollowedByDBQueries.class.getSimpleName();
    private Context context;
    private DBManager manager;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public FollowedByDBQueries(Context context) {
        this.context = context;
        getDBInstance();
    }

    private void getDBInstance() {
        manager = DBManager.getInstance(context);
        manager.open();
    }

    public Observable<ArrayList<FollowerBean>> saveFollowedBy(ArrayList<FollowerBean> arylstFollowedBy) {
        return Observable.just(saveFollowedByToDB(arylstFollowedBy));
    }

    private ArrayList<FollowerBean> saveFollowedByToDB(ArrayList<FollowerBean> arylstFollowedBy) {
        readWriteLock.readLock().lock();
        ArrayList<Map<String, Object>> columnDetails = new ArrayList<>();
        DBQueries dbQueries = new DBQueries(context);
        for (FollowerBean followedBy : arylstFollowedBy) {
            Map<String, Object> valuesMap = new HashMap<>();

            Cursor cursor = manager.getDB().query(
                    USERS.TABLE_NAME, // table name
                    null, // projection
                    USERS.ID + " = ?", // selection
                    new String[]{followedBy.getId()}, // selection args
                    null,
                    null, // having
                    null, // order by
                    null // limit
            );
            valuesMap.put(USERS.ID, followedBy.getId());
            valuesMap.put(USERS.USERNAME, followedBy.getUserName());
            valuesMap.put(USERS.FULLNAME, followedBy.getFullName());
            valuesMap.put(USERS.PROFILEPICTURE, followedBy.getProfilePic());
            valuesMap.put(USERS.BIO, "");
            valuesMap.put(USERS.WEBSITE, "");
            valuesMap.put(USERS.MEDIA_COUNT, "");
            valuesMap.put(USERS.FOLLOWED_BY_COUNT, "");
            valuesMap.put(USERS.FOLLOWS_COUNT, "");
            valuesMap.put(USERS.FOLLOWED_BY, "1");
            //Follows will not put as it has only followed by data

            long insertUpdateRows = -1;
            if (cursor.getCount() == 0) {
                valuesMap.put(USERS.ISNEW_FOLLOWED_BY, "1");
                valuesMap.put(USERS.CREATEDTIME, "" + SystemClock.currentThreadTimeMillis());
                columnDetails.add(valuesMap);
//                insert data
                insertUpdateRows = dbQueries.insertValues(USERS.TABLE_NAME, columnDetails);
            } else {
//                update data
                columnDetails.add(valuesMap);
                insertUpdateRows = dbQueries.updateFollowedBy(USERS.TABLE_NAME, columnDetails);
            }
            Log.d(TAG, "saveFollowedByToDB:insertUpdateValues::" + insertUpdateRows);
        }
        readWriteLock.readLock().unlock();
        return getFollowedBy();
    }

    public ArrayList<FollowerBean> getFollowedBy() {
        ArrayList<FollowerBean> arylstFollowedBy = new ArrayList<>();
        readWriteLock.readLock().lock();

        Cursor cursor = manager.getDB().query(
                true, // distinct
                USERS.TABLE_NAME, // table name
                null, // projection
                USERS.ISNEW_FOLLOWED_BY + " = ? AND " + USERS.FOLLOWED_BY + " = ?", // selection
                new String[]{"1", "1"}, // selection args
                null, // group by
                null, // having
                null, // order by
                null // limit
        );
        /*query(LIKEDBYUSER.TABLE_NAME, null, null, null, null, null, LIKEDBYUSER.LIKEDBYUSER_CREATEDTIME + " desc");*/

        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                FollowerBean followedByBean = new FollowerBean();
                followedByBean.setId(cursor.getString(cursor.getColumnIndex(USERS.ID)));
                followedByBean.setUserName(cursor.getString(cursor.getColumnIndex(USERS.USERNAME)));
                followedByBean.setFullName(cursor.getString(cursor.getColumnIndex(USERS.FULLNAME)));
                followedByBean.setProfilePic(cursor.getString(cursor.getColumnIndex(USERS.PROFILEPICTURE)));
                arylstFollowedBy.add(followedByBean);
                cursor.moveToNext();
            }
        }
        cursor.close();
        readWriteLock.readLock().unlock();
        return arylstFollowedBy;
    }

    public Observable<Long> removeFollowedByFromNewFollowedBy() {
        ArrayList<FollowerBean> arylstFollowedBy = getFollowedBy();
        readWriteLock.readLock().lock();
        ArrayList<Map<String, Object>> columnDetails = new ArrayList<>();
        DBQueries dbQueries = new DBQueries(context);
        long insertUpdateRows = -1;
        for (FollowerBean follows : arylstFollowedBy) {
            Map<String, Object> valuesMap = new HashMap<>();

            Cursor cursor = manager.getDB().query(
                    USERS.TABLE_NAME, // table name
                    null, // projection
                    USERS.ID + " = ?", // selection
                    new String[]{follows.getId()}, // selection args
                    null,
                    null, // having
                    null, // order by
                    null // limit
            );
            valuesMap.put(USERS.ID, follows.getId());
            valuesMap.put(USERS.USERNAME, follows.getUserName());
            valuesMap.put(USERS.FULLNAME, follows.getFullName());
            valuesMap.put(USERS.PROFILEPICTURE, follows.getProfilePic());
            valuesMap.put(USERS.BIO, "");
            valuesMap.put(USERS.WEBSITE, "");
            valuesMap.put(USERS.MEDIA_COUNT, "");
            valuesMap.put(USERS.FOLLOWED_BY_COUNT, "");
            valuesMap.put(USERS.FOLLOWS_COUNT, "");
            //Follows will not put as it has only followed by data


            if (cursor.getCount() > 0) {
                valuesMap.put(USERS.ISNEW_FOLLOWED_BY, "0");
                // update data
                columnDetails.add(valuesMap);
                insertUpdateRows = insertUpdateRows + dbQueries.updateFollowedBy(USERS.TABLE_NAME, columnDetails);
            }
            Log.d(TAG, "removeFollowedByFromNewFollowedBy:insertUpdateValues::" + insertUpdateRows);
        }
        readWriteLock.readLock().unlock();
        return Observable.just(insertUpdateRows);
    }
}

