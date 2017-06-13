package com.instainsight.constants;

import static com.instainsight.instagram.util.Cons.DAGGER_API_BASE_ENDPOINT_URL;

/**
 * Created by SONY on 17-12-2016.
 */

public class Constants {

    public static final int SPLASH_TIME = 2000;
    public static final int ADS_DELAY = 10 * 1000;

    //Mi 4i
//    public static final String TEST_ID = "F26D9F2D292FFCC31770FE3853CFE277";
//    public static final String TEST_ID = "E03A891485FBD59D7C136786E893137C";
    public static final String TEST_ID = "76D570241D60E079F6A38E39E6C12F57";
    
    public static final String CLIENT_ID = "a1521f53d33b4dca82947358c75940da";
    public static final String CLIENT_SECRET = "8282f6ef07fb4f2cb47caf135053a11c";
    public static final String REDIRECT_URI = "http://yourcallback.com/";
    public static final String SKU_REMOVE_ADS_MONTHLY = "remove_ads_1_month_subscription";
    // (arbitrary) request code for th  e purchase flow
    public static final int RC_REQUEST = 10001;
    public static final String INAPP_BASE64KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjMBYres8lMU0" +
            "mHP6MvSpTp7cIr06c1yy+l2bjF0f48ZjntNkaLYSdaU1U8sri3eDL3pRi6Jz+4uQltw0p8Mu/v/NZ3ZpEU" +
            "8HZdqDf0V0qazmkNP8qHfrmP8kAcjjh5kE44Un1MfG3tLaSGQx6fLMETRvOLBkdj/n9TK397nDvx/2lqhi" +
            "Rd2i0An1mEkwvuEtIJ3jHjwyQVMrktd/8hOimLCPDJNLz15r+Lt4SMzntshLUafhNLQeMaVnAbMTwqxiZ" +
            "Er+eFribwU9FZ8oFArZhnpr37s6wyX3/uHfDM1P6aDNMeHmNNWGvbYcIksFCmVaD8SjBCy7RXCLrOIbjbwK/QIDAQAB";
    //PROFILE VIEWER
    public static final int INAPP_PROFILE_VIEWER = 1;
    public static final String INAPP_PROFILE_VIEWER_MONTHLY = "profile_viewer_1_month";
    public static final String INAPP_PROFILE_VIEWER_HALLFYEARLY = "profile_viewer_6_months";
    public static final String INAPP_PROFILE_VIEWER_YEARLY = "profile_viewer_12_months";
    //MY TOP LIKERS
    public static final int INAPP_MY_TOP_LIKERS = 2;
    public static final String INAPP_MY_TOP_LIKERS_MONTHLY = "my_top_likers_1_month";
    public static final String INAPP_MY_TOP_LIKERS_HALLFYEARLY = "my_top_likers_6_months";
    public static final String INAPP_MY_TOP_LIKERS_YEARLY = "my_top_likers_12_months";
    //WHO I LIKED MOST
    public static final int INAPP_WHO_I_LIKED_MOST = 3;
    public static final String INAPP_WHO_I_LIKED_MOST_MONTHLY = "who_i_liked_most_1_month";
    public static final String INAPP_WHO_I_LIKED_MOST_HALLFYEARLY = "who_i_liked_most_6_months";
    public static final String INAPP_WHO_I_LIKED_MOST_YEARLY = "who_i_liked_most_12_months";
    //MOST POPULAR FOLLOWERS
    public static final int INAPP_MOST_POPULAR_FOLLOWERS = 4;
    public static final String INAPP_MOST_POPULAR_FOLLOWERS_MONTHLY = "most_popular_followers_1_month";
    public static final String INAPP_MOST_POPULAR_FOLLOWERS_HALLFYEARLY = "most_popular_followers_6_months";
    public static final String INAPP_MOST_POPULAR_FOLLOWERS_YEARLY = "most_popular_followers_12_months";
    //GHOST FOLLOWERS
    public static final int INAPP_GHOST_FOLLOWERS = 5;
    public static final String INAPP_GHOST_FOLLOWERS_MONTHLY = "ghost_followers_1_month";
    public static final String INAPP_GHOST_FOLLOWERS_HALLFYEARLY = "ghost_followers_6_months";
    public static final String INAPP_GHOST_FOLLOWERS_YEARLY = "ghost_followers_12_months";
    //REMOVE ADS
    public static final int INAPP_REMOVE_ADS = 6;
    public static final String INAPP_REMOVE_ADS_MONTHLY = "remove_the_ad_1_month";
    public static final String INAPP_REMOVE_ADS_HALLFYEARLY = "remove_the_ad_6_month";
    public static final String INAPP_REMOVE_ADS_YEARLY = "remove_the_ad_12_months";
    //UPGRADE TO PRO
    public static final int INAPP_UPGRADE_TO_PRO = 7;
    public static final String INAPP_UPGRADE_TO_PRO_MONTHLY = "upgrade_to_pro_1_month";
    public static final String INAPP_UPGRADE_TO_PRO_HALLFYEARLY = "upgrade_to_pro_6_months";
    public static final String INAPP_UPGRADE_TO_PRO_YEARLY = "upgrade_to_pro_12_months";

