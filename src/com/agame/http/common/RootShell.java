// Decompiled by JEB v1.5.201408040 - http://www.android-decompiler.com

package com.agame.http.common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.util.Log;

public final class RootShell {

        public static ReturnValue execCmdAsRoot(String cmd) {
                DataOutputStream dos = null;
                Process process = null;
                ReturnValue rValue = new ReturnValue();
                String rMsg = "";
                try {
                        process = Runtime.getRuntime().exec("su");
                        dos = new DataOutputStream(process.getOutputStream());
                        Log.i("RootCmd", cmd);
                        dos.writeBytes(String.valueOf(cmd) + "\n");
                        dos.flush();
                        dos.writeBytes("exit\n");
                        dos.flush();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String v5;
                        for (v5 = bufferedReader.readLine(); v5 != null; v5 = bufferedReader.readLine()) {
                                rMsg = String.valueOf(rMsg) + "\r\n" + v5;
                        }
                        rValue.body = rMsg;
                        process.waitFor();
                        rValue.code = process.exitValue();
                } catch (Exception e) {
                        e.printStackTrace();
                } finally {
                        try {
                                if (dos != null)
                                        dos.close();
                                if (process != null)
                                        process.destroy();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
                return rValue;
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
                BufferedReader bufferedReader = null;
                Process process = null;
                ReturnValue rValue = new ReturnValue();
                String rMsg = "";
                try {
                        process = Runtime.getRuntime().exec(cmd);
                        bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String v4;
                        for (v4 = bufferedReader.readLine(); v4 != null; v4 = bufferedReader.readLine()) {
                                rMsg = String.valueOf(rMsg) + "\r\n" + v4;
                        }
                        rValue.body = rMsg;
                        process.waitFor();
                        rValue.code = process.exitValue();
                } catch (Throwable v8) {
                } finally {
                        try {
                                if (bufferedReader != null)
                                        bufferedReader.close();
                                if (process != null)
                                        process.destroy();
                        } catch (IOException e) {

                                e.printStackTrace();
                        }
                }
                return rValue;
        }
}
