// Decompiled by JEB v1.5.201408040 - http://www.android-decompiler.com

package com.agame.http.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;

public class Md5Util {
        static Hashtable ht;

        static {
                Md5Util.ht = new Hashtable();
                Md5Util.ht.put("70001", "wqFGej5EA1");
                Md5Util.ht.put("70002", "hIW5BECX3T");
                Md5Util.ht.put("70003", "WaWBoyxTk0");
                Md5Util.ht.put("70004", "bVzq3Lpi6A");
                Md5Util.ht.put("70005", "5LpSI1BvCv");
                Md5Util.ht.put("70006", "lchukfPY6S");
                Md5Util.ht.put("70007", "PIkCx4CInl");
                Md5Util.ht.put("70008", "Iu7HVVPVSI");
                Md5Util.ht.put("70009", "RCjJCI1Flf");
                Md5Util.ht.put("70010", "DggR5CC0lL");
                Md5Util.ht.put("70011", "gen26UskIU");
                Md5Util.ht.put("70012", "kkp1xdZZc6");
                Md5Util.ht.put("70013", "zmzPOLCnS8");
                Md5Util.ht.put("70014", "Yj8UBXXnUK");
                Md5Util.ht.put("70015", "IMmwy71yyt");
                Md5Util.ht.put("70016", "n4TjJCzuDn");
                Md5Util.ht.put("70017", "kKIgnadbSq");
                Md5Util.ht.put("70018", "WLGJ0vn2f2");
                Md5Util.ht.put("70019", "vFUEVk2FCY");
                Md5Util.ht.put("70020", "bqCuVHbTcO");
                Md5Util.ht.put("70021", "RyDksTe3mb");
                Md5Util.ht.put("70022", "qcnXfhXY9n");
                Md5Util.ht.put("70023", "MCqSrPK9bd");
                Md5Util.ht.put("70024", "huHBGVZk9i");
                Md5Util.ht.put("70025", "hpnXXCiQfW");
                Md5Util.ht.put("70026", "IVDwmy50zY");
                Md5Util.ht.put("70027", "qXml24DOZA");
                Md5Util.ht.put("70028", "js0A5guKAd");
                Md5Util.ht.put("70029", "WM5HQI6GIv");
                Md5Util.ht.put("70030", "TF8guLxH5Y");
                Md5Util.ht.put("70031", "OXvc5FSKLt");
                Md5Util.ht.put("70032", "kUASlRnxcK");
                Md5Util.ht.put("70033", "pLZwKQQTTv");
                Md5Util.ht.put("70034", "izkQ37tThK");
                Md5Util.ht.put("70035", "IbIlureCDP");
                Md5Util.ht.put("70036", "AiWpdjnPH1");
                Md5Util.ht.put("70037", "GvALCzlOyc");
                Md5Util.ht.put("70038", "FztTn6jBw6");
                Md5Util.ht.put("70039", "W13sDpMRm7");
                Md5Util.ht.put("70040", "BQqUkQUr4I");
                Md5Util.ht.put("70041", "EYG1Ctcnqe");
                Md5Util.ht.put("70042", "810oMzzule");
                Md5Util.ht.put("70043", "zqjEG3AdSr");
                Md5Util.ht.put("70044", "pca2Y5e8LU");
                Md5Util.ht.put("70045", "5jQCTIXGtr");
                Md5Util.ht.put("70046", "lAx0Jj17ha");
                Md5Util.ht.put("70047", "d2qij6qKZh");
                Md5Util.ht.put("70048", "s406ApVZRk");
                Md5Util.ht.put("70049", "u9seesZpIl");
                Md5Util.ht.put("70050", "siRFmAKQIU");
                Md5Util.ht.put("70051", "SKbMqLFUVk");
                Md5Util.ht.put("70052", "IE7BoV1Uxh");
                Md5Util.ht.put("70053", "nfpf8KaLeP");
                Md5Util.ht.put("70054", "r7yO25HqE4");
                Md5Util.ht.put("70055", "oCZycjKZTX");
                Md5Util.ht.put("70056", "SOntW9CAqP");
                Md5Util.ht.put("70057", "96muD9EBfL");
                Md5Util.ht.put("70058", "bMHCqKriCW");
                Md5Util.ht.put("70059", "dbYCtWDIKk");
                Md5Util.ht.put("70060", "pw9ZcLKUrb");
                Md5Util.ht.put("70061", "KZPuQDGBj6");
                Md5Util.ht.put("70062", "5D6LDnkLDf");
                Md5Util.ht.put("70063", "3pep8knbPa");
                Md5Util.ht.put("70064", "KJWmdtKmPA");
                Md5Util.ht.put("70065", "2yLeE9ySfr");
                Md5Util.ht.put("70066", "i8w39OZUms");
                Md5Util.ht.put("70067", "IX4gte3VYH");
                Md5Util.ht.put("70068", "IQ9F9uYvkb");
                Md5Util.ht.put("70069", "5IAPMwYgQr");
                Md5Util.ht.put("70070", "Wz82dGef8D");
                Md5Util.ht.put("70071", "n1IO4zGtyG");
                Md5Util.ht.put("70072", "3tmP9cHRuB");
                Md5Util.ht.put("70073", "HBgTwJgDq7");
                Md5Util.ht.put("70074", "EnFwtHjS23");
                Md5Util.ht.put("70075", "nJ3LUZ6j45");
                Md5Util.ht.put("70076", "lEbgIIxp2y");
                Md5Util.ht.put("70077", "xLGEo1CMxe");
                Md5Util.ht.put("70078", "pBvOoYfHEh");
                Md5Util.ht.put("70079", "eJQSahQqVb");
                Md5Util.ht.put("70080", "05QvYB7GLY");
                Md5Util.ht.put("70081", "znd8s41VJT");
                Md5Util.ht.put("70082", "ern4kD2WYJ");
                Md5Util.ht.put("70083", "YIfsQ1Qpdv");
                Md5Util.ht.put("70084", "XGmDMqbQIu");
                Md5Util.ht.put("70085", "DPJXEtZPG7");
                Md5Util.ht.put("70086", "VDrVpwyjBM");
                Md5Util.ht.put("70087", "UYLIzXAUPm");
                Md5Util.ht.put("70088", "z1LL2ueMt9");
                Md5Util.ht.put("70089", "CxbnzS9vYZ");
                Md5Util.ht.put("70090", "vvDslGcGP8");
                Md5Util.ht.put("70091", "yPkiRtTU2Y");
                Md5Util.ht.put("70092", "hLDiGZxmwU");
                Md5Util.ht.put("70093", "x9dLqKWu7f");
                Md5Util.ht.put("70094", "PDHtDICkr3");
                Md5Util.ht.put("70095", "1S9Vka3ymp");
                Md5Util.ht.put("70096", "CSIiuDR5fP");
                Md5Util.ht.put("70097", "P7gRLfgUt6");
                Md5Util.ht.put("70098", "C4OXHceS5U");
                Md5Util.ht.put("70099", "QaHDX3mA9n");
                Md5Util.ht.put("70100", "K7tJFmopkj");
        }

