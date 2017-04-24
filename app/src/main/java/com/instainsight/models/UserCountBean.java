package com.instainsight.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by SONY on 14-02-2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCountBean {
    private String media;
    private String follows;
    private String followed_by;

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getFollows() {
        return follows;
    }

    public void setFollows(String follows) {
        this.follows = follows;
    }

    public String getFollowed_by() {
        return followed_by;
    }

    public void setFollowed_by(String followed_by) {
        this.followed_by = followed_by;
    }
}
