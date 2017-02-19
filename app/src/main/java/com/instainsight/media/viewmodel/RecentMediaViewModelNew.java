package com.instainsight.media.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.instainsight.Utils.Utility;
import com.instainsight.constants.Constants;
import com.instainsight.instagram.InstagramSession;
import com.instainsight.login.LoginActivity;
import com.instainsight.media.MediaEvent;
import com.instainsight.media.RecentMediaDBQueriesNew;
import com.instainsight.media.RecentMediaEvent;
import com.instainsight.media.RecentMediaService;
import com.instainsight.media.models.MediaBean;
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

public class RecentMediaViewModelNew extends BaseViewModel implements IViewModel {

    private String TAG = RecentMediaViewModelNew.class.getSimpleName();
    private RecentMediaService recentMediaService;
    private Activity mActivity;
    private InstagramSession mInstagramSession;
    private Context mContext;

    public RecentMediaViewModelNew(RecentMediaService recentMediaService, Context context, InstagramSession instagramSession) {
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
                recentMediaService.getRecentMediaNew(new MyCallBack<ListResponseBean<MediaBean>>() {
                    @Override
                    public void onSuccess(ListResponseBean<MediaBean> arylstRecentMedia) {
                        Log.d(TAG, "getRecentMediaLikes:" + arylstRecentMedia.getData().size());
                        setRecentMediaToDB(arylstRecentMedia.getData());
                    }

                    @Override
                    public void onError(String header, String message) {
                        Log.d(TAG, "getRecentMediaLikes:onError: header:" + header + " & message:" + message);
                        EventBus.getDefault().post(new RecentMediaEvent());
                    }
                }, mInstagramSession.getAccessToken());
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

    private void setRecentMediaToDB(ArrayList<MediaBean> arylstRecentMedia) {
        RecentMediaDBQueriesNew recentMediaDBQueries = new RecentMediaDBQueriesNew(mContext);
        try {
            Utility.makeObservable(recentMediaDBQueries.saveRecentMedia(arylstRecentMedia))
                    .subscribe(new Consumer<ArrayList<MediaBean>>() {
                        @Override
                        public void accept(ArrayList<MediaBean> mediaBeen) throws Exception {
                            MediaEvent mediaEvent = new MediaEvent();
                            mediaEvent.setAryLstMedia(mediaBeen);
                            EventBus.getDefault().post(mediaEvent);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getRecentMediaLikesFromDB() {
        RecentMediaDBQueriesNew recentMediaDBQueries = new RecentMediaDBQueriesNew(mContext);
        try {
            Utility.makeObservable(recentMediaDBQueries.getRecentMedia())
                    .subscribe(new Consumer<ArrayList<MediaBean>>() {
                        @Override
                        public void accept(ArrayList<MediaBean> recentMediaBeen) throws Exception {
                            MediaEvent recentMediaEvent = new MediaEvent();
                            recentMediaEvent.setAryLstMedia(recentMediaBeen);
                            EventBus.getDefault().post(recentMediaEvent);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
