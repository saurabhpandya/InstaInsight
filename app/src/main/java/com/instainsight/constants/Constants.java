package com.instainsight.constants;

import static com.instainsight.instagram.util.Cons.DAGGER_API_BASE_ENDPOINT_URL;

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
