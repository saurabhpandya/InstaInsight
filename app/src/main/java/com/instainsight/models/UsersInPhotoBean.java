package com.instainsight.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SONY on 12-02-2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersInPhotoBean {

    @JsonProperty("user")
    private UserBean userBean;

    @JsonProperty("position")
    private PositionBean positionBean;


}
