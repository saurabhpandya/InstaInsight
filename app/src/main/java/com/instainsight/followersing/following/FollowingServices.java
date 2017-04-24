package com.instainsight.followersing.following;

import com.instainsight.followersing.following.bean.FollowingBean;
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

import static com.instainsight.constants.Constants.WebFields.ENDPOINT_FOLLOWS_DAG;
import static com.instainsight.constants.Constants.WebFields.ENDPOINT_RELATIONSHIP;

/**
 * Created by SONY on 28-03-2017.
 */

public class FollowingServices {

    private IFollowsServices iFollowsServices;


    public FollowingServices(RestClient restClient) {
        iFollowsServices = restClient.create(IFollowsServices.class);
    }

    public Observable<ListResponseBean<FollowingBean>> getFollows(String access_token) {
        return iFollowsServices.getFollows(access_token)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ObjectResponseBean<RelationShipStatus>> getRelationShipStatus(String userId, String accessToken) {
        return iFollowsServices.getRelationShipStatus(userId, accessToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ObjectResponseBean<RelationShipStatus>> setRelationShipStatus(String action, String userId, String accessToken) {
        return iFollowsServices.setRelationshipStatus(action, userId, accessToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public interface IFollowsServices {

        @GET(ENDPOINT_FOLLOWS_DAG)
        Observable<ListResponseBean<FollowingBean>> getFollows(@Query("access_token") String accessToken);

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
