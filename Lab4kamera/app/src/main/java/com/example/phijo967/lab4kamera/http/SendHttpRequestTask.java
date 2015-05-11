package com.example.phijo967.lab4kamera.http;

import android.os.AsyncTask;

import com.example.phijo967.lab4kamera.http.HttpClient;
import com.example.phijo967.lab4kamera.http.HttpPostExecute;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by fumoffu947 on 2015-05-08.
 */
public class SendHttpRequestTask extends AsyncTask<HashMap<String, JSONObject>,String,JSONObject> {

    private final HttpPostExecute httpPostExecute;

    public SendHttpRequestTask(HttpPostExecute httpPostExecute) {
        this.httpPostExecute = httpPostExecute;
    }

    @Override
    protected JSONObject doInBackground(HashMap<String, JSONObject>... params) {
        HashMap<String, JSONObject> map = params[0];
        Set keySet = map.keySet();
        String urlEnd = null;
        for (Object s : keySet) {
            urlEnd = (String) s;
        }
        JSONObject jO = map.get(urlEnd);
        String responsData = null;
        try {
            responsData = HttpClient.sendAPost(jO, urlEnd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new JSONObject(responsData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        httpPostExecute.httpOnPostExecute(jsonObject);
    }
}
