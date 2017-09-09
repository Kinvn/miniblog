package com.kinvn.miniblog.utils;

import android.content.Context;

import com.kinvn.miniblog.Constants;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;

/**
 * Created by Kinvn on 2017/8/31.
 */
public class WeiboSDK {
    private static WeiboSDK ourInstance;
    private Context context;


    public static WeiboSDK getInstance() {
        if (ourInstance == null) {
            synchronized (WeiboSDK.class) {
                if (ourInstance == null) {
                    ourInstance = new WeiboSDK();
                }
            }
        }
        return ourInstance;
    }

    private WeiboSDK() {

    }

    public void getWbSdk(Context context) {
        if (this.context == null) {
            WbSdk.install(context, new AuthInfo(
                    context, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE));
            this.context = context;
        }
    }

}
