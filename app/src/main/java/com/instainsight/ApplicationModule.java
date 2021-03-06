package com.instainsight;

import android.app.Application;
import android.content.Context;

import com.instainsight.followersing.OtherUserService;
import com.instainsight.followersing.followers.FollowersServices;
import com.instainsight.followersing.followers.viewmodel.FollowersViewModel;
import com.instainsight.followersing.following.FollowingServices;
import com.instainsight.followersing.following.viewmodel.FollowingViewModel;
import com.instainsight.followersing.viewmodel.OtherUserViewModel;
import com.instainsight.ghostfollowers.GhostFollowersServices;
import com.instainsight.ghostfollowers.viewmodel.GhostFollowersViewModel;
import com.instainsight.iamnotfollowingback.NotFollowingBackServices;
import com.instainsight.iamnotfollowingback.viewmodel.NotFollowingBackViewModel;
import com.instainsight.ilikedmost.ILikedMostService;
import com.instainsight.ilikedmost.viewmodel.ILikedMostViewModel;
import com.instainsight.instagram.Instagram;
import com.instainsight.instagram.InstagramServices;
import com.instainsight.instagram.InstagramSession;
import com.instainsight.instagram.util.Cons;
import com.instainsight.likegraph.LikeGraphService;
import com.instainsight.likegraph.viewmodel.LikeGraphViewModel;
import com.instainsight.login.viewmodel.LoginViewModel;
import com.instainsight.media.RecentMediaService;
import com.instainsight.media.viewmodel.RecentMediaViewModel;
import com.instainsight.media.viewmodel.RecentMediaViewModelNew;
import com.instainsight.mostpopularfollowers.MostPopularFollowersServices;
import com.instainsight.mostpopularfollowers.viewmodel.MostPopularFollowersViewModel;
import com.instainsight.mytoplikers.viewmodel.MyTopLikersViewModel;
import com.instainsight.networking.RestClient;
import com.instainsight.profile.LandingServices;
import com.instainsight.profile.viewmodel.LandingViewModel;
import com.instainsight.unfollowers.UnFollowersServices;
import com.instainsight.unfollowers.viewmodel.UnFollowersViewModel;
import com.instainsight.upgradetopro.viewmodel.UpgradeToProViewModel;
import com.instainsight.whoviewedprofile.WhoViewedProfileService;
import com.instainsight.whoviewedprofile.viewmodel.WhoViewedProfileViewModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Things that live for the duration of the application and will be injected into whatever the
 * component defines
 */
@Module
public class ApplicationModule {

    Application application;
    Context context;
    Instagram mInstagram;
    InstagramSession mInstagramSession;

    public ApplicationModule(Application app) {
        application = app;
        context = app.getApplicationContext();
        mInstagram = ((InstaInsightApp) app).getInstagramInstance(context);
        mInstagramSession = ((InstaInsightApp) app).getmInstagramSession(mInstagram);
    }

    @Provides
    @Singleton
    RestClient provideRestService() {
        return new RestClient(Cons.DAGGER_API_BASE_URL);
    }

    // Recent Media - Starts
    @Provides
    @Singleton
    InstagramServices provideInstagramServices(RestClient restClient) {
        return new InstagramServices(restClient);
    }

    @Provides
    @Singleton
    LoginViewModel provideLoginViewMode(InstagramServices instagramServices) {
        return new LoginViewModel(instagramServices, context, mInstagramSession);
    }

    @Provides
    @Singleton
    LandingServices provideLandingServices(RestClient restClient) {
        return new LandingServices(restClient);
    }

    @Provides
    @Singleton
    LandingViewModel provideLandingViewModel(InstagramServices instagramServices, LandingServices landingServices) {
        return new LandingViewModel(instagramServices, landingServices, context, mInstagramSession);
    }

    // LikeGraph - Starts
    @Provides
    @Singleton
    LikeGraphService provideLikeGraphService(RestClient restClient) {
        return new LikeGraphService(restClient);
    }

    @Provides
    @Singleton
    LikeGraphViewModel provideLikeGraphViewModel(LikeGraphService likeGraphService) {
        return new LikeGraphViewModel(likeGraphService, context, mInstagramSession);
    }
    // LikeGraph - Ends

    // Recent Media - Starts
    @Provides
    @Singleton
    RecentMediaService provideMediaService(RestClient restClient) {
        return new RecentMediaService(restClient);
    }

