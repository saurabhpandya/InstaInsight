package com.instainsight.followersing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.instainsight.BaseActivity;
import com.instainsight.R;
import com.instainsight.Utils.DividerItemDecoration;
import com.instainsight.Utils.Utility;
import com.instainsight.constants.Constants;
import com.instainsight.followersing.adapter.FollowersingAdap;
import com.instainsight.followersing.followers.dao.FollowersDao;
import com.instainsight.followersing.following.dao.FollowingDao;
import com.instainsight.instagram.InstagramRequest;
import com.instainsight.login.LoginActivity;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotFollowingMeBackActivity extends BaseActivity {

    private String TAG = NotFollowingMeBackActivity.class.getSimpleName();
    private RecyclerView rcyclrvw_notfollowingmeback;
    private TextView txtvw_no_notfollowingmeback;
    private FollowersingAdap mAdapter;
    private ArrayList<Object> arylstNotFollowingMeBack;
    private ProgressBar prgsbr_notfollowingmeback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_following_me_back);
        setTitle(R.string.lbl_blockedfollowers);
        initActionbar();
        getIds();
        initRecyclerView();
        getFollowersData();
    }

    private void initActionbar() {
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.base));
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getIds() {
        rcyclrvw_notfollowingmeback = (RecyclerView) findViewById(R.id.rcyclrvw_notfollowingmeback);
        txtvw_no_notfollowingmeback = (TextView) findViewById(R.id.txtvw_no_notfollowingmeback);
        prgsbr_notfollowingmeback = (ProgressBar) findViewById(R.id.prgsbr_notfollowingmeback);
    }

    private void initRecyclerView() {
        arylstNotFollowingMeBack = new ArrayList<Object>();
        mAdapter = new FollowersingAdap(NotFollowingMeBackActivity.this, arylstNotFollowingMeBack, "NotFollowingMeBack");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcyclrvw_notfollowingmeback.setLayoutManager(mLayoutManager);
        rcyclrvw_notfollowingmeback.setItemAnimator(new DefaultItemAnimator());
        rcyclrvw_notfollowingmeback.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rcyclrvw_notfollowingmeback.setAdapter(mAdapter);
    }

    private void getData() {
        FollowersDao followersDao = new FollowersDao(NotFollowingMeBackActivity.this);
        followersDao.getFollowersToWhomNotFollowing();
    }

    private void getFollowersData() {
        if (mInstagramSession.isActive()) {
            if (isConnected()) {
                prgsbr_notfollowingmeback.setVisibility(View.VISIBLE);
                InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
                request.createRequest("GET", Constants.WebFields.ENDPOINT_FOLLOWEDBY, new ArrayList<NameValuePair>(),
                        new InstagramRequest.InstagramRequestListener() {
                            @Override
                            public void onSuccess(String response) {
                                FollowersDao followersDao = new FollowersDao(NotFollowingMeBackActivity.this);
                                ArrayList<Object> arylstFollowers = followersDao.getFollowers(response);
                                followersDao.saveFollowers(arylstFollowers);
                                JSONObject jsnObjRsps = null;
                                try {
                                    jsnObjRsps = new JSONObject(response);
                                    if (jsnObjRsps.has("pagination")) {
                                        JSONObject jsnObjPagination = jsnObjRsps.getJSONObject("pagination");
                                        callNextUrlToFetchFollowers(jsnObjPagination);
                                    } else {
                                        getFollowingData();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(String error) {
                                Utility.showToast(NotFollowingMeBackActivity.this, error);
                            }
                        }

                );
            } else {
                getNotFollowingMeBackData();
            }
        } else {
            Utility.showToast(NotFollowingMeBackActivity.this, "Could not authentication, need to log in again");
            Intent intent = new Intent(NotFollowingMeBackActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

    }

    private void callNextUrlToFetchFollowers(JSONObject jsnObjPagination) {
        InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
        try {
            if (jsnObjPagination.has("next_url")) {
                String strNxtUrl = jsnObjPagination.getString("next_url");
                request.createPaginationRequest(strNxtUrl, new InstagramRequest.InstagramRequestListener() {
                    @Override
                    public void onSuccess(String response) {

                        FollowersDao followersDao = new FollowersDao(NotFollowingMeBackActivity.this);
                        ArrayList<Object> arylstFollowers = followersDao.getFollowers(response);
                        followersDao.saveFollowers(arylstFollowers);

                        JSONObject jsnObjRsps = null;
                        try {
                            jsnObjRsps = new JSONObject(response);
                            if (jsnObjRsps.has("pagination")) {
                                JSONObject jsnObjPagination = jsnObjRsps.getJSONObject("pagination");
                                callNextUrlToFetchFollowers(jsnObjPagination);
                            } else {
                                getFollowingData();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            } else {
                getFollowingData();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getFollowingData() {
        if (mInstagramSession.isActive()) {
            if (isConnected()) {
                InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
                request.createRequest("GET", Constants.WebFields.ENDPOINT_FOLLOWEDBY, new ArrayList<NameValuePair>(),
                        new InstagramRequest.InstagramRequestListener() {
                            @Override
                            public void onSuccess(String response) {
                                FollowingDao followingDao = new FollowingDao(NotFollowingMeBackActivity.this);
                                ArrayList<Object> arylstFollowing = followingDao.getFollowing(response);
                                followingDao.saveFollowing(arylstFollowing);

                                JSONObject jsnObjRsps = null;
                                try {
                                    jsnObjRsps = new JSONObject(response);
                                    if (jsnObjRsps.has("pagination")) {
                                        JSONObject jsnObjPagination = jsnObjRsps.getJSONObject("pagination");
                                        callNextUrlToFetchFollowing(jsnObjPagination);
                                    } else {
                                        getNotFollowingMeBackData();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(String error) {
                                Utility.showToast(NotFollowingMeBackActivity.this, error);
                            }
                        });
            } else {
                getNotFollowingMeBackData();
            }
        } else {
            Utility.showToast(NotFollowingMeBackActivity.this, "Could not authentication, need to log in again");
            Intent intent = new Intent(NotFollowingMeBackActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void callNextUrlToFetchFollowing(JSONObject jsnObjPagination) {
        InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
        try {
            if (jsnObjPagination.has("next_url")) {
                String strNxtUrl = jsnObjPagination.getString("next_url");
                request.createPaginationRequest(strNxtUrl, new InstagramRequest.InstagramRequestListener() {
                    @Override
                    public void onSuccess(String response) {

                        FollowingDao followingDao = new FollowingDao(NotFollowingMeBackActivity.this);
                        ArrayList<Object> arylstFollowing = followingDao.getFollowing(response);
                        followingDao.saveFollowing(arylstFollowing);

                        JSONObject jsnObjRsps = null;
                        try {
                            jsnObjRsps = new JSONObject(response);
                            if (jsnObjRsps.has("pagination")) {
                                JSONObject jsnObjPagination = jsnObjRsps.getJSONObject("pagination");
                                callNextUrlToFetchFollowing(jsnObjPagination);
                            } else {
                                getNotFollowingMeBackData();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            } else {
                getNotFollowingMeBackData();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getNotFollowingMeBackData() {
        FollowersDao followersDao = new FollowersDao(NotFollowingMeBackActivity.this);
        ArrayList<Object> arylstNotFollowingBack = followersDao.getFollowersToWhomNotFollowing();
        setData(arylstNotFollowingBack);
        prgsbr_notfollowingmeback.setVisibility(View.GONE);
    }

    private void setData(ArrayList<Object> arylstNotFollowingBack) {
        if (arylstNotFollowingBack.size() > 0) {
            mAdapter.addFollowersing(arylstNotFollowingBack);
            mAdapter.notifyDataSetChanged();
            rcyclrvw_notfollowingmeback.setVisibility(View.VISIBLE);
            txtvw_no_notfollowingmeback.setVisibility(View.GONE);
        } else {
            rcyclrvw_notfollowingmeback.setVisibility(View.GONE);
            txtvw_no_notfollowingmeback.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
