// Decompiled by JEB v1.5.201408040 - http://www.android-decompiler.com

package com.agame.http.common;

import java.io.UnsupportedEncodingException;

public class Base64 {
    abstract class Coder {
        public int op;
        public byte[] output;

        Coder() {
            super();
        }

        public abstract int maxOutputSize(int arg1);

        public abstract boolean process(byte[] arg1, int arg2, int arg3, boolean arg4);
    }

    class Decoder extends Coder {
        private static final int[] DECODE = null;
        private static final int[] DECODE_WEBSAFE = null;
        private static final int EQUALS = -2;
        private static final int SKIP = -1;
        private final int[] alphabet;
        private int state;
        private int value;

        static {
            int[] v0 = new int[256];
            v0[0] = -1;
            v0[1] = -1;
            v0[2] = -1;
            v0[3] = -1;
            v0[4] = -1;
            v0[5] = -1;
            v0[6] = -1;
            v0[7] = -1;
            v0[8] = -1;
            v0[9] = -1;
            v0[10] = -1;
            v0[11] = -1;
            v0[12] = -1;
            v0[13] = -1;
            v0[14] = -1;
            v0[15] = -1;
            v0[16] = -1;
            v0[17] = -1;
            v0[18] = -1;
            v0[19] = -1;
            v0[20] = -1;
            v0[21] = -1;
            v0[22] = -1;
            v0[23] = -1;
            v0[24] = -1;
            v0[25] = -1;
            v0[26] = -1;
            v0[27] = -1;
            v0[28] = -1;
            v0[29] = -1;
            v0[30] = -1;
            v0[31] = -1;
            v0[32] = -1;
            v0[33] = -1;
            v0[34] = -1;
            v0[35] = -1;
            v0[36] = -1;
            v0[37] = -1;
            v0[38] = -1;
            v0[39] = -1;
            v0[40] = -1;
            v0[41] = -1;
            v0[42] = -1;
            v0[43] = 62;
            v0[44] = -1;
            v0[45] = -1;
            v0[46] = -1;
            v0[47] = 63;
            v0[48] = 52;
            v0[49] = 53;
            v0[50] = 54;
            v0[51] = 55;
            v0[52] = 56;
            v0[53] = 57;
            v0[54] = 58;
            v0[55] = 59;
            v0[56] = 60;
            v0[57] = 61;
            v0[58] = -1;
            v0[59] = -1;
            v0[60] = -1;
            v0[61] = -2;
            v0[62] = -1;
            v0[63] = -1;
            v0[64] = -1;
            v0[66] = 1;
            v0[67] = 2;
            v0[68] = 3;
            v0[69] = 4;
            v0[70] = 5;
            v0[71] = 6;
            v0[72] = 7;
            v0[73] = 8;
            v0[74] = 9;
            v0[75] = 10;
            v0[76] = 11;
            v0[77] = 12;
            v0[78] = 13;
            v0[79] = 14;
            v0[80] = 15;
            v0[81] = 16;
            v0[82] = 17;
            v0[83] = 18;
            v0[84] = 19;
            v0[85] = 20;
            v0[86] = 21;
            v0[87] = 22;
            v0[88] = 23;
            v0[89] = 24;
            v0[90] = 25;
            v0[91] = -1;
            v0[92] = -1;
            v0[93] = -1;
            v0[94] = -1;
            v0[95] = -1;
            v0[96] = -1;
            v0[97] = 26;
            v0[98] = 27;
            v0[99] = 28;
            v0[100] = 29;
            v0[101] = 30;
            v0[102] = 31;
            v0[103] = 32;
            v0[104] = 33;
            v0[105] = 34;
            v0[106] = 35;
            v0[107] = 36;
            v0[108] = 37;
            v0[109] = 38;
            v0[110] = 39;
            v0[111] = 40;
            v0[112] = 41;
            v0[113] = 42;
            v0[114] = 43;
            v0[115] = 44;
            v0[116] = 45;
            v0[117] = 46;
            v0[118] = 47;
            v0[119] = 48;
            v0[120] = 49;
            v0[121] = 50;
            v0[122] = 51;
            v0[123] = -1;
            v0[124] = -1;
            v0[125] = -1;
            v0[126] = -1;
            v0[127] = -1;
            v0[128] = -1;
            v0[129] = -1;
            v0[130] = -1;
            v0[131] = -1;
            v0[132] = -1;
            v0[133] = -1;
            v0[134] = -1;
            v0[135] = -1;
            v0[136] = -1;
            v0[137] = -1;
            v0[138] = -1;
            v0[139] = -1;
            v0[140] = -1;
            v0[141] = -1;
            v0[142] = -1;
            v0[143] = -1;
            v0[144] = -1;
            v0[145] = -1;
            v0[146] = -1;
            v0[147] = -1;
            v0[148] = -1;
            v0[149] = -1;
            v0[150] = -1;
            v0[151] = -1;
            v0[152] = -1;
            v0[153] = -1;
            v0[154] = -1;
            v0[155] = -1;
            v0[156] = -1;
            v0[157] = -1;
            v0[158] = -1;
            v0[159] = -1;
            v0[160] = -1;
            v0[161] = -1;
            v0[162] = -1;
            v0[163] = -1;
            v0[164] = -1;
            v0[165] = -1;
            v0[166] = -1;
            v0[167] = -1;
            v0[168] = -1;
            v0[169] = -1;
            v0[170] = -1;
            v0[171] = -1;
            v0[172] = -1;
            v0[173] = -1;
            v0[174] = -1;
            v0[175] = -1;
            v0[176] = -1;
            v0[177] = -1;
            v0[178] = -1;
            v0[179] = -1;
            v0[180] = -1;
            v0[181] = -1;
            v0[182] = -1;
            v0[183] = -1;
            v0[184] = -1;
            v0[185] = -1;
            v0[186] = -1;
            v0[187] = -1;
            v0[188] = -1;
            v0[189] = -1;
            v0[190] = -1;
            v0[191] = -1;
            v0[192] = -1;
            v0[193] = -1;
            v0[194] = -1;
            v0[195] = -1;
            v0[196] = -1;
            v0[197] = -1;
            v0[198] = -1;
            v0[199] = -1;
            v0[200] = -1;
            v0[201] = -1;
            v0[202] = -1;
            v0[203] = -1;
            v0[204] = -1;
            v0[205] = -1;
            v0[206] = -1;
            v0[207] = -1;
            v0[208] = -1;
            v0[209] = -1;
            v0[210] = -1;
            v0[211] = -1;
            v0[212] = -1;
            v0[213] = -1;
            v0[214] = -1;
            v0[215] = -1;
            v0[216] = -1;
            v0[217] = -1;
            v0[218] = -1;
            v0[219] = -1;
            v0[220] = -1;
            v0[221] = -1;
            v0[222] = -1;
            v0[223] = -1;
            v0[224] = -1;
            v0[225] = -1;
            v0[226] = -1;
            v0[227] = -1;
            v0[228] = -1;
            v0[229] = -1;
            v0[230] = -1;
            v0[231] = -1;
            v0[232] = -1;
            v0[233] = -1;
            v0[234] = -1;
            v0[235] = -1;
            v0[236] = -1;
            v0[237] = -1;
            v0[238] = -1;
            v0[239] = -1;
            v0[240] = -1;
            v0[241] = -1;
            v0[242] = -1;
            v0[243] = -1;
            v0[244] = -1;
            v0[245] = -1;
            v0[246] = -1;
            v0[247] = -1;
            v0[248] = -1;
            v0[249] = -1;
            v0[250] = -1;
            v0[251] = -1;
            v0[252] = -1;
            v0[253] = -1;
            v0[254] = -1;
            v0[255] = -1;
            Decoder.DECODE = v0;
            v0 = new int[256];
            v0[0] = -1;
            v0[1] = -1;
            v0[2] = -1;
            v0[3] = -1;
            v0[4] = -1;
            v0[5] = -1;
            v0[6] = -1;
            v0[7] = -1;
            v0[8] = -1;
            v0[9] = -1;
            v0[10] = -1;
            v0[11] = -1;
            v0[12] = -1;
            v0[13] = -1;
            v0[14] = -1;
            v0[15] = -1;
            v0[16] = -1;
            v0[17] = -1;
            v0[18] = -1;
            v0[19] = -1;
            v0[20] = -1;
            v0[21] = -1;
            v0[22] = -1;
            v0[23] = -1;
            v0[24] = -1;
            v0[25] = -1;
            v0[26] = -1;
            v0[27] = -1;
            v0[28] = -1;
            v0[29] = -1;
            v0[30] = -1;
            v0[31] = -1;
            v0[32] = -1;
            v0[33] = -1;
            v0[34] = -1;
            v0[35] = -1;
            v0[36] = -1;
            v0[37] = -1;
            v0[38] = -1;
            v0[39] = -1;
            v0[40] = -1;
            v0[41] = -1;
            v0[42] = -1;
            v0[43] = -1;
            v0[44] = -1;
            v0[45] = 62;
            v0[46] = -1;
            v0[47] = -1;
            v0[48] = 52;
            v0[49] = 53;
            v0[50] = 54;
            v0[51] = 55;
            v0[52] = 56;
            v0[53] = 57;
            v0[54] = 58;
            v0[55] = 59;
            v0[56] = 60;
            v0[57] = 61;
            v0[58] = -1;
            v0[59] = -1;
            v0[60] = -1;
            v0[61] = -2;
            v0[62] = -1;
            v0[63] = -1;
            v0[64] = -1;
            v0[66] = 1;
            v0[67] = 2;
            v0[68] = 3;
            v0[69] = 4;
            v0[70] = 5;
            v0[71] = 6;
            v0[72] = 7;
            v0[73] = 8;
            v0[74] = 9;
            v0[75] = 10;
            v0[76] = 11;
            v0[77] = 12;
            v0[78] = 13;
            v0[79] = 14;
            v0[80] = 15;
            v0[81] = 16;
            v0[82] = 17;
            v0[83] = 18;
            v0[84] = 19;
            v0[85] = 20;
            v0[86] = 21;
            v0[87] = 22;
            v0[88] = 23;
            v0[89] = 24;
            v0[90] = 25;
            v0[91] = -1;
            v0[92] = -1;
            v0[93] = -1;
            v0[94] = -1;
            v0[95] = 63;
            v0[96] = -1;
            v0[97] = 26;
            v0[98] = 27;
            v0[99] = 28;
            v0[100] = 29;
            v0[101] = 30;
            v0[102] = 31;
            v0[103] = 32;
            v0[104] = 33;
            v0[105] = 34;
            v0[106] = 35;
            v0[107] = 36;
            v0[108] = 37;
            v0[109] = 38;
            v0[110] = 39;
            v0[111] = 40;
            v0[112] = 41;
            v0[113] = 42;
            v0[114] = 43;
            v0[115] = 44;
            v0[116] = 45;
            v0[117] = 46;
            v0[118] = 47;
            v0[119] = 48;
            v0[120] = 49;
            v0[121] = 50;
            v0[122] = 51;
            v0[123] = -1;
            v0[124] = -1;
            v0[125] = -1;
            v0[126] = -1;
            v0[127] = -1;
            v0[128] = -1;
            v0[129] = -1;
            v0[130] = -1;
            v0[131] = -1;
            v0[132] = -1;
            v0[133] = -1;
            v0[134] = -1;
            v0[135] = -1;
            v0[136] = -1;
            v0[137] = -1;
            v0[138] = -1;
            v0[139] = -1;
            v0[140] = -1;
            v0[141] = -1;
            v0[142] = -1;
            v0[143] = -1;
            v0[144] = -1;
            v0[145] = -1;
            v0[146] = -1;
            v0[147] = -1;
            v0[148] = -1;
            v0[149] = -1;
            v0[150] = -1;
            v0[151] = -1;
            v0[152] = -1;
            v0[153] = -1;
            v0[154] = -1;
            v0[155] = -1;
            v0[156] = -1;
            v0[157] = -1;
            v0[158] = -1;
            v0[159] = -1;
            v0[160] = -1;
            v0[161] = -1;
            v0[162] = -1;
            v0[163] = -1;
            v0[164] = -1;
            v0[165] = -1;
            v0[166] = -1;
            v0[167] = -1;
            v0[168] = -1;
            v0[169] = -1;
            v0[170] = -1;
            v0[171] = -1;
            v0[172] = -1;
            v0[173] = -1;
            v0[174] = -1;
            v0[175] = -1;
            v0[176] = -1;
            v0[177] = -1;
            v0[178] = -1;
            v0[179] = -1;
            v0[180] = -1;
            v0[181] = -1;
            v0[182] = -1;
            v0[183] = -1;
            v0[184] = -1;
            v0[185] = -1;
            v0[186] = -1;
            v0[187] = -1;
            v0[188] = -1;
            v0[189] = -1;
            v0[190] = -1;
            v0[191] = -1;
            v0[192] = -1;
            v0[193] = -1;
            v0[194] = -1;
            v0[195] = -1;
            v0[196] = -1;
            v0[197] = -1;
            v0[198] = -1;
            v0[199] = -1;
            v0[200] = -1;
            v0[201] = -1;
            v0[202] = -1;
            v0[203] = -1;
            v0[204] = -1;
            v0[205] = -1;
            v0[206] = -1;
            v0[207] = -1;
            v0[208] = -1;
            v0[209] = -1;
            v0[210] = -1;
            v0[211] = -1;
            v0[212] = -1;
            v0[213] = -1;
            v0[214] = -1;
            v0[215] = -1;
            v0[216] = -1;
            v0[217] = -1;
            v0[218] = -1;
            v0[219] = -1;
            v0[220] = -1;
            v0[221] = -1;
            v0[222] = -1;
            v0[223] = -1;
            v0[224] = -1;
            v0[225] = -1;
            v0[226] = -1;
            v0[227] = -1;
            v0[228] = -1;
            v0[229] = -1;
            v0[230] = -1;
            v0[231] = -1;
            v0[232] = -1;
            v0[233] = -1;
            v0[234] = -1;
            v0[235] = -1;
            v0[236] = -1;
            v0[237] = -1;
            v0[238] = -1;
            v0[239] = -1;
            v0[240] = -1;
            v0[241] = -1;
            v0[242] = -1;
            v0[243] = -1;
            v0[244] = -1;
            v0[245] = -1;
            v0[246] = -1;
            v0[247] = -1;
            v0[248] = -1;
            v0[249] = -1;
            v0[250] = -1;
            v0[251] = -1;
            v0[252] = -1;
            v0[253] = -1;
            v0[254] = -1;
            v0[255] = -1;
            Decoder.DECODE_WEBSAFE = v0;
        }

