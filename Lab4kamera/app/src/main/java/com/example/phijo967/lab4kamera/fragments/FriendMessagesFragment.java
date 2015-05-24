package com.example.phijo967.lab4kamera.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.ListFragment;
import android.widget.Toast;

import com.example.phijo967.lab4kamera.JsonParse;

import com.example.phijo967.lab4kamera.adapter.MessagesFragmentAdapter;
import com.example.phijo967.lab4kamera.datastruct.Friend;
import com.example.phijo967.lab4kamera.datastruct.SavedInfo;
import com.example.phijo967.lab4kamera.http.HttpPostExecute;
import com.example.phijo967.lab4kamera.http.SendHttpRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;


public class FriendMessagesFragment extends ListFragment {


    private OnMessageInteractionListener mListener;
    private MessagesFragmentAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HttpPostExecute httpPostExecute = new HttpPostExecute() {
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) { // sets the friends of current user in the adapter
                List<Friend> res = JsonParse.getFriendPars(jsonObject);
                mAdapter.setFriends(res);
            }
        };

        this.mAdapter = new MessagesFragmentAdapter(getActivity(),
                android.R.layout.simple_list_item_1, SavedInfo.userFriends);
        setListAdapter(mAdapter);
        mAdapter.setListener(mListener);

        // do an httpPost to get the friends of the current user
        SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecute, getActivity());
        if (task.inNetworkAvailable()) {
            //the HashMAp<String, JsonObject> is for setting the string ass the url and jsonobj is the data to send to that url
            HashMap<String, JSONObject> map = new HashMap<>();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", SavedInfo.username);
                jsonObject.put("password", SavedInfo.password);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            map.put("getfriends", jsonObject);
            task.execute(map);
        }else Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
    }


    @Override //auto generated
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnMessageInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override //auto generated
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnMessageInteractionListener {

        public void onMessagesFriendInteraction(Friend friend);
    }

}
