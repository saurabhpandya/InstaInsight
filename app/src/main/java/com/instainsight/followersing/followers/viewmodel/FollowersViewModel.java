package com.instainsight.followersing.followers.viewmodel;

import android.app.Activity;
import android.content.Context;

import com.instainsight.followersing.followers.FollowersServices;
import com.instainsight.followersing.followers.bean.FollowerBean;
import com.instainsight.instagram.InstagramSession;
import com.instainsight.models.ListResponseBean;
import com.instainsight.models.ObjectResponseBean;
import com.instainsight.models.RelationShipStatus;
import com.instainsight.viewmodels.BaseViewModel;
import com.instainsight.viewmodels.IViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * Created by SONY on 28-03-2017.
 */

public class FollowersViewModel extends BaseViewModel implements IViewModel {

    private String TAG = FollowersViewModel.class.getSimpleName();
    private Context mContext;
    private Activity mActivity;
    private InstagramSession mInstagramSession;
    private FollowersServices iFollowersServices;

    public FollowersViewModel(FollowersServices followersServices, Context context, InstagramSession mInstagramSession) {
        mContext = context;
        this.mInstagramSession = mInstagramSession;
        this.iFollowersServices = followersServices;
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

    public Observable<ArrayList<FollowerBean>> getFollowedBy(String accessToken) {
        return iFollowersServices.getFollowedBy(accessToken)
                .flatMap(new Function<ListResponseBean<FollowerBean>, Observable<ArrayList<FollowerBean>>>() {
                    @Override
                    public Observable<ArrayList<FollowerBean>> apply(ListResponseBean<FollowerBean> followerBeanList) throws Exception {
                        return Observable.just(followerBeanList.getData());
                    }
                });
    }

    public Single<List<FollowerBean>> getRelationShipStatus(final ArrayList<FollowerBean> arylstFollowedBy) {

        return Observable.fromIterable(arylstFollowedBy)
                .concatMap(new Function<FollowerBean, Observable<FollowerBean>>() {
                    @Override
                    public Observable<FollowerBean> apply(FollowerBean followedByBean) throws Exception {
                        return Observable.zip(Observable.just(followedByBean),
                                iFollowersServices.getRelationShipStatus(followedByBean.getId(), mInstagramSession.getAccessToken()),
                                new BiFunction<FollowerBean, ObjectResponseBean<RelationShipStatus>, FollowerBean>() {
                                    @Override
                                    public FollowerBean apply(FollowerBean followedByBean, ObjectResponseBean<RelationShipStatus> relationShipStatusBean) throws Exception {
                                        followedByBean.setRelationShipStatus(relationShipStatusBean.getData());
                                        return followedByBean;
                                    }
                                });
                    }
                }).toList();
    }

    public Observable<ObjectResponseBean<RelationShipStatus>> changeRelationshipStatus(String action,
                                                                                       String userId,
                                                                                       String accessToken) {
        return iFollowersServices.setRelationShipStatus(action, userId, accessToken);
    }
}
