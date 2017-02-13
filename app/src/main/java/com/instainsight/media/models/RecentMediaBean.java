package com.instainsight.media.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.instainsight.models.CaptionBean;
import com.instainsight.models.CommentsBean;
import com.instainsight.models.ImagesBean;
import com.instainsight.models.LikesBean;
import com.instainsight.models.LocationBean;
import com.instainsight.models.UserBean;
import com.instainsight.models.UsersInPhotoBean;

import java.util.ArrayList;

/**
 * Created by SONY on 12-02-2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecentMediaBean {

    private String created_time;
    private String link;
    @JsonProperty("location")
    private LocationBean locationBean;
    private String[] tags;
    private String user_has_liked;
    @JsonProperty("likes")
    private LikesBean likesBean;
    @JsonProperty("id")
    private String mediaId;
    private String attribution;
    @JsonProperty("images")
    private ImagesBean imagesBean;
    private String filter;
    private String type;
    @JsonProperty("comments")
    private CommentsBean commentsBean;
    @JsonProperty("user")
    private UserBean userBean;
    @JsonProperty("caption")
    private CaptionBean captionBean;
    @JsonProperty("users_in_photo")
    private ArrayList<UsersInPhotoBean> users_in_photo;

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

    public LocationBean getLocationBean() {
        return locationBean;
    }

    public void setLocationBean(LocationBean locationBean) {
        this.locationBean = locationBean;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getUser_has_liked() {
        return user_has_liked;
    }

    public void setUser_has_liked(String user_has_liked) {
        this.user_has_liked = user_has_liked;
    }

    public LikesBean getLikesBean() {
        return likesBean;
    }

    public void setLikesBean(LikesBean likesBean) {
        this.likesBean = likesBean;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public ImagesBean getImagesBean() {
        return imagesBean;
    }

    public void setImagesBean(ImagesBean imagesBean) {
        this.imagesBean = imagesBean;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CommentsBean getCommentsBean() {
        return commentsBean;
    }

    public void setCommentsBean(CommentsBean commentsBean) {
        this.commentsBean = commentsBean;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public CaptionBean getCaptionBean() {
        return captionBean;
    }

    public void setCaptionBean(CaptionBean captionBean) {
        this.captionBean = captionBean;
    }

    public ArrayList<UsersInPhotoBean> getUsers_in_photo() {
        return users_in_photo;
    }

    public void setUsers_in_photo(ArrayList<UsersInPhotoBean> users_in_photo) {
        this.users_in_photo = users_in_photo;
    }
}
