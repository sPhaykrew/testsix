package com.example.toshiba.testsix.soundex;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.example.toshiba.testsix.DatabaseSpellAccess;

import java.util.ArrayList;

/**
 * Created by Lattapol on 16/12/2559.
 */

public class Soundex {

    MissSpellingWord missSpell;
    ArrayList<Part> result;
    Context ctx;

    public Soundex(Context context) {
        this.ctx = context;
    }


    public ArrayList<String> getSoundex(String text) {
        ArrayList<String> result = new ArrayList<String>();

        IgnoreLR LRSwap = new IgnoreLR(ctx);
        ArrayList<String> G2PSound = LRSwap.getIgnoreLR(text); //G2PSound คำในรูปเสียง

        Log.i("G2PSound!!!!!!!!!!!!!S", G2PSound.toString()); //get G2P

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

        StringBuilder str = null;
        String noteFilter;

        //int countpipe = 0;

        int g2pLength = g2p.length();
        if (g2pLength > 1) {
            for (int i = 0; i < g2pLength; i++) {

              //  if (g2p.charAt(i) == '|') {
              //      countpipe++;
              //  }

                if (Character.isDigit(g2p.charAt(i))) {
                    g2p = g2p.substring(0, i) + "%" + g2p.substring(i + 1);
                }


            }
            g2p = g2p.substring(0, g2p.length() - 1).trim();

            str = new StringBuilder(g2p);
            if (g2p.charAt(1) == '-')
            {
                str.insert(1,'%');
            }
            else if (g2p.charAt(2) == '-')
            {
                str.replace(1,2, "%");
            }

        }

        /*if(g2p.contains("|")){
            int pos = g2p.indexOf("|");
            cut1consonant = g2p.substring(0,pos);
        }*/

        //if(g2p.length() != 0) {

            /*for (int i = 0; i < g2pLength - 1; i++)
            {
                if (g2p.charAt(i) == '|')
                {
                    if (g2p.charAt(i + 2) == '-')
                    {
                        str.insert(i+2,'%');
                    }
                }
            }*/
        //}

        g2p = String.valueOf(str);

        noteFilter = "(" + "G2P" + " LIKE '" + g2p + "'";

        //noteFilter += ") AND (LENGTH(G2P) < " + g2pLength * 2 + ")"; //g2pLength พยางค์คำ
        noteFilter += ") AND (LENGTH(G2P) < " + g2pLength  + ")";

        Log.i("NECTECWorldABC--->", noteFilter);

        DatabaseSpellAccess databaseSpellAccess = new DatabaseSpellAccess(ctx);
        databaseSpellAccess.open();
        Cursor cursor = null;
        //DatabaseSpell databaseSpell = null;
        //SQLiteDatabase db = null;
        //databaseSpell = new DatabaseSpell(ctx);
        //db = databaseSpell.getWritableDatabase();
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
                        g2p_word = cursor.getString(cursor.getColumnIndex("SENSEGROUP"));
                        similarWord.add(g2p_word);
                    } while (cursor.moveToNext());
                }
            } databaseSpellAccess.close();
        }
        catch (NullPointerException e)
        {
                e.printStackTrace();
        }
        //Log.i("similarWord!!!!! ", similarWord.toString());
        //TWSActivity.isEnglish = false;
        return similarWord;
    }

}
