package com.instainsight.ghostfollowers.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.instainsight.models.UserBean;

/**
 * Created by SONY on 19-02-2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentsBean {

    private String create_time;
    private String text;
    @JsonProperty("from")
    private UserBean from;
    private String id;

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserBean getFrom() {
        return from;
    }

    public void setFrom(UserBean from) {
        this.from = from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
