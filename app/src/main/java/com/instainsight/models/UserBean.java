package com.instainsight.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by SONY on 11-02-2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBean {

    private String username;
    private String full_name;
    private String profile_picture;
    private String id;
    private int order;
    private RelationShipStatus relationshipStatus;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RelationShipStatus getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(RelationShipStatus relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
