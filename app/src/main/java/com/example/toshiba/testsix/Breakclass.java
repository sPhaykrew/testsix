package com.example.toshiba.testsix;

import android.util.Log;
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
            StringBuffer th = new StringBuffer();
            int start = boundary.first();
                for (int next = boundary.next(); next != java.text.BreakIterator.DONE; start = next, next = boundary.next()) {
                    int sum = next - start;
                    if(sum >= 2){
                    th.append(replace.substring(start, next) + ",");
                    word = th.toString();
                    word1 = word; }
                    else{ next--;}
                }}
        catch(Exception e){
            System.out.println("Error !" + e.getMessage());
        }
        return word1;
    }

    public  ArrayList<Integer> breakHighLight_Start(String i){
        Locale u = new Locale("th");
        ArrayList<Integer> first = new ArrayList<Integer>();
        java.text.BreakIterator boundary = java.text.BreakIterator.getWordInstance(u);
        try {
            replace = i.replace(" ","");
            boundary.setText(replace);
            StringBuffer th = new StringBuffer();
            int start = boundary.first();
                for (int next = boundary.next(); next != java.text.BreakIterator.DONE; start = next, next = boundary.next()) {
                    int sum = next - start;
                    if(sum >= 2){
                    th.append(replace.substring(start, next) + ",");
                    word = th.toString();
                    word1 = word;
                    first.add(start);}
                    else{ next--;}
                }}
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
            StringBuffer th = new StringBuffer();
            int start = boundary.first();
                for (int next = boundary.next(); next != java.text.BreakIterator.DONE; start = next, next = boundary.next()) {
                    int sum = next - start;
                    if(sum >= 2){
                    th.append(replace.substring(start, next) + ",");
                    word = th.toString();
                    word1 = word;
                    second.add(next);}
                    else{ next--; }
                }}
        catch(Exception e){
            System.out.println("Error !" + e.getMessage());
        }
        word.toString();
        return second;
    }

}
