package com.instainsight.ilikedmost;

import com.instainsight.ilikedmost.models.ILikedMostBean;
import com.instainsight.models.ListResponseBean;
import com.instainsight.models.ObjectResponseBean;
import com.instainsight.models.RelationShipStatus;
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

import static com.instainsight.constants.Constants.WebFields.ENDPOINT_RELATIONSHIP;

/**
 * Created by SONY on 11-02-2017.
 */

public class ILikedMostService {

    private String TAG = ILikedMostService.class.getSimpleName();
    private IILikedMostService iiLikedMostService;

    public ILikedMostService(RestClient restClient) {
        iiLikedMostService = restClient.create(IILikedMostService.class);
    }

    public void getILikedMost(MyCallBack<ListResponseBean<ILikedMostBean>> iLikeMostCallBack, String urlILikedMost) {
        getILikedMostAPI(iLikeMostCallBack, urlILikedMost);
    }

    private void getILikedMostAPI(final MyCallBack<ListResponseBean<ILikedMostBean>> iLikeMostCallBack, String urlILikedMost) {
        Observable<ListResponseBean<ILikedMostBean>> iLikedMostCall
                = iiLikedMostService.getILikedMostMedia(urlILikedMost);

        iLikedMostCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ListResponseBean<ILikedMostBean>>() {
                    @Override
                    public void accept(ListResponseBean<ILikedMostBean> iLikedMostBeanListResponseBean) throws Exception {
                        iLikeMostCallBack.onSuccess(iLikedMostBeanListResponseBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        iLikeMostCallBack.onError("Server Error!", throwable.getCause().toString());
                    }
                });

//        iLikedMostCall.enqueue(new Callback<ListResponseBean<ILikedMostBean>>() {
//            @Override
//            public void onResponse(Call<ListResponseBean<ILikedMostBean>> call,
//                                   Response<ListResponseBean<ILikedMostBean>> response) {
//
//                if (response.isSuccessful()) {
//                    iLikeMostCallBack.onSuccess(response.body());
//                } else {
//                    iLikeMostCallBack.onError("Server Error!", response.errorBody().toString());
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ListResponseBean<ILikedMostBean>> call, Throwable t) {
//                iLikeMostCallBack.onError("Android Application Error", t.getMessage());
//            }
//        });

    }

    public Observable<ObjectResponseBean<RelationShipStatus>> getRelationShipStatus(String userId, String accessToken) {
        return iiLikedMostService.getRelationShipStatus(userId, accessToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ObjectResponseBean<RelationShipStatus>> setRelationShipStatus(String action, String userId, String accessToken) {
        return iiLikedMostService.setRelationshipStatus(action, userId, accessToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public interface IILikedMostService {
        @GET
        Observable<ListResponseBean<ILikedMostBean>> getILikedMostMedia(@Url String ENDPOINT_MEDIALIKED);

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
