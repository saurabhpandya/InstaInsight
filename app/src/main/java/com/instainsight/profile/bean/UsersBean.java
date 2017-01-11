package com.instainsight.profile.bean;

/**
 * Created by SONY on 18-12-2016.
 */

public class UsersBean {

    private String userId;
    private String userName;
    private String userFullName;
    private String profilePic;
    private String bio;
    private String followingCount;
    private String followerCount;
    private String newFollowingCount;
    private String newFollowerCount;

    public String getNewFollowingCount() {
        return newFollowingCount;
    }

    public void setNewFollowingCount(String newFollowingCount) {
        this.newFollowingCount = newFollowingCount;
    }

    public String getNewFollowerCount() {
        return newFollowerCount;
    }

    public void setNewFollowerCount(String newFollowerCount) {
        this.newFollowerCount = newFollowerCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(String followingCount) {
        this.followingCount = followingCount;
    }

    public String getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(String followerCount) {
        this.followerCount = followerCount;
    }
}
