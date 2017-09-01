package com.kinvn.miniblog.network.api;

import com.kinvn.miniblog.model.WeiboUserInfo;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Kinvn on 2017/9/1.
 */

public interface WeiboUserInfoApi {
    @GET("2/users/show.json")
    Observable<WeiboUserInfo> getWbUserInfo(@Query("access_token") String token,
                                            @Query("uid") String uid);
}
