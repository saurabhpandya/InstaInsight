package com.instainsight.followersing.following;

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
import com.instainsight.databinding.ActivityFollowingBinding;
import com.instainsight.followersing.following.bean.FollowingBean;
import com.instainsight.followersing.following.dao.FollowingDao;
import com.instainsight.followersing.following.viewmodel.FollowingViewModel;
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

public class FollowingActivityNew extends BaseActivity implements RelationshipStatusChangeListner {

    @Inject
    FollowingViewModel followsViewModel;

    private String TAG = FollowingActivityNew.class.getSimpleName();
    private ActivityFollowingBinding followsBinding;

    private FollowingAdap mAdapter;
    private ArrayList<FollowingBean> arylstFollows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((InstaInsightApp) getApplication()).getComponent().inject(FollowingActivityNew.this);
        super.onCreate(savedInstanceState);
        followsBinding = DataBindingUtil.setContentView(this, R.layout.activity_following);
        followsBinding.setFollowsViewModel(followsViewModel);
        setTitle(R.string.lbl_newfollowing);
        initActionbar();
        initRecyclerView();
        getFollowersData();
    }

    private void initActionbar() {
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRecyclerView() {
        arylstFollows = new ArrayList<>();
        mAdapter = new FollowingAdap(FollowingActivityNew.this, arylstFollows, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        followsBinding.rcyclrvwFollowing.setLayoutManager(mLayoutManager);
        followsBinding.rcyclrvwFollowing.setItemAnimator(new DefaultItemAnimator());
        followsBinding.rcyclrvwFollowing.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        followsBinding.rcyclrvwFollowing.setAdapter(mAdapter);
    }

    private void getFollowersData() {
        if (mInstagramSession.isActive()) {
            if (isConnected()) {

                followsViewModel.getFollows(mInstagramSession.getAccessToken())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                followsBinding.prgsbrFollowing.setVisibility(View.VISIBLE);
                            }
                        })
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                followsBinding.prgsbrFollowing.setVisibility(View.GONE);
                            }
                        })
                        .flatMap(new Function<ArrayList<FollowingBean>, Observable<ArrayList<FollowingBean>>>() {
                            @Override
                            public Observable<ArrayList<FollowingBean>> apply(ArrayList<FollowingBean> followingBeen) throws Exception {
                                FollowsDBQueries followsDBQueries = new FollowsDBQueries(getApplicationContext());
                                return followsDBQueries.saveFollows(followingBeen);
                            }
                        })
                        .subscribe(new Consumer<ArrayList<FollowingBean>>() {
                            @Override
                            public void accept(ArrayList<FollowingBean> arylstfollows) throws Exception {

                                arylstFollows = arylstfollows;

                                if (arylstFollows.size() > 0) {
                                    followsBinding.txtvwNoFollowing.setVisibility(View.GONE);
                                    followsBinding.rcyclrvwFollowing.setVisibility(View.VISIBLE);
                                    mAdapter.addFollows(FollowingActivityNew.this.arylstFollows);
                                    mAdapter.notifyDataSetChanged();
                                    getRelationShipStatus(FollowingActivityNew.this.arylstFollows);
                                } else {
                                    followsBinding.rcyclrvwFollowing.setVisibility(View.GONE);
                                    followsBinding.txtvwNoFollowing.setVisibility(View.VISIBLE);
                                }

//                                FollowingDao followsDao = new FollowingDao(FollowingActivityNew.this);
//                                followsDao.saveFollowing(arylstfollows);
//                                FollowingActivityNew.this.arylstFollows = followsDao.getNewFollowing();
//
//                                if (FollowingActivityNew.this.arylstFollows.size() > 0) {
//                                    followsBinding.txtvwNoFollowing.setVisibility(View.GONE);
//                                    followsBinding.rcyclrvwFollowing.setVisibility(View.VISIBLE);
//                                    mAdapter.addFollows(FollowingActivityNew.this.arylstFollows);
//                                    mAdapter.notifyDataSetChanged();
//                                    getRelationShipStatus(FollowingActivityNew.this.arylstFollows);
//                                } else {
//                                    followsBinding.rcyclrvwFollowing.setVisibility(View.GONE);
//                                    followsBinding.txtvwNoFollowing.setVisibility(View.VISIBLE);
//                                }

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                            }
                        });
            } else {
                FollowingDao followsDao = new FollowingDao(FollowingActivityNew.this);
                arylstFollows = followsDao.getAllFollowing();
                mAdapter.addFollows(arylstFollows);
                mAdapter.notifyDataSetChanged();
            }
        } else {
            Utility.showToast(FollowingActivityNew.this, "Could not authentication, need to log in again");
            Intent intent = new Intent(FollowingActivityNew.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void getRelationShipStatus(ArrayList<FollowingBean> arylstFollows) {
        followsViewModel.getRelationShipStatus(arylstFollows)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<FollowingBean>>() {
                    @Override
                    public void accept(List<FollowingBean> followsList) throws Exception {
                        Log.d(TAG, "getRelationShipStatus:followsList.size():" + followsList.size());
                        if (followsList.size() > 0) {
                            FollowingActivityNew.this.arylstFollows = new ArrayList<>(followsList);
                            mAdapter.addFollows(FollowingActivityNew.this.arylstFollows);
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
        if (arylstFollows.size() > 0) {

            final FollowingBean followsBean = arylstFollows.get(position);

            String action = "";
            if (followsBean.getRelationShipStatus().getOutgoing_status().equalsIgnoreCase("none")) {
                action = "follow";
            } else if (followsBean.getRelationShipStatus().getOutgoing_status().equalsIgnoreCase("requested")) {
                action = "unfollow";
            } else if (followsBean.getRelationShipStatus().getOutgoing_status().equalsIgnoreCase("follows")) {
                action = "unfollow";
            }
            followsViewModel.changeRelationshipStatus(action, followsBean.getId(),
                    mInstagramSession.getAccessToken())
                    .subscribe(new Consumer<ObjectResponseBean<RelationShipStatus>>() {
                        @Override
                        public void accept(ObjectResponseBean<RelationShipStatus> relationShipStatusBean) throws Exception {
                            RelationShipStatus relationShipStatus = relationShipStatusBean.getData();

                            followsBean.setRelationShipStatus(relationShipStatus);
                            arylstFollows.set(position, followsBean);
                            mAdapter.notifyDataSetChanged();

                            if (arylstFollows.size() == 0) {
                                followsBinding.rcyclrvwFollowing.setVisibility(View.GONE);
                                followsBinding.prgsbrFollowing.setVisibility(View.GONE);
                                followsBinding.txtvwNoFollowing.setVisibility(View.VISIBLE);
                            }

//                            if (relationShipStatus.getOutgoing_status().equalsIgnoreCase("none")) {
////                                arylstFollows.remove(position);
////                                mAdapter.removeFollows(position);
//
//                                if (arylstFollows.size() == 0) {
//                                    followsBinding.rcyclrvwFollowing.setVisibility(View.GONE);
//                                    followsBinding.prgsbrFollowing.setVisibility(View.GONE);
//                                    followsBinding.txtvwNoFollowing.setVisibility(View.VISIBLE);
//                                }
//                            } else {
//                                followsBean.setRelationShipStatus(relationShipStatus);
//                                arylstFollows.set(position, followsBean);
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
