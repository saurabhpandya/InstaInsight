package com.instainsight.mostpopularfollowers;

import com.instainsight.followersing.models.OtherUsersBean;

import java.util.ArrayList;

/**
 * Created by SONY on 19-02-2017.
 */

public class MostPopularFollowersEvent {

    private ArrayList<OtherUsersBean> mostPopularFollowers;

    public ArrayList<OtherUsersBean> getMostPopularFollowers() {
        return mostPopularFollowers;
    }

    public void setMostPopularFollowers(ArrayList<OtherUsersBean> mostPopularFollowers) {
        this.mostPopularFollowers = mostPopularFollowers;
    }
}
