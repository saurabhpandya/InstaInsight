package com.instainsight.ghostfollowers;

import com.instainsight.followersing.followers.bean.FollowerBean;
import com.instainsight.ghostfollowers.model.CommentsBean;
import com.instainsight.ghostfollowers.model.LikesBean;
import com.instainsight.media.models.MediaBean;
import com.instainsight.models.ListResponseBean;
import com.instainsight.models.ObjectResponseBean;
import com.instainsight.models.RelationShipStatus;
import com.instainsight.networking.RestClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.instainsight.constants.Constants.WebFields.ENDPOINT_COMMENTSBYMEDIAID;
import static com.instainsight.constants.Constants.WebFields.ENDPOINT_FOLLOWEDBY_DAG;
import static com.instainsight.constants.Constants.WebFields.ENDPOINT_LIKESBYMEDIAID;
import static com.instainsight.constants.Constants.WebFields.ENDPOINT_RECENT_MEDIA_DAG;
import static com.instainsight.constants.Constants.WebFields.ENDPOINT_RELATIONSHIP;

/**
 * Created by SONY on 19-02-2017.
 */

public class GhostFollowersServices {


    private IGhostFollowers iGhostFollowers;

    public GhostFollowersServices(RestClient restClient) {
        iGhostFollowers = restClient.create(IGhostFollowers.class);
    }

    public Observable<ListResponseBean<MediaBean>> getRecentMedia(String access_token) {
        return iGhostFollowers.getRecentMedia(access_token)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ListResponseBean<LikesBean>> getLikesByMediaId(String mediaId,
                                                                     String access_token) {
        return iGhostFollowers.getLikesByMediaId(mediaId, access_token)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ListResponseBean<CommentsBean>> getCommentsByMediaId(String mediaId,
                                                                           String access_token) {
        return iGhostFollowers.getCommentsByMediaId(mediaId, access_token)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ListResponseBean<FollowerBean>> getFollowedBy(String access_token) {
        return iGhostFollowers.getFollowedBy(access_token)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ObjectResponseBean<RelationShipStatus>> getRelationShipStatus(String userId, String accessToken) {
        return iGhostFollowers.getRelationShipStatus(userId, accessToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ObjectResponseBean<RelationShipStatus>> setRelationShipStatus(String action, String userId, String accessToken) {
        return iGhostFollowers.setRelationshipStatus(action, userId, accessToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public interface IGhostFollowers {

        // Recent Posts MediaBean
        @GET(ENDPOINT_RECENT_MEDIA_DAG)
        Observable<ListResponseBean<MediaBean>> getRecentMedia(@Query("access_token") String access_token);

        // Likes LikesBean(List of Users)
        @GET(ENDPOINT_LIKESBYMEDIAID)
        Observable<ListResponseBean<LikesBean>> getLikesByMediaId(@Path("media-id") String mediaId,
                                                                  @Query("access_token") String access_token);

        // Comments CommentsBean (List of Users)
        @GET(ENDPOINT_COMMENTSBYMEDIAID)
        Observable<ListResponseBean<CommentsBean>> getCommentsByMediaId(@Path("media-id") String mediaId,
                                                                        @Query("access_token") String access_token);

        // Followed-by UserBean (List of Users)
        @GET(ENDPOINT_FOLLOWEDBY_DAG)
        Observable<ListResponseBean<FollowerBean>> getFollowedBy(@Query("access_token") String access_token);
        //

        @GET(ENDPOINT_RELATIONSHIP)
        Observable<ObjectResponseBean<RelationShipStatus>> getRelationShipStatus(@Path("user-id") String userId,
                                                                                 @Query("access_token") String accessToken);

        @POST(ENDPOINT_RELATIONSHIP)
        @FormUrlEncoded
        Observable<ObjectResponseBean<RelationShipStatus>> setRelationshipStatus(
                @Field("action") String action,
                @Path("user-id") String userId,
                @Field("access_token") String access_token);

    }

}
