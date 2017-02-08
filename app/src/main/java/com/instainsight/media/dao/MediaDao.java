package com.instainsight.media.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.instainsight.constants.Constants;
import com.instainsight.db.DatabaseHelper;
import com.instainsight.media.bean.MediaBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by SONY on 07-02-2017.
 */

public class MediaDao {

    public Context mContext;
    public String TAG = MediaDao.class.getSimpleName();

    public MediaDao(Context context) {
        mContext = context;
    }

    public ArrayList<MediaBean> getMediaDetailsFromJson(String strMedia) {
        ArrayList<MediaBean> aryLstMediaBean = new ArrayList<MediaBean>();
        try {
            JSONObject jsonObject = new JSONObject(strMedia);
            if (jsonObject.has(Constants.WebFields.RSP_DATA)) {
                JSONArray jsnArryData = jsonObject.getJSONArray(Constants.WebFields.RSP_DATA);
                for (int i = 0; i < jsnArryData.length(); i++) {
                    MediaBean mediaBean = new MediaBean();
                    JSONObject jsnObjData = jsnArryData.getJSONObject(i);
                    if (jsnObjData.has(DatabaseHelper.KEY_MEDIA_TYPE))
                        mediaBean.setType(jsnObjData.getString(DatabaseHelper.KEY_MEDIA_TYPE));
                    if (jsnObjData.has(DatabaseHelper.KEY_MEDIA_MEDIAID))
                        mediaBean.setMediaId(jsnObjData.getString(DatabaseHelper.KEY_MEDIA_MEDIAID));
                    if (jsnObjData.has(DatabaseHelper.KEY_MEDIA_IMAGES)) {
                        JSONObject jsnObjImages = jsnObjData.getJSONObject(DatabaseHelper.KEY_MEDIA_IMAGES);
                        if (jsnObjImages.has(DatabaseHelper.KEY_MEDIA_STANDARDRESOLUTION)) {
                            JSONObject jsnObjStandardUrl = jsnObjImages.getJSONObject(DatabaseHelper.KEY_MEDIA_STANDARDRESOLUTION);
                            if (jsnObjStandardUrl.has(DatabaseHelper.KEY_MEDIA_IMAGEURL))
                                mediaBean.setImageurl(jsnObjStandardUrl.getString(DatabaseHelper.KEY_MEDIA_IMAGEURL));
                        }
                    }
                    if (jsnObjData.has(DatabaseHelper.KEY_MEDIA_LIKES)) {
                        JSONObject jsnObjLikes = jsnObjData.getJSONObject(DatabaseHelper.KEY_MEDIA_LIKES);
                        if (jsnObjLikes.has(DatabaseHelper.KEY_MEDIA_LIKESCOUNT))
                            mediaBean.setLikes(jsnObjLikes.getString(DatabaseHelper.KEY_MEDIA_LIKESCOUNT));
                    }

                    if (jsnObjData.has(DatabaseHelper.KEY_MEDIA_USER)) {
                        JSONObject jsnObjUser = jsnObjData.getJSONObject(DatabaseHelper.KEY_MEDIA_USER);
                        if (jsnObjUser.has(DatabaseHelper.KEY_MEDIA_USERID))
                            mediaBean.setUserid(jsnObjUser.getString(DatabaseHelper.KEY_MEDIA_USERID));
                    }
                    if (jsnObjData.has(DatabaseHelper.KEY_MEDIA_CREATEDTIME))
                        mediaBean.setCreated_time(jsnObjData.getString(DatabaseHelper.KEY_MEDIA_CREATEDTIME));

                    if (jsnObjData.has(DatabaseHelper.KEY_MEDIA_COMMENTS)) {
                        JSONObject jsnObjCommentCounts = jsnObjData.getJSONObject(DatabaseHelper.KEY_MEDIA_COMMENTS);
                        if (jsnObjCommentCounts.has(DatabaseHelper.KEY_MEDIA_COMMENTSCOUNT))
                            mediaBean.setComments(jsnObjCommentCounts.getString(DatabaseHelper.KEY_MEDIA_COMMENTSCOUNT));
                    }

                    if (jsnObjData.has(DatabaseHelper.KEY_MEDIA_LINK))
                        mediaBean.setLink(jsnObjData.getString(DatabaseHelper.KEY_MEDIA_LINK));
                    aryLstMediaBean.add(mediaBean);
                }
            }
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return aryLstMediaBean;
    }

    public ArrayList<MediaBean> saveMedia(ArrayList<MediaBean> aryLstMediaBean) {
        ArrayList<MediaBean> aryLstMediaBeans = new ArrayList<MediaBean>();
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (int i = 0; i < aryLstMediaBean.size(); i++) {
            MediaBean mediaBean = aryLstMediaBean.get(i);
            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.KEY_MEDIA_MEDIAID_, mediaBean.getMediaId());
            values.put(DatabaseHelper.KEY_MEDIA_USERID, mediaBean.getUserid());
            values.put(DatabaseHelper.KEY_MEDIA_TYPE, mediaBean.getType());
            values.put(DatabaseHelper.KEY_MEDIA_IMAGEURL_, mediaBean.getImageurl());
            values.put(DatabaseHelper.KEY_MEDIA_LIKESCOUNT_, mediaBean.getLikes());
            values.put(DatabaseHelper.KEY_MEDIA_CREATEDTIME, mediaBean.getCreated_time());
            values.put(DatabaseHelper.KEY_MEDIA_COMMENTSCOUNT_, mediaBean.getComments());
            values.put(DatabaseHelper.KEY_MEDIA_LINK, mediaBean.getLink());

            Cursor cur = db.query(DatabaseHelper.TABLE_MEDIA, null,
                    DatabaseHelper.KEY_MEDIA_MEDIAID_ + " = ? ", new String[]{mediaBean.getMediaId()},
                    null, null, null);

            if (cur.getCount() == 0) {
                long inserId = db.insert(DatabaseHelper.TABLE_MEDIA, null, values);
                Log.d(TAG, "inserted : " + inserId);
            } else {
                int updatedRecords = db.update(DatabaseHelper.TABLE_MEDIA, values,
                        DatabaseHelper.KEY_MEDIA_MEDIAID_ + " = ? ",
                        new String[]{mediaBean.getMediaId()});
                Log.d(TAG, "updated : " + updatedRecords);
            }
        }
        db.close();
        aryLstMediaBeans = getMedia();
        return aryLstMediaBeans;
    }

    public ArrayList<MediaBean> getMedia() {
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cur = db.query(DatabaseHelper.TABLE_MEDIA, null, null, null, null, null,
                DatabaseHelper.KEY_MEDIA_CREATEDTIME + " DESC");
        ArrayList<MediaBean> aryLstMediaBean = new ArrayList<MediaBean>();
        if (cur.getCount() > 0 && cur.moveToFirst()) {
            do {
                MediaBean mediaBean = new MediaBean();
                mediaBean.setMediaId(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_MEDIA_MEDIAID)));
                mediaBean.setUserid(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_MEDIA_USERID)));
                mediaBean.setType(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_MEDIA_TYPE)));
                mediaBean.setType(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_MEDIA_IMAGEURL_)));
                mediaBean.setLikes(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_MEDIA_LIKESCOUNT_)));
                mediaBean.setCreated_time(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_MEDIA_CREATEDTIME)));
                mediaBean.setComments(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_MEDIA_COMMENTSCOUNT_)));
                mediaBean.setLink(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_MEDIA_LINK)));
                aryLstMediaBean.add(mediaBean);
            } while (cur.moveToNext());
            db.close();
        }
        return aryLstMediaBean;
    }

}
