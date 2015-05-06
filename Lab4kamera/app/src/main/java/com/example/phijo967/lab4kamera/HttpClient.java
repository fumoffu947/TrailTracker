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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by fumoffu on 2015-04-09.
 */
public class HttpClient {

    private static String uriToServer = "http://flask-projekt.openshift.ida.liu.se/user";

    public static Object sendAPost(JSONObject jsonObject) throws IOException {
        HttpURLConnection urlConnection = null;
        int httpResult;
        try {
            URL url = new URL(uriToServer);
            System.out.println("##################################################################################");
            System.out.println(url.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(30000);
            urlConnection.setReadTimeout(30000);
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Content-type", "application/json");
            urlConnection.connect();

            System.out.println("000000000000000000000000000000000000000000000000000000000000000000000");

            DataOutputStream printOut = new DataOutputStream(urlConnection.getOutputStream());
            printOut.writeUTF(URLEncoder.encode(jsonObject.toString(), "UTF-8"));
            printOut.flush();
            printOut.close();

            System.out.println("1111111111111111111111111111111111111111111111111111111111111111111");

            httpResult = urlConnection.getResponseCode();
            System.out.println(httpResult);

            BufferedReader in =new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuffer br = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("222222222222222222222222222222222222222222222222222222222222222222222222222222222222222");
                br.append(line);
            }
            in.close();
            System.out.println(br.toString());
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }


        return httpResult;
    }
}