    @Provides
    @Singleton
    RecentMediaViewModel provideRecentMediaViewMode(RecentMediaService recentMediaService) {
        return new RecentMediaViewModel(recentMediaService, context, mInstagramSession);
    }

    @Provides
    @Singleton
    RecentMediaViewModelNew provideRecentMediaViewModel(RecentMediaService recentMediaService) {
        return new RecentMediaViewModelNew(recentMediaService, context, mInstagramSession);
    }

    @Provides
    @Singleton
    MyTopLikersViewModel provideMyTopLikersViewModel(RecentMediaService recentMediaService) {
        return new MyTopLikersViewModel(recentMediaService, context, mInstagramSession);
    }
    // Recent Media - Ends

    // ILikedMost - Starts
    @Provides
    @Singleton
    ILikedMostService provideILikedMostService(RestClient restClient) {
        return new ILikedMostService(restClient);
    }

    @Provides
    @Singleton
    ILikedMostViewModel provideILikedMostViewModel(ILikedMostService iLikedMostService) {
        return new ILikedMostViewModel(iLikedMostService, context, mInstagramSession);
    }
    // ILikedMost - Ends

    // OtherUsers - Starts
    @Provides
    @Singleton
    OtherUserService otherUserService(RestClient restClient) {
        return new OtherUserService(restClient);
    }

    @Provides
    @Singleton
    OtherUserViewModel otherUserViewModel(OtherUserService otherUserService) {
        return new OtherUserViewModel(otherUserService, context, mInstagramSession);
    }
    // OtherUsers - Ends

    @Provides
    @Singleton
    MostPopularFollowersServices mostPopularFollowersServices(RestClient restClient) {
        return new MostPopularFollowersServices(restClient);
    }

    @Provides
    @Singleton
    MostPopularFollowersViewModel mostPopularFollowersViewModel(MostPopularFollowersServices mostPopularFollowersServices) {
        return new MostPopularFollowersViewModel(mostPopularFollowersServices, context, mInstagramSession);
    }

    @Provides
    @Singleton
    GhostFollowersServices ghostFollowersServices(RestClient restClient) {
        return new GhostFollowersServices(restClient);
    }

    @Provides
    @Singleton
    GhostFollowersViewModel ghostFollowersViewModel(GhostFollowersServices ghostFollowersServices) {
        return new GhostFollowersViewModel(ghostFollowersServices, context, mInstagramSession);
    }

    @Provides
    @Singleton
    WhoViewedProfileService whoViewedProfileService(RestClient restClient) {
        return new WhoViewedProfileService(restClient);
    }

    @Provides
    @Singleton
    WhoViewedProfileViewModel whoViewedProfileViewModel(WhoViewedProfileService whoViewedProfileService) {
        return new WhoViewedProfileViewModel(whoViewedProfileService, context, mInstagramSession);
    }

    @Provides
    @Singleton
    FollowersServices followersServices(RestClient restClient) {
        return new FollowersServices(restClient);
    }

    @Provides
    @Singleton
    FollowersViewModel followersViewModel(FollowersServices followersServices) {
        return new FollowersViewModel(followersServices, context, mInstagramSession);
    }

    @Provides
    @Singleton
    FollowingServices followingServices(RestClient restClient) {
        return new FollowingServices(restClient);
    }

    @Provides
    @Singleton
    FollowingViewModel followingViewModel(FollowingServices followingServices) {
        return new FollowingViewModel(followingServices, context, mInstagramSession);
    }

    @Provides
    @Singleton
    UnFollowersServices unFollowerServices(RestClient restClient) {
        return new UnFollowersServices(restClient);
    }

    @Provides
    @Singleton
    UnFollowersViewModel unFollowersViewModel(UnFollowersServices unFollowersServices) {
        return new UnFollowersViewModel(unFollowersServices, context, mInstagramSession);
    }

    @Provides
    @Singleton
    NotFollowingBackServices notFollowingBackServices(RestClient restClient) {
        return new NotFollowingBackServices(restClient);
    }

    @Provides
    @Singleton
    NotFollowingBackViewModel notFollowingBackViewModel(NotFollowingBackServices notFollowingBackServices) {
        return new NotFollowingBackViewModel(notFollowingBackServices, context, mInstagramSession);
    }

    @Provides
    @Singleton
    UpgradeToProViewModel upgradeToProViewModel() {
        return new UpgradeToProViewModel(context, mInstagramSession);
    }



}