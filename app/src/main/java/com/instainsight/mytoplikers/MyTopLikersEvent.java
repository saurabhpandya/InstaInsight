package com.instainsight.mytoplikers;

import com.instainsight.models.UserBean;

/**
 * Created by SONY on 19-02-2017.
 */

public class MyTopLikersEvent {

    private UserBean topLiker;

    public UserBean getTopLiker() {
        return topLiker;
    }

    public void setTopLiker(UserBean topLiker) {
        this.topLiker = topLiker;
    }
}
