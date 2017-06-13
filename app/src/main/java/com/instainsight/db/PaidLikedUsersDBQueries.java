package com.instainsight.db;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.instainsight.db.tables.PAID_LIKEDUSERS;
import com.instainsight.ghostfollowers.model.LikesBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by SONY on 23-02-2017.
 */

public class PaidLikedUsersDBQueries {

    private String TAG = PaidLikedUsersDBQueries.class.getSimpleName();

    private Context mContext;
    private DBManager manager;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public PaidLikedUsersDBQueries(Context context) {
        mContext = context;
        getDBInstance();
    }

    private void getDBInstance() {
        manager = DBManager.getInstance(mContext);
        manager.open();
    }

    public ArrayList<LikesBean> saveMediaLikesToDb(ArrayList<LikesBean> arylstMediaLikes) {
        readWriteLock.readLock().lock();
        ArrayList<Map<String, Object>> columnDetails = new ArrayList<>();

        for (LikesBean bean : arylstMediaLikes) {
            Map<String, Object> valuesMap = new HashMap<>();

            valuesMap.put(PAID_LIKEDUSERS.LIKED_USERID, bean.getId());
            valuesMap.put(PAID_LIKEDUSERS.LIKED_USERFIRSTNAME, bean.getFirst_name());
            valuesMap.put(PAID_LIKEDUSERS.LIKED_USERLASTNAME, bean.getLast_name());
            valuesMap.put(PAID_LIKEDUSERS.LIKED_USERNAME, bean.getUsername());
            valuesMap.put(PAID_LIKEDUSERS.TYPE, bean.getType());
            valuesMap.put(PAID_LIKEDUSERS.LIKED_MEDIAID, bean.getMediaId());
            columnDetails.add(valuesMap);
            if (getRecentMediaLike(bean.getId())) {
                DBQueries dbQueries = new DBQueries(mContext);
                long deletedCommentRows = dbQueries.deleteTable(PAID_LIKEDUSERS.TABLE_NAME, PAID_LIKEDUSERS.LIKED_USERID, new String[]{bean.getId()});
                Log.d(TAG, "saveMediaLikesToDb:deletedCommentRows::" + deletedCommentRows);
            }

        }
        DBQueries dbQueries = new DBQueries(mContext);
//        long deletedRows = dbQueries.deleteTable(PAID_LIKEDUSERS.TABLE_NAME);
//        Log.d(TAG, "saveMediaLikesToDb:deletedRows::" + deletedRows);
        long insertedRows = dbQueries.insertValues(PAID_LIKEDUSERS.TABLE_NAME, columnDetails);
        Log.d(TAG, "saveMediaLikesToDb:insertUpdateValues::" + insertedRows);
        readWriteLock.readLock().unlock();
        return getRecentMediaLikes();
    }

    private boolean getRecentMediaLike(String commentId) {
        boolean isLikeAvailable = false;

        Cursor cursor = manager.getDB().query(
                PAID_LIKEDUSERS.TABLE_NAME, // table name
                new String[]{PAID_LIKEDUSERS.LIKED_USERID}, // projection
                PAID_LIKEDUSERS.LIKED_USERID + " = ?", // selection
                new String[]{commentId}, // selection args
                null, // group by
                null, // having
                null // order by,
        );

        if (null != cursor && cursor.getCount() > 0) {
            LikesBean data = new LikesBean();
            isLikeAvailable = true;
            cursor.moveToFirst();

            data.setId(cursor.getString(cursor.getColumnIndex(PAID_LIKEDUSERS.LIKED_USERID)));
//            data.setFirst_name(cursor.getString(cursor.getColumnIndex(PAID_LIKEDUSERS.LIKED_USERFIRSTNAME)));
//            data.setLast_name(cursor.getString(cursor.getColumnIndex(PAID_LIKEDUSERS.LIKED_USERLASTNAME)));
//            data.setUsername(cursor.getString(cursor.getColumnIndex(PAID_LIKEDUSERS.LIKED_USERNAME)));
//            data.setType(cursor.getString(cursor.getColumnIndex(PAID_LIKEDUSERS.TYPE)));
//            data.setMediaId(cursor.getString(cursor.getColumnIndex(PAID_LIKEDUSERS.LIKED_MEDIAID)));

            cursor.moveToNext();
            cursor.close();
        }

        return isLikeAvailable;
    }

    public ArrayList<LikesBean> getRecentMediaLikes() {
        ArrayList<LikesBean> arylstRecentMediaLikes = new ArrayList<LikesBean>();
        readWriteLock.readLock().lock();

        Cursor cursor = manager.getDB().query(
//                false, // distinct
                PAID_LIKEDUSERS.TABLE_NAME, // table name
                null, // projection
                null, // selection
                null, // selection args
                null, // group by
                null, // having
                null // order by,
        );

        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                LikesBean data = new LikesBean();
                data.setMediaId(cursor.getString(cursor.getColumnIndex(PAID_LIKEDUSERS.LIKED_MEDIAID)));
                data.setId(cursor.getString(cursor.getColumnIndex(PAID_LIKEDUSERS.LIKED_USERID)));
                data.setFirst_name(cursor.getString(cursor.getColumnIndex(PAID_LIKEDUSERS.LIKED_USERFIRSTNAME)));
                data.setLast_name(cursor.getString(cursor.getColumnIndex(PAID_LIKEDUSERS.LIKED_USERLASTNAME)));
                data.setUsername(cursor.getString(cursor.getColumnIndex(PAID_LIKEDUSERS.LIKED_USERNAME)));
                data.setType(cursor.getString(cursor.getColumnIndex(PAID_LIKEDUSERS.TYPE)));
                arylstRecentMediaLikes.add(data);
                cursor.moveToNext();
            }
        }
        cursor.close();
        readWriteLock.readLock().unlock();
        return arylstRecentMediaLikes;
    }

}
