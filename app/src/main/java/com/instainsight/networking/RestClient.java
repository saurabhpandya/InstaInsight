package com.instainsight.networking;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * For REST over HTTP(S). Holds the client for other services to put interfaces against.
 */
public class RestClient {
    private static final String TAG = RestClient.class.toString();
    private final Retrofit mClient;


    public RestClient(String baseUrl) {
        mClient = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    /**
     * Creates an implementation of the API defined by the ApiInterfaceClass
     *
     * @param apiInterfaceClass
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> apiInterfaceClass) {
        return mClient.create(apiInterfaceClass);
    }
}
