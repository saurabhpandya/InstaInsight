package com.instainsight.whoviewedprofile;

import com.instainsight.whoviewedprofile.model.WhoViewedProfileBean;

import java.util.ArrayList;

/**
 * Created by SONY on 26-02-2017.
 */

public class WhoViewedProfileEvent {

    private ArrayList<WhoViewedProfileBean> arylstWhoViewedProfile;

    public ArrayList<WhoViewedProfileBean> getArylstWhoViewedProfile() {
        return arylstWhoViewedProfile;
    }

    public void setArylstWhoViewedProfile(ArrayList<WhoViewedProfileBean> arylstWhoViewedProfile) {
        this.arylstWhoViewedProfile = arylstWhoViewedProfile;
    }
}
