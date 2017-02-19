package com.instainsight.followersing;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.instainsight.InstaInsightApp;
import com.instainsight.R;
import com.instainsight.ViewModelActivity;
import com.instainsight.databinding.ActivityOtherUserBinding;
import com.instainsight.followersing.viewmodel.OtherUserViewModel;

import javax.inject.Inject;

public class OtherUserActivity extends ViewModelActivity {

    @Inject
    public OtherUserViewModel otherUserViewModel;
    public ActivityOtherUserBinding activityOtherUserBinding;
    private String TAG = OtherUserActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((InstaInsightApp) getApplication()).getComponent().inject(OtherUserActivity.this);
        super.onCreate(savedInstanceState);
        setTitle(R.string.lbl_follower);
        initActionbar();
        activityOtherUserBinding = DataBindingUtil.setContentView(this, R.layout.activity_other_user);
        activityOtherUserBinding.setOtherUserViewModel(otherUserViewModel);
        getUsers();

    }

    private void getUsers() {
        otherUserViewModel.getFollowedBy();
        otherUserViewModel.getFollows();
    }

    @Override
    protected void createViewModel() {
        mViewModel = otherUserViewModel;
    }

    private void initActionbar() {
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.base));
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

//    @Override
//    protected void onStart() {
//        EventBus.getDefault().register(this);
//        super.onStart();
//    }
//
//    @Override
//    protected void onStop() {
//        EventBus.getDefault().unregister(this);
//        super.onStop();
//    }


}
