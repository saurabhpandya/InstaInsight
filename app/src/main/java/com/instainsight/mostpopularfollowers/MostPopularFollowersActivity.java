package com.instainsight.mostpopularfollowers;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.instainsight.InstaInsightApp;
import com.instainsight.R;
import com.instainsight.Utils.DividerItemDecoration;
import com.instainsight.ViewModelActivity;
import com.instainsight.databinding.ActivityMostPopularFollowersBinding;
import com.instainsight.followersing.models.OtherUsersBean;
import com.instainsight.mostpopularfollowers.viewmodel.MostPopularFollowersViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import javax.inject.Inject;

public class MostPopularFollowersActivity extends ViewModelActivity {

    @Inject
    MostPopularFollowersViewModel mostPopularFollowersViewModel;
    private String TAG = MostPopularFollowersActivity.class.getSimpleName();
    private ActivityMostPopularFollowersBinding activityMostPopularFollowersBinding;
    private MostPopularFollowersAdap mAdapter;
    private ArrayList<OtherUsersBean> mostPopularFollowers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((InstaInsightApp) getApplication()).getComponent().inject(MostPopularFollowersActivity.this);
        super.onCreate(savedInstanceState);
        activityMostPopularFollowersBinding = DataBindingUtil.setContentView(MostPopularFollowersActivity.this,
                R.layout.activity_most_popular_followers);
        activityMostPopularFollowersBinding.setMostPopularFollowers(mostPopularFollowersViewModel);
        setTitle(R.string.lbl_popular_followers);
        initActionbar();
        initRecyclerView();
        activityMostPopularFollowersBinding.prgsbrMostPopularFollowers.setVisibility(View.VISIBLE);
        mostPopularFollowersViewModel.getMostPopularFollowers();
    }

    private void initRecyclerView() {
        ArrayList<OtherUsersBean> arylstMostPopularFollowers = new ArrayList<OtherUsersBean>();
        mAdapter = new MostPopularFollowersAdap(MostPopularFollowersActivity.this, arylstMostPopularFollowers);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        activityMostPopularFollowersBinding.rcyclrvwMostPopularFollowers.setLayoutManager(mLayoutManager);
        activityMostPopularFollowersBinding.rcyclrvwMostPopularFollowers.setItemAnimator(new DefaultItemAnimator());
        activityMostPopularFollowersBinding.rcyclrvwMostPopularFollowers
                .addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        activityMostPopularFollowersBinding.rcyclrvwMostPopularFollowers.setAdapter(mAdapter);
    }

    private void initActionbar() {
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.base));
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    @Override
    protected void createViewModel() {
        mViewModel = mostPopularFollowersViewModel;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MostPopularFollowersEvent mostPopularFollowersEvent) {
        if (mostPopularFollowersEvent.getMostPopularFollowers() != null) {
            mostPopularFollowers.addAll(mostPopularFollowersEvent.getMostPopularFollowers());

            if (mostPopularFollowers.size() > 0) {
                mAdapter.addMostPopularFollowers(mostPopularFollowers);
                mAdapter.notifyDataSetChanged();
                activityMostPopularFollowersBinding.rcyclrvwMostPopularFollowers.setVisibility(View.VISIBLE);
                activityMostPopularFollowersBinding.prgsbrMostPopularFollowers.setVisibility(View.GONE);
                activityMostPopularFollowersBinding.txtvwNoMostPopularFollowers.setVisibility(View.GONE);
            } else {
                activityMostPopularFollowersBinding.rcyclrvwMostPopularFollowers.setVisibility(View.GONE);
                activityMostPopularFollowersBinding.prgsbrMostPopularFollowers.setVisibility(View.GONE);
                activityMostPopularFollowersBinding.txtvwNoMostPopularFollowers.setVisibility(View.VISIBLE);
            }
        } else {
            activityMostPopularFollowersBinding.rcyclrvwMostPopularFollowers.setVisibility(View.GONE);
            activityMostPopularFollowersBinding.prgsbrMostPopularFollowers.setVisibility(View.GONE);
            activityMostPopularFollowersBinding.txtvwNoMostPopularFollowers.setVisibility(View.VISIBLE);
        }
    }
}
