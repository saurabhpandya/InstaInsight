package com.instainsight.db;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.instainsight.db.tables.PAID_MEDIA;
import com.instainsight.media.models.MediaBean;
import com.instainsight.models.CommentsBean;
import com.instainsight.models.ImagesBean;
import com.instainsight.models.LikesBean;
import com.instainsight.models.StandardResolution;
import com.instainsight.models.UserBean;
import com.instainsight.models.VideosBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by SONY on 23-02-2017.
 */

public class PaidMediaDBQueries {

    private String TAG = PaidMediaDBQueries.class.getSimpleName();

    private Context mContext;
    private DBManager manager;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public PaidMediaDBQueries(Context context) {
        mContext = context;
        getDBInstance();
    }

    private void getDBInstance() {
        manager = DBManager.getInstance(mContext);
        manager.open();
    }

    public ArrayList<MediaBean> saveGraphDataToDb(ArrayList<MediaBean> arylstRecentMedia) {
        readWriteLock.readLock().lock();
        ArrayList<Map<String, Object>> columnDetails = new ArrayList<>();

        int count = 0;

        for (MediaBean bean : arylstRecentMedia) {
            if (count > 6)
                break;
            count++;
            Map<String, Object> valuesMap = new HashMap<>();

            valuesMap.put(PAID_MEDIA.MEDIAID, bean.getMediaId());

            if (bean.getUserBean() != null) {
                valuesMap.put(PAID_MEDIA.MEDIAOWNER_ID, bean.getUserBean().getId());
                valuesMap.put(PAID_MEDIA.MEDIAOWNER_USERNAME, bean.getUserBean().getUsername());
                valuesMap.put(PAID_MEDIA.MEDIAOWNER_PROFILEPIC, bean.getUserBean().getProfile_picture());
                valuesMap.put(PAID_MEDIA.MEDIAOWNER_FULLNAME, bean.getUserBean().getFull_name());
            } else {
                valuesMap.put(PAID_MEDIA.MEDIAOWNER_ID, "");
                valuesMap.put(PAID_MEDIA.MEDIAOWNER_USERNAME, "");
                valuesMap.put(PAID_MEDIA.MEDIAOWNER_PROFILEPIC, "");
                valuesMap.put(PAID_MEDIA.MEDIAOWNER_FULLNAME, "");
            }

            valuesMap.put(PAID_MEDIA.MEDIA_TYPE, bean.getType());

            if (bean.getType().equalsIgnoreCase("image"))
                if (bean.getImagesBean() != null)
                    valuesMap.put(PAID_MEDIA.IMAGEURL, bean.getImagesBean().getStandardResolution().getUrl());
                else
                    valuesMap.put(PAID_MEDIA.IMAGEURL, "");
            else if (bean.getType().equalsIgnoreCase("video")) {
                if (bean.getImagesBean() != null)
                    valuesMap.put(PAID_MEDIA.IMAGEURL, bean.getImagesBean().getStandardResolution().getUrl());
                else
                    valuesMap.put(PAID_MEDIA.IMAGEURL, "");
                if (bean.getVideosBean() != null)
                    valuesMap.put(PAID_MEDIA.VIDEOURL, bean.getVideosBean().getStandardResolution().getUrl());
                else
                    valuesMap.put(PAID_MEDIA.VIDEOURL, "");
            }

            valuesMap.put(PAID_MEDIA.CREATEDTIME, bean.getCreated_time());

            if (bean.getLikesBean() != null) {
                valuesMap.put(PAID_MEDIA.LIKES_COUNT, "" + bean.getLikesBean().getCount());
            } else {
                valuesMap.put(PAID_MEDIA.LIKES_COUNT, "");
            }
            if (bean.getCommentsBean() != null) {
                valuesMap.put(PAID_MEDIA.COMMENTS_COUNT, bean.getCommentsBean().getCount());
            } else {
                valuesMap.put(PAID_MEDIA.COMMENTS_COUNT, "");
            }

            valuesMap.put(PAID_MEDIA.LINK, bean.getLink());

            if (bean.getUsers_in_photo() != null) {
                valuesMap.put(PAID_MEDIA.USERS_IN_PHOTO, bean.getUsers_in_photo());
            } else {
                valuesMap.put(PAID_MEDIA.USERS_IN_PHOTO, "");
            }

            columnDetails.add(valuesMap);
        }
        DBQueries dbQueries = new DBQueries(mContext);
        long insertedRows = dbQueries.insertUpdateValuesToRECENTMEDIA(PAID_MEDIA.TABLE_NAME, columnDetails);
        Log.d(TAG, "saveRecentMediaToDb:insertUpdateValues::" + insertedRows);
        readWriteLock.readLock().unlock();
        return getLikeGraphFromDb();
    }

