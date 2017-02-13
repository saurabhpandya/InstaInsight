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
import com.instainsight.followersing.followers.FollowersActivity;
import com.instainsight.followersing.following.FollowingActivity;
import com.instainsight.ilikedmost.ILikedMostActivity;
import com.instainsight.instagram.InstagramRequest;
import com.instainsight.instagram.InstagramUser;
import com.instainsight.profile.bean.UsersBean;
import com.instainsight.profile.dao.UsersDao;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.apache.http.NameValuePair;

import java.util.ArrayList;

/**
 * Created by SONY on 17-12-2016.
 */

public class PaidProfileFragment extends BaseFragment implements View.OnClickListener {

    private String TAG = ProfileFragment.class.getSimpleName();

    private CircularImageView imgvw_pp_profilepic;
    private TextView txtvw_pp_profilename, txtvw_pp_followercount, txtvw_pp_followingcount;
    private LinearLayout lnrlyt_pp_followers, lnrlyt_pp_following, lnrlyt_pp_profileviewer,
            lnrlyt_pp_mytoplikes, lnrlyt_pp_whoilikemost, lnrlyt_pp_popularfollower,
            lnrlyt_pp_ghostfollower;

    private View vwPaidProfile;

//    private InstaInsightApp instaInsightApp;

    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vwPaidProfile = inflater.inflate(R.layout.frgmnt_profilepaid, container, false);
        getIds();
        regListner();
        getUserData();

        return vwPaidProfile;
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
//        instaInsightApp = (InstaInsightApp) context.getApplicationContext();
        super.onAttach(context);
    }

    private void getIds() {
        imgvw_pp_profilepic = (CircularImageView) vwPaidProfile.findViewById(R.id.imgvw_pp_profilepic);
        txtvw_pp_profilename = (TextView) vwPaidProfile.findViewById(R.id.txtvw_pp_profilename);

        lnrlyt_pp_followers = (LinearLayout) vwPaidProfile.findViewById(R.id.lnrlyt_pp_followers);
        lnrlyt_pp_following = (LinearLayout) vwPaidProfile.findViewById(R.id.lnrlyt_pp_following);

        txtvw_pp_followercount = (TextView) vwPaidProfile.findViewById(R.id.txtvw_pp_followercount);
        txtvw_pp_followingcount = (TextView) vwPaidProfile.findViewById(R.id.txtvw_pp_followingcount);

        lnrlyt_pp_profileviewer = (LinearLayout) vwPaidProfile.findViewById(R.id.lnrlyt_pp_profileviewer);
        lnrlyt_pp_mytoplikes = (LinearLayout) vwPaidProfile.findViewById(R.id.lnrlyt_pp_mytoplikes);
        lnrlyt_pp_whoilikemost = (LinearLayout) vwPaidProfile.findViewById(R.id.lnrlyt_pp_whoilikemost);
        lnrlyt_pp_popularfollower = (LinearLayout) vwPaidProfile.findViewById(R.id.lnrlyt_pp_popularfollower);
        lnrlyt_pp_ghostfollower = (LinearLayout) vwPaidProfile.findViewById(R.id.lnrlyt_pp_ghostfollower);
    }

    private void regListner() {
        lnrlyt_pp_followers.setOnClickListener(this);
        lnrlyt_pp_following.setOnClickListener(this);

        lnrlyt_pp_profileviewer.setOnClickListener(this);
        lnrlyt_pp_mytoplikes.setOnClickListener(this);
        lnrlyt_pp_whoilikemost.setOnClickListener(this);
        lnrlyt_pp_popularfollower.setOnClickListener(this);
        lnrlyt_pp_ghostfollower.setOnClickListener(this);
    }

    private void getUserData() {

        if (mInstagramSession.isActive()) {

            InstagramUser instagramUser = mInstagramSession.getUser();

            txtvw_pp_profilename.setText(instagramUser.fullName);

            Glide.with(getActivity()).load(instagramUser.profilPicture).placeholder(R.drawable.defaultpic)
                    .crossFade().into(imgvw_pp_profilepic);

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
                                txtvw_pp_followercount.setText(usersBean.getFollowerCount());
                                txtvw_pp_followingcount.setText(usersBean.getFollowingCount());

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
                txtvw_pp_followercount.setText(usersBean.getFollowerCount());
                txtvw_pp_followingcount.setText(usersBean.getFollowingCount());

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
            case R.id.lnrlyt_pp_followers:
                openFollowers();
                break;
            case R.id.lnrlyt_pp_following:
                openFollowing();
                break;
            case R.id.lnrlyt_pp_profileviewer:
                openProfileViewer();
                break;
            case R.id.lnrlyt_pp_mytoplikes:
                openMyTopLikes();
                break;
            case R.id.lnrlyt_pp_whoilikemost:
                openWhoILikeMost();
                break;
            case R.id.lnrlyt_pp_popularfollower:
                openPopularFollower();
                break;
            case R.id.lnrlyt_pp_ghostfollower:
                openGhostFollower();
                break;
        }
    }

    private void openFollowers() {
        startActivity(new Intent(getActivity(), FollowersActivity.class));
    }

    private void openFollowing() {
        startActivity(new Intent(getActivity(), FollowingActivity.class));
    }

    private void openProfileViewer() {
        Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.lbl_prfl_vwrs));
    }

    private void openMyTopLikes() {
        Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.lbl_my_top_likes));

    }

    private void openWhoILikeMost() {
        Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.lbl_who_i_like_most));
        startActivity(ILikedMostActivity.class);
    }

    private void openPopularFollower() {
        Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.lbl_popular_followers));
    }

    private void openGhostFollower() {
        Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.lbl_ghost_followers));
    }

    private void startActivity(Class aClass) {
        startActivity(new Intent(getActivity(), aClass));
    }

}