package com.example.toshiba.testsix;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class DownloadDataActivity extends Activity {
    private static String TAG = "G2P DDA";
    private static boolean stDownload = false;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_data);

        DownloadFileAsync DFA = new DownloadFileAsync();
        DFA.execute(G2PApp.G2P_URL);
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Downloading file..");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }

    public void onBackPressed() {
        finish();
    }

    class DownloadFileAsync extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        protected String doInBackground(String... aurl) {
            checkFolder();
            String result = postURL(aurl[0]);
            Log.i(TAG, "Result URl: " + result);
            try {
                JSONObject jsonResult = new JSONObject(result);
               JSONObject jsonResponse = jsonResult.getJSONObject("return");
                String jsonStatus = jsonResponse.getString("status");
                String jsonMessage = jsonResponse.getString("message");
                Log.i(TAG, "json Status: " + jsonStatus);
                if (jsonStatus.equals("success")) {
                    JSONObject jsonURL = jsonResult.getJSONObject("url");
                    String jasonKey = jsonURL.getString("key");
                    String jasonData = jsonURL.getString("voice");
                    Log.i(TAG, "json Data: " + jasonData);
                    Log.i(TAG, "json Key: " + jasonKey);
                    if(downloadData(jasonData,G2PApp.G2P_DATA_PATH+G2PApp.G2P_ZIP_FILE))
                    {
                        if(downloadData(jasonKey,G2PApp.G2P_DATA_PATH+G2PApp.G2P_KEY_FILE))
                            stDownload = true;
                    }
                }
            }
            catch (Exception e) {
                Log.e(TAG, "Error: " + e.toString());
            }

            return null;
        }

        private String postURL(String urlInput)
        {
            String deviceId = G2PApp.getDeviceID(DownloadDataActivity.this);
            String account = G2PApp.getAccount(DownloadDataActivity.this);
            String deviceName = G2PApp.getDeviceName();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("device", deviceId));
            nameValuePairs.add(new BasicNameValuePair("account", account));
            nameValuePairs.add(new BasicNameValuePair("build", deviceName));Log.d("DeviceName",nameValuePairs.toString());

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urlInput);
            BufferedReader in = null;
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
                HttpResponse response = httpclient.execute(httppost);
                in = new BufferedReader(new InputStreamReader(response
                        .getEntity().getContent()));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }
                in.close();
                String page = sb.toString();
                return page;
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
            return "";
        }

        private boolean downloadData(String urlInput, String outputFile)
        {
            try {
                URL url = new URL(urlInput);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                Log.i(TAG, "Lenght of file: " + lenghtOfFile);

                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(outputFile);

                byte data[] = new byte[1024];

                long total = 0;

                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            }
            catch (Exception e) {
                Log.e(TAG,"Error: Download file.");
                return false;
            }
            return true;
        }

        protected void onProgressUpdate(String... progress) {
//            Log.i(TAG,progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            if(stDownload) {
                Log.i(TAG, "ExtractFileAsync -> start");
                Intent intent = new Intent(DownloadDataActivity.this, ExtractActivity.class);
                startActivity(intent);
                Log.i(TAG, "ExtractFileAsync -> end");
            }
            Log.i(TAG,"finish");
            finish();
        }

        protected void checkFolder()
        {
            File dir = new File(G2PApp.G2P_DATA_PATH);
            if(!dir.exists())
                dir.mkdir();
        }
    }
}
