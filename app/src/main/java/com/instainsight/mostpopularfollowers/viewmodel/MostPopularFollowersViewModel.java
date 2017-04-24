package com.instainsight.mostpopularfollowers.viewmodel;

import android.app.Activity;
import android.content.Context;

import com.instainsight.followersing.followers.bean.FollowerBean;
import com.instainsight.followersing.models.OtherUsersBean;
import com.instainsight.instagram.InstagramSession;
import com.instainsight.models.ListResponseBean;
import com.instainsight.models.ObjectResponseBean;
import com.instainsight.models.RelationShipStatus;
import com.instainsight.mostpopularfollowers.MostPopularFollowersServices;
import com.instainsight.viewmodels.BaseViewModel;
import com.instainsight.viewmodels.IViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
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

    public Single<List<OtherUsersBean>> getMostPopularFollowers() {

        return mostPopularFollowersServices.getFollowers(mInstagramSession.getAccessToken())
                .concatMap(new Function<ListResponseBean<FollowerBean>, Observable<FollowerBean>>() {
                    @Override
                    public Observable<FollowerBean> apply(ListResponseBean<FollowerBean> followerListBean) throws Exception {
                        return Observable.fromIterable(followerListBean.getData());
                    }
                })
                .concatMap(new Function<FollowerBean, Observable<ObjectResponseBean<OtherUsersBean>>>() {
                    @Override
                    public Observable<ObjectResponseBean<OtherUsersBean>> apply(FollowerBean followerBean) throws Exception {
                        return mostPopularFollowersServices.getFollowersInfo(followerBean.getId(),
                                mInstagramSession.getAccessToken());
                    }
                })
                .concatMap(new Function<ObjectResponseBean<OtherUsersBean>, Observable<OtherUsersBean>>() {
                    @Override
                    public Observable<OtherUsersBean> apply(ObjectResponseBean<OtherUsersBean> otherUsersBeanObjectResponseBean) throws Exception {
                        return Observable.just(otherUsersBeanObjectResponseBean.getData());
                    }
                }).toList();
    }

    public Single<List<OtherUsersBean>> getRelationShipStatus(final ArrayList<OtherUsersBean> arylstIlikeMost) {

        return Observable.fromIterable(arylstIlikeMost)
                .concatMap(new Function<OtherUsersBean, Observable<OtherUsersBean>>() {
                    @Override
                    public Observable<OtherUsersBean> apply(OtherUsersBean otherUsersBean) throws Exception {
                        return Observable.zip(Observable.just(otherUsersBean),
                                mostPopularFollowersServices.getRelationShipStatus(otherUsersBean.getId(), mInstagramSession.getAccessToken()),
                                new BiFunction<OtherUsersBean, ObjectResponseBean<RelationShipStatus>, OtherUsersBean>() {
                                    @Override
                                    public OtherUsersBean apply(OtherUsersBean popularFollowerBean, ObjectResponseBean<RelationShipStatus> relationShipStatusBean) throws Exception {
                                        popularFollowerBean.setRelationShipStatus(relationShipStatusBean.getData());
                                        return popularFollowerBean;
                                    }
                                });
                    }
                }).toList();
    }

    public Observable<ObjectResponseBean<RelationShipStatus>> changeRelationshipStatus(String action,
                                                                                       String userId,
                                                                                       String accessToken) {
        return mostPopularFollowersServices.setRelationShipStatus(action, userId, accessToken);
    }

}
