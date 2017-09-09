package com.kinvn.miniblog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kinvn on 2017/9/4.
 */

public class WbDBOpenHelper extends SQLiteOpenHelper {
    private static final String ACCOUNT_DATA_BASE = "ACCOUNT_DATA_BASE";

    public WbDBOpenHelper(Context context, String name
            , SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
