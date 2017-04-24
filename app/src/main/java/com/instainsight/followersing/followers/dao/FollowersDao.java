package com.instainsight.followersing.followers.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.instainsight.Utils.Utility;
import com.instainsight.constants.Constants.WebFields;
import com.instainsight.db.DatabaseHelper;
import com.instainsight.followersing.followers.bean.FollowerBean;
import com.instainsight.followersing.following.bean.FollowingBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by SONY on 18-12-2016.
 */

public class FollowersDao {

    private String TAG = FollowersDao.class.getSimpleName();
    private Context mContext;

    public FollowersDao(Context context) {
        mContext = context;
    }

    public ArrayList<FollowerBean> getFollowers(String strFollowers) {
        ArrayList<FollowerBean> arylstFollowers = new ArrayList<>();

        try {
            JSONObject jsnObjData = new JSONObject(strFollowers);
            if (jsnObjData.has(WebFields.RSP_DATA)) {
                JSONArray jsnAryData = jsnObjData.getJSONArray(WebFields.RSP_DATA);
                for (int i = 0; i <= (jsnAryData.length() - 1); i++) {
                    FollowerBean followerBean = new FollowerBean();
                    JSONObject jsnObj = jsnAryData.getJSONObject(i);
                    if (jsnObj.has(WebFields.RSP_FOLLOWS_USERNAME))
                        followerBean.setUserName(jsnObj.getString(WebFields.RSP_FOLLOWS_USERNAME));
                    else
                        followerBean.setUserName("-");
                    if (jsnObj.has(WebFields.RSP_FOLLOWS_FULLNAME))
                        followerBean.setFullName(jsnObj.getString(WebFields.RSP_FOLLOWS_FULLNAME));
                    else
                        followerBean.setFullName("-");
                    if (jsnObj.has(WebFields.RSP_FOLLOWS_PROFILEPIC))
                        followerBean.setProfilePic(jsnObj.getString(WebFields.RSP_FOLLOWS_PROFILEPIC));
                    else
                        followerBean.setProfilePic("-");
                    if (jsnObj.has(WebFields.RSP_FOLLOWS_ID))
                        followerBean.setId(jsnObj.getString(WebFields.RSP_FOLLOWS_ID));
                    else
                        followerBean.setId("-");
                    arylstFollowers.add(followerBean);
                }
            }
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return arylstFollowers;
    }

    public void saveFollowers(ArrayList<FollowerBean> arylstFollowers) {

        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues values = new ContentValues();
            for (int i = 0; i <= (arylstFollowers.size() - 1); i++) {
                FollowerBean followerBean = arylstFollowers.get(i);
                values.put(DatabaseHelper.KEY_USERID, followerBean.getId());
                values.put(DatabaseHelper.KEY_FULLNAME, followerBean.getFullName());
                values.put(DatabaseHelper.KEY_USERNAME, followerBean.getUserName());
                values.put(DatabaseHelper.KEY_PROFILEPIC, followerBean.getProfilePic());

                Cursor cur = db.query(DatabaseHelper.TABLE_FOLLOWERS, null,
                        DatabaseHelper.KEY_USERID + " = ? ",
                        new String[]{followerBean.getId()}, null,
                        null, null);

                if (cur.getCount() == 0) {
                    values.put(DatabaseHelper.KEY_CREATEDAT, Utility.getDateTime());
                    values.put(DatabaseHelper.KEY_ISNEW, "1");

                    long inserId = db.insert(DatabaseHelper.TABLE_FOLLOWERS, null, values);
                    Log.d(TAG, "inserted : " + inserId);
                }
//                else {
//                    values.put(DatabaseHelper.KEY_ISNEW, "0");
//                    int updatedRecords = db.update(DatabaseHelper.TABLE_FOLLOWERS, values,
//                            DatabaseHelper.KEY_USERID + " = ? ",
//                            new String[]{followerBean.getId()});
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

    public ArrayList<FollowerBean> getAllFollowers() {
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<FollowerBean> arylstFollowers = new ArrayList<FollowerBean>();
        Cursor cur = db.query(DatabaseHelper.TABLE_FOLLOWERS, null, null, null, null, null, null);
        if (cur.getCount() > 0 && cur.moveToFirst()) {

            do {
                FollowerBean followerBean = new FollowerBean();
                followerBean.setId(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_USERID)));
                followerBean.setUserName(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_USERNAME)));
                followerBean.setProfilePic(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_PROFILEPIC)));
                followerBean.setFullName(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_FULLNAME)));
                arylstFollowers.add(followerBean);
            } while (cur.moveToNext());
            db.close();
        }
        return arylstFollowers;
    }

    public ArrayList<FollowerBean> getNewFollowers() {
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<FollowerBean> arylstFollowers = new ArrayList<FollowerBean>();
        Cursor cur = db.query(DatabaseHelper.TABLE_FOLLOWERS, null,
                DatabaseHelper.KEY_ISNEW + "=?", new String[]{"1"}, null, null, null);
        if (cur.getCount() > 0 && cur.moveToFirst()) {

            do {
                FollowerBean followerBean = new FollowerBean();
                followerBean.setId(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_USERID)));
                followerBean.setUserName(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_USERNAME)));
                followerBean.setProfilePic(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_PROFILEPIC)));
                followerBean.setFullName(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_FULLNAME)));
                arylstFollowers.add(followerBean);
            } while (cur.moveToNext());
            db.close();
        }
        return arylstFollowers;
    }

    public void removePreviousFollowers() {
        ArrayList<FollowerBean> arylstFollowers = getNewFollowers();
        if (arylstFollowers != null && arylstFollowers.size() > 0) {
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                db.beginTransaction();
                ContentValues values = new ContentValues();
                for (int i = 0; i <= (arylstFollowers.size() - 1); i++) {
                    FollowerBean followerBean = (FollowerBean) arylstFollowers.get(i);
                    values.put(DatabaseHelper.KEY_USERID, followerBean.getId());
                    values.put(DatabaseHelper.KEY_FULLNAME, followerBean.getFullName());
                    values.put(DatabaseHelper.KEY_USERNAME, followerBean.getUserName());
                    values.put(DatabaseHelper.KEY_PROFILEPIC, followerBean.getProfilePic());

                    Cursor cur = db.query(DatabaseHelper.TABLE_FOLLOWERS, null,
                            DatabaseHelper.KEY_USERID + " = ? ",
                            new String[]{followerBean.getId()}, null,
                            null, null);

//                    if (cur.getCount() == 0) {
//                        values.put(DatabaseHelper.KEY_CREATEDAT, Utility.getDateTime());
//                        values.put(DatabaseHelper.KEY_ISNEW, "1");
//
//                        long inserId = db.insert(DatabaseHelper.TABLE_FOLLOWERS, null, values);
//                        Log.d(TAG, "inserted : " + inserId);
//                    }
//                else
                    if (cur.getCount() > 0) {
                        values.put(DatabaseHelper.KEY_ISNEW, "0");
                        int updatedRecords = db.update(DatabaseHelper.TABLE_FOLLOWERS, values,
                                DatabaseHelper.KEY_USERID + " = ? ",
                                new String[]{followerBean.getId()});
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

    public ArrayList<FollowingBean> getFollowersToWhomNotFollowing() {
        // Get the followers who are not in following
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<FollowingBean> arylstFollowersNotFollowing = new ArrayList<>();

//        select distinct *
//        from old a join new b on a.ManName = b. Manager_Name and a. ManNumber = b. Manager_Number
//        SELECT * FROM DatabaseHelper.TABLE_FOLLOWING WHERE DatabaseHelper.KEY_USERID NOT IN ()
        String followersToWhomNotFollowingQuery = "SELECT DISTINCT * FROM " +
                DatabaseHelper.TABLE_FOLLOWERS + " followers " +
                "JOIN " +
                DatabaseHelper.TABLE_FOLLOWING + " following " +
                "ON " +
                "followers." + DatabaseHelper.KEY_USERID + " != following." + DatabaseHelper.KEY_USERID;
        Cursor curFollowersNotFollowing = db.rawQuery(followersToWhomNotFollowingQuery, null);
        Log.d(TAG, "getFollowersToWhomNotFollowing:curFollowersNotFollowing.getCount()::" + curFollowersNotFollowing.getCount());
        if (curFollowersNotFollowing.getCount() > 0 && curFollowersNotFollowing.moveToFirst()) {
            do {
                FollowingBean followingBean = new FollowingBean();
                followingBean.setId(curFollowersNotFollowing.getString(curFollowersNotFollowing.getColumnIndex(DatabaseHelper.KEY_USERID)));
                followingBean.setUserName(curFollowersNotFollowing.getString(curFollowersNotFollowing.getColumnIndex(DatabaseHelper.KEY_USERNAME)));
                followingBean.setProfilePic(curFollowersNotFollowing.getString(curFollowersNotFollowing.getColumnIndex(DatabaseHelper.KEY_PROFILEPIC)));
                followingBean.setFullName(curFollowersNotFollowing.getString(curFollowersNotFollowing.getColumnIndex(DatabaseHelper.KEY_FULLNAME)));
                arylstFollowersNotFollowing.add(followingBean);
            } while (curFollowersNotFollowing.moveToNext());
            db.close();
        }
        return arylstFollowersNotFollowing;
    }
}
