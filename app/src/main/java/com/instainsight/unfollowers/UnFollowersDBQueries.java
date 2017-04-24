package com.instainsight.unfollowers;

import android.content.Context;
import android.database.Cursor;
import android.os.SystemClock;
import android.util.Log;

import com.instainsight.db.DBManager;
import com.instainsight.db.DBQueries;
import com.instainsight.db.tables.USERS_UNFOLLOWERS;
import com.instainsight.followersing.followers.bean.FollowerBean;
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

public class UnFollowersDBQueries {
    private String TAG = UnFollowersDBQueries.class.getSimpleName();
    private Context context;
    private DBManager manager;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public UnFollowersDBQueries(Context context) {
        this.context = context;
        getDBInstance();
    }

    private void getDBInstance() {
        manager = DBManager.getInstance(context);
        manager.open();
    }

    public Observable<Long> saveFollows(ArrayList<FollowingBean> arylstFollows) {
        return Observable.just(saveFollowsToDB(arylstFollows));
    }

    private long saveFollowsToDB(ArrayList<FollowingBean> arylstFollows) {
        readWriteLock.readLock().lock();
        DBQueries dbQueries = new DBQueries(context);
        long insertUpdateRows = -1;
        for (FollowingBean follows : arylstFollows) {
            ArrayList<Map<String, Object>> columnDetails = new ArrayList<>();
            Map<String, Object> valuesMap = new HashMap<>();

            Cursor cursor = manager.getDB().query(
                    USERS_UNFOLLOWERS.TABLE_NAME, // table name
                    null, // projection
                    USERS_UNFOLLOWERS.ID + " = ?", // selection
                    new String[]{follows.getId()}, // selection args
                    null,
                    null, // having
                    null, // order by
                    null // limit
            );
            valuesMap.put(USERS_UNFOLLOWERS.ID, follows.getId());
            valuesMap.put(USERS_UNFOLLOWERS.USERNAME, follows.getUserName());
            valuesMap.put(USERS_UNFOLLOWERS.FULLNAME, follows.getFullName());
            valuesMap.put(USERS_UNFOLLOWERS.PROFILEPICTURE, follows.getProfilePic());
            valuesMap.put(USERS_UNFOLLOWERS.BIO, "");
            valuesMap.put(USERS_UNFOLLOWERS.WEBSITE, "");
            valuesMap.put(USERS_UNFOLLOWERS.MEDIA_COUNT, "");
            valuesMap.put(USERS_UNFOLLOWERS.FOLLOWED_BY_COUNT, "");
            valuesMap.put(USERS_UNFOLLOWERS.FOLLOWS_COUNT, "");
            valuesMap.put(USERS_UNFOLLOWERS.FOLLOWS, "1");
            //Followed by will not put as it has only follows data

            if (cursor.getCount() == 0) {
                valuesMap.put(USERS_UNFOLLOWERS.ISNEW_FOLLOWS, "1");
                valuesMap.put(USERS_UNFOLLOWERS.CREATEDTIME, "" + SystemClock.currentThreadTimeMillis());
                columnDetails.add(valuesMap);
//                insert data

                long insertedRows = dbQueries.insertValues(USERS_UNFOLLOWERS.TABLE_NAME, columnDetails);
                if (insertedRows > 0)
                    insertUpdateRows = insertUpdateRows + insertedRows;
            } else {
//                update data
                columnDetails.add(valuesMap);
                long updatedRows = dbQueries.updateFollowedBy(USERS_UNFOLLOWERS.TABLE_NAME, columnDetails);
                if (updatedRows > 0)
                    insertUpdateRows = insertUpdateRows + updatedRows;
            }
            Log.d(TAG, "saveFollowsToDB:insertUpdateValues::" + insertUpdateRows);
        }
        readWriteLock.readLock().unlock();
        return insertUpdateRows;
    }

