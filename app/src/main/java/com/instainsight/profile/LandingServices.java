package com.instainsight.profile;

import com.instainsight.models.ObjectResponseBean;
import com.instainsight.networking.RestClient;
import com.instainsight.profile.bean.UsersBean;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.instainsight.constants.Constants.WebFields.ENDPOINT_ADMOBADS;
import static com.instainsight.constants.Constants.WebFields.ENDPOINT_USERSELF_DAG;
import static com.instainsight.constants.Constants.WebFields.URL_ADMOBADS;

/**
 * Created by SONY on 28-03-2017.
 */

public class LandingServices {

    private ILandingServices iLandingServices;


    public LandingServices(RestClient restClient) {
        iLandingServices = restClient.create(ILandingServices.class);
    }

    public Observable<ObjectResponseBean<UsersBean>> getSelfData(String access_token) {
        return iLandingServices.getSelfData(access_token)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Call<String> getAdMobAds(String token) {

        Retrofit restClient = new Retrofit.Builder()
                .baseUrl(URL_ADMOBADS)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ILandingServices iAdMobService = restClient.create(ILandingServices.class);

        return iAdMobService.getAdMobAds(token);
    }

    public interface ILandingServices {

        @GET(ENDPOINT_USERSELF_DAG)
        Observable<ObjectResponseBean<UsersBean>> getSelfData(@Query("access_token") String accessToken);

        @GET(ENDPOINT_ADMOBADS)
        Call<String> getAdMobAds(@Query("Token") String token);
    }
}
