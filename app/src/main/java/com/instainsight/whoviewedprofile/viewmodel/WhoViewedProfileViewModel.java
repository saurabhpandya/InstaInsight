package com.instainsight.whoviewedprofile.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.instainsight.followersing.followers.bean.FollowerBean;
import com.instainsight.ghostfollowers.model.CommentsBean;
import com.instainsight.ghostfollowers.model.LikesBean;
import com.instainsight.instagram.InstagramSession;
import com.instainsight.media.models.MediaBean;
import com.instainsight.models.ListResponseBean;
import com.instainsight.models.UserBean;
import com.instainsight.models.UsersInPhotoBean;
import com.instainsight.viewmodels.BaseViewModel;
import com.instainsight.viewmodels.IViewModel;
import com.instainsight.whoviewedprofile.ProfileViewerDBQueries;
import com.instainsight.whoviewedprofile.WhoViewedProfileEvent;
import com.instainsight.whoviewedprofile.WhoViewedProfileService;
import com.instainsight.whoviewedprofile.model.WhoViewedProfileBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by SONY on 24-02-2017.
 */

public class WhoViewedProfileViewModel extends BaseViewModel implements IViewModel {

    private WhoViewedProfileService whoViewedProfileService;
    private Context mContext;
    private InstagramSession mInstagramSession;
    private Activity mActivity;
    private String TAG = WhoViewedProfileViewModel.class.getSimpleName();
    private ArrayList<UserBean> arylstTaggedByUser;
    private ArrayList<UserBean> arylstLikeUser;
    private ArrayList<UserBean> arylstCommentUser;
    private ArrayList<WhoViewedProfileBean> arylstWhoViewedProfile = new ArrayList<WhoViewedProfileBean>();
    private int recentMedia;
    private int count;

