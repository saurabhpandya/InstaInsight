package com.instainsight.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SONY on 11-02-2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImagesBean {
    @JsonProperty("standard_resolution")
    StandardResolution standardResolution;

    public StandardResolution getStandardResolution() {
        return standardResolution;
    }

    public void setStandardResolution(StandardResolution standardResolution) {
        this.standardResolution = standardResolution;
    }
}
