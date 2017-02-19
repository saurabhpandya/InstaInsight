package com.instainsight.mytoplikers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.instainsight.R;
import com.instainsight.models.UserBean;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

/**
 * Created by SONY on 12-02-2017.
 */

public class MyTopLikersAdap extends RecyclerView.Adapter<MyTopLikersAdap.MyViewHolder> {
    private ArrayList<UserBean> myTopLikersList;
    private Context mContext;

    public MyTopLikersAdap(Context context, ArrayList<UserBean> myTopLikersList) {
        mContext = context;
        this.myTopLikersList = myTopLikersList;
    }

    public void addMyTopLikers(ArrayList<UserBean> myTopLikersList) {
        this.myTopLikersList = new ArrayList<UserBean>();
        this.myTopLikersList.addAll(myTopLikersList);
    }

    private void loadImage(String strUrl, ImageView imgvw_prflpc) {
        Glide.with(mContext).load(strUrl).placeholder(R.drawable.defaultlist)
                .dontAnimate().into(imgvw_prflpc);
    }

    @Override
    public MyTopLikersAdap.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_followersing, parent, false);

        return new MyTopLikersAdap.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyTopLikersAdap.MyViewHolder holder, int position) {

        UserBean myTopLikersBean = (UserBean) myTopLikersList.get(position);
        holder.txtvw_followersing_name.setText(myTopLikersBean.getFull_name());
        loadImage(myTopLikersBean.getProfile_picture(), holder.imgvw_followersing);

    }

    @Override
    public int getItemCount() {
        return myTopLikersList.size();
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
