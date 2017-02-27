package com.instainsight.likegraph.viewmodel;

import android.app.Activity;
import android.content.Context;

import com.instainsight.instagram.InstagramSession;
import com.instainsight.likegraph.LikeGraphDBQueries;
import com.instainsight.likegraph.LikeGraphService;
import com.instainsight.media.models.MediaBean;
import com.instainsight.models.ListResponseBean;
import com.instainsight.viewmodels.BaseViewModel;
import com.instainsight.viewmodels.IViewModel;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by SONY on 22-02-2017.
 */

public class LikeGraphViewModel extends BaseViewModel implements IViewModel {

    private String TAG = LikeGraphViewModel.class.getSimpleName();
    private Context mContext;
    private Activity mActivity;
    private InstagramSession mInstagramSession;
    private LikeGraphService likeGraphService;

    public LikeGraphViewModel(LikeGraphService likeGraphService, Context context, InstagramSession mInstagramSession) {
        mContext = context;
        this.mInstagramSession = mInstagramSession;
        this.likeGraphService = likeGraphService;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void setCurrentActivity(Activity value) {
        mActivity = value;
    }

    public Observable<ArrayList<MediaBean>> getLikeGraphData() {
        return likeGraphService.getLikeGraphData(mInstagramSession.getAccessToken())
                .concatMap(new Function<ListResponseBean<MediaBean>, Observable<ArrayList<MediaBean>>>() {
                    @Override
                    public Observable<ArrayList<MediaBean>>
                    apply(ListResponseBean<MediaBean> likeGraphBeanListResponseBean) throws Exception {
                        LikeGraphDBQueries likeGraphDBQueries = new LikeGraphDBQueries(mContext);

                        return Observable.just(likeGraphDBQueries.saveGraphDataToDb(likeGraphBeanListResponseBean.getData()));
                    }
                });
    }

}