    public class WebFields {

        public static final String RSP_DATA = "data";

        public static final String URL_ADMOBADS = "https://www.getirali.com/InstaInsight.asmx/";
        public static final String ENDPOINT_ADMOBADS = "GetAndroidAdmobIds?";
        public static final String TOKEN_ADMOBADS = "KnXhHKOr6tHcxTriZlifm3D1YrpsajKLGDop3fdHGcyl525NDzNgRqEC8oXNe98U";


        //    /users/self
        public static final String ENDPOINT_USERSELF = "/users/self";
        public static final String ENDPOINT_USERSELF_DAG = DAGGER_API_BASE_ENDPOINT_URL + "users/self?";

        public static final String RSP_USERSELF_COUNTS = "counts";
        public static final String RSP_USERSELF_FOLLOWEDBY = "followed_by";
        public static final String RSP_USERSELF_FOLLOWS = "follows";

        //  /users/self/follows
        public static final String ENDPOINT_FOLLOWS = "/users/self/follows"; // FOLLOWINGS
        public static final String ENDPOINT_FOLLOWS_DAG = DAGGER_API_BASE_ENDPOINT_URL
                + "users/self/follows?"; // FOLLOWINGS

        //  /users/self/followed-by
        public static final String ENDPOINT_FOLLOWEDBY = "/users/self/followed-by"; // FOLLOWERS
        public static final String ENDPOINT_FOLLOWEDBY_DAG = DAGGER_API_BASE_ENDPOINT_URL
                + "users/self/followed-by?"; // FOLLOWERS

        public static final String RSP_FOLLOWS_USERNAME = "username";
        public static final String RSP_FOLLOWS_PROFILEPIC = "profile_picture";
        public static final String RSP_FOLLOWS_FULLNAME = "full_name";
        public static final String RSP_FOLLOWS_ID = "id";

        //  /users/self/media/recent
        public static final String ENDPOINT_RECENT_MEDIA = "/users/self/media/recent"; // RECENT MEDIA
        public static final String ENDPOINT_RECENT_MEDIA_DAG = DAGGER_API_BASE_ENDPOINT_URL
                + "users/self/media/recent?"; // RECENT MEDIA
        // /users/self/media/liked
        public static final String ENDPOINT_MEDIALIKED = DAGGER_API_BASE_ENDPOINT_URL
                + "users/self/media/liked?access_token="; // MEDIA LIKED BY USER

        // media/{media-id}/likes?access_token=ACCESS-TOKEN
        public static final String ENDPOINT_MEDIALIKES = DAGGER_API_BASE_ENDPOINT_URL
                + "media/{media-id}/likes?access_token="; // MEDIA LIKES BY OTHER USER

