package com.instainsight;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import com.instainsight.followersing.followers.FollowedByDBQueries;
import com.instainsight.followersing.following.FollowsDBQueries;
import com.instainsight.login.LoginActivity;
import com.instainsight.profile.LandingActivityNew;

import io.reactivex.functions.Consumer;

import static com.instainsight.constants.Constants.SPLASH_TIME;

public class SplashScreen extends BaseActivity {

    private String TAG = SplashScreen.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        removeOldFollowers();
        removeOldFollowing();
        showSplash();
    }

    private void removeOldFollowers() {
        FollowedByDBQueries followedByDBQueries = new FollowedByDBQueries(SplashScreen.this);
        followedByDBQueries.removeFollowedByFromNewFollowedBy()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    private void removeOldFollowing() {
        FollowsDBQueries followsDBQueries = new FollowsDBQueries(SplashScreen.this);
        followsDBQueries.removeFollowsFromNewFollows()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    private void showSplash() {

        new Handler().postDelayed(new Runnable() {
                                      /*
                                       * Showing splash screen with a timer. This will be useful when you
                                       * want to show case your app logo / company
                                       */
                                      @Override
                                      public void run() {
                                          // This method will be executed once the timer is over
                                          // Start your app main activity
                                          if (mInstagramSession.isActive()) {
                                              // open profile activity
                                              Intent i = new Intent(SplashScreen.this, LandingActivityNew.class);
                                              startActivity(i);
                                              // TODO: Move this to where you establish a user session
                                              logUser();

                                          } else {
                                              //  open login activity
                                              Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                                              startActivity(i);
                                          }
                                          finish();
                                      }
                                  }
                , SPLASH_TIME);
    }

    private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        Crashlytics.setUserIdentifier(mInstagramSession.getUser().getUserBean().getUsername());
    }

}
