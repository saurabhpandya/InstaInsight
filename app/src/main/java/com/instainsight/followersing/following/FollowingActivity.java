package com.instainsight.followersing.following;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.instainsight.BaseActivity;
import com.instainsight.LoginActivity;
import com.instainsight.R;
import com.instainsight.Utils.DividerItemDecoration;
import com.instainsight.Utils.Utility;
import com.instainsight.constants.Constants;
import com.instainsight.followersing.adapter.FollowersingAdap;
import com.instainsight.followersing.following.dao.FollowingDao;
import com.instainsight.instagram.InstagramRequest;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FollowingActivity extends BaseActivity {

    private String TAG = FollowingActivity.class.getSimpleName();
    private RecyclerView rcyclrvw_following;
    private FollowersingAdap mAdapter;
    private ArrayList<Object> arylstFollowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        getIds();
        initRecyclerView();
        getFollowingData();
    }

    private void getIds() {
        rcyclrvw_following = (RecyclerView) findViewById(R.id.rcyclrvw_following);
    }

    private void initRecyclerView() {
        arylstFollowing = new ArrayList<Object>();
        mAdapter = new FollowersingAdap(FollowingActivity.this, arylstFollowing, "Following");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcyclrvw_following.setLayoutManager(mLayoutManager);
        rcyclrvw_following.setItemAnimator(new DefaultItemAnimator());
        rcyclrvw_following.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rcyclrvw_following.setAdapter(mAdapter);
    }

    private void getFollowingData() {
        if (mInstagramSession.isActive()) {
            if (isConnected()) {
                InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
                request.createRequest("GET", Constants.WebFields.ENDPOINT_FOLLOWEDBY, new ArrayList<NameValuePair>(),
                        new InstagramRequest.InstagramRequestListener() {
                            @Override
                            public void onSuccess(String response) {
                                FollowingDao followingDao = new FollowingDao(FollowingActivity.this);
                                arylstFollowing = followingDao.getFollowing(response);
                                followingDao.saveFollowing(arylstFollowing);
                                arylstFollowing = followingDao.getAllFollowing();
                                mAdapter.addFollowersing(arylstFollowing);
                                mAdapter.notifyDataSetChanged();
//                            mAdapter = new FollowersingAdap(FollowingActivity.this, arylstFollowing, "Following");
//                            rcyclrvw_following.setAdapter(mAdapter);

                                JSONObject jsnObjRsps = null;
                                try {
                                    jsnObjRsps = new JSONObject(response);
                                    if (jsnObjRsps.has("pagination")) {
                                        JSONObject jsnObjPagination = jsnObjRsps.getJSONObject("pagination");
                                        callNextUrl(jsnObjPagination);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(String error) {
                                Utility.showToast(FollowingActivity.this, error);
                            }
                        });
            } else {
                FollowingDao followingDao = new FollowingDao(FollowingActivity.this);
                arylstFollowing = followingDao.getAllFollowing();
                mAdapter.addFollowersing(arylstFollowing);
                mAdapter.notifyDataSetChanged();
            }
        } else {
            Utility.showToast(FollowingActivity.this, "Could not authentication, need to log in again");
            Intent intent = new Intent(FollowingActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void callNextUrl(JSONObject jsnObjPagination) {
        InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
        try {
            if (jsnObjPagination.has("next_url")) {
                String strNxtUrl = jsnObjPagination.getString("next_url");
                request.createPaginationRequest(strNxtUrl, new InstagramRequest.InstagramRequestListener() {
                    @Override
                    public void onSuccess(String response) {

                        FollowingDao followingDao = new FollowingDao(FollowingActivity.this);
                        arylstFollowing = followingDao.getFollowing(response);
                        followingDao.saveFollowing(arylstFollowing);
                        arylstFollowing = followingDao.getAllFollowing();
                        mAdapter.addFollowersing(arylstFollowing);
                        mAdapter.notifyDataSetChanged();

                        JSONObject jsnObjRsps = null;
                        try {
                            jsnObjRsps = new JSONObject(response);
                            if (jsnObjRsps.has("pagination")) {
                                JSONObject jsnObjPagination = jsnObjRsps.getJSONObject("pagination");
                                callNextUrl(jsnObjPagination);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
