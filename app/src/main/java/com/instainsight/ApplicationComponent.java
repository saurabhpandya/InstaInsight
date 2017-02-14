package com.instainsight;

import com.instainsight.followersing.OtherUserActivity;
import com.instainsight.ilikedmost.ILikedMostActivity;
import com.instainsight.media.LikeGraphActivityNew;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Defines injections at the application level
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(ILikedMostActivity iLikedMostActivity);

    void inject(LikeGraphActivityNew likeGraphActivityNew);

    void inject(OtherUserActivity otherUserActivity);
}