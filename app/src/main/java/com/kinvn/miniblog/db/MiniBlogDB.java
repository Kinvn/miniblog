package com.kinvn.miniblog.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kinvn.miniblog.model.WbUserInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kinvn on 2017/9/4.
 */
public class MiniBlogDB {
    private static final String KEY_UID = "uid";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_EXPIRES_TIME = "expires_time";
    private static final String KEY_ACCOUNT_NAME = "account_name";

    private static final String TABLE_OAUTH_DB = "OauthDB";
    private static final String TABLE_ACCOUNT_DB = "AccountDB";
    private static volatile MiniBlogDB instance;
    private SQLiteDatabase db;

    private MiniBlogDB(Context context) {
        WbDBOpenHelper openHelper = new WbDBOpenHelper(context);
        db = openHelper.getWritableDatabase();
    }

    public static MiniBlogDB getInstance(Context context) {
        if (instance == null) {
            synchronized (MiniBlogDB.class) {
                if (instance == null) {
                    instance = new MiniBlogDB(context);
                }
            }
        }
        return instance;
    }

    public void saveOauthInfo(Oauth2AccessToken token) {
        ContentValues values = new ContentValues();
        values.put(KEY_UID, token.getUid());
        values.put(KEY_ACCESS_TOKEN, token.getToken());
        values.put(KEY_REFRESH_TOKEN, token.getRefreshToken());
        values.put(KEY_EXPIRES_TIME, token.getExpiresTime());
        Cursor cursor = db.query(TABLE_OAUTH_DB, null, "uid = ?",
                new String[]{token.getUid()}, null, null, null);
        if (cursor.moveToNext()) {
            db.update(TABLE_OAUTH_DB, values, "uid = ?", new String[]{token.getUid()});
        } else {
            db.insert(TABLE_OAUTH_DB, null, values);
        }
        cursor.close();
    }

    public List<Oauth2AccessToken> loadAllOauthInfo() {
        List<Oauth2AccessToken> list = new ArrayList<>();
        Cursor cursor = db.query(TABLE_OAUTH_DB, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Oauth2AccessToken token = new Oauth2AccessToken();
            token.setUid(cursor.getString(cursor.getColumnIndex(KEY_UID)));
            token.setToken(cursor.getString(cursor.getColumnIndex(KEY_ACCESS_TOKEN)));
            token.setRefreshToken(cursor.getString(cursor.getColumnIndex(KEY_REFRESH_TOKEN)));
            token.setExpiresTime(cursor.getLong(cursor.getColumnIndex(KEY_EXPIRES_TIME)));
            list.add(token);
        }
        cursor.close();
        return list;
    }

    public Oauth2AccessToken loadOauthInfo(String uid) {
        Oauth2AccessToken token = new Oauth2AccessToken();
        Cursor cursor = db.query(TABLE_OAUTH_DB, null, "uid = ?", new String[] {uid}, null, null, null);
        if (cursor.moveToFirst()) {
            token.setToken(cursor.getString(cursor.getColumnIndex(KEY_ACCESS_TOKEN)));
            token.setRefreshToken(cursor.getString(cursor.getColumnIndex(KEY_REFRESH_TOKEN)));
            token.setUid(uid);
            token.setExpiresTime(cursor.getLong(cursor.getColumnIndex(KEY_EXPIRES_TIME)));
        }
        cursor.close();
        return token;
    }

    public void saveAccountInfo(WbUserInfo userInfo) {
        Cursor cursor = db.query(TABLE_ACCOUNT_DB, null, "uid = ?",
                new String[] {String.valueOf(userInfo.getId())}, null, null, null);
        ContentValues values = new ContentValues();
        values.put(KEY_UID, String.valueOf(userInfo.getId()));
        values.put(KEY_ACCOUNT_NAME, userInfo.getName());
        if (cursor.moveToFirst()) {
            db.update(TABLE_ACCOUNT_DB, values, "uid = ?", new String[] {String.valueOf(userInfo.getId())});
        } else {
            db.insert(TABLE_ACCOUNT_DB, null, values);
        }
        cursor.close();
    }

    public List<WbUserInfo> loadAccountInfo() {
        List<WbUserInfo> list = new ArrayList<>();
        Cursor cursor = db.query(TABLE_ACCOUNT_DB, null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            WbUserInfo wbUserInfo = new WbUserInfo();
            wbUserInfo.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(KEY_UID))));
            wbUserInfo.setName(cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_NAME)));
            list.add(wbUserInfo);
        }
        cursor.close();
        return list;
    }
}
