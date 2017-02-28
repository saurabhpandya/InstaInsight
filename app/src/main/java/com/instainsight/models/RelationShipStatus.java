package com.instainsight.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by SONY on 01-03-2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class RelationShipStatus {
    private String incoming_status;
    private String outgoing_status;
}
