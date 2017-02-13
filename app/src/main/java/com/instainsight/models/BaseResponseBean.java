package com.instainsight.models;

/**
 * Created by saurabhp on 12-01-2017.
 */

public class BaseResponseBean {
    private TMWError error;

    public TMWError getError() {
        return error;
    }

    public void setError(TMWError error) {
        this.error = error;
    }
}
