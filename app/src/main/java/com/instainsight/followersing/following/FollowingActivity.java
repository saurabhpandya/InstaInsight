package com.instainsight.followersing.following;

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

public class FollowingActivity extends BaseActivity implements RelationshipStatusChangeListner {

    private String TAG = FollowingActivity.class.getSimpleName();
    private RecyclerView rcyclrvw_following;
    private TextView txtvw_no_following;
    private FollowersingAdap mAdapter;
    private ArrayList<Object> arylstFollowing;
    private ProgressBar prgsbr_following;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        initActionbar();
        setTitle(R.string.lbl_newfollowing);
        getIds();
        initRecyclerView();
        getFollowingData();
    }

    private void initActionbar() {
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.base));
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getIds() {
        rcyclrvw_following = (RecyclerView) findViewById(R.id.rcyclrvw_following);
        txtvw_no_following = (TextView) findViewById(R.id.txtvw_no_following);
        prgsbr_following = (ProgressBar) findViewById(R.id.prgsbr_following);
    }

    private void initRecyclerView() {
        arylstFollowing = new ArrayList<Object>();
        mAdapter = new FollowersingAdap(FollowingActivity.this, arylstFollowing, "Following", this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcyclrvw_following.setLayoutManager(mLayoutManager);
        rcyclrvw_following.setItemAnimator(new DefaultItemAnimator());
        rcyclrvw_following.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rcyclrvw_following.setAdapter(mAdapter);
    }

    private void getFollowingData() {
        if (mInstagramSession.isActive()) {
            if (isConnected()) {
                prgsbr_following.setVisibility(View.VISIBLE);
                InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
                request.createRequest("GET", Constants.WebFields.ENDPOINT_FOLLOWEDBY, new ArrayList<NameValuePair>(),
                        new InstagramRequest.InstagramRequestListener() {
                            @Override
                            public void onSuccess(String response) {
                                FollowingDao followingDao = new FollowingDao(FollowingActivity.this);
                                arylstFollowing = followingDao.getFollowing(response);
                                followingDao.saveFollowing(arylstFollowing);
//                                arylstFollowing = followingDao.getAllFollowing();
                                arylstFollowing = followingDao.getNewFollowing();
                                if (arylstFollowing.size() > 0) {
                                    rcyclrvw_following.setVisibility(View.VISIBLE);
                                    txtvw_no_following.setVisibility(View.GONE);
                                    mAdapter.addFollowersing(arylstFollowing);
                                    mAdapter.notifyDataSetChanged();
                                } else {
                                    rcyclrvw_following.setVisibility(View.GONE);
                                    txtvw_no_following.setVisibility(View.VISIBLE);
                                }
                                prgsbr_following.setVisibility(View.GONE);

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
                                prgsbr_following.setVisibility(View.GONE);
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
    public void onClickToChangeRelationStatus(final int position, String userId) {
        RestClient restClient = new RestClient(DAGGER_API_BASE_URL);
        IRelationshipStatus iRelationshipStatus = restClient.create(IRelationshipStatus.class);

        iRelationshipStatus.changeRelationshipStatus("unfollow", userId, mInstagramSession.getAccessToken())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ObjectResponseBean<RelationShipStatus>>() {
                    @Override
                    public void accept(ObjectResponseBean<RelationShipStatus> relationShipStatusObjectResponseBean) throws Exception {
                        arylstFollowing.remove(position);
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
