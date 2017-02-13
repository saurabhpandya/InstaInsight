package com.instainsight.models;

/**
 * Created by saurabhp on 12-01-2017.
 */

public class ObjectResponseBean<T> extends BaseResponseBean {
    private T Data;

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }
}
