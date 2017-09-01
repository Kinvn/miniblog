package com.kinvn.miniblog.base;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kinvn.miniblog.Constants;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;

/**
 * Created by Kinvn on 2017/8/31.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WbSdk.install(this, new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE));
    }

    public class MyWbAuthListener implements WbAuthListener {

        @Override
        public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
            if (oauth2AccessToken.isSessionValid()) {
                AccessTokenKeeper.writeAccessToken(BaseActivity.this, oauth2AccessToken);
                SharedPreferences sharedPreferences = getSharedPreferences
                        (oauth2AccessToken.getUid(), MODE_APPEND);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.KEY_ACCESS_TOKEN, oauth2AccessToken.getToken());
                editor.putString(Constants.KEY_REFRESH_TOKEN, oauth2AccessToken.getRefreshToken());
                editor.putLong(Constants.KEY_EXPIRES_IN, oauth2AccessToken.getExpiresTime());
                editor.apply();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BaseActivity.this);
                SharedPreferences.Editor globalEditor = preferences.edit();
                globalEditor.putString("latest_uid", oauth2AccessToken.getUid());
                globalEditor.apply();
                Log.i("tag1", oauth2AccessToken.getToken() + " ," + oauth2AccessToken.getRefreshToken() +
                " ," + oauth2AccessToken.getUid() + " ," + oauth2AccessToken.getExpiresTime());
            }
        }

        @Override
        public void cancel() {
            Log.i("tag1", "取消");

        }

        @Override
        public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
            Log.i("tag1", "出错");
            Log.i("tag1", wbConnectErrorMessage.getErrorMessage());
        }
    }
}
