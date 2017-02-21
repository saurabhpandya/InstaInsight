package com.instainsight.mostpopularfollowers.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.instainsight.Utils.Utility;
import com.instainsight.followersing.followers.bean.FollowerBean;
import com.instainsight.followersing.models.OtherUsersBean;
import com.instainsight.instagram.InstagramSession;
import com.instainsight.login.LoginActivity;
import com.instainsight.models.ListResponseBean;
import com.instainsight.models.ObjectResponseBean;
import com.instainsight.mostpopularfollowers.MostPopularFollowersEvent;
import com.instainsight.mostpopularfollowers.MostPopularFollowersServices;
import com.instainsight.viewmodels.BaseViewModel;
import com.instainsight.viewmodels.IViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by SONY on 19-02-2017.
 */

public class MostPopularFollowersViewModel extends BaseViewModel implements IViewModel {

    MostPopularFollowersServices mostPopularFollowersServices;
    private String TAG = MostPopularFollowersViewModel.class.getSimpleName();
    private Context mContext;
    private Activity mActivity;
    private InstagramSession mInstagramSession;

    public MostPopularFollowersViewModel(MostPopularFollowersServices mostPopularFollowersServices,
                                         Context context, InstagramSession mInstagramSession) {
        this.mostPopularFollowersServices = mostPopularFollowersServices;
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

    public void getMostPopularFollowers() {
        if (mInstagramSession.isActive()) {
            if (isConnected()) {
                mostPopularFollowersServices.getFollowers(mInstagramSession.getAccessToken())
                        .concatMap(new Function<ListResponseBean<FollowerBean>,
                                Observable<ObjectResponseBean<OtherUsersBean>>>() {
                            @Override
                            public Observable<ObjectResponseBean<OtherUsersBean>> apply(
                                    ListResponseBean<FollowerBean> followerBeanListResponseBean) throws Exception {
                                Log.d(TAG, "followerBeanListResponseBean::" + followerBeanListResponseBean.getData().size());

                                ArrayList<Observable<ListResponseBean<OtherUsersBean>>> arylstOtherUsersObservable
                                        = new ArrayList<Observable<ListResponseBean<OtherUsersBean>>>();

                                for (FollowerBean followerBean : followerBeanListResponseBean.getData()) {
                                    mostPopularFollowersServices.getFollowersInfo(followerBean.getId()
                                            , mInstagramSession.getAccessToken())
                                            .subscribe(new Consumer<ObjectResponseBean<OtherUsersBean>>() {
                                                @Override
                                                public void accept(ObjectResponseBean<OtherUsersBean> otherUsersBeanListResponseBean)
                                                        throws Exception {
                                                    Log.d(TAG, "otherUsersBeanListResponseBean::"
                                                            + otherUsersBeanListResponseBean.getData().getId());
                                                    ArrayList<OtherUsersBean> otherUsersBeanList = new ArrayList<OtherUsersBean>();
                                                    otherUsersBeanList.add(otherUsersBeanListResponseBean.getData());
                                                    MostPopularFollowersEvent mostPopularFollowersEvent
                                                            = new MostPopularFollowersEvent();
                                                    mostPopularFollowersEvent.setMostPopularFollowers(otherUsersBeanList);
                                                    EventBus.getDefault().post(mostPopularFollowersEvent);
                                                }
                                            }, new Consumer<Throwable>() {
                                                @Override
                                                public void accept(Throwable throwable) throws Exception {
                                                    throwable.printStackTrace();
                                                    MostPopularFollowersEvent mostPopularFollowersEvent
                                                            = new MostPopularFollowersEvent();
                                                    EventBus.getDefault().post(mostPopularFollowersEvent);
                                                }
                                            });
                                }

                                return Observable.just(new ObjectResponseBean<OtherUsersBean>());
                            }
                        }).subscribe(new Consumer<ObjectResponseBean<OtherUsersBean>>() {
                    @Override
                    public void accept(ObjectResponseBean<OtherUsersBean> otherUsersBeanListResponseBean) throws Exception {

                    }
                });
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
}
