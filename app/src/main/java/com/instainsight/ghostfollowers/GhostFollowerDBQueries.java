package com.instainsight.ghostfollowers;

import android.content.Context;
import android.database.Cursor;

import com.instainsight.db.DBManager;
import com.instainsight.db.tables.PAID_COMMENTS;
import com.instainsight.db.tables.PAID_FOLLOWEDBY;
import com.instainsight.db.tables.PAID_LIKEDUSERS;
import com.instainsight.followersing.followers.bean.FollowerBean;

import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by SONY on 23-02-2017.
 */

public class GhostFollowerDBQueries {

    private String TAG = GhostFollowerDBQueries.class.getSimpleName();

    private Context mContext;
    private DBManager manager;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public GhostFollowerDBQueries(Context context) {
        mContext = context;
        getDBInstance();
    }

    private void getDBInstance() {
        manager = DBManager.getInstance(mContext);
        manager.open();
    }

    public ArrayList<FollowerBean> getGhostFollowers() {
        ArrayList<FollowerBean> arylstGhostFollowers = new ArrayList<FollowerBean>();
        readWriteLock.readLock().lock();

        StringBuilder qryGhostFollowers = new StringBuilder();

        qryGhostFollowers
                .append("SELECT * FROM ").append(PAID_FOLLOWEDBY.TABLE_NAME).append(" WHERE ").append(PAID_FOLLOWEDBY.ID).append(" NOT IN(")
                .append("SELECT ").append(PAID_COMMENTS.COMMENTED_USERID).append(" FROM ").append(PAID_COMMENTS.TABLE_NAME)
                .append(" UNION ")
                .append("SELECT ").append(PAID_LIKEDUSERS.LIKED_USERID).append(" FROM ").append(PAID_LIKEDUSERS.TABLE_NAME).append(")");
        Cursor cursor = manager.getDB().rawQuery(qryGhostFollowers.toString(), null);
//        Cursor cursor = manager.getDB().query(
////                false, // distinct
//                PAID_COMMENTS.TABLE_NAME, // table name
//                null, // projection
//                null, // selection
//                null, // selection args
//                null, // group by
//                null, // having
//                null // order by,
//        );

        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                FollowerBean data = new FollowerBean();
                data.setId(cursor.getString(cursor.getColumnIndex(PAID_FOLLOWEDBY.ID)));
                data.setUserName(cursor.getString(cursor.getColumnIndex(PAID_FOLLOWEDBY.USERNAME)));
                data.setProfilePic(cursor.getString(cursor.getColumnIndex(PAID_FOLLOWEDBY.PROFILEPICTURE)));
                data.setFullName(cursor.getString(cursor.getColumnIndex(PAID_FOLLOWEDBY.FULLNAME)));
                data.setRelationShipStatus(null);
                arylstGhostFollowers.add(data);
                cursor.moveToNext();
            }
        }
        cursor.close();
        readWriteLock.readLock().unlock();
        return arylstGhostFollowers;
    }

}
