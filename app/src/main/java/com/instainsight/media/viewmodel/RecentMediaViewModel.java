package com.instainsight.media.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.instainsight.LoginActivity;
import com.instainsight.Utils.Utility;
import com.instainsight.constants.Constants;
import com.instainsight.instagram.InstagramSession;
import com.instainsight.media.RecentMediaDBQueries;
import com.instainsight.media.RecentMediaEvent;
import com.instainsight.media.RecentMediaService;
import com.instainsight.media.models.RecentMediaBean;
import com.instainsight.models.ListResponseBean;
import com.instainsight.networking.MyCallBack;
import com.instainsight.viewmodels.BaseViewModel;
import com.instainsight.viewmodels.IViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;

/**
 * Created by SONY on 12-02-2017.
 */

public class RecentMediaViewModel extends BaseViewModel implements IViewModel {

    private String TAG = RecentMediaViewModel.class.getSimpleName();
    private RecentMediaService recentMediaService;
    private Activity mActivity;
    private InstagramSession mInstagramSession;
    private Context mContext;

    public RecentMediaViewModel(RecentMediaService recentMediaService, Context context, InstagramSession instagramSession) {
        this.recentMediaService = recentMediaService;
        mInstagramSession = instagramSession;
        mContext = context;
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

    public void getRecentMediaLikes() {
        if (mInstagramSession.isActive()) {
            if (isConnected()) {
                StringBuilder strBldrILikeMostUrl = new StringBuilder()
                        .append(Constants.WebFields.ENDPOINT_RECENT_MEDIA_DAG)
                        .append(mInstagramSession.getAccessToken());
                recentMediaService.getRecentMedia(new MyCallBack<ListResponseBean<RecentMediaBean>>() {
                    @Override
                    public void onSuccess(ListResponseBean<RecentMediaBean> response) {
                        Log.d(TAG, "getRecentMediaLikes:" + response.getData().size());
                        setRecentMediaToDB(response.getData());
                    }

                    @Override
                    public void onError(String header, String message) {
                        Log.d(TAG, "getRecentMediaLikes:onError: header:" + header + " & message:" + message);
                        EventBus.getDefault().post(new RecentMediaEvent());
                    }
                }, strBldrILikeMostUrl.toString());
            } else {
                getRecentMediaLikesFromDB();
            }
        } else {
            Utility.showToast(mActivity, "Could not authentication, need to log in again");
            Intent intent = new Intent(mActivity, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mActivity.startActivity(intent);
            mActivity.finish();
        }
    }

    private void setRecentMediaToDB(ArrayList<RecentMediaBean> arylstRecentMedia) {
        RecentMediaDBQueries recentMediaDBQueries = new RecentMediaDBQueries(mContext);
        try {
            Utility.makeObservable(recentMediaDBQueries.saveRecentMedia(arylstRecentMedia))
                    .subscribe(new Consumer<ArrayList<RecentMediaBean>>() {
                        @Override
                        public void accept(ArrayList<RecentMediaBean> recentMediaBeen) throws Exception {
                            RecentMediaEvent recentMediaEvent = new RecentMediaEvent();
                            recentMediaEvent.setAryLstRecentMedia(recentMediaBeen);
                            EventBus.getDefault().post(recentMediaEvent);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getRecentMediaLikesFromDB() {
        RecentMediaDBQueries recentMediaDBQueries = new RecentMediaDBQueries(mContext);
        try {
            Utility.makeObservable(recentMediaDBQueries.getRecentMedia())
                    .subscribe(new Consumer<ArrayList<RecentMediaBean>>() {
                        @Override
                        public void accept(ArrayList<RecentMediaBean> recentMediaBeen) throws Exception {
                            RecentMediaEvent recentMediaEvent = new RecentMediaEvent();
                            recentMediaEvent.setAryLstRecentMedia(recentMediaBeen);
                            EventBus.getDefault().post(recentMediaEvent);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
