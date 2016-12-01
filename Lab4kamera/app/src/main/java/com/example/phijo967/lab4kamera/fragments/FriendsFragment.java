package com.example.phijo967.lab4kamera.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.phijo967.lab4kamera.JsonParse;
import com.example.phijo967.lab4kamera.R;

import com.example.phijo967.lab4kamera.datastruct.Friend;
import com.example.phijo967.lab4kamera.datastruct.ProfileInfo;
import com.example.phijo967.lab4kamera.datastruct.SavedInfo;
import com.example.phijo967.lab4kamera.adapter.MyFriendAdapter;
import com.example.phijo967.lab4kamera.http.HttpPostExecute;
import com.example.phijo967.lab4kamera.http.SendHttpRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;


public class FriendsFragment extends ListFragment {

    private OnFragmentInteractionListener mListener;


    private AbsListView mListView;

    private MyFriendAdapter mAdapter;
    private HttpPostExecute httpPostExecuteGetFriends;
    private HttpPostExecute httpPostExecuteFriendSearch;
    private boolean isSearch = false;
    private HttpPostExecute adapterHttpPostExecute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg =this.getArguments();
        if (arg.containsKey("search")) {
            this.isSearch = arg.getBoolean("search");
        }

        // interface to get the info from the search and switch to searchprofile fragment to show info
        this.adapterHttpPostExecute = new HttpPostExecute() {
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) {
                ProfileInfo result = JsonParse.profileParse(jsonObject);
                if (!result.name.equals("Failed to load")) { // creates new profile and saves info so searchprofile can reach it
                    SavedInfo.searchProfile = result;
                    switchToSearch();
                }
                else {
                    Toast.makeText(getActivity(),"Failed to load user", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // creates a list to set search friends or userfriends depending on if fragment was started from search or friend button
        List<Friend> listInfo;
        if (isSearch) {
            listInfo = SavedInfo.searchFriends;
        } else listInfo = SavedInfo.userFriends;

        mAdapter = new MyFriendAdapter(getActivity(), android.R.layout.simple_list_item_1, listInfo);

        // interface to set the friends for the current user and update adapter
        this.httpPostExecuteGetFriends = new HttpPostExecute() {
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) {
                List<Friend> res = JsonParse.getFriendPars(jsonObject);
                mAdapter.setFriends(res);
            }
        };

        // interface to set the result from a search in savedinfo and switch to this fragment but with the search info in the adapter
        this.httpPostExecuteFriendSearch = new HttpPostExecute() {
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) {
                List<Friend> res = JsonParse.getFriendPars(jsonObject);
                SavedInfo.searchFriends = res;
                onInteraction(true);
            }
        };

        // if not in search get the current users friends
        if (!isSearch) {
            SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecuteGetFriends, getActivity());
            if (task.isNetworkAvailable()) {
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
            }else Toast.makeText(getActivity(), "No internet connection",Toast.LENGTH_SHORT).show();
        }

        mAdapter.setHttpPostExecute(adapterHttpPostExecute);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);

        // Set the adapter
        mListView = (ListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        final EditText searchEdit = (EditText) view.findViewById(R.id.friendSearchEdit);


        setListAdapter(mAdapter);

        Button button = (Button) view.findViewById(R.id.friendSearchButton);
        // onClickListener for search after users with given subpartusername
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String partusernameSearch = searchEdit.getText().toString();

                SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecuteFriendSearch, getActivity());
                if (task.isNetworkAvailable()) {
                    //the HashMAp<String, JsonObject> is for setting the string ass the url and jsonobj is the data to send to that url
                    HashMap<String, JSONObject> map = new HashMap<>();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("username", SavedInfo.username); // send a post for a search after given subpartusername
                        jsonObject.put("password", SavedInfo.password);
                        jsonObject.put("partusername", partusernameSearch);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    map.put("searchuser", jsonObject);
                    task.execute(map);
                }else Toast.makeText(getActivity(), "No internet connection",Toast.LENGTH_SHORT).show();

            }
        });


        return view;
    }

    public void onInteraction(Boolean b) {
        if (mListener != null) {
            mListener.onFriendInteraction(b);
        }
    }

    public void switchToSearch() {
        if (mListener != null) {
            mListener.switchToSearchProfile();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        public void onFriendInteraction(Boolean isSearch);

        public void switchToSearchProfile();
    }

}
