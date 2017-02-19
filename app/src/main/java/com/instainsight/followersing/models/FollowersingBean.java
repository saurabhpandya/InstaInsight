package com.instainsight.followersing.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SONY on 19-12-2016.
 */

public class FollowersingBean {

    @JsonProperty("username")
    private String userName;
    @JsonProperty("profile_picture")
    private String profilePic;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("id")
    private String id;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
