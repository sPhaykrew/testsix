package com.example.toshiba.testsix;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class senseRecyclerview extends RecyclerView.Adapter<senseRecyclerview.MyViewHolder> {

    private List<sense> senseList;
    public Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView word, senseTH, senseEng;
        public ImageView voiceTH,voiceEng;

        public MyViewHolder(View view) {
            super(view);
            word = (TextView) view.findViewById(R.id.headerWord);
            senseTH = (TextView) view.findViewById(R.id.senseTh);
            senseEng = (TextView) view.findViewById(R.id.senseEng);
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

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
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

