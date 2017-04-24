package com.instainsight.ilikedmost;

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
import com.instainsight.ilikedmost.models.ILikedMostBean;
import com.instainsight.models.RelationShipStatus;
import com.instainsight.models.UserBean;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

/**
 * Created by SONY on 12-02-2017.
 */

public class ILikedMostAdap extends RecyclerView.Adapter<ILikedMostAdap.MyViewHolder> {
    private ArrayList<ILikedMostBean> iLikedMostList;
    private Context mContext;
    private RelationshipStatusChangeListner relationshipStatusChangeListner;

    public ILikedMostAdap(Context context, ArrayList<ILikedMostBean> iLikedMostList,
                          RelationshipStatusChangeListner relationshipStatusChangeListner) {
        mContext = context;
        this.iLikedMostList = iLikedMostList;
        this.relationshipStatusChangeListner = relationshipStatusChangeListner;
    }

    public void addFollowersing(ArrayList<ILikedMostBean> iLikedMostList) {
        this.iLikedMostList = new ArrayList<>();
        this.iLikedMostList.addAll(iLikedMostList);
        notifyDataSetChanged();
    }

    public void removeILikedMost(int position) {
        this.iLikedMostList.remove(position);
        notifyDataSetChanged();
    }

    private void loadImage(String strUrl, ImageView imgvw_prflpc) {
        Glide.with(mContext).load(strUrl).placeholder(R.drawable.defaultlist)
                .dontAnimate().into(imgvw_prflpc);
    }

    @Override
    public ILikedMostAdap.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_followersing, parent, false);
        return new ILikedMostAdap.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ILikedMostAdap.MyViewHolder holder, final int position) {

        final ILikedMostBean iLikedMostBean = (ILikedMostBean) iLikedMostList.get(position);
        holder.txtvw_followersing_name.setText(iLikedMostBean.getUsersBean().getFull_name());
        loadImage(iLikedMostBean.getUsersBean().getProfile_picture(), holder.imgvw_followersing);

        final UserBean iLikedMostUserBean = iLikedMostBean.getUsersBean();
        if (iLikedMostUserBean.getRelationshipStatus() != null) {
            holder.txtvw_followersing.setVisibility(View.VISIBLE);
            RelationShipStatus relationShipStatus = iLikedMostUserBean.getRelationshipStatus();
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
                relationshipStatusChangeListner.onClickToChangeRelationStatus(position, iLikedMostUserBean.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return iLikedMostList.size();
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
