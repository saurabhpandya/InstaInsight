package com.instainsight.followersing.followers;

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
import com.instainsight.constants.Constants.WebFields;
import com.instainsight.followersing.adapter.FollowersingAdap;
import com.instainsight.followersing.followers.dao.FollowersDao;
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

import static com.instainsight.instagram.util.Cons.DAGGER_API_BASE_ENDPOINT_URL;
import static com.instainsight.instagram.util.Cons.DAGGER_API_BASE_URL;

public class FollowersActivity extends BaseActivity implements RelationshipStatusChangeListner {

    private String TAG = FollowersActivity.class.getSimpleName();
    private RecyclerView rcyclrvw_follower;
    private TextView txtvw_no_followers;
    private FollowersingAdap mAdapter;
    private ArrayList<Object> arylstFollowers;
    private ProgressBar prgsbr_followers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        setTitle(R.string.lbl_newfollowers);
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
        rcyclrvw_follower = (RecyclerView) findViewById(R.id.rcyclrvw_follower);
        txtvw_no_followers = (TextView) findViewById(R.id.txtvw_no_followers);
        prgsbr_followers = (ProgressBar) findViewById(R.id.prgsbr_followers);
    }

    private void initRecyclerView() {
        arylstFollowers = new ArrayList<Object>();
        mAdapter = new FollowersingAdap(FollowersActivity.this, arylstFollowers, "Follower", this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcyclrvw_follower.setLayoutManager(mLayoutManager);
        rcyclrvw_follower.setItemAnimator(new DefaultItemAnimator());
        rcyclrvw_follower.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rcyclrvw_follower.setAdapter(mAdapter);
    }

    private void getFollowersData() {
        if (mInstagramSession.isActive()) {
            if (isConnected()) {
                prgsbr_followers.setVisibility(View.VISIBLE);
                InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
                request.createRequest("GET", WebFields.ENDPOINT_FOLLOWEDBY, new ArrayList<NameValuePair>(),
                        new InstagramRequest.InstagramRequestListener() {
                            @Override
                            public void onSuccess(String response) {

                                FollowersDao followersDao = new FollowersDao(FollowersActivity.this);
                                arylstFollowers = followersDao.getFollowers(response);
                                followersDao.saveFollowers(arylstFollowers);
//                            Utility.showToast(getApplicationContext(), "Found " + arylstFollowers.size() + " followers.");
//                                arylstFollowers = followersDao.getAllFollowers();
                                arylstFollowers = followersDao.getNewFollowers();
                                if (arylstFollowers.size() > 0) {
                                    txtvw_no_followers.setVisibility(View.GONE);
                                    rcyclrvw_follower.setVisibility(View.VISIBLE);
                                    mAdapter.addFollowersing(arylstFollowers);
                                    mAdapter.notifyDataSetChanged();
                                } else {
                                    rcyclrvw_follower.setVisibility(View.GONE);
                                    txtvw_no_followers.setVisibility(View.VISIBLE);
                                }
                                prgsbr_followers.setVisibility(View.GONE);
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
                                prgsbr_followers.setVisibility(View.GONE);
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
    public void onClickToChangeRelationStatus(final int position) {
        RestClient restClient = new RestClient(DAGGER_API_BASE_URL + DAGGER_API_BASE_ENDPOINT_URL);
        IRelationshipStatus iRelationshipStatus = restClient.create(IRelationshipStatus.class);

        iRelationshipStatus.changeRelationshipStatus("unfollow", mInstagramSession.getAccessToken())
                .observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ObjectResponseBean<RelationShipStatus>>() {
                    @Override
                    public void accept(ObjectResponseBean<RelationShipStatus> relationShipStatusObjectResponseBean) throws Exception {
                        arylstFollowers.remove(position);
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
