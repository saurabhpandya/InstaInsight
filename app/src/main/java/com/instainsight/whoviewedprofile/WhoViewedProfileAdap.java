package com.instainsight.whoviewedprofile;

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
import com.instainsight.models.RelationShipStatus;
import com.instainsight.whoviewedprofile.model.WhoViewedProfileBean;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

/**
 * Created by SONY on 12-02-2017.
 */

public class WhoViewedProfileAdap extends RecyclerView.Adapter<WhoViewedProfileAdap.MyViewHolder> {
    private ArrayList<WhoViewedProfileBean> whoViewedProfileList;
    private Context mContext;
    private RelationshipStatusChangeListner relationshipStatusChangeListner;

    public WhoViewedProfileAdap(Context context, ArrayList<WhoViewedProfileBean> whoViewedProfileList,
                                RelationshipStatusChangeListner relationshipStatusChangeListner) {
        mContext = context;
        this.whoViewedProfileList = whoViewedProfileList;
        this.relationshipStatusChangeListner = relationshipStatusChangeListner;
    }

    public void addWhoViewedProfile(ArrayList<WhoViewedProfileBean> whoViewedProfileList) {
        this.whoViewedProfileList = new ArrayList<>();
        this.whoViewedProfileList = whoViewedProfileList;
//        this.whoViewedProfileList.addAll(this.whoViewedProfileList);
    }

    public void removeWhoViewedProfile(int position) {
        this.whoViewedProfileList.remove(position);
        notifyDataSetChanged();
    }

    private void loadImage(String strUrl, ImageView imgvw_prflpc) {
        Glide.with(mContext).load(strUrl).placeholder(R.drawable.defaultlist)
                .dontAnimate().into(imgvw_prflpc);
    }

    @Override
    public WhoViewedProfileAdap.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_followersing, parent, false);

        return new WhoViewedProfileAdap.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final WhoViewedProfileAdap.MyViewHolder holder, final int position) {
        final WhoViewedProfileBean whoViewedProfileBean = (WhoViewedProfileBean) whoViewedProfileList.get(position);
        holder.txtvw_followersing_name.setText(whoViewedProfileBean.getFull_name());
        loadImage(whoViewedProfileBean.getProfile_picture(), holder.imgvw_followersing);
        if (whoViewedProfileBean.getRelationShipStatus() != null) {
            holder.txtvw_followersing.setVisibility(View.VISIBLE);
            RelationShipStatus relationShipStatus = whoViewedProfileBean.getRelationShipStatus();
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
        holder.txtvw_followersing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.prgrs_followunfollow.setVisibility(View.VISIBLE);
                relationshipStatusChangeListner.onClickToChangeRelationStatus(position, whoViewedProfileBean.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return whoViewedProfileList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CircularImageView imgvw_followersing;
        public TextView txtvw_followersing_name, txtvw_followersing;
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
