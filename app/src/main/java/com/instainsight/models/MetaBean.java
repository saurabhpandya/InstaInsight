package com.instainsight.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SONY on 11-02-2017.
 */

public class MetaBean {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("code")
    private String code;

}
