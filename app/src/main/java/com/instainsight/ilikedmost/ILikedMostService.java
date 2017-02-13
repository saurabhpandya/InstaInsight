package com.instainsight.ilikedmost;

import com.instainsight.ilikedmost.models.ILikedMostBean;
import com.instainsight.models.ListResponseBean;
import com.instainsight.networking.MyCallBack;
import com.instainsight.networking.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Url;

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
        Call<ListResponseBean<ILikedMostBean>> iLikedMostCall
                = iiLikedMostService.getILikedMostMedia(urlILikedMost);
        iLikedMostCall.enqueue(new Callback<ListResponseBean<ILikedMostBean>>() {
            @Override
            public void onResponse(Call<ListResponseBean<ILikedMostBean>> call,
                                   Response<ListResponseBean<ILikedMostBean>> response) {

                if (response.isSuccessful()) {
                    iLikeMostCallBack.onSuccess(response.body());
                } else {
                    iLikeMostCallBack.onError("Server Error!", response.errorBody().toString());
                }

            }

            @Override
            public void onFailure(Call<ListResponseBean<ILikedMostBean>> call, Throwable t) {
                iLikeMostCallBack.onError("Android Application Error", t.getMessage());
            }
        });
    }

    public interface IILikedMostService {
        @GET
        Call<ListResponseBean<ILikedMostBean>> getILikedMostMedia(@Url String ENDPOINT_MEDIALIKED);
    }
}
