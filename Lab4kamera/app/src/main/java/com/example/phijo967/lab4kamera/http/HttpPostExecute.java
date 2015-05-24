package com.example.phijo967.lab4kamera.http;

import org.json.JSONObject;

/**
 * Created by fumoffu947 on 2015-05-08.
 * This interface is supposed to be used to send to an SendHttpRequestTask so that the data is return
 * to the place of httpOnPostExecute implementation.
 */
public interface HttpPostExecute {

    public void httpOnPostExecute(JSONObject jsonObject);

}