        public Decoder(int flags, byte[] output) {
            super();
            this.output = output;
            int[] v0 = (flags & 8) == 0 ? Decoder.DECODE : Decoder.DECODE_WEBSAFE;
            this.alphabet = v0;
            this.state = 0;
            this.value = 0;
        }

        public int maxOutputSize(int len) {
            return len * 3 / 4 + 10;
        }

        public boolean process(byte[] input, int offset, int len, boolean finish) {
            int v3;
            boolean v9;
            if(this.state == 6) {
                v9 = false;
            }
            else {
                int v5 = offset;
                len += offset;
                int v7 = this.state;
                int v8 = this.value;
                int v2 = 0;
                byte[] v4 = this.output;
                int[] v0 = this.alphabet;
            label_12:
                while(v5 < len) {
                    if(v7 == 0) {
                        while(v5 + 4 <= len) {
                            v8 = v0[input[v5] & 255] << 18 | v0[input[v5 + 1] & 255] << 12 | v0[input[v5 + 2] & 255] << 6 | v0[input[v5 + 3] & 255];
                            if(v8 < 0) {
                                break;
                            }

                            v4[v2 + 2] = ((byte)v8);
                            v4[v2 + 1] = ((byte)(v8 >> 8));
                            v4[v2] = ((byte)(v8 >> 16));
                            v2 += 3;
                            v5 += 4;
                        }

                        if(v5 < len) {
                            goto label_61;
                        }

                        v3 = v2;
                        goto label_14;
                    }

                label_61:
                    int v6 = v5 + 1;
                    int v1 = v0[input[v5] & 255];
                    switch(v7) {
                        case 0: {
                            if(v1 >= 0) {
                                v8 = v1;
                                ++v7;
                                v5 = v6;
                                goto label_12;
                            }

                            if(v1 == -1) {
                                goto label_66;
                            }

                            this.state = 6;
                            return false;
                        }
                        case 1: {
                            if(v1 >= 0) {
                                v8 = v8 << 6 | v1;
                                ++v7;
                                v5 = v6;
                                goto label_12;
                            }

                            if(v1 == -1) {
                                goto label_66;
                            }

                            this.state = 6;
                            return false;
                        }
                        case 2: {
                            if(v1 >= 0) {
                                v8 = v8 << 6 | v1;
                                ++v7;
                                v5 = v6;
                                goto label_12;
                            }

                            if(v1 == -2) {
                                v4[v2] = ((byte)(v8 >> 4));
                                v7 = 4;
                                ++v2;
                                v5 = v6;
                                goto label_12;
                            }

                            if(v1 == -1) {
                                goto label_66;
                            }

                            this.state = 6;
                            return false;
                        }
                        case 3: {
                            if(v1 >= 0) {
                                v8 = v8 << 6 | v1;
                                v4[v2 + 2] = ((byte)v8);
                                v4[v2 + 1] = ((byte)(v8 >> 8));
                                v4[v2] = ((byte)(v8 >> 16));
                                v2 += 3;
                                v7 = 0;
                                v5 = v6;
                                goto label_12;
                            }

                            if(v1 == -2) {
                                v4[v2 + 1] = ((byte)(v8 >> 2));
                                v4[v2] = ((byte)(v8 >> 10));
                                v2 += 2;
                                v7 = 5;
                                v5 = v6;
                                goto label_12;
                            }

                            if(v1 == -1) {
                                goto label_66;
                            }

                            this.state = 6;
                            return false;
                        }
                        case 4: {
                            if(v1 == -2) {
                                ++v7;
                                v5 = v6;
                                goto label_12;
                            }

                            if(v1 == -1) {
                                goto label_66;
                            }

                            this.state = 6;
                            return false;
                        }
                        case 5: {
                            if(v1 == -1) {
                                goto label_66;
                            }

                            this.state = 6;
                            return false;
                        }
                    }

                label_66:
                    v5 = v6;
                }

                v3 = v2;
            label_14:
                if(!finish) {
                    this.state = v7;
                    this.value = v8;
                    this.op = v3;
                    return true;
                }

                switch(v7) {
                    case 0: {
                        goto label_172;
                    }
                    case 1: {
                        goto label_174;
                    }
                    case 2: {
                        goto label_178;
                    }
                    case 3: {
                        goto label_183;
                    }
                    case 4: {
                        goto label_193;
                    }
                }

                v2 = v3;
                goto label_168;
            label_174:
                this.state = 6;
                return false;
            label_172:
                v2 = v3;
                goto label_168;
            label_183:
                v2 = v3 + 1;
                v4[v3] = ((byte)(v8 >> 10));
                v4[v2] = ((byte)(v8 >> 2));
                ++v2;
                goto label_168;
            label_178:
                v2 = v3 + 1;
                v4[v3] = ((byte)(v8 >> 4));
            label_168:
                this.state = v7;
                this.op = v2;
                return true;
            label_193:
                this.state = 6;
                v9 = false;
            }

            return v9;
        }
    }

