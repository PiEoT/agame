// Decompiled by JEB v1.5.201408040 - http://www.android-decompiler.com

package com.agame.http.common;

public class ReturnValue {
        public String body;
        public int code;

        public ReturnValue() {
                super();
                this.code = -1;
        }

        public String toString() {
                return String.valueOf(this.code) + "\n" + this.body;
        }
}
