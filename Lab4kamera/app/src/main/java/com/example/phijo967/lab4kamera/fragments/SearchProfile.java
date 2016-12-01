package com.example.phijo967.lab4kamera.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phijo967.lab4kamera.JsonParse;
import com.example.phijo967.lab4kamera.R;
import com.example.phijo967.lab4kamera.datastruct.Friend;
import com.example.phijo967.lab4kamera.datastruct.SavedInfo;
import com.example.phijo967.lab4kamera.http.HttpPostExecute;
import com.example.phijo967.lab4kamera.http.SendHttpRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SearchProfile extends Fragment {

    private HttpPostExecute httpPostExecute;
    private Button addRemoveFriendButton;
    private Button followButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //shows the result of httpPost result
         this.httpPostExecute = new HttpPostExecute() {
             @Override
             public void httpOnPostExecute(JSONObject jsonObject) {
                 String result = JsonParse.resultParse(jsonObject);
                 if (result.equals("failed to parse")) {

                 }else Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
             }
         };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search_profile, container, false);
        SavedInfo.searchFriendInFriends = false;

        TextView name = (TextView) rootView.findViewById(R.id.profileSearchNameText);
        TextView lastname = (TextView) rootView.findViewById(R.id.profileSearchLastnameText);
        TextView numberOfPaths = (TextView) rootView.findViewById(R.id.profileSearchNumberOfPathsText);
        TextView lengthWent = (TextView) rootView.findViewById(R.id.profileSearchLengthWentText);

        ImageView profilePic = (ImageView) rootView.findViewById(R.id.profileSearchProfileImg);

        this.addRemoveFriendButton = (Button) rootView.findViewById(R.id.profileSearchAddFriendButton);
        this.followButton = (Button) rootView.findViewById(R.id.profileSearchFollowButton);

        // sets the info of the SavedInfo.searchProfile to TextView etc.
        name.setText(SavedInfo.searchProfile.name);
        lastname.setText(SavedInfo.searchProfile.lastname);
        numberOfPaths.setText("Number of paths: "+SavedInfo.searchProfile.numberOfPaths);
        lengthWent.setText("Length went: "+SavedInfo.searchProfile.lengthWent);

        if (SavedInfo.searchProfile.profilePic != null) {
            profilePic.setImageBitmap(SavedInfo.searchProfile.profilePic);
        }

        for (Friend userFriend : SavedInfo.userFriends) {
            if (userFriend.id_u.equals(SavedInfo.searchId)) {
                changeTextOnButtonFriend();
                SavedInfo.searchFriendInFriends = true;
            }
        }

        addRemoveFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SavedInfo.searchFriendInFriends) { // if current search user is in current users friends then add friendrequest to that user
                    SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecute, getActivity());
                    if (task.isNetworkAvailable()) {
                        //the HashMAp<String, JsonObject> is for setting the string ass the url and jsonobj is the data to send to that url
                        HashMap<String, JSONObject> map = new HashMap<>();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("username", SavedInfo.username); // necessary info to add friendrequest to id_u_fr user
                            jsonObject.put("password", SavedInfo.password);
                            jsonObject.put("id_u_fr", SavedInfo.searchId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        map.put("addfriendrequest", jsonObject);
                        changeTextOnButtonFriend();
                        task.execute(map);
                    } else Toast.makeText(getActivity(), "No internet connection",Toast.LENGTH_SHORT).show();
                } else { // if in friends then remove the friend
                    SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecute, getActivity());
                    if (task.isNetworkAvailable()) {
                        //the HashMAp<String, JsonObject> is for setting the string ass the url and jsonobj is the data to send to that url
                        HashMap<String, JSONObject> map = new HashMap<>();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("username", SavedInfo.username); // necessary info to remove friend with id id_u_friend
                            jsonObject.put("password", SavedInfo.password);
                            jsonObject.put("id_u_friend", SavedInfo.searchId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        map.put("addremovefriend", jsonObject);
                        changeTextOnButtonFriend();
                        task.execute(map);
                    } else Toast.makeText(getActivity(), "No internet connection",Toast.LENGTH_SHORT).show();
                }
            }
        });

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // follow or onfollow user depending on what is in the database
                SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecute, getActivity());
                if (task.isNetworkAvailable()) {
                    HashMap<String, JSONObject> map = new HashMap<>();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("username", SavedInfo.username); // necessary info to follow or unfollow
                        jsonObject.put("password", SavedInfo.password); // the user with id as id_u_follow
                        jsonObject.put("id_u_follow", SavedInfo.searchId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    map.put("addremovefollow", jsonObject);
                    task.execute(map);
                } else Toast.makeText(getActivity(), "No internet connection",Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    /**
     * changes name of button
     */
    private void changeTextOnButtonFriend() {
        if (this.addRemoveFriendButton.getText().toString().equals("Remove friend")) {
            this.addRemoveFriendButton.setText("Add friend");
        }else this.addRemoveFriendButton.setText("Remove friend");
    }
}
