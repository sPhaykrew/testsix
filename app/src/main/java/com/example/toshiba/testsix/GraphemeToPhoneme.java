package com.example.toshiba.testsix;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.example.bablueza.g2p.g2pJni;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Lattapol on 29/11/2559.
 */

public class GraphemeToPhoneme {

    private g2pJni g2p = null;
    private int stG2P = 0;
    private String deviceID = "";
    private String account = "";
    Context ctx;

    public GraphemeToPhoneme(Context context){
        this.ctx = context;
        g2p = new g2pJni();
        deviceID = G2PApp.getDeviceID(ctx);
    }

    public String getG2P(String grapheme){
        downloadVoiceData();
        stG2P = g2p.initG2P(G2PApp.G2P_DATA_PATH+"initTTS.in",G2PApp.G2P_DATA_PATH,deviceID);

        if(!grapheme.equals("")) {
            if(stG2P==0) {
                stG2P = g2p.initG2P(G2PApp.G2P_DATA_PATH+"initTTS.in",G2PApp.G2P_DATA_PATH,deviceID);
                if(stG2P==1)
                {
                    return g2p.getPhoneme(grapheme);
                }
            }
            else {
                return g2p.getPhoneme(grapheme);
            }
        }
        return "";
    }

    public void downloadVoiceData() {
        if(!G2PApp.checkData())
        {
            Intent intent = new Intent(ctx, DownloadDataActivity.class);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        }
    }
}
