// Decompiled by JEB v1.5.201408040 - http://www.android-decompiler.com

package com.agame.http;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager.WakeLock;

import com.agame.TRecevier;

public class Wksvr extends Service {
        public boolean isregiset;
        WakeLock wakelock;

        public Wksvr() {
                super();
                this.isregiset = false;
        }

        public IBinder onBind(Intent intent) {
                return null;
        }

        public void onCreate() {
                super.onCreate();
        }

        public void onDestroy() {
                super.onDestroy();
                if (this.wakelock != null) {
                        this.wakelock.release();
                        this.wakelock = null;
                }
        }

        public void onStart(Intent intent, int startid) {
                super.onStart(intent, startid);
                new ServiceThread(((Context) this), TRecevier.class).start();
        }
}
