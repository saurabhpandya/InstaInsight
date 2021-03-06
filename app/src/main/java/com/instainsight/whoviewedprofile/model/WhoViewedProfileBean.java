package com.instainsight.whoviewedprofile.model;

import com.instainsight.models.RelationShipStatus;

/**
 * Created by SONY on 26-02-2017.
 */

public class WhoViewedProfileBean {

    private String username;
    private String full_name;
    private String profile_picture;
    private String id;
    private String type;
    private int points;
    private RelationShipStatus relationShipStatus;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RelationShipStatus getRelationShipStatus() {
        return relationShipStatus;
    }

    public void setRelationShipStatus(RelationShipStatus relationShipStatus) {
        this.relationShipStatus = relationShipStatus;
    }
}
