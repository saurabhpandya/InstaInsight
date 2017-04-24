package com.instainsight;

import com.instainsight.followersing.OtherUserActivity;
import com.instainsight.followersing.followers.FollowersActivityNew;
import com.instainsight.followersing.following.FollowingActivityNew;
import com.instainsight.ghostfollowers.GhostFollowersActivity;
import com.instainsight.iamnotfollowingback.NotFollowingBackActivity;
import com.instainsight.ilikedmost.ILikedMostActivity;
import com.instainsight.likegraph.LikeGraphActivityNew;
import com.instainsight.login.LoginActivity;
import com.instainsight.mostpopularfollowers.MostPopularFollowersActivity;
import com.instainsight.mytoplikers.MyTopLikersActivity;
import com.instainsight.profile.LandingActivityNew;
import com.instainsight.unfollowers.UnFollowersActivity;
import com.instainsight.upgradetopro.UpgradeToProActivity;
import com.instainsight.whoviewedprofile.WhoViewedProfileActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Defines injections at the application level
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(NotFollowingBackActivity notFollowingBackActivity);

    void inject(UnFollowersActivity unFollowersActivity);

    void inject(UpgradeToProActivity upgradeToProActivity);

    void inject(LandingActivityNew landingActivityNew);

    void inject(FollowersActivityNew followersActivityNew);

    void inject(FollowingActivityNew followingActivityNew);

    void inject(WhoViewedProfileActivity whoViewedProfileActivity);

    void inject(GhostFollowersActivity ghostFollowersActivity);

    void inject(MostPopularFollowersActivity mostPopularFollowersActivity);

    void inject(MyTopLikersActivity myTopLikersActivity);

    void inject(LoginActivity loginActivity);

    void inject(ILikedMostActivity iLikedMostActivity);

    void inject(LikeGraphActivityNew likeGraphActivityNew);

    void inject(OtherUserActivity otherUserActivity);
}