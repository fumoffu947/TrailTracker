package com.example.phijo967.lab4kamera;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fumoffu947 on 2015-05-08.
 */
public class JSonParse {

    public static String loginResult(JSONObject jsonObject) {
        String result;
        try {
            result = jsonObject.getString("result");
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return "failed To parse";
        }
    }

    public static Bundle profileResult(JSONObject jsonObject) {
        Bundle arg = new Bundle();
        jsonObject
    }
}
