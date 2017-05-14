package com.instainsight.followersing.followers;

import android.content.Context;
import android.database.Cursor;
import android.os.SystemClock;
import android.util.Log;

import com.instainsight.db.DBManager;
import com.instainsight.db.DBQueries;
import com.instainsight.db.tables.USERS_FOLLOWEDBY;
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
                    USERS_FOLLOWEDBY.TABLE_NAME, // table name
                    null, // projection
                    USERS_FOLLOWEDBY.ID + " = ?", // selection
                    new String[]{followedBy.getId()}, // selection args
                    null,
                    null, // having
                    null, // order by
                    null // limit
            );
            valuesMap.put(USERS_FOLLOWEDBY.ID, followedBy.getId());
            valuesMap.put(USERS_FOLLOWEDBY.USERNAME, followedBy.getUserName());
            valuesMap.put(USERS_FOLLOWEDBY.FULLNAME, followedBy.getFullName());
            valuesMap.put(USERS_FOLLOWEDBY.PROFILEPICTURE, followedBy.getProfilePic());
            valuesMap.put(USERS_FOLLOWEDBY.BIO, "");
            valuesMap.put(USERS_FOLLOWEDBY.WEBSITE, "");
            valuesMap.put(USERS_FOLLOWEDBY.MEDIA_COUNT, "");
            valuesMap.put(USERS_FOLLOWEDBY.FOLLOWED_BY_COUNT, "");
            valuesMap.put(USERS_FOLLOWEDBY.FOLLOWS_COUNT, "");
            valuesMap.put(USERS_FOLLOWEDBY.FOLLOWED_BY, "1");
            //Follows will not put as it has only followed by data

            long insertUpdateRows = -1;
            if (cursor.getCount() == 0) {
                valuesMap.put(USERS_FOLLOWEDBY.ISNEW_FOLLOWED_BY, "1");
                valuesMap.put(USERS_FOLLOWEDBY.CREATEDTIME, "" + SystemClock.currentThreadTimeMillis());
                columnDetails.add(valuesMap);
//                insert data
                insertUpdateRows = dbQueries.insertValues(USERS_FOLLOWEDBY.TABLE_NAME, columnDetails);
            } else {
//                update data
                columnDetails.add(valuesMap);
                insertUpdateRows = dbQueries.updateFollowedBy(USERS_FOLLOWEDBY.TABLE_NAME, columnDetails);
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
                USERS_FOLLOWEDBY.TABLE_NAME, // table name
                null, // projection
                USERS_FOLLOWEDBY.ISNEW_FOLLOWED_BY + " = ? AND " + USERS_FOLLOWEDBY.FOLLOWED_BY + " = ?", // selection
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
                followedByBean.setId(cursor.getString(cursor.getColumnIndex(USERS_FOLLOWEDBY.ID)));
                followedByBean.setUserName(cursor.getString(cursor.getColumnIndex(USERS_FOLLOWEDBY.USERNAME)));
                followedByBean.setFullName(cursor.getString(cursor.getColumnIndex(USERS_FOLLOWEDBY.FULLNAME)));
                followedByBean.setProfilePic(cursor.getString(cursor.getColumnIndex(USERS_FOLLOWEDBY.PROFILEPICTURE)));
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
                    USERS_FOLLOWEDBY.TABLE_NAME, // table name
                    null, // projection
                    USERS_FOLLOWEDBY.ID + " = ?", // selection
                    new String[]{follows.getId()}, // selection args
                    null,
                    null, // having
                    null, // order by
                    null // limit
            );
            valuesMap.put(USERS_FOLLOWEDBY.ID, follows.getId());
            valuesMap.put(USERS_FOLLOWEDBY.USERNAME, follows.getUserName());
            valuesMap.put(USERS_FOLLOWEDBY.FULLNAME, follows.getFullName());
            valuesMap.put(USERS_FOLLOWEDBY.PROFILEPICTURE, follows.getProfilePic());
            valuesMap.put(USERS_FOLLOWEDBY.BIO, "");
            valuesMap.put(USERS_FOLLOWEDBY.WEBSITE, "");
            valuesMap.put(USERS_FOLLOWEDBY.MEDIA_COUNT, "");
            valuesMap.put(USERS_FOLLOWEDBY.FOLLOWED_BY_COUNT, "");
            valuesMap.put(USERS_FOLLOWEDBY.FOLLOWS_COUNT, "");
            //Follows will not put as it has only followed by data

            if (cursor.getCount() > 0) {
                valuesMap.put(USERS_FOLLOWEDBY.ISNEW_FOLLOWED_BY, "0");
                // update data
                columnDetails.add(valuesMap);
                insertUpdateRows = insertUpdateRows + dbQueries.updateFollowedBy(USERS_FOLLOWEDBY.TABLE_NAME, columnDetails);
            }
            Log.d(TAG, "removeFollowedByFromNewFollowedBy:insertUpdateValues::" + insertUpdateRows);
        }
        readWriteLock.readLock().unlock();
        return Observable.just(insertUpdateRows);
    }

    public void getFollowedByNotInRelation(ArrayList<FollowerBean> arylstFollowedByFromInsta) {
        ArrayList<FollowerBean> arylstFollowedBy = new ArrayList<>();
        readWriteLock.readLock().lock();

        StringBuilder usersNotIn = new StringBuilder();

        for (int i = 0; i < arylstFollowedByFromInsta.size(); i++) {
            if (usersNotIn.toString().isEmpty())
                usersNotIn.append("'").append(arylstFollowedByFromInsta.get(i).getId()).append("'");
            else
                usersNotIn.append(", '").append(arylstFollowedByFromInsta.get(i).getId()).append("'");
        }

        Cursor cursor = manager.getDB().rawQuery("Select * from " + USERS_FOLLOWEDBY.TABLE_NAME
                + " WHERE " + USERS_FOLLOWEDBY.ID + " NOT IN(" + usersNotIn.toString() + ")", null);

        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                FollowerBean followedByBean = new FollowerBean();
                followedByBean.setId(cursor.getString(cursor.getColumnIndex(USERS_FOLLOWEDBY.ID)));
                followedByBean.setUserName(cursor.getString(cursor.getColumnIndex(USERS_FOLLOWEDBY.USERNAME)));
                followedByBean.setFullName(cursor.getString(cursor.getColumnIndex(USERS_FOLLOWEDBY.FULLNAME)));
                followedByBean.setProfilePic(cursor.getString(cursor.getColumnIndex(USERS_FOLLOWEDBY.PROFILEPICTURE)));
                arylstFollowedBy.add(followedByBean);
                cursor.moveToNext();
            }
        }
        cursor.close();
        readWriteLock.readLock().unlock();
        deleteFollowedByNotInRelation(arylstFollowedBy);
    }

    private void deleteFollowedByNotInRelation(ArrayList<FollowerBean> arylstFollowedByToBeDeleted) {
        readWriteLock.readLock().lock();

        for (int i = 0; i < arylstFollowedByToBeDeleted.size(); i++) {
            manager.getDB().delete(USERS_FOLLOWEDBY.TABLE_NAME, USERS_FOLLOWEDBY.ID + " = ?",
                    new String[]{arylstFollowedByToBeDeleted.get(i).getId()});
        }
        readWriteLock.readLock().unlock();
    }

}

