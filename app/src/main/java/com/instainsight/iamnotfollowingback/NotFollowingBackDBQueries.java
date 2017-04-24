package com.instainsight.iamnotfollowingback;

import android.content.Context;
import android.database.Cursor;
import android.os.SystemClock;
import android.util.Log;

import com.instainsight.db.DBManager;
import com.instainsight.db.DBQueries;
import com.instainsight.db.tables.USERS_NOT_FOLLOWING_BACK;
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

public class NotFollowingBackDBQueries {
    private String TAG = NotFollowingBackDBQueries.class.getSimpleName();
    private Context context;
    private DBManager manager;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public NotFollowingBackDBQueries(Context context) {
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
                    USERS_NOT_FOLLOWING_BACK.TABLE_NAME, // table name
                    null, // projection
                    USERS_NOT_FOLLOWING_BACK.ID + " = ?", // selection
                    new String[]{follows.getId()}, // selection args
                    null,
                    null, // having
                    null, // order by
                    null // limit
            );
            valuesMap.put(USERS_NOT_FOLLOWING_BACK.ID, follows.getId());
            valuesMap.put(USERS_NOT_FOLLOWING_BACK.USERNAME, follows.getUserName());
            valuesMap.put(USERS_NOT_FOLLOWING_BACK.FULLNAME, follows.getFullName());
            valuesMap.put(USERS_NOT_FOLLOWING_BACK.PROFILEPICTURE, follows.getProfilePic());
            valuesMap.put(USERS_NOT_FOLLOWING_BACK.BIO, "");
            valuesMap.put(USERS_NOT_FOLLOWING_BACK.WEBSITE, "");
            valuesMap.put(USERS_NOT_FOLLOWING_BACK.MEDIA_COUNT, "");
            valuesMap.put(USERS_NOT_FOLLOWING_BACK.FOLLOWED_BY_COUNT, "");
            valuesMap.put(USERS_NOT_FOLLOWING_BACK.FOLLOWS_COUNT, "");
            valuesMap.put(USERS_NOT_FOLLOWING_BACK.FOLLOWS, "1");
            //Follows will not put as it has only followed by data


            if (cursor.getCount() == 0) {
                valuesMap.put(USERS_NOT_FOLLOWING_BACK.ISNEW_FOLLOWS, "1");
                valuesMap.put(USERS_NOT_FOLLOWING_BACK.CREATEDTIME, "" + SystemClock.currentThreadTimeMillis());
                columnDetails.add(valuesMap);
//                insert data

                long insertedRows = dbQueries.insertValues(USERS_NOT_FOLLOWING_BACK.TABLE_NAME, columnDetails);
                if (insertedRows > 0)
                    insertUpdateRows = insertUpdateRows + insertedRows;
            } else {
//                update data
                columnDetails.add(valuesMap);
                long updatedRows = dbQueries.updateFollowedBy(USERS_NOT_FOLLOWING_BACK.TABLE_NAME, columnDetails);
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
                USERS_NOT_FOLLOWING_BACK.TABLE_NAME, // table name
                null, // projection
                USERS_NOT_FOLLOWING_BACK.ISNEW_FOLLOWS + " = ? AND " + USERS_NOT_FOLLOWING_BACK.FOLLOWS + " = ?", // selection
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
                followsBean.setId(cursor.getString(cursor.getColumnIndex(USERS_NOT_FOLLOWING_BACK.ID)));
                followsBean.setUserName(cursor.getString(cursor.getColumnIndex(USERS_NOT_FOLLOWING_BACK.USERNAME)));
                followsBean.setFullName(cursor.getString(cursor.getColumnIndex(USERS_NOT_FOLLOWING_BACK.FULLNAME)));
                followsBean.setProfilePic(cursor.getString(cursor.getColumnIndex(USERS_NOT_FOLLOWING_BACK.PROFILEPICTURE)));
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
                    USERS_NOT_FOLLOWING_BACK.TABLE_NAME, // table name
                    null, // projection
                    USERS_NOT_FOLLOWING_BACK.ID + " = ?", // selection
                    new String[]{followedBy.getId()}, // selection args
                    null,
                    null, // having
                    null, // order by
                    null // limit
            );
            valuesMap.put(USERS_NOT_FOLLOWING_BACK.ID, followedBy.getId());
            valuesMap.put(USERS_NOT_FOLLOWING_BACK.USERNAME, followedBy.getUserName());
            valuesMap.put(USERS_NOT_FOLLOWING_BACK.FULLNAME, followedBy.getFullName());
            valuesMap.put(USERS_NOT_FOLLOWING_BACK.PROFILEPICTURE, followedBy.getProfilePic());
            valuesMap.put(USERS_NOT_FOLLOWING_BACK.BIO, "");
            valuesMap.put(USERS_NOT_FOLLOWING_BACK.WEBSITE, "");
            valuesMap.put(USERS_NOT_FOLLOWING_BACK.MEDIA_COUNT, "");
            valuesMap.put(USERS_NOT_FOLLOWING_BACK.FOLLOWED_BY_COUNT, "");
            valuesMap.put(USERS_NOT_FOLLOWING_BACK.FOLLOWS_COUNT, "");
            valuesMap.put(USERS_NOT_FOLLOWING_BACK.FOLLOWED_BY, "1");
            //Follows will not put as it has only followed by data


