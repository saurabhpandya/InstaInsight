package com.instainsight.media;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.instainsight.db.DBManager;
import com.instainsight.db.DBQueries;
import com.instainsight.db.tables.RECENTMEDIA;
import com.instainsight.media.models.RecentMediaBean;
import com.instainsight.models.CommentsBean;
import com.instainsight.models.ImagesBean;
import com.instainsight.models.LikesBean;
import com.instainsight.models.LocationBean;
import com.instainsight.models.StandardResolution;
import com.instainsight.models.UserBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * Created by SONY on 12-02-2017.
 */

public class RecentMediaDBQueries {

    private String TAG = RecentMediaDBQueries.class.getSimpleName();
    private Context mContext;
    private DBManager manager;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public RecentMediaDBQueries(Context context) {
        mContext = context;
        getDBInstance();
    }

    private void getDBInstance() {
        manager = DBManager.getInstance(mContext);
        manager.open();
    }

    public Callable<ArrayList<RecentMediaBean>> saveRecentMedia(final ArrayList<RecentMediaBean> arylstRecentMedia) {
        return new Callable<ArrayList<RecentMediaBean>>() {
            @Override
            public ArrayList<RecentMediaBean> call() throws Exception {
                return saveRecentMediaToDb(arylstRecentMedia);
            }
        };
    }

    private ArrayList<RecentMediaBean> saveRecentMediaToDb(ArrayList<RecentMediaBean> arylstRecentMedia) {
        readWriteLock.readLock().lock();
        ArrayList<Map<String, Object>> columnDetails = new ArrayList<>();

        for (RecentMediaBean bean : arylstRecentMedia) {
            Map<String, Object> valuesMap = new HashMap<>();

            valuesMap.put(RECENTMEDIA.CREATEDTIME, bean.getCreated_time());
            valuesMap.put(RECENTMEDIA.LINK, bean.getLink());
            if (bean.getLocationBean() != null) {
                valuesMap.put(RECENTMEDIA.LOCATIONNAME, bean.getLocationBean().getName());
                valuesMap.put(RECENTMEDIA.LOCATIONLAT, bean.getLocationBean().getLatitude());
                valuesMap.put(RECENTMEDIA.LOCATIONLONG, bean.getLocationBean().getLongitude());
                valuesMap.put(RECENTMEDIA.LOCATIONID, bean.getLocationBean().getId());
            } else {
                valuesMap.put(RECENTMEDIA.LOCATIONNAME, "");
                valuesMap.put(RECENTMEDIA.LOCATIONLAT, "");
                valuesMap.put(RECENTMEDIA.LOCATIONLONG, "");
                valuesMap.put(RECENTMEDIA.LOCATIONID, "");
            }

            valuesMap.put(RECENTMEDIA.TAGS, bean.getTags());
            valuesMap.put(RECENTMEDIA.USERHASLIKED, bean.getUser_has_liked());
            valuesMap.put(RECENTMEDIA.LIKES_COUNT, bean.getLikesBean().getCount());
            valuesMap.put(RECENTMEDIA.MEDIAID, bean.getMediaId());
            valuesMap.put(RECENTMEDIA.ATTRIBUTION, bean.getAttribution());
            valuesMap.put(RECENTMEDIA.IMAGEURL, bean.getImagesBean().getStandardResolution().getUrl());
            valuesMap.put(RECENTMEDIA.MEDIA_TYPE, bean.getType());
            valuesMap.put(RECENTMEDIA.FILTER, bean.getFilter());
            valuesMap.put(RECENTMEDIA.COMMENTS_COUNT, bean.getCommentsBean().getCount());
            valuesMap.put(RECENTMEDIA.MEDIAOWNER_ID, bean.getUserBean().getId());
            valuesMap.put(RECENTMEDIA.MEDIAOWNER_USERNAME, bean.getUserBean().getUsername());
            valuesMap.put(RECENTMEDIA.MEDIAOWNER_PROFILEPIC, bean.getUserBean().getProfile_picture());
            valuesMap.put(RECENTMEDIA.MEDIAOWNER_FULLNAME, bean.getUserBean().getFull_name());
            columnDetails.add(valuesMap);
        }
        DBQueries dbQueries = new DBQueries(mContext);
        long insertedRows = dbQueries.insertUpdateValuesToRECENTMEDIA(RECENTMEDIA.TABLE_NAME, columnDetails);
        Log.d(TAG, "saveRecentMediaToDb:insertUpdateValues::" + insertedRows);
        readWriteLock.readLock().unlock();
        return getRecentMediaFromDb();
    }

