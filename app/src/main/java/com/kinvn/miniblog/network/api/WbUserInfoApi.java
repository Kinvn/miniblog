package com.kinvn.miniblog.network.api;

import com.kinvn.miniblog.model.WbUserInfo;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Kinvn on 2017/9/1.
 */

public interface WbUserInfoApi {
    @GET("2/users/show.json")
    WbUserInfo getWbUserInfo(@Query("access_token") String token,
                                         @Query("uid") String uid);
}
