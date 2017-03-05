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
import com.instainsight.RelationshipStatusChangeListner;
import com.instainsight.models.UserBean;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

/**
 * Created by SONY on 12-02-2017.
 */

public class MyTopLikersAdap extends RecyclerView.Adapter<MyTopLikersAdap.MyViewHolder> {
    private ArrayList<UserBean> myTopLikersList;
    private Context mContext;
    private RelationshipStatusChangeListner relationshipStatusChangeListner;

    public MyTopLikersAdap(Context context, ArrayList<UserBean> myTopLikersList,
                           RelationshipStatusChangeListner relationshipStatusChangeListner) {
        mContext = context;
        this.myTopLikersList = myTopLikersList;
        this.relationshipStatusChangeListner = relationshipStatusChangeListner;
    }

    public void addMyTopLikers(ArrayList<UserBean> myTopLikersList) {
        this.myTopLikersList = new ArrayList<UserBean>();
        this.myTopLikersList.addAll(myTopLikersList);
    }

    public void removeMyTopLikers(int position) {
        this.myTopLikersList.remove(position);
        notifyDataSetChanged();
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
    public void onBindViewHolder(MyTopLikersAdap.MyViewHolder holder, final int position) {

        final UserBean myTopLikersBean = (UserBean) myTopLikersList.get(position);
        holder.txtvw_followersing_name.setText(myTopLikersBean.getFull_name());
        loadImage(myTopLikersBean.getProfile_picture(), holder.imgvw_followersing);
        holder.txtvw_followersing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relationshipStatusChangeListner.onClickToChangeRelationStatus(position, myTopLikersBean.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return myTopLikersList.size();
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