    public Callable<ArrayList<RecentMediaBean>> getRecentMedia() {
        return new Callable<ArrayList<RecentMediaBean>>() {
            @Override
            public ArrayList<RecentMediaBean> call() throws Exception {
                return getRecentMediaFromDb();
            }
        };
    }

    private ArrayList<RecentMediaBean> getRecentMediaFromDb() {
        ArrayList<RecentMediaBean> arylstILikedMost = new ArrayList<RecentMediaBean>();
        readWriteLock.readLock().lock();

        Cursor cursor = manager.getDB().query(
//                true, // distinct
                RECENTMEDIA.TABLE_NAME, // table name
                null, // projection
                null, // selection
                null, // selection args
                null, // group by
                null, // having
                RECENTMEDIA.CREATEDTIME + " desc", // order by
                null // limit
        );
        /*query(RECENTMEDIA.TABLE_NAME, null, null, null, null, null, RECENTMEDIA.LIKEDBYUSER_CREATEDTIME + " desc");*/

        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                RecentMediaBean data = new RecentMediaBean();

                data.setCreated_time(cursor.getString(cursor.getColumnIndex(RECENTMEDIA.CREATEDTIME)));
                data.setLink(cursor.getString(cursor.getColumnIndex(RECENTMEDIA.LINK)));

                LocationBean locationBean = new LocationBean();
                locationBean.setId(cursor.getString(cursor.getColumnIndex(RECENTMEDIA.LOCATIONID)));
                locationBean.setName(cursor.getString(cursor.getColumnIndex(RECENTMEDIA.LOCATIONNAME)));
                locationBean.setLatitude(cursor.getString(cursor.getColumnIndex(RECENTMEDIA.LOCATIONLAT)));
                locationBean.setLongitude(cursor.getString(cursor.getColumnIndex(RECENTMEDIA.LOCATIONLONG)));
                data.setLocationBean(locationBean);

                data.setTags(new String[]{cursor.getString(cursor.getColumnIndex(RECENTMEDIA.TAGS))});
                data.setUser_has_liked(cursor.getString(cursor.getColumnIndex(RECENTMEDIA.USERHASLIKED)));

                LikesBean likesBean = new LikesBean();
                likesBean.setCount(cursor.getInt(cursor.getColumnIndex(RECENTMEDIA.LIKES_COUNT)));
                data.setLikesBean(likesBean);

                data.setMediaId(cursor.getString(cursor.getColumnIndex(RECENTMEDIA.MEDIAID)));
                data.setAttribution(cursor.getString(cursor.getColumnIndex(RECENTMEDIA.ATTRIBUTION)));

                ImagesBean imagesBean = new ImagesBean();
                StandardResolution standardResolution = new StandardResolution();
                standardResolution.setUrl(cursor.getString(cursor.getColumnIndex(RECENTMEDIA.IMAGEURL)));
                standardResolution.setHeight("640");
                standardResolution.setWidth("640");
                imagesBean.setStandardResolution(standardResolution);
                data.setImagesBean(imagesBean);

                data.setType(cursor.getString(cursor.getColumnIndex(RECENTMEDIA.MEDIA_TYPE)));
                data.setFilter(cursor.getString(cursor.getColumnIndex(RECENTMEDIA.FILTER)));

                CommentsBean commentsBean = new CommentsBean();
                commentsBean.setCount(cursor.getString(cursor.getColumnIndex(RECENTMEDIA.COMMENTS_COUNT)));
                data.setCommentsBean(commentsBean);

                UserBean userBean = new UserBean();
                userBean.setId(cursor.getString(cursor.getColumnIndex(RECENTMEDIA.MEDIAOWNER_ID)));
                userBean.setFull_name(cursor.getString(cursor.getColumnIndex(RECENTMEDIA.MEDIAOWNER_FULLNAME)));
                userBean.setUsername(cursor.getString(cursor.getColumnIndex(RECENTMEDIA.MEDIAOWNER_USERNAME)));
                userBean.setProfile_picture(cursor.getString(cursor.getColumnIndex(RECENTMEDIA.MEDIAOWNER_PROFILEPIC)));
                data.setUserBean(userBean);
                arylstILikedMost.add(data);
                cursor.moveToNext();
            }
        }
        cursor.close();
        readWriteLock.readLock().unlock();
        return arylstILikedMost;
    }

}
