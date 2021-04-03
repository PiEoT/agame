// Decompiled by JEB v1.5.201408040 - http://www.android-decompiler.com

package com.agame.http.beans;

public class Param {
        public String key;
        public byte[] value;

        public Param(String _key, byte[] _value) {
                super();
                this.key = new String(_key);
                this.value = _value.clone();
        }

        public Param() {
                super();
        }
}
