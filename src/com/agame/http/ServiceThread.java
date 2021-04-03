// Decompiled by JEB v1.5.201408040 - http://www.android-decompiler.com

package com.agame.http;

import android.content.Context;

public class ServiceThread extends Thread {
    public Context ctx;
    public Class mClass;

    public ServiceThread(Context _ctx, Class mClass) {
        super();
        this.ctx = _ctx;
        this.mClass = mClass;
    }

    public void run() {
        ServiceInstance.run(this.ctx, this.mClass);
    }
}