    class Encoder extends Coder {
        private static final byte[] ENCODE = null;
        private static final byte[] ENCODE_WEBSAFE = null;
        public static final int LINE_GROUPS = 19;
        private final byte[] alphabet;
        private int count;
        public final boolean do_cr;
        public final boolean do_newline;
        public final boolean do_padding;
        private final byte[] tail;
        int tailLen;

        static {
            int v1 = 64;
            boolean v0 = !Base64.class.desiredAssertionStatus() ? true : false;
            Encoder.$assertionsDisabled = v0;
            Encoder.ENCODE = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
            Encoder.ENCODE_WEBSAFE = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
        }

        public Encoder(int flags, byte[] output) {
            boolean v1 = true;
            super();
            this.output = output;
            boolean v0 = (flags & 1) == 0 ? true : false;
            this.do_padding = v0;
            v0 = (flags & 2) == 0 ? true : false;
            this.do_newline = v0;
            if((flags & 4) == 0) {
                v1 = false;
            }

            this.do_cr = v1;
            byte[] v0_1 = (flags & 8) == 0 ? Encoder.ENCODE : Encoder.ENCODE_WEBSAFE;
            this.alphabet = v0_1;
            this.tail = new byte[2];
            this.tailLen = 0;
            int v0_2 = this.do_newline ? 19 : -1;
            this.count = v0_2;
        }

