package com.instainsight.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by saurabhp on 12-01-2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectResponseBean<T> extends BaseResponseBean {
    @JsonProperty("data")
    private T Data;

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }
}
