package com.instainsight.ilikedmost;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.instainsight.IRelationshipStatus;
import com.instainsight.InstaInsightApp;
import com.instainsight.R;
import com.instainsight.RelationshipStatusChangeListner;
import com.instainsight.Utils.DividerItemDecoration;
import com.instainsight.ViewModelActivity;
import com.instainsight.databinding.ActivityIlikedMostBinding;
import com.instainsight.ilikedmost.models.ILikedMostBean;
import com.instainsight.ilikedmost.viewmodel.ILikedMostViewModel;
import com.instainsight.models.ObjectResponseBean;
import com.instainsight.models.RelationShipStatus;
import com.instainsight.networking.RestClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.instainsight.instagram.util.Cons.DAGGER_API_BASE_URL;

public class ILikedMostActivity extends ViewModelActivity implements RelationshipStatusChangeListner {

    @Inject
    ILikedMostViewModel iLikedMostViewModel;
    ActivityIlikedMostBinding activityIlikedMostBinding;
    private String TAG = ILikedMostActivity.class.getSimpleName();
    private ILikedMostAdap mAdapter;

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
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.base));
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
        } else {
            activityIlikedMostBinding.rcyclrvwIlikedmost.setVisibility(View.GONE);
            activityIlikedMostBinding.prgsbrIlikedmost.setVisibility(View.GONE);
            activityIlikedMostBinding.txtvwNoIlikedmost.setVisibility(View.VISIBLE);
        }
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
        RestClient restClient = new RestClient(DAGGER_API_BASE_URL);
        IRelationshipStatus iRelationshipStatus = restClient.create(IRelationshipStatus.class);

        iRelationshipStatus.changeRelationshipStatus("unfollow", userId, mInstagramSession.getAccessToken())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ObjectResponseBean<RelationShipStatus>>() {
                    @Override
                    public void accept(ObjectResponseBean<RelationShipStatus> relationShipStatusObjectResponseBean) throws Exception {
                        mAdapter.removeILikedMost(position);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }
}
