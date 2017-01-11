package com.instainsight;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.instainsight.profile.LandingActivity;

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
        showSplash();
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
                                              Intent i = new Intent(SplashScreen.this, LandingActivity.class);
                                              startActivity(i);
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

}
