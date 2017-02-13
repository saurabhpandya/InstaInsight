package com.instainsight.models;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by SONY on 12-02-2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationBean {
    private String name;
    private String latitude;
    private String longitude;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
