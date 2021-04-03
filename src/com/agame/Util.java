// Decompiled by JEB v1.5.201408040 - http://www.android-decompiler.com

package com.agame;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class Util {
        public static boolean isWorked(Context context, String str) {
                ActivityManager manager = (ActivityManager) context.getSystemService("activity");
                List<RunningServiceInfo> list = manager.getRunningServices(30);
                if (list.size() == 0) {
                        return false;
                }
                if (str == null || "".equals(str)) {
                        return false;
                }
                for (RunningServiceInfo runningServiceInfo : list) {
                        String className = runningServiceInfo.service.getClassName();
                        if (str.equals(className)) {
                                return true;
                        }
                }
                return false;
        }
}
