package com.instainsight.mytoplikers;

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
import com.instainsight.Utils.DividerItemDecoration;
import com.instainsight.ViewModelActivity;
import com.instainsight.databinding.ActivityMyTopLikersBinding;
import com.instainsight.models.UserBean;
import com.instainsight.mytoplikers.viewmodel.MyTopLikersViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import javax.inject.Inject;

public class MyTopLikersActivity extends ViewModelActivity {

    @Inject
    MyTopLikersViewModel myTopLikersViewModel;
    ArrayList<UserBean> arylstTopLikers = new ArrayList<UserBean>();
    ArrayList<UserBean> arylstTopLikersSorted = new ArrayList<UserBean>();
    private ActivityMyTopLikersBinding activityMyTopLikersBinding;
    private String TAG = MyTopLikersActivity.class.getSimpleName();
    private MyTopLikersAdap mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((InstaInsightApp) getApplication()).getComponent().inject(MyTopLikersActivity.this);
        super.onCreate(savedInstanceState);
        setTitle(R.string.lbl_my_top_likers);
        initActionbar();
        activityMyTopLikersBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_top_likers);
        activityMyTopLikersBinding.setMyTopLikers(myTopLikersViewModel);
        initRecyclerView();
        activityMyTopLikersBinding.prgsbrMyTopLikers.setVisibility(View.VISIBLE);
        myTopLikersViewModel.getRecentMediaToGetTopLikers();
    }

    private void initRecyclerView() {
        ArrayList<UserBean> arylstMyTopLikers = new ArrayList<UserBean>();
        mAdapter = new MyTopLikersAdap(MyTopLikersActivity.this, arylstMyTopLikers);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        activityMyTopLikersBinding.rcyclrvwMyTopLikers.setLayoutManager(mLayoutManager);
        activityMyTopLikersBinding.rcyclrvwMyTopLikers.setItemAnimator(new DefaultItemAnimator());
        activityMyTopLikersBinding.rcyclrvwMyTopLikers
                .addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        activityMyTopLikersBinding.rcyclrvwMyTopLikers.setAdapter(mAdapter);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MyTopLikersEvent myTopLikersEvent) {

        if (myTopLikersEvent.getTopLiker() != null) {

            UserBean myTopLiker = myTopLikersEvent.getTopLiker();
            if (myTopLiker.getId().equalsIgnoreCase(mInstagramSession.getUser().getUserBean().getId()))
                return;
//        Log.d(TAG, "onEvent:arylstTopLikers:" + arylstTopLikers.size());

            boolean isAvail = false;
            for (UserBean userBean : arylstTopLikers) {
                if (userBean.getId().equalsIgnoreCase(myTopLiker.getId())) {
                    isAvail = true;
                    continue;
                }
            }
            if (!isAvail) {
                arylstTopLikers.add(myTopLiker);
                Log.d(TAG, "onEvent:arylstTopLikers:" + arylstTopLikers.size());

                if (arylstTopLikers.size() > 0) {
                    mAdapter.addMyTopLikers(arylstTopLikers);
                    mAdapter.notifyDataSetChanged();
                    activityMyTopLikersBinding.rcyclrvwMyTopLikers.setVisibility(View.VISIBLE);
                    activityMyTopLikersBinding.prgsbrMyTopLikers.setVisibility(View.GONE);
                    activityMyTopLikersBinding.txtvwNoMyTopLikers.setVisibility(View.GONE);
                } else {
                    activityMyTopLikersBinding.rcyclrvwMyTopLikers.setVisibility(View.GONE);
                    activityMyTopLikersBinding.prgsbrMyTopLikers.setVisibility(View.GONE);
                    activityMyTopLikersBinding.txtvwNoMyTopLikers.setVisibility(View.VISIBLE);
                }
            }
        } else {
            activityMyTopLikersBinding.rcyclrvwMyTopLikers.setVisibility(View.GONE);
            activityMyTopLikersBinding.txtvwNoMyTopLikers.setVisibility(View.VISIBLE);
            activityMyTopLikersBinding.prgsbrMyTopLikers.setVisibility(View.GONE);
        }
    }

    private void initActionbar() {
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.base));
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    protected void createViewModel() {
        mViewModel = myTopLikersViewModel;
    }


}
