// Decompiled by JEB v1.5.201408040 - http://www.android-decompiler.com

package com.agame.http.beans;


public class WapUrl {
        public String readlabel_param;
        public String[] write_params;
        public String writelabel_param;
        public String wpurl = "";
        public int httptype = 0;
        public int is_setcookie;
        public boolean is_setrefer;
        public int accessbytecount;
        public String body;

        public WapUrl() {
                super();
                this.readlabel_param = "";
                this.wpurl = "";
                this.httptype = 0;
                this.is_setrefer = false;
                this.is_setcookie = 0;
                this.accessbytecount = 0;
                this.writelabel_param = "";
                this.body = null;
        }
}
