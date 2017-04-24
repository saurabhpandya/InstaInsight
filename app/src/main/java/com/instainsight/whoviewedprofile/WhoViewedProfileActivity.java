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
import com.instainsight.RelationshipStatusChangeListner;
import com.instainsight.Utils.DividerItemDecoration;
import com.instainsight.ViewModelActivity;
import com.instainsight.databinding.ActivityWhoViewedProfileBinding;
import com.instainsight.models.ObjectResponseBean;
import com.instainsight.models.RelationShipStatus;
import com.instainsight.whoviewedprofile.model.WhoViewedProfileBean;
import com.instainsight.whoviewedprofile.viewmodel.WhoViewedProfileViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class WhoViewedProfileActivity extends ViewModelActivity implements RelationshipStatusChangeListner {

    @Inject
    WhoViewedProfileViewModel whoViewedProfileViewModel;
    private String TAG = WhoViewedProfileActivity.class.getSimpleName();
    private ActivityWhoViewedProfileBinding activityWhoViewedProfileBinding;
    private WhoViewedProfileAdap mAdapter;
    private ArrayList<WhoViewedProfileBean> arylstWhoViewedProfile = new ArrayList<>();

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
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRecyclerView() {
        arylstWhoViewedProfile = new ArrayList<WhoViewedProfileBean>();
        mAdapter = new WhoViewedProfileAdap(WhoViewedProfileActivity.this, arylstWhoViewedProfile, this);
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
            arylstWhoViewedProfile = whoViewedProfileEvent.getArylstWhoViewedProfile();
            Log.d(TAG, "arylstVwdPrflByUserFinal:" + arylstWhoViewedProfile.size());
            if (arylstWhoViewedProfile.size() > 0) {
                mAdapter.addWhoViewedProfile(arylstWhoViewedProfile);
                mAdapter.notifyDataSetChanged();
                activityWhoViewedProfileBinding.rcyclrvwWhoViewedProfile.setVisibility(View.VISIBLE);
                activityWhoViewedProfileBinding.prgsbrWhoViewedProfile.setVisibility(View.GONE);
                activityWhoViewedProfileBinding.txtvwWhoViewedProfile.setVisibility(View.GONE);
                getRelationShipStatus(arylstWhoViewedProfile);
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

    private void getRelationShipStatus(ArrayList<WhoViewedProfileBean> arylstWhoViewedProfile) {
        whoViewedProfileViewModel.getRelationShipStatus(arylstWhoViewedProfile)
                .subscribe(new Consumer<List<WhoViewedProfileBean>>() {
                    @Override
                    public void accept(List<WhoViewedProfileBean> whoViewedProfileBeen) throws Exception {
                        WhoViewedProfileActivity.this.arylstWhoViewedProfile = new ArrayList<WhoViewedProfileBean>(whoViewedProfileBeen);
                        if (WhoViewedProfileActivity.this.arylstWhoViewedProfile.size() > 0) {
                            mAdapter.addWhoViewedProfile(WhoViewedProfileActivity.this.arylstWhoViewedProfile);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void onClickToChangeRelationStatus(final int position, String userId) {

        Log.d(TAG, "onClickToChangeRelationStatus::position:" + position + " & userId:" + userId);

        if (arylstWhoViewedProfile.size() > 0) {

            final WhoViewedProfileBean whoViewedProfileBean = arylstWhoViewedProfile.get(position);

            String action = "";
            if (whoViewedProfileBean.getRelationShipStatus().getOutgoing_status().equalsIgnoreCase("none")) {
                action = "follow";
            } else if (whoViewedProfileBean.getRelationShipStatus().getOutgoing_status().equalsIgnoreCase("requested")) {
                action = "unfollow";
            } else if (whoViewedProfileBean.getRelationShipStatus().getOutgoing_status().equalsIgnoreCase("follows")) {
                action = "unfollow";
            }
            whoViewedProfileViewModel.changeRelationshipStatus(action, whoViewedProfileBean.getId(),
                    mInstagramSession.getAccessToken())
                    .subscribe(new Consumer<ObjectResponseBean<RelationShipStatus>>() {
                        @Override
                        public void accept(ObjectResponseBean<RelationShipStatus> relationShipStatusBean) throws Exception {
                            RelationShipStatus relationShipStatus = relationShipStatusBean.getData();

                            whoViewedProfileBean.setRelationShipStatus(relationShipStatus);
                            arylstWhoViewedProfile.set(position, whoViewedProfileBean);
                            mAdapter.notifyDataSetChanged();

                            if (arylstWhoViewedProfile.size() == 0) {
                                activityWhoViewedProfileBinding.rcyclrvwWhoViewedProfile.setVisibility(View.GONE);
                                activityWhoViewedProfileBinding.prgsbrWhoViewedProfile.setVisibility(View.GONE);
                                activityWhoViewedProfileBinding.txtvwWhoViewedProfile.setVisibility(View.VISIBLE);
                            }

//                            if (relationShipStatus.getOutgoing_status().equalsIgnoreCase("none")) {
////                                arylstWhoViewedProfile.remove(position);
////                                mAdapter.removeWhoViewedProfile(position);
////                                mAdapter.notifyDataSetChanged();
//                                if (arylstWhoViewedProfile.size() == 0) {
//                                    activityWhoViewedProfileBinding.rcyclrvwWhoViewedProfile.setVisibility(View.GONE);
//                                    activityWhoViewedProfileBinding.prgsbrWhoViewedProfile.setVisibility(View.GONE);
//                                    activityWhoViewedProfileBinding.txtvwWhoViewedProfile.setVisibility(View.VISIBLE);
//                                }
//                            } else {
//                                whoViewedProfileBean.setRelationShipStatus(relationShipStatus);
//                                arylstWhoViewedProfile.set(position, whoViewedProfileBean);
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
