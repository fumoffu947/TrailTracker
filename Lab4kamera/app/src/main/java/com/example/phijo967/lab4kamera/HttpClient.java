package com.example.phijo967.lab4kamera;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
<<<<<<< HEAD
 * Created by fumoffu947 on 2015-04-27.
 */
public class HttpClient {

    private static String uriToServer = "http://flask-projekt.openshift.ida.liu.se/";

    public static String sendAPost(JSONObject jsonObject,String appendix) throws IOException {
        HttpURLConnection urlConnection = null;
        int httpResult;
        StringBuffer br;
        try {
            URL url = new URL(uriToServer + appendix);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-type", "application/json");
            urlConnection.connect();

            OutputStreamWriter printOut = new OutputStreamWriter(urlConnection.getOutputStream());
            printOut.write(jsonObject.toString());
            printOut.flush();
            printOut.close();

            httpResult = urlConnection.getResponseCode();
            System.out.println(httpResult);

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            br = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null) {
                br.append(line);
            }
            in.close();
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }

        System.out.println(br.toString());
        return br.toString();
    }
}

