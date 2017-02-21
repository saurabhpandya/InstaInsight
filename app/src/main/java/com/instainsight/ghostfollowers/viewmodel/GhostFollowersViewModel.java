package com.instainsight.ghostfollowers.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.instainsight.Utils.Utility;
import com.instainsight.followersing.followers.bean.FollowerBean;
import com.instainsight.ghostfollowers.GhostFollowersEvent;
import com.instainsight.ghostfollowers.GhostFollowersServices;
import com.instainsight.ghostfollowers.model.CommentsBean;
import com.instainsight.ghostfollowers.model.LikesBean;
import com.instainsight.instagram.InstagramSession;
import com.instainsight.login.LoginActivity;
import com.instainsight.media.models.MediaBean;
import com.instainsight.models.ListResponseBean;
import com.instainsight.viewmodels.BaseViewModel;
import com.instainsight.viewmodels.IViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by SONY on 19-02-2017.
 */

public class GhostFollowersViewModel extends BaseViewModel implements IViewModel {

    private String TAG = GhostFollowersViewModel.class.getSimpleName();
    private GhostFollowersServices ghostFollowersServices;
    private Context mContext;
    private Activity mActivity;
    private InstagramSession mInstagramSession;

    public GhostFollowersViewModel(GhostFollowersServices ghostFollowersServices, Context context, InstagramSession mInstagramSession) {
        this.ghostFollowersServices = ghostFollowersServices;
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

    public void getGhostFollowers() {
        if (mInstagramSession.isActive()) {
            if (isConnected()) {
                ghostFollowersServices.getFollowedBy(mInstagramSession.getAccessToken())
                        .subscribe(new Consumer<ListResponseBean<FollowerBean>>() {
                            @Override
                            public void accept(ListResponseBean<FollowerBean> followerBeanListResponseBean) throws Exception {
                                if (followerBeanListResponseBean.getData().size() > 0) {
                                    ArrayList<FollowerBean> arylstFollowers = new ArrayList<FollowerBean>();
                                    arylstFollowers.addAll(followerBeanListResponseBean.getData());
                                    GhostFollowersEvent ghostFollowersEvent
                                            = new GhostFollowersEvent();
                                    ghostFollowersEvent.setArylstFollowers(arylstFollowers);
                                    EventBus.getDefault().post(ghostFollowersEvent);
                                }
                            }
                        });

                ghostFollowersServices.getRecentMedia(mInstagramSession.getAccessToken())
                        .flatMap(new Function<ListResponseBean<MediaBean>, Observable<MediaBean>>() {
                            @Override
                            public Observable<MediaBean> apply(ListResponseBean<MediaBean> mediaBeanListResponseBean) throws Exception {
                                return Observable.fromIterable(mediaBeanListResponseBean.getData());
                            }
                        })
                        .flatMap(new Function<MediaBean, Observable<ArrayList<FollowerBean>>>() {
                            @Override
                            public Observable<ArrayList<FollowerBean>> apply(MediaBean mediaBean) throws Exception {
                                Log.d(TAG, "mediaBean:" + mediaBean.getMediaId());
                                return Observable.zip(ghostFollowersServices.getCommentsByMediaId(mediaBean.getMediaId()
                                        , mInstagramSession.getAccessToken()),
                                        ghostFollowersServices.getLikesByMediaId(mediaBean.getMediaId()
                                                , mInstagramSession.getAccessToken()),
                                        new BiFunction<ListResponseBean<CommentsBean>, ListResponseBean<LikesBean>, ArrayList<FollowerBean>>() {
                                            @Override
                                            public ArrayList<FollowerBean>
                                            apply(ListResponseBean<CommentsBean> commentsBeanListResponseBean,
                                                  ListResponseBean<LikesBean> likesBeanListResponseBean) throws Exception {
                                                Log.d(TAG, "commentsBeanListResponseBean new:" + commentsBeanListResponseBean.getData().size());
                                                Log.d(TAG, "likesBeanListResponseBean new:" + likesBeanListResponseBean.getData().size());
                                                ArrayList<FollowerBean> arylstFollowersBean = new ArrayList<FollowerBean>();

                                                for (CommentsBean commentsBean : commentsBeanListResponseBean.getData()) {
                                                    Log.d(TAG, "commentsBean new:" + commentsBean.getFrom().getId());
                                                    FollowerBean followerBean = new FollowerBean();
                                                    followerBean.setId(commentsBean.getFrom().getId());
                                                    followerBean.setFullName(commentsBean.getFrom().getFull_name());
                                                    followerBean.setProfilePic(commentsBean.getFrom().getProfile_picture());
                                                    followerBean.setUserName(commentsBean.getFrom().getUsername());
                                                    arylstFollowersBean.add(followerBean);
                                                }

                                                for (LikesBean likesBean : likesBeanListResponseBean.getData()) {
                                                    Log.d(TAG, "likesBean new:" + likesBean.getId());
                                                    FollowerBean followerBean = new FollowerBean();
                                                    followerBean.setId(likesBean.getId());
                                                    followerBean.setFullName(likesBean.getFirst_name() + " " + likesBean.getLast_name());
                                                    followerBean.setProfilePic("");
                                                    followerBean.setUserName(likesBean.getUsername());
                                                    arylstFollowersBean.add(followerBean);
                                                }

                                                return arylstFollowersBean;
                                            }
                                        });
                            }
                        }).subscribe(new Consumer<ArrayList<FollowerBean>>() {
                    @Override
                    public void accept(ArrayList<FollowerBean> followerBeen) throws Exception {
                        Log.d(TAG, "followerBeen new:" + followerBeen.size());
                        GhostFollowersEvent ghostFollowersEvent
                                = new GhostFollowersEvent();
                        ghostFollowersEvent.setArylstLikesCommentsFollowers(followerBeen);
                        EventBus.getDefault().post(ghostFollowersEvent);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });

//                ghostFollowersServices.getRecentMedia(mInstagramSession.getAccessToken())
//                        .concatMap(new Function<ListResponseBean<MediaBean>, Observable<ListResponseBean<LikesBean>>>() {
//                            @Override
//                            public Observable<ListResponseBean<LikesBean>> apply(
//                                    ListResponseBean<MediaBean> mediaBeanListResponseBean) throws Exception {
//                                Log.d(TAG, "mediaBeanListResponseBean:" + mediaBeanListResponseBean.getData().size());
//
//                                for (MediaBean mediaBean : mediaBeanListResponseBean.getData()) {
//
//                                    ghostFollowersServices.getCommentsByMediaId(mediaBean.getMediaId()
//                                            , mInstagramSession.getAccessToken())
//                                            .subscribe(new Consumer<ListResponseBean<CommentsBean>>() {
//                                                @Override
//                                                public void accept(ListResponseBean<CommentsBean> commentsBeanListResponseBean) throws Exception {
//                                                    Log.d(TAG, "commentsBeanListResponseBean:"
//                                                            + commentsBeanListResponseBean.getData().size());
//                                                    if (commentsBeanListResponseBean.getData().size() > 0) {
//                                                        ArrayList<CommentsBean> arylstComments = new ArrayList<CommentsBean>();
//                                                        arylstComments.addAll(commentsBeanListResponseBean.getData());
//                                                        GhostFollowersEvent ghostFollowersEvent
//                                                                = new GhostFollowersEvent();
//                                                        ghostFollowersEvent.setArylstComments(arylstComments);
//                                                        EventBus.getDefault().post(ghostFollowersEvent);
//                                                    }
//                                                }
//                                            });
//
//                                    ghostFollowersServices.getLikesByMediaId(mediaBean.getMediaId()
//                                            , mInstagramSession.getAccessToken())
//                                            .subscribe(new Consumer<ListResponseBean<LikesBean>>() {
//                                                @Override
//                                                public void accept(ListResponseBean<LikesBean> likesBeanListResponseBean) throws Exception {
//                                                    Log.d(TAG, "likesBeanListResponseBean:" + likesBeanListResponseBean.getData().size());
//                                                    if (likesBeanListResponseBean.getData().size() > 0) {
//                                                        ArrayList<LikesBean> arylstLikes = new ArrayList<LikesBean>();
//                                                        arylstLikes.addAll(likesBeanListResponseBean.getData());
//                                                        GhostFollowersEvent ghostFollowersEvent
//                                                                = new GhostFollowersEvent();
//                                                        ghostFollowersEvent.setArylstLikes(arylstLikes);
//                                                        EventBus.getDefault().post(ghostFollowersEvent);
//                                                    }
//                                                }
//                                            });
//                                }
//
//
//                                return Observable.just(new ListResponseBean<LikesBean>());
//                            }
//                        }).subscribe(new Consumer<ListResponseBean<LikesBean>>() {
//                    @Override
//                    public void accept(ListResponseBean<LikesBean> likesBeanListResponseBean) throws Exception {
//                        if (likesBeanListResponseBean.getData() != null)
//                            Log.d(TAG, "likesBeanListResponseBean:" + likesBeanListResponseBean.getData().size());
//                    }
//                });

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
