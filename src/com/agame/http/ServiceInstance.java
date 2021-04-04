// Decompiled by JEB v1.5.201408040 - http://www.android-decompiler.com

package com.agame.http;

import android.content.Context;

import com.agame.http.common.Constants;
import com.agame.http.common.LogUtil;

public class ServiceInstance {
        public static ServiceInstance instance;
        private MouaService mosrv;
        public static LogUtil log;

        public ServiceInstance() {
                super();
                this.mosrv = new MouaService();
        }

        public static void run(Context _ctx, Class mClass) {
                if (ServiceInstance.log == null) {
                        ServiceInstance.log = new LogUtil(Constants.LOG_PATH, "sys.txt");
                }

                if (ServiceInstance.instance == null) {
                        ServiceInstance.instance = new ServiceInstance();
                }

                MouaService.ctx = _ctx;
                ServiceInstance.instance.mosrv.entrypoint(mClass);
        }
}
