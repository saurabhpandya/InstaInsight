package com.instainsight.mostpopularfollowers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.instainsight.R;
import com.instainsight.RelationshipStatusChangeListner;
import com.instainsight.followersing.models.OtherUsersBean;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

/**
 * Created by SONY on 12-02-2017.
 */

public class MostPopularFollowersAdap extends RecyclerView.Adapter<MostPopularFollowersAdap.MyViewHolder> {
    private ArrayList<OtherUsersBean> mostPopularFollowersList;
    private Context mContext;
    private RelationshipStatusChangeListner relationshipStatusChangeListner;

    public MostPopularFollowersAdap(Context context, ArrayList<OtherUsersBean> myTopLikersList,
                                    RelationshipStatusChangeListner relationshipStatusChangeListner) {
        mContext = context;
        this.mostPopularFollowersList = myTopLikersList;
        this.relationshipStatusChangeListner = relationshipStatusChangeListner;
    }

    public void addMostPopularFollowers(ArrayList<OtherUsersBean> mostPopularFollowersList) {
        this.mostPopularFollowersList = new ArrayList<OtherUsersBean>();
        this.mostPopularFollowersList.addAll(mostPopularFollowersList);
    }

    public void removeMostPopularFollower(int position) {
        this.mostPopularFollowersList.remove(position);
        notifyDataSetChanged();
    }

    private void loadImage(String strUrl, ImageView imgvw_prflpc) {
        Glide.with(mContext).load(strUrl).placeholder(R.drawable.defaultlist)
                .dontAnimate().into(imgvw_prflpc);
    }

    @Override
    public MostPopularFollowersAdap.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_followersing, parent, false);

        return new MostPopularFollowersAdap.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MostPopularFollowersAdap.MyViewHolder holder, final int position) {

        final OtherUsersBean otherUsersBean = (OtherUsersBean) mostPopularFollowersList.get(position);
        holder.txtvw_followersing_name.setText(otherUsersBean.getFull_name());
        loadImage(otherUsersBean.getProfile_picture(), holder.imgvw_followersing);

        holder.txtvw_followersing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relationshipStatusChangeListner.onClickToChangeRelationStatus(position, otherUsersBean.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mostPopularFollowersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CircularImageView imgvw_followersing;
        public TextView txtvw_followersing_name, txtvw_followersing;

        public MyViewHolder(View view) {
            super(view);
//            if (objType.equalsIgnoreCase("Follower")){
            txtvw_followersing_name = (TextView) view.findViewById(R.id.txtvw_followersing_name);
            txtvw_followersing = (TextView) view.findViewById(R.id.txtvw_followersing);
            imgvw_followersing = (CircularImageView) view.findViewById(R.id.imgvw_followersing);
//            }else if (objType.equalsIgnoreCase("Following")){}

        }
    }
}
