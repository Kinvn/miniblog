package com.kinvn.miniblog.model.manager;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * Created by Kinvn on 2017/9/7.
 */
public class OauthManager {
    private Oauth2AccessToken token;

    private static OauthManager ourInstance = new OauthManager();

    public static OauthManager getInstance() {
        return ourInstance;
    }

    private OauthManager() {
    }

    public Oauth2AccessToken getToken() {
        return token;
    }

    public void setToken(Oauth2AccessToken token) {
        this.token = token;
    }
}
