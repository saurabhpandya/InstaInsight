package com.instainsight.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SONY on 14-02-2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class VideosBean {

    @JsonProperty("standard_resolution")
    StandardResolution standardResolution;

    public StandardResolution getStandardResolution() {
        return standardResolution;
    }

    public void setStandardResolution(StandardResolution standardResolution) {
        this.standardResolution = standardResolution;
    }

}
