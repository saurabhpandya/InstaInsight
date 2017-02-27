package com.instainsight.ilikedmost;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.instainsight.db.DBManager;
import com.instainsight.db.DBQueries;
import com.instainsight.db.tables.LIKEDBYUSER;
import com.instainsight.ilikedmost.models.ILikedMostBean;
import com.instainsight.models.CommentsBean;
import com.instainsight.models.ImagesBean;
import com.instainsight.models.LikesBean;
import com.instainsight.models.StandardResolution;
import com.instainsight.models.UserBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import io.reactivex.Observable;

/**
 * Created by SONY on 11-02-2017.
 */

public class ILikeMostDBQueries {
    private String TAG = ILikeMostDBQueries.class.getSimpleName();
    private Context context;
    private DBManager manager;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public ILikeMostDBQueries(Context context) {
        this.context = context;
        getDBInstance();
    }

    private void getDBInstance() {
        manager = DBManager.getInstance(context);
        manager.open();
    }

//    public Callable<ArrayList<ILikedMostBean>> saveILikedMost(final ArrayList<ILikedMostBean> arylstILikedMost) {
//        return new Callable<ArrayList<ILikedMostBean>>() {
//            @Override
//            public ArrayList<ILikedMostBean> call() throws Exception {
//                return saveILikedMostToDB(arylstILikedMost);
//            }
//        };
//    }

    public Observable<ArrayList<ILikedMostBean>> saveILikedMost(final ArrayList<ILikedMostBean> arylstILikedMost) {
        return Observable.just(saveILikedMostToDB(arylstILikedMost));
    }

    private ArrayList<ILikedMostBean> saveILikedMostToDB(ArrayList<ILikedMostBean> arylstILikedMost) {
        readWriteLock.readLock().lock();
        ArrayList<Map<String, Object>> columnDetails = new ArrayList<>();
        String accessTokenOwnedUserId = null;
        for (ILikedMostBean bean : arylstILikedMost) {
            Map<String, Object> valuesMap = new HashMap<>();

            valuesMap.put(LIKEDBYUSER.LIKEDBYUSER_MEDIAID, bean.getId());
            valuesMap.put(LIKEDBYUSER.LIKEDBYUSER_TYPE, bean.getType());
            valuesMap.put(LIKEDBYUSER.LIKEDBYUSER_IMAGEURL, bean.getImagesBean().getStandardResolution().getUrl());
            valuesMap.put(LIKEDBYUSER.LIKEDBYUSER_LIKESCOUNT, bean.getLikesBean().getCount());
            valuesMap.put(LIKEDBYUSER.LIKEDBYUSER_USERID, bean.getLikedUserId());
            valuesMap.put(LIKEDBYUSER.LIKEDBYUSER_CREATEDTIME, bean.getCreated_time());
            valuesMap.put(LIKEDBYUSER.LIKEDBYUSER_COMMENTS, bean.getCommentsBean().getCount());
            valuesMap.put(LIKEDBYUSER.LIKEDBYUSER_LINK, bean.getLink());
            valuesMap.put(LIKEDBYUSER.LIKEDBYUSER_LIKEDUSERID, bean.getUsersBean().getId());
            valuesMap.put(LIKEDBYUSER.LIKEDBYUSER_LIKEDUSERNAME, bean.getUsersBean().getUsername());
            valuesMap.put(LIKEDBYUSER.LIKEDBYUSER_LIKEDUSERPROFILENAME, bean.getUsersBean().getProfile_picture());
            valuesMap.put(LIKEDBYUSER.LIKEDBYUSER_LIKEDUSERFULLNAME, bean.getUsersBean().getFull_name());

            columnDetails.add(valuesMap);
            accessTokenOwnedUserId = bean.getLikedUserId();
        }
        DBQueries dbQueries = new DBQueries(context);
        long insertedRows = dbQueries.insertUpdateValuesToLIKEDBYUSER(LIKEDBYUSER.TABLE_NAME, columnDetails);
        Log.d(TAG, "saveILikedMostToDB:insertUpdateValues::" + insertedRows);
        readWriteLock.readLock().unlock();
        return getILikedMostFromDB(accessTokenOwnedUserId);
    }

//    public Callable<ArrayList<ILikedMostBean>> getILikedMost(final String accessTokenOwnedUserId) {
//        return new Callable<ArrayList<ILikedMostBean>>() {
//            @Override
//            public ArrayList<ILikedMostBean> call() throws Exception {
//                return getILikedMostFromDB(accessTokenOwnedUserId);
//            }
//        };
//    }