        public int maxOutputSize(int len) {
            return len * 8 / 5 + 10;
        }

        public boolean process(byte[] input, int offset, int len, boolean finish) {
            byte[] v11_1;
            int v12;
            int v8;
            int v4;
            int v11;
            int v7;
            byte[] v1 = this.alphabet;
            byte[] v5 = this.output;
            int v3 = 0;
            int v2 = this.count;
            int v6 = offset;
            len += offset;
            int v10 = -1;
            switch(this.tailLen) {
                case 1: {
                    if(v6 + 2 > len) {
                        goto label_9;
                    }

                    v7 = v6 + 1;
                    v11 = (this.tail[0] & 255) << 16 | (input[v6] & 255) << 8;
                    v6 = v7 + 1;
                    v10 = v11 | input[v7] & 255;
                    this.tailLen = 0;
                    break;
                }
                case 2: {
                    if(v6 + 1 > len) {
                        goto label_9;
                    }

                    v10 = (this.tail[0] & 255) << 16 | (this.tail[1] & 255) << 8 | input[v6] & 255;
                    this.tailLen = 0;
                    ++v6;
                    break;
                }
            }

        label_9:
            if(v10 != -1) {
                v4 = 1;
                v5[0] = v1[v10 >> 18 & 63];
                v3 = v4 + 1;
                v5[v4] = v1[v10 >> 12 & 63];
                v4 = v3 + 1;
                v5[v3] = v1[v10 >> 6 & 63];
                v3 = v4 + 1;
                v5[v4] = v1[v10 & 63];
                --v2;
                if(v2 == 0) {
                    if(this.do_cr) {
                        v5[v3] = 13;
                        ++v3;
                    }

                    v4 = v3 + 1;
                    v5[v3] = 10;
                    v2 = 19;
                    v7 = v6;
                label_43:
                    while(v7 + 3 <= len) {
                        v10 = (input[v7] & 255) << 16 | (input[v7 + 1] & 255) << 8 | input[v7 + 2] & 255;
                        v5[v4] = v1[v10 >> 18 & 63];
                        v5[v4 + 1] = v1[v10 >> 12 & 63];
                        v5[v4 + 2] = v1[v10 >> 6 & 63];
                        v5[v4 + 3] = v1[v10 & 63];
                        v6 = v7 + 3;
                        v3 = v4 + 4;
                        --v2;
                        if(v2 != 0) {
                            goto label_316;
                        }

                        if(this.do_cr) {
                            v5[v3] = 13;
                            ++v3;
                        }

                        v4 = v3 + 1;
                        v5[v3] = 10;
                        v2 = 19;
                        v7 = v6;
                    }

                    if(finish) {
                        if(v7 - this.tailLen == len - 1) {
                            v8 = 0;
                            if(this.tailLen > 0) {
                                v11 = this.tail[0];
                                v8 = 1;
                                v6 = v7;
                            }
                            else {
                                v6 = v7 + 1;
                                v11 = input[v7];
                            }

                            v10 = (v11 & 255) << 4;
                            this.tailLen -= v8;
                            v3 = v4 + 1;
                            v5[v4] = v1[v10 >> 6 & 63];
                            v4 = v3 + 1;
                            v5[v3] = v1[v10 & 63];
                            if(this.do_padding) {
                                v3 = v4 + 1;
                                v5[v4] = 61;
                                v4 = v3 + 1;
                                v5[v3] = 61;
                            }

                            v3 = v4;
                            if(!this.do_newline) {
                                goto label_94;
                            }

                            if(this.do_cr) {
                                v5[v3] = 13;
                                ++v3;
                            }

                            v5[v3] = 10;
                            ++v3;
                        }
                        else {
                            if(v7 - this.tailLen == len - 2) {
                                v8 = 0;
                                if(this.tailLen > 1) {
                                    v11 = this.tail[0];
                                    v8 = 1;
                                    v6 = v7;
                                }
                                else {
                                    v6 = v7 + 1;
                                    v11 = input[v7];
                                }

                                v12 = (v11 & 255) << 10;
                                if(this.tailLen > 0) {
                                    v11 = this.tail[v8];
                                    ++v8;
                                }
                                else {
                                    v11 = input[v6];
                                    ++v6;
                                }

                                v10 = v12 | (v11 & 255) << 2;
                                this.tailLen -= v8;
                                v3 = v4 + 1;
                                v5[v4] = v1[v10 >> 12 & 63];
                                v4 = v3 + 1;
                                v5[v3] = v1[v10 >> 6 & 63];
                                v3 = v4 + 1;
                                v5[v4] = v1[v10 & 63];
                                if(this.do_padding) {
                                    v5[v3] = 61;
                                    ++v3;
                                }

                                if(!this.do_newline) {
                                    goto label_94;
                                }

                                if(this.do_cr) {
                                    v5[v3] = 13;
                                    ++v3;
                                }

                                v5[v3] = 10;
                                ++v3;
                                goto label_94;
                            }

                            if((this.do_newline) && v4 > 0 && v2 != 19) {
                                if(this.do_cr) {
                                    v3 = v4 + 1;
                                    v5[v4] = 13;
                                }
                                else {
                                    v3 = v4;
                                }

                                v4 = v3 + 1;
                                v5[v3] = 10;
                            }

                            v6 = v7;
                            v3 = v4;
                        }

                    label_94:
                        if(!Encoder.$assertionsDisabled && this.tailLen != 0) {
                            throw new AssertionError();
                        }

                        if(Encoder.$assertionsDisabled) {
                            goto label_292;
                        }

                        if(v6 == len) {
                            goto label_292;
                        }

                        throw new AssertionError();
                    }
                    else {
                        if(v7 == len - 1) {
                            v11_1 = this.tail;
                            v12 = this.tailLen;
                            this.tailLen = v12 + 1;
                            v11_1[v12] = input[v7];
                            v3 = v4;
                            goto label_292;
                        }

                        if(v7 == len - 2) {
                            v11_1 = this.tail;
                            v12 = this.tailLen;
                            this.tailLen = v12 + 1;
                            v11_1[v12] = input[v7];
                            v11_1 = this.tail;
                            v12 = this.tailLen;
                            this.tailLen = v12 + 1;
                            v11_1[v12] = input[v7 + 1];
                        }

                        v3 = v4;
                    }

                label_292:
                    this.op = v3;
                    this.count = v2;
                    return 1;
                }
            }

        label_316:
            v7 = v6;
            v4 = v3;
            goto label_43;
        }
    }

