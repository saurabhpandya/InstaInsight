package com.instainsight.iamnotfollowingback.viewmodel;

import android.app.Activity;
import android.content.Context;

import com.instainsight.followersing.followers.bean.FollowerBean;
import com.instainsight.followersing.following.bean.FollowingBean;
import com.instainsight.iamnotfollowingback.NotFollowingBackDBQueries;
import com.instainsight.iamnotfollowingback.NotFollowingBackServices;
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
 * Created by SONY on 08-04-2017.
 */

public class NotFollowingBackViewModel extends BaseViewModel implements IViewModel {

    private String TAG = NotFollowingBackViewModel.class.getSimpleName();
    private NotFollowingBackServices notFollowingBackServices;
    private Context mContext;
    private InstagramSession mInstagramSession;
    private Activity mActivity;

    public NotFollowingBackViewModel(NotFollowingBackServices notFollowingBackServices, Context context, InstagramSession mInstagramSession) {
        this.notFollowingBackServices = notFollowingBackServices;
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

    public Observable<ArrayList<FollowerBean>> getNotFollowingBack() {
        return notFollowingBackServices.getFollowedBy(mInstagramSession.getAccessToken())
                .flatMap(new Function<ListResponseBean<FollowerBean>, Observable<ListResponseBean<FollowingBean>>>() {
                    @Override
                    public Observable<ListResponseBean<FollowingBean>> apply(ListResponseBean<FollowerBean> followedByListResponseBean) throws Exception {
                        NotFollowingBackDBQueries notFollowingBackDBQueries = new NotFollowingBackDBQueries(mContext);
                        notFollowingBackDBQueries.deleteNotFollowingBack();
                        notFollowingBackDBQueries.saveFollowedBy(followedByListResponseBean.getData());
                        return notFollowingBackServices.getFollows(mInstagramSession.getAccessToken());
                    }
                })
                .flatMap(new Function<ListResponseBean<FollowingBean>, Observable<ArrayList<FollowerBean>>>() {
                    @Override
                    public Observable<ArrayList<FollowerBean>> apply(ListResponseBean<FollowingBean> followingBeanListResponseBean) throws Exception {
                        NotFollowingBackDBQueries notFollowingBackDBQueries = new NotFollowingBackDBQueries(mContext);
                        notFollowingBackDBQueries.saveFollows(followingBeanListResponseBean.getData());
                        return notFollowingBackDBQueries.getNotFollowingBackFromDB();
                    }
                });
    }

    public Single<List<FollowerBean>> getRelationShipStatus(final ArrayList<FollowerBean> arylstFollowedBy) {

        return Observable.fromIterable(arylstFollowedBy)
                .concatMap(new Function<FollowerBean, Observable<FollowerBean>>() {
                    @Override
                    public Observable<FollowerBean> apply(FollowerBean followedByBean) throws Exception {
                        return Observable.zip(Observable.just(followedByBean),
                                notFollowingBackServices.getRelationShipStatus(followedByBean.getId(), mInstagramSession.getAccessToken()),
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
        return notFollowingBackServices.setRelationShipStatus(action, userId, accessToken);
    }
}
