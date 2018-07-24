package com.example.toshiba.testsix.soundex;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.toshiba.testsix.DatabaseSpell;
import com.example.toshiba.testsix.DatabaseSpellAccess;
import com.example.toshiba.testsix.DictionaryDBOpenHelper;
import com.example.toshiba.testsix.DictionaryProvider;

import java.util.ArrayList;


public class SoundexWord {

    MissSpellingWord missSpell;
    ArrayList<Part> result;
    Context ctx;


    public SoundexWord(Context context) {
        this.ctx = context;
    }


    public ArrayList<String> getSoundex(String text) {
        ArrayList<String> result = new ArrayList<String>();

        IgnoreLR LRSwap = new IgnoreLR(ctx);
        ArrayList<String> G2PSound = LRSwap.getIgnoreLR(text);

        Log.i("G2PSound!!!!!!!!!!!!!", G2PSound.toString()); //get G2P

        ArrayList<String> similarWord = new ArrayList<String>();
        for (int i = 0; i < G2PSound.size(); i++) {
            if (G2PSound.get(i).contains("r")) {

                similarWord.addAll(isInDictionary(G2PSound.get(i).replace("r", "")));
            }
            similarWord.addAll(isInDictionary(G2PSound.get(i)));
            result.addAll(similarWord);
        }

        return result;
    }

    private ArrayList<String> isInDictionary(String g2p) {

        String noteFilter;

        int g2pLength = g2p.length();
        if (g2pLength > 1) {
            for (int i = 0; i < g2pLength; i++) {

                if (Character.isDigit(g2p.charAt(i))) {
                    g2p = g2p.substring(0, i) + "%" + g2p.substring(i + 1);
                }
            }
            g2p = g2p.substring(0, g2p.length() - 1).trim();
        }

        noteFilter = "(" + DictionaryDBOpenHelper.WORD_G2P + " LIKE '" + g2p + "'";


        noteFilter += ") AND (LENGTH(G2P) < " + g2pLength + ")";
        //noteFilter += ") AND (LENGTH(G2P) < " + g2pLength * 2 + ")"; g2pLength คือพยางค์คำ
        Log.i("NECTECWorldABC--->", noteFilter); //(G2P LIKE '') AND (LENGTH(G2P) < 0)

//        Cursor cursor = ctx.getContentResolver().query(DictionaryProvider.CONTENT_URI,
//                    DictionaryDBOpenHelper.ALL_COLUMNS, noteFilter, null, null);

        DatabaseSpellAccess databaseSpellAccess = new DatabaseSpellAccess(ctx);
        databaseSpellAccess.open();
        Cursor cursor = null;
//        DatabaseSpell databaseSpell = null;
//        SQLiteDatabase db;
//        databaseSpell = new DatabaseSpell(ctx);
//        db = databaseSpell.getWritableDatabase();
        //cursor = databaseSpellAccess.db.rawQuery("SELECT * FROM BEST_SPELL_TH WHERE "+noteFilter+"",null);
        cursor = databaseSpellAccess.queryWrong(noteFilter);

        ArrayList<String> similarWord = new ArrayList<String>();

        try {
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.moveToFirst()) {
                    do
                    {
                        String g2p_word;
                        g2p_word = cursor.getString(cursor.getColumnIndex(DictionaryDBOpenHelper.WORD_VOCAB));
                        similarWord.add(g2p_word);
                    } while (cursor.moveToNext());
                }
            } databaseSpellAccess.close();
        }
        catch (NullPointerException e)
        {
                e.printStackTrace();
        }
        Log.i("similarWord!!!!! ", similarWord.toString());
        //TWSActivity.isEnglish = false;
        return similarWord;
    }

}
