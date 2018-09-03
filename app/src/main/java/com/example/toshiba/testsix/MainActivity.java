package com.example.toshiba.testsix;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.RecyclerView;

import com.example.toshiba.testsix.editdistance.Distance;
import com.example.toshiba.testsix.ldrule.AddMissingChar;
import com.example.toshiba.testsix.ldrule.LDRule;
import com.example.toshiba.testsix.soundex.Soundex;
import com.example.toshiba.testsix.soundex.SoundexWord;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.xeoh.android.texthighlighter.TextHighlighter;

import android.widget.ExpandableListView;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/** S.phaykrew 31/05/61 **/

public class MainActivity extends AppCompatActivity {

    ArrayList<String> mylist = new ArrayList<String>();
    ArrayList<String> highLight = new ArrayList<String>();
    int id=1;

    Toolbar toolbar;
    EditText inputText;
    String inputWord;
    SharedPreferences sharedPreferences;

    private String inputSearch = new String();

    private List<sense> senseList = new ArrayList<>();
    private RecyclerView recyclerView;
    private senseRecyclerview senseAdapter;

    Soundex soundex;
    SoundexWord soundexWord;
    ArrayList<String> wordPredictList;
    BreakIterator boundary;
    ArrayList<String> firstLD;
    AddMissingChar missChar;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ImageView Search;
    ImageView Voice;
    ImageView Clear;
    Dialog dialog;

    private static String my_Setting = "my_Setting";
    private static String Thame = "Thame";
    SharedPreferences shared,ThameChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new Dialog(this);

        shared = getSharedPreferences(my_Setting, Context.MODE_PRIVATE);
        ThameChange = getSharedPreferences(Thame, Context.MODE_PRIVATE);

        Locale thaiLocale = new Locale("th");
        boundary = BreakIterator.getWordInstance(thaiLocale);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        inputText = (EditText) findViewById(R.id.intest);
        expListView = (ExpandableListView) findViewById(R.id.recycler1);
        Search = (ImageView) findViewById(R.id.Search);
        Voice = (ImageView) findViewById(R.id.Voice);
        Clear = (ImageView) findViewById(R.id.Clear);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Spell Checker");

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() { //highLight
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if(parent.isGroupExpanded(groupPosition))
                {
                    inputText.setText(inputWord);
                }
                else{ //highLight
                    TextHighlighter textHighlighter = new TextHighlighter();
                    inputText.setText(inputWord);
                    textHighlighter.setBackgroundColor(Color.YELLOW).invalidate(TextHighlighter.BASE_MATCHER);
                    textHighlighter.addTarget(findViewById(R.id.intest));
                    textHighlighter.highlight(listDataHeader.get(groupPosition),
                            TextHighlighter.BASE_MATCHER);
                }
                return false;
            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() { //คลิกแทนข้อความ child ใน inputText
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

//                mylist.set(groupPosition,listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
//                inputText.setText(mylist.toString().replace("[","").replace("]","").replace(" ","" +
//                        "").replace(",","")); // แทนคำแบบ arraylist index

                inputWord = inputWord.replace(listDataHeader.get(groupPosition),
                        listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
                inputText.setText(inputWord);

                listDataHeader.remove(groupPosition); //ลบ header
                listAdapter.notifyDataSetChanged();
                expListView.collapseGroup(groupPosition); // ยุบ expandable

                return false;
            }
        });

        expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { //popUp ความหมาย
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.setContentView(R.layout.sense);
                int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                int childPosition = ExpandableListView.getPackedPositionChild(id);

                String colorPrimaryDark = ThameChange.getString("colorPrimaryDark","FFD54F");
                Button close = (Button) dialog.findViewById(R.id.goBack);
                close.setBackgroundColor((Color.parseColor(colorPrimaryDark)));

                senseList.clear();
                dialog.show();

                try {
                recyclerView = (RecyclerView) dialog.findViewById(R.id.headersense);
                senseAdapter = new senseRecyclerview(senseList,getApplicationContext());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(senseAdapter);
                prepareDatasense(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition)); }
                catch (Exception e){
                    Log.d("Check","Error!");
                    dialog.dismiss();
                    listDataHeader.remove(groupPosition);
                    listAdapter.notifyDataSetChanged();
                                }

                GravitySnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP); //snap recyclerview popup
                snapHelper.attachToRecyclerView(recyclerView);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                return true;
            }
        });

        Search.setOnClickListener(new View.OnClickListener() { // กดค้นหาคำผิดตรงรูป Icon
            @Override
            public void onClick(View v) {
                closeKeyboard();
                if (inputText.length() > 1 ){
                    new SearchEngingTask().execute(); }
                else { Toast.makeText(getApplicationContext(),"กรุณาพิมพ์ข้อความ",Toast.LENGTH_SHORT).show(); }
            }
        });

        Voice.setOnClickListener(new View.OnClickListener() { //กดอ่านข้อความ
            @Override
            public void onClick(View v) {
                if( inputText.length() > 0){
                Speech.getInstance(getApplicationContext()).speak(inputText.getText().toString()); }
                else { Toast.makeText(getApplicationContext(),"ไม่พบข้อความ",Toast.LENGTH_SHORT).show(); }
            }
        });

        Clear.setOnClickListener(new View.OnClickListener() { // clear ข้อความทั้งหมด
            @Override
            public void onClick(View v) {
                inputText.setText(""); inputWord = "";
                expListView.setAdapter((BaseExpandableListAdapter)null);
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();

        int fontSize = shared.getInt("setting",20);
        String colorPrimaryDark = ThameChange.getString("colorPrimaryDark","#FF8F00");
        String colorPrimary = ThameChange.getString("colorPrimary","#FFB300");

        changeStatusBarColor(colorPrimaryDark);
        toolbar.setBackgroundColor((Color.parseColor(colorPrimary)));

        inputText.setTextSize(fontSize);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void attachBaseContext(Context newBase){ //Fonts
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){ //เมนู actionBar
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.exit : System.exit(0); break;
            case R.id.Setting :
                Intent intent = new Intent(MainActivity.this,setting_java.class);
                startActivity(intent);
            case R.id.Manual :
                Intent intent1 = new Intent(MainActivity.this,manual_java.class);
                startActivity(intent1);
        }
        return super.onOptionsItemSelected(item);
    }

    public void Break(View view){//ตัดคำ
                if(inputText.length() > 0){
                    Breakclass breakClass = new Breakclass();
                    inputWord = inputText.getText().toString().replace("|","");
                    inputText.setText(breakClass.Break(inputWord.toString()));
                } else{ Toast.makeText(this,"กรุณาพิมพ์ข้อความ",Toast.LENGTH_SHORT).show();}
            }

    private ArrayList<String> onKey(String input) {

        inputSearch = input;
        wordPredictList = new ArrayList<String>();
        soundex = new Soundex(this);
        soundexWord = new SoundexWord(this);

        if(inputSearch.length() >20){ // mComposing เป็น input // First // มีคำในฐานข้อมูล
            Toast.makeText(this,"กรุณาค้นหาทีละคำ",Toast.LENGTH_LONG).show();
        }else{
            if(inputSearch.length() > 1){

                if(isInDictionary(inputSearch)){
                wordPredictList.add(inputSearch);
                ArrayList<String> similarWord = soundexWord.getSoundex(inputSearch);
                wordPredictList.addAll(similarWord);

                LinkedHashSet<String> lhs = new LinkedHashSet<String>();
                lhs.addAll(wordPredictList);
                wordPredictList.clear();
                wordPredictList.addAll(lhs);

            } else{
                    //new SearchEngingTask().execute(input.toString());
                   wordPredictList = Predict(inputSearch); //กรณีที่ไม่เจอคำในฐานข้อมูล
                }
            }}return wordPredictList;
    }

    private boolean isInDictionary(String word) {
        //เช็คคำในฐานข้อมูล
        DatabaseSpellAccess databaseSpellAccess = new DatabaseSpellAccess(getApplicationContext());
        //DatabaseSpellAccess databaseSpellAccess = DatabaseSpellAccess.getSingle_instance(this);
        databaseSpellAccess.open();
        Cursor c;
        //c = databaseAccess.db.rawQuery("SELECT * FROM BEST_SPELL_TH WHERE SENSEGROUP = '"+word+"'",null);
        c = databaseSpellAccess.queryCheck(word);

        try {
            if (c.getCount() == 0) {
                c.close();
            } else {
                c.close();
                return true;
            } databaseSpellAccess.close();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }  databaseSpellAccess.close();
        return false;
    }

    private void prepareListData() {

        String i; 
        String[] j = null;
            Breakclass breakClass = new Breakclass();
            inputWord = inputText.getText().toString();
            i = breakClass.Break(inputWord);
            j = i.split(",");
            mylist = new ArrayList<String>(Arrays.asList(j));//output break
            highLight = mylist; //highLight Text

        listDataHeader = new ArrayList<String>(); //คำผิด
        listDataChild = new HashMap<String, List<String>>(); //คำที่แก้ไข

        // Adding child data
            for(String key : mylist){
                listDataHeader.add(key);
                ArrayList<String> child = new ArrayList<String>();
                child = onKey(key);
                listDataChild.put(key,child);
            }
    }

    private class SearchEngingTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... para) {
            Snackbar snackbar = Snackbar.make(Search, "กรุณารอสักครู่...", Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
            prepareListData();
            snackbar.dismiss();
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            try{
            super.onPostExecute(s);
            listAdapter = new ExpandableListAdapter(getApplicationContext(), listDataHeader, listDataChild);
            expListView.setAdapter(listAdapter); }
            catch (Exception e){
               e.printStackTrace();
            }
        }

    }

    private static int countWord(BreakIterator boundary, String source) {

        int wordCount = 0;
        int start = boundary.first();
        for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
            wordCount++;
        }
        return wordCount;
    }

    private ArrayList<String> Predict(String onkey){

        //Call Soundex**********************************************
        ArrayList<String> similarWord = soundex.getSoundex(onkey);
        //Log.d("smilarWord predict",similarWord.toString());

        //Count syllable
        boundary.setText(onkey);
        int wordCount = countWord(boundary, onkey);

        ArrayList<String> LDWord = new ArrayList<String>();

        ArrayList<String> delRule = new ArrayList<String>();
        ArrayList<String> delSoundex;
        ArrayList<String> allDelSoundex = new ArrayList<String>();

        ArrayList<String> missingChar;

        Log.i("No.of Syllable--->: ", String.valueOf(wordCount));
        LDRule search = new LDRule();

        if (wordCount == 1) {

            firstLD = search.findWordWithLDLaw(onkey);
            int wordSize = firstLD.size();

            for (int i = 0; i < wordSize; i++) {
                if (isInDictionary(firstLD.get(i))) {
                    LDWord.add(firstLD.get(i));
                }
            }
            //Log.i("LDWord--->", String.valueOf(LDWord));

            delRule.addAll(search.deleteChar(onkey));
            for (int i = 0; i < delRule.size(); i++) {
                delSoundex = soundex.getSoundex(delRule.get(i));
                allDelSoundex.addAll(delSoundex);

            }
            //Log.i("allDelSoundex--->", String.valueOf(allDelSoundex));

            LDWord.addAll(allDelSoundex);

        } else {

            //LDWord.addAll(missChar.findMissingChar(para[0]));
            AddMissingChar addMissingChar = new AddMissingChar(getApplicationContext());
            missingChar = addMissingChar.findMissingChar(onkey);
            Log.i("missingChar--->", String.valueOf(missingChar));

            for (int i = 0; i < missingChar.size(); i++) {
                boundary.setText(missingChar.get(i));
                if (wordCount - 1 <= countWord(boundary, missingChar.get(i))
                        || wordCount + 1 >= countWord(boundary, missingChar.get(i))) {
                    //Log.i("wordCountwordCount--->", String.valueOf(countWord(boundary, missingChar.get(i))));
                    LDWord.add(missingChar.get(i));
                }

            }
            //Log.i("LDWord2:--->", String.valueOf(LDWord));
        }

        Distance sortWord = new Distance(LDWord, onkey);
        LDWord = sortWord.getEditDistance();

        //Log.i("similarWordsim--->", String.valueOf(similarWord));
        ArrayList<String> PredictList = new ArrayList<String>();

        PredictList.addAll(similarWord); //from above soundex
        PredictList.addAll(LDWord);
        //Remove duplicate word
        LinkedHashSet<String> lhs = new LinkedHashSet<String>();
        lhs.addAll(PredictList);
        PredictList.clear();
        PredictList.addAll(lhs);

        return PredictList;

    }

    private void prepareDatasense(String word){
        int Eng = 0;
        int p =1;
        ArrayList<String> senseTH = new ArrayList<String>();
        ArrayList<String> senseENG = new ArrayList<String>();
        DatabaseAccess databaseAccess = new DatabaseAccess(getApplicationContext());
        databaseAccess.open();
        senseTH = databaseAccess.senseTH(word);
        senseENG = databaseAccess.senseEng(word);
        for(String i : senseTH) {
            sense sense = new sense(p +"."+ word,"ไทย : " + i,"อังกฤษ : "+senseENG.get(Eng));
            senseList.add(sense);
            Eng++; p++;
        }
            databaseAccess.close();
            senseAdapter.notifyDataSetChanged();
    }

    private void closeKeyboard(){ //ซ่อนคีบอร์ด
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager ip = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            ip.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    //////////////////////////////////////////////////////////////////////////////

    //Expandable adpter
    public class ExpandableListAdapter extends BaseExpandableListAdapter{

        private Context _context;
        private List<String> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<String, List<String>> _listDataChild;

        public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                     HashMap<String, List<String>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            int fontSize = shared.getInt("setting",24); //setting font size

            final String childText = (String) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.expandable_recyclerview, null);
            }

            ImageView buttonVoice = (ImageView) convertView
                    .findViewById(R.id.buttonVoice);
            final TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.expandable);

            buttonVoice.setOnClickListener(new View.OnClickListener() { //อ่านข้อความ child
                @Override
                public void onClick(View v) {
                    Speech.getInstance(_context).speak(childText);
                }
            });

            Typeface regular = Typeface.createFromAsset(_context.getAssets(),
                    "fonts/THSarabunNew.ttf");
            txtListChild.setTypeface(regular, Typeface.NORMAL);

            txtListChild.setTextSize(fontSize);//setting font size

            txtListChild.setText(childText);
            buttonVoice.setImageResource(R.drawable.ic_volume_up_black_24dp);

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {

            int fontSize = shared.getInt("setting",24); //setting font size
            String colorAccent = ThameChange.getString("colorAccent","FFD54F");

            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.listviewrecycler, null);
            }

            TextView lblListHeader = (TextView) convertView.findViewById(R.id.textlayout);
            Typeface regular = Typeface.createFromAsset(_context.getAssets(),
                    "fonts/THSarabunNew.ttf");
            lblListHeader.setTypeface(regular, Typeface.NORMAL);

            lblListHeader.setTextSize(fontSize);

            //lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            convertView.setBackgroundColor((Color.parseColor(colorAccent)));

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    //senseRecyclerview adpter
    public class senseRecyclerview extends RecyclerView.Adapter<senseRecyclerview.MyViewHolder> {

        private List<sense> senseList;
        public Context context;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView word, senseTH, senseEng;
            public ImageView voiceTH,voiceEng;

            public MyViewHolder(View view) {
                super(view);

                int senseFontsHeader = shared.getInt("senseFontsHeader",22); //setting font size
                int senseFonts = shared.getInt("senseFonts",18); //setting font size

                word = (TextView) view.findViewById(R.id.headerWord);
                word.setBackgroundColor(000000);
                word.setTextSize(senseFontsHeader);

                senseTH = (TextView) view.findViewById(R.id.senseTh);
                senseTH.setBackgroundColor(000000);
                senseTH.setTextSize(senseFonts);

                senseEng = (TextView) view.findViewById(R.id.senseEng);
                senseEng.setBackgroundColor(000000);
                senseEng.setTextSize(senseFonts);

                voiceTH = (ImageView) view.findViewById(R.id.imageView1);
                voiceEng = (ImageView) view.findViewById(R.id.imageView2);
            }
        }

        public senseRecyclerview(List<sense> moviesList,Context context) {
            this.senseList = moviesList;
            this.context = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.senserecyclerview, parent, false);

            itemView.setBackgroundColor(000000);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(senseRecyclerview.MyViewHolder holder, int position) {
            final sense sense = senseList.get(position);
            holder.word.setText(sense.getWord());
            holder.senseTH.setText(sense.getSenseTH());
            holder.senseEng.setText(sense.getSenseEng());

            holder.voiceTH.setOnClickListener(new View.OnClickListener() { //อ่านความหมายภาษาไทย
                @Override
                public void onClick(View v) {
                    Speech.getInstance(context).speak(sense.getSenseTH().substring(6));
                }
            });

            holder.voiceEng.setOnClickListener(new View.OnClickListener() { //อ่านคำภาษาอังกฤษ
                @Override
                public void onClick(View v) {
                    Speech.getInstance(context).speak(sense.getSenseEng().substring(9));
                }
            });

        }

        @Override
        public int getItemCount() {
            return senseList.size();
        }
    }

    private void changeStatusBarColor(String color){
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }
}


