package com.instainsight.mytoplikers;

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
import com.instainsight.databinding.ActivityMyTopLikersBinding;
import com.instainsight.login.LoginActivity;
import com.instainsight.models.ObjectResponseBean;
import com.instainsight.models.RelationShipStatus;
import com.instainsight.models.UserBean;
import com.instainsight.mytoplikers.viewmodel.MyTopLikersViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MyTopLikersActivity extends ViewModelActivity implements RelationshipStatusChangeListner {

    @Inject
    MyTopLikersViewModel myTopLikersViewModel;
    ArrayList<UserBean> arylstTopLikers = new ArrayList<UserBean>();
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
//        myTopLikersViewModel.getRecentMediaToGetTopLikers();
        getMyTopLikers();
    }


    private ArrayList<UserBean> clearListFromDuplicateFirstName(ArrayList<UserBean> list1) {

        Map<String, UserBean> cleanMap = new LinkedHashMap<String, UserBean>();
        for (int i = 0; i < list1.size(); i++) {
            cleanMap.put(list1.get(i).getId(), list1.get(i));
        }
        ArrayList<UserBean> list = new ArrayList<UserBean>(cleanMap.values());
        return list;
    }

    private void getMyTopLikers() {

        if (mInstagramSession.isActive()) {
            if (isConnected()) {

                myTopLikersViewModel.getMyTopLikers()
                        .subscribe(new Consumer<List<ArrayList<UserBean>>>() {
                            @Override
                            public void accept(List<ArrayList<UserBean>> myTopLikersList) throws Exception {

                                Log.d(TAG, "getMyTopLikers:arylstTopLikers:" + myTopLikersList.size());
                                for (int i = 0; i < myTopLikersList.size(); i++) {
                                    arylstTopLikers.addAll(myTopLikersList.get(i));
                                }
//                                arylstTopLikers.addAll(myTopLikersList.getData());
                                arylstTopLikers = clearListFromDuplicateFirstName(arylstTopLikers);
                                Log.d(TAG, "getMyTopLikers:arylstTopLikers:" + arylstTopLikers.size());

                                if (arylstTopLikers.size() > 0) {
                                    mAdapter.addMyTopLikers(arylstTopLikers);
                                    mAdapter.notifyDataSetChanged();
                                    activityMyTopLikersBinding.rcyclrvwMyTopLikers.setVisibility(View.VISIBLE);
                                    activityMyTopLikersBinding.prgsbrMyTopLikers.setVisibility(View.GONE);
                                    activityMyTopLikersBinding.txtvwNoMyTopLikers.setVisibility(View.GONE);
                                    getRelationShipStatus(arylstTopLikers);
                                } else {
                                    activityMyTopLikersBinding.rcyclrvwMyTopLikers.setVisibility(View.GONE);
                                    activityMyTopLikersBinding.prgsbrMyTopLikers.setVisibility(View.GONE);
                                    activityMyTopLikersBinding.txtvwNoMyTopLikers.setVisibility(View.VISIBLE);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                            }
                        });
            } else {
                Utility.showToast(MyTopLikersActivity.this, "Not connected to internet.");
            }
        } else {
            Utility.showToast(getApplicationContext(), "Could not authentication, need to log in again");
            Intent intent = new Intent(MyTopLikersActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void getRelationShipStatus(ArrayList<UserBean> arylstTopLikers) {
        myTopLikersViewModel.getRelationShipStatus(arylstTopLikers)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<UserBean>>() {
                    @Override
                    public void accept(List<UserBean> topLikersWithRelationshipStatus) throws Exception {
                        Log.d(TAG, "getRelationShipStatus:" + MyTopLikersActivity.this.arylstTopLikers.size());
                        if (topLikersWithRelationshipStatus.size() > 0) {
                            MyTopLikersActivity.this.arylstTopLikers = new ArrayList<UserBean>();
                            for (int i = 0; i < topLikersWithRelationshipStatus.size(); i++) {
                                MyTopLikersActivity.this.arylstTopLikers.add(topLikersWithRelationshipStatus.get(i));
                                mAdapter.addMyTopLikers(MyTopLikersActivity.this.arylstTopLikers);
                                mAdapter.notifyDataSetChanged();
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

    private void initRecyclerView() {
        ArrayList<UserBean> arylstMyTopLikers = new ArrayList<UserBean>();
        mAdapter = new MyTopLikersAdap(MyTopLikersActivity.this, arylstMyTopLikers, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        activityMyTopLikersBinding.rcyclrvwMyTopLikers.setLayoutManager(mLayoutManager);
        activityMyTopLikersBinding.rcyclrvwMyTopLikers.setItemAnimator(new DefaultItemAnimator());
        activityMyTopLikersBinding.rcyclrvwMyTopLikers
                .addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        activityMyTopLikersBinding.rcyclrvwMyTopLikers.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
//        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
//        EventBus.getDefault().unregister(this);
        super.onStop();
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(MyTopLikersEvent myTopLikersEvent) {
//
//        if (myTopLikersEvent.getTopLiker() != null) {
//
//            UserBean myTopLiker = myTopLikersEvent.getTopLiker();
//            if (myTopLiker.getId().equalsIgnoreCase(mInstagramSession.getUser().getUserBean().getId()))
//                return;
////        Log.d(TAG, "onEvent:arylstTopLikers:" + arylstTopLikers.size());
//
//            boolean isAvail = false;
//            for (UserBean userBean : arylstTopLikers) {
//                if (userBean.getId().equalsIgnoreCase(myTopLiker.getId())) {
//                    isAvail = true;
//                    continue;
//                }
//            }
//            if (!isAvail) {
//                arylstTopLikers.add(myTopLiker);
//                Log.d(TAG, "onEvent:arylstTopLikers:" + arylstTopLikers.size());
//
//                if (arylstTopLikers.size() > 0) {
//                    mAdapter.addMyTopLikers(arylstTopLikers);
//                    mAdapter.notifyDataSetChanged();
//                    activityMyTopLikersBinding.rcyclrvwMyTopLikers.setVisibility(View.VISIBLE);
//                    activityMyTopLikersBinding.prgsbrMyTopLikers.setVisibility(View.GONE);
//                    activityMyTopLikersBinding.txtvwNoMyTopLikers.setVisibility(View.GONE);
//                } else {
//                    activityMyTopLikersBinding.rcyclrvwMyTopLikers.setVisibility(View.GONE);
//                    activityMyTopLikersBinding.prgsbrMyTopLikers.setVisibility(View.GONE);
//                    activityMyTopLikersBinding.txtvwNoMyTopLikers.setVisibility(View.VISIBLE);
//                }
//            }
//        } else {
//            activityMyTopLikersBinding.rcyclrvwMyTopLikers.setVisibility(View.GONE);
//            activityMyTopLikersBinding.txtvwNoMyTopLikers.setVisibility(View.VISIBLE);
//            activityMyTopLikersBinding.prgsbrMyTopLikers.setVisibility(View.GONE);
//        }
//    }

    private void initActionbar() {
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

    @Override
    public void onClickToChangeRelationStatus(final int position, String userId) {

        if (arylstTopLikers.size() > 0) {
            final UserBean topLikerBean = arylstTopLikers.get(position);
            String action = "";
            if (topLikerBean.getRelationshipStatus().getOutgoing_status().equalsIgnoreCase("none")) {
                action = "follow";
            } else if (topLikerBean.getRelationshipStatus().getOutgoing_status().equalsIgnoreCase("requested")) {
                action = "unfollow";
            } else if (topLikerBean.getRelationshipStatus().getOutgoing_status().equalsIgnoreCase("follows")) {
                action = "unfollow";
            }
            myTopLikersViewModel.changeRelationshipStatus(action, topLikerBean.getId(),
                    mInstagramSession.getAccessToken())
                    .subscribe(new Consumer<ObjectResponseBean<RelationShipStatus>>() {
                        @Override
                        public void accept(ObjectResponseBean<RelationShipStatus> relationShipStatusBean) throws Exception {
                            RelationShipStatus relationShipStatus = relationShipStatusBean.getData();

                            topLikerBean.setRelationshipStatus(relationShipStatus);
                            arylstTopLikers.set(position, topLikerBean);
                            mAdapter.notifyDataSetChanged();

                            if (arylstTopLikers.size() == 0) {
                                activityMyTopLikersBinding.rcyclrvwMyTopLikers.setVisibility(View.GONE);
                                activityMyTopLikersBinding.prgsbrMyTopLikers.setVisibility(View.GONE);
                                activityMyTopLikersBinding.txtvwNoMyTopLikers.setVisibility(View.VISIBLE);
                            }

//                            if (relationShipStatus.getOutgoing_status().equalsIgnoreCase("none")) {
////                                arylstTopLikers.remove(position);
////                                mAdapter.removeMyTopLikers(position);
////                                mAdapter.notifyDataSetChanged();
//                                if (arylstTopLikers.size() == 0){
//                                    activityMyTopLikersBinding.rcyclrvwMyTopLikers.setVisibility(View.GONE);
//                                    activityMyTopLikersBinding.prgsbrMyTopLikers.setVisibility(View.GONE);
//                                    activityMyTopLikersBinding.txtvwNoMyTopLikers.setVisibility(View.VISIBLE);
//                                }
//                            } else {
//                                topLikerBean.setRelationshipStatus(relationShipStatus);
//                                arylstTopLikers.set(position, topLikerBean);
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
