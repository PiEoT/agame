// Decompiled by JEB v1.5.201408040 - http://www.android-decompiler.com

package com.agame.http.common;

import android.util.Log;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public final class RootShell {
        private static final String TAG = "RootCmd";

        public RootShell() {
                super();
        }

        public static ReturnValue execCmdAsRoot(String cmd) {
        DataOutputStream v3;
        Process v6;
        ReturnValue v7 = new ReturnValue();
        String v8 = "";
        DataOutputStream v2 = null;
        try {
            v6 = Runtime.getRuntime().exec("su");
            v3 = new DataOutputStream(v6.getOutputStream());
            goto label_11;
        }
        catch(Throwable v9) {
        }
        catch(Exception v4) {
            goto label_50;
            try {
            label_11:
                Log.i("RootCmd", cmd);
                v3.writeBytes(String.valueOf(cmd) + "\n");
                v3.flush();
                v3.writeBytes("exit\n");
                v3.flush();
                BufferedReader v1 = new BufferedReader(new InputStreamReader(v6.getInputStream()));
            }
            catch(Throwable v9) {
                v2 = v3;
                goto label_58;
            }
            catch(Exception v4) {
                v2 = v3;
                goto label_50;
            }

            try {
                String v5;
                for(v5 = v1.readLine(); v5 != null; v5 = v1.readLine()) {
                    v8 = String.valueOf(v8) + "\r\n" + v5;
                }

                v7.body = v8;
                v6.waitFor();
                v7.code = v6.exitValue();
                if(v3 == null) {
                    return v7;
                }
            }
            catch(Exception v4) {
                goto label_81;
            }
            catch(Throwable v9) {
                goto label_74;
            }

            try {
                v3.close();
            }
            catch(IOException v4_1) {
                v4_1.printStackTrace();
            }

            return v7;
        label_81:
            v2 = v3;
            try {
            label_50:
                v4.printStackTrace();
                if(v2 == null) {
                    return v7;
                }
            }
            catch(Throwable v9) {
                goto label_58;
            }
        }

        try {
            v2.close();
        }
        catch(IOException v4_1) {
            v4_1.printStackTrace();
        }

        return v7;
    label_74:
        v2 = v3;
    label_58:
        if(v2 != null) {
            try {
                v2.close();
            }
            catch(IOException v4_1) {
                v4_1.printStackTrace();
            }
        }

        throw v9;
    }

        public static boolean isSuExists() {
                boolean v5 = true;
                String[] v2 = new String[] {"/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/", "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"};
                int v8 = v2.length;
                int v7;
                for (v7 = 0; v7 < v8; ++v7) {
                        String v1 = RootShell.runCmd("ls -l " + v2[v7] + " su").body;
                        if (v1 != null && (v1.contains("root")) && !v1.toLowerCase().contains("directory")) {
                                return v5;
                        }
                }

                return false;
        }

        public static ReturnValue runCmd(String cmd) {
        BufferedReader v1;
        Process v5;
        ReturnValue v6 = new ReturnValue();
        String v7 = "";
        BufferedReader v0 = null;
        try {
            v5 = Runtime.getRuntime().exec(cmd);
            v1 = new BufferedReader(new InputStreamReader(v5.getInputStream()));
            goto label_11;
        }
        catch(Throwable v8) {
        }
        catch(Exception v2) {
            goto label_38;
            try {
            label_11:
                String v4;
                for(v4 = v1.readLine(); v4 != null; v4 = v1.readLine()) {
                    v7 = String.valueOf(v7) + "\r\n" + v4;
                }

                v6.body = v7;
                v5.waitFor();
                v6.code = v5.exitValue();
                if(v1 == null) {
                    goto label_20;
                }
            }
            catch(Exception v2) {
                goto label_60;
            }
            catch(Throwable v8) {
                goto label_57;
            }

            try {
                v1.close();
            }
            catch(IOException v2_1) {
                v2_1.printStackTrace();
            }

            goto label_20;
        label_60:
            v0 = v1;
            try {
            label_38:
                v2.printStackTrace();
                if(v0 == null) {
                    goto label_20;
                }
            }
            catch(Throwable v8) {
                goto label_46;
            }
        }

        try {
            v0.close();
        }
        catch(IOException v2_1) {
            v2_1.printStackTrace();
        }

    label_20:
        Log.d("RootCmd", "cmd out " + v6);
        return v6;
    label_57:
        v0 = v1;
    label_46:
        if(v0 != null) {
            try {
                v0.close();
            }
            catch(IOException v2_1) {
                v2_1.printStackTrace();
            }
        }

        throw v8;
    }
}
