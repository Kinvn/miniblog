package com.kinvn.miniblog.model.manager;

import com.kinvn.miniblog.model.WbUserInfo;

/**
 * Created by Kinvn on 2017/9/6.
 */
public class WbUserInfoManager {
    private WbUserInfo userInfo;
    private static WbUserInfoManager ourInstance = new WbUserInfoManager();

    public static WbUserInfoManager getOurInstance() {
        return ourInstance;
    }

    public static void setOurInstance(WbUserInfoManager ourInstance) {
        WbUserInfoManager.ourInstance = ourInstance;
    }

    public static WbUserInfoManager getInstance() {
        return ourInstance;
    }

    private WbUserInfoManager() {
    }

    public WbUserInfo getUserInfo() {
        if (userInfo == null)
            userInfo = new WbUserInfo();
        return userInfo;
    }

    public void setUserInfo(WbUserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