    public ArrayList<MediaBean> getLikeGraphFromDb() {
        ArrayList<MediaBean> arylstILikedMost = new ArrayList<MediaBean>();
        readWriteLock.readLock().lock();

        Cursor cursor = manager.getDB().query(
//                false, // distinct
                PAID_MEDIA.TABLE_NAME, // table name
                null, // projection
                null, // selection
                null, // selection args
                null, // group by
                null, // having
                PAID_MEDIA.CREATEDTIME + " DESC", // order by,
                "7"
        );

//        Cursor cursor = manager.getDB().rawQuery("SELECT * FROM (SELECT * FROM " + PAID_MEDIA.TABLE_NAME
//                + " ORDER BY " + PAID_MEDIA.CREATEDTIME + " DESC LIMIT 7) ORDER BY " + PAID_MEDIA.LIKES_COUNT + " ASC;", null);

        /*query(RECENTPAID_MEDIA.TABLE_NAME, null, null, null, null, null, RECENTPAID_MEDIA.LIKEDBYUSER_CREATEDTIME + " desc");*/

        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                MediaBean data = new MediaBean();

                data.setMediaId(cursor.getString(cursor.getColumnIndex(PAID_MEDIA.MEDIAID)));

                UserBean userBean = new UserBean();
                userBean.setId(cursor.getString(cursor.getColumnIndex(PAID_MEDIA.MEDIAOWNER_ID)));
                userBean.setFull_name(cursor.getString(cursor.getColumnIndex(PAID_MEDIA.MEDIAOWNER_FULLNAME)));
                userBean.setUsername(cursor.getString(cursor.getColumnIndex(PAID_MEDIA.MEDIAOWNER_USERNAME)));
                userBean.setProfile_picture(cursor.getString(cursor.getColumnIndex(PAID_MEDIA.MEDIAOWNER_PROFILEPIC)));
                data.setUserBean(userBean);

                data.setType(cursor.getString(cursor.getColumnIndex(PAID_MEDIA.MEDIA_TYPE)));

                if (cursor.getString(cursor.getColumnIndex(PAID_MEDIA.MEDIA_TYPE)).equalsIgnoreCase("video")) {
                    VideosBean videosBean = new VideosBean();
                    StandardResolution standardResolution1 = new StandardResolution();
                    standardResolution1.setHeight("640");
                    standardResolution1.setWidth("640");
                    standardResolution1.setUrl(cursor.getString(cursor.getColumnIndex(PAID_MEDIA.VIDEOURL)));
                    videosBean.setStandardResolution(standardResolution1);
                    data.setVideosBean(videosBean);
                } else if (cursor.getString(cursor.getColumnIndex(PAID_MEDIA.MEDIA_TYPE)).equalsIgnoreCase("video")) {
                    ImagesBean imagesBean = new ImagesBean();
                    StandardResolution standardResolution = new StandardResolution();
                    standardResolution.setUrl(cursor.getString(cursor.getColumnIndex(PAID_MEDIA.IMAGEURL)));
                    standardResolution.setHeight("640");
                    standardResolution.setWidth("640");
                    imagesBean.setStandardResolution(standardResolution);
                    data.setImagesBean(imagesBean);
                }

                data.setCreated_time(cursor.getString(cursor.getColumnIndex(PAID_MEDIA.CREATEDTIME)));

                LikesBean likesBean = new LikesBean();
                likesBean.setCount(cursor.getInt(cursor.getColumnIndex(PAID_MEDIA.LIKES_COUNT)));
                data.setLikesBean(likesBean);
                Log.d(TAG, "getLikeGraphFromDb:LikeCount:" + likesBean.getCount());

                CommentsBean commentsBean = new CommentsBean();
                commentsBean.setCount(cursor.getString(cursor.getColumnIndex(PAID_MEDIA.COMMENTS_COUNT)));
                data.setCommentsBean(commentsBean);

                data.setLink(cursor.getString(cursor.getColumnIndex(PAID_MEDIA.LINK)));

                arylstILikedMost.add(data);
                cursor.moveToNext();
            }
        }
        cursor.close();
        readWriteLock.readLock().unlock();
        return arylstILikedMost;
    }

}
