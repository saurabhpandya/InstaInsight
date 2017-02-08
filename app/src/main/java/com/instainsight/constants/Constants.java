package com.instainsight.constants;

/**
 * Created by SONY on 17-12-2016.
 */

public class Constants {

    public static final int SPLASH_TIME = 2000;

    public static final String CLIENT_ID = "a1521f53d33b4dca82947358c75940da";
    public static final String CLIENT_SECRET = "8282f6ef07fb4f2cb47caf135053a11c";
    public static final String REDIRECT_URI = "http://yourcallback.com/";

    public class WebFields {

        public static final String RSP_DATA = "data";

        //    /users/self
        public static final String ENDPOINT_USERSELF = "/users/self";

        public static final String RSP_USERSELF_COUNTS = "counts";
        public static final String RSP_USERSELF_FOLLOWEDBY = "followed_by";
        public static final String RSP_USERSELF_FOLLOWS = "follows";

        //  /users/self/follows
        public static final String ENDPOINT_FOLLOWS = "/users/self/follows"; // FOLLOWINGS

        //  /users/self/followed-by
        public static final String ENDPOINT_FOLLOWEDBY = "/users/self/followed-by"; // FOLLOWERS

        public static final String RSP_FOLLOWS_USERNAME = "username";
        public static final String RSP_FOLLOWS_PROFILEPIC = "profile_picture";
        public static final String RSP_FOLLOWS_FULLNAME = "full_name";
        public static final String RSP_FOLLOWS_ID = "id";

        //  /users/self/media/recent
        public static final String ENDPOINT_RECENT_MEDIA = "/users/self/media/recent"; // FOLLOWINGS


    }
}
