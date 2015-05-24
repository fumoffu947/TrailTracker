package com.example.phijo967.lab4kamera.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.example.phijo967.lab4kamera.datastruct.SavedInfo;
import com.example.phijo967.lab4kamera.http.HttpClient;
import com.example.phijo967.lab4kamera.http.HttpPostExecute;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by fumoffu947 on 2015-05-08.
 * this is called with an HttpPostExecute to send the responds data back
 * and a context to show the loading image
 */
public class SendHttpRequestTask extends AsyncTask<HashMap<String, JSONObject>,String,JSONObject> {

    private final HttpPostExecute httpPostExecute;
    private final Context context;
    private ProgressDialog mDialoge;

    public SendHttpRequestTask(HttpPostExecute httpPostExecute, Context context) {
        this.httpPostExecute = httpPostExecute;
        this.context = context;
    }

    public boolean inNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    protected JSONObject doInBackground(HashMap<String, JSONObject>... params) {
        // gets the sent HashMap with url (HashMap key) and data (HashMap value)
        HashMap<String, JSONObject> map = params[0];
        Set keySet = map.keySet();
        String urlEnd = null;
        for (Object s : keySet) { // (built for the chance to send several at the same time but) expecten just one key in HashMap
            urlEnd = (String) s;
        }
        // extract value in HashMap to be sent
        JSONObject jO = map.get(urlEnd);
        String responsData = null;
        try { // send the data with the key as endpart url
            responsData = HttpClient.sendAPost(jO, urlEnd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try { // return the data in JsonForm
            return new JSONObject(responsData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // shows loading image
        this.mDialoge = new ProgressDialog(context);
        mDialoge.setMessage("Pleas Wait...");
        mDialoge.show();
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        // remove the loading image and send the data back
        mDialoge.dismiss();
        httpPostExecute.httpOnPostExecute(jsonObject);
    }
}
