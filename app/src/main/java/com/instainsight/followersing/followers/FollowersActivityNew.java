package com.instainsight.followersing.followers;

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
import com.instainsight.databinding.ActivityFollowersBinding;
import com.instainsight.followersing.followers.bean.FollowerBean;
import com.instainsight.followersing.followers.dao.FollowersDao;
import com.instainsight.followersing.followers.viewmodel.FollowersViewModel;
import com.instainsight.login.LoginActivity;
import com.instainsight.models.ObjectResponseBean;
import com.instainsight.models.RelationShipStatus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class FollowersActivityNew extends BaseActivity implements RelationshipStatusChangeListner {

    @Inject
    FollowersViewModel followersViewModel;

    private String TAG = FollowersActivityNew.class.getSimpleName();
    private ActivityFollowersBinding followersBinding;

    private FollowersAdap mAdapter;
    private ArrayList<FollowerBean> arylstFollowers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((InstaInsightApp) getApplication()).getComponent().inject(FollowersActivityNew.this);
        super.onCreate(savedInstanceState);
        followersBinding = DataBindingUtil.setContentView(this, R.layout.activity_followers);
        followersBinding.setFollowerViewModel(followersViewModel);
        setTitle(R.string.lbl_newfollowers);
        initActionbar();
        initRecyclerView();
        getFollowersData();
    }

    private void initActionbar() {
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRecyclerView() {
        arylstFollowers = new ArrayList<>();
        mAdapter = new FollowersAdap(FollowersActivityNew.this, arylstFollowers, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        followersBinding.rcyclrvwFollower.setLayoutManager(mLayoutManager);
        followersBinding.rcyclrvwFollower.setItemAnimator(new DefaultItemAnimator());
        followersBinding.rcyclrvwFollower.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        followersBinding.rcyclrvwFollower.setAdapter(mAdapter);
    }

    private void getFollowersData() {
        if (mInstagramSession.isActive()) {
            if (isConnected()) {

                followersViewModel.getFollowedBy(mInstagramSession.getAccessToken())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                followersBinding.prgsbrFollowers.setVisibility(View.VISIBLE);
                            }
                        })
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                followersBinding.prgsbrFollowers.setVisibility(View.GONE);
                            }
                        })
                        .flatMap(new Function<ArrayList<FollowerBean>, Observable<ArrayList<FollowerBean>>>() {
                            @Override
                            public Observable<ArrayList<FollowerBean>> apply(ArrayList<FollowerBean> aryLstfollowedBy) throws Exception {
                                FollowedByDBQueries followedByDBQueries = new FollowedByDBQueries(getApplicationContext());
                                followedByDBQueries.getFollowedByNotInRelation(aryLstfollowedBy);
                                return followedByDBQueries.saveFollowedBy(aryLstfollowedBy);
                            }
                        })
                        .subscribe(new Consumer<ArrayList<FollowerBean>>() {
                            @Override
                            public void accept(ArrayList<FollowerBean> aryLstfollowedBy) throws Exception {
                                arylstFollowers = aryLstfollowedBy;
                                if (arylstFollowers.size() > 0) {
                                    followersBinding.txtvwNoFollowers.setVisibility(View.GONE);
                                    followersBinding.rcyclrvwFollower.setVisibility(View.VISIBLE);
                                    mAdapter.addFollowersing(arylstFollowers);
                                    mAdapter.notifyDataSetChanged();
                                    getRelationShipStatus(arylstFollowers);
                                } else {
                                    followersBinding.rcyclrvwFollower.setVisibility(View.GONE);
                                    followersBinding.txtvwNoFollowers.setVisibility(View.VISIBLE);
                                }
//                                FollowersDao followersDao = new FollowersDao(FollowersActivityNew.this);
//                                followersDao.saveFollowers(followerBeen);
//                                arylstFollowers = followersDao.getNewFollowers();
//
//                                if (arylstFollowers.size() > 0) {
//                                    followersBinding.txtvwNoFollowers.setVisibility(View.GONE);
//                                    followersBinding.rcyclrvwFollower.setVisibility(View.VISIBLE);
//                                    mAdapter.addFollowersing(arylstFollowers);
//                                    mAdapter.notifyDataSetChanged();
//                                    getRelationShipStatus(arylstFollowers);
//                                } else {
//                                    followersBinding.rcyclrvwFollower.setVisibility(View.GONE);
//                                    followersBinding.txtvwNoFollowers.setVisibility(View.VISIBLE);
//                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                            }
                        });
            } else {
                FollowersDao followersDao = new FollowersDao(FollowersActivityNew.this);
                arylstFollowers = followersDao.getAllFollowers();
                mAdapter.addFollowersing(arylstFollowers);
                mAdapter.notifyDataSetChanged();
            }
        } else {
            Utility.showToast(FollowersActivityNew.this, "Could not authentication, need to log in again");
            Intent intent = new Intent(FollowersActivityNew.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void getRelationShipStatus(ArrayList<FollowerBean> arylstFollowedBy) {
        followersViewModel.getRelationShipStatus(arylstFollowedBy)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<FollowerBean>>() {
                    @Override
                    public void accept(List<FollowerBean> followedByList) throws Exception {
                        Log.d(TAG, "getRelationShipStatus:followedByList.size():" + followedByList.size());
                        if (followedByList.size() > 0) {
                            arylstFollowers = new ArrayList<>(followedByList);
                            mAdapter.addFollowersing(arylstFollowers);
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
        if (arylstFollowers.size() > 0) {

            final FollowerBean followerBean = arylstFollowers.get(position);

            String action = "";
            if (followerBean.getRelationShipStatus().getOutgoing_status().equalsIgnoreCase("none")) {
                action = "follow";
            } else if (followerBean.getRelationShipStatus().getOutgoing_status().equalsIgnoreCase("requested")) {
                action = "unfollow";
            } else if (followerBean.getRelationShipStatus().getOutgoing_status().equalsIgnoreCase("follows")) {
                action = "unfollow";
            }
            followersViewModel.changeRelationshipStatus(action, followerBean.getId(),
                    mInstagramSession.getAccessToken())
                    .subscribe(new Consumer<ObjectResponseBean<RelationShipStatus>>() {
                        @Override
                        public void accept(ObjectResponseBean<RelationShipStatus> relationShipStatusBean) throws Exception {
                            RelationShipStatus relationShipStatus = relationShipStatusBean.getData();

                            followerBean.setRelationShipStatus(relationShipStatus);
                            arylstFollowers.set(position, followerBean);
                            mAdapter.notifyDataSetChanged();

                            if (arylstFollowers.size() == 0) {
                                followersBinding.rcyclrvwFollower.setVisibility(View.GONE);
                                followersBinding.prgsbrFollowers.setVisibility(View.GONE);
                                followersBinding.txtvwNoFollowers.setVisibility(View.VISIBLE);
                            }

//                            if (relationShipStatus.getOutgoing_status().equalsIgnoreCase("none")) {
////                                arylstFollowers.remove(position);
////                                mAdapter.removeFollowersing(position);
//
//                                if (arylstFollowers.size() == 0) {
//                                    followersBinding.rcyclrvwFollower.setVisibility(View.GONE);
//                                    followersBinding.prgsbrFollowers.setVisibility(View.GONE);
//                                    followersBinding.txtvwNoFollowers.setVisibility(View.VISIBLE);
//                                }
//                            } else {
//                                followerBean.setRelationShipStatus(relationShipStatus);
//                                arylstFollowers.set(position, followerBean);
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
