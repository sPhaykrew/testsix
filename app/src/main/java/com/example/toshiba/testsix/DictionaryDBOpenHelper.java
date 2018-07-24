package com.example.toshiba.testsix;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DictionaryDBOpenHelper extends SQLiteOpenHelper{

    //Constants for db name and version
    public static final String DATABASE_NAME = "BEST_SPELL_TH";
    private static final int DATABASE_VERSION = 1;

    //Constants for identifying table and columns
    public static String TABLE_NAME = "BEST_SPELL_TH";
    public static final String _ID = "ID";
    public static final String WORD_VOCAB = "SENSEGROUP";
    public static final String WORD_G2P = "G2P";

    public static final String[] ALL_COLUMNS =
            {_ID, WORD_VOCAB, WORD_G2P};

    public DictionaryDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        /*if(TWSActivity.isEnglish){
            TABLE_NAME = "BEST_SPELL_EN";
        }else{
            TABLE_NAME = "BEST_SPELL_TH";
        }*/
        TABLE_NAME = "BEST_SPELL_TH";
        //DictionaryDBOpenHelper.TABLE_NAME = "BEST_SPELL_EN";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Do Notting
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Do Notting
    }

}
