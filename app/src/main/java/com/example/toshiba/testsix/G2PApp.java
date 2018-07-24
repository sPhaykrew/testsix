package com.example.toshiba.testsix;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.os.Build;
import java.util.Locale;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import android.os.Environment;

public class G2PApp {
    protected static String ROOT_DIR=Environment.getExternalStorageDirectory().toString();
    protected static String G2P_DATA_DIR = "/G2P-data/";
    public static String G2P_DATA_PATH = ROOT_DIR+G2P_DATA_DIR;
//    protected static String G2P_URL = "http://vaja.nectec.or.th/vaja_android/G2P-data.zip";
//    protected static String G2P_URL = "http://localhost:8080/G2PAndroid/files/G2P-data.zip";
//    protected static String G2P_URL = "http://10.225.62.95:8080/G2PAndroid/files/G2P-data.zip";
//    protected static String G2P_URL = "http://10.225.62.95:8080/G2PAndroid/register.php";
    protected static String G2P_URL = "http://spt-g2p.openservice.in.th/G2PAndroid/register.php";
    protected static String G2P_ZIP_FILE = "g2p-data.zip";
    protected static String G2P_KEY_FILE = "data.dat";
    protected static long G2P_SIZE_DATA = 9735342;
//    protected static long G2P_SIZE_DATA = 41261092; //vaja-data

    public static String getDeviceID(Context context) {
        TelephonyManager telephonyManager  = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        ContentResolver contentResolver = context.getContentResolver();
        String uid = telephonyManager.getDeviceId();
        if (uid == null) {
            uid = android.provider.Settings.Secure.getString(
                    contentResolver,
                    android.provider.Settings.Secure.ANDROID_ID);
        }
        if (uid.length() < 24) {
            int lenuid = 24 - uid.length();
            for (int i = 0; i < lenuid; i++) {
                uid = "0" + uid;
            }
        } else if (uid.length() > 24) {
            uid = uid.substring(uid.length() - 24, uid.length());
        }
        return uid;
    }
    public static String getAccount(Context context) {
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> possibleEmails = new LinkedList<String>();
        for (Account account : accounts) {
            possibleEmails.add(account.name);
        }
        if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
            String email = possibleEmails.get(0);
            String[] parts = email.split("@");
            if (parts.length > 1)
                return parts[0];
        }
        return "";
    }
    protected static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String device = Build.DEVICE;
        if (device.toLowerCase(Locale.US).startsWith(manufacturer.toLowerCase(Locale.US))) {
            return capitalize(device);
        } else {
            return capitalize(manufacturer) + " " + device;
        }
    }
    public static boolean checkData()
    {
        File dir = new File(G2P_DATA_PATH);
        if(!dir.exists())
        {
            return false;
        }
        return true;
    }
    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}
