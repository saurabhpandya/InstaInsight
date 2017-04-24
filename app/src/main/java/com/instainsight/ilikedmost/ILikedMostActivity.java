package com.instainsight.ilikedmost;

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
import com.instainsight.databinding.ActivityIlikedMostBinding;
import com.instainsight.ilikedmost.models.ILikedMostBean;
import com.instainsight.ilikedmost.viewmodel.ILikedMostViewModel;
import com.instainsight.models.ObjectResponseBean;
import com.instainsight.models.RelationShipStatus;
import com.instainsight.models.UserBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ILikedMostActivity extends ViewModelActivity implements RelationshipStatusChangeListner {

    @Inject
    ILikedMostViewModel iLikedMostViewModel;
    ActivityIlikedMostBinding activityIlikedMostBinding;
    private String TAG = ILikedMostActivity.class.getSimpleName();
    private ILikedMostAdap mAdapter;
    private ArrayList<ILikedMostBean> arylstILikedMost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((InstaInsightApp) getApplication()).getComponent().inject(ILikedMostActivity.this);
        super.onCreate(savedInstanceState);
        setTitle(R.string.lbl_who_i_like_most);
        initActionbar();
        activityIlikedMostBinding = DataBindingUtil.setContentView(this, R.layout.activity_iliked_most);
        activityIlikedMostBinding.setILikedMostViewModel(iLikedMostViewModel);
        initRecyclerView();
        activityIlikedMostBinding.prgsbrIlikedmost.setVisibility(View.VISIBLE);
        iLikedMostViewModel.getILikedMost();
    }

    private void initActionbar() {
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRecyclerView() {
        ArrayList<ILikedMostBean> arylstILikedMost = new ArrayList<ILikedMostBean>();
        mAdapter = new ILikedMostAdap(ILikedMostActivity.this, arylstILikedMost, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        activityIlikedMostBinding.rcyclrvwIlikedmost.setLayoutManager(mLayoutManager);
        activityIlikedMostBinding.rcyclrvwIlikedmost.setItemAnimator(new DefaultItemAnimator());
        activityIlikedMostBinding.rcyclrvwIlikedmost
                .addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        activityIlikedMostBinding.rcyclrvwIlikedmost.setAdapter(mAdapter);
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
        mViewModel = iLikedMostViewModel;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ILikedMostEvent iLikedMostEvent) {
        if (iLikedMostEvent != null && iLikedMostEvent.getArylstILikedMost() != null && iLikedMostEvent.getArylstILikedMost().size() > 0) {
            ArrayList<ILikedMostBean> arylstILikedMost = iLikedMostEvent.getArylstILikedMost();
            Log.d(TAG, "onEvent::arylstILikedMost:" + arylstILikedMost.size());
            mAdapter.addFollowersing(arylstILikedMost);
            mAdapter.notifyDataSetChanged();
            activityIlikedMostBinding.rcyclrvwIlikedmost.setVisibility(View.VISIBLE);
            activityIlikedMostBinding.prgsbrIlikedmost.setVisibility(View.GONE);
            activityIlikedMostBinding.txtvwNoIlikedmost.setVisibility(View.GONE);
            getRelationShipStatus(arylstILikedMost);
        } else {
            activityIlikedMostBinding.rcyclrvwIlikedmost.setVisibility(View.GONE);
            activityIlikedMostBinding.prgsbrIlikedmost.setVisibility(View.GONE);
            activityIlikedMostBinding.txtvwNoIlikedmost.setVisibility(View.VISIBLE);
        }

    }

    private void getRelationShipStatus(ArrayList<ILikedMostBean> arylstTopLikers) {
        iLikedMostViewModel.getRelationShipStatus(arylstTopLikers)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ILikedMostBean>>() {
                    @Override
                    public void accept(List<ILikedMostBean> iLikedMostBeanList) throws Exception {
                        Log.d(TAG, "getRelationShipStatus:iLikedMostBeanList.size():" + iLikedMostBeanList.size());
                        if (iLikedMostBeanList.size() > 0) {
                            arylstILikedMost = new ArrayList<ILikedMostBean>();
                            for (int i = 0; i < iLikedMostBeanList.size(); i++) {
                                arylstILikedMost.add(iLikedMostBeanList.get(i));
                                mAdapter.addFollowersing(arylstILikedMost);
                            }
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
    public void onClickToChangeRelationStatus(final int position, String userId) {

        if (arylstILikedMost.size() > 0) {

            final ILikedMostBean iLikedMostBean = arylstILikedMost.get(position);

            final UserBean iLikeMostUserBean = iLikedMostBean.getUsersBean();
            String action = "";
            if (iLikeMostUserBean.getRelationshipStatus().getOutgoing_status().equalsIgnoreCase("none")) {
                action = "follow";
            } else if (iLikeMostUserBean.getRelationshipStatus().getOutgoing_status().equalsIgnoreCase("requested")) {
                action = "unfollow";
            } else if (iLikeMostUserBean.getRelationshipStatus().getOutgoing_status().equalsIgnoreCase("follows")) {
                action = "unfollow";
            }
            iLikedMostViewModel.changeRelationshipStatus(action, iLikeMostUserBean.getId(),
                    mInstagramSession.getAccessToken())
                    .subscribe(new Consumer<ObjectResponseBean<RelationShipStatus>>() {
                        @Override
                        public void accept(ObjectResponseBean<RelationShipStatus> relationShipStatusBean) throws Exception {
                            RelationShipStatus relationShipStatus = relationShipStatusBean.getData();

                            iLikeMostUserBean.setRelationshipStatus(relationShipStatus);
                            iLikedMostBean.setUsersBean(iLikeMostUserBean);
                            arylstILikedMost.set(position, iLikedMostBean);
                            mAdapter.notifyDataSetChanged();

                            if (arylstILikedMost.size() == 0) {
                                activityIlikedMostBinding.rcyclrvwIlikedmost.setVisibility(View.GONE);
                                activityIlikedMostBinding.prgsbrIlikedmost.setVisibility(View.GONE);
                                activityIlikedMostBinding.txtvwNoIlikedmost.setVisibility(View.VISIBLE);
                            }

//                            if (relationShipStatus.getOutgoing_status().equalsIgnoreCase("none")) {
////                                arylstILikedMost.remove(position);
////                                mAdapter.removeILikedMost(position);
////                                mAdapter.notifyDataSetChanged();
//                                if (arylstILikedMost.size() == 0) {
//                                    activityIlikedMostBinding.rcyclrvwIlikedmost.setVisibility(View.GONE);
//                                    activityIlikedMostBinding.prgsbrIlikedmost.setVisibility(View.GONE);
//                                    activityIlikedMostBinding.txtvwNoIlikedmost.setVisibility(View.VISIBLE);
//                                }
//                            } else {
//                                iLikeMostUserBean.setRelationshipStatus(relationShipStatus);
//                                iLikedMostBean.setUsersBean(iLikeMostUserBean);
//                                arylstILikedMost.set(position, iLikedMostBean);
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
