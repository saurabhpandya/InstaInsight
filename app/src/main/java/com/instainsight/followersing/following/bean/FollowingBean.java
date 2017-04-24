package com.instainsight.followersing.following.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.instainsight.models.RelationShipStatus;

/**
 * Created by SONY on 18-12-2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FollowingBean {

    @JsonProperty("username")
    private String userName;
    @JsonProperty("profile_picture")
    private String profilePic;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("id")
    private String id;

    private RelationShipStatus relationShipStatus;

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

    public RelationShipStatus getRelationShipStatus() {
        return relationShipStatus;
    }

    public void setRelationShipStatus(RelationShipStatus relationShipStatus) {
        this.relationShipStatus = relationShipStatus;
    }
}