        //        users/user-id
        public static final String ENDPOINT_USERINFOBYID = DAGGER_API_BASE_ENDPOINT_URL
                + "users/{user-id}?"; // USER INFO BY USER ID

        // media/{media-id}/likes?
        public static final String ENDPOINT_LIKESBYMEDIAID = DAGGER_API_BASE_ENDPOINT_URL
                + "media/{media-id}/likes?"; // Likes by media-id

        // media/{media-id}/comments?
        public static final String ENDPOINT_COMMENTSBYMEDIAID = DAGGER_API_BASE_ENDPOINT_URL
                + "media/{media-id}/comments?"; // Comments by media-id


        public static final String ENDPOINT_LIKEGRAPH = DAGGER_API_BASE_ENDPOINT_URL
                + "users/self/media/recent?";

        /**
         * Who Viewed Profile
         * -> Fetch followers,
         * -> Fetch Every Follower's recent posts,
         * - Filter users_in_photo for the logged in user in follower's post
         * -> Fetch Logged in user's recent posts,
         * - Fetch comments made
         * - Fetch commented user
         * -> Fetch Logged in user's recent posts,
         * - Fetch Likes made
         * - Fetch Likes user
         */

        // Followers
        public static final String ENDPOINT_WHOVIEWEDPROFILE_FOLLOWERS = DAGGER_API_BASE_ENDPOINT_URL
                + "users/self/followed-by?";
        // Follower's Recent Posts
        public static final String ENDPOINT_WHOVIEWEDPROFILE_FOLLOWERS_RECENTPOST = DAGGER_API_BASE_ENDPOINT_URL
                + "users/{user-id}/media/recent/?";
        // User's Recent Posts
        public static final String ENDPOINT_WHOVIEWEDPROFILE_USERS_RECENTPOST = DAGGER_API_BASE_ENDPOINT_URL
                + "users/self/media/recent/?";
        // Recent post Comments
        public static final String ENDPOINT_WHOVIEWEDPROFILE_RECENTPOST_COMMENTS = DAGGER_API_BASE_ENDPOINT_URL
                + "media/{media-id}/comments/?";
        // Recent post Likes
        public static final String ENDPOINT_WHOVIEWEDPROFILE_RECENTPOST_LIKES = DAGGER_API_BASE_ENDPOINT_URL
                + "media/{media-id}/likes/?";

        // Relationship
        public static final String ENDPOINT_RELATIONSHIP = DAGGER_API_BASE_ENDPOINT_URL
                + "users/{user-id}/relationship/?";

        public static final String RSP_MEDIALIKED_MEDIAID = "id";
        public static final String RSP_MEDIALIKED_TYPE = "type";
        public static final String RSP_MEDIALIKED_LINK = "link";
        public static final String RSP_MEDIALIKED_CREATEDTIME = "created_time";
        public static final String RSP_MEDIALIKED_COMMENTS = "comments";
        public static final String RSP_MEDIALIKED_COMMENTSCOUNT = "count";
        public static final String RSP_MEDIALIKED_LIKES = "likes";
        public static final String RSP_MEDIALIKED_LIKESCOUNT = "count";
        public static final String RSP_MEDIALIKED_IMAGES = "images";
        public static final String RSP_MEDIALIKED_STANDARDRESOLUTION = "standard_resolution";
        public static final String RSP_MEDIALIKED_URL = "url";
        public static final String RSP_MEDIALIKED_USER = "user";
        public static final String RSP_MEDIALIKED_USERID = "id";
        public static final String RSP_MEDIALIKED_USERFULLNAME = "full_name";
        public static final String RSP_MEDIALIKED_USERUSERNAME = "username";
        public static final String RSP_MEDIALIKED_USERPROFILEPIC = "profile_picture";
    }


}
