package com.example.phijo967.lab4kamera;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fumoffu947 on 2015-05-08.
 */
public class JsonParse {

    public static String loginParse(JSONObject jsonObject) {
        String result;
        try {
            result = jsonObject.getString("result");
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return "failed to parse";
        }
    }

    public static Bundle profileParse(JSONObject jsonObject) {
        Bundle arg = new Bundle();
        //jsonObject.getString();
        return arg;
    }

    public static String addUserParse(JSONObject jsonObject) {
        String result;
        try {
            result = jsonObject.getString("result");
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return "failed to parse";
        }
    }
}
