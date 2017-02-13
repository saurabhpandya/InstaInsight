package com.instainsight.ilikedmost.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.instainsight.LoginActivity;
import com.instainsight.Utils.Utility;
import com.instainsight.constants.Constants;
import com.instainsight.ilikedmost.ILikeMostDBQueries;
import com.instainsight.ilikedmost.ILikedMostEvent;
import com.instainsight.ilikedmost.ILikedMostService;
import com.instainsight.ilikedmost.models.ILikedMostBean;
import com.instainsight.instagram.InstagramSession;
import com.instainsight.models.ListResponseBean;
import com.instainsight.networking.MyCallBack;
import com.instainsight.viewmodels.BaseViewModel;
import com.instainsight.viewmodels.IViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;

/**
 * Created by SONY on 11-02-2017.
 */

public class ILikedMostViewModel extends BaseViewModel implements IViewModel {

    private String TAG = ILikedMostViewModel.class.getSimpleName();
    private ILikedMostService iLikedMostService;
    private Context mContext;
    private InstagramSession mInstagramSession;
    private Activity mActivity;

    public ILikedMostViewModel(ILikedMostService iLikedMostService, Context context, InstagramSession mInstagramSession) {
        this.iLikedMostService = iLikedMostService;
        mContext = context;
        this.mInstagramSession = mInstagramSession;
//        getILikedMost();
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

    public void getILikedMost() {
        if (mInstagramSession.isActive()) {
            if (isConnected()) {

                StringBuilder strBldrILikeMostUrl = new StringBuilder()
                        .append(Constants.WebFields.ENDPOINT_MEDIALIKED)
                        .append(mInstagramSession.getAccessToken());

                iLikedMostService.getILikedMost(new MyCallBack<ListResponseBean<ILikedMostBean>>() {
                    @Override
                    public void onSuccess(ListResponseBean<ILikedMostBean> response) {
                        Log.d(TAG, "getILikedMost:" + response.getData().size());
                        setILikedMostToDB(response.getData());
                    }

                    @Override
                    public void onError(String header, String message) {
                        Log.d(TAG, "getILikedMost:onError: header:" + header + " & message:" + message);
                        EventBus.getDefault().post(new ILikedMostEvent());
                    }
                }, strBldrILikeMostUrl.toString());
            } else {
                getILikedMostFromDB();
            }

        } else {
            Utility.showToast(mActivity, "Could not authentication, need to log in again");
            Intent intent = new Intent(mActivity, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mActivity.startActivity(intent);
            mActivity.finish();
        }
    }

    private void getILikedMostFromDB() {
        ILikeMostDBQueries iLikeMostDBQueries = new ILikeMostDBQueries(mContext);
        try {
            Utility.makeObservable(iLikeMostDBQueries.getILikedMost(mInstagramSession.getUser().id))
                    .subscribe(new Consumer<ArrayList<ILikedMostBean>>() {
                        @Override
                        public void accept(ArrayList<ILikedMostBean> iLikedMostBeen) throws Exception {
                            ILikedMostEvent iLikedMostEvent = new ILikedMostEvent();
                            iLikedMostEvent.setArylstILikedMost(iLikedMostBeen);
                            EventBus.getDefault().post(iLikedMostEvent);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setILikedMostToDB(ArrayList<ILikedMostBean> arylstILikedMost) {
        ILikeMostDBQueries iLikeMostDBQueries = new ILikeMostDBQueries(mContext);
        for (int i = 0; i < arylstILikedMost.size(); i++) {
            ILikedMostBean iLikedMostBean = arylstILikedMost.get(i);
            iLikedMostBean.setLikedUserId(mInstagramSession.getUser().id);
            arylstILikedMost.set(i, iLikedMostBean);
        }
        try {
            Utility.makeObservable(iLikeMostDBQueries.saveILikedMost(arylstILikedMost))
                    .subscribe(new Consumer<ArrayList<ILikedMostBean>>() {
                        @Override
                        public void accept(ArrayList<ILikedMostBean> iLikedMostBeen) throws Exception {
                            ILikedMostEvent iLikedMostEvent = new ILikedMostEvent();
                            iLikedMostEvent.setArylstILikedMost(iLikedMostBeen);
                            EventBus.getDefault().post(iLikedMostEvent);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
