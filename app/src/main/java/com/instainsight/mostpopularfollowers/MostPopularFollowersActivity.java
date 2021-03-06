package com.instainsight.mostpopularfollowers;

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
import com.instainsight.databinding.ActivityMostPopularFollowersBinding;
import com.instainsight.followersing.models.OtherUsersBean;
import com.instainsight.login.LoginActivity;
import com.instainsight.models.ObjectResponseBean;
import com.instainsight.models.RelationShipStatus;
import com.instainsight.mostpopularfollowers.viewmodel.MostPopularFollowersViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MostPopularFollowersActivity extends ViewModelActivity implements RelationshipStatusChangeListner {

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
        getMostPopularFollowers();
    }

    private void getMostPopularFollowers() {
        if (mInstagramSession.isActive()) {
            if (isConnected()) {
                mostPopularFollowersViewModel.getMostPopularFollowers()
                        .subscribe(new Consumer<List<Long>>() {
                            @Override
                            public void accept(List<Long> otherUsersBeen) throws Exception {

                                Log.d(TAG, "getMostPopularFollowers:otherUsersBeen:" + otherUsersBeen.size());
                                mostPopularFollowers = new ArrayList<OtherUsersBean>();
                                MostPopularFollowersDBQueries mostPopularFollowersDBQueries = new MostPopularFollowersDBQueries(MostPopularFollowersActivity.this);
                                mostPopularFollowers = mostPopularFollowersDBQueries.getMostPopularFollowers();
//                                for (int i = 0; i < otherUsersBeen.size(); i++) {
//                                    mostPopularFollowers.add(otherUsersBeen.get(i));
//                                }
//
//                                ArrayList<OtherUsersBean> arylstMostPopularFiltered = new ArrayList<OtherUsersBean>();
//                                ArrayList<String> arylstMostPopularId = new ArrayList<String>();
//                                for (int i = 0; i < mostPopularFollowers.size(); i++) {
//                                    arylstMostPopularId.add(mostPopularFollowers.get(i).getId());
//                                }
//
//                                Map<String, Integer> map = new HashMap<String, Integer>();
//
//                                for (String temp : arylstMostPopularId) {
//                                    Integer count = map.get(temp);
//                                    map.put(temp, (count == null) ? 1 : count + 1);
//                                }
//
//                                Map<String, Integer> treeMap = new TreeMap<String, Integer>(map);
//
//                                for (Map.Entry<String, Integer> entry : treeMap.entrySet()) {
//                                    Log.d(TAG, "Key : " + entry.getKey() + " Value : "
//                                            + entry.getValue());
//
//                                    for (OtherUsersBean otherUsersBean : mostPopularFollowers) {
//                                        if (otherUsersBean.getId().equalsIgnoreCase(entry.getKey().toString())
//                                                && !mInstagramSession.getUser().getUserBean().getId().equalsIgnoreCase(otherUsersBean.getId())) {
//                                            arylstMostPopularFiltered.add(otherUsersBean);
//                                        }
//
//                                    }
//                                }
//
//                                mostPopularFollowers = clearListFromDuplicateFirstName(arylstMostPopularFiltered);
//
////                                mostPopularFollowers.addAll(otherUsersBeen);
////                                Collections.sort(mostPopularFollowers, new Comparator<OtherUsersBean>() {
////                                    @Override
////                                    public int compare(OtherUsersBean otherUsersBean, OtherUsersBean t1) {
////                                        return t1.getUserCountBean().getFollowed_by().compareTo(otherUsersBean.getUserCountBean().getFollowed_by());
////                                    }
////                                });

                                if (mostPopularFollowers.size() > 0) {
                                    mAdapter.addMostPopularFollowers(mostPopularFollowers);
                                    mAdapter.notifyDataSetChanged();
                                    activityMostPopularFollowersBinding.rcyclrvwMostPopularFollowers.setVisibility(View.VISIBLE);
                                    activityMostPopularFollowersBinding.prgsbrMostPopularFollowers.setVisibility(View.GONE);
                                    activityMostPopularFollowersBinding.txtvwNoMostPopularFollowers.setVisibility(View.GONE);
                                    getRelationShipStatus(mostPopularFollowers);
                                } else {
                                    activityMostPopularFollowersBinding.rcyclrvwMostPopularFollowers.setVisibility(View.GONE);
                                    activityMostPopularFollowersBinding.prgsbrMostPopularFollowers.setVisibility(View.GONE);
                                    activityMostPopularFollowersBinding.txtvwNoMostPopularFollowers.setVisibility(View.VISIBLE);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Utility.showToast(MostPopularFollowersActivity.this, throwable.getMessage());
                                activityMostPopularFollowersBinding.rcyclrvwMostPopularFollowers.setVisibility(View.GONE);
                                activityMostPopularFollowersBinding.prgsbrMostPopularFollowers.setVisibility(View.GONE);
                                activityMostPopularFollowersBinding.txtvwNoMostPopularFollowers.setVisibility(View.VISIBLE);
                            }
                        });
            } else {
                Utility.showToast(this, getResources().getString(R.string.tst_no_internet));
            }
        } else {
            Utility.showToast(this, "Could not authentication, need to log in again");
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    private ArrayList<OtherUsersBean> clearListFromDuplicateFirstName(ArrayList<OtherUsersBean> list1) {

        Map<String, OtherUsersBean> cleanMap = new LinkedHashMap<String, OtherUsersBean>();
        for (int i = 0; i < list1.size(); i++) {
            cleanMap.put(list1.get(i).getId(), list1.get(i));
        }
        ArrayList<OtherUsersBean> list = new ArrayList<OtherUsersBean>(cleanMap.values());
        return list;
    }

    private void initRecyclerView() {
        ArrayList<OtherUsersBean> arylstMostPopularFollowers = new ArrayList<OtherUsersBean>();
        mAdapter = new MostPopularFollowersAdap(MostPopularFollowersActivity.this, arylstMostPopularFollowers, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        activityMostPopularFollowersBinding.rcyclrvwMostPopularFollowers.setLayoutManager(mLayoutManager);
        activityMostPopularFollowersBinding.rcyclrvwMostPopularFollowers.setItemAnimator(new DefaultItemAnimator());
        activityMostPopularFollowersBinding.rcyclrvwMostPopularFollowers
                .addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        activityMostPopularFollowersBinding.rcyclrvwMostPopularFollowers.setAdapter(mAdapter);
    }

    private void initActionbar() {
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
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

    private void getRelationShipStatus(ArrayList<OtherUsersBean> arylstOtherUsersBean) {
        mostPopularFollowersViewModel.getRelationShipStatus(arylstOtherUsersBean)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<OtherUsersBean>>() {
                    @Override
                    public void accept(List<OtherUsersBean> otherUsersBeanList) throws Exception {
                        Log.d(TAG, "getRelationShipStatus:otherUsersBeanList.size():" + otherUsersBeanList.size());
                        if (otherUsersBeanList.size() > 0) {
                            mostPopularFollowers = new ArrayList<>(otherUsersBeanList);
                            mAdapter.addMostPopularFollowers(mostPopularFollowers);
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
    public void onClickToChangeRelationStatus(final int position, String userId) {
        {

            if (mostPopularFollowers.size() > 0) {

                final OtherUsersBean mostPopularFollowersBean = mostPopularFollowers.get(position);

                String action = "";
                if (mostPopularFollowersBean.getRelationShipStatus().getOutgoing_status().equalsIgnoreCase("none")) {
                    action = "follow";
                } else if (mostPopularFollowersBean.getRelationShipStatus().getOutgoing_status().equalsIgnoreCase("requested")) {
                    action = "unfollow";
                } else if (mostPopularFollowersBean.getRelationShipStatus().getOutgoing_status().equalsIgnoreCase("follows")) {
                    action = "unfollow";
                }
                mostPopularFollowersViewModel.changeRelationshipStatus(action, mostPopularFollowersBean.getId(),
                        mInstagramSession.getAccessToken())
                        .subscribe(new Consumer<ObjectResponseBean<RelationShipStatus>>() {
                            @Override
                            public void accept(ObjectResponseBean<RelationShipStatus> relationShipStatusBean) throws Exception {
                                RelationShipStatus relationShipStatus = relationShipStatusBean.getData();

                                mostPopularFollowersBean.setRelationShipStatus(relationShipStatus);
                                mostPopularFollowers.set(position, mostPopularFollowersBean);
                                mAdapter.notifyDataSetChanged();

                                if (mostPopularFollowers.size() == 0) {
                                    activityMostPopularFollowersBinding.rcyclrvwMostPopularFollowers.setVisibility(View.GONE);
                                    activityMostPopularFollowersBinding.prgsbrMostPopularFollowers.setVisibility(View.GONE);
                                    activityMostPopularFollowersBinding.txtvwNoMostPopularFollowers.setVisibility(View.VISIBLE);
                                }

//                                if (relationShipStatus.getOutgoing_status().equalsIgnoreCase("none")) {
////                                    mostPopularFollowers.remove(position);
////                                    mAdapter.removeMostPopularFollower(position);
////                                    mAdapter.notifyDataSetChanged();
//                                    if (mostPopularFollowers.size() == 0) {
//                                        activityMostPopularFollowersBinding.rcyclrvwMostPopularFollowers.setVisibility(View.GONE);
//                                        activityMostPopularFollowersBinding.prgsbrMostPopularFollowers.setVisibility(View.GONE);
//                                        activityMostPopularFollowersBinding.txtvwNoMostPopularFollowers.setVisibility(View.VISIBLE);
//                                    }
//                                } else {
//                                    mostPopularFollowersBean.setRelationShipStatus(relationShipStatus);
//                                    mostPopularFollowers.set(position, mostPopularFollowersBean);
//                                    mAdapter.notifyDataSetChanged();
//                                }
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
}
