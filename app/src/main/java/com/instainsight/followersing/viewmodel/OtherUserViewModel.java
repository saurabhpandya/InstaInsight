package com.instainsight.followersing.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.instainsight.LoginActivity;
import com.instainsight.Utils.Utility;
import com.instainsight.constants.Constants;
import com.instainsight.followersing.OtherUserService;
import com.instainsight.followersing.models.FollowersingBean;
import com.instainsight.instagram.InstagramSession;
import com.instainsight.models.ListResponseBean;
import com.instainsight.networking.MyCallBack;
import com.instainsight.viewmodels.BaseViewModel;
import com.instainsight.viewmodels.IViewModel;

/**
 * Created by SONY on 14-02-2017.
 */

public class OtherUserViewModel extends BaseViewModel implements IViewModel {

    private String TAG = OtherUserViewModel.class.getSimpleName();
    private OtherUserService otherUserService;
    private Context mContext;
    private InstagramSession mInstagramSession;
    private Activity mActivity;

    public OtherUserViewModel(OtherUserService otherUserService, Context context, InstagramSession instagramSession) {
        this.otherUserService = otherUserService;
        this.mContext = context;
        this.mInstagramSession = instagramSession;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void setCurrentActivity(Activity value) {
        mActivity = value;
    }

    public void getFollowedBy() {
        if (mInstagramSession.isActive()) {
            if (isConnected()) {
                getFollowedByFromAPI();
            } else {

            }
        } else {
            Utility.showToast(mActivity, "Could not authentication, need to log in again");
            Intent intent = new Intent(mActivity, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mActivity.startActivity(intent);
            mActivity.finish();
        }
    }

    private void getFollowedByFromAPI() {
        StringBuilder strBldrFollowedBy = new StringBuilder()
                .append(Constants.WebFields.ENDPOINT_FOLLOWEDBY_DAG)
                .append(mInstagramSession.getAccessToken());

        otherUserService.getFollowedBy(new MyCallBack<ListResponseBean<FollowersingBean>>() {
            @Override
            public void onSuccess(ListResponseBean<FollowersingBean> arylstFollowedBy) {
                Log.d(TAG, "getFollowedBy:" + arylstFollowedBy.getData().size());
            }

            @Override
            public void onError(String header, String message) {

            }
        }, strBldrFollowedBy.toString());

    }

    public void getFollows() {
        if (mInstagramSession.isActive()) {
            if (isConnected()) {
                getFollowsFromAPI();
            } else {

            }
        } else {
            Utility.showToast(mActivity, "Could not authentication, need to log in again");
            Intent intent = new Intent(mActivity, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mActivity.startActivity(intent);
            mActivity.finish();
        }
    }

    private void getFollowsFromAPI() {
        StringBuilder strBldrFollows = new StringBuilder()
                .append(Constants.WebFields.ENDPOINT_FOLLOWS_DAG)
                .append(mInstagramSession.getAccessToken());

        otherUserService.getFollows(new MyCallBack<ListResponseBean<FollowersingBean>>() {
            @Override
            public void onSuccess(ListResponseBean<FollowersingBean> response) {

            }

            @Override
            public void onError(String header, String message) {

            }
        }, strBldrFollows.toString());
    }


}
