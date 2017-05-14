package com.instainsight.followersing.following;

import android.content.Context;
import android.database.Cursor;
import android.os.SystemClock;
import android.util.Log;

import com.instainsight.db.DBManager;
import com.instainsight.db.DBQueries;
import com.instainsight.db.tables.USERS_FOLLOWS;
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

public class FollowsDBQueries {
    private String TAG = FollowsDBQueries.class.getSimpleName();
    private Context context;
    private DBManager manager;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public FollowsDBQueries(Context context) {
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
                    USERS_FOLLOWS.TABLE_NAME, // table name
                    null, // projection
                    USERS_FOLLOWS.ID + " = ?", // selection
                    new String[]{follows.getId()}, // selection args
                    null,
                    null, // having
                    null, // order by
                    null // limit
            );
            valuesMap.put(USERS_FOLLOWS.ID, follows.getId());
            valuesMap.put(USERS_FOLLOWS.USERNAME, follows.getUserName());
            valuesMap.put(USERS_FOLLOWS.FULLNAME, follows.getFullName());
            valuesMap.put(USERS_FOLLOWS.PROFILEPICTURE, follows.getProfilePic());
            valuesMap.put(USERS_FOLLOWS.BIO, "");
            valuesMap.put(USERS_FOLLOWS.WEBSITE, "");
            valuesMap.put(USERS_FOLLOWS.MEDIA_COUNT, "");
            valuesMap.put(USERS_FOLLOWS.FOLLOWED_BY_COUNT, "");
            valuesMap.put(USERS_FOLLOWS.FOLLOWS_COUNT, "");
            valuesMap.put(USERS_FOLLOWS.FOLLOWS, "1");
            //Follows will not put as it has only followed by data