    public WhoViewedProfileViewModel(WhoViewedProfileService whoViewedProfileService,
                                     Context context, InstagramSession mInstagramSession) {
        this.whoViewedProfileService = whoViewedProfileService;
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

    public void getWhoViewedProfile() {

        arylstTaggedByUser = new ArrayList<>();
        arylstLikeUser = new ArrayList<>();
        arylstCommentUser = new ArrayList<>();
        whoViewedProfileService.getFollowedBy(mInstagramSession.getAccessToken())
                .flatMap(new Function<ListResponseBean<FollowerBean>, Observable<FollowerBean>>() {
                    @Override
                    public Observable<FollowerBean> apply(ListResponseBean<FollowerBean> followedByBeanListResponseBean)
                            throws Exception {
                        return Observable.fromIterable(followedByBeanListResponseBean.getData());
                    }
                })
                .flatMap(new Function<FollowerBean, Observable<ListResponseBean<MediaBean>>>() {
                    @Override
                    public Observable<ListResponseBean<MediaBean>> apply(FollowerBean followerBean) throws Exception {
                        return whoViewedProfileService.getFollowedByRecentPost(followerBean.getId()
                                , mInstagramSession.getAccessToken());
                    }
                })
                .flatMap(new Function<ListResponseBean<MediaBean>, Observable<MediaBean>>() {
                    @Override
                    public Observable<MediaBean> apply(ListResponseBean<MediaBean> mediaBeanListResponseBean) throws Exception {
                        return Observable.fromIterable(mediaBeanListResponseBean.getData());
                    }
                })
                .filter(new Predicate<MediaBean>() {
                    @Override
                    public boolean test(MediaBean mediaBean) throws Exception {
                        ArrayList<UsersInPhotoBean> arylstUsersInPhoto = mediaBean.getUsers_in_photo();
                        if (arylstUsersInPhoto.size() > 0) {
                            for (UsersInPhotoBean usersInPhotoBean : arylstUsersInPhoto) {
                                if (!usersInPhotoBean.getUserBean().getId()
                                        .equalsIgnoreCase(mInstagramSession.getUser().getUserBean().getId()))
                                    return true;
                            }
                        }
                        return false;
                    }
                })
//                .toList().observeOn(Schedulers.computation())
                .flatMap(new Function<MediaBean, Observable<ArrayList<UserBean>>>() {
                    @Override
                    public Observable<ArrayList<UserBean>> apply(MediaBean mediaBean) throws Exception {
                        arylstTaggedByUser.add(mediaBean.getUserBean());
                        return Observable.fromArray(arylstTaggedByUser);
                    }
                })
                .flatMap(new Function<ArrayList<UserBean>, Observable<ListResponseBean<MediaBean>>>() {
                    @Override
                    public Observable<ListResponseBean<MediaBean>> apply(ArrayList<UserBean> arylstTaggedByUser) throws Exception {
                        Log.d(TAG, "arylstTaggedByUser:" + arylstTaggedByUser.size());
                        return whoViewedProfileService.getUsersRecentPost(mInstagramSession.getAccessToken());
                    }
                })
                .flatMap(new Function<ListResponseBean<MediaBean>, Observable<MediaBean>>() {
                    @Override
                    public Observable<MediaBean> apply(ListResponseBean<MediaBean> mediaBeanListResponseBean) throws Exception {
                        recentMedia = mediaBeanListResponseBean.getData().size();
                        return Observable.fromIterable(mediaBeanListResponseBean.getData());
                    }
                })
                .flatMap(new Function<MediaBean, Observable<ArrayList<UserBean>>>() {
                    @Override
                    public Observable<ArrayList<UserBean>> apply(MediaBean mediaBean) throws Exception {

                        Log.d(TAG, "Media-Id" + mediaBean.getMediaId());
                        return Observable.zip(whoViewedProfileService.getRecentPostComments(mediaBean.getMediaId(), mInstagramSession.getAccessToken()),
                                whoViewedProfileService.getRecentPostLikes(mediaBean.getMediaId(), mInstagramSession.getAccessToken()),
                                new BiFunction<ListResponseBean<CommentsBean>, ListResponseBean<LikesBean>, ArrayList<UserBean>>() {
                                    @Override
                                    public ArrayList<UserBean> apply(ListResponseBean<CommentsBean> commentsBeanListResponseBean
                                            , ListResponseBean<LikesBean> likesBeanListResponseBean) throws Exception {

                                        Log.d(TAG, "commentsBeanListResponseBean:" + commentsBeanListResponseBean.getData().size());
                                        Log.d(TAG, "likesBeanListResponseBean:" + likesBeanListResponseBean.getData().size());
                                        for (CommentsBean commentsBean : commentsBeanListResponseBean.getData()) {
                                            if (!commentsBean.getFrom().getId().
                                                    equalsIgnoreCase(mInstagramSession.getUser().getUserBean().getId())) {
                                                arylstCommentUser.add(commentsBean.getFrom());
                                                Log.d(TAG, "CommentId:" + commentsBean.getText());
                                            }
                                        }

                                        for (LikesBean likesBean : likesBeanListResponseBean.getData()) {
                                            if (!likesBean.getId().equalsIgnoreCase(
                                                    mInstagramSession.getUser().getUserBean().getId())) {
                                                Log.d(TAG, "LikesId:" + likesBean.getId());
                                                UserBean userBean = new UserBean();
                                                userBean.setId(likesBean.getId());
                                                if (likesBean.getFirst_name() != null && likesBean.getLast_name() != null)
                                                    userBean.setFull_name(likesBean.getFirst_name() + " " + likesBean.getLast_name());
                                                else if (likesBean.getFull_name() != null)
                                                    userBean.setFull_name(likesBean.getFull_name());
                                                userBean.setUsername(likesBean.getUsername());
                                                arylstLikeUser.add(userBean);
                                            }
                                        }

//                                        if (arylstTaggedByUser.size() > 0) {
//                                            arylstUsersCommentsLikes.addAll(arylstTaggedByUser);
//                                        }

                                        return new ArrayList<UserBean>();
                                    }
                                });
                    }
                })
                .subscribe(new Consumer<ArrayList<UserBean>>() {
                    @Override
                    public void accept(ArrayList<UserBean> userBeen) throws Exception {

                        count++;
                        if (count == recentMedia) {

                            arylstWhoViewedProfile = new ArrayList<WhoViewedProfileBean>();

                            for (UserBean commentsBean : arylstCommentUser) {
                                WhoViewedProfileBean whoViewedProfileBean = new WhoViewedProfileBean();
                                whoViewedProfileBean.setId(commentsBean.getId());
                                whoViewedProfileBean.setFull_name(commentsBean.getFull_name());
                                whoViewedProfileBean.setProfile_picture(commentsBean.getProfile_picture());
                                whoViewedProfileBean.setUsername(commentsBean.getUsername());
                                whoViewedProfileBean.setType("comments");
                                whoViewedProfileBean.setPoints(3);
                                arylstWhoViewedProfile.add(whoViewedProfileBean);
                            }

                            for (UserBean likesBean : arylstLikeUser) {
                                WhoViewedProfileBean whoViewedProfileBean = new WhoViewedProfileBean();
                                whoViewedProfileBean.setId(likesBean.getId());
                                whoViewedProfileBean.setFull_name(likesBean.getFull_name());
                                whoViewedProfileBean.setProfile_picture(likesBean.getProfile_picture());
                                whoViewedProfileBean.setUsername(likesBean.getUsername());
                                whoViewedProfileBean.setType("likes");
                                whoViewedProfileBean.setPoints(2);
                                arylstWhoViewedProfile.add(whoViewedProfileBean);
                            }

                            for (UserBean userBean : arylstTaggedByUser) {
                                WhoViewedProfileBean whoViewedProfileBean = new WhoViewedProfileBean();
                                whoViewedProfileBean.setId(userBean.getId());
                                whoViewedProfileBean.setFull_name(userBean.getFull_name());
                                whoViewedProfileBean.setProfile_picture(userBean.getProfile_picture());
                                whoViewedProfileBean.setUsername(userBean.getUsername());
                                whoViewedProfileBean.setType("tags");
                                whoViewedProfileBean.setPoints(5);
                                arylstWhoViewedProfile.add(whoViewedProfileBean);
                            }

                            Log.d(TAG, "whoViewedProfileBean::arylstWhoViewedProfile.size():" + arylstWhoViewedProfile.size());

//                            Collections.sort(arylstWhoViewedProfile, new Comparator<WhoViewedProfileBean>() {
//                                @Override
//                                public int compare(WhoViewedProfileBean whoViewedProfileBean, WhoViewedProfileBean t1) {
//                                    if (Long.parseLong(whoViewedProfileBean.getId()) < Long.parseLong(t1.getId()))
//                                        return 1;
//                                    else
//                                        return -1;
//                                }
//                            });

                            //Final Calcualted points will be stored in arylstVwdPrflByUserFinal array
                            ArrayList<WhoViewedProfileBean> arylstVwdPrflByUserFinal
                                    = new ArrayList<WhoViewedProfileBean>();

                            ProfileViewerDBQueries profileViewerDBQueries = new ProfileViewerDBQueries(mContext);
                            profileViewerDBQueries.deleteProfileViewersFromDB();
                            ArrayList<WhoViewedProfileBean> arylstVwdPrflByUser =
                                    profileViewerDBQueries.saveProfileViewersToDb(arylstWhoViewedProfile);

                            for (WhoViewedProfileBean whoViewedProfileBean : arylstVwdPrflByUser) {
                                ArrayList<WhoViewedProfileBean> whoVwdPrflByUser
                                        = profileViewerDBQueries.getWhoViewedProfileByUser(whoViewedProfileBean.getId());
                                WhoViewedProfileBean whoVwdPrflBeanByUserFinal = new WhoViewedProfileBean();
                                for (WhoViewedProfileBean whoVwdPrflBeanByUser : whoVwdPrflByUser) {
                                    whoVwdPrflBeanByUserFinal.setId(whoVwdPrflBeanByUser.getId());
                                    whoVwdPrflBeanByUserFinal.setType(whoVwdPrflBeanByUserFinal.getType()
                                            + "|" + whoVwdPrflBeanByUser.getType());
                                    whoVwdPrflBeanByUserFinal.setPoints(whoVwdPrflBeanByUserFinal.getPoints()
                                            + whoVwdPrflBeanByUser.getPoints());
                                    whoVwdPrflBeanByUserFinal.setProfile_picture(whoVwdPrflBeanByUser.getProfile_picture());
                                    whoVwdPrflBeanByUserFinal.setUsername(whoVwdPrflBeanByUser.getUsername());
                                    whoVwdPrflBeanByUserFinal.setFull_name(whoVwdPrflBeanByUser.getFull_name());
                                }
                                arylstVwdPrflByUserFinal.add(whoVwdPrflBeanByUserFinal);
                            }

//                            for (WhoViewedProfileBean whoViewedProfileBean : arylstVwdPrflByUserFinal) {
//                                Log.d(TAG, "whoViewedProfileBean::Id:" + whoViewedProfileBean.getId());
//                                Log.d(TAG, "whoViewedProfileBean::Full_name:" + whoViewedProfileBean.getFull_name());
//                                Log.d(TAG, "whoViewedProfileBean::Type:" + whoViewedProfileBean.getType());
//                                Log.d(TAG, "whoViewedProfileBean::Points:" + whoViewedProfileBean.getPoints());
//                            }

                            Log.d(TAG, "arylstVwdPrflByUserFinal:" + arylstVwdPrflByUserFinal.size());
                            WhoViewedProfileEvent whoViewedProfileEvent = new WhoViewedProfileEvent();
                            whoViewedProfileEvent.setArylstWhoViewedProfile(arylstVwdPrflByUserFinal);
                            EventBus.getDefault().post(whoViewedProfileEvent);
//                            for (WhoViewedProfileBean whoViewedProfileBean : arylstWhoViewedProfile) {
//                                Log.d(TAG, "whoViewedProfileBean::Id:" + whoViewedProfileBean.getId());
//                                Log.d(TAG, "whoViewedProfileBean::Full_name:" + whoViewedProfileBean.getFull_name());
//                                Log.d(TAG, "whoViewedProfileBean::Type:" + whoViewedProfileBean.getType());
//                            }
                            return;
                        }
                    }
                });
//                .flatMap(new Function<ArrayList<MediaBean>, Observable<ListResponseBean<MediaBean>>>() {
//                    @Override
//                    public Observable<ListResponseBean<MediaBean>> apply(ArrayList<MediaBean> arylstTaggedMedia) throws Exception {
//                        arylstTaggedByUser = new ArrayList<UserBean>();
//                        for (MediaBean mediaBean : arylstTaggedMedia) {
//                            arylstTaggedByUser.add(mediaBean.getUserBean());
//                        }
//                        return whoViewedProfileService.getUsersRecentPost(mInstagramSession.getAccessToken());
//                    }
//                })
//                .flatMap(new Function<Observable<ListResponseBean<MediaBean>>, SingleSource<?>>() {
//                    @Override
//                    public SingleSource<?> apply(Observable<ListResponseBean<MediaBean>> listResponseBeanObservable) throws Exception {
//                        return null;
//                    }
//                })

//                .subscribe(new)
    }
}
