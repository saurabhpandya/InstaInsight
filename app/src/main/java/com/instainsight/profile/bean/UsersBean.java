package com.instainsight.profile.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.instainsight.models.RelationShipStatus;
import com.instainsight.models.UserCountBean;

/**
 * Created by SONY on 18-12-2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersBean {
    @JsonProperty("id")
    private String userId;
    @JsonProperty("username")
    private String userName;
    @JsonProperty("full_name")
    private String userFullName;
    @JsonProperty("profile_picture")
    private String profilePic;
    @JsonProperty("bio")
    private String bio;
    @JsonProperty("counts")
    private UserCountBean userCountBean;
    private RelationShipStatus relationShipStatus;
    private String newFollowedByCount;
    private String newFollowsCount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public UserCountBean getUserCountBean() {
        return userCountBean;
    }

    public void setUserCountBean(UserCountBean userCountBean) {
        this.userCountBean = userCountBean;
    }

    public RelationShipStatus getRelationShipStatus() {
        return relationShipStatus;
    }

    public void setRelationShipStatus(RelationShipStatus relationShipStatus) {
        this.relationShipStatus = relationShipStatus;
    }

    public String getNewFollowedByCount() {
        return newFollowedByCount;
    }

    public void setNewFollowedByCount(String newFollowedByCount) {
        this.newFollowedByCount = newFollowedByCount;
    }

    public String getNewFollowsCount() {
        return newFollowsCount;
    }

    public void setNewFollowsCount(String newFollowsCount) {
        this.newFollowsCount = newFollowsCount;
    }
}

