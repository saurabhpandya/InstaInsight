package com.instainsight.followersing.following.viewmodel;

import android.app.Activity;
import android.content.Context;

import com.instainsight.followersing.following.FollowingServices;
import com.instainsight.followersing.following.bean.FollowingBean;
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

public class FollowingViewModel extends BaseViewModel implements IViewModel {

    private String TAG = FollowingViewModel.class.getSimpleName();
    private Context mContext;
    private Activity mActivity;
    private InstagramSession mInstagramSession;
    private FollowingServices iFollowsServices;

    public FollowingViewModel(FollowingServices followingServices, Context context, InstagramSession mInstagramSession) {
        mContext = context;
        this.mInstagramSession = mInstagramSession;
        this.iFollowsServices = followingServices;
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

    public Observable<ArrayList<FollowingBean>> getFollows(String accessToken) {
        return iFollowsServices.getFollows(accessToken)
                .flatMap(new Function<ListResponseBean<FollowingBean>, Observable<ArrayList<FollowingBean>>>() {
                    @Override
                    public Observable<ArrayList<FollowingBean>> apply(ListResponseBean<FollowingBean> followsBeanList) throws Exception {
                        return Observable.just(followsBeanList.getData());
                    }
                });
    }

    public Single<List<FollowingBean>> getRelationShipStatus(final ArrayList<FollowingBean> arylstFollows) {

        return Observable.fromIterable(arylstFollows)
                .concatMap(new Function<FollowingBean, Observable<FollowingBean>>() {
                    @Override
                    public Observable<FollowingBean> apply(FollowingBean followsBean) throws Exception {
                        return Observable.zip(Observable.just(followsBean),
                                iFollowsServices.getRelationShipStatus(followsBean.getId(), mInstagramSession.getAccessToken()),
                                new BiFunction<FollowingBean, ObjectResponseBean<RelationShipStatus>, FollowingBean>() {
                                    @Override
                                    public FollowingBean apply(FollowingBean followsBean, ObjectResponseBean<RelationShipStatus> relationShipStatusBean) throws Exception {
                                        followsBean.setRelationShipStatus(relationShipStatusBean.getData());
                                        return followsBean;
                                    }
                                });
                    }
                }).toList();
    }

    public Observable<ObjectResponseBean<RelationShipStatus>> changeRelationshipStatus(String action,
                                                                                       String userId,
                                                                                       String accessToken) {
        return iFollowsServices.setRelationShipStatus(action, userId, accessToken);
    }
}
