package com.instainsight.instagram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SONY on 18-02-2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBean {
    //    {"user":{
//            "username": "saurabhpandya7",
//            "profile_picture": "https://scontent.cdninstagram.com/t51.2885-19/10808938_1527893894127084_54757867_a.jpg",
//            "id": "628497665",
//            "full_name": "Saurabh Pandya",
//            "website": "",
//            "bio": ""
//    },
//        "access_token": "628497665.a1521f5.887aec620da9450eb5d60f3d0fb271d5"
//    }
    public String id;
    public String username;
    @JsonProperty("full_name")
    public String fullName;
    @JsonProperty("profile_picture")
    public String profilPicture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfilPicture() {
        return profilPicture;
    }

    public void setProfilPicture(String profilPicture) {
        this.profilPicture = profilPicture;
    }

}
