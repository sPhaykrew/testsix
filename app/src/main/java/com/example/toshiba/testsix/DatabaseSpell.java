package com.example.toshiba.testsix;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseSpell extends SQLiteAssetHelper {

    private static final String Database_name = "spell_sys.db";
    private static final int Database_version = 1;
    Context context;

    public DatabaseSpell(Context context){
        super(context,Database_name,null,Database_version);
        this.context = context;
    }

}
