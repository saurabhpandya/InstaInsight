package com.instainsight.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SONY on 12-02-2017.
 */

public class CaptionBean {

    private String text;
    private String created_time;
    private String id;
    @JsonProperty("from")
    private UserBean userBean;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
}