            long insertUpdateRows = -1;
            if (cursor.getCount() == 0) {
                valuesMap.put(USERS_FOLLOWS.ISNEW_FOLLOWS, "1");
                valuesMap.put(USERS_FOLLOWS.CREATEDTIME, "" + SystemClock.currentThreadTimeMillis());
                columnDetails.add(valuesMap);
//                insert data
                insertUpdateRows = dbQueries.insertValues(USERS_FOLLOWS.TABLE_NAME, columnDetails);
                Log.d(TAG, "saveFollowsToDB:insert" + insertUpdateRows);
            } else {
//                update data
                columnDetails.add(valuesMap);
                insertUpdateRows = dbQueries.updateFollowedBy(USERS_FOLLOWS.TABLE_NAME, columnDetails);
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
                USERS_FOLLOWS.TABLE_NAME, // table name
                null, // projection
                USERS_FOLLOWS.ISNEW_FOLLOWS + " = ? AND " + USERS_FOLLOWS.FOLLOWS + " = ?", // selection
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
                followsBean.setId(cursor.getString(cursor.getColumnIndex(USERS_FOLLOWS.ID)));
                followsBean.setUserName(cursor.getString(cursor.getColumnIndex(USERS_FOLLOWS.USERNAME)));
                followsBean.setFullName(cursor.getString(cursor.getColumnIndex(USERS_FOLLOWS.FULLNAME)));
                followsBean.setProfilePic(cursor.getString(cursor.getColumnIndex(USERS_FOLLOWS.PROFILEPICTURE)));
                arylstFollows.add(followsBean);
                cursor.moveToNext();
            }
        }
        cursor.close();
        readWriteLock.readLock().unlock();
        return arylstFollows;
    }

    public Observable<Long> removeFollowsFromNewFollows() {
        ArrayList<FollowingBean> arylstFollows = getFollows();
        readWriteLock.readLock().lock();
        ArrayList<Map<String, Object>> columnDetails = new ArrayList<>();
        DBQueries dbQueries = new DBQueries(context);

        long insertUpdateRows = -1;
        for (FollowingBean follows : arylstFollows) {
            Map<String, Object> valuesMap = new HashMap<>();

            Cursor cursor = manager.getDB().query(
                    USERS_FOLLOWS.TABLE_NAME, // table name
                    null, // projection
                    USERS_FOLLOWS.ID + " = ?", // selection
                    new String[]{follows.getId()}, // selection args
                    null,
                    null, // having
                    null, // order by
                    null // limit
            );
            valuesMap.put(USERS_FOLLOWS.ID, follows.getId());
            valuesMap.put(USERS_FOLLOWS.USERNAME, follows.getUserName());
            valuesMap.put(USERS_FOLLOWS.FULLNAME, follows.getFullName());
            valuesMap.put(USERS_FOLLOWS.PROFILEPICTURE, follows.getProfilePic());
            valuesMap.put(USERS_FOLLOWS.BIO, "");
            valuesMap.put(USERS_FOLLOWS.WEBSITE, "");
            valuesMap.put(USERS_FOLLOWS.MEDIA_COUNT, "");
            valuesMap.put(USERS_FOLLOWS.FOLLOWED_BY_COUNT, "");
            valuesMap.put(USERS_FOLLOWS.FOLLOWS_COUNT, "");
            //Follows will not put as it has only followed by data


            if (cursor.getCount() > 0) {
                valuesMap.put(USERS_FOLLOWS.ISNEW_FOLLOWS, "0");
                // update data
                columnDetails.add(valuesMap);
                insertUpdateRows = insertUpdateRows + dbQueries.updateFollowedBy(USERS_FOLLOWS.TABLE_NAME, columnDetails);
            }
            Log.d(TAG, "removeFollowsFromNewFollows:insertUpdateValues::" + insertUpdateRows);
        }
        readWriteLock.readLock().unlock();
        return Observable.just(insertUpdateRows);
    }

    public void getFollowsNotInRelation(ArrayList<FollowingBean> arylstFollowingFromInsta) {
        ArrayList<FollowingBean> arylstFollows = new ArrayList<>();
        readWriteLock.readLock().lock();

        StringBuilder usersNotIn = new StringBuilder();

        for (int i = 0; i < arylstFollowingFromInsta.size(); i++) {
            if (usersNotIn.toString().isEmpty())
                usersNotIn.append("'").append(arylstFollowingFromInsta.get(i).getId()).append("'");
            else
                usersNotIn.append(", '").append(arylstFollowingFromInsta.get(i).getId()).append("'");
        }

        Cursor cursor = manager.getDB().rawQuery("Select * from " + USERS_FOLLOWS.TABLE_NAME
                + " WHERE " + USERS_FOLLOWS.ID + " NOT IN(" + usersNotIn.toString() + ")", null);

        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                FollowingBean followedByBean = new FollowingBean();
                followedByBean.setId(cursor.getString(cursor.getColumnIndex(USERS_FOLLOWS.ID)));
                followedByBean.setUserName(cursor.getString(cursor.getColumnIndex(USERS_FOLLOWS.USERNAME)));
                followedByBean.setFullName(cursor.getString(cursor.getColumnIndex(USERS_FOLLOWS.FULLNAME)));
                followedByBean.setProfilePic(cursor.getString(cursor.getColumnIndex(USERS_FOLLOWS.PROFILEPICTURE)));
                arylstFollows.add(followedByBean);
                cursor.moveToNext();
            }
        }
        cursor.close();
        readWriteLock.readLock().unlock();
        deleteFollowedByNotInRelation(arylstFollows);
    }

    private void deleteFollowedByNotInRelation(ArrayList<FollowingBean> arylstFollowedByToBeDeleted) {
        readWriteLock.readLock().lock();

        for (int i = 0; i < arylstFollowedByToBeDeleted.size(); i++) {
            manager.getDB().delete(USERS_FOLLOWS.TABLE_NAME, USERS_FOLLOWS.ID + " = ?",
                    new String[]{arylstFollowedByToBeDeleted.get(i).getId()});
        }
        readWriteLock.readLock().unlock();
    }

}

