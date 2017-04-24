package com.instainsight.unfollowers;

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
import com.instainsight.databinding.ActivityUnfollowersBinding;
import com.instainsight.followersing.followers.bean.FollowerBean;
import com.instainsight.login.LoginActivity;
import com.instainsight.models.ObjectResponseBean;
import com.instainsight.models.RelationShipStatus;
import com.instainsight.unfollowers.viewmodel.UnFollowersViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UnFollowersActivity extends BaseActivity implements RelationshipStatusChangeListner {

    @Inject
    UnFollowersViewModel unFollowersViewModel;
    private String TAG = UnFollowersActivity.class.getSimpleName();
    private ActivityUnfollowersBinding unfollowersBinding;
    private UnFollowersAdap mAdapter;
    private ArrayList<FollowerBean> arylstUnFollowers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((InstaInsightApp) getApplication()).getComponent().inject(UnFollowersActivity.this);
        super.onCreate(savedInstanceState);
        unfollowersBinding = DataBindingUtil.setContentView(this, R.layout.activity_unfollowers);
        unfollowersBinding.setUnFollowersViewModel(unFollowersViewModel);
        setTitle(R.string.lbl_blockedbyfollowing);
        initActionbar();

        initRecyclerView();
        getUnFollowersData();
    }

    private void initActionbar() {
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRecyclerView() {
        arylstUnFollowers = new ArrayList<>();
        mAdapter = new UnFollowersAdap(UnFollowersActivity.this, arylstUnFollowers, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        unfollowersBinding.rcyclrvwUnfollowers.setLayoutManager(mLayoutManager);
        unfollowersBinding.rcyclrvwUnfollowers.setItemAnimator(new DefaultItemAnimator());
        unfollowersBinding.rcyclrvwUnfollowers.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        unfollowersBinding.rcyclrvwUnfollowers.setAdapter(mAdapter);
    }

    private void setData(ArrayList<FollowerBean> arylstUnfollowers) {
        arylstUnFollowers = arylstUnfollowers;
        if (arylstUnFollowers.size() > 0) {
            mAdapter.addFollowersing(arylstUnFollowers);
            mAdapter.notifyDataSetChanged();
            unfollowersBinding.rcyclrvwUnfollowers.setVisibility(View.VISIBLE);
            unfollowersBinding.txtvwNoUnfollowers.setVisibility(View.GONE);
            getRelationShipStatus(arylstUnFollowers);
        } else {
            unfollowersBinding.rcyclrvwUnfollowers.setVisibility(View.GONE);
            unfollowersBinding.txtvwNoUnfollowers.setVisibility(View.VISIBLE);
        }
    }

    private void getUnFollowersData() {
        if (mInstagramSession.isActive()) {
            if (isConnected()) {

                unFollowersViewModel.getUnFollowers()
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                unfollowersBinding.prgsbrUnfollowers.setVisibility(View.VISIBLE);
                            }
                        })
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                unfollowersBinding.prgsbrUnfollowers.setVisibility(View.GONE);
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

            } else {

            }
        } else {
            Utility.showToast(UnFollowersActivity.this, "Could not authentication, need to log in again");
            Intent intent = new Intent(UnFollowersActivity.this, LoginActivity.class);
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
        unFollowersViewModel.getRelationShipStatus(arylstFollowedBy)
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
            unFollowersViewModel.changeRelationshipStatus(action, followerBean.getId(),
                    mInstagramSession.getAccessToken())
                    .subscribe(new Consumer<ObjectResponseBean<RelationShipStatus>>() {
                        @Override
                        public void accept(ObjectResponseBean<RelationShipStatus> relationShipStatusBean) throws Exception {
                            RelationShipStatus relationShipStatus = relationShipStatusBean.getData();

                            followerBean.setRelationShipStatus(relationShipStatus);
                            arylstUnFollowers.set(position, followerBean);
                            mAdapter.notifyDataSetChanged();

                            if (arylstUnFollowers.size() == 0) {
                                unfollowersBinding.rcyclrvwUnfollowers.setVisibility(View.GONE);
                                unfollowersBinding.prgsbrUnfollowers.setVisibility(View.GONE);
                                unfollowersBinding.txtvwNoUnfollowers.setVisibility(View.VISIBLE);
                            }

//                            if (relationShipStatus.getOutgoing_status().equalsIgnoreCase("none")) {
////                                arylstUnFollowers.remove(position);
////                                mAdapter.removeFollowersing(position);
//
//                                if (arylstUnFollowers.size() == 0) {
//                                    unfollowersBinding.rcyclrvwUnfollowers.setVisibility(View.GONE);
//                                    unfollowersBinding.prgsbrUnfollowers.setVisibility(View.GONE);
//                                    unfollowersBinding.txtvwNoUnfollowers.setVisibility(View.VISIBLE);
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
