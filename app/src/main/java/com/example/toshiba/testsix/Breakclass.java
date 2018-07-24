package com.example.toshiba.testsix;

import java.util.ArrayList;
import java.util.Locale;

public class Breakclass {
    String replace;
    static String word1 = null;
    String word;


    public  String Break(String i){
        Locale u = new Locale("th");
        java.text.BreakIterator boundary = java.text.BreakIterator.getWordInstance(u);
        try {
            replace = i.replace(" ","");
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
            System.out.println("Error !" + e.getMessage());
        }
        word.toString();
        return word1;
    }

    public  ArrayList<Integer> breakHighLight_Start(String i){
        Locale u = new Locale("th");
        ArrayList<Integer> first = new ArrayList<Integer>();
        java.text.BreakIterator boundary = java.text.BreakIterator.getWordInstance(u);
        try {
            replace = i.replace(" ","");
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
                    first.add(start);

                }}}
        catch(Exception e){
            System.out.println("Error !" + e.getMessage());
        }
        word.toString();
        return first;
    }

    public  ArrayList<Integer> breakHighLight_End(String i){
        Locale u = new Locale("th");
        ArrayList<Integer> second = new ArrayList<Integer>();
        java.text.BreakIterator boundary = java.text.BreakIterator.getWordInstance(u);
        try {
            replace = i.replace(" ","");
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
                    second.add(pp);

                }}}
        catch(Exception e){
            System.out.println("Error !" + e.getMessage());
        }
        word.toString();
        return second;
    }

}
