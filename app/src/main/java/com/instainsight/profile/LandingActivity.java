package com.instainsight.profile;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.instainsight.BaseActivity;
import com.instainsight.InstaInsightApp;
import com.instainsight.R;
import com.instainsight.Utils.ConnectivityReceiver;

public class LandingActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {

    private String TAG = LandingActivity.class.getSimpleName();

    //    private BottomNavigationView bottomNavigationView;
    private ImageView imgvw_paid_user, imgvw_free_user;

    private int mSelectedItem;
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//Do what you need for this SDK
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getWindow().setStatusBarColor(getResources().getColor(R.color.actionbar));

        };
        getIds();
        regListner();
//        selectFragment(bottomNavigationView.getMenu().getItem(0));
        selectFragment(0);
    }

    private void getIds() {
//        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        imgvw_free_user = (ImageView) findViewById(R.id.imgvw_free_user);
        imgvw_paid_user = (ImageView) findViewById(R.id.imgvw_paid_user);
    }

    @Override
    protected void onResume() {
        super.onResume();
        InstaInsightApp.getInstance().setConnectivityListener(this);
    }

    private void regListner() {

        imgvw_free_user.setOnClickListener(this);
        imgvw_paid_user.setOnClickListener(this);


//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                selectFragment(item);
////                switch (item.getItemId()) {
////                    case R.id.menu_freeuser:
////                        Utility.showToast(getApplicationContext(), "Free User");
////                        break;
////                    case R.id.menu_paiduser:
////                        Utility.showToast(getApplicationContext(), "Paid User");
////                        break;
////                }
//                return true;
//            }
//        });
    }

//    private void selectFragment(MenuItem item) {
//        Fragment frag = null;
//        // init corresponding fragment
//        switch (item.getItemId()) {
//            case R.id.menu_freeuser:
//                frag = new ProfileFragment();
//                break;
//            case R.id.menu_paiduser:
//                frag = new PaidProfileFragment();
//                break;
//        }
//
//        // update selected item
//        mSelectedItem = item.getItemId();
//
//        // uncheck the other items.
//        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
//            MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
//            menuItem.setChecked(menuItem.getItemId() == item.getItemId());
//        }
//
//        updateToolbarText(item.getTitle());
//
//        if (frag != null) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.container, frag, frag.getTag());
//            ft.commit();
//        }
//    }

    private void selectFragment(int fragment) {
        Fragment frag = null;
        // init corresponding fragment
        switch (fragment) {
            case 0:
                imgvw_free_user.setBackgroundResource(R.drawable.freemag);
                imgvw_paid_user.setBackgroundResource(R.drawable.paidgray);
                frag = new ProfileFragment();
                break;
            case 1:
                imgvw_free_user.setBackgroundResource(R.drawable.freegray);
                imgvw_paid_user.setBackgroundResource(R.drawable.paidmag);
                frag = new PaidProfileFragment();
                break;
        }

        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, frag, frag.getTag());
            ft.commit();
        }
    }

//    private void updateToolbarText(CharSequence text) {
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setTitle(text);
//        }
//    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgvw_free_user:
                selectFragment(0);
                break;
            case R.id.imgvw_paid_user:
                selectFragment(1);
                break;
            default:
        }
    }
}
