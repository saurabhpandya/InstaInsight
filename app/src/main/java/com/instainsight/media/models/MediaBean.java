package com.instainsight.media.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.instainsight.models.CaptionBean;
import com.instainsight.models.CommentsBean;
import com.instainsight.models.ImagesBean;
import com.instainsight.models.LikesBean;
import com.instainsight.models.LocationBean;
import com.instainsight.models.UserBean;
import com.instainsight.models.UsersInPhotoBean;
import com.instainsight.models.VideosBean;

import java.util.ArrayList;

/**
 * Created by SONY on 07-02-2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaBean {

    //    @JsonProperty("attribution")
//    private String attribution;
    @JsonProperty("likes")
    private LikesBean likesBean;
    private String user_has_liked;
    @JsonProperty("location")
    private LocationBean locationBean;
    private String filter;
    private String created_time;
    @JsonProperty("user")
    private UserBean userBean;
    private String[] tags;
    @JsonProperty("id")
    private String mediaId;
    @JsonProperty("comments")
    private CommentsBean commentsBean;
    @JsonProperty("users_in_photo")
    private ArrayList<UsersInPhotoBean> users_in_photo;
    @JsonProperty("images")
    private ImagesBean imagesBean;
    @JsonProperty("videos")
    private VideosBean videosBean;
    @JsonProperty("caption")
    private CaptionBean captionBean;
    private String link;
    private String type;

//    public String getAttribution() {
//        return attribution;
//    }
//
//    public void setAttribution(String attribution) {
//        this.attribution = attribution;
//    }

    public LikesBean getLikesBean() {
        return likesBean;
    }

    public void setLikesBean(LikesBean likesBean) {
        this.likesBean = likesBean;
    }

    public String getUser_has_liked() {
        return user_has_liked;
    }

    public void setUser_has_liked(String user_has_liked) {
        this.user_has_liked = user_has_liked;
    }

    public LocationBean getLocationBean() {
        return locationBean;
    }

    public void setLocationBean(LocationBean locationBean) {
        this.locationBean = locationBean;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public CommentsBean getCommentsBean() {
        return commentsBean;
    }

    public void setCommentsBean(CommentsBean commentsBean) {
        this.commentsBean = commentsBean;
    }

    public ArrayList<UsersInPhotoBean> getUsers_in_photo() {
        return users_in_photo;
    }

    public void setUsers_in_photo(ArrayList<UsersInPhotoBean> users_in_photo) {
        this.users_in_photo = users_in_photo;
    }

    public ImagesBean getImagesBean() {
        return imagesBean;
    }

    public void setImagesBean(ImagesBean imagesBean) {
        this.imagesBean = imagesBean;
    }

    public CaptionBean getCaptionBean() {
        return captionBean;
    }

    public void setCaptionBean(CaptionBean captionBean) {
        this.captionBean = captionBean;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public VideosBean getVideosBean() {
        return videosBean;
    }

    public void setVideosBean(VideosBean videosBean) {
        this.videosBean = videosBean;
    }
}
