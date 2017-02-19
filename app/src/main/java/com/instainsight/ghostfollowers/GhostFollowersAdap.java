package com.instainsight.ghostfollowers;

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
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

/**
 * Created by SONY on 12-02-2017.
 */

public class GhostFollowersAdap extends RecyclerView.Adapter<GhostFollowersAdap.MyViewHolder> {
    private ArrayList<FollowerBean> ghostFollowersList;
    private Context mContext;

    public GhostFollowersAdap(Context context, ArrayList<FollowerBean> ghostFollowersList) {
        mContext = context;
        this.ghostFollowersList = ghostFollowersList;
    }

    public void addGhostFollowers(FollowerBean ghostFollower) {
        this.ghostFollowersList.add(ghostFollower);
    }

    private void loadImage(String strUrl, ImageView imgvw_prflpc) {
        Glide.with(mContext).load(strUrl).placeholder(R.drawable.defaultlist)
                .dontAnimate().into(imgvw_prflpc);
    }

    @Override
    public GhostFollowersAdap.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_followersing, parent, false);

        return new GhostFollowersAdap.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GhostFollowersAdap.MyViewHolder holder, int position) {
        FollowerBean ghostFollower = (FollowerBean) ghostFollowersList.get(position);

        holder.txtvw_followersing_name.setText(ghostFollower.getFullName());
        loadImage(ghostFollower.getProfilePic(), holder.imgvw_followersing);

    }

    @Override
    public int getItemCount() {
        return ghostFollowersList.size();
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