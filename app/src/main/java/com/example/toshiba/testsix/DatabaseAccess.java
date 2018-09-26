package com.example.toshiba.testsix;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Locale;

public class DatabaseAccess {

    SQLiteDatabase db;
    private DatabaseOpenHelper openHelper;
    Cursor c = null;
    static String word1 = null;
    String word,replace;

    public DatabaseAccess(Context context) {
       openHelper = new DatabaseOpenHelper(context);
    }

    public void open(){
        db = openHelper.getWritableDatabase();
    }

    public void close(){
        openHelper.close();
    }

    public ArrayList<String> getAll(String i){ //query ธรรมดา
        ArrayList<String> todoList = new ArrayList<String>();
        c = db.rawQuery("SELECT * FROM lexitron WHERE sentry = '"+i+"'",null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            todoList.add(c.getString(1));
            c.moveToNext();
        }
        c.close();
        return todoList;
    }

    public ArrayList<String> op(String i){ //ตัดคำแล้วนำคำผิดไปเข้า db lexitron เพื่อหาคำที่ไม่มีใน db

        Locale u = new Locale("th");
        java.text.BreakIterator boundary = java.text.BreakIterator.getWordInstance(u);
        try {
            replace = i.replaceAll(" ","");
            boundary.setText(replace);
            int yy = 0;
            int t = 0;
            StringBuffer th = new StringBuffer();
            int start = boundary.first();
            while (t == yy) {
                yy++;
                for (int pp = boundary.next(); pp != java.text.BreakIterator.DONE; start = pp, pp = boundary.next()) {
                    th.append(replace.substring(start, pp) + ",");
                    word = th.toString();
                    word1 = word;
                }}}
        catch(Exception e){
            System.out.println("Error !" + e.getMessage());}

        ArrayList<String> wrong = new ArrayList<String>();
try{
        String[] cut = word1.split(",");
        String r = "test";
        String[] yy = new String[200];
        ArrayList<String> todoList = new ArrayList<String>();
        for(int p = 0; p<cut.length ; p++){
            c = db.rawQuery("SELECT * FROM lexitron WHERE sentry = '"+cut[p]+"'",null);
            if(c.getCount() == 0){  //Check null
                wrong.add(cut[p]);
                }
        } c.close();
     } catch(Exception e){e.printStackTrace();}
    return wrong;}

    public ArrayList<String> senseTH(String childwotd){
        ArrayList<String> senseChildTH = new ArrayList<String>();
        c = db.rawQuery("SELECT * FROM lexitron WHERE sentry = '"+childwotd+"'",null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            senseChildTH.add(c.getString(3));
            c.moveToNext();
        }
        c.close();
        return senseChildTH;
    }

    public ArrayList<String> senseEng(String childwotd){
        ArrayList<String> senseChildEng = new ArrayList<String>();
        c = db.rawQuery("SELECT * FROM lexitron WHERE sentry = '"+childwotd+"'",null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            senseChildEng.add(c.getString(7));
            c.moveToNext();
        }
        c.close();
        return senseChildEng;
    }


}



