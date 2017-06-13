package com.instainsight.mostpopularfollowers;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.instainsight.db.DBManager;
import com.instainsight.db.tables.PAID_FOLLOWEDBY;
import com.instainsight.followersing.models.OtherUsersBean;
import com.instainsight.models.UserCountBean;

import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by SONY on 23-02-2017.
 */

public class MostPopularFollowersDBQueries {

    private String TAG = MostPopularFollowersDBQueries.class.getSimpleName();

    private Context mContext;
    private DBManager manager;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public MostPopularFollowersDBQueries(Context context) {
        mContext = context;
        getDBInstance();
    }

    private void getDBInstance() {
        manager = DBManager.getInstance(mContext);
        manager.open();
    }

    public ArrayList<OtherUsersBean> getMostPopularFollowers() {
        ArrayList<OtherUsersBean> arylstMostPopularFollowers = new ArrayList<OtherUsersBean>();
        readWriteLock.readLock().lock();

        Cursor cursor = manager.getDB().query(
//                false, // distinct
                PAID_FOLLOWEDBY.TABLE_NAME, // table name
                null, // projection
                null, //PAID_FOLLOWEDBY.ISPRIVATE + " = ?", // selection
                null, //new String[]{"false"}, // selection args
                null, // group by
                null, // having
                PAID_FOLLOWEDBY.FOLLOWED_BY_COUNT + " DESC" // order by,
        );

        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                OtherUsersBean mostPopularFollower = new OtherUsersBean();
                mostPopularFollower.setId(cursor.getString(cursor.getColumnIndex(PAID_FOLLOWEDBY.ID)));
                mostPopularFollower.setUsername(cursor.getString(cursor.getColumnIndex(PAID_FOLLOWEDBY.USERNAME)));
                mostPopularFollower.setProfile_picture(cursor.getString(cursor.getColumnIndex(PAID_FOLLOWEDBY.PROFILEPICTURE)));
                mostPopularFollower.setFull_name(cursor.getString(cursor.getColumnIndex(PAID_FOLLOWEDBY.FULLNAME)));
                UserCountBean userCountBean = new UserCountBean();
                userCountBean.setFollowed_by(cursor.getInt(cursor.getColumnIndex(PAID_FOLLOWEDBY.FOLLOWED_BY_COUNT)));
                userCountBean.setFollows(cursor.getInt(cursor.getColumnIndex(PAID_FOLLOWEDBY.FOLLOWS_COUNT)));
                mostPopularFollower.setUserCountBean(userCountBean);
                mostPopularFollower.setRelationShipStatus(null);
                arylstMostPopularFollowers.add(mostPopularFollower);
                cursor.moveToNext();
            }
        }
        cursor.close();
        readWriteLock.readLock().unlock();
        Log.d(TAG, "getMostPopularFollowers:arylstMostPopularFollowers::" + arylstMostPopularFollowers.size());
        return arylstMostPopularFollowers;
    }

}
