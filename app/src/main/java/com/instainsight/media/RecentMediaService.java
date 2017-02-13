package com.instainsight.media;

import com.instainsight.media.models.RecentMediaBean;
import com.instainsight.models.ListResponseBean;
import com.instainsight.networking.MyCallBack;
import com.instainsight.networking.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Url;

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
        Call<ListResponseBean<RecentMediaBean>> recentMediaCall
                = iMediaService.getILikedMostMedia(strRecentMedia);

        recentMediaCall.enqueue(new Callback<ListResponseBean<RecentMediaBean>>() {
            @Override
            public void onResponse(Call<ListResponseBean<RecentMediaBean>> call,
                                   Response<ListResponseBean<RecentMediaBean>> response) {
                if (response.isSuccessful()) {
                    recentMediaCallBack.onSuccess(response.body());
                } else {
                    recentMediaCallBack.onError("Server Error!", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ListResponseBean<RecentMediaBean>> call, Throwable t) {
                recentMediaCallBack.onError("Android Application Error", t.getMessage());
            }
        });

    }

    public interface IMediaService {
        @GET
        Call<ListResponseBean<RecentMediaBean>> getILikedMostMedia(@Url String ENDPOINT_RECENT_MEDIA);
    }

}
