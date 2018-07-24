package com.example.toshiba.testsix;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseOpenHelper extends SQLiteAssetHelper  {

    private static final String Database_name = "lexitron_v3.db";
    private static final int Database_version = 1;
    Context context;


    public DatabaseOpenHelper(Context context){
        super(context,Database_name,null,Database_version);
        this.context = context;
    }
}
