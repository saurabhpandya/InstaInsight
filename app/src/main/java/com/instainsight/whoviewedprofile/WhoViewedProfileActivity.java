package com.instainsight.whoviewedprofile;

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
import com.instainsight.databinding.ActivityWhoViewedProfileBinding;
import com.instainsight.whoviewedprofile.model.WhoViewedProfileBean;
import com.instainsight.whoviewedprofile.viewmodel.WhoViewedProfileViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import javax.inject.Inject;

public class WhoViewedProfileActivity extends ViewModelActivity {

    @Inject
    WhoViewedProfileViewModel whoViewedProfileViewModel;
    private String TAG = WhoViewedProfileActivity.class.getSimpleName();
    private ActivityWhoViewedProfileBinding activityWhoViewedProfileBinding;
    private WhoViewedProfileAdap mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((InstaInsightApp) getApplication()).getComponent().inject(WhoViewedProfileActivity.this);
        super.onCreate(savedInstanceState);
        activityWhoViewedProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_who_viewed_profile);
        activityWhoViewedProfileBinding.setWhoViewedProfile(whoViewedProfileViewModel);
        setTitle(R.string.lbl_prfl_vwrs);
        initActionbar();
        initRecyclerView();
        activityWhoViewedProfileBinding.prgsbrWhoViewedProfile.setVisibility(View.VISIBLE);
        whoViewedProfileViewModel.getWhoViewedProfile();
    }

    @Override
    protected void createViewModel() {
        mViewModel = whoViewedProfileViewModel;
    }

    private void initActionbar() {
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.base));
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRecyclerView() {
        ArrayList<WhoViewedProfileBean> arylstWhoViewedProfile = new ArrayList<WhoViewedProfileBean>();
        mAdapter = new WhoViewedProfileAdap(WhoViewedProfileActivity.this, arylstWhoViewedProfile);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        activityWhoViewedProfileBinding.rcyclrvwWhoViewedProfile.setLayoutManager(mLayoutManager);
        activityWhoViewedProfileBinding.rcyclrvwWhoViewedProfile.setItemAnimator(new DefaultItemAnimator());
        activityWhoViewedProfileBinding.rcyclrvwWhoViewedProfile
                .addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        activityWhoViewedProfileBinding.rcyclrvwWhoViewedProfile.setAdapter(mAdapter);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(WhoViewedProfileEvent whoViewedProfileEvent) {

        if (whoViewedProfileEvent.getArylstWhoViewedProfile() != null) {
            ArrayList<WhoViewedProfileBean> arylstWhoViewProfile = whoViewedProfileEvent.getArylstWhoViewedProfile();
            Log.d(TAG, "arylstVwdPrflByUserFinal:" + arylstWhoViewProfile.size());
            if (arylstWhoViewProfile.size() > 0) {
                mAdapter.addWhoViewedProfile(arylstWhoViewProfile);
                mAdapter.notifyDataSetChanged();
                activityWhoViewedProfileBinding.rcyclrvwWhoViewedProfile.setVisibility(View.VISIBLE);
                activityWhoViewedProfileBinding.prgsbrWhoViewedProfile.setVisibility(View.GONE);
                activityWhoViewedProfileBinding.txtvwWhoViewedProfile.setVisibility(View.GONE);
            } else {
                activityWhoViewedProfileBinding.rcyclrvwWhoViewedProfile.setVisibility(View.GONE);
                activityWhoViewedProfileBinding.prgsbrWhoViewedProfile.setVisibility(View.GONE);
                activityWhoViewedProfileBinding.txtvwWhoViewedProfile.setVisibility(View.VISIBLE);
            }
        } else {
            activityWhoViewedProfileBinding.rcyclrvwWhoViewedProfile.setVisibility(View.GONE);
            activityWhoViewedProfileBinding.prgsbrWhoViewedProfile.setVisibility(View.GONE);
            activityWhoViewedProfileBinding.txtvwWhoViewedProfile.setVisibility(View.VISIBLE);
        }
    }

}
