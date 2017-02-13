package com.instainsight.media;

import com.instainsight.media.models.RecentMediaBean;

import java.util.ArrayList;

/**
 * Created by SONY on 12-02-2017.
 */

public class RecentMediaEvent {

    private ArrayList<RecentMediaBean> aryLstRecentMedia;

    public ArrayList<RecentMediaBean> getAryLstRecentMedia() {
        return aryLstRecentMedia;
    }

    public void setAryLstRecentMedia(ArrayList<RecentMediaBean> aryLstRecentMedia) {
        this.aryLstRecentMedia = aryLstRecentMedia;
    }
}
