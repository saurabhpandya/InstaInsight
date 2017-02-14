package com.instainsight.followersing;

import android.util.Log;

import com.instainsight.followersing.models.FollowersingBean;
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
 * Created by SONY on 14-02-2017.
 */

public class OtherUserService {

    private String TAG = OtherUserService.class.getSimpleName();
    private IOtherUserService IOtherUserService;

    public OtherUserService(RestClient restClient) {
        IOtherUserService = restClient.create(IOtherUserService.class);
    }

    public void getFollowedBy(MyCallBack<ListResponseBean<FollowersingBean>> followedByCallback, String urlFollowedBy) {
        getFollowedByAPI(followedByCallback, urlFollowedBy);
    }

    private void getFollowedByAPI(final MyCallBack<ListResponseBean<FollowersingBean>> followedByCallback, String urlFollowedBY) {
        IOtherUserService.getFollowedBy(urlFollowedBY).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ListResponseBean<FollowersingBean>>() {
                    @Override
                    public void accept(ListResponseBean<FollowersingBean> arylstFollowedBy) throws Exception {
                        followedByCallback.onSuccess(arylstFollowedBy);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "throwable:" + throwable.getCause().getMessage());
                        followedByCallback.onError("Server Error!", "" + throwable.getCause().getMessage());
                    }
                });
    }

    public void getFollows(MyCallBack<ListResponseBean<FollowersingBean>> followsCallback, String urlFollows) {
        getFollowsAPI(followsCallback, urlFollows);
    }

    private void getFollowsAPI(final MyCallBack<ListResponseBean<FollowersingBean>> followsCallback, String urlFollows) {
        IOtherUserService.getFollows(urlFollows).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ListResponseBean<FollowersingBean>>() {
                    @Override
                    public void accept(ListResponseBean<FollowersingBean> arylstFollows) throws Exception {
                        followsCallback.onSuccess(arylstFollows);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        followsCallback.onError("Server Error!", throwable.getCause().toString());
                    }
                });
    }

    public interface IOtherUserService {

        @GET
        Observable<ListResponseBean<FollowersingBean>> getFollows(@Url String ENDPOINT_FOLLOWS);

        @GET
        Observable<ListResponseBean<FollowersingBean>> getFollowedBy(@Url String ENDPOINT_FOLLOWED_BY);

    }
}
