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

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public PositionBean getPositionBean() {
        return positionBean;
    }

    public void setPositionBean(PositionBean positionBean) {
        this.positionBean = positionBean;
    }
}