    public Observable<ArrayList<ILikedMostBean>> getILikedMost(final String accessTokenOwnedUserId) {
        return Observable.just(getILikedMostFromDB(accessTokenOwnedUserId));
    }


    private ArrayList<ILikedMostBean> getILikedMostFromDB(String accessTokenUserId) {
        ArrayList<ILikedMostBean> arylstILikedMost = new ArrayList<ILikedMostBean>();
        readWriteLock.readLock().lock();

        Cursor cursor = manager.getDB().query(
                true, // distinct
                LIKEDBYUSER.TABLE_NAME, // table name
                null, // projection
                LIKEDBYUSER.LIKEDBYUSER_LIKEDUSERID + " != ?", // selection
                new String[]{accessTokenUserId}, // selection args
                LIKEDBYUSER.LIKEDBYUSER_LIKEDUSERID, // group by
                null, // having
                LIKEDBYUSER.LIKEDBYUSER_CREATEDTIME + " desc", // order by
                null // limit
        );
        /*query(LIKEDBYUSER.TABLE_NAME, null, null, null, null, null, LIKEDBYUSER.LIKEDBYUSER_CREATEDTIME + " desc");*/

        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                ILikedMostBean data = new ILikedMostBean();

                data.setId(cursor.getString(cursor.getColumnIndex(LIKEDBYUSER.LIKEDBYUSER_MEDIAID)));
                data.setType(cursor.getString(cursor.getColumnIndex(LIKEDBYUSER.LIKEDBYUSER_TYPE)));
                ImagesBean imagesBean = new ImagesBean();
                StandardResolution standardResolution = new StandardResolution();
                standardResolution.setUrl(cursor.getString(cursor.getColumnIndex(LIKEDBYUSER.LIKEDBYUSER_IMAGEURL)));
                standardResolution.setHeight("640");
                standardResolution.setWidth("640");
                imagesBean.setStandardResolution(standardResolution);
                data.setImagesBean(imagesBean);
                LikesBean likesBean = new LikesBean();
                likesBean.setCount(cursor.getInt(cursor.getColumnIndex(LIKEDBYUSER.LIKEDBYUSER_LIKESCOUNT)));
                data.setLikesBean(likesBean);
                data.setLikedUserId(cursor.getString(cursor.getColumnIndex(LIKEDBYUSER.LIKEDBYUSER_USERID)));
                data.setCreated_time(cursor.getString(cursor.getColumnIndex(LIKEDBYUSER.LIKEDBYUSER_CREATEDTIME)));
                CommentsBean commentsBean = new CommentsBean();
                commentsBean.setCount(cursor.getString(cursor.getColumnIndex(LIKEDBYUSER.LIKEDBYUSER_COMMENTS)));
                data.setCommentsBean(commentsBean);
                data.setLink(cursor.getString(cursor.getColumnIndex(LIKEDBYUSER.LIKEDBYUSER_LINK)));
                UserBean userBean = new UserBean();
                userBean.setId(cursor.getString(cursor.getColumnIndex(LIKEDBYUSER.LIKEDBYUSER_LIKEDUSERID)));
                userBean.setFull_name(cursor.getString(cursor.getColumnIndex(LIKEDBYUSER.LIKEDBYUSER_LIKEDUSERFULLNAME)));
                userBean.setUsername(cursor.getString(cursor.getColumnIndex(LIKEDBYUSER.LIKEDBYUSER_LIKEDUSERNAME)));
                userBean.setProfile_picture(cursor.getString(cursor.getColumnIndex(LIKEDBYUSER.LIKEDBYUSER_LIKEDUSERPROFILENAME)));
                data.setUsersBean(userBean);
                arylstILikedMost.add(data);
                cursor.moveToNext();
            }
        }
        cursor.close();
        readWriteLock.readLock().unlock();
        return arylstILikedMost;
    }

}

