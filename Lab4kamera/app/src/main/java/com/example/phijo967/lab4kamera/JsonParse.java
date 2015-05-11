package com.example.phijo967.lab4kamera;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.example.phijo967.lab4kamera.fragments.arrayadapterContent.Comment;
import com.example.phijo967.lab4kamera.fragments.arrayadapterContent.PostItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fumoffu947 on 2015-05-08.
 */
public class JsonParse {

    public static String resultParse(JSONObject jsonObject) {
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
        try {
            arg.putString("name",jsonObject.getString("name"));
            arg.putString("lastname",jsonObject.getString("lastname"));
            arg.putString("email",jsonObject.getString("email"));
            arg.putInt("numbOfPath", jsonObject.getInt("numb_of_path"));
            arg.putInt("numberOfSteps", jsonObject.getInt("number_of_steps"));
            arg.putInt("legnthWent", jsonObject.getInt("length_went"));
            arg.putString("result","ok");
            return arg;
        } catch (JSONException e) {
            e.printStackTrace();
            arg.putString("result","failed");
            return arg;
        }
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

    public static List<PostItem> postParse(JSONObject jsonObject) {
        List<PostItem> res = new ArrayList<>();

        try {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(jsonObject.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            for (int index = 0; index < jsonArray.length(); index++) {
                Object jsonObj2 = jsonArray.get(index);
                System.out.println(jsonObj2);
                JSONObject jsonObj = new JSONObject(jsonObj2.toString());

                JSONArray jsonComment = jsonObj.getJSONArray("comments");
                List<Comment> comments = new ArrayList<>();
                for (int comentindex = 0; comentindex < jsonComment.length(); comentindex++) {
                    comments.add(new Comment(jsonComment.getJSONArray(comentindex).getString(0),"",jsonComment.getJSONArray(comentindex).getString(1)));
                }

                Object jsonP = jsonObj.get("position_list");
                JSONArray jsonPosition = new JSONArray(jsonP.toString());
                List<List<Double>> postitonList = new ArrayList<>();
                for (int posindex = 0; posindex < jsonPosition.length(); posindex++) {
                    List<Double> pos = new ArrayList<>();
                    Object jsonO = jsonPosition.get(posindex);
                    JSONArray jsonArr = new JSONArray(jsonO.toString());
                    if (jsonArr.length() >1) {
                        pos.add(jsonArr.getDouble(0));
                        pos.add(jsonArr.getDouble(1));
                    }
                }

                Object jsonBitArray = jsonObj.get("photos");
                JSONArray jsonBitmap = new JSONArray(jsonBitArray.toString());
                List<Bitmap> bitmaps = new ArrayList<>();
                for (int bitpos = 0; bitpos < jsonBitmap.length(); bitpos++) {
                    JSONArray bitmap = jsonBitmap.getJSONArray(bitpos);

                }
                res.add(new PostItem(jsonObj.getString("post_name"),jsonObj.getString("post_lastname"),
                        jsonObj.getString("name"),jsonObj.getString("description"),
                        jsonObj.getInt("id_p"),jsonObj.getInt("likes"),comments,postitonList,bitmaps));
            }
            return res;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }
}
