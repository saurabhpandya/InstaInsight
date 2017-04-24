package com.instainsight.iamnotfollowingback;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.instainsight.R;
import com.instainsight.RelationshipStatusChangeListner;
import com.instainsight.followersing.followers.bean.FollowerBean;
import com.instainsight.models.RelationShipStatus;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SONY on 19-12-2016.
 */

public class NotFollowingBackAdap extends RecyclerView.Adapter<NotFollowingBackAdap.MyViewHolder> {

    private List<FollowerBean> unFollowersList;
    private Context mContext;
    private RelationshipStatusChangeListner relationshipStatusChangeListner;

    public NotFollowingBackAdap(Context context, List<FollowerBean> unFollowersList,
                                RelationshipStatusChangeListner relationshipStatusChangeListner) {
        mContext = context;
        this.unFollowersList = unFollowersList;
        this.relationshipStatusChangeListner = relationshipStatusChangeListner;
    }

    public void addFollowersing(ArrayList<FollowerBean> unFollowersList) {
        this.unFollowersList = new ArrayList<>();
        this.unFollowersList.addAll(unFollowersList);
    }

    public void removeFollowersing(int position) {
        this.unFollowersList.remove(position);
    }

    private void loadImage(String strUrl, ImageView imgvw_prflpc) {
        Glide.with(mContext).load(strUrl).placeholder(R.drawable.defaultlist)
                .dontAnimate().into(imgvw_prflpc);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_followersing, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final FollowerBean followerBean = unFollowersList.get(position);
        holder.txtvw_followersing_name.setText(followerBean.getFullName());
        loadImage(followerBean.getProfilePic(), holder.imgvw_followersing);

        if (followerBean.getRelationShipStatus() != null) {
            holder.txtvw_followersing.setVisibility(View.VISIBLE);
            RelationShipStatus relationShipStatus = followerBean.getRelationShipStatus();
            String outRelation = relationShipStatus.getOutgoing_status();
            if (outRelation.equalsIgnoreCase("none")) {
                holder.txtvw_followersing.setBackgroundResource(R.drawable.purple);
                holder.txtvw_followersing.setText(mContext.getResources().getString(R.string.lbl_follow));
            } else if (outRelation.equalsIgnoreCase("follows")) {
                holder.txtvw_followersing.setBackgroundResource(R.drawable.gray);
                holder.txtvw_followersing.setText(mContext.getResources().getString(R.string.lbl_unfollow));
            } else if (outRelation.equalsIgnoreCase("requested")) {
                holder.txtvw_followersing.setBackgroundResource(R.drawable.gray);
                holder.txtvw_followersing.setText(mContext.getResources().getString(R.string.lbl_unfollow));
            }
            holder.prgrs_followunfollow.setVisibility(View.GONE);
        } else {
            holder.prgrs_followunfollow.setVisibility(View.VISIBLE);
        }

        holder.rltv_followersing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext, "Clicked", Toast.LENGTH_SHORT).show();
                holder.prgrs_followunfollow.setVisibility(View.VISIBLE);
                relationshipStatusChangeListner.onClickToChangeRelationStatus(position, followerBean.getId());
            }
        });
    }

    private void changeRelationshipStatus() {

    }

    @Override
    public int getItemCount() {
        return unFollowersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CircularImageView imgvw_followersing;
        public TextView txtvw_followersing_name;
        public TextView txtvw_followersing;
        public RelativeLayout rltv_followersing;
        public ProgressBar prgrs_followunfollow;

        public MyViewHolder(View view) {
            super(view);
//            if (objType.equalsIgnoreCase("Follower")){
            txtvw_followersing_name = (TextView) view.findViewById(R.id.txtvw_followersing_name);
            txtvw_followersing = (TextView) view.findViewById(R.id.txtvw_followersing);
            imgvw_followersing = (CircularImageView) view.findViewById(R.id.imgvw_followersing);
            rltv_followersing = (RelativeLayout) view.findViewById(R.id.rltv_followersing);
            prgrs_followunfollow = (ProgressBar) view.findViewById(R.id.prgrs_followunfollow);
//            }else if (objType.equalsIgnoreCase("Following")){}

        }
    }
}
