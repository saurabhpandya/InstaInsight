package com.instainsight.networking;

public interface MyCallBack<T> {
    void onSuccess(T response);

    void onError(String header, String message);
}
