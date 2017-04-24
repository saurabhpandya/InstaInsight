package com.instainsight.mostpopularfollowers;

import com.instainsight.followersing.followers.bean.FollowerBean;
import com.instainsight.followersing.models.OtherUsersBean;
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

import static com.instainsight.constants.Constants.WebFields.ENDPOINT_FOLLOWEDBY_DAG;
import static com.instainsight.constants.Constants.WebFields.ENDPOINT_RELATIONSHIP;
import static com.instainsight.constants.Constants.WebFields.ENDPOINT_USERINFOBYID;

/**
 * Created by SONY on 19-02-2017.
 */

public class MostPopularFollowersServices {

    IMostPopularFollower iMostPopularFollower;
    private String TAG = MostPopularFollowersServices.class.getSimpleName();

    public MostPopularFollowersServices(RestClient restClient) {
        iMostPopularFollower = restClient.create(IMostPopularFollower.class);
    }

    public Observable<ListResponseBean<FollowerBean>> getFollowers(String accessToken) {
        return iMostPopularFollower.getFollowers(accessToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ObjectResponseBean<OtherUsersBean>> getFollowersInfo(String userId,
                                                                           String accessToken) {
        return iMostPopularFollower.getFollowersInfo(userId, accessToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ObjectResponseBean<RelationShipStatus>> getRelationShipStatus(String userId, String accessToken) {
        return iMostPopularFollower.getRelationShipStatus(userId, accessToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ObjectResponseBean<RelationShipStatus>> setRelationShipStatus(String action, String userId, String accessToken) {
        return iMostPopularFollower.setRelationshipStatus(action, userId, accessToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public interface IMostPopularFollower {

        @GET(ENDPOINT_USERINFOBYID)
        Observable<ObjectResponseBean<OtherUsersBean>> getFollowersInfo(@Path("user-id") String userId,
                                                                        @Query("access_token") String access_token);

        @GET(ENDPOINT_FOLLOWEDBY_DAG)
        Observable<ListResponseBean<FollowerBean>> getFollowers(@Query("access_token") String access_token);

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
