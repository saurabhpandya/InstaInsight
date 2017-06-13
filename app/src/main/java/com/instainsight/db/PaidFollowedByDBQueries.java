package com.instainsight.db;

import android.content.Context;
import android.database.Cursor;
import android.os.SystemClock;
import android.util.Log;

import com.instainsight.db.tables.PAID_FOLLOWEDBY;
import com.instainsight.followersing.followers.bean.FollowerBean;
import com.instainsight.followersing.models.OtherUsersBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import io.reactivex.Observable;

/**
 * Created by SONY on 11-02-2017.
 */

public class PaidFollowedByDBQueries {
    private String TAG = PaidFollowedByDBQueries.class.getSimpleName();
    private Context context;
    private DBManager manager;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public PaidFollowedByDBQueries(Context context) {
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

    public long updatePrivateUser(String userId) {
        readWriteLock.readLock().lock();
        DBQueries dbQueries = new DBQueries(context);
        ArrayList<Map<String, Object>> columnDetails = new ArrayList<>();
        Map<String, Object> valuesMap = new HashMap<>();
        valuesMap.put(PAID_FOLLOWEDBY.ID, userId);
        valuesMap.put(PAID_FOLLOWEDBY.ISPRIVATE, "true");
        columnDetails.add(valuesMap);
        long updatedRows = dbQueries.updateFollowedBy(PAID_FOLLOWEDBY.TABLE_NAME, columnDetails);
        readWriteLock.readLock().unlock();
        return updatedRows;
    }

    public ArrayList<FollowerBean> getNonPrivateFollowedBy() {
        ArrayList<FollowerBean> arylstFollowedBy = new ArrayList<>();
        readWriteLock.readLock().lock();

        Cursor cursor = manager.getDB().query(
                true, // distinct
                PAID_FOLLOWEDBY.TABLE_NAME, // table name
                null, // projection
                PAID_FOLLOWEDBY.ISPRIVATE + " = ?", // selection
                new String[]{"false"}, // selection args
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
                followedByBean.setId(cursor.getString(cursor.getColumnIndex(PAID_FOLLOWEDBY.ID)));
                followedByBean.setUserName(cursor.getString(cursor.getColumnIndex(PAID_FOLLOWEDBY.USERNAME)));
                followedByBean.setFullName(cursor.getString(cursor.getColumnIndex(PAID_FOLLOWEDBY.FULLNAME)));
                followedByBean.setProfilePic(cursor.getString(cursor.getColumnIndex(PAID_FOLLOWEDBY.PROFILEPICTURE)));
                followedByBean.setPrivate(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(PAID_FOLLOWEDBY.ISPRIVATE))));
                arylstFollowedBy.add(followedByBean);
                cursor.moveToNext();
            }
        }
        cursor.close();
        readWriteLock.readLock().unlock();
        return arylstFollowedBy;
    }

    private ArrayList<FollowerBean> saveFollowedByToDB(ArrayList<FollowerBean> arylstFollowedBy) {
        readWriteLock.readLock().lock();

        DBQueries dbQueries = new DBQueries(context);
        long deletedRows = dbQueries.deleteTable(PAID_FOLLOWEDBY.TABLE_NAME);
        Log.d(TAG, "saveFollowedByToDB:deletedRows::" + deletedRows);
        ArrayList<Map<String, Object>> columnDetails = new ArrayList<>();
        for (FollowerBean followedBy : arylstFollowedBy) {

            Map<String, Object> valuesMap = new HashMap<>();
            valuesMap.put(PAID_FOLLOWEDBY.ID, followedBy.getId());
            valuesMap.put(PAID_FOLLOWEDBY.USERNAME, followedBy.getUserName());
            valuesMap.put(PAID_FOLLOWEDBY.FULLNAME, followedBy.getFullName());
            valuesMap.put(PAID_FOLLOWEDBY.PROFILEPICTURE, followedBy.getProfilePic());
            valuesMap.put(PAID_FOLLOWEDBY.BIO, "");
            valuesMap.put(PAID_FOLLOWEDBY.WEBSITE, "");
            valuesMap.put(PAID_FOLLOWEDBY.MEDIA_COUNT, "");
            valuesMap.put(PAID_FOLLOWEDBY.FOLLOWED_BY_COUNT, "");
            valuesMap.put(PAID_FOLLOWEDBY.FOLLOWS_COUNT, "");
            valuesMap.put(PAID_FOLLOWEDBY.FOLLOWED_BY, "1");
            valuesMap.put(PAID_FOLLOWEDBY.ISPRIVATE, "" + followedBy.isPrivate());
            //Follows will not put as it has only followed by data

            valuesMap.put(PAID_FOLLOWEDBY.ISNEW_FOLLOWED_BY, "1");
            valuesMap.put(PAID_FOLLOWEDBY.CREATEDTIME, "" + SystemClock.currentThreadTimeMillis());
            columnDetails.add(valuesMap);


        }
        long insertedRows = dbQueries.insertValues(PAID_FOLLOWEDBY.TABLE_NAME, columnDetails);
        Log.d(TAG, "saveFollowedByToDB:insertedRows::" + insertedRows);
        readWriteLock.readLock().unlock();
        return getFollowedBy();
    }

    public ArrayList<FollowerBean> getFollowedBy() {
        ArrayList<FollowerBean> arylstFollowedBy = new ArrayList<>();
        readWriteLock.readLock().lock();

        Cursor cursor = manager.getDB().query(
                true, // distinct
                PAID_FOLLOWEDBY.TABLE_NAME, // table name
                null, // projection
//                PAID_FOLLOWEDBY.ISNEW_FOLLOWED_BY + " = ? AND " + PAID_FOLLOWEDBY.FOLLOWED_BY + " = ?", // selection
//                new String[]{"1", "1"}, // selection args
                null, null,
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
                followedByBean.setId(cursor.getString(cursor.getColumnIndex(PAID_FOLLOWEDBY.ID)));
                followedByBean.setUserName(cursor.getString(cursor.getColumnIndex(PAID_FOLLOWEDBY.USERNAME)));
                followedByBean.setFullName(cursor.getString(cursor.getColumnIndex(PAID_FOLLOWEDBY.FULLNAME)));
                followedByBean.setProfilePic(cursor.getString(cursor.getColumnIndex(PAID_FOLLOWEDBY.PROFILEPICTURE)));
                followedByBean.setPrivate(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(PAID_FOLLOWEDBY.ISPRIVATE))));
                arylstFollowedBy.add(followedByBean);
                cursor.moveToNext();
            }
        }
        cursor.close();
        readWriteLock.readLock().unlock();
        return arylstFollowedBy;
    }

    public long updateFollowedByInfo(OtherUsersBean otherUsersBean) {
        readWriteLock.readLock().lock();
        DBQueries dbQueries = new DBQueries(context);
        ArrayList<Map<String, Object>> columnDetails = new ArrayList<>();
        Map<String, Object> valuesMap = new HashMap<>();
        valuesMap.put(PAID_FOLLOWEDBY.ID, otherUsersBean.getId());
        valuesMap.put(PAID_FOLLOWEDBY.FOLLOWED_BY_COUNT, otherUsersBean.getUserCountBean().getFollowed_by());
        valuesMap.put(PAID_FOLLOWEDBY.FOLLOWS_COUNT, otherUsersBean.getUserCountBean().getFollows());

        columnDetails.add(valuesMap);
        long updatedRows = dbQueries.updateFollowedBy(PAID_FOLLOWEDBY.TABLE_NAME, columnDetails);
        readWriteLock.readLock().unlock();
        return updatedRows;
    }

}