    public static final int CRLF = 4;
    public static final int DEFAULT = 0;
    public static final int NO_CLOSE = 16;
    public static final int NO_PADDING = 1;
    public static final int NO_WRAP = 2;
    public static final int URL_SAFE = 8;

    static {
        boolean v0 = !Base64.class.desiredAssertionStatus() ? true : false;
        Base64.$assertionsDisabled = v0;
    }

    private Base64() {
        super();
    }

    public static byte[] decode(String str, int flags) {
        return Base64.decode(str.getBytes(), flags);
    }

    public static byte[] decode(byte[] input, int flags) {
        return Base64.decode(input, 0, input.length, flags);
    }

    public static byte[] decode(byte[] input, int offset, int len, int flags) {
        byte[] v1;
        Decoder v0 = new Decoder(flags, new byte[len * 3 / 4]);
        if(!v0.process(input, offset, len, true)) {
            throw new IllegalArgumentException("bad base-64");
        }

        if(v0.op == v0.output.length) {
            v1 = v0.output;
        }
        else {
            v1 = new byte[v0.op];
            System.arraycopy(v0.output, 0, v1, 0, v0.op);
        }

        return v1;
    }

    public static byte[] encode(byte[] input, int flags) {
        return Base64.encode(input, 0, input.length, flags);
    }

