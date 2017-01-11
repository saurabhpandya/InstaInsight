package com.instainsight;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.instainsight.Utils.Utility;
import com.instainsight.instagram.Instagram;
import com.instainsight.instagram.InstagramUser;
import com.instainsight.profile.LandingActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private String TAG = LoginActivity.class.getSimpleName();
    private Instagram.InstagramAuthListener mAuthListener = new Instagram.InstagramAuthListener() {
        @Override
        public void onSuccess(InstagramUser user) {
            startActivity(new Intent(LoginActivity.this, LandingActivity.class));
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

    private RelativeLayout rltv_connectwithinstagram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getIds();
        regListners();
    }

    private void getIds() {
        rltv_connectwithinstagram = (RelativeLayout) findViewById(R.id.rltv_connectwithinstagram);
    }

    private void regListners() {
        rltv_connectwithinstagram.setOnClickListener(this);
    }

    private void authenticateUser() {
        mInstagram.authorize(mAuthListener);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        super.onNetworkConnectionChanged(isConnected);
        Utility.showConnectivitySnack(rltv_connectwithinstagram, isConnected);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rltv_connectwithinstagram:
                if (isConnected())
                    authenticateUser();
                else
                    Utility.showConnectivitySnack(rltv_connectwithinstagram, isConnected);
                break;
            default:
        }
    }
}
