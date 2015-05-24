package com.example.phijo967.lab4kamera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

import com.example.phijo967.lab4kamera.datastruct.Friend;
import com.example.phijo967.lab4kamera.datastruct.Comment;
import com.example.phijo967.lab4kamera.datastruct.Message;
import com.example.phijo967.lab4kamera.datastruct.PostItem;
import com.example.phijo967.lab4kamera.datastruct.ProfileInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fumoffu947 on 2015-05-08.
 */
public class JsonParse {

    /**
     * this is suposed to parse a JsonObject with simpel one string response
     * @param jsonObject
     * the response result
     * @return
     * returns the string result or failed to parse
     */
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

    /**
     * this is suposed to parse a JSONObject to a profile with the information
     * @param jsonObject
     * the profile JsonObject
     * @return
     * returns Profile object with the info and if it failed then returns "Failed to load" in the profile name
     */
    public static ProfileInfo profileParse(JSONObject jsonObject) {
        try {
            byte[] decodeByte = Base64.decode(jsonObject.getString("profilepic"), 0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
            ProfileInfo profileInfo = new ProfileInfo(jsonObject.getString("name"),
                    jsonObject.getString("lastname"),jsonObject.getInt("numb_of_path"),
                    jsonObject.getInt("length_went"),bitmap);
            return profileInfo;
        } catch (JSONException e) {
            e.printStackTrace();
            return new ProfileInfo("Failed to load","",0,0,null);
        }
    }

    /**
     * this parses a JsonObject with one key ass "result" with an sorted list in right order and
     * goes through that list and make PostItems on each item
     * @param jsonObject
     * the JsonObject corresponding of a list with zero or more posts
     * @return
     * A ArrayList<PostItem>
     */
    public static List<PostItem> postParse(JSONObject jsonObject) {
        List<PostItem> res = new ArrayList<>();

        try {

            // gets the PostItems and iterate through them
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            for (int index = 0; index < jsonArray.length(); index++) {
                // extract the JsonPostItem
                Object jsonObj2 = jsonArray.get(index);
                System.out.println(jsonObj2);
                JSONObject jsonObj = new JSONObject(jsonObj2.toString());

                // iterate through the comments and create them
                JSONArray jsonComment = jsonObj.getJSONArray("comments");
                List<Comment> comments = new ArrayList<>();
                for (int comentindex = 0; comentindex < jsonComment.length(); comentindex++) {
                    comments.add(new Comment(jsonComment.getJSONArray(comentindex).getString(0),"",jsonComment.getJSONArray(comentindex).getString(1)));
                }

                // iterate through the positions and add them in a list
                Object jsonP = jsonObj.get("position_list");
                JSONArray jsonPosition = new JSONArray(jsonP.toString());
                List<List<Double>> postitonList = new ArrayList<>();
                for (int posindex = 0; posindex < jsonPosition.length(); posindex++) {
                    // create a list to hold latitude and longitude
                    List<Double> pos = new ArrayList<>();
                    Object jsonO = jsonPosition.get(posindex);
                    JSONArray jsonArr = new JSONArray(jsonO.toString());
                    // add the lat and lng
                    if (jsonArr.length() >1) {
                        pos.add(jsonArr.getDouble(0));
                        pos.add(jsonArr.getDouble(1));
                        postitonList.add(pos);
                    }
                }

                // iterate through the JasonPhotos
                Object jsonBitArray = jsonObj.get("photos");
                JSONArray jsonBase64Strings = new JSONArray(jsonBitArray.toString());
                List<Bitmap> bitmaps = new ArrayList<>();
                //iterate over the list and decode the Bitmaps
                for (int base64Pos = 0; base64Pos < jsonBase64Strings.length(); base64Pos++) {
                    String base64String = jsonBase64Strings.getString(base64Pos);
                    byte[] decodeByte = Base64.decode(base64String, 0);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
                    bitmaps.add(bitmap);


                }
                // create the PostItem and add it to the result list
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

    /**
     * this is supposed to parse a JsonObject with one key "result" with the value as a list of Friend
     * data ( as [name, lastname, id_u])
     * @param jsonObject
     * @return
     */
    public static List<Friend> getFriendPars(JSONObject jsonObject) {
        List<Friend> res = new ArrayList<>();

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            for (int index = 0; index < jsonArray.length(); index++) {
                // iterate through the jsonArray and creates FriendItems for each list item
                Object object = jsonArray.get(index);
                JSONArray friendJsonArray = new JSONArray(object.toString());
                res.add(new Friend(friendJsonArray.getString(1),friendJsonArray.getString(2),friendJsonArray.getInt(0)));
            }
            return res;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * this is supposed to parse a JsonObject with one key "result" with a list
     * (as [id_u, name+lastname, message])
     * @param jsonObject
     * the JsonObject corresponding of a list with zero or more messages
     * @return
     */
    public static List<Message> messagePars(JSONObject jsonObject) {
        List<Message> res = new ArrayList<>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            for (int index = 0; index < jsonArray.length(); index++) {
                // goes through the list and creates a message for each item in the jsonArray
                JSONArray jsonMessage = (JSONArray) jsonArray.get(index);
                res.add(new Message(jsonMessage.getInt(2), jsonMessage.getString(0),"",jsonMessage.getString(1)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }
}
