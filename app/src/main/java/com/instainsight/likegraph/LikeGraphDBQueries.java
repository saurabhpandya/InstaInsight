package com.instainsight.likegraph;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.instainsight.db.DBManager;
import com.instainsight.db.DBQueries;
import com.instainsight.db.tables.MEDIA;
import com.instainsight.media.models.MediaBean;
import com.instainsight.models.CaptionBean;
import com.instainsight.models.CommentsBean;
import com.instainsight.models.ImagesBean;
import com.instainsight.models.LikesBean;
import com.instainsight.models.LocationBean;
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

public class LikeGraphDBQueries {

    private String TAG = LikeGraphDBQueries.class.getSimpleName();

    private Context mContext;
    private DBManager manager;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public LikeGraphDBQueries(Context context) {
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

            valuesMap.put(MEDIA.ATTRIBUTION, bean.getAttribution());
            valuesMap.put(MEDIA.LIKES_COUNT, "" + bean.getLikesBean().getCount());
            valuesMap.put(MEDIA.USERHASLIKED, bean.getUser_has_liked());

            if (bean.getLocationBean() != null) {
                valuesMap.put(MEDIA.LOCATIONNAME, bean.getLocationBean().getName());
                valuesMap.put(MEDIA.LOCATIONLAT, bean.getLocationBean().getLatitude());
                valuesMap.put(MEDIA.LOCATIONLONG, bean.getLocationBean().getLongitude());
                valuesMap.put(MEDIA.LOCATIONID, bean.getLocationBean().getId());
            } else {
                valuesMap.put(MEDIA.LOCATIONNAME, "");
                valuesMap.put(MEDIA.LOCATIONLAT, "");
                valuesMap.put(MEDIA.LOCATIONLONG, "");
                valuesMap.put(MEDIA.LOCATIONID, "");
            }
            valuesMap.put(MEDIA.FILTER, bean.getFilter());
            valuesMap.put(MEDIA.CREATEDTIME, bean.getCreated_time());

            if (bean.getUserBean() != null) {
                valuesMap.put(MEDIA.MEDIAOWNER_ID, bean.getUserBean().getId());
                valuesMap.put(MEDIA.MEDIAOWNER_USERNAME, bean.getUserBean().getUsername());
                valuesMap.put(MEDIA.MEDIAOWNER_PROFILEPIC, bean.getUserBean().getProfile_picture());
                valuesMap.put(MEDIA.MEDIAOWNER_FULLNAME, bean.getUserBean().getFull_name());
            } else {
                valuesMap.put(MEDIA.MEDIAOWNER_ID, "");
                valuesMap.put(MEDIA.MEDIAOWNER_USERNAME, "");
                valuesMap.put(MEDIA.MEDIAOWNER_PROFILEPIC, "");
                valuesMap.put(MEDIA.MEDIAOWNER_FULLNAME, "");
            }


            valuesMap.put(MEDIA.TAGS, bean.getTags());

            valuesMap.put(MEDIA.MEDIAID, bean.getMediaId());

            if (bean.getCommentsBean() != null) {
                valuesMap.put(MEDIA.COMMENTS_COUNT, bean.getCommentsBean().getCount());
            } else {
                valuesMap.put(MEDIA.COMMENTS_COUNT, "");
            }

            if (bean.getUsers_in_photo() != null) {
                valuesMap.put(MEDIA.USERS_IN_PHOTO, bean.getUsers_in_photo());
            } else {
                valuesMap.put(MEDIA.USERS_IN_PHOTO, "");
            }

            if (bean.getCaptionBean() != null) {
                valuesMap.put(MEDIA.CAPTION_ID, bean.getCaptionBean().getId());
                valuesMap.put(MEDIA.CAPTION_TEXT, bean.getCaptionBean().getText());
            } else {
                valuesMap.put(MEDIA.CAPTION_ID, "");
                valuesMap.put(MEDIA.CAPTION_TEXT, "");
            }


            valuesMap.put(MEDIA.LINK, bean.getLink());

            valuesMap.put(MEDIA.MEDIA_TYPE, bean.getType());

            if (bean.getType().equalsIgnoreCase("image"))
                if (bean.getImagesBean() != null)
                    valuesMap.put(MEDIA.IMAGEURL, bean.getImagesBean().getStandardResolution().getUrl());
                else
                    valuesMap.put(MEDIA.IMAGEURL, "");
            else if (bean.getType().equalsIgnoreCase("video")) {
                if (bean.getImagesBean() != null)
                    valuesMap.put(MEDIA.IMAGEURL, bean.getImagesBean().getStandardResolution().getUrl());
                else
                    valuesMap.put(MEDIA.IMAGEURL, "");
                if (bean.getVideosBean() != null)
                    valuesMap.put(MEDIA.VIDEOURL, bean.getVideosBean().getStandardResolution().getUrl());
                else
                    valuesMap.put(MEDIA.VIDEOURL, "");
            }

            columnDetails.add(valuesMap);
        }
        DBQueries dbQueries = new DBQueries(mContext);
        long insertedRows = dbQueries.insertUpdateValuesToRECENTMEDIA(MEDIA.TABLE_NAME, columnDetails);
        Log.d(TAG, "saveRecentMediaToDb:insertUpdateValues::" + insertedRows);
        readWriteLock.readLock().unlock();
        return getLikeGraphFromDb();
    }

    public ArrayList<MediaBean> getLikeGraphFromDb() {
        ArrayList<MediaBean> arylstILikedMost = new ArrayList<MediaBean>();
        readWriteLock.readLock().lock();

        Cursor cursor = manager.getDB().query(
//                false, // distinct
                MEDIA.TABLE_NAME, // table name
                null, // projection
                null, // selection
                null, // selection args
                null, // group by
                null, // having
                MEDIA.CREATEDTIME + " DESC", // order by,
                "7"
        );

//        Cursor cursor = manager.getDB().rawQuery("SELECT * FROM (SELECT * FROM " + MEDIA.TABLE_NAME
//                + " ORDER BY " + MEDIA.CREATEDTIME + " DESC LIMIT 7) ORDER BY " + MEDIA.LIKES_COUNT + " ASC;", null);

        /*query(RECENTMEDIA.TABLE_NAME, null, null, null, null, null, RECENTMEDIA.LIKEDBYUSER_CREATEDTIME + " desc");*/

        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                MediaBean data = new MediaBean();

                data.setAttribution(cursor.getString(cursor.getColumnIndex(MEDIA.ATTRIBUTION)));

                LikesBean likesBean = new LikesBean();
                likesBean.setCount(cursor.getInt(cursor.getColumnIndex(MEDIA.LIKES_COUNT)));
                data.setLikesBean(likesBean);
                Log.d(TAG, "getLikeGraphFromDb:LikeCount:" + likesBean.getCount());
                data.setUser_has_liked(cursor.getString(cursor.getColumnIndex(MEDIA.USERHASLIKED)));

                LocationBean locationBean = new LocationBean();
                locationBean.setId(cursor.getString(cursor.getColumnIndex(MEDIA.LOCATIONID)));
                locationBean.setName(cursor.getString(cursor.getColumnIndex(MEDIA.LOCATIONNAME)));
                locationBean.setLatitude(cursor.getString(cursor.getColumnIndex(MEDIA.LOCATIONLAT)));
                locationBean.setLongitude(cursor.getString(cursor.getColumnIndex(MEDIA.LOCATIONLONG)));
                data.setLocationBean(locationBean);

                data.setFilter(cursor.getString(cursor.getColumnIndex(MEDIA.FILTER)));
                data.setCreated_time(cursor.getString(cursor.getColumnIndex(MEDIA.CREATEDTIME)));

                UserBean userBean = new UserBean();
                userBean.setId(cursor.getString(cursor.getColumnIndex(MEDIA.MEDIAOWNER_ID)));
                userBean.setFull_name(cursor.getString(cursor.getColumnIndex(MEDIA.MEDIAOWNER_FULLNAME)));
                userBean.setUsername(cursor.getString(cursor.getColumnIndex(MEDIA.MEDIAOWNER_USERNAME)));
                userBean.setProfile_picture(cursor.getString(cursor.getColumnIndex(MEDIA.MEDIAOWNER_PROFILEPIC)));
                data.setUserBean(userBean);

                data.setTags(new String[]{cursor.getString(cursor.getColumnIndex(MEDIA.TAGS))});
                data.setMediaId(cursor.getString(cursor.getColumnIndex(MEDIA.MEDIAID)));

                CommentsBean commentsBean = new CommentsBean();
                commentsBean.setCount(cursor.getString(cursor.getColumnIndex(MEDIA.COMMENTS_COUNT)));
                data.setCommentsBean(commentsBean);

//                data.setUsers_in_photo();

                ImagesBean imagesBean = new ImagesBean();
                StandardResolution standardResolution = new StandardResolution();
                standardResolution.setUrl(cursor.getString(cursor.getColumnIndex(MEDIA.IMAGEURL)));
                standardResolution.setHeight("640");
                standardResolution.setWidth("640");
                imagesBean.setStandardResolution(standardResolution);
                data.setImagesBean(imagesBean);

                CaptionBean captionBean = new CaptionBean();
                captionBean.setId(cursor.getString(cursor.getColumnIndex(MEDIA.CAPTION_ID)));
                captionBean.setText(cursor.getString(cursor.getColumnIndex(MEDIA.CAPTION_TEXT)));
                captionBean.setCreated_time("");
                captionBean.setUserBean(new UserBean());
                data.setCaptionBean(captionBean);
                data.setLink(cursor.getString(cursor.getColumnIndex(MEDIA.LINK)));
                data.setType(cursor.getString(cursor.getColumnIndex(MEDIA.MEDIA_TYPE)));

                if (cursor.getString(cursor.getColumnIndex(MEDIA.MEDIA_TYPE)).equalsIgnoreCase("video")) {
                    VideosBean videosBean = new VideosBean();
                    StandardResolution standardResolution1 = new StandardResolution();
                    standardResolution1.setHeight("640");
                    standardResolution1.setWidth("640");
                    standardResolution1.setUrl(cursor.getString(cursor.getColumnIndex(MEDIA.VIDEOURL)));
                    videosBean.setStandardResolution(standardResolution1);
                    data.setVideosBean(videosBean);
                } else {
                    data.setVideosBean(new VideosBean());
                }

                arylstILikedMost.add(data);
                cursor.moveToNext();
            }
        }
        cursor.close();
        readWriteLock.readLock().unlock();
        return arylstILikedMost;
    }

}
