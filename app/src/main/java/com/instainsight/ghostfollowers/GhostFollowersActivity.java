package com.instainsight.ghostfollowers;

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
import com.instainsight.ViewModelActivity;
import com.instainsight.databinding.ActivityGhostFollowersBinding;
import com.instainsight.followersing.followers.bean.FollowerBean;
import com.instainsight.ghostfollowers.model.CommentsBean;
import com.instainsight.ghostfollowers.model.LikesBean;
import com.instainsight.ghostfollowers.viewmodel.GhostFollowersViewModel;
import com.instainsight.models.ObjectResponseBean;
import com.instainsight.models.RelationShipStatus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

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
        ghostFollowersViewModel.getGhostFollowers();
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
        }

        if (ghostFollowersEvent.getArylstLikesCommentsFollowers() != null) {
            arylstLikesCommentsFollowers.addAll(ghostFollowersEvent.getArylstLikesCommentsFollowers());
            Log.d(TAG, "arylstLikesCommentsFollowers:" + arylstLikesCommentsFollowers.size());
        }

        addUserToRecyclerView(arylstFollowers, arylstLikesCommentsFollowers);

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

    private void addUserToRecyclerView(ArrayList<FollowerBean> arylstFollowers, ArrayList<FollowerBean> arylstLikesCommentsFollowers) {
        ArrayList<FollowerBean> tempFollowers = new ArrayList<>();
        for (FollowerBean followerBean : arylstFollowers) {
            if (arylstLikesCommentsFollowers.size() > 0 && arylstLikesCommentsFollowers.contains(followerBean)) {
                tempFollowers.add(followerBean);
            }
        }

        if (tempFollowers.size() > 0) {
//            for (FollowerBean followerBean : tempFollowers) {
//                Log.d(TAG, "addUserToRecyclerView:" + followerBean.getId());
//            }
            activityGhostFollowersBinding.rcyclrvwGhostfollowers.setVisibility(View.VISIBLE);
            activityGhostFollowersBinding.txtvwNoGhostfollowers.setVisibility(View.GONE);
            mAdapter.addGhostFollowers(tempFollowers);
            mAdapter.notifyDataSetChanged();

        } else {
            activityGhostFollowersBinding.rcyclrvwGhostfollowers.setVisibility(View.GONE);
            activityGhostFollowersBinding.txtvwNoGhostfollowers.setVisibility(View.VISIBLE);
        }

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
