package com.instainsight.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by SONY on 01-03-2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class RelationShipStatus {
    private String incoming_status;
    private String outgoing_status;

    public String getIncoming_status() {
        return incoming_status;
    }

    public void setIncoming_status(String incoming_status) {
        this.incoming_status = incoming_status;
    }

    public String getOutgoing_status() {
        return outgoing_status;
    }

    public void setOutgoing_status(String outgoing_status) {
        this.outgoing_status = outgoing_status;
    }
}
