package com.kinvn.miniblog.network;

import com.kinvn.miniblog.Constants;
import com.kinvn.miniblog.network.api.WeiboUserInfoApi;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kinvn on 2017/9/1.
 */

public class ApiManager {
    private static ApiManager instance;
    private OkHttpClient okHttpClient;
    private Converter.Factory gsonConverterFactory;
    private CallAdapter.Factory rxJavaCallAdapterFactory;

    public static ApiManager getInstance() {
        if (instance == null) {
            synchronized (ApiManager.class) {
                if (instance == null)
                    instance = new ApiManager();
            }
        }
        return instance;
    }

    public WeiboUserInfoApi getWbInfoApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Constants.WB_API_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();
        return retrofit.create(WeiboUserInfoApi.class);
    }

    private ApiManager() {
        okHttpClient = new OkHttpClient();
        gsonConverterFactory = GsonConverterFactory.create();
        rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    }
}
