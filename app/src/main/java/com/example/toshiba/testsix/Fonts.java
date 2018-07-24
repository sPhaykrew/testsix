package com.example.toshiba.testsix;

import android.app.Application;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Fonts extends Application { //เปลี่ยน font

    @Override
    public void onCreate(){
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/THSarabunNew.ttf")
                .setFontAttrId(R.attr.fontPath).build());
    }
}
