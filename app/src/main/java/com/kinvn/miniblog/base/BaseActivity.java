package com.kinvn.miniblog.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.kinvn.miniblog.Constants;
import com.kinvn.miniblog.db.MiniBlogDB;
import com.kinvn.miniblog.model.WbUserInfo;
import com.kinvn.miniblog.network.ApiManager;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Kinvn on 2017/8/31.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WbSdk.install(this, new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE));
    }

    protected void notifyData() {

    }

    protected void startActivity(Class<? extends Activity> clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public class MyWbAuthListener implements WbAuthListener {

        @Override
        public void onSuccess(final Oauth2AccessToken oauth2AccessToken) {
            if (oauth2AccessToken.isSessionValid()) {
                AccessTokenKeeper.writeAccessToken(BaseActivity.this, oauth2AccessToken);
                MiniBlogDB.getInstance(BaseActivity.this).saveOauthInfo(oauth2AccessToken);
                ApiManager.getInstance().getWbInfoApi()
                        .getWbUserInfoByUid(oauth2AccessToken.getToken(), oauth2AccessToken.getUid())
                        .map(new Func1<WbUserInfo, Bitmap>() {
                            @Override
                            public Bitmap call(WbUserInfo userInfo) {
                                MiniBlogDB.getInstance(BaseActivity.this).saveAccountInfo(userInfo);
                                return ApiManager.getInstance().getUserIcon(userInfo, BaseActivity.this);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Bitmap>() {
                            @Override
                            public void call(Bitmap bitmap) {
                                if (bitmap == null)
                                    return;
                                String dir = getFilesDir().toString() + "/icon/";
                                File dirs = new File(dir);
                                if (!dirs.exists()) {
                                    dirs.mkdirs();
                                }
                                File file = new File(dir + oauth2AccessToken.getUid() + ".png");
                                try {
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
                                    notifyData();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                                // TODO: 2017/9/6
                            }
                        });
            }
        }

        @Override
        public void cancel() {

        }

        @Override
        public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {

        }
    }
}
