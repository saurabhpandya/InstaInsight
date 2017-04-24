package com.instainsight.followersing.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.instainsight.models.RelationShipStatus;
import com.instainsight.models.UserCountBean;

/**
 * Created by SONY on 14-02-2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OtherUsersBean {

    private String id;
    private String username;
    private String profile_picture;
    private String full_name;
    private String bio;
    private String website;
    @JsonProperty("counts")
    private UserCountBean userCountBean;
    private String follows;
    private String followed_by;

    private RelationShipStatus relationShipStatus;

    public RelationShipStatus getRelationShipStatus() {
        return relationShipStatus;
    }

    public void setRelationShipStatus(RelationShipStatus relationShipStatus) {
        this.relationShipStatus = relationShipStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public UserCountBean getUserCountBean() {
        return userCountBean;
    }

    public void setUserCountBean(UserCountBean userCountBean) {
        this.userCountBean = userCountBean;
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
