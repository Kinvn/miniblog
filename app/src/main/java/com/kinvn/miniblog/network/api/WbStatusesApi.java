package com.kinvn.miniblog.network.api;

import com.kinvn.miniblog.Constants;
import com.kinvn.miniblog.model.WbStatus;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Kinvn on 2017/9/6.
 */

public interface WbStatusesApi {
    @GET("2/statuses/home_timeline.json")
    Observable<List<WbStatus>> getHomeTimelineStatuses(@Query(Constants.KEY_ACCESS_TOKEN) String accessToken,
                                                       @Query(Constants.KEY_SINCE_ID) long sinceId,
                                                       @Query(Constants.KEY_COUNT) int count,
                                                       @Query(Constants.KEY_PAGE) int page);
}
