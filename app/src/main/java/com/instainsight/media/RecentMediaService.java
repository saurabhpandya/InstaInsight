package com.instainsight.media;

import com.instainsight.media.models.MediaBean;
import com.instainsight.media.models.RecentMediaBean;
import com.instainsight.models.ListResponseBean;
import com.instainsight.models.ObjectResponseBean;
import com.instainsight.models.RelationShipStatus;
import com.instainsight.models.UserBean;
import com.instainsight.networking.MyCallBack;
import com.instainsight.networking.RestClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

import static com.instainsight.constants.Constants.WebFields.ENDPOINT_MEDIALIKES;
import static com.instainsight.constants.Constants.WebFields.ENDPOINT_RECENT_MEDIA_DAG;
import static com.instainsight.constants.Constants.WebFields.ENDPOINT_RELATIONSHIP;

/**
 * Created by SONY on 12-02-2017.
 */

public class RecentMediaService {

    IMediaService iMediaService;
    private String TAG = RecentMediaService.class.getSimpleName();

    public RecentMediaService(RestClient restClient) {
        iMediaService = restClient.create(IMediaService.class);
    }

    public void getRecentMedia(MyCallBack<ListResponseBean<RecentMediaBean>> recentMediaCallBack,
                               String strRecentMedia) {
        getRecentMediaFromAPI(recentMediaCallBack, strRecentMedia);
    }

    private void getRecentMediaFromAPI(final MyCallBack<ListResponseBean<RecentMediaBean>> recentMediaCallBack
            , String strRecentMedia) {
        iMediaService.getILikedMostMedia(strRecentMedia).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ListResponseBean<RecentMediaBean>>() {
            @Override
            public void accept(ListResponseBean<RecentMediaBean> recentMediaBeanListResponseBean) throws Exception {
                recentMediaCallBack.onSuccess(recentMediaBeanListResponseBean);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                recentMediaCallBack.onError("Server Error!", throwable.getMessage().toString());
            }
        });
    }

    public void getRecentMediaNew(MyCallBack<ListResponseBean<MediaBean>> recentMediaCallBack,
                                  String strRecentMedia) {
        getRecentMediaNewFromAPI(recentMediaCallBack, strRecentMedia);
    }

    private void getRecentMediaNewFromAPI(final MyCallBack<ListResponseBean<MediaBean>> recentMediaCallBack
            , String strRecentMedia) {
        iMediaService.getRecentMedia(strRecentMedia).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ListResponseBean<MediaBean>>() {
            @Override
            public void accept(ListResponseBean<MediaBean> recentMediaBeanListResponseBean) throws Exception {
                recentMediaCallBack.onSuccess(recentMediaBeanListResponseBean);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                recentMediaCallBack.onError("Server Error!", throwable.getMessage().toString());
            }
        });
    }

    public Observable<ListResponseBean<MediaBean>> getMediaToGetTopLikers(String access_token) {
        return iMediaService.getRecentMediaToGetTopLikers(access_token)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ListResponseBean<UserBean>> getRecentMediaTopLikers(String mediaId, String accessToken) {
        return iMediaService.getRecentMediaTopLikers(mediaId, accessToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ObjectResponseBean<RelationShipStatus>> getRelationShipStatus(String userId, String accessToken) {
        return iMediaService.getRelationShipStatus(userId, accessToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ObjectResponseBean<RelationShipStatus>> setRelationShipStatus(String action, String userId, String accessToken) {
        return iMediaService.setRelationshipStatus(action, userId, accessToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public interface IMediaService {
        @GET
        Observable<ListResponseBean<RecentMediaBean>> getILikedMostMedia(@Url String ENDPOINT_RECENT_MEDIA);

        @GET(ENDPOINT_RECENT_MEDIA_DAG)
        Observable<ListResponseBean<MediaBean>> getRecentMedia(@Query("access_token") String accessToken);

        @GET(ENDPOINT_RECENT_MEDIA_DAG)
        Observable<ListResponseBean<MediaBean>> getRecentMediaToGetTopLikers(@Query("access_token") String accessToken);

        @GET(ENDPOINT_MEDIALIKES)
        Observable<ListResponseBean<UserBean>> getRecentMediaTopLikers(@Path("media-id") String mediaId,
                                                                       @Query("access_token") String accessToken);

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
