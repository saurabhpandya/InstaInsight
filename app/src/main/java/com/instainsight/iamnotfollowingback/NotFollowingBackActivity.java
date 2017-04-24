package com.instainsight.iamnotfollowingback;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.instainsight.BaseActivity;
import com.instainsight.InstaInsightApp;
import com.instainsight.R;
import com.instainsight.RelationshipStatusChangeListner;
import com.instainsight.Utils.DividerItemDecoration;
import com.instainsight.Utils.Utility;
import com.instainsight.databinding.ActivityNotFollowingBackBinding;
import com.instainsight.followersing.followers.bean.FollowerBean;
import com.instainsight.iamnotfollowingback.viewmodel.NotFollowingBackViewModel;
import com.instainsight.login.LoginActivity;
import com.instainsight.models.ObjectResponseBean;
import com.instainsight.models.RelationShipStatus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NotFollowingBackActivity extends BaseActivity implements RelationshipStatusChangeListner {

    @Inject
    NotFollowingBackViewModel notFollowingBackViewModel;
    private String TAG = NotFollowingBackActivity.class.getSimpleName();
    private ActivityNotFollowingBackBinding notFollowingBackBinding;
    private NotFollowingBackAdap mAdapter;
    private ArrayList<FollowerBean> arylstUnFollowers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((InstaInsightApp) getApplication()).getComponent().inject(NotFollowingBackActivity.this);
        super.onCreate(savedInstanceState);
        notFollowingBackBinding = DataBindingUtil.setContentView(this, R.layout.activity_not_following_back);
        notFollowingBackBinding.setNotFollowingBackViewModel(notFollowingBackViewModel);
        setTitle(R.string.lbl_blockedfollowers);
        initActionbar();

        initRecyclerView();
        getNotFollowingBack();
    }

    private void initActionbar() {
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRecyclerView() {
        arylstUnFollowers = new ArrayList<>();
        mAdapter = new NotFollowingBackAdap(NotFollowingBackActivity.this, arylstUnFollowers, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        notFollowingBackBinding.rcyclrvwNotfollowingback.setLayoutManager(mLayoutManager);
        notFollowingBackBinding.rcyclrvwNotfollowingback.setItemAnimator(new DefaultItemAnimator());
        notFollowingBackBinding.rcyclrvwNotfollowingback.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        notFollowingBackBinding.rcyclrvwNotfollowingback.setAdapter(mAdapter);
    }

    private void setData(ArrayList<FollowerBean> arylstUnfollowers) {
        arylstUnFollowers = arylstUnfollowers;
        if (arylstUnFollowers.size() > 0) {
            mAdapter.addFollowersing(arylstUnFollowers);
            mAdapter.notifyDataSetChanged();
            notFollowingBackBinding.rcyclrvwNotfollowingback.setVisibility(View.VISIBLE);
            notFollowingBackBinding.txtvwNoNotfollowingback.setVisibility(View.GONE);
            getRelationShipStatus(arylstUnFollowers);
        } else {
            notFollowingBackBinding.rcyclrvwNotfollowingback.setVisibility(View.GONE);
            notFollowingBackBinding.txtvwNoNotfollowingback.setVisibility(View.VISIBLE);
        }
    }

    private void getNotFollowingBack() {
        if (mInstagramSession.isActive()) {
            if (isConnected()) {

                notFollowingBackViewModel.getNotFollowingBack()
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                notFollowingBackBinding.prgsbrNotfollowingback.setVisibility(View.VISIBLE);
                            }
                        })
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                notFollowingBackBinding.prgsbrNotfollowingback.setVisibility(View.GONE);
                            }
                        })
                        .subscribe(new Consumer<ArrayList<FollowerBean>>() {
                            @Override
                            public void accept(ArrayList<FollowerBean> arylstUnFollowers) throws Exception {
                                setData(arylstUnFollowers);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                            }
                        });


