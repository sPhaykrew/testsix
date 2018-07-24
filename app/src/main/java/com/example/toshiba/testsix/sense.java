package com.example.toshiba.testsix;

public class sense {

    private String word,senseTH,senseEng;

    public sense(String word, String senseTH, String senseEng) {
        this.word = word;
        this.senseTH = senseTH;
        this.senseEng = senseEng;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getSenseTH() {
        return senseTH;
    }

    public void setSenseTH(String senseTH) {
        this.senseTH = senseTH;
    }

    public String getSenseEng() {
        return senseEng;
    }

    public void setSenseEng(String senseEng) {
        this.senseEng = senseEng;
    }
}
