package com.instainsight.profile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.instainsight.constants.Constants.WebFields;
import com.instainsight.db.DatabaseHelper;
import com.instainsight.models.UserCountBean;
import com.instainsight.profile.bean.UsersBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SONY on 18-12-2016.
 */

public class UsersDao {

    public Context mContext;
    public String TAG = UsersDao.class.getSimpleName();

    public UsersDao(Context context) {
        mContext = context;
    }

    public UsersBean saveUserDetails(UsersBean usersBean) {
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_USERID, usersBean.getUserId());
        values.put(DatabaseHelper.KEY_FULLNAME, usersBean.getUserFullName());
        values.put(DatabaseHelper.KEY_USERNAME, usersBean.getUserName());
        values.put(DatabaseHelper.KEY_PROFILEPIC, usersBean.getProfilePic());
        values.put(DatabaseHelper.KEY_BIO, usersBean.getBio());
        values.put(DatabaseHelper.KEY_FOLLOWERCOUNT, usersBean.getUserCountBean().getFollowed_by());
        values.put(DatabaseHelper.KEY_FOLLOWINGCOUNT, usersBean.getUserCountBean().getFollows());

        Cursor cur = db.query(DatabaseHelper.TABLE_USERS, null,
                DatabaseHelper.KEY_USERID + " = ? ", new String[]{usersBean.getUserId()},
                null, null, null);

        if (cur.getCount() == 0) {
            values.put(DatabaseHelper.KEY_NEWFOLLOWERCOUNT, usersBean.getUserCountBean().getFollowed_by());
            values.put(DatabaseHelper.KEY_NEWFOLLOWINGCOUNT, usersBean.getUserCountBean().getFollows());
            long inserId = db.insert(DatabaseHelper.TABLE_USERS, null, values);
            Log.d(TAG, "inserted : " + inserId);
        } else {
            cur.moveToFirst();
            int followerCount = usersBean.getUserCountBean().getFollowed_by();
            int followingCount = usersBean.getUserCountBean().getFollows();
            int newFollowerCount = Integer.parseInt(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_NEWFOLLOWERCOUNT)));
            int newFollowingCount = Integer.parseInt(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_NEWFOLLOWINGCOUNT)));

            if (newFollowerCount > followerCount)
                usersBean.setNewFollowedByCount(String.valueOf(newFollowerCount - followerCount));
            else
                usersBean.setNewFollowedByCount("0");

            if (newFollowingCount > followingCount)
                usersBean.setNewFollowsCount(String.valueOf(newFollowingCount - followingCount));
            else
                usersBean.setNewFollowsCount("0");

            values.put(DatabaseHelper.KEY_NEWFOLLOWERCOUNT, usersBean.getNewFollowedByCount());
            values.put(DatabaseHelper.KEY_NEWFOLLOWINGCOUNT, usersBean.getNewFollowsCount());
            int updatedRecords = db.update(DatabaseHelper.TABLE_USERS, values,
                    DatabaseHelper.KEY_USERID + " = ? ",
                    new String[]{usersBean.getUserId()});
            Log.d(TAG, "updated : " + updatedRecords);
        }
        db.close();
        usersBean = getUserDetails(usersBean.getUserId());
        return usersBean;
    }

    public UsersBean getUserDetails(String userId) {
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cur = db.query(DatabaseHelper.TABLE_USERS, null,
                DatabaseHelper.KEY_USERID + " = ? ", new String[]{userId},
                null, null, null);
        UsersBean usersBean = new UsersBean();
        if (cur.getCount() > 0 && cur.moveToFirst()) {
            do {
                usersBean.setUserId(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_USERID)));
                usersBean.setUserName(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_USERNAME)));
                usersBean.setProfilePic(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_PROFILEPIC)));
                usersBean.setUserFullName(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_FULLNAME)));
                usersBean.setBio(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_BIO)));
                String followedByCount = cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_FOLLOWERCOUNT));
                String followsCount = cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_FOLLOWINGCOUNT));
                UserCountBean userCountBean = new UserCountBean();
                userCountBean.setFollowed_by(Integer.parseInt(followedByCount));
                userCountBean.setFollows(Integer.parseInt(followsCount));
                usersBean.setUserCountBean(userCountBean);
                usersBean.setNewFollowedByCount(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_NEWFOLLOWERCOUNT)));
                usersBean.setNewFollowsCount(cur.getString(cur.getColumnIndex(DatabaseHelper.KEY_NEWFOLLOWINGCOUNT)));
            } while (cur.moveToNext());
            db.close();
        }
        return usersBean;
    }

    public UsersBean getUserDetailsFromJson(String strFollowingFollowers) {
        UsersBean usersBean = new UsersBean();
        try {
            JSONObject jsonObject = new JSONObject(strFollowingFollowers);
            if (jsonObject.has(WebFields.RSP_DATA)) {
                JSONObject jsnObjData = jsonObject.getJSONObject(WebFields.RSP_DATA);
                if (jsnObjData.has(DatabaseHelper.KEY_USERID))
                    usersBean.setUserId(jsnObjData.getString(DatabaseHelper.KEY_USERID));
                if (jsnObjData.has(DatabaseHelper.KEY_USERNAME))
                    usersBean.setUserName(jsnObjData.getString(DatabaseHelper.KEY_USERNAME));
                if (jsnObjData.has(DatabaseHelper.KEY_FULLNAME))
                    usersBean.setUserFullName(jsnObjData.getString(DatabaseHelper.KEY_FULLNAME));
                if (jsnObjData.has(DatabaseHelper.KEY_PROFILEPIC))
                    usersBean.setProfilePic(jsnObjData.getString(DatabaseHelper.KEY_PROFILEPIC));
                if (jsnObjData.has(DatabaseHelper.KEY_BIO))
                    usersBean.setBio(jsnObjData.getString(DatabaseHelper.KEY_BIO));
                if (jsnObjData.has(WebFields.RSP_USERSELF_COUNTS)) {
                    JSONObject jsnObjDataCounts = jsnObjData.getJSONObject(WebFields.RSP_USERSELF_COUNTS);
                    UserCountBean userCountBean = new UserCountBean();
                    if (jsnObjDataCounts.has(WebFields.RSP_USERSELF_FOLLOWEDBY))
                        userCountBean.setFollowed_by(jsnObjDataCounts.getInt(WebFields.RSP_USERSELF_FOLLOWEDBY));
                    else
                        userCountBean.setFollowed_by(0);

                    if (jsnObjDataCounts.has(WebFields.RSP_USERSELF_FOLLOWS))
                        userCountBean.setFollows(jsnObjDataCounts.getInt(WebFields.RSP_USERSELF_FOLLOWS));
                    else
                        userCountBean.setFollows(0);
                    usersBean.setUserCountBean(userCountBean);
                }
            }
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return usersBean;
    }

}
