package com.instainsight.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SONY on 01-03-2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class RelationShipStatus {
    private String incoming_status;
    private String outgoing_status;

    @JsonProperty("target_user_is_private")
    private Boolean target_user_is_private;

    public Boolean getTarget_user_is_private() {
        return target_user_is_private;
    }

    public void setTarget_user_is_private(Boolean target_user_is_private) {
        this.target_user_is_private = target_user_is_private;
    }

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
