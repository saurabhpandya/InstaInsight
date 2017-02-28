package com.instainsight;

import com.instainsight.models.ObjectResponseBean;
import com.instainsight.models.RelationShipStatus;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.instainsight.constants.Constants.WebFields.ENDPOINT_RELATIONSHIP;

/**
 * Created by SONY on 01-03-2017.
 */

public interface IRelationshipStatus {

    @POST(ENDPOINT_RELATIONSHIP)
    Observable<ObjectResponseBean<RelationShipStatus>> changeRelationshipStatus(
            @Field("action") String action,
            @Query("access_token") String access_token);

}
