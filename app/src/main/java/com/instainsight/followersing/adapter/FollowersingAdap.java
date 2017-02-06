package com.instainsight.followersing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.instainsight.R;
import com.instainsight.followersing.followers.bean.FollowerBean;
import com.instainsight.followersing.following.bean.FollowingBean;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SONY on 19-12-2016.
 */

public class FollowersingAdap extends RecyclerView.Adapter<FollowersingAdap.MyViewHolder> {

    private List<Object> followersingList;
    private Context mContext;
    private String objType;

    public FollowersingAdap(Context context, List<Object> followersingList, String objType) {
        mContext = context;
        this.followersingList = followersingList;
        this.objType = objType;
    }

    public void addFollowersing(ArrayList<Object> followersingList) {
        this.followersingList.addAll(followersingList);
    }

    private void loadImage(String strUrl, ImageView imgvw_prflpc) {
        Glide.with(mContext).load(strUrl).placeholder(R.drawable.defaultlist)
                .crossFade().into(imgvw_prflpc);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_followersing, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (objType.equalsIgnoreCase("Follower")) {
            FollowerBean followerBean = (FollowerBean) followersingList.get(position);
            holder.txtvw_followersing_name.setText(followerBean.getFullName());
            loadImage(followerBean.getProfilePic(), holder.imgvw_followersing);

        } else if (objType.equalsIgnoreCase("Following")) {
            FollowingBean followingBean = (FollowingBean) followersingList.get(position);
            holder.txtvw_followersing_name.setText(followingBean.getFullName());
            loadImage(followingBean.getProfilePic(), holder.imgvw_followersing);
        } else if (objType.equalsIgnoreCase("NotFollowingBack")) {
            FollowingBean followingBean = (FollowingBean) followersingList.get(position);
            holder.txtvw_followersing_name.setText(followingBean.getFullName());
            loadImage(followingBean.getProfilePic(), holder.imgvw_followersing);
        }
    }

    @Override
    public int getItemCount() {
        return followersingList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CircularImageView imgvw_followersing;
        public TextView txtvw_followersing_name;

        public MyViewHolder(View view) {
            super(view);
//            if (objType.equalsIgnoreCase("Follower")){
            txtvw_followersing_name = (TextView) view.findViewById(R.id.txtvw_followersing_name);
            imgvw_followersing = (CircularImageView) view.findViewById(R.id.imgvw_followersing);
//            }else if (objType.equalsIgnoreCase("Following")){}

        }
    }
}
