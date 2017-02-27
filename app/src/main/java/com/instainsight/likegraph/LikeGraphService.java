package com.instainsight.likegraph;

import com.instainsight.media.models.MediaBean;
import com.instainsight.models.ListResponseBean;
import com.instainsight.networking.RestClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.instainsight.constants.Constants.WebFields.ENDPOINT_LIKEGRAPH;

/**
 * Created by SONY on 23-02-2017.
 */

public class LikeGraphService {

    private String TAG = LikeGraphService.class.getSimpleName();
    private ILikeGraphService iLikeGraphService;

    public LikeGraphService(RestClient restClient) {
        iLikeGraphService = restClient.create(ILikeGraphService.class);
    }

    public Observable<ListResponseBean<MediaBean>> getLikeGraphData(String accessToken) {
        return iLikeGraphService.getLikeGraphData(accessToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public interface ILikeGraphService {
        @GET(ENDPOINT_LIKEGRAPH)
        Observable<ListResponseBean<MediaBean>> getLikeGraphData(@Query("access_token") String accessToken);
    }
}
