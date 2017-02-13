package com.instainsight.media;

import com.instainsight.media.models.MediaBean;
import com.instainsight.media.models.RecentMediaBean;
import com.instainsight.models.ListResponseBean;
import com.instainsight.networking.MyCallBack;
import com.instainsight.networking.RestClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by SONY on 12-02-2017.
 */

public class RecentMediaService {

    IMediaService iMediaService;
    private String TAG = RecentMediaService.class.getSimpleName();

    public RecentMediaService(RestClient restClient) {
        iMediaService = restClient.create(IMediaService.class);
    }

    public void getRecentMedia(MyCallBack<ListResponseBean<RecentMediaBean>> recentMediaCallBack,
                               String strRecentMedia) {
        getRecentMediaFromAPI(recentMediaCallBack, strRecentMedia);
    }

    private void getRecentMediaFromAPI(final MyCallBack<ListResponseBean<RecentMediaBean>> recentMediaCallBack
            , String strRecentMedia) {
        iMediaService.getILikedMostMedia(strRecentMedia).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ListResponseBean<RecentMediaBean>>() {
            @Override
            public void accept(ListResponseBean<RecentMediaBean> recentMediaBeanListResponseBean) throws Exception {
                recentMediaCallBack.onSuccess(recentMediaBeanListResponseBean);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                recentMediaCallBack.onError("Server Error!", throwable.getMessage().toString());
            }
        });
    }

    public void getRecentMediaNew(MyCallBack<ListResponseBean<MediaBean>> recentMediaCallBack,
                                  String strRecentMedia) {
        getRecentMediaNewFromAPI(recentMediaCallBack, strRecentMedia);
    }

    private void getRecentMediaNewFromAPI(final MyCallBack<ListResponseBean<MediaBean>> recentMediaCallBack
            , String strRecentMedia) {
        iMediaService.getRecentMedia(strRecentMedia).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ListResponseBean<MediaBean>>() {
            @Override
            public void accept(ListResponseBean<MediaBean> recentMediaBeanListResponseBean) throws Exception {
                recentMediaCallBack.onSuccess(recentMediaBeanListResponseBean);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                recentMediaCallBack.onError("Server Error!", throwable.getMessage().toString());
            }
        });
    }

    public interface IMediaService {
        @GET
        Observable<ListResponseBean<RecentMediaBean>> getILikedMostMedia(@Url String ENDPOINT_RECENT_MEDIA);

        @GET
        Observable<ListResponseBean<MediaBean>> getRecentMedia(@Url String ENDPOINT_RECENT_MEDIA);
    }

}
