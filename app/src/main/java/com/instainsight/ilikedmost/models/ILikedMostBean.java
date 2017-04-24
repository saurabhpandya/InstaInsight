package com.instainsight.ilikedmost.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.instainsight.models.CommentsBean;
import com.instainsight.models.ImagesBean;
import com.instainsight.models.LikesBean;
import com.instainsight.models.RelationShipStatus;
import com.instainsight.models.UserBean;

/**
 * Created by SONY on 11-02-2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ILikedMostBean {

    @JsonProperty("likes")
    private LikesBean likesBean;

    @JsonProperty("images")
    private ImagesBean imagesBean;

    @JsonProperty("comments")
    private CommentsBean commentsBean;

    private String id;
    private String type;
    private String created_time;
    private String link;

    @JsonProperty("user")
    private UserBean usersBean;

    private String likedUserId;

    private RelationShipStatus relationShipStatus;

    public String getLikedUserId() {
        return likedUserId;
    }

    public void setLikedUserId(String likedUserId) {
        this.likedUserId = likedUserId;
    }

    public LikesBean getLikesBean() {
        return likesBean;
    }

    public void setLikesBean(LikesBean likesBean) {
        this.likesBean = likesBean;
    }

    public ImagesBean getImagesBean() {
        return imagesBean;
    }

    public void setImagesBean(ImagesBean imagesBean) {
        this.imagesBean = imagesBean;
    }

    public CommentsBean getCommentsBean() {
        return commentsBean;
    }

    public void setCommentsBean(CommentsBean commentsBean) {
        this.commentsBean = commentsBean;
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

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public UserBean getUsersBean() {
        return usersBean;
    }

    public void setUsersBean(UserBean usersBean) {
        this.usersBean = usersBean;
    }

    public RelationShipStatus getRelationShipStatus() {
        return relationShipStatus;
    }

    public void setRelationShipStatus(RelationShipStatus relationShipStatus) {
        this.relationShipStatus = relationShipStatus;
    }
}
