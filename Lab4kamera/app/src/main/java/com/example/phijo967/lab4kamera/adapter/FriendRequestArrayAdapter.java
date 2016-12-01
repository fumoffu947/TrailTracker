package com.example.phijo967.lab4kamera.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.List;

/**
 * Created by fumoffu947 on 2015-05-20.
 */
public class FriendRequestArrayAdapter extends ArrayAdapter<Friend> {
    private final List<Friend> friendRequests;
    private final Context context;

    public FriendRequestArrayAdapter(Context context, int resource, List<Friend> objects) {
        super(context, resource, objects);
        this.friendRequests = objects;
        this.context = context;
    }

    static class ViewHolder {
        public TextView name;
        public TextView lastname;
        public Button accept;
        public Button decline;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rootView = convertView;

        // if there is not an old view to use
        if (convertView == null) {
            // create an new layout and finde the views inside and sett the in the ViewHolder
            LayoutInflater inflater = LayoutInflater.from(context);
            rootView = inflater.inflate(R.layout.friend_request, parent, false);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) rootView.findViewById(R.id.friendRequestNameText);
            viewHolder.lastname = (TextView) rootView.findViewById(R.id.friendRequestLstnameText);

            viewHolder.accept = (Button) rootView.findViewById(R.id.friendRequestAccept);
            viewHolder.decline = (Button) rootView.findViewById(R.id.friendRequestDecline);
            rootView.setTag(viewHolder);
        }

        // get the viewHolder to sett the different information
        ViewHolder holder = (ViewHolder) rootView.getTag();
        final Friend friendRequest = friendRequests.get(position);

        holder.name.setText(friendRequest.name);
        holder.lastname.setText(friendRequest.lastname);

        // make an interface to make an action on the return object
        final HttpPostExecute httpPostExecute = new HttpPostExecute() {
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) {
                String res = JsonParse.resultParse(jsonObject);
                Toast.makeText(getContext(), res, Toast.LENGTH_SHORT).show();
                friendRequests.remove(SavedInfo.removePositionRequest);
                notifyDataSetChanged();
            }
        };

        // set an onCLickListener to accept the friend request and do an httpPost upon click
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecute, getContext()); // post the comment
                if (task.isNetworkAvailable()) {
                    //the HashMAp<String, JsonObject> is for setting the string ass the url and jsonobj is the data to send to that url
                    HashMap<String, JSONObject> map = new HashMap<>();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("username", SavedInfo.username); // pack an jsonobject with the necessary information
                        jsonObject.put("password", SavedInfo.password); // to accept an friendrequest
                        jsonObject.put("id_u_friend", friendRequest.id_u);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SavedInfo.removePositionRequest = position;
                    map.put("addremovefriend", jsonObject); // put a string ass the url and value ass the jsonobject to send
                    task.execute(map);
                }else Toast.makeText(getContext(), "No internet connection",Toast.LENGTH_SHORT).show();
            }
        });

        // set an onCLickListener to decline the friend request and do an httpPost upon click
        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecute, getContext()); // post the comment
                if (task.isNetworkAvailable()) {
                    //the HashMAp<String, JsonObject> is for setting the string ass the url and jsonobj is the data to send to that url
                    HashMap<String, JSONObject> map = new HashMap<>();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("username", SavedInfo.username); // pack an jsonobject with the necessary information
                        jsonObject.put("password", SavedInfo.password); // to accept an friendrequest
                        jsonObject.put("id_u_fr", friendRequest.id_u);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SavedInfo.removePositionRequest = position;
                    map.put("removefriendrequest", jsonObject); // put a string ass the url and value ass the jsonobject to send
                    task.execute(map);
                }else Toast.makeText(getContext(), "No internet connection",Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    public void setFriendsRequests(List<Friend> friendsRequestList) {
        this.friendRequests.clear();
        for (Friend friendRequest : friendsRequestList) {
            this.friendRequests.add(friendRequest);
        }
        notifyDataSetChanged();
    }
}
