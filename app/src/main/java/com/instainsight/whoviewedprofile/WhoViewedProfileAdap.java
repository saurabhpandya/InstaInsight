package com.instainsight.whoviewedprofile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.instainsight.R;
import com.instainsight.whoviewedprofile.model.WhoViewedProfileBean;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

/**
 * Created by SONY on 12-02-2017.
 */

public class WhoViewedProfileAdap extends RecyclerView.Adapter<WhoViewedProfileAdap.MyViewHolder> {
    private ArrayList<WhoViewedProfileBean> whoViewedProfileList;
    private Context mContext;

    public WhoViewedProfileAdap(Context context, ArrayList<WhoViewedProfileBean> whoViewedProfileList) {
        mContext = context;
        this.whoViewedProfileList = whoViewedProfileList;
    }

    public void addWhoViewedProfile(ArrayList<WhoViewedProfileBean> whoViewedProfileList) {
        this.whoViewedProfileList = new ArrayList<>();
        this.whoViewedProfileList = whoViewedProfileList;
//        this.whoViewedProfileList.addAll(this.whoViewedProfileList);
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
    public void onBindViewHolder(WhoViewedProfileAdap.MyViewHolder holder, int position) {
        WhoViewedProfileBean whoViewedProfileBean = (WhoViewedProfileBean) whoViewedProfileList.get(position);

        holder.txtvw_followersing_name.setText(whoViewedProfileBean.getFull_name());
        loadImage(whoViewedProfileBean.getProfile_picture(), holder.imgvw_followersing);

    }

    @Override
    public int getItemCount() {
        return whoViewedProfileList.size();
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
