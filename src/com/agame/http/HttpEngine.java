// Decompiled by JEB v1.5.201408040 - http://www.android-decompiler.com

package com.agame.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo$State;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class HttpEngine {
    public HttpEngine() {
        super();
    }

    public static byte[] __doPost(Context ctx, String posturl, byte[] buffer) {
        Object v6_1;
        byte[] v6;
        if(!HttpEngine.isconnect(ctx)) {
            v6 = null;
            goto label_3;
        }

        HttpPost v12 = new HttpPost(posturl);
        try {
            BasicHttpParams v11 = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(((HttpParams)v11), 180000);
            HttpConnectionParams.setSoTimeout(((HttpParams)v11), 180000);
            HttpConnectionParams.setSocketBufferSize(((HttpParams)v11), 8192);
            HttpClientParams.setRedirecting(((HttpParams)v11), false);
            HttpHost v13 = HttpEngine.__gethost(HttpEngine.getCurrentApn(ctx));
            if(v13 != null) {
                ((HttpParams)v11).setParameter("http.route.default-proxy", v13);
            }

            if(buffer != null) {
                v12.setEntity(new EntityTemplate(new ContentProducer() {
                    public void writeTo(OutputStream outstream) throws IOException {
                        DataOutputStream v0 = new DataOutputStream(outstream);
                        v0.write(this.val$buffer);
                        v0.flush();
                        v0.close();
                    }
                }));
            }

            DefaultHttpClient v8 = new DefaultHttpClient(((HttpParams)v11));
            v8.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
            HttpResponse v14 = v8.execute(((HttpUriRequest)v12));
            int v4 = v14.getStatusLine().getStatusCode();
            if(v4 == 200 && v4 == 206) {
                if(v14.getEntity() != null) {
                    InputStream v9 = v14.getEntity().getContent();
                    ByteArrayOutputStream v2 = new ByteArrayOutputStream();
                    byte[] v3 = new byte[1024];
                    while(true) {
                        int v10 = v9.read(v3);
                        if(v10 <= 0) {
                            break;
                        }

                        v2.write(v3, 0, v10);
                    }

                    v2.flush();
                    v2.close();
                    v9.close();
                    v6_1 = v2.toByteArray().clone();
                    if(!v12.isAborted()) {
                        v12.abort();
                    }
                    else {
                    }

                    goto label_3;
                }
                else {
                    goto label_65;
                }
            }

            v6 = null;
            goto label_3;
        }
        catch(Exception v15) {
        }
        catch(ClientProtocolException v15_1) {
        }

    label_65:
        if(!v12.isAborted()) {
            v12.abort();
        }

        v6 = null;
    label_3:
        return ((byte[])v6_1);
    }

    private static HttpHost __gethost(String strapn) {
        int v3 = 80;
        HttpHost v0 = null;
        if(strapn != null) {
            if(!strapn.contains("3gwap") && !strapn.contains("cmwap") && !strapn.contains("3GWAP") && !strapn.contains("CMWAP")) {
                if(!strapn.contains("ctwap") && !strapn.contains("CTWAP")) {
                    return v0;
                }

                return new HttpHost("10.0.0.200", v3, "http");
            }

            v0 = new HttpHost("10.0.0.172", v3, "http");
        }

        return v0;
    }

    public static byte[] doGet(Context ctx, String url) {
        byte[] v2;
        ByteArrayOutputStream v1;
        InputStream v8;
        byte[] v4;
        if(!HttpEngine.isconnect(ctx)) {
            v4 = null;
            return v4;
        }

        HttpGet v7 = new HttpGet(url);
        BasicHttpParams v10 = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(((HttpParams)v10), 180000);
        HttpConnectionParams.setSoTimeout(((HttpParams)v10), 180000);
        HttpHost v11 = HttpEngine.__gethost(HttpEngine.getCurrentApn(ctx));
        if(v11 != null) {
            ((HttpParams)v10).setParameter("http.route.default-proxy", v11);
        }

        DefaultHttpClient v6 = new DefaultHttpClient(((HttpParams)v10));
        try {
            HttpResponse v12 = ((HttpClient)v6).execute(((HttpUriRequest)v7));
            int v3 = v12.getStatusLine().getStatusCode();
            if(v3 != 200 && v3 != 206) {
                return null;
            }

            if(v12.getEntity() == null) {
                return null;
            }

            v8 = v12.getEntity().getContent();
            v1 = new ByteArrayOutputStream();
            v2 = new byte[1024];
            goto label_36;
        }
        catch(Exception v13) {
            return null;
        }
        catch(ClientProtocolException v13_1) {
            return null;
        }

        return null;
        try {
            while(true) {
            label_36:
                int v9 = v8.read(v2);
                if(v9 <= 0) {
                    break;
                }

                v1.write(v2, 0, v9);
            }

            v1.flush();
            v1.close();
            Object v4_1 = v1.toByteArray().clone();
            v8.close();
            if(v7.isAborted()) {
                return v4;
            }

            v7.abort();
        }
        catch(Exception v13) {
            return null;
        }
        catch(ClientProtocolException v13_1) {
            return null;
        }

        return v4;
    }

    public static String doPost(Context ctx, String purl, String postStr) {
        try {
            HttpPost v7 = new HttpPost(new URL(purl).toURI());
            ByteArrayEntity v2 = new ByteArrayEntity(postStr.getBytes());
            v2.setContentType("text/plain");
            v7.setEntity(((HttpEntity)v2));
            DefaultHttpClient v5 = new DefaultHttpClient();
            HttpParams v6 = v5.getParams();
            HttpConnectionParams.setConnectionTimeout(v6, 3000);
            HttpConnectionParams.setSoTimeout(v6, 3000);
            HttpResponse v9 = v5.execute(((HttpUriRequest)v7));
            int v11 = v9.getStatusLine().getStatusCode();
            if(v11 != 200) {
                v7.abort();
                String v13 = null;
                return v13;
            }

            StringBuilder v1 = new StringBuilder();
            BufferedReader v0 = new BufferedReader(new InputStreamReader(v9.getEntity().getContent()));
            String v10;
            for(v10 = v0.readLine(); v10 != null; v10 = v0.readLine()) {
                v1.append(v10);
            }

            v7.abort();
            return v1.toString();
        }
        catch(Exception v4) {
            return null;
        }
    }

    public static int download_file(Context ctx, String strurl, File fd) {
        if(!HttpEngine.isconnect(ctx)) {
            int v12 = -1;
            return v12;
        }

        HttpGet v5 = new HttpGet(strurl);
        BasicHttpParams v9 = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(((HttpParams)v9), 180000);
        HttpConnectionParams.setSoTimeout(((HttpParams)v9), 180000);
        HttpHost v10 = HttpEngine.__gethost(HttpEngine.getCurrentApn(ctx));
        if(v10 != null) {
            ((HttpParams)v9).setParameter("http.route.default-proxy", v10);
        }

        DefaultHttpClient v6 = new DefaultHttpClient(((HttpParams)v9));
        try {
            HttpResponse v11 = ((HttpClient)v6).execute(((HttpUriRequest)v5));
            if(v11.getStatusLine().getStatusCode() != 200) {
                return -1;
            }

            if(v11.getEntity() == null) {
                goto label_51;
            }

            if(fd.exists()) {
                fd.delete();
            }

            fd.createNewFile();
            InputStream v7 = v11.getEntity().getContent();
            FileOutputStream v4 = new FileOutputStream(fd);
            byte[] v1 = new byte[512];
            while(true) {
                int v8 = v7.read(v1);
                if(v8 <= 0) {
                    break;
                }

                v4.write(v1, 0, v8);
            }

            v4.flush();
            v4.close();
            v7.close();
            if(!v5.isAborted()) {
                v5.abort();
            }

            return 0;
        }
        catch(Exception v12_1) {
        }
        catch(ClientProtocolException v12_2) {
        }

    label_51:
        if(!v5.isAborted()) {
            v5.abort();
        }

        return -1;
    }

    public static String getCurrentApn(Context ctx) {
        Object v0 = ctx.getSystemService("connectivity");
        NetworkInfo$State v3 = ((ConnectivityManager)v0).getNetworkInfo(1).getState();
        String v1 = v3 == NetworkInfo$State.CONNECTED || v3 == NetworkInfo$State.CONNECTING ? "wifi" : ((ConnectivityManager)v0).getNetworkInfo(0).getExtraInfo();
        return v1;
    }

    public static boolean isconnect(Context ctx) {
        boolean v3 = true;
        Object v0 = ctx.getSystemService("connectivity");
        NetworkInfo v2 = ((ConnectivityManager)v0).getNetworkInfo(1);
        if(v2 == null || !v2.isAvailable()) {
            NetworkInfo v1 = ((ConnectivityManager)v0).getNetworkInfo(0);
            if(v1 != null && (v1.isAvailable())) {
                return v3;
            }

            v3 = false;
        }

        return v3;
    }
}

