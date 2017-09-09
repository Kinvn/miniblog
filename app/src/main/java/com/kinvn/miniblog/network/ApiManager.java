package com.kinvn.miniblog.network;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.kinvn.miniblog.Constants;
import com.kinvn.miniblog.model.WbUserInfo;
import com.kinvn.miniblog.network.api.WbStatusesApi;
import com.kinvn.miniblog.network.api.WbUserInfoApi;

import java.util.concurrent.ExecutionException;

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
    private static volatile ApiManager instance;
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

    private ApiManager() {
        okHttpClient = new OkHttpClient();
        gsonConverterFactory = GsonConverterFactory.create();
        rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    }

    public WbUserInfoApi getWbInfoApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Constants.WB_API_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();
        return retrofit.create(WbUserInfoApi.class);
    }

    public WbStatusesApi getWbStatusesApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Constants.WB_API_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();
        return retrofit.create(WbStatusesApi.class);
    }

    public Bitmap getUserIcon(WbUserInfo userInfo, final Context context) {
        try {
            return Glide.with(context).load(userInfo.getProfile_image_url()).asBitmap()
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }


}
