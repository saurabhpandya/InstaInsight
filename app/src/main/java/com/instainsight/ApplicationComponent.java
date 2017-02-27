package com.instainsight;

import com.instainsight.followersing.OtherUserActivity;
import com.instainsight.ghostfollowers.GhostFollowersActivity;
import com.instainsight.ilikedmost.ILikedMostActivity;
import com.instainsight.likegraph.LikeGraphActivityNew;
import com.instainsight.login.LoginActivity;
import com.instainsight.mostpopularfollowers.MostPopularFollowersActivity;
import com.instainsight.mytoplikers.MyTopLikersActivity;
import com.instainsight.whoviewedprofile.WhoViewedProfileActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Defines injections at the application level
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(WhoViewedProfileActivity whoViewedProfileActivity);

    void inject(GhostFollowersActivity ghostFollowersActivity);

    void inject(MostPopularFollowersActivity mostPopularFollowersActivity);

    void inject(MyTopLikersActivity myTopLikersActivity);

    void inject(LoginActivity loginActivity);

    void inject(ILikedMostActivity iLikedMostActivity);

    void inject(LikeGraphActivityNew likeGraphActivityNew);

    void inject(OtherUserActivity otherUserActivity);
}