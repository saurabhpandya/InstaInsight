package com.instainsight.ghostfollowers;

import com.instainsight.followersing.followers.bean.FollowerBean;
import com.instainsight.ghostfollowers.model.CommentsBean;
import com.instainsight.ghostfollowers.model.LikesBean;

import java.util.ArrayList;

/**
 * Created by SONY on 19-02-2017.
 */

public class GhostFollowersEvent {

    private ArrayList<LikesBean> arylstLikes;
    private ArrayList<CommentsBean> arylstComments;
    private ArrayList<FollowerBean> arylstFollowers;

    public ArrayList<LikesBean> getArylstLikes() {
        return arylstLikes;
    }

    public void setArylstLikes(ArrayList<LikesBean> arylstLikes) {
        this.arylstLikes = arylstLikes;
    }

    public ArrayList<CommentsBean> getArylstComments() {
        return arylstComments;
    }

    public void setArylstComments(ArrayList<CommentsBean> arylstComments) {
        this.arylstComments = arylstComments;
    }

    public ArrayList<FollowerBean> getArylstFollowers() {
        return arylstFollowers;
    }

    public void setArylstFollowers(ArrayList<FollowerBean> arylstFollowers) {
        this.arylstFollowers = arylstFollowers;
    }
}
