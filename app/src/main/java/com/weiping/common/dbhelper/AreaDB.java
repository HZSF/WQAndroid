package com.weiping.common.dbhelper;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class AreaDB extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "area.db";
    private static final int DATABASE_VERSION = 1;
    public AreaDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
