package com.instainsight.followersing.followers;

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
import com.instainsight.constants.Constants.WebFields;
import com.instainsight.followersing.adapter.FollowersingAdap;
import com.instainsight.followersing.followers.dao.FollowersDao;
import com.instainsight.instagram.InstagramRequest;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FollowersActivity extends BaseActivity {

    private String TAG = FollowersActivity.class.getSimpleName();
    private RecyclerView rcyclrvw_follower;
    private FollowersingAdap mAdapter;
    private ArrayList<Object> arylstFollowers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        getIds();
        initRecyclerView();
        getFollowersData();
    }

    private void getIds() {
        rcyclrvw_follower = (RecyclerView) findViewById(R.id.rcyclrvw_follower);
    }

    private void initRecyclerView() {
        arylstFollowers = new ArrayList<Object>();
        mAdapter = new FollowersingAdap(FollowersActivity.this, arylstFollowers, "Follower");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcyclrvw_follower.setLayoutManager(mLayoutManager);
        rcyclrvw_follower.setItemAnimator(new DefaultItemAnimator());
        rcyclrvw_follower.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rcyclrvw_follower.setAdapter(mAdapter);
    }

    private void getFollowersData() {
        if (mInstagramSession.isActive()) {
            if (isConnected()) {
                InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
                request.createRequest("GET", WebFields.ENDPOINT_FOLLOWEDBY, new ArrayList<NameValuePair>(),
                        new InstagramRequest.InstagramRequestListener() {
                            @Override
                            public void onSuccess(String response) {
                                FollowersDao followersDao = new FollowersDao(FollowersActivity.this);
                                arylstFollowers = followersDao.getFollowers(response);
                                followersDao.saveFollowers(arylstFollowers);
//                            Utility.showToast(getApplicationContext(), "Found " + arylstFollowers.size() + " followers.");
                                arylstFollowers = followersDao.getAllFollowers();
                                mAdapter.addFollowersing(arylstFollowers);
                                mAdapter.notifyDataSetChanged();
//                            mAdapter = new FollowersingAdap(FollowersActivity.this, arylstFollowers, "Follower");
//                            rcyclrvw_follower.setAdapter(mAdapter);
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
                                Utility.showToast(FollowersActivity.this, error);
                            }
                        }

                );
            } else {
                FollowersDao followersDao = new FollowersDao(FollowersActivity.this);
                arylstFollowers = followersDao.getAllFollowers();
                mAdapter.addFollowersing(arylstFollowers);
                mAdapter.notifyDataSetChanged();
            }
        } else {
            Utility.showToast(FollowersActivity.this, "Could not authentication, need to log in again");
            Intent intent = new Intent(FollowersActivity.this, LoginActivity.class);
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

                        FollowersDao followersDao = new FollowersDao(FollowersActivity.this);
                        arylstFollowers = followersDao.getFollowers(response);
                        followersDao.saveFollowers(arylstFollowers);
                        arylstFollowers = followersDao.getAllFollowers();
                        mAdapter.addFollowersing(arylstFollowers);
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
