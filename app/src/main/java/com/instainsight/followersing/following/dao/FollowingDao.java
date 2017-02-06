package com.instainsight.followersing.following.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.instainsight.Utils.Utility;
import com.instainsight.constants.Constants.WebFields;
import com.instainsight.db.DatabaseHelper;
import com.instainsight.followersing.following.bean.FollowingBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by SONY on 18-12-2016.
 */

public class FollowingDao {

    private String TAG = FollowingDao.class.getSimpleName();
    private Context mContext;

    public FollowingDao(Context context) {
        mContext = context;
    }

    public ArrayList<Object> getFollowing(String strFollowing) {
        ArrayList<Object> arylstFollowing = new ArrayList<Object>();

        try {
            JSONObject jsnObjData = new JSONObject(strFollowing);
            if (jsnObjData.has(WebFields.RSP_DATA)) {
                JSONArray jsnAryData = jsnObjData.getJSONArray(WebFields.RSP_DATA);
                for (int i = 0; i <= jsnAryData.length() - 1; i++) {
                    FollowingBean followingBean = new FollowingBean();
                    JSONObject jsnObj = jsnAryData.getJSONObject(i);
                    if (jsnObj.has(WebFields.RSP_FOLLOWS_USERNAME))
                        followingBean.setUserName(jsnObj.getString(WebFields.RSP_FOLLOWS_USERNAME));
                    else
                        followingBean.setUserName("-");
                    if (jsnObj.has(WebFields.RSP_FOLLOWS_FULLNAME))
                        followingBean.setFullName(jsnObj.getString(WebFields.RSP_FOLLOWS_FULLNAME));
                    else
                        followingBean.setFullName("-");
                    if (jsnObj.has(WebFields.RSP_FOLLOWS_PROFILEPIC))
                        followingBean.setProfilePic(jsnObj.getString(WebFields.RSP_FOLLOWS_PROFILEPIC));
                    else
                        followingBean.setProfilePic("-");
                    if (jsnObj.has(WebFields.RSP_FOLLOWS_ID))
                        followingBean.setId(jsnObj.getString(WebFields.RSP_FOLLOWS_ID));
                    else
                        followingBean.setId("-");
                    arylstFollowing.add(followingBean);
                }
            }
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return arylstFollowing;
    }

    public void saveFollowing(ArrayList<Object> arylstFollowing) {
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues values = new ContentValues();
            for (int i = 0; i <= (arylstFollowing.size() - 1); i++) {
                FollowingBean followingBean = (FollowingBean) arylstFollowing.get(i);
                values.put(DatabaseHelper.KEY_USERID, followingBean.getId());
                values.put(DatabaseHelper.KEY_FULLNAME, followingBean.getFullName());
                values.put(DatabaseHelper.KEY_USERNAME, followingBean.getUserName());
                values.put(DatabaseHelper.KEY_PROFILEPIC, followingBean.getProfilePic());

                Cursor cur = db.query(DatabaseHelper.TABLE_FOLLOWING, null,
                        DatabaseHelper.KEY_USERID + " = ? ",
                        new String[]{followingBean.getId()}, null,
                        null, null);

                if (cur.getCount() == 0) {
                    values.put(DatabaseHelper.KEY_CREATEDAT, Utility.getDateTime());
                    values.put(DatabaseHelper.KEY_ISNEW, "1");
                    long inserId = db.insert(DatabaseHelper.TABLE_FOLLOWING, null, values);
                    Log.d(TAG, "inserted : " + inserId);
                }
//                else {
//                    values.put(DatabaseHelper.KEY_ISNEW, "0");
//                    int updatedRecords = db.update(DatabaseHelper.TABLE_FOLLOWING, values,
//                            DatabaseHelper.KEY_USERID + " = ? ",
//                            new String[]{followingBean.getId()});
//                    Log.d(TAG, "updated : " + updatedRecords);
//                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public ArrayList<Object> getAllFollowing() {
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<Object> arylstFollowing = new ArrayList<Object>();
        Cursor cur = db.query(DatabaseHelper.TABLE_FOLLOWING, null, null, null, null, null, null);
        if (cur.getCount() > 0 && cur.moveToFirst()) {
            FollowingBean followingBean = new FollowingBean();
            do {
                followingBean.setId(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_USERID)));
                followingBean.setUserName(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_USERNAME)));
                followingBean.setProfilePic(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_PROFILEPIC)));
                followingBean.setFullName(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_FULLNAME)));
                arylstFollowing.add(followingBean);
            } while (cur.moveToNext());
            db.close();
        }
        return arylstFollowing;
    }

    public ArrayList<Object> getNewFollowing() {
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<Object> arylstFollowing = new ArrayList<Object>();
        Cursor cur = db.query(DatabaseHelper.TABLE_FOLLOWING, null,
                DatabaseHelper.KEY_ISNEW + "=?", new String[]{"1"}, null, null, null);
        if (cur.getCount() > 0 && cur.moveToFirst()) {
            FollowingBean followingBean = new FollowingBean();
            do {
                followingBean.setId(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_USERID)));
                followingBean.setUserName(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_USERNAME)));
                followingBean.setProfilePic(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_PROFILEPIC)));
                followingBean.setFullName(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_FULLNAME)));
                arylstFollowing.add(followingBean);
            } while (cur.moveToNext());
            db.close();
        }
        return arylstFollowing;
    }

    public void removePreviousFollowing() {
        ArrayList<Object> arylstFollowing = getNewFollowing();
        if (arylstFollowing != null && arylstFollowing.size() > 0) {
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                db.beginTransaction();
                ContentValues values = new ContentValues();
                for (int i = 0; i <= (arylstFollowing.size() - 1); i++) {
                    FollowingBean followingBean = (FollowingBean) arylstFollowing.get(i);
                    values.put(DatabaseHelper.KEY_USERID, followingBean.getId());
                    values.put(DatabaseHelper.KEY_FULLNAME, followingBean.getFullName());
                    values.put(DatabaseHelper.KEY_USERNAME, followingBean.getUserName());
                    values.put(DatabaseHelper.KEY_PROFILEPIC, followingBean.getProfilePic());

                    Cursor cur = db.query(DatabaseHelper.TABLE_FOLLOWING, null,
                            DatabaseHelper.KEY_USERID + " = ? ",
                            new String[]{followingBean.getId()}, null,
                            null, null);

//                    if (cur.getCount() == 0) {
//                        values.put(DatabaseHelper.KEY_CREATEDAT, Utility.getDateTime());
//                        values.put(DatabaseHelper.KEY_ISNEW, "1");
//                        long inserId = db.insert(DatabaseHelper.TABLE_FOLLOWING, null, values);
//                        Log.d(TAG, "inserted : " + inserId);
//                    }
//                else
                    if (cur.getCount() > 0) {
                        values.put(DatabaseHelper.KEY_ISNEW, "0");
                        int updatedRecords = db.update(DatabaseHelper.TABLE_FOLLOWING, values,
                                DatabaseHelper.KEY_USERID + " = ? ",
                                new String[]{followingBean.getId()});
                        Log.d(TAG, "updated : " + updatedRecords);
                    }
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
                db.close();
            }
        }
    }

    public ArrayList<Object> getFollowingsNotFollowingBack() {
        // Get the following who are not in followers
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<Object> arylstNotFollowingBack = new ArrayList<Object>();

//        select distinct *
//        from old a join new b on a.ManName = b. Manager_Name and a. ManNumber = b. Manager_Number
//        SELECT * FROM DatabaseHelper.TABLE_FOLLOWING WHERE DatabaseHelper.KEY_USERID NOT IN ()
        String notFollowingBackQuery = "SELECT DISTINCT * FROM " +
                DatabaseHelper.TABLE_FOLLOWING + " following " +
                "JOIN " +
                DatabaseHelper.TABLE_FOLLOWERS + " followers " +
                "ON " +
                "following." + DatabaseHelper.KEY_USERID + " != followers." + DatabaseHelper.KEY_USERID;
        Cursor curNotFollowingBack = db.rawQuery(notFollowingBackQuery, null);
        Log.d(TAG, "getFollowingsNotFollowingBack:curNotFollowingBack.getCount()::" + curNotFollowingBack.getCount());

        if (curNotFollowingBack.getCount() > 0 && curNotFollowingBack.moveToFirst()) {
            FollowingBean followingBean = new FollowingBean();
            do {
                followingBean.setId(curNotFollowingBack.getString(curNotFollowingBack.getColumnIndex(DatabaseHelper.KEY_USERID)));
                followingBean.setUserName(curNotFollowingBack.getString(curNotFollowingBack.getColumnIndex(DatabaseHelper.KEY_USERNAME)));
                followingBean.setProfilePic(curNotFollowingBack.getString(curNotFollowingBack.getColumnIndex(DatabaseHelper.KEY_PROFILEPIC)));
                followingBean.setFullName(curNotFollowingBack.getString(curNotFollowingBack.getColumnIndex(DatabaseHelper.KEY_FULLNAME)));
                arylstNotFollowingBack.add(followingBean);
            } while (curNotFollowingBack.moveToNext());
            db.close();
        }
        return arylstNotFollowingBack;
    }
}
