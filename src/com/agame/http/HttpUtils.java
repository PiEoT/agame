// Decompiled by JEB v1.5.201408040 - http://www.android-decompiler.com

package com.agame.http;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.agame.http.common.LogUtil;

public class HttpUtils {
        public ArrayList cooklist;
        private Context ctx;
        private LogUtil log = null;
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
                FileOutputStream v24 = null;
                InputStream v27 = null;
                String v4 = null;
                String v30;
                int v20 = 0;
                HttpGet v0 = null;
                HttpResponse v31 = null;
                this.log.trace("download url:", strurl);
                if ((needProxy) && !this.iswapconnect(this.ctx)) {
                        int v3 = -1;
                        return v3;
                }

                HttpGet v9 = new HttpGet(strurl);
                BasicHttpParams v10 = new BasicHttpParams();
                this.get_getclient(strurl, 0, -1, false, 0, v9, ((HttpParams) v10), needProxy);
                DefaultHttpClient v26 = new DefaultHttpClient(((HttpParams) v10));
                ((HttpClient) v26).getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
                String v28 = new String(strurl);
                try {
                        v31 = v26.execute(((HttpUriRequest) v9));
                        if (!HttpUtils.passagent) {
                                v9.abort();
                                this.log.trace("http download retry url:", strurl);
                                v0 = new HttpGet(strurl);
                                v31 = v26.execute(v0);
                                HttpUtils.passagent = true;
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
                try {
                        v20 = v31.getStatusLine().getStatusCode();
                        this.log.trace("get status code", v20 + "");
                        if (v20 == 302 || v20 == 303 || v20 == 307 || v20 == 301) {
                                this.getHttpCookie(v31);
                                v30 = v31.getLastHeader("Location").getValue();
                                this.log.trace("location:", v30);
                                if (v30 != null && v30.length() > 0) {
                                        if (!v30.contains("http://")) {
                                                v4 = String.valueOf(new URL(v28).getHost()) + v30;
                                                if (!v4.contains("http://")) {
                                                        v4 = "http://" + v4;
                                                }
                                        }
                                }
                                v9 = new HttpGet(v4);
                                v31 = v26.execute(((HttpUriRequest) v9));
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }

                try {
                        this.log.trace("status code", v20 + "");
                        if (v20 == 200) {
                                if (v31.getEntity() != null) {
                                        if (fd.exists()) {
                                                fd.delete();
                                        }

                                        fd.createNewFile();
                                        v27 = v31.getEntity().getContent();
                                        v24 = new FileOutputStream(fd);
                                        v19 = new byte[512];
                                        while (true) {
                                                int v29 = v27.read(v19);
                                                if (v29 <= 0) {
                                                        break;
                                                }

                                                v24.write(v19, 0, v29);
                                        }

                                        v24.flush();
                                        v24.close();
                                        v27.close();
                                        if (!v0.isAborted()) {
                                                v0.abort();
                                        }

                                        return 0;
                                }

                                this.log.trace("http", "entity null");
                        }

                        this.log.trace("http", "error code :" + v20);
                        return -1;
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return -1;
        }

        private void getHttpCookie(HttpResponse response) {
                Header[] v1 = response.getHeaders("Set-Cookie");
                int v2;
                for (v2 = 0; v2 < v1.length; ++v2) {
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
                if (needProxy) {
                        if (MouaService.ischinatel == 1) {
                                this.log.trace("apn", "dianxin");
                                params.setParameter("http.route.default-proxy", new HttpHost("10.0.0.200", 80, "http"));
                        } else {
                                this.log.trace("apn", "yidong");
                                params.setParameter("http.route.default-proxy", new HttpHost("10.0.0.172", 80, "http"));
                        }
                }

                HttpClientParams.setRedirecting(params, false);
                this.log.trace("get ua:", this.user_agent);
                if (this.user_agent != null && this.user_agent.length() > 0) {
                        HttpProtocolParams.setUserAgent(params, this.user_agent);
                }

                get.setHeader("Accept-Charset", "iso-8859-1, utf-8; q=0.7, *; q=0.7");
                if (end >= begin) {
                        get.setHeader("Range", "bytes=" + begin + "-" + end);
                }

                if (cks > 0) {
                        int v0 = this.cooklist.size();
                        int v1;
                        for (v1 = 0; v1 < v0; ++v1) {
                                Object v2 = this.cooklist.get(v1);
                                if (v2 != null && ((String) v2).length() > 0) {
                                        this.log.trace("Cookie", ((String) v2));
                                        get.addHeader("Cookie", ((String) v2));
                                }
                        }
                }

                if ((rf) && this.refer != null && this.refer.length() != 0) {
                        get.setHeader("Referer", this.refer);
                }

                get.setHeader("Connection", "Keep-Alive");
        }

        private void get_postclient(String strurl, final byte[] buffer, String contenttype, int begin, int end, boolean rf, int cks, HttpPost post, HttpParams params) {
                HttpConnectionParams.setConnectionTimeout(params, 180000);
                HttpConnectionParams.setSoTimeout(params, 180000);
                HttpConnectionParams.setSocketBufferSize(params, 8192);
                HttpClientParams.setRedirecting(params, false);
                if (MouaService.ischinatel == 1) {
                        this.log.trace("apn", "dianxin");
                        params.setParameter("http.route.default-proxy", new HttpHost("10.0.0.200", 80, "http"));
                } else {
                        this.log.trace("apn", "yidong");
                        params.setParameter("http.route.default-proxy", new HttpHost("10.0.0.172", 80, "http"));
                }

                this.log.trace("get ua:", this.user_agent);
                if (this.user_agent != null && this.user_agent.length() > 0) {
                        HttpProtocolParams.setUserAgent(params, this.user_agent);
                }

                if (contenttype.contains("post900")) {
                        this.log.trace("contenttype", "post900");
                        post.setHeader("Accept", "*/*, application/vnd.wap.mms-message, application/vnd.wap.sic");
                        post.setHeader("Accept-Charset", "iso-8859-1, utf-8; q=0.7, *; q=0.7");
                        post.setHeader("Content-Type ", "text/plain");
                } else {
                        post.setHeader("Accept",
                                        "text/html, application/vnd.wap.xhtml+xml, application/xhtml+xml, text/css, multipart/mixed, text/vnd.wap.wml, application/vnd.wap.wmlc, application/vnd.wap.wmlscriptc, application/java-archive, application/java, application/x-java-archive, text/vnd.sun.j2me.app-descriptor, application/vnd.oma.drm.message, application/vnd.oma.drm.content, application/vnd.wap.mms-message, application/vnd.wap.sic, text/x-co-desc, application/vnd.oma.dd+xml, text/javascript, */*, text/x-vcard, text/x-vcalendar, image/gif, image/vnd.wap.wbmp");
                        post.setHeader("Accept-Charset", "iso-8859-1, utf-8, iso-10646-ucs-2; q=0.6");
                        post.setHeader("Content-Type ", "application/x-www-form-urlencoded");
                }

                if (end >= begin) {
                        post.setHeader("Range", "bytes=" + begin + "-" + end);
                }

                if (cks > 0) {
                        int v2 = this.cooklist.size();
                        int v4;
                        for (v4 = 0; v4 < v2; ++v4) {
                                Object v5 = this.cooklist.get(v4);
                                if (v5 != null && ((String) v5).length() > 0) {
                                        this.log.trace("Cookie", ((String) v5));
                                        post.addHeader("Cookie", ((String) v5));
                                }
                        }
                }

                if ((rf) && this.refer != null && this.refer.length() != 0) {
                        post.setHeader("Referer", this.refer);
                }

                post.setHeader("Connection", "close");
                if (buffer != null) {
                        post.setEntity(new EntityTemplate(new ContentProducer() {
                                public void writeTo(OutputStream outstream) throws IOException {
                                        DataOutputStream v0 = new DataOutputStream(outstream);
                                        v0.write(buffer);
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
                Object v22_1 = null;
                byte[] v20 = null;
                ByteArrayOutputStream v19 = null;
                InputStream v28 = null;
                String v4 = null;
                String httpHeaderLocation;
                HttpResponse httpResponse = null;
                this.log.trace("http get url:", strurl);
                if (needWap && !this.iswapconnect(this.ctx)) {
                        return null;

                }

                HttpGet httpGet = new HttpGet(strurl);
                BasicHttpParams basicHttpParams = new BasicHttpParams();
                // if (needWap) {
                // this.get_getclient(strurl, begin, end, rf, cks, httpGet, ((HttpParams)
                // basicHttpParams));
                // } else {
                // this.get_getclient(strurl, begin, end, rf, cks, httpGet, ((HttpParams)
                // basicHttpParams), false);
                // }
                DefaultHttpClient httpClient = new DefaultHttpClient(((HttpParams) basicHttpParams));
                httpClient.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
                try {
                        httpResponse = httpClient.execute(httpGet);
                        if (!HttpUtils.passagent) {
                                httpGet.abort();
                                this.log.trace("http get retry url:", strurl);
                                httpGet = new HttpGet(strurl);
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }

                int statusCode = httpResponse.getStatusLine().getStatusCode();
                this.log.trace("get status code", statusCode + "");
                if (statusCode == 302 || statusCode == 303 || statusCode == 307 || statusCode == 301) {
                        this.getHttpCookie(httpResponse);
                        httpHeaderLocation = httpResponse.getLastHeader("Location").getValue();
                        this.log.trace("location:", httpHeaderLocation);
                        if (httpHeaderLocation != null && httpHeaderLocation.length() > 0) {
                                if (!httpHeaderLocation.contains("http://")) {
                                        try {
                                                v4 = String.valueOf(new URL(strurl).getHost()) + httpHeaderLocation;
                                        } catch (MalformedURLException e) {
                                                e.printStackTrace();
                                        }
                                        if (!v4.contains("http://")) {
                                                v4 = "http://" + v4;
                                        }
                                }
                        }

                        try {
                                httpGet.abort();
                                this.log.trace("http post 30x local url:", v4);
                                httpGet = new HttpGet(v4);
                                httpResponse = httpClient.execute(httpGet);
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                }



                statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode != 200 && statusCode != 206) {
                        return null;
                }

                try {
                        this.getHttpCookie(httpResponse);
                        if (httpResponse.getEntity() != null) {
                                v28 = httpResponse.getEntity().getContent();
                                v19 = new ByteArrayOutputStream();
                                v20 = new byte[1024];
                        }

                        while (true) {
                                int length = v28.read(v20);
                                if (length <= 0) {
                                        break;
                                }
                                v19.write(v20, 0, length);
                        }

                        v19.flush();
                        v19.close();
                        v22_1 = v19.toByteArray().clone();
                        v28.close();

                        if (!httpGet.isAborted()) {
                                httpGet.abort();
                        }
                        httpGet.abort();
                        return ((byte[]) v22_1);

                } catch (Exception e) {
                        e.printStackTrace();
                }

                if (!httpGet.isAborted()) {
                        httpGet.abort();
                }
                return ((byte[]) v22_1);
        }

        public byte[] httppostbyte(String strurl, byte[] buffer, String contenttype, int begin, int end, boolean rf, int cks) {
                Object v26_1 = null;
                byte[] v24;
                ByteArrayOutputStream v23 = null;
                InputStream v32 = null;
                String v4 = null;
                String v35;
                HttpPost v0;
                HttpResponse v36 = null;
                this.log.trace("http post url:", strurl);
                if (!this.iswapconnect(this.ctx)) {
                        return null;
                }

                HttpPost v11 = new HttpPost(strurl);
                BasicHttpParams v12 = new BasicHttpParams();
                this.get_postclient(strurl, buffer, contenttype, begin, end, rf, cks, v11, ((HttpParams) v12));
                DefaultHttpClient v31 = new DefaultHttpClient(((HttpParams) v12));
                v31.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
                String v33 = new String(strurl);
                try {
                        v36 = v31.execute(((HttpUriRequest) v11));
                        if (!HttpUtils.passagent) {
                                v11.abort();
                                this.log.trace("http get retry url:", strurl);
                                v0 = new HttpPost(strurl);
                                v36 = v31.execute(v0);
                                HttpUtils.passagent = true;
                        }
                } catch (Exception v28) {
                        v28.printStackTrace();
                }

                try {
                        // while (true) {
                        int v25 = v36.getStatusLine().getStatusCode();
                        this.log.trace("post status code", v25 + "");
                        if (v25 == 302 || v25 == 303 || v25 == 307 || v25 == 301) {
                                this.getHttpCookie(v36);
                                v35 = v36.getLastHeader("Location").getValue();
                                this.log.trace("location:", v35);
                                if (v35 != null && v35.length() > 0) {
                                        if (!v35.contains("http://")) {
                                                v4 = String.valueOf(new URL(v33).getHost()) + v35;
                                                if (!v4.contains("http://")) {
                                                        v4 = "http://" + v4;
                                                }
                                        }
                                }
                                // do 30x
                                v11.abort();
                                this.log.trace("http post 30x local url:", v4);
                                v11 = new HttpPost(v4);
                                v36 = v31.execute(((HttpUriRequest) v11));
                        }
                        // }
                } catch (Exception e) {
                        e.printStackTrace();
                }

                int code = v36.getStatusLine().getStatusCode();
                if (code != 200 && code != 206) {
                        return null;
                }
                try {
                        if (v36.getEntity() != null) {
                                this.getHttpCookie(v36);
                                v32 = v36.getEntity().getContent();
                                v23 = new ByteArrayOutputStream();
                                v24 = new byte[1024];

                                while (true) {
                                        int length = v32.read(v24);
                                        if (length <= 0) {
                                                break;
                                        }

                                        v23.write(v24, 0, length);
                                }

                                v23.flush();
                                v23.close();
                                v32.close();
                                v26_1 = v23.toByteArray().clone();
                                if (!v11.isAborted()) {
                                        v11.abort();
                                }
                                return ((byte[]) v26_1);

                        } else {
                                this.log.trace("http", "entity null");
                        }
                } catch (Exception v28) {
                        v28.printStackTrace();
                }

                return ((byte[]) v26_1);
        }

        public boolean iswapconnect(Context ctx) {
                boolean v5 = false;
                Object v0 = ctx.getSystemService("connectivity");
                NetworkInfo v4 = ((ConnectivityManager) v0).getNetworkInfo(1);
                if (v4 != null) {
                        NetworkInfo.State v3 = v4.getState();
                        if (v3 != NetworkInfo.State.CONNECTED && v3 != NetworkInfo.State.CONNECTING) {
                                NetworkInfo v2 = ((ConnectivityManager) v0).getNetworkInfo(0);
                                if (v2 != null && (v2.isAvailable())) {
                                        this.log.trace(" wap apn", "used");
                                        String v1 = v2.getExtraInfo();
                                        if (v1 != null) {
                                                this.log.trace("current apn", v1);
                                                if ((v1.contains("net")) && !MouaService.isIcs) {
                                                        this.log.trace("apn", "360 error");
                                                        return v5;
                                                }
                                        } else {
                                                this.log.trace("current apn", "null");
                                        }

                                        return true;
                                }

                                this.log.trace("apn", "can not used");
                        }

                        this.log.trace("wifi", "is opened");
                } else {
                        NetworkInfo v2 = ((ConnectivityManager) v0).getNetworkInfo(0);
                        if (v2 != null && (v2.isAvailable())) {
                                this.log.trace(" wap apn", "used");
                                String v1 = v2.getExtraInfo();
                                if (v1 != null) {
                                        this.log.trace("current apn", v1);
                                        if ((v1.contains("net")) && !MouaService.isIcs) {
                                                this.log.trace("apn", "360 error");
                                                return v5;
                                        }
                                } else {
                                        this.log.trace("current apn", "null");
                                }

                                return true;
                        }

                        this.log.trace("apn", "can not used");
                }

                return v5;
        }
}
