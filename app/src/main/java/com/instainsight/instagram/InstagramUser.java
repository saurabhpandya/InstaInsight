package com.instainsight.instagram;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Instagram user
 *
 * @author Lorensius W. L. T
 */

public class InstagramUser {

    @JsonProperty("access_token")
    public String accessToken;
    @JsonProperty("user")
    private UserBean userBean;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
}