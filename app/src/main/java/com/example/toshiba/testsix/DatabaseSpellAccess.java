package com.example.toshiba.testsix;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseSpellAccess {

    public SQLiteDatabase db;
    private DatabaseSpell openHelper;
    public Cursor c = null;
    static String word1 = null;
    String word,replace;

    public DatabaseSpellAccess(Context context) {
        openHelper = new DatabaseSpell(context);
    }

    public void open(){
        db = openHelper.getWritableDatabase();
    }

    public void close(){
        if(db.isOpen()){
        openHelper.close();}
    }

    public Cursor queryCheck(String search){
      c = db.rawQuery("SELECT * FROM BEST_SPELL_TH WHERE SENSEGROUP = '"+search+"'",null);
      return c;
    }

    public Cursor queryWrong(String search){
        c = db.rawQuery("SELECT * FROM BEST_SPELL_TH WHERE "+search+"",null);
        return c;
    }

    public Cursor set(){
        c = db.rawQuery("SELECT * FROM BEST_SPELL_TH",null);
        return c;
    }
}
