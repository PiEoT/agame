// Decompiled by JEB v1.5.201408040 - http://www.android-decompiler.com

package com.agame.http.common;

import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogUtil {
        private boolean debugEnabled;
        private File fd;
        private static List logStr;
        private String strdir;
        private String strfname;

        static {
                LogUtil.logStr = new ArrayList();
        }

        public LogUtil(String fileDir, String fileName) {
                super();
                this.debugEnabled = false;
                this.initLog(fileDir, fileName, true);
        }

        public static void clearLogArray() {
                LogUtil.logStr.clear();
        }

        public void dump() {
                if (this.fd.exists()) {
                        this.fd.delete();
                }
        }

        public static String getLogArrayString() {
                String v2 = "";
                int v0;
                for (v0 = LogUtil.logStr.size() - 1; v0 >= 0; --v0) {
                        v2 = String.valueOf(v2) + "\n" + LogUtil.logStr.get(v0);
                }

                return v2;
        }

        public void initLog(String fileDir, String fileName, boolean enable) {
                this.strdir = fileDir;
                this.strfname = fileName;
                if (enable) {
                        File v0 = new File(this.strdir);
                        if (!v0.exists()) {
                                this.debugEnabled = false;
                                return;
                        }

                        this.fd = new File(v0, this.strfname);
                        if (!this.fd.exists()) {
                                try {
                                        this.fd.createNewFile();
                                } catch (IOException v1) {
                                        this.debugEnabled = false;
                                        return;
                                }
                        }

                        this.debugEnabled = true;
                } else {
                        this.debugEnabled = false;
                }
        }

        public boolean isDebugEnabled() {
                return this.debugEnabled;
        }

        public void logException(Exception e) {
                StackTraceElement[] v2 = e.getStackTrace();
                int v3 = v2.length;
                int v1;
                for (v1 = 0; v1 < v3; ++v1) {
                        this.trace(v2[v1].getClassName(), String.valueOf(v2[v1].getFileName()) + " / " + v2[v1].getLineNumber());
                }
        }

        public void trace(String tagName, long msg) {
                this.trace(tagName, msg + "");
        }
        
        public void trace(String tagName, int msg) {
                this.trace(tagName, msg + "");
        }

        public void trace(String tagName, String msg) {
                Log.e("testt", "tagName: " + msg);
                if (!this.debugEnabled) {
                        File v1 = new File(this.strdir);
                        if (v1.exists()) {
                                this.fd = new File(v1, this.strfname);
                                try {
                                        this.fd.createNewFile();
                                } catch (IOException v2) {
                                        this.debugEnabled = false;
                                        return;
                                }

                                this.debugEnabled = true;
                        }
                }

                if (this.debugEnabled) {
                        try {
                                BufferedWriter v4 = new BufferedWriter(new FileWriter(this.fd, true));
                                v4.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                v4.append("\t\t");
                                v4.append(((CharSequence) tagName));
                                v4.append(": \t");
                                v4.append(((CharSequence) msg));
                                v4.newLine();
                                v4.close();
                                if (LogUtil.logStr.size() >= 255) {
                                        LogUtil.logStr.remove(0);
                                }

                                LogUtil.logStr.add(msg);
                        } catch (IOException v2) {
                                v2.printStackTrace();
                        }
                }
        }

        public void tracebyte(String tagName, byte[] buff) {
                int v4 = 0;
                if (!this.debugEnabled) {
                        File v1 = new File(this.strdir);
                        if (v1.exists()) {
                                this.fd = new File(v1, this.strfname);
                                try {
                                        this.fd.createNewFile();
                                } catch (IOException v2) {
                                        this.debugEnabled = false;
                                        return;
                                }

                                this.debugEnabled = true;
                        }
                }

                if ((this.debugEnabled) && buff != null) {
                        try {
                                BufferedOutputStream v0 = new BufferedOutputStream(new FileOutputStream(this.fd));
                                int v5 = buff.length;
                                while (v4 < v5) {
                                        v0.write(buff[v4]);
                                        ++v4;
                                }

                                v0.flush();
                                v0.close();
                        } catch (IOException v2) {
                                v2.printStackTrace();
                        }
                }
        }
}
