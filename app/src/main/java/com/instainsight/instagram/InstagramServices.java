package com.instainsight.instagram;

import com.instainsight.networking.RestClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.instainsight.instagram.util.Cons.DAGGER_ACCESS_TOKEN_URL;

/**
 * Created by SONY on 18-02-2017.
 */

public class InstagramServices {

    private String TAG = InstagramServices.class.getSimpleName();
    private IInstagramServices iInstagramServices;

    public InstagramServices(RestClient restClient) {
        iInstagramServices = restClient.create(IInstagramServices.class);
    }

    /**
     * @param clientId
     * @param clientSecret
     * @param redirectUri
     * @param code
     * @return
     */
    public Observable<InstagramUser> getInstagramUser(String clientId, String clientSecret, String redirectUri, String code) {
        return iInstagramServices.getInstagramUserAccessToken(clientId, clientSecret,
                "authorization_code", redirectUri, code)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public interface IInstagramServices {

        @FormUrlEncoded
        @POST(DAGGER_ACCESS_TOKEN_URL)
        Observable<InstagramUser> getInstagramUserAccessToken(@Field("client_id") String client_id,
                                                              @Field("client_secret") String client_secret,
                                                              @Field("grant_type") String grant_type,
                                                              @Field("redirect_uri") String redirect_uri,
                                                              @Field("code") String code);
    }

}
