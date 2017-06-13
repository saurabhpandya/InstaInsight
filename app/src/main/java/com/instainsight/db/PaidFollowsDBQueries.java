package com.instainsight.db;

import android.content.Context;
import android.database.Cursor;
import android.os.SystemClock;
import android.util.Log;

import com.instainsight.db.tables.PAID_FOLLOWS;
import com.instainsight.followersing.following.bean.FollowingBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import io.reactivex.Observable;

/**
 * Created by SONY on 11-02-2017.
 */

public class PaidFollowsDBQueries {
    private String TAG = PaidFollowsDBQueries.class.getSimpleName();
    private Context context;
    private DBManager manager;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public PaidFollowsDBQueries(Context context) {
        this.context = context;
        getDBInstance();
    }

    private void getDBInstance() {
        manager = DBManager.getInstance(context);
        manager.open();
    }

    public Observable<ArrayList<FollowingBean>> saveFollows(ArrayList<FollowingBean> arylstFollows) {
        return Observable.just(saveFollowsToDB(arylstFollows));
    }

    private ArrayList<FollowingBean> saveFollowsToDB(ArrayList<FollowingBean> arylstFollows) {
        readWriteLock.readLock().lock();
        ArrayList<Map<String, Object>> columnDetails = new ArrayList<>();
        DBQueries dbQueries = new DBQueries(context);
        for (FollowingBean follows : arylstFollows) {
            Map<String, Object> valuesMap = new HashMap<>();

            Cursor cursor = manager.getDB().query(
                    PAID_FOLLOWS.TABLE_NAME, // table name
                    null, // projection
                    PAID_FOLLOWS.ID + " = ?", // selection
                    new String[]{follows.getId()}, // selection args
                    null,
                    null, // having
                    null, // order by
                    null // limit
            );
            valuesMap.put(PAID_FOLLOWS.ID, follows.getId());
            valuesMap.put(PAID_FOLLOWS.USERNAME, follows.getUserName());
            valuesMap.put(PAID_FOLLOWS.FULLNAME, follows.getFullName());
            valuesMap.put(PAID_FOLLOWS.PROFILEPICTURE, follows.getProfilePic());
            valuesMap.put(PAID_FOLLOWS.BIO, "");
            valuesMap.put(PAID_FOLLOWS.WEBSITE, "");
            valuesMap.put(PAID_FOLLOWS.MEDIA_COUNT, "");
            valuesMap.put(PAID_FOLLOWS.FOLLOWED_BY_COUNT, "");
            valuesMap.put(PAID_FOLLOWS.FOLLOWS_COUNT, "");
            valuesMap.put(PAID_FOLLOWS.FOLLOWS, "1");
            //Follows will not put as it has only followed by data

            long insertUpdateRows = -1;
            if (cursor.getCount() == 0) {
                valuesMap.put(PAID_FOLLOWS.ISNEW_FOLLOWS, "1");
                valuesMap.put(PAID_FOLLOWS.CREATEDTIME, "" + SystemClock.currentThreadTimeMillis());
                columnDetails.add(valuesMap);
//                insert data
                insertUpdateRows = dbQueries.insertValues(PAID_FOLLOWS.TABLE_NAME, columnDetails);
                Log.d(TAG, "saveFollowsToDB:insert" + insertUpdateRows);
            } else {
//                update data
                columnDetails.add(valuesMap);
                insertUpdateRows = dbQueries.updateFollowedBy(PAID_FOLLOWS.TABLE_NAME, columnDetails);
                Log.d(TAG, "saveFollowsToDB:update" + insertUpdateRows);
            }
            Log.d(TAG, "saveFollowsToDB:insertUpdateValues::" + insertUpdateRows);
        }
        readWriteLock.readLock().unlock();
        return getFollows();
    }

    public ArrayList<FollowingBean> getFollows() {
        ArrayList<FollowingBean> arylstFollows = new ArrayList<>();
        readWriteLock.readLock().lock();

        Cursor cursor = manager.getDB().query(
                true, // distinct
                PAID_FOLLOWS.TABLE_NAME, // table name
                null, // projection
                PAID_FOLLOWS.ISNEW_FOLLOWS + " = ? AND " + PAID_FOLLOWS.FOLLOWS + " = ?", // selection
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
                FollowingBean followsBean = new FollowingBean();
                followsBean.setId(cursor.getString(cursor.getColumnIndex(PAID_FOLLOWS.ID)));
                followsBean.setUserName(cursor.getString(cursor.getColumnIndex(PAID_FOLLOWS.USERNAME)));
                followsBean.setFullName(cursor.getString(cursor.getColumnIndex(PAID_FOLLOWS.FULLNAME)));
                followsBean.setProfilePic(cursor.getString(cursor.getColumnIndex(PAID_FOLLOWS.PROFILEPICTURE)));
                arylstFollows.add(followsBean);
                cursor.moveToNext();
            }
        }
        cursor.close();
        readWriteLock.readLock().unlock();
        return arylstFollows;
    }

}

