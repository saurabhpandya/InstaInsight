package com.instainsight.whoviewedprofile;

import com.instainsight.followersing.followers.bean.FollowerBean;
import com.instainsight.ghostfollowers.model.CommentsBean;
import com.instainsight.ghostfollowers.model.LikesBean;
import com.instainsight.media.models.MediaBean;
import com.instainsight.models.ListResponseBean;
import com.instainsight.networking.RestClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.instainsight.constants.Constants.WebFields.ENDPOINT_WHOVIEWEDPROFILE_FOLLOWERS;
import static com.instainsight.constants.Constants.WebFields.ENDPOINT_WHOVIEWEDPROFILE_FOLLOWERS_RECENTPOST;
import static com.instainsight.constants.Constants.WebFields.ENDPOINT_WHOVIEWEDPROFILE_RECENTPOST_COMMENTS;
import static com.instainsight.constants.Constants.WebFields.ENDPOINT_WHOVIEWEDPROFILE_RECENTPOST_LIKES;
import static com.instainsight.constants.Constants.WebFields.ENDPOINT_WHOVIEWEDPROFILE_USERS_RECENTPOST;

/**
 * Created by SONY on 24-02-2017.
 */

public class WhoViewedProfileService {

    private IWhoViewedProfile iWhoViewedProfile;


    public WhoViewedProfileService(RestClient restClient) {
        iWhoViewedProfile = restClient.create(IWhoViewedProfile.class);
    }

    public Observable<ListResponseBean<FollowerBean>> getFollowedBy(String accessToken) {
        return iWhoViewedProfile.getFollowedBy(accessToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ListResponseBean<MediaBean>> getFollowedByRecentPost(String userId, String accessToken) {
        return iWhoViewedProfile.getFollowedByRecentPost(userId, accessToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ListResponseBean<MediaBean>> getUsersRecentPost(String accessToken) {
        return iWhoViewedProfile.getUsersRecentPost(accessToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ListResponseBean<CommentsBean>> getRecentPostComments(String mediaId, String accessToken) {
        return iWhoViewedProfile.getRecentPostComments(mediaId, accessToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ListResponseBean<LikesBean>> getRecentPostLikes(String mediaId, String accessToken) {
        return iWhoViewedProfile.getRecentPostLikes(mediaId, accessToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public interface IWhoViewedProfile {

        /**
         * Who Viewed Profile
         * -> Fetch followers,
         * -> Fetch Every Follower's recent posts,
         * - Filter users_in_photo for the logged in user in follower's post
         * -> Fetch Logged in user's recent posts,
         * - Fetch comments made
         * - Fetch commented user
         * -> Fetch Logged in user's recent posts,
         * - Fetch Likes made
         * - Fetch Likes user
         */

        // Followed-by UserBean (List of Users)
        @GET(ENDPOINT_WHOVIEWEDPROFILE_FOLLOWERS)
        Observable<ListResponseBean<FollowerBean>> getFollowedBy(@Query("access_token") String access_token);

        // Followed-by Recent Posts
        @GET(ENDPOINT_WHOVIEWEDPROFILE_FOLLOWERS_RECENTPOST)
        Observable<ListResponseBean<MediaBean>> getFollowedByRecentPost(@Path("user-id") String userId,
                                                                        @Query("access_token") String access_token);

        // User's Recent Posts
        @GET(ENDPOINT_WHOVIEWEDPROFILE_USERS_RECENTPOST)
        Observable<ListResponseBean<MediaBean>> getUsersRecentPost(@Query("access_token") String access_token);

        // Recent post Comments
        @GET(ENDPOINT_WHOVIEWEDPROFILE_RECENTPOST_COMMENTS)
        Observable<ListResponseBean<CommentsBean>> getRecentPostComments(@Path("media-id") String mediaId,
                                                                         @Query("access_token") String access_token);

        // Recent post Likes
        @GET(ENDPOINT_WHOVIEWEDPROFILE_RECENTPOST_LIKES)
        Observable<ListResponseBean<LikesBean>> getRecentPostLikes(@Path("media-id") String mediaId,
                                                                   @Query("access_token") String access_token);


    }

}