    public static byte[] encode(byte[] input, int offset, int len, int flags) {
        Encoder v0 = new Encoder(flags, null);
        int v1 = len / 3 * 4;
        if(!v0.do_padding) {
            switch(len % 3) {
                case 1: {
                    goto label_35;
                }
                case 2: {
                    goto label_37;
                }
            }

            goto label_11;
        label_37:
            v1 += 3;
            goto label_11;
        label_35:
            v1 += 2;
        }
        else if(len % 3 > 0) {
            v1 += 4;
        }

    label_11:
        if((v0.do_newline) && len > 0) {
            int v4 = (len - 1) / 57 + 1;
            int v2 = v0.do_cr ? 2 : 1;
            v1 += v2 * v4;
        }

        v0.output = new byte[v1];
        v0.process(input, offset, len, true);
        if(!Base64.$assertionsDisabled && v0.op != v1) {
            throw new AssertionError();
        }

        return v0.output;
    }

    public static String encodeToString(byte[] input, int flags) {
        try {
            return new String(Base64.encode(input, flags), "US-ASCII");
        }
        catch(UnsupportedEncodingException v0) {
            throw new AssertionError(v0);
        }
    }

    public static String encodeToString(byte[] input, int offset, int len, int flags) {
        try {
            return new String(Base64.encode(input, offset, len, flags), "US-ASCII");
        }
        catch(UnsupportedEncodingException v0) {
            throw new AssertionError(v0);
        }
    }
}

