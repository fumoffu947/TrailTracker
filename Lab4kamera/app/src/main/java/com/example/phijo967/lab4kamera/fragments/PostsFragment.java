package com.example.phijo967.lab4kamera.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.phijo967.lab4kamera.JsonParse;
import com.example.phijo967.lab4kamera.adapter.MyListAdapter;
import com.example.phijo967.lab4kamera.R;

import com.example.phijo967.lab4kamera.datastruct.SavedInfo;
import com.example.phijo967.lab4kamera.datastruct.PostItem;
import com.example.phijo967.lab4kamera.http.HttpPostExecute;
import com.example.phijo967.lab4kamera.http.SendHttpRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * interface.
 */
public class PostsFragment extends Fragment {

    private AbsListView mListView;

    private MyListAdapter mAdapter;
    private HttpPostExecute httpPostExecute;
    private String dataType;
    private boolean update = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg =this.getArguments();
        this.dataType = arg.getString("datatype");

        SavedInfo.adapterList.clear();

        mAdapter = new MyListAdapter(getActivity(),android.R.layout.simple_list_item_1, SavedInfo.adapterList);

        // interface to set the result in the adapter and the right list in SavedInfo
        this.httpPostExecute = new HttpPostExecute() {
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) {
                List<PostItem> res = JsonParse.postParse(jsonObject);
                mAdapter.setPostItems(res);
                if (dataType.equals("userpost")) {
                    SavedInfo.userpost = res;
                }
                else {
                    SavedInfo.userflow = res;
                }
            }
        };
        httpCall();
    }

    private void httpCall() {
        if (dataType.equals("userpost")) { // gets the postFlow or current users posts depending on arguments input
            // if update is presed then posts is updated
            if(SavedInfo.userpost.isEmpty() || this.update) {
                SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecute, getActivity());
                if (task.isNetworkAvailable()) {
                    //the HashMAp<String, JsonObject> is for setting the string ass the url and jsonobj is the data to send to that url
                    HashMap<String, JSONObject> map = new HashMap<>();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("username", SavedInfo.username);
                        jsonObject.put("password", SavedInfo.password);
                        jsonObject.put("id_u", SavedInfo.id_u);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    map.put("getuserpost", jsonObject);
                    update = false;
                    task.execute(map);
                } else Toast.makeText(getActivity(), "No internet connection",Toast.LENGTH_SHORT).show();
            }
            else {
                mAdapter.setPostItems(SavedInfo.userpost);
            }
        }else {
            // updates the userflow
            if (SavedInfo.userflow.isEmpty() || this.update) {
                SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecute, getActivity());
                if (task.isNetworkAvailable()) {
                    //the HashMAp<String, JsonObject> is for setting the string ass the url and jsonobj is the data to send to that url
                    HashMap<String, JSONObject> map = new HashMap<>();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("username", SavedInfo.username); //gets the userflow foe the current user
                        jsonObject.put("password", SavedInfo.password); // ( friends of user and the one tha the user follows)
                        jsonObject.put("id_u", SavedInfo.id_u);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    map.put("getuserflowpost", jsonObject);
                    update = false;
                    task.execute(map);
                } else Toast.makeText(getActivity(), "No internet connection",Toast.LENGTH_SHORT).show();
            }
            else {
                mAdapter.setPostItems(SavedInfo.userflow);
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_posts, container, false);

        //update button for posts
        Button button = (Button) rootview.findViewById(R.id.postFragmentUpdatePostsButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // update userflow or userpost depending on argeument input
                update = true;
                httpCall();
            }
        });

        View view = (FrameLayout) rootview.findViewById(R.id.postFragmentListHolder);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        return rootview;
    }
}
