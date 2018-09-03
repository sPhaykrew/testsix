package com.example.toshiba.testsix;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class setting_java extends AppCompatActivity {

    Toolbar toolbar;
    RadioButton Font1,Font2,Font3,BG1,BG2,BG3,BG4;
    SharedPreferences shared,ThameChange;
    ImageView themeBgImageView,backMain;
    TextView preText;

    private static String my_Setting = "my_Setting";
    private static String Thame = "Thame";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        shared  = getSharedPreferences(my_Setting, Context.MODE_PRIVATE);
        ThameChange  = getSharedPreferences(Thame, Context.MODE_PRIVATE);

        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        Font1 = (RadioButton) findViewById(R.id.Font1);
        Font2 = (RadioButton) findViewById(R.id.Font2);
        Font3 = (RadioButton) findViewById(R.id.Font3);
        BG1 = (RadioButton) findViewById(R.id.BG1);
        BG2 = (RadioButton) findViewById(R.id.BG2);
        BG3 = (RadioButton) findViewById(R.id.BG3);
        BG4 = (RadioButton) findViewById(R.id.BG4);
        preText = (TextView) findViewById(R.id.preText);
        backMain = (ImageView) findViewById(R.id.backMain);

        themeBgImageView = (ImageView) findViewById(R.id.themeBgImageView);

        Font1.setOnClickListener(fontClick);
        Font2.setOnClickListener(fontClick);
        Font3.setOnClickListener(fontClick);

        BG1.setOnClickListener(BGClick);
        BG2.setOnClickListener(BGClick);
        BG3.setOnClickListener(BGClick);
        BG4.setOnClickListener(BGClick);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Spell Checker");

        String colorPrimaryDark = ThameChange.getString("colorPrimaryDark","#FF8F00");
        String colorPrimary = ThameChange.getString("colorPrimary","#FFB300");
        changeStatusBarColor(colorPrimaryDark);
        toolbar.setBackgroundColor((Color.parseColor(colorPrimary)));

        int Check = shared.getInt("setting",1);
        preText.setTextSize(Check);

        switch (Check) {
            case 16 : Font1.setChecked(true);break;
            case 24 : Font2.setChecked(true);break;
            case 32 : Font3.setChecked(true);break;
        }

        int checkThame = ThameChange.getInt("checkThame",0);
        switch (checkThame){
            case 1 : BG1.setChecked(true); themeBgImageView.setImageResource(R.drawable.pre1); break;
            case 2 : BG2.setChecked(true); themeBgImageView.setImageResource(R.drawable.pre2); break;
            case 3 : BG3.setChecked(true); themeBgImageView.setImageResource(R.drawable.pre4); break;
            case 4 : BG4.setChecked(true); themeBgImageView.setImageResource(R.drawable.pre3); break;
        }

        backMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase){ //Fonts
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public View.OnClickListener fontClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            selectFont();
        }
    };

    public View.OnClickListener BGClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            selectThame();
        }
    };

    public void selectFont(){ //sizeFonts
        SharedPreferences.Editor editor = shared.edit();

        if(Font1.isChecked()){
            preText.setTextSize(16);
            editor.putInt("setting",16);
            editor.putInt("senseFontsHeader",18);
            editor.putInt("senseFonts",16);
            editor.commit(); }

            else if (Font2.isChecked()) {
                preText.setTextSize(24);
                editor.putInt("setting",24);
                editor.putInt("senseFontsHeader",22);
                editor.putInt("senseFonts",18);
                editor.commit(); }

            else if(Font3.isChecked()) {
                preText.setTextSize(32);
                editor.putInt("setting",32);
                editor.putInt("senseFontsHeader",25);
                editor.putInt("senseFonts",22);
                editor.commit(); }
    }

    public void selectThame(){ //Thame
        SharedPreferences.Editor editor = ThameChange.edit();
        if(BG1.isChecked()){
            themeBgImageView.setImageResource(R.drawable.pre1);
            editor.putString("colorPrimaryDark","#FF8F00");
            editor.putString("colorPrimary","#FFB300");
            editor.putString("colorAccent","#FFD54F");
            editor.putInt("checkThame",1);
            editor.commit();

            changeStatusBarColor("#FF8F00");
            toolbar.setBackgroundColor((Color.parseColor("#FFB300")));

        } else if (BG2.isChecked()){
            themeBgImageView.setImageResource(R.drawable.pre2);
            editor.putString("colorPrimaryDark","#689F38");
            editor.putString("colorPrimary","#8BC34A");
            editor.putString("colorAccent","#9CCC65");
            editor.putInt("checkThame",2);
            editor.commit();

            changeStatusBarColor("#689F38");
            toolbar.setBackgroundColor((Color.parseColor("#8BC34A")));

        } else if (BG3.isChecked()){
            themeBgImageView.setImageResource(R.drawable.pre4);
            editor.putString("colorPrimaryDark","#039BE5");
            editor.putString("colorPrimary","#29B6F6");
            editor.putString("colorAccent","#81D4FA");
            editor.putInt("checkThame",3);
            editor.commit();

            changeStatusBarColor("#039BE5");
            toolbar.setBackgroundColor((Color.parseColor("#29B6F6")));

        } else if (BG4.isChecked()){
            themeBgImageView.setImageResource(R.drawable.pre3);
            editor.putString("colorPrimaryDark","#E57373");
            editor.putString("colorPrimary","#EF9A9A");
            editor.putString("colorAccent","#FFCDD2");
            editor.putInt("checkThame",4);
            editor.commit();

            changeStatusBarColor("#E57373");
            toolbar.setBackgroundColor((Color.parseColor("#EF9A9A")));
        }
    }

    private void changeStatusBarColor(String color) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }
}
