package com.instainsight.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.instainsight.InstaInsightApp;
import com.instainsight.R;
import com.instainsight.Utils.Utility;
import com.instainsight.ViewModelActivity;
import com.instainsight.databinding.ActivityLoginBinding;
import com.instainsight.instagram.Instagram;
import com.instainsight.instagram.InstagramUser;
import com.instainsight.login.viewmodel.LoginViewModel;
import com.instainsight.profile.LandingActivityNew;

import javax.inject.Inject;

public class LoginActivity extends ViewModelActivity {

    @Inject
    LoginViewModel loginViewModel;
    private String TAG = LoginActivity.class.getSimpleName();
    private Instagram.InstagramAuthListener mAuthListener = new Instagram.InstagramAuthListener() {
        @Override
        public void onSuccess(InstagramUser user) {
            startActivity(new Intent(LoginActivity.this, LandingActivityNew.class));
            finish();
        }

        @Override
        public void onError(String error) {
            Utility.showToast(getApplicationContext(), error);
        }

        @Override
        public void onCancel() {
            Utility.showToast(getApplicationContext(), "OK. Maybe later?");

        }
    };

    private ActivityLoginBinding activityLoginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((InstaInsightApp) getApplication()).getComponent().inject(LoginActivity.this);
        super.onCreate(savedInstanceState);
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        activityLoginBinding.setLoginViewModel(loginViewModel);
    }

    public void authUser(View v) {
        loginViewModel.authenticateUser(mInstagram, mAuthListener);
    }

    @Override
    protected void createViewModel() {
        mViewModel = loginViewModel;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        super.onNetworkConnectionChanged(isConnected);
        Utility.showConnectivitySnack(activityLoginBinding.rltvConnectwithinstagram, isConnected);
    }

}
