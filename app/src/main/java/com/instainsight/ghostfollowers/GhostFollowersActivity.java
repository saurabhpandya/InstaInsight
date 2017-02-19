package com.instainsight.ghostfollowers;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.instainsight.InstaInsightApp;
import com.instainsight.R;
import com.instainsight.Utils.DividerItemDecoration;
import com.instainsight.ViewModelActivity;
import com.instainsight.databinding.ActivityGhostFollowersBinding;
import com.instainsight.followersing.followers.bean.FollowerBean;
import com.instainsight.ghostfollowers.model.CommentsBean;
import com.instainsight.ghostfollowers.model.LikesBean;
import com.instainsight.ghostfollowers.viewmodel.GhostFollowersViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

public class GhostFollowersActivity extends ViewModelActivity {

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
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.base));
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRecyclerView() {
        ArrayList<FollowerBean> arylstFollowers = new ArrayList<FollowerBean>();
        mAdapter = new GhostFollowersAdap(GhostFollowersActivity.this, arylstFollowers);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        activityGhostFollowersBinding.rcyclrvwGhostfollowers.setLayoutManager(mLayoutManager);
        activityGhostFollowersBinding.rcyclrvwGhostfollowers.setItemAnimator(new DefaultItemAnimator());
        activityGhostFollowersBinding.rcyclrvwGhostfollowers
                .addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        activityGhostFollowersBinding.rcyclrvwGhostfollowers.setAdapter(mAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GhostFollowersEvent ghostFollowersEvent) {
        if (ghostFollowersEvent.getArylstComments() != null) {
            commentsUsers = new ArrayList<>();
            arylstComments.addAll(ghostFollowersEvent.getArylstComments());
            Log.d(TAG, "arylstComments:" + arylstComments.size());
            for (CommentsBean commentsBean : arylstComments) {
                commentsUsers.add(commentsBean.getFrom().getId());
                arylstLikesCommentsUsers.addAll(commentsUsers);
            }
        }
        if (ghostFollowersEvent.getArylstLikes() != null) {
            likesUsers = new ArrayList<>();
            arylstLikes.addAll(ghostFollowersEvent.getArylstLikes());
            Log.d(TAG, "arylstLikes:" + arylstLikes.size());
            for (LikesBean likesBean : arylstLikes) {
                likesUsers.add(likesBean.getId());
                arylstLikesCommentsUsers.addAll(likesUsers);
            }
        }

        if (ghostFollowersEvent.getArylstFollowers() != null) {
            arylstFollowers.addAll(ghostFollowersEvent.getArylstFollowers());
            Log.d(TAG, "arylstFollowers:" + arylstFollowers.size());
        }

        Set<String> uniqueLikesCommentsUsers = new HashSet<>(arylstLikesCommentsUsers);
        arylstLikesCommentsUsers = new ArrayList<>(uniqueLikesCommentsUsers);
        Log.d(TAG, "arylstLikesCommentsUsers:" + arylstLikesCommentsUsers.toString());


        for (FollowerBean followerBean : arylstFollowers) {
            for (String unique : arylstLikesCommentsUsers) {
                if (!unique.equalsIgnoreCase(followerBean.getId())) {
                    Log.d(TAG, "addUserToRecyclerView:followerBean.getId():" + followerBean.getId());
                    Log.d(TAG, "addUserToRecyclerView:unique:" + unique);
                    addUserToRecyclerView(followerBean);
                } else {
                    continue;
                }
            }
        }
    }

    private void addUserToRecyclerView(FollowerBean followerBean) {
        mAdapter.addGhostFollowers(followerBean);
        mAdapter.notifyDataSetChanged();
    }
}
