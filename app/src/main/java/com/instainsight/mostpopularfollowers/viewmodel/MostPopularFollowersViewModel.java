package com.instainsight.mostpopularfollowers.viewmodel;

import android.app.Activity;
import android.content.Context;

import com.instainsight.db.PaidFollowedByDBQueries;
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

//    .flatMap(new Function<FollowerBean, Observable<ObjectResponseBean<RelationShipStatus>>>() {
//        @Override
//        public Observable<ObjectResponseBean<RelationShipStatus>> apply(FollowerBean followerBean) throws Exception {
//            return Observable.zip(mostPopularFollowersServices.getRelationShipStatus(followerBean.getId(), mInstagramSession.getAccessToken())
//                    followerBean,
//                    new BiFunction<ObjectResponseBean<RelationShipStatus>, FollowerBean, Long>() {
//                        @Override
//                        public Long apply(ObjectResponseBean<RelationShipStatus> relationShipStatusBean,
//                                          FollowerBean followerBean) throws Exception {
//                            RelationShipStatus relationShipStatus = relationShipStatusBean.getData();
//                            long updatedRows = -1;
//                            if (null != relationShipStatus.getTarget_user_is_private()) {
//                                PaidFollowedByDBQueries paidFollowedByDBQueries = new PaidFollowedByDBQueries(mContext);
//                                updatedRows = paidFollowedByDBQueries.updatePrivateUser(followerBean.getId());
//                            }
//                            return updatedRows;
//                        }
//
//                    }
//            )

    public Single<List<Long>> getMostPopularFollowers() {
        return mostPopularFollowersServices.getFollowers(mInstagramSession.getAccessToken())
                .flatMap(new Function<ListResponseBean<FollowerBean>, Observable<FollowerBean>>() {
                    @Override
                    public Observable<FollowerBean> apply(ListResponseBean<FollowerBean> followerListBean) throws Exception {
                        PaidFollowedByDBQueries paidFollowedByDBQueries = new PaidFollowedByDBQueries(mContext);
                        paidFollowedByDBQueries.saveFollowedBy(followerListBean.getData());
                        return Observable.fromIterable(followerListBean.getData());
                    }
                }).flatMap(new Function<FollowerBean, Observable<Long>>() {
                    @Override
                    public Observable<Long> apply(FollowerBean followerBean) throws Exception {
                        return Observable.zip(mostPopularFollowersServices.
                                        getRelationShipStatus(followerBean.getId(), mInstagramSession.getAccessToken()),
                                Observable.just(followerBean), new BiFunction<ObjectResponseBean<RelationShipStatus>, FollowerBean, Long>() {
                                    @Override
                                    public Long apply(ObjectResponseBean<RelationShipStatus> relationShipStatusBean,
                                                      FollowerBean followerBean) throws Exception {
                                        RelationShipStatus relationShipStatus = relationShipStatusBean.getData();
                                        long updatedRows = -1;
                                        if (relationShipStatus.getTarget_user_is_private()) {
                                            PaidFollowedByDBQueries paidFollowedByDBQueries = new PaidFollowedByDBQueries(mContext);
                                            updatedRows = paidFollowedByDBQueries.updatePrivateUser(followerBean.getId());
                                        }
                                        return updatedRows;
                                    }
                                });
                    }
                }).flatMap(new Function<Long, Observable<FollowerBean>>() {
                    @Override
                    public Observable<FollowerBean> apply(Long aLong) throws Exception {
                        PaidFollowedByDBQueries paidFollowedByDBQueries = new PaidFollowedByDBQueries(mContext);
                        ArrayList<FollowerBean> nonPrivateFollowers = paidFollowedByDBQueries.getNonPrivateFollowedBy();

                        return Observable.fromIterable(nonPrivateFollowers);
                    }
                }).flatMap(new Function<FollowerBean, Observable<ObjectResponseBean<OtherUsersBean>>>() {
                    @Override
                    public Observable<ObjectResponseBean<OtherUsersBean>> apply(FollowerBean followerBean) throws Exception {
                        return mostPopularFollowersServices.getFollowersInfo(followerBean.getId(), mInstagramSession.getAccessToken());
                    }
                }).flatMap(new Function<ObjectResponseBean<OtherUsersBean>, Observable<Long>>() {
                    @Override
                    public Observable<Long> apply(ObjectResponseBean<OtherUsersBean> otherUsersBeanBean) throws Exception {
                        long updatedOtherUserProfileRows = -1;
                        PaidFollowedByDBQueries paidFollowedByDBQueries = new PaidFollowedByDBQueries(mContext);
                        updatedOtherUserProfileRows = paidFollowedByDBQueries.updateFollowedByInfo(otherUsersBeanBean.getData());
                        return Observable.just(updatedOtherUserProfileRows);
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