//                InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
//                request.createRequest("GET", Constants.WebFields.ENDPOINT_FOLLOWEDBY, new ArrayList<NameValuePair>(),
//                        new InstagramRequest.InstagramRequestListener() {
//                            @Override
//                            public void onSuccess(String response) {
//                                FollowersDao followersDao = new FollowersDao(UnFollowersActivity.this);
//                                ArrayList<FollowerBean> arylstFollowers = followersDao.getFollowers(response);
//                                followersDao.saveFollowers(arylstFollowers);
//                                JSONObject jsnObjRsps = null;
//                                try {
//                                    jsnObjRsps = new JSONObject(response);
//                                    if (jsnObjRsps.has("pagination")) {
//                                        JSONObject jsnObjPagination = jsnObjRsps.getJSONObject("pagination");
//                                        callNextUrlToFetchFollowers(jsnObjPagination);
//                                    } else {
//                                        getFollowingData();
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//
//                            @Override
//                            public void onError(String error) {
//                                Utility.showToast(UnFollowersActivity.this, error);
//                            }
//                        }
//
//                );
            } else {
//                getNotFollowingBackData();
            }
        } else {
            Utility.showToast(NotFollowingBackActivity.this, "Could not authentication, need to log in again");
            Intent intent = new Intent(NotFollowingBackActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
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

    private void getRelationShipStatus(ArrayList<FollowerBean> arylstFollowedBy) {
        notFollowingBackViewModel.getRelationShipStatus(arylstFollowedBy)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<FollowerBean>>() {
                    @Override
                    public void accept(List<FollowerBean> followedByList) throws Exception {
                        Log.d(TAG, "getRelationShipStatus:followedByList.size():" + followedByList.size());
                        if (followedByList.size() > 0) {
                            arylstUnFollowers = new ArrayList<>(followedByList);
                            mAdapter.addFollowersing(arylstUnFollowers);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        Utility.showToast(getApplicationContext(), getResources().getString(R.string.tst_api_failure));
                    }
                });
    }

    @Override
    public void onClickToChangeRelationStatus(final int position, final String userId) {
        if (arylstUnFollowers.size() > 0) {

            final FollowerBean followerBean = arylstUnFollowers.get(position);

            String action = "";
            if (followerBean.getRelationShipStatus().getOutgoing_status().equalsIgnoreCase("none")) {
                action = "follow";
            } else if (followerBean.getRelationShipStatus().getOutgoing_status().equalsIgnoreCase("requested")) {
                action = "unfollow";
            } else if (followerBean.getRelationShipStatus().getOutgoing_status().equalsIgnoreCase("follows")) {
                action = "unfollow";
            }
            notFollowingBackViewModel.changeRelationshipStatus(action, followerBean.getId(),
                    mInstagramSession.getAccessToken())
                    .subscribe(new Consumer<ObjectResponseBean<RelationShipStatus>>() {
                        @Override
                        public void accept(ObjectResponseBean<RelationShipStatus> relationShipStatusBean) throws Exception {
                            RelationShipStatus relationShipStatus = relationShipStatusBean.getData();

                            followerBean.setRelationShipStatus(relationShipStatus);
                            arylstUnFollowers.set(position, followerBean);
                            mAdapter.notifyDataSetChanged();

                            if (arylstUnFollowers.size() == 0) {
                                notFollowingBackBinding.rcyclrvwNotfollowingback.setVisibility(View.GONE);
                                notFollowingBackBinding.prgsbrNotfollowingback.setVisibility(View.GONE);
                                notFollowingBackBinding.txtvwNoNotfollowingback.setVisibility(View.VISIBLE);
                            }

//                            if (relationShipStatus.getOutgoing_status().equalsIgnoreCase("none")) {
////                                arylstUnFollowers.remove(position);
////                                mAdapter.removeFollowersing(position);
//
//                                if (arylstUnFollowers.size() == 0) {
//                                    notFollowingBackBinding.rcyclrvwNotfollowingback.setVisibility(View.GONE);
//                                    notFollowingBackBinding.prgsbrNotfollowingback.setVisibility(View.GONE);
//                                    notFollowingBackBinding.txtvwNoNotfollowingback.setVisibility(View.VISIBLE);
//                                }
//                            } else {
//                                followerBean.setRelationShipStatus(relationShipStatus);
//                                arylstUnFollowers.set(position, followerBean);
//                                mAdapter.notifyDataSetChanged();
//                            }

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                        }
                    });
        }
    }
}
