package com.instainsight.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by saurabhp on 12-01-2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListResponseBean<T> /*extends BaseResponseBean*/ {

    @JsonProperty("data")
    private ArrayList<T> Data;

    public ArrayList<T> getData() {
        return Data;
    }

    public void setData(ArrayList<T> data) {
        this.Data = data;
    }
}
