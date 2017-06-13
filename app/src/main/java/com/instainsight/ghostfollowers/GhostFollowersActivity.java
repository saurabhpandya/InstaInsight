package com.instainsight.ghostfollowers;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.instainsight.InstaInsightApp;
import com.instainsight.R;
import com.instainsight.RelationshipStatusChangeListner;
import com.instainsight.Utils.DividerItemDecoration;
import com.instainsight.Utils.Utility;
import com.instainsight.ViewModelActivity;
import com.instainsight.databinding.ActivityGhostFollowersBinding;
import com.instainsight.followersing.followers.bean.FollowerBean;
import com.instainsight.ghostfollowers.model.CommentsBean;
import com.instainsight.ghostfollowers.model.LikesBean;
import com.instainsight.ghostfollowers.viewmodel.GhostFollowersViewModel;
import com.instainsight.login.LoginActivity;
import com.instainsight.models.ObjectResponseBean;
import com.instainsight.models.RelationShipStatus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GhostFollowersActivity extends ViewModelActivity implements RelationshipStatusChangeListner {

    @Inject
    GhostFollowersViewModel ghostFollowersViewModel;
    ActivityGhostFollowersBinding activityGhostFollowersBinding;
    GhostFollowersAdap mAdapter;

    private ArrayList<LikesBean> arylstLikes = new ArrayList<>();
    private ArrayList<CommentsBean> arylstComments = new ArrayList<>();
    private ArrayList<FollowerBean> arylstFollowers = new ArrayList<>();
    private String TAG = GhostFollowersActivity.class.getSimpleName();
    private ArrayList<String> likesUsers;
    private ArrayList<String> commentsUsers;
    private ArrayList<String> arylstLikesCommentsUsers = new ArrayList<>();
    private ArrayList<FollowerBean> arylstLikesCommentsFollowers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((InstaInsightApp) getApplication()).getComponent().inject(GhostFollowersActivity.this);
        super.onCreate(savedInstanceState);
        activityGhostFollowersBinding = DataBindingUtil.setContentView(GhostFollowersActivity.this,
                R.layout.activity_ghost_followers);
        activityGhostFollowersBinding.setGhostFollowers(ghostFollowersViewModel);
        setTitle(R.string.lbl_ghost_followers);
        initActionbar();
        initRecyclerView();
        activityGhostFollowersBinding.rcyclrvwGhostfollowers.setVisibility(View.GONE);
        activityGhostFollowersBinding.prgsbrGhostfollowers.setVisibility(View.VISIBLE);
        getGhostFollowers();
    }

    private void getGhostFollowers() {
        if (mInstagramSession.isActive()) {
            if (isConnected()) {
                ghostFollowersViewModel.getFollowedByForGhostFollowers()
                        .subscribe(new Consumer<List<ArrayList<FollowerBean>>>() {
                            @Override
                            public void accept(List<ArrayList<FollowerBean>> arrayLists) throws Exception {

//                                arylstFollowers = new ArrayList<FollowerBean>();
//                                arylstFollowers = arrayLists;

//                                GhostFollowerDBQueries ghostFollowerDBQueries = new GhostFollowerDBQueries(GhostFollowersActivity.this);
//                                arylstFollowers = ghostFollowerDBQueries.getGhostFollowers();
//                                Log.d(TAG, "arylstFollowers size:" + arylstFollowers.size());

                                GhostFollowerDBQueries ghostFollowerDBQueries = new GhostFollowerDBQueries(GhostFollowersActivity.this);
                                arylstFollowers = ghostFollowerDBQueries.getGhostFollowers();

//                                Log.d(TAG, "getGhostFollowers size:" + arrayLists.get(0).size());
//                                arylstFollowers = new ArrayList<FollowerBean>();
//                                for (int i = 0; i < arrayLists.size(); i++) {
//                                    arylstFollowers.addAll(arrayLists.get(i));
//                                }
//
//                                arylstFollowers = clearListFromDuplicateFirstName(arylstFollowers);

                                if (arylstFollowers.size() > 0) {
                                    activityGhostFollowersBinding.txtvwNoGhostfollowers.setVisibility(View.GONE);
                                    activityGhostFollowersBinding.rcyclrvwGhostfollowers.setVisibility(View.VISIBLE);
                                    mAdapter.addGhostFollowers(arylstFollowers);
                                    mAdapter.notifyDataSetChanged();
                                    getRelationShipStatus(arylstFollowers);
                                } else {
                                    activityGhostFollowersBinding.txtvwNoGhostfollowers.setVisibility(View.VISIBLE);
                                    activityGhostFollowersBinding.rcyclrvwGhostfollowers.setVisibility(View.GONE);
                                }
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
            Utility.showToast(this, "Could not authentication, need to log in again");
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void createViewModel() {
        mViewModel = ghostFollowersViewModel;
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
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void initActionbar() {
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRecyclerView() {
        arylstFollowers = new ArrayList<FollowerBean>();
        mAdapter = new GhostFollowersAdap(GhostFollowersActivity.this, arylstFollowers, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        activityGhostFollowersBinding.rcyclrvwGhostfollowers.setLayoutManager(mLayoutManager);
        activityGhostFollowersBinding.rcyclrvwGhostfollowers.setItemAnimator(new DefaultItemAnimator());
        activityGhostFollowersBinding.rcyclrvwGhostfollowers
                .addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        activityGhostFollowersBinding.rcyclrvwGhostfollowers.setAdapter(mAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GhostFollowersEvent ghostFollowersEvent) {

        if (ghostFollowersEvent.getArylstFollowers() != null) {
            arylstFollowers.addAll(ghostFollowersEvent.getArylstFollowers());
            Log.d(TAG, "arylstFollowers:" + arylstFollowers.size());
        } else {

            if (ghostFollowersEvent.getArylstLikesCommentsFollowers() != null) {
                arylstLikesCommentsFollowers.addAll(ghostFollowersEvent.getArylstLikesCommentsFollowers());
                Log.d(TAG, "arylstLikesCommentsFollowers:" + arylstLikesCommentsFollowers.size());
            }

            arylstLikesCommentsFollowers = clearListFromDuplicateFirstName(arylstLikesCommentsFollowers);

            ArrayList<FollowerBean> arylstGhostFollowerFiltered = new ArrayList<FollowerBean>();
            ArrayList<String> arylstGhostFollowerId = new ArrayList<String>();
            for (int i = 0; i < arylstLikesCommentsFollowers.size(); i++) {
                arylstGhostFollowerId.add(arylstLikesCommentsFollowers.get(i).getId());
            }

            Map<String, Integer> map = new HashMap<String, Integer>();

            for (String temp : arylstGhostFollowerId) {
                Integer count = map.get(temp);
                map.put(temp, (count == null) ? 1 : count + 1);
            }

            Map<String, Integer> treeMap = new TreeMap<String, Integer>(map);

            for (Map.Entry<String, Integer> entry : treeMap.entrySet()) {
                Log.d(TAG, "Key : " + entry.getKey() + " Value : "
                        + entry.getValue());

                for (FollowerBean followerBean : arylstLikesCommentsFollowers) {
                    if (followerBean.getId().equalsIgnoreCase(entry.getKey().toString())
                            && !mInstagramSession.getUser().getUserBean().getId().equalsIgnoreCase(followerBean.getId())) {
                        arylstGhostFollowerFiltered.add(followerBean);
                    }

                }
            }

            addUserToRecyclerView(arylstFollowers, arylstGhostFollowerFiltered);
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(GhostFollowersEvent ghostFollowersEvent) {
//        if (ghostFollowersEvent.getArylstComments() != null) {
//            commentsUsers = new ArrayList<>();
//            arylstComments.addAll(ghostFollowersEvent.getArylstComments());
//            Log.d(TAG, "arylstComments:" + arylstComments.size());
//            for (CommentsBean commentsBean : arylstComments) {
//                commentsUsers.add(commentsBean.getFrom().getId());
//                arylstLikesCommentsUsers.addAll(commentsUsers);
//            }
//        }
//        if (ghostFollowersEvent.getArylstLikes() != null) {
//            likesUsers = new ArrayList<>();
//            arylstLikes.addAll(ghostFollowersEvent.getArylstLikes());
//            Log.d(TAG, "arylstLikes:" + arylstLikes.size());
//            for (LikesBean likesBean : arylstLikes) {
//                likesUsers.add(likesBean.getId());
//                arylstLikesCommentsUsers.addAll(likesUsers);
//            }
//        }
//
//        if (ghostFollowersEvent.getArylstFollowers() != null) {
//            arylstFollowers.addAll(ghostFollowersEvent.getArylstFollowers());
//            Log.d(TAG, "arylstFollowers:" + arylstFollowers.size());
//        }
//
//        Set<String> uniqueLikesCommentsUsers = new HashSet<>(arylstLikesCommentsUsers);
//        arylstLikesCommentsUsers = new ArrayList<>(uniqueLikesCommentsUsers);
//        Log.d(TAG, "arylstLikesCommentsUsers:" + arylstLikesCommentsUsers.toString());
//
//
//        for (FollowerBean followerBean : arylstFollowers) {
//            for (String unique : arylstLikesCommentsUsers) {
//                if (!unique.equalsIgnoreCase(followerBean.getId())) {
//                    Log.d(TAG, "addUserToRecyclerView:followerBean.getId():" + followerBean.getId());
//                    Log.d(TAG, "addUserToRecyclerView:unique:" + unique);
//                    addUserToRecyclerView(followerBean);
//                } else {
//                    continue;
//                }
//            }
//        }
//    }

    private ArrayList<FollowerBean> clearListFromDuplicateFirstName(ArrayList<FollowerBean> list1) {

        Map<String, FollowerBean> cleanMap = new LinkedHashMap<String, FollowerBean>();
        for (int i = 0; i < list1.size(); i++) {
            cleanMap.put(list1.get(i).getId(), list1.get(i));
        }
        ArrayList<FollowerBean> list = new ArrayList<FollowerBean>(cleanMap.values());
        return list;
    }

    private void addUserToRecyclerView(ArrayList<FollowerBean> arylstFollowers, ArrayList<FollowerBean> arylstLikesCommentsFollowers) {
        ArrayList<FollowerBean> tempFollowers = new ArrayList<>();

        if (arylstLikesCommentsFollowers.size() > 0) {
            for (FollowerBean followerBean : arylstFollowers) {
                for (int i = 0; i < arylstLikesCommentsFollowers.size(); i++) {
                    if (!arylstLikesCommentsFollowers.get(i).getId().equalsIgnoreCase(followerBean.getId())) {
                        FollowerBean likeCommentFollowerNew = arylstLikesCommentsFollowers.get(i);
                        likeCommentFollowerNew.setProfilePic(followerBean.getProfilePic());
                        arylstLikesCommentsFollowers.set(i, likeCommentFollowerNew);
                    }
                }
            }
            arylstLikesCommentsFollowers.removeAll(new HashSet<FollowerBean>(arylstFollowers));
            tempFollowers = arylstLikesCommentsFollowers;
        } else {
            tempFollowers.addAll(arylstFollowers);
        }

        if (tempFollowers.size() > 0) {
//            for (FollowerBean followerBean : tempFollowers) {
//                Log.d(TAG, "addUserToRecyclerView:" + followerBean.getId());
//            }
            activityGhostFollowersBinding.rcyclrvwGhostfollowers.setVisibility(View.VISIBLE);
            activityGhostFollowersBinding.txtvwNoGhostfollowers.setVisibility(View.GONE);
            activityGhostFollowersBinding.prgsbrGhostfollowers.setVisibility(View.GONE);
            mAdapter.addGhostFollowers(tempFollowers);
            mAdapter.notifyDataSetChanged();
            getRelationShipStatus(tempFollowers);
        } else {
            activityGhostFollowersBinding.rcyclrvwGhostfollowers.setVisibility(View.GONE);
            activityGhostFollowersBinding.prgsbrGhostfollowers.setVisibility(View.GONE);
            activityGhostFollowersBinding.txtvwNoGhostfollowers.setVisibility(View.VISIBLE);
        }

    }

    private void getRelationShipStatus(ArrayList<FollowerBean> arylstGhostFollowers) {
        ghostFollowersViewModel.getRelationShipStatus(arylstGhostFollowers)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        activityGhostFollowersBinding.prgsbrGhostfollowers.setVisibility(View.VISIBLE);
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        activityGhostFollowersBinding.prgsbrGhostfollowers.setVisibility(View.GONE);
                    }
                })
                .subscribe(new Consumer<List<FollowerBean>>() {
                    @Override
                    public void accept(List<FollowerBean> ghostFollowerBeanList) throws Exception {
                        Log.d(TAG, "getRelationShipStatus:ghostFollowerBeanList.size():" + ghostFollowerBeanList.size());
                        if (ghostFollowerBeanList.size() > 0) {
                            ArrayList<FollowerBean> arylstGhostFollowers = new ArrayList<FollowerBean>();
                            for (int i = 0; i < ghostFollowerBeanList.size(); i++) {
                                arylstGhostFollowers.add(ghostFollowerBeanList.get(i));
                                mAdapter.addGhostFollowers(arylstGhostFollowers);
                            }
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
        Log.d(TAG, "onClickToChangeRelationStatus::position:" + position + " & userId:" + userId);

        if (arylstFollowers.size() > 0) {

            final FollowerBean ghostFollowersBean = arylstFollowers.get(position);

            String action = "";
            if (ghostFollowersBean.getRelationShipStatus().getOutgoing_status().equalsIgnoreCase("none")) {
                action = "follow";
            } else if (ghostFollowersBean.getRelationShipStatus().getOutgoing_status().equalsIgnoreCase("requested")) {
                action = "unfollow";
            } else if (ghostFollowersBean.getRelationShipStatus().getOutgoing_status().equalsIgnoreCase("follows")) {
                action = "unfollow";
            }
            ghostFollowersViewModel.changeRelationshipStatus(action, ghostFollowersBean.getId(),
                    mInstagramSession.getAccessToken())
                    .subscribe(new Consumer<ObjectResponseBean<RelationShipStatus>>() {
                        @Override
                        public void accept(ObjectResponseBean<RelationShipStatus> relationShipStatusBean) throws Exception {
                            RelationShipStatus relationShipStatus = relationShipStatusBean.getData();

                            ghostFollowersBean.setRelationShipStatus(relationShipStatus);
                            arylstFollowers.set(position, ghostFollowersBean);
                            mAdapter.notifyDataSetChanged();

                            if (arylstFollowers.size() == 0) {
                                activityGhostFollowersBinding.rcyclrvwGhostfollowers.setVisibility(View.GONE);
                                activityGhostFollowersBinding.prgsbrGhostfollowers.setVisibility(View.GONE);
                                activityGhostFollowersBinding.txtvwNoGhostfollowers.setVisibility(View.VISIBLE);
                            }

//                            if (relationShipStatus.getOutgoing_status().equalsIgnoreCase("none")) {
//                                arylstFollowers.remove(position);
////                                mAdapter.removeWhoViewedProfile(position);
////                                mAdapter.notifyDataSetChanged();
//                                if (arylstFollowers.size() == 0) {
//                                    activityGhostFollowersBinding.rcyclrvwGhostfollowers.setVisibility(View.GONE);
//                                    activityGhostFollowersBinding.prgsbrGhostfollowers.setVisibility(View.GONE);
//                                    activityGhostFollowersBinding.txtvwNoGhostfollowers.setVisibility(View.VISIBLE);
//                                }
//                            } else {
//                                ghostFollowersBean.setRelationShipStatus(relationShipStatus);
//                                arylstFollowers.set(position, ghostFollowersBean);
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