            if (cursor.getCount() == 0) {
                valuesMap.put(USERS_NOT_FOLLOWING_BACK.ISNEW_FOLLOWED_BY, "1");
                valuesMap.put(USERS_NOT_FOLLOWING_BACK.CREATEDTIME, "" + SystemClock.currentThreadTimeMillis());
                columnDetails.add(valuesMap);
//                insert data
                long insertedRows = dbQueries.insertValues(USERS_NOT_FOLLOWING_BACK.TABLE_NAME, columnDetails);
                if (insertedRows > 0)
                    insertUpdateRows = insertUpdateRows + insertedRows;
            } else {
//                update data
                columnDetails.add(valuesMap);
                long updatedRows = dbQueries.updateFollowedBy(USERS_NOT_FOLLOWING_BACK.TABLE_NAME, columnDetails);
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
                USERS_NOT_FOLLOWING_BACK.TABLE_NAME, // table name
                null, // projection
                USERS_NOT_FOLLOWING_BACK.ISNEW_FOLLOWED_BY + " = ? AND " + USERS_NOT_FOLLOWING_BACK.FOLLOWED_BY + " = ?", // selection
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
                followedByBean.setId(cursor.getString(cursor.getColumnIndex(USERS_NOT_FOLLOWING_BACK.ID)));
                followedByBean.setUserName(cursor.getString(cursor.getColumnIndex(USERS_NOT_FOLLOWING_BACK.USERNAME)));
                followedByBean.setFullName(cursor.getString(cursor.getColumnIndex(USERS_NOT_FOLLOWING_BACK.FULLNAME)));
                followedByBean.setProfilePic(cursor.getString(cursor.getColumnIndex(USERS_NOT_FOLLOWING_BACK.PROFILEPICTURE)));
                arylstFollowedBy.add(followedByBean);
                cursor.moveToNext();
            }
        }
        cursor.close();
        readWriteLock.readLock().unlock();
        return arylstFollowedBy;
    }

    public Observable<ArrayList<FollowerBean>> getNotFollowingBackFromDB() {
        return Observable.just(getNotFollowingBack());
    }

    public ArrayList<FollowerBean> getNotFollowingBack() {
        ArrayList<FollowerBean> arylstUnFollowers = new ArrayList<>();
        readWriteLock.readLock().lock();

        Cursor cursor = manager.getDB().query(
//                true, // distinct
                USERS_NOT_FOLLOWING_BACK.TABLE_NAME, // table name
                null, // projection
                USERS_NOT_FOLLOWING_BACK.FOLLOWS + " = ? AND " + USERS_NOT_FOLLOWING_BACK.FOLLOWED_BY + " = ?", // selection
                new String[]{"0", "1"}, // selection args
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
                followedByBean.setId(cursor.getString(cursor.getColumnIndex(USERS_NOT_FOLLOWING_BACK.ID)));
                followedByBean.setUserName(cursor.getString(cursor.getColumnIndex(USERS_NOT_FOLLOWING_BACK.USERNAME)));
                followedByBean.setFullName(cursor.getString(cursor.getColumnIndex(USERS_NOT_FOLLOWING_BACK.FULLNAME)));
                followedByBean.setProfilePic(cursor.getString(cursor.getColumnIndex(USERS_NOT_FOLLOWING_BACK.PROFILEPICTURE)));
                arylstUnFollowers.add(followedByBean);
                cursor.moveToNext();
            }
        }
        cursor.close();
        readWriteLock.readLock().unlock();
        return arylstUnFollowers;
    }

    public long deleteNotFollowingBack() {
        readWriteLock.readLock().lock();
        DBQueries dbQueries = new DBQueries(context);
        long deletedData = dbQueries.deleteTable(USERS_NOT_FOLLOWING_BACK.TABLE_NAME);
        readWriteLock.readLock().unlock();
        return deletedData;
    }

}

