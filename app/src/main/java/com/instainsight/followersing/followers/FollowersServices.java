package com.instainsight.followersing.followers;

import com.instainsight.followersing.followers.bean.FollowerBean;
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

/**
 * Created by SONY on 28-03-2017.
 */

public class FollowersServices {

    private IFollowerServices iFollowerServices;


    public FollowersServices(RestClient restClient) {
        iFollowerServices = restClient.create(IFollowerServices.class);
    }

    public Observable<ListResponseBean<FollowerBean>> getFollowedBy(String access_token) {
        return iFollowerServices.getFollowedBy(access_token)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ObjectResponseBean<RelationShipStatus>> getRelationShipStatus(String userId, String accessToken) {
        return iFollowerServices.getRelationShipStatus(userId, accessToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ObjectResponseBean<RelationShipStatus>> setRelationShipStatus(String action, String userId, String accessToken) {
        return iFollowerServices.setRelationshipStatus(action, userId, accessToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public interface IFollowerServices {

        @GET(ENDPOINT_FOLLOWEDBY_DAG)
        Observable<ListResponseBean<FollowerBean>> getFollowedBy(@Query("access_token") String accessToken);

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
