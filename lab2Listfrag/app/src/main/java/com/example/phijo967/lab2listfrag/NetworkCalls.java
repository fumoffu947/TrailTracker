package com.example.phijo967.lab2listfrag;

import android.os.AsyncTask;

import com.example.phijo967.lab2listfrag.content.ExampleContent;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by phijo967 on 2015-02-25.
 */
public class NetworkCalls {

    public static String doNetworkCall(String url) {
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("http://tddd80-afteach.rhcloud.com/api/groups"+url);
            HttpResponse response = null;
            response = httpclient.execute(httpget);

            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            String result = sb.toString();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static JSONArray getGroups(String json) throws JSONException {
        if (json != null) {
            JSONObject jObj = new JSONObject(json);
            JSONArray listan = jObj.getJSONArray(jObj.keys().next());
            return listan;

        }
        return new JSONArray();
    }

    public static void createAddContent(String group, int id) throws JSONException {
        JSONObject jObj = new JSONObject(doNetworkCall("/"+group));
        JSONArray array = jObj.getJSONArray(jObj.keys().next());
        if (array.length() >0) {
            String members = "";
            int length = array.length();
            for (int index = 0; index < length; index++) {
                JSONObject content = new JSONObject(array.get(index).toString());
                members += content.getString("namn") + "\n";
                members += content.getString("epost") + "\n";
                if (!content.isNull("svarade")){
                    members += content.getString("svarade") + "\n" + "\n";
                }else {
                    members +="\n";
                }

            }
            ExampleContent.addItem(new ExampleContent.TestItem(String.valueOf(id), group, members + "\n"));
        }
        else {
            ExampleContent.addItem(new ExampleContent.TestItem(String.valueOf(id), group, ""));
        }
    }


}