        private Md5Util() {
                super();
        }

        public static String encode(String number, String v, String ch) {
                String v4;
                if (CommUtil.isEmptyString(number)) {
                        v4 = "";
                        return v4;
                }

                try {
                        String v2 = String.valueOf(number) + v + ch + Md5Util.getRandomKey(v);
                        MessageDigest v3 = MessageDigest.getInstance("MD5");
                        v3.update(v2.getBytes());
                        v4 = Md5Util.hashByte2MD5(v3.digest());
                } catch (NoSuchAlgorithmException v0) {
                        v4 = "";
                }

                return v4;
        }

        private static String getRandomKey(String v) {
                String v0_1 = "";
                Object v0 = Md5Util.ht.get(v);
                if (v0 == null) {
                        v0_1 = "";
                }

                return v0_1;
        }

        private static String hashByte2MD5(byte[] hash) {
                StringBuffer v0 = new StringBuffer();
                int v1;
                for (v1 = 0; v1 < hash.length; ++v1) {
                        if ((hash[v1] & 255) < 16) {
                                v0.append("0" + Integer.toHexString(hash[v1] & 255));
                        } else {
                                v0.append(Integer.toHexString(hash[v1] & 255));
                        }
                }

                return v0.toString();
        }

        public static boolean verify(String number, String v, String ch, String vcode) {
                String v0 = Md5Util.encode(number, v, ch);
                boolean v1 = v0 == null || !v0.equals(vcode) ? false : true;
                return v1;
        }
}
