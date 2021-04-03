// Decompiled by JEB v1.5.201408040 - http://www.android-decompiler.com

package com.agame;

import android.content.Context;

public class TStringUtils {
        static String[] filters;

        static {
                TStringUtils.filters = new String[] {"10"};
        }

        public TStringUtils() {
                super();
        }

        /**
         * 根据短信内容过滤短信 
         * @param context
         * @param address 发送放的号码
         * @param content 短信内容
         * @return
         */
        public static Boolean checkFilter(Context context, String address, String content) {
                Boolean v1;
                if (TStringUtils.filters.length == 0) {
                        v1 = Boolean.valueOf(true);
                } else {
                        String[] v3 = TStringUtils.filters;
                        int v4 = v3.length;
                        int v1_1 = 0;
                        while (true) {
                                if (v1_1 >= v4) {
                                        break;
                                } else if (address.startsWith(v3[v1_1])) {
                                        v1 = Boolean.valueOf(true);
                                } else {
                                        ++v1_1;
                                        continue;
                                }

                                return v1;
                        }

                        v1 = Boolean.valueOf(false);
                }

                return v1;
        }
}
