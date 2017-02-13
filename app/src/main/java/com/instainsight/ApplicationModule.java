package com.instainsight;

import android.app.Application;
import android.content.Context;

import com.instainsight.ilikedmost.ILikedMostService;
import com.instainsight.ilikedmost.viewmodel.ILikedMostViewModel;
import com.instainsight.instagram.Instagram;
import com.instainsight.instagram.InstagramSession;
import com.instainsight.instagram.util.Cons;
import com.instainsight.media.RecentMediaService;
import com.instainsight.media.viewmodel.RecentMediaViewModel;
import com.instainsight.media.viewmodel.RecentMediaViewModelNew;
import com.instainsight.networking.RestClient;

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


}