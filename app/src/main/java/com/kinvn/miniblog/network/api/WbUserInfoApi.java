package com.kinvn.miniblog.network.api;

import com.kinvn.miniblog.Constants;
import com.kinvn.miniblog.model.WbUserInfo;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Kinvn on 2017/9/1.
 */

public interface WbUserInfoApi {
    @GET("2/users/show.json")
    Observable<WbUserInfo> getWbUserInfoByUid(@Query(Constants.KEY_ACCESS_TOKEN) String token,
                                              @Query(Constants.KEY_UID) String uid);
}
