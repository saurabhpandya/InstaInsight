package com.instainsight.mytoplikers.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.instainsight.Utils.Utility;
import com.instainsight.constants.Constants;
import com.instainsight.instagram.InstagramSession;
import com.instainsight.login.LoginActivity;
import com.instainsight.media.RecentMediaService;
import com.instainsight.media.models.MediaBean;
import com.instainsight.models.ListResponseBean;
import com.instainsight.models.UserBean;
import com.instainsight.mytoplikers.MyTopLikersEvent;
import com.instainsight.viewmodels.BaseViewModel;
import com.instainsight.viewmodels.IViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by SONY on 18-02-2017.
 */

public class MyTopLikersViewModel extends BaseViewModel implements IViewModel {

    private String TAG = MyTopLikersViewModel.class.getSimpleName();
    private Context mContext;
    private RecentMediaService recentMediaService;
    private InstagramSession mInstagramSession;
    private Activity mActivity;

    public MyTopLikersViewModel(RecentMediaService recentMediaService, Context context, InstagramSession mInstagramSession) {
        this.recentMediaService = recentMediaService;
        mContext = context;
        this.mInstagramSession = mInstagramSession;
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

    public void getRecentMediaToGetTopLikers() {
        if (mInstagramSession.isActive()) {
            if (isConnected()) {
                StringBuilder strBldrILikeMostUrl = new StringBuilder()
                        .append(Constants.WebFields.ENDPOINT_RECENT_MEDIA_DAG)
                        .append(mInstagramSession.getAccessToken());
//                recentMediaService.getMediaToGetTopLikers(strBldrILikeMostUrl.toString())
//                        .subscribe(new Consumer<ListResponseBean<MediaBean>>() {
//                            @Override
//                            public void accept(ListResponseBean<MediaBean> arylstMediaToGetTopLikers) throws Exception {
//                                Log.d(TAG, "arylstMediaToGetTopLikers.getData().size()::"
//                                        + arylstMediaToGetTopLikers.getData().size());
////                                recentMediaService
//
//                            }
//                        }, new Consumer<Throwable>() {
//                            @Override
//                            public void accept(Throwable throwable) throws Exception {
//                                throwable.printStackTrace();
//                            }
//                        });

                recentMediaService.getMediaToGetTopLikers(mInstagramSession.getAccessToken())/*.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())*/
                        .concatMap(new Function<ListResponseBean<MediaBean>, Observable<ListResponseBean<UserBean>>>() {
                            @Override
                            public Observable<ListResponseBean<UserBean>> apply(ListResponseBean<MediaBean> mediaBeanListResponseBean) throws Exception {
                                Log.d(TAG, "mediaBeanListResponseBean::" + mediaBeanListResponseBean.getData().size());

                                ArrayList<Observable<ListResponseBean<UserBean>>> arylstUsersObservable = new ArrayList<Observable<ListResponseBean<UserBean>>>();


//                                final ArrayList<UserBean> arylstTopLikers = new ArrayList<UserBean>();
                                for (MediaBean mediaBean : mediaBeanListResponseBean.getData()) {
                                    recentMediaService.getRecentMediaTopLikers(mediaBean.getMediaId(), mInstagramSession.getAccessToken())
                                            .subscribe(new Consumer<ListResponseBean<UserBean>>() {
                                                @Override
                                                public void accept(ListResponseBean<UserBean> userBeanListResponseBean) throws Exception {
                                                    Log.d(TAG, "userBeanListResponseBean::" + userBeanListResponseBean.getData().size());
                                                    if (userBeanListResponseBean.getData().size() > 0) {
                                                        for (UserBean userBean : userBeanListResponseBean.getData()) {
//                                                            ArrayList<UserBean> arylstTopLikers = new ArrayList<UserBean>();
//                                                            arylstTopLikers.add(userBean);
                                                            Log.d(TAG, "userBean.getFull_name()::" + userBean.getFull_name());
                                                            MyTopLikersEvent myTopLikersEvent = new MyTopLikersEvent();
                                                            myTopLikersEvent.setTopLiker(userBean);
                                                            EventBus.getDefault().post(myTopLikersEvent);
                                                        }
                                                    }
                                                }
                                            });

                                }

                                return Observable.just(new ListResponseBean<UserBean>());
//                                return recentMediaService.getRecentMediaTopLikers(mediaBeanListResponseBean.getData().get(0).getMediaId(), mInstagramSession.getAccessToken());
//                                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
                            }
                        }).subscribe(new Consumer<ListResponseBean<UserBean>>() {
                    @Override
                    public void accept(ListResponseBean<UserBean> userBeanListResponseBean) throws Exception {
                        if (userBeanListResponseBean != null && userBeanListResponseBean.getData() != null)
                            Log.d(TAG, "accept:userBeanListResponseBean::" + userBeanListResponseBean.getData().size());
                    }
                });

            } else {
                MyTopLikersEvent myTopLikersEvent = new MyTopLikersEvent();
                EventBus.getDefault().post(myTopLikersEvent);
            }
        } else {
            Utility.showToast(mActivity, "Could not authentication, need to log in again");
            Intent intent = new Intent(mActivity, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mActivity.startActivity(intent);
            mActivity.finish();
        }
    }


}
