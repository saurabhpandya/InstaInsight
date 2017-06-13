package com.instainsight.db;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.instainsight.db.tables.PAID_COMMENTS;
import com.instainsight.ghostfollowers.model.CommentsBean;
import com.instainsight.models.UserBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by SONY on 23-02-2017.
 */

public class PaidCommentedUsersDBQueries {

    private String TAG = PaidCommentedUsersDBQueries.class.getSimpleName();

    private Context mContext;
    private DBManager manager;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public PaidCommentedUsersDBQueries(Context context) {
        mContext = context;
        getDBInstance();
    }

    private void getDBInstance() {
        manager = DBManager.getInstance(mContext);
        manager.open();
    }

    public ArrayList<CommentsBean> saveMediaCommentsToDb(ArrayList<CommentsBean> arylstMediaComments) {
        readWriteLock.readLock().lock();
        ArrayList<Map<String, Object>> columnDetails = new ArrayList<>();

        for (CommentsBean bean : arylstMediaComments) {
            Map<String, Object> valuesMap = new HashMap<>();
            valuesMap.put(PAID_COMMENTS.COMMENTED_MEDIA_ID, bean.getMediaId());
            valuesMap.put(PAID_COMMENTS.COMMENT_ID, bean.getId());
            valuesMap.put(PAID_COMMENTS.COMMENTED_TEXT, bean.getText());
            valuesMap.put(PAID_COMMENTS.CREATEDTIME, bean.getCreate_time());
            valuesMap.put(PAID_COMMENTS.COMMENTED_USERID, bean.getFrom().getId());
            valuesMap.put(PAID_COMMENTS.COMMENTED_USERFULLNAME, bean.getFrom().getFull_name());
            valuesMap.put(PAID_COMMENTS.COMMENTED_USERNAME, bean.getFrom().getUsername());
            valuesMap.put(PAID_COMMENTS.COMMENTED_USERPROFILEPIC, bean.getFrom().getProfile_picture());
            columnDetails.add(valuesMap);
            if (getRecentMediaComment(bean.getId())) {
                DBQueries dbQueries = new DBQueries(mContext);
                long deletedCommentRows = dbQueries.deleteTable(PAID_COMMENTS.TABLE_NAME, PAID_COMMENTS.COMMENT_ID, new String[]{bean.getId()});
                Log.d(TAG, "saveMediaCommentsToDb:deletedCommentRows::" + deletedCommentRows);
            }

        }
        DBQueries dbQueries = new DBQueries(mContext);
//        long deletedCommentRows = dbQueries.deleteTable(PAID_COMMENTS.TABLE_NAME);
//        Log.d(TAG, "saveMediaCommentsToDb:deletedCommentRows::" + deletedCommentRows);
        long insertedRows = dbQueries.insertValues(PAID_COMMENTS.TABLE_NAME, columnDetails);
        Log.d(TAG, "saveMediaCommentsToDb:insertedRows::" + insertedRows);
        readWriteLock.readLock().unlock();
        return getRecentMediaComments();
    }

    private boolean getRecentMediaComment(String commentId) {
        boolean isCommentAvailable = false;

        Cursor cursor = manager.getDB().query(
                PAID_COMMENTS.TABLE_NAME, // table name
                new String[]{PAID_COMMENTS.COMMENT_ID}, // projection
                PAID_COMMENTS.COMMENT_ID + " = ?", // selection
                new String[]{commentId}, // selection args
                null, // group by
                null, // having
                null // order by,
        );

        if (null != cursor && cursor.getCount() > 0) {
            CommentsBean data = new CommentsBean();
            isCommentAvailable = true;
            cursor.moveToFirst();
//            data.setMediaId(cursor.getString(cursor.getColumnIndex(PAID_COMMENTS.COMMENTED_MEDIA_ID)));
            data.setId(cursor.getString(cursor.getColumnIndex(PAID_COMMENTS.COMMENT_ID)));
//            data.setCreate_time(cursor.getString(cursor.getColumnIndex(PAID_COMMENTS.CREATEDTIME)));
//            data.setText(cursor.getString(cursor.getColumnIndex(PAID_COMMENTS.COMMENTED_TEXT)));

//            UserBean userBean = new UserBean();
//            userBean.setId(cursor.getString(cursor.getColumnIndex(PAID_COMMENTS.COMMENTED_USERID)));
//            userBean.setFull_name(cursor.getString(cursor.getColumnIndex(PAID_COMMENTS.COMMENTED_USERFULLNAME)));
//            userBean.setUsername(cursor.getString(cursor.getColumnIndex(PAID_COMMENTS.COMMENTED_USERNAME)));
//            userBean.setProfile_picture(cursor.getString(cursor.getColumnIndex(PAID_COMMENTS.COMMENTED_USERPROFILEPIC)));
//            data.setFrom(userBean);

            cursor.moveToNext();
            cursor.close();
        }

        return isCommentAvailable;
    }

    public ArrayList<CommentsBean> getRecentMediaComments() {
        ArrayList<CommentsBean> arylstRecentMediaComments = new ArrayList<CommentsBean>();
        readWriteLock.readLock().lock();

        Cursor cursor = manager.getDB().query(
//                false, // distinct
                PAID_COMMENTS.TABLE_NAME, // table name
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
                CommentsBean data = new CommentsBean();
                data.setMediaId(cursor.getString(cursor.getColumnIndex(PAID_COMMENTS.COMMENTED_MEDIA_ID)));
                data.setId(cursor.getString(cursor.getColumnIndex(PAID_COMMENTS.COMMENT_ID)));
                data.setCreate_time(cursor.getString(cursor.getColumnIndex(PAID_COMMENTS.CREATEDTIME)));
                data.setText(cursor.getString(cursor.getColumnIndex(PAID_COMMENTS.COMMENTED_TEXT)));

                UserBean userBean = new UserBean();
                userBean.setId(cursor.getString(cursor.getColumnIndex(PAID_COMMENTS.COMMENTED_USERID)));
                userBean.setFull_name(cursor.getString(cursor.getColumnIndex(PAID_COMMENTS.COMMENTED_USERFULLNAME)));
                userBean.setUsername(cursor.getString(cursor.getColumnIndex(PAID_COMMENTS.COMMENTED_USERNAME)));
                userBean.setProfile_picture(cursor.getString(cursor.getColumnIndex(PAID_COMMENTS.COMMENTED_USERPROFILEPIC)));
                data.setFrom(userBean);

                arylstRecentMediaComments.add(data);
                cursor.moveToNext();
            }
        }
        cursor.close();
        readWriteLock.readLock().unlock();
        return arylstRecentMediaComments;
    }

}
