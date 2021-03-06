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
import com.instainsight.IRelationshipStatus;
import com.instainsight.R;
import com.instainsight.RelationshipStatusChangeListner;
import com.instainsight.Utils.DividerItemDecoration;
import com.instainsight.Utils.Utility;
import com.instainsight.constants.Constants;
import com.instainsight.followersing.adapter.FollowersingAdap;
import com.instainsight.followersing.followers.bean.FollowerBean;
import com.instainsight.followersing.followers.dao.FollowersDao;
import com.instainsight.followersing.following.bean.FollowingBean;
import com.instainsight.followersing.following.dao.FollowingDao;
import com.instainsight.instagram.InstagramRequest;
import com.instainsight.login.LoginActivity;
import com.instainsight.models.ObjectResponseBean;
import com.instainsight.models.RelationShipStatus;
import com.instainsight.networking.RestClient;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.instainsight.instagram.util.Cons.DAGGER_API_BASE_URL;

public class NotFollowingBackActivity extends BaseActivity implements RelationshipStatusChangeListner {

    private String TAG = NotFollowingBackActivity.class.getSimpleName();
    private RecyclerView rcyclrvw_notfollowingback;
    private TextView txtvw_no_notfollowingback;
    private FollowersingAdap mAdapter;
    private ArrayList<Object> arylstNotFollowingBack;
    private ProgressBar prgsbr_notfollowingback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unfollowers);
        setTitle(R.string.lbl_blockedbyfollowing);
        initActionbar();
        getIds();
        initRecyclerView();
        getFollowersData();
    }

    private void initActionbar() {
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getIds() {
        rcyclrvw_notfollowingback = (RecyclerView) findViewById(R.id.rcyclrvw_unfollowers);
        txtvw_no_notfollowingback = (TextView) findViewById(R.id.txtvw_no_unfollowers);
        prgsbr_notfollowingback = (ProgressBar) findViewById(R.id.prgsbr_unfollowers);
    }

    private void initRecyclerView() {
        arylstNotFollowingBack = new ArrayList<Object>();
        mAdapter = new FollowersingAdap(NotFollowingBackActivity.this, arylstNotFollowingBack, "NotFollowingBack", this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcyclrvw_notfollowingback.setLayoutManager(mLayoutManager);
        rcyclrvw_notfollowingback.setItemAnimator(new DefaultItemAnimator());
        rcyclrvw_notfollowingback.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rcyclrvw_notfollowingback.setAdapter(mAdapter);
    }

    private void getNotFollowingBackData() {
        FollowingDao followingDao = new FollowingDao(NotFollowingBackActivity.this);
        ArrayList<FollowingBean> arylstNotFollowingBack = followingDao.getFollowingsNotFollowingBack();
        setData(arylstNotFollowingBack);
        prgsbr_notfollowingback.setVisibility(View.GONE);
    }

    private void setData(ArrayList<FollowingBean> arylstNotFollowingBack) {
        if (arylstNotFollowingBack.size() > 0) {
            mAdapter.addFollowersing(arylstNotFollowingBack);
            mAdapter.notifyDataSetChanged();
            rcyclrvw_notfollowingback.setVisibility(View.VISIBLE);
            txtvw_no_notfollowingback.setVisibility(View.GONE);
        } else {
            rcyclrvw_notfollowingback.setVisibility(View.GONE);
            txtvw_no_notfollowingback.setVisibility(View.VISIBLE);
        }
    }

    private void getFollowersData() {
        if (mInstagramSession.isActive()) {
            if (isConnected()) {
                prgsbr_notfollowingback.setVisibility(View.VISIBLE);
                InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
                request.createRequest("GET", Constants.WebFields.ENDPOINT_FOLLOWEDBY, new ArrayList<NameValuePair>(),
                        new InstagramRequest.InstagramRequestListener() {
                            @Override
                            public void onSuccess(String response) {
                                FollowersDao followersDao = new FollowersDao(NotFollowingBackActivity.this);
                                ArrayList<FollowerBean> arylstFollowers = followersDao.getFollowers(response);
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
                                Utility.showToast(NotFollowingBackActivity.this, error);
                            }
                        }

                );
            } else {
                getNotFollowingBackData();
            }
        } else {
            Utility.showToast(NotFollowingBackActivity.this, "Could not authentication, need to log in again");
            Intent intent = new Intent(NotFollowingBackActivity.this, LoginActivity.class);
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

                        FollowersDao followersDao = new FollowersDao(NotFollowingBackActivity.this);
                        ArrayList<FollowerBean> arylstFollowers = followersDao.getFollowers(response);
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
                                FollowingDao followingDao = new FollowingDao(NotFollowingBackActivity.this);
                                ArrayList<FollowingBean> arylstFollowing = followingDao.getFollowing(response);
                                followingDao.saveFollowing(arylstFollowing);

                                JSONObject jsnObjRsps = null;
                                try {
                                    jsnObjRsps = new JSONObject(response);
                                    if (jsnObjRsps.has("pagination")) {
                                        JSONObject jsnObjPagination = jsnObjRsps.getJSONObject("pagination");
                                        callNextUrlToFetchFollowing(jsnObjPagination);
                                    } else {
                                        getNotFollowingBackData();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(String error) {
                                Utility.showToast(NotFollowingBackActivity.this, error);
                            }
                        });
            } else {
                getNotFollowingBackData();
            }
        } else {
            Utility.showToast(NotFollowingBackActivity.this, "Could not authentication, need to log in again");
            Intent intent = new Intent(NotFollowingBackActivity.this, LoginActivity.class);
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

                        FollowingDao followingDao = new FollowingDao(NotFollowingBackActivity.this);
                        ArrayList<FollowingBean> arylstFollowing = followingDao.getFollowing(response);
                        followingDao.saveFollowing(arylstFollowing);

                        JSONObject jsnObjRsps = null;
                        try {
                            jsnObjRsps = new JSONObject(response);
                            if (jsnObjRsps.has("pagination")) {
                                JSONObject jsnObjPagination = jsnObjRsps.getJSONObject("pagination");
                                callNextUrlToFetchFollowing(jsnObjPagination);
                            } else {
                                getNotFollowingBackData();
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
                getNotFollowingBackData();
            }

        } catch (JSONException e) {
            e.printStackTrace();
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

    @Override
    public void onClickToChangeRelationStatus(final int position, final String userId) {
        RestClient restClient = new RestClient(DAGGER_API_BASE_URL);
        IRelationshipStatus iRelationshipStatus = restClient.create(IRelationshipStatus.class);

        iRelationshipStatus.changeRelationshipStatus("unfollow", userId, mInstagramSession.getAccessToken())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ObjectResponseBean<RelationShipStatus>>() {
                    @Override
                    public void accept(ObjectResponseBean<RelationShipStatus> relationShipStatusObjectResponseBean) throws Exception {
                        arylstNotFollowingBack.remove(position);
                        mAdapter.removeFollowersing(position);
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }
}
