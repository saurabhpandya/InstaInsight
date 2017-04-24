package com.instainsight.ghostfollowers;

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

/**
 * Created by SONY on 12-02-2017.
 */

public class GhostFollowersAdap extends RecyclerView.Adapter<GhostFollowersAdap.MyViewHolder> {
    private ArrayList<FollowerBean> ghostFollowersList;
    private Context mContext;
    private RelationshipStatusChangeListner relationshipStatusChangeListner;

    public GhostFollowersAdap(Context context, ArrayList<FollowerBean> ghostFollowersList,
                              RelationshipStatusChangeListner relationshipStatusChangeListner) {
        mContext = context;
        this.ghostFollowersList = ghostFollowersList;
        this.relationshipStatusChangeListner = relationshipStatusChangeListner;
    }

    public void addGhostFollowers(ArrayList<FollowerBean> arylstGhostFollower) {
        this.ghostFollowersList = new ArrayList<>();
        this.ghostFollowersList.addAll(arylstGhostFollower);
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
    public void onBindViewHolder(final GhostFollowersAdap.MyViewHolder holder, final int position) {
        FollowerBean ghostFollower = (FollowerBean) ghostFollowersList.get(position);

        holder.txtvw_followersing_name.setText(ghostFollower.getFullName());
        loadImage(ghostFollower.getProfilePic(), holder.imgvw_followersing);

        if (ghostFollower.getRelationShipStatus() != null) {
            holder.txtvw_followersing.setVisibility(View.VISIBLE);
            RelationShipStatus relationShipStatus = ghostFollower.getRelationShipStatus();
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
                relationshipStatusChangeListner.onClickToChangeRelationStatus(position, ghostFollowersList.get(position).getId());
            }
        });

        holder.txtvw_followersing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext, "Clicked", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void removeItem(int position) {
        ghostFollowersList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ghostFollowersList.size();
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
            imgvw_followersing = (CircularImageView) view.findViewById(R.id.imgvw_followersing);
            txtvw_followersing = (TextView) view.findViewById(R.id.txtvw_followersing);
            rltv_followersing = (RelativeLayout) view.findViewById(R.id.rltv_followersing);
            prgrs_followunfollow = (ProgressBar) view.findViewById(R.id.prgrs_followunfollow);
//            }else if (objType.equalsIgnoreCase("Following")){}

        }
    }
}