    public ArrayList<FollowingBean> getFollows() {
        ArrayList<FollowingBean> arylstFollows = new ArrayList<>();
        readWriteLock.readLock().lock();

        Cursor cursor = manager.getDB().query(
                true, // distinct
                USERS_UNFOLLOWERS.TABLE_NAME, // table name
                null, // projection
                USERS_UNFOLLOWERS.ISNEW_FOLLOWS + " = ? AND " + USERS_UNFOLLOWERS.FOLLOWS + " = ?", // selection
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
                followsBean.setId(cursor.getString(cursor.getColumnIndex(USERS_UNFOLLOWERS.ID)));
                followsBean.setUserName(cursor.getString(cursor.getColumnIndex(USERS_UNFOLLOWERS.USERNAME)));
                followsBean.setFullName(cursor.getString(cursor.getColumnIndex(USERS_UNFOLLOWERS.FULLNAME)));
                followsBean.setProfilePic(cursor.getString(cursor.getColumnIndex(USERS_UNFOLLOWERS.PROFILEPICTURE)));
                arylstFollows.add(followsBean);
                cursor.moveToNext();
            }
        }
        cursor.close();
        readWriteLock.readLock().unlock();
        return arylstFollows;
    }

    public Observable<Long> saveFollowedBy(ArrayList<FollowerBean> arylstFollowedBy) {
        return Observable.just(saveFollowedByToDB(arylstFollowedBy));
    }

    private long saveFollowedByToDB(ArrayList<FollowerBean> arylstFollowedBy) {
        readWriteLock.readLock().lock();

        DBQueries dbQueries = new DBQueries(context);
        long insertUpdateRows = -1;
        for (FollowerBean followedBy : arylstFollowedBy) {
            ArrayList<Map<String, Object>> columnDetails = new ArrayList<>();
            Map<String, Object> valuesMap = new HashMap<>();

            Cursor cursor = manager.getDB().query(
                    USERS_UNFOLLOWERS.TABLE_NAME, // table name
                    null, // projection
                    USERS_UNFOLLOWERS.ID + " = ?", // selection
                    new String[]{followedBy.getId()}, // selection args
                    null,
                    null, // having
                    null, // order by
                    null // limit
            );
            valuesMap.put(USERS_UNFOLLOWERS.ID, followedBy.getId());
            valuesMap.put(USERS_UNFOLLOWERS.USERNAME, followedBy.getUserName());
            valuesMap.put(USERS_UNFOLLOWERS.FULLNAME, followedBy.getFullName());
            valuesMap.put(USERS_UNFOLLOWERS.PROFILEPICTURE, followedBy.getProfilePic());
            valuesMap.put(USERS_UNFOLLOWERS.BIO, "");
            valuesMap.put(USERS_UNFOLLOWERS.WEBSITE, "");
            valuesMap.put(USERS_UNFOLLOWERS.MEDIA_COUNT, "");
            valuesMap.put(USERS_UNFOLLOWERS.FOLLOWED_BY_COUNT, "");
            valuesMap.put(USERS_UNFOLLOWERS.FOLLOWS_COUNT, "");
            valuesMap.put(USERS_UNFOLLOWERS.FOLLOWED_BY, "1");
            //Follows will not put as it has only followed by data

            if (cursor.getCount() == 0) {
                valuesMap.put(USERS_UNFOLLOWERS.ISNEW_FOLLOWED_BY, "1");
                valuesMap.put(USERS_UNFOLLOWERS.CREATEDTIME, "" + SystemClock.currentThreadTimeMillis());
                columnDetails.add(valuesMap);
//                insert data
                long insertedRows = dbQueries.insertValues(USERS_UNFOLLOWERS.TABLE_NAME, columnDetails);
                if (insertedRows > 0)
                    insertUpdateRows = insertUpdateRows + insertedRows;
            } else {
//                update data
                columnDetails.add(valuesMap);
                long updatedRows = dbQueries.updateFollowedBy(USERS_UNFOLLOWERS.TABLE_NAME, columnDetails);
                if (updatedRows > 0)
                    insertUpdateRows = insertUpdateRows + updatedRows;
            }
            Log.d(TAG, "saveFollowedByToDB:insertUpdateValues::" + insertUpdateRows);
        }
        readWriteLock.readLock().unlock();
        return insertUpdateRows;
    }

    public ArrayList<FollowerBean> getFollowedBy() {
        ArrayList<FollowerBean> arylstFollowedBy = new ArrayList<>();
        readWriteLock.readLock().lock();

        Cursor cursor = manager.getDB().query(
                true, // distinct
                USERS_UNFOLLOWERS.TABLE_NAME, // table name
                null, // projection
                USERS_UNFOLLOWERS.ISNEW_FOLLOWED_BY + " = ? AND " + USERS_UNFOLLOWERS.FOLLOWED_BY + " = ?", // selection
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
                followedByBean.setId(cursor.getString(cursor.getColumnIndex(USERS_UNFOLLOWERS.ID)));
                followedByBean.setUserName(cursor.getString(cursor.getColumnIndex(USERS_UNFOLLOWERS.USERNAME)));
                followedByBean.setFullName(cursor.getString(cursor.getColumnIndex(USERS_UNFOLLOWERS.FULLNAME)));
                followedByBean.setProfilePic(cursor.getString(cursor.getColumnIndex(USERS_UNFOLLOWERS.PROFILEPICTURE)));
                arylstFollowedBy.add(followedByBean);
                cursor.moveToNext();
            }
        }
        cursor.close();
        readWriteLock.readLock().unlock();
        return arylstFollowedBy;
    }

    public Observable<ArrayList<FollowerBean>> getUnFollowersFromDB() {
        return Observable.just(getUnFollowers());
    }

    public ArrayList<FollowerBean> getUnFollowers() {
        ArrayList<FollowerBean> arylstUnFollowers = new ArrayList<>();
        readWriteLock.readLock().lock();

        Cursor cursor = manager.getDB().query(
//                true, // distinct
                USERS_UNFOLLOWERS.TABLE_NAME, // table name
                null, // projection
//                null, null,
                USERS_UNFOLLOWERS.FOLLOWS + " = ? AND " + USERS_UNFOLLOWERS.FOLLOWED_BY + " = ?", // selection
                new String[]{"1", "0"}, // selection args
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
                followedByBean.setId(cursor.getString(cursor.getColumnIndex(USERS_UNFOLLOWERS.ID)));
                followedByBean.setUserName(cursor.getString(cursor.getColumnIndex(USERS_UNFOLLOWERS.USERNAME)));
                followedByBean.setFullName(cursor.getString(cursor.getColumnIndex(USERS_UNFOLLOWERS.FULLNAME)));
                followedByBean.setProfilePic(cursor.getString(cursor.getColumnIndex(USERS_UNFOLLOWERS.PROFILEPICTURE)));
                arylstUnFollowers.add(followedByBean);
                cursor.moveToNext();
            }
        }
        cursor.close();
        readWriteLock.readLock().unlock();
        return arylstUnFollowers;
    }

    public long deleteUnFollowers() {
        readWriteLock.readLock().lock();
        DBQueries dbQueries = new DBQueries(context);
        long deletedData = dbQueries.deleteTable(USERS_UNFOLLOWERS.TABLE_NAME);
        readWriteLock.readLock().unlock();
        return deletedData;
    }

}

