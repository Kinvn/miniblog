package com.kinvn.miniblog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kinvn on 2017/9/4.
 */

public class WbDBOpenHelper extends SQLiteOpenHelper {
    private static final String CREATE_OAUTH_DB = "create table OauthDB(" +
            "uid text primary key, " +
            "access_token text, " +
            "refresh_token text, " +
            "expires_time integer)";

    private static final String CREATE_ACCOUNT_DB = "create table AccountDB(" +
            "uid text primary key, " +
            "account_name text)";

    public WbDBOpenHelper(Context context) {
        super(context, "MiniBlog", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_OAUTH_DB);
        sqLiteDatabase.execSQL(CREATE_ACCOUNT_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exits OauthDB");
        sqLiteDatabase.execSQL("drop table if exits AccountDB");
        onCreate(sqLiteDatabase);
    }
}
