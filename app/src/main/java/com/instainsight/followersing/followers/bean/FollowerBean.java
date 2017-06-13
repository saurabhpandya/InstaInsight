package com.instainsight.followersing.followers.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.instainsight.models.RelationShipStatus;

/**
 * Created by SONY on 18-12-2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class FollowerBean {

    @JsonProperty("username")
    private String userName;
    @JsonProperty("profile_picture")
    private String profilePic;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("id")
    private String id;
    private boolean isPrivate;
    private RelationShipStatus relationShipStatus;

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public RelationShipStatus getRelationShipStatus() {
        return relationShipStatus;
    }

    public void setRelationShipStatus(RelationShipStatus relationShipStatus) {
        this.relationShipStatus = relationShipStatus;
    }

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
