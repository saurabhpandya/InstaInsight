package com.instainsight.unfollowers.viewmodel;

import android.app.Activity;
import android.content.Context;

import com.instainsight.followersing.followers.bean.FollowerBean;
import com.instainsight.followersing.following.bean.FollowingBean;
import com.instainsight.instagram.InstagramSession;
import com.instainsight.models.ListResponseBean;
import com.instainsight.models.ObjectResponseBean;
import com.instainsight.models.RelationShipStatus;
import com.instainsight.unfollowers.UnFollowersDBQueries;
import com.instainsight.unfollowers.UnFollowersServices;
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

public class UnFollowersViewModel extends BaseViewModel implements IViewModel {

    private String TAG = UnFollowersViewModel.class.getSimpleName();
    private UnFollowersServices unFollowersServices;
    private Context mContext;
    private InstagramSession mInstagramSession;
    private Activity mActivity;

    public UnFollowersViewModel(UnFollowersServices unFollowersServices, Context context, InstagramSession mInstagramSession) {
        this.unFollowersServices = unFollowersServices;
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

    public Observable<ArrayList<FollowerBean>> getUnFollowers() {
        return unFollowersServices.getFollowedBy(mInstagramSession.getAccessToken())
                .flatMap(new Function<ListResponseBean<FollowerBean>, Observable<ListResponseBean<FollowingBean>>>() {
                    @Override
                    public Observable<ListResponseBean<FollowingBean>> apply(ListResponseBean<FollowerBean> followedByListResponseBean) throws Exception {
                        UnFollowersDBQueries unFollowersDBQueries = new UnFollowersDBQueries(mContext);
                        unFollowersDBQueries.deleteUnFollowers();
                        unFollowersDBQueries.saveFollowedBy(followedByListResponseBean.getData());
                        return unFollowersServices.getFollows(mInstagramSession.getAccessToken());
                    }
                })
                .flatMap(new Function<ListResponseBean<FollowingBean>, Observable<ArrayList<FollowerBean>>>() {
                    @Override
                    public Observable<ArrayList<FollowerBean>> apply(ListResponseBean<FollowingBean> followingBeanListResponseBean) throws Exception {
                        UnFollowersDBQueries unFollowersDBQueries = new UnFollowersDBQueries(mContext);
                        unFollowersDBQueries.saveFollows(followingBeanListResponseBean.getData());
                        return unFollowersDBQueries.getUnFollowersFromDB();
                    }
                });
    }

    public Single<List<FollowerBean>> getRelationShipStatus(final ArrayList<FollowerBean> arylstFollowedBy) {

        return Observable.fromIterable(arylstFollowedBy)
                .concatMap(new Function<FollowerBean, Observable<FollowerBean>>() {
                    @Override
                    public Observable<FollowerBean> apply(FollowerBean followedByBean) throws Exception {
                        return Observable.zip(Observable.just(followedByBean),
                                unFollowersServices.getRelationShipStatus(followedByBean.getId(), mInstagramSession.getAccessToken()),
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
        return unFollowersServices.setRelationShipStatus(action, userId, accessToken);
    }
}
