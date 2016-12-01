package com.example.phijo967.lab4kamera.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phijo967.lab4kamera.JsonParse;
import com.example.phijo967.lab4kamera.datastruct.ProfileInfo;
import com.example.phijo967.lab4kamera.R;
import com.example.phijo967.lab4kamera.datastruct.SavedInfo;
import com.example.phijo967.lab4kamera.http.HttpPostExecute;
import com.example.phijo967.lab4kamera.http.SendHttpRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ProfileScreen extends Fragment {

    private OnProfileScreenInteractionListener mListener;
    private HttpPostExecute httpPostExecute;
    private TextView name;
    private TextView lastname;
    private TextView numberOfPaths;
    private TextView numberOfSteps;
    private TextView lengthWent;
    private ImageView profilepic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // the interface to take care of the result of user profile load
        this.httpPostExecute = new HttpPostExecute() {
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) {
                ProfileInfo profileInfo = JsonParse.profileParse(jsonObject);
                SavedInfo.profileInfo = profileInfo;
                 if (profileInfo.name.equals("Failed to load")) {
                    Toast.makeText(getActivity(),"Failed to load user", Toast.LENGTH_SHORT).show();
                }
                name.setText(SavedInfo.profileInfo.name); // setts the new info in the fragment
                lastname.setText(SavedInfo.profileInfo.lastname);
                numberOfPaths.setText("Number of paths: "+SavedInfo.profileInfo.numberOfPaths);
                lengthWent.setText("Length went: "+SavedInfo.profileInfo.lengthWent);
                if (SavedInfo.profileInfo.profilePic != null) {
                    profilepic.setImageBitmap(SavedInfo.profileInfo.profilePic);
                }
            }
        };

        // a httpPost to get the current user info
        SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecute, getActivity());
        if (task.isNetworkAvailable()) {
            //the HashMAp<String, JsonObject> is for setting the string ass the url and jsonobj is the data to send to that url
            HashMap<String, JSONObject> map = new HashMap<>();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", SavedInfo.username); // necessary info to get current users info
                jsonObject.put("password", SavedInfo.password);
                jsonObject.put("id_u", SavedInfo.id_u);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            map.put("getuser", jsonObject);
            task.execute(map);
        } else Toast.makeText(getActivity(), "No internet connection",Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Tell mainactivity to add a PostFragment to the Framelayout in current layout so user info is set
        onInteraction("addPost");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile_screen, container, false);
        this.name = (TextView) rootView.findViewById(R.id.profileScreenNameText);
        this.lastname = (TextView) rootView.findViewById(R.id.profileScreenLastnameText);
        this.numberOfPaths = (TextView) rootView.findViewById(R.id.profileScreenNumberOfPathsText);
        this.lengthWent = (TextView) rootView.findViewById(R.id.profileScreenLengthWentText);
        this.profilepic = (ImageView) rootView.findViewById(R.id.profileScreenProfileImg);
        return rootView;
    }

    public void onInteraction(String s) {
        if (mListener != null) {
            mListener.onProfileScreenInteraction(s);
        }
    }

    @Override // auto generated
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnProfileScreenInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnProfileScreenInteractionListener");
        }
    }

    @Override // auto generated
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnProfileScreenInteractionListener {

        public void onProfileScreenInteraction(String s);
    }

}
