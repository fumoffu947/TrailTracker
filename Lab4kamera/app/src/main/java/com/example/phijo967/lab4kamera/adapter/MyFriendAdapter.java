package com.example.phijo967.lab4kamera.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.phijo967.lab4kamera.R;
import com.example.phijo967.lab4kamera.SavedInfo;
import com.example.phijo967.lab4kamera.fragments.Friend;
import com.example.phijo967.lab4kamera.http.HttpPostExecute;
import com.example.phijo967.lab4kamera.http.SendHttpRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by fumoffu on 2015-05-11.
 */
public class MyFriendAdapter extends ArrayAdapter<Friend> {

    private final Context context;
    private List<Friend> friends;
    private HttpPostExecute httpPostExecute;

    public MyFriendAdapter(Context context, int resource, List<Friend> objects) {
        super(context, resource, objects);
        this.friends = objects;
        this.context = context;
    }

    public void setHttpPostExecute(HttpPostExecute httpPostExecute) {
        this.httpPostExecute = httpPostExecute;
    }

    static class FriendViewHolder {
        public TextView name;
        public TextView lastname;
        public RelativeLayout relativeLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = convertView;

        if (rootView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            rootView = inflater.inflate(R.layout.friend_layout, parent, false);

            FriendViewHolder viewHolder = new FriendViewHolder();
            viewHolder.name = (TextView) rootView.findViewById(R.id.friendNameText);
            viewHolder.lastname = (TextView) rootView.findViewById(R.id.friendLstnameText);
            viewHolder.relativeLayout = (RelativeLayout) rootView.findViewById(R.id.friendRelativelayout);
            rootView.setTag(viewHolder);
        }

        final FriendViewHolder holder = (FriendViewHolder) rootView.getTag();
        final Friend friend = friends.get(position);

        holder.name.setText(friend.name);
        holder.lastname.setText(friend.lastname);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecute); // post the comment
                HashMap<String, JSONObject> map = new HashMap<>();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", SavedInfo.username); //// dont do it here do it in FriendFragment
                    jsonObject.put("password",SavedInfo.password);
                    jsonObject.put("id_u", friend.id_u);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                map.put("getuser",jsonObject);
                task.execute(map);
            }
        });
        return rootView;
    }
}
