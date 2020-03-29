package com.health.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import com.health.settings.Settings;

public class HttpUtils {
	
	public static String getJSON(String url) throws IOException {
        HttpUriRequest httpGet = new HttpGet(url);
        HttpParams httpParams = new BasicHttpParams();
        httpParams.setParameter("http.protocol.handle-redirects", false);
        httpGet.setParams(httpParams);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(httpGet);
        StringBuilder sb = new StringBuilder();
        if (response.getStatusLine().getStatusCode() == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), Settings.DEFAULT_ENCODING));
            for (String line = null; (line = reader.readLine()) != null;) {
                sb.append(line);
            }
        }
        return sb.toString();
    }	
}
