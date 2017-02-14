package com.instainsight.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.instainsight.BaseFragment;
import com.instainsight.LoginActivity;
import com.instainsight.R;
import com.instainsight.Utils.Utility;
import com.instainsight.constants.Constants;
import com.instainsight.followersing.NotFollowingBackActivity;
import com.instainsight.followersing.NotFollowingMeBackActivity;
import com.instainsight.followersing.OtherUserActivity;
import com.instainsight.followersing.followers.FollowersActivity;
import com.instainsight.followersing.following.FollowingActivity;
import com.instainsight.instagram.InstagramRequest;
import com.instainsight.instagram.InstagramUser;
import com.instainsight.media.LikeGraphActivityNew;
import com.instainsight.profile.bean.UsersBean;
import com.instainsight.profile.dao.UsersDao;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.apache.http.NameValuePair;

import java.util.ArrayList;

public class ProfileFragment extends BaseFragment implements View.OnClickListener {

    private String TAG = ProfileFragment.class.getSimpleName();

    private CircularImageView imgvw_profilepic;
    private TextView txtvw_profilename, txtvw_followercount, txtvw_followingcount,
            txtvw_newfollowing_count, txtvw_newfollowers_count;
    private LinearLayout lnrlyt_followers, lnrlyt_following, lnrlyt_newfollowers,
            lnrlyt_newfollowing, lnrlyt_blockedbyfollowing, lnrlyt_blockedfollowers,
            lnrlyt_likegraph;

    private View vwProfile;

//    private InstaInsightApp instaInsightApp;

    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vwProfile = inflater.inflate(R.layout.frgmnt_profile, container, false);
        getIds();
        regListner();
        getUserData();

        return vwProfile;
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
//        instaInsightApp = (InstaInsightApp) context.getApplicationContext();
        super.onAttach(context);
    }

    private void getIds() {
        imgvw_profilepic = (CircularImageView) vwProfile.findViewById(R.id.imgvw_profilepic);
        txtvw_profilename = (TextView) vwProfile.findViewById(R.id.txtvw_profilename);

        lnrlyt_followers = (LinearLayout) vwProfile.findViewById(R.id.lnrlyt_followers);
        lnrlyt_following = (LinearLayout) vwProfile.findViewById(R.id.lnrlyt_following);

        txtvw_followercount = (TextView) vwProfile.findViewById(R.id.txtvw_followercount);
        txtvw_followingcount = (TextView) vwProfile.findViewById(R.id.txtvw_followingcount);

        lnrlyt_newfollowers = (LinearLayout) vwProfile.findViewById(R.id.lnrlyt_newfollowers);
        lnrlyt_newfollowing = (LinearLayout) vwProfile.findViewById(R.id.lnrlyt_newfollowing);
        txtvw_newfollowers_count = (TextView) vwProfile.findViewById(R.id.txtvw_newfollowers_count);
        txtvw_newfollowing_count = (TextView) vwProfile.findViewById(R.id.txtvw_newfollowing_count);
        lnrlyt_blockedbyfollowing = (LinearLayout) vwProfile.findViewById(R.id.lnrlyt_blockedbyfollowing);
        lnrlyt_blockedfollowers = (LinearLayout) vwProfile.findViewById(R.id.lnrlyt_blockedfollowers);
        lnrlyt_likegraph = (LinearLayout) vwProfile.findViewById(R.id.lnrlyt_likegraph);
    }

    private void regListner() {
        lnrlyt_followers.setOnClickListener(this);
        lnrlyt_following.setOnClickListener(this);
        lnrlyt_newfollowers.setOnClickListener(this);
        lnrlyt_newfollowing.setOnClickListener(this);
        lnrlyt_blockedbyfollowing.setOnClickListener(this);
        lnrlyt_blockedfollowers.setOnClickListener(this);
        lnrlyt_likegraph.setOnClickListener(this);
    }

    private void getUserData() {

        if (mInstagramSession.isActive()) {

            final InstagramUser instagramUser = mInstagramSession.getUser();

            txtvw_profilename.setText(instagramUser.fullName);

            Glide.with(getActivity()).load(instagramUser.profilPicture).placeholder(R.drawable.defaultpic)
                    .dontAnimate().into(imgvw_profilepic);

            if (isConnected()) {

//                instaInsightApp.getUserBeanObserver().setUpdate(false);
                InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
                request.createRequest("GET", Constants.WebFields.ENDPOINT_USERSELF, new ArrayList<NameValuePair>(),
                        new InstagramRequest.InstagramRequestListener() {
                            @Override
                            public void onSuccess(String response) {

                                UsersDao usersDao = new UsersDao(mContext);
                                UsersBean usersBean = usersDao.getUserDetailsFromJson(response);
                                usersBean = usersDao.saveUserDetails(usersBean);
//                                instaInsightApp.getUserBeanObserver().setUpdate(true);
                                txtvw_followercount.setText(usersBean.getFollowerCount());
                                txtvw_followingcount.setText(usersBean.getFollowingCount());
                                txtvw_newfollowers_count.setText(usersBean.getNewFollowerCount());
                                txtvw_newfollowing_count.setText(usersBean.getNewFollowingCount());
                            }

                            @Override
                            public void onError(String error) {
                                Utility.showToast(getActivity(), error);
//                                instaInsightApp.getUserBeanObserver().setUpdate(true);
                            }
                        });
            } else {
                UsersDao usersDao = new UsersDao(getActivity());
                UsersBean usersBean = usersDao.getUserDetails(instagramUser.id);

                txtvw_followercount.setText(usersBean.getFollowerCount());
                txtvw_followingcount.setText(usersBean.getFollowingCount());
                txtvw_newfollowers_count.setText(usersBean.getNewFollowerCount());
                txtvw_newfollowing_count.setText(usersBean.getNewFollowingCount());
            }
        } else {
            Utility.showToast(getActivity(), "Could not authentication, need to log in again");
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lnrlyt_followers:
                openFollowers();
                break;
            case R.id.lnrlyt_following:
                openFollowing();
                break;
            case R.id.lnrlyt_newfollowers:
                openNewFollowers();
                break;
            case R.id.lnrlyt_newfollowing:
                openNewFollowing();
                break;
            case R.id.lnrlyt_blockedbyfollowing:
                openBlockedByFollowing();
                break;
            case R.id.lnrlyt_blockedfollowers:
                openBlockedFollowers();
                break;
            case R.id.lnrlyt_likegraph:
                openLikeGraph();
                break;
        }
    }

    private void openFollowers() {
        startActivity(OtherUserActivity.class);
    }

    private void openFollowing() {
        startActivity(FollowingActivity.class);
    }

    private void openNewFollowers() {
        startActivity(FollowersActivity.class);
    }

    private void openNewFollowing() {
        startActivity(FollowingActivity.class);
    }

    private void startActivity(Class aClass) {
        startActivity(new Intent(getActivity(), aClass));
    }

    private void openBlockedByFollowing() {
        // It will show list of users who doesn't Follow me but I follow them
        // Get the following who are not in followers
        startActivity(NotFollowingBackActivity.class);
    }

    private void openBlockedFollowers() {
        // It will show list of users who follow me but I don't follow them
        // Get the followers who are not in following
        startActivity(NotFollowingMeBackActivity.class);
    }

    private void openLikeGraph() {
        startActivity(LikeGraphActivityNew.class);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        super.onNetworkConnectionChanged(isConnected);
        Utility.showConnectivitySnack(vwProfile, isConnected);
    }
}
