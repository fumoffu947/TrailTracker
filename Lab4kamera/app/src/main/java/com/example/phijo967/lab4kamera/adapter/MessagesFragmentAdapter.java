package com.example.phijo967.lab4kamera.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.phijo967.lab4kamera.R;
import com.example.phijo967.lab4kamera.datastruct.Friend;
import com.example.phijo967.lab4kamera.fragments.FriendMessagesFragment;

import java.util.List;

/**
 * Created by fumoffu947 on 2015-05-20.
 */
public class MessagesFragmentAdapter extends ArrayAdapter<Friend> {
    private final List<Friend> friends;
    private final Context context;
    private FriendMessagesFragment.OnMessageInteractionListener mListener;

    public MessagesFragmentAdapter(Context context, int resource, List<Friend> objects) {
        super(context, resource, objects);
        this.friends = objects;
        this.context = context;
    }

    static class FriendMessagesViewHolder {
        public TextView name;
        public TextView lastname;
        public RelativeLayout relativeLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = convertView;

        // if there is not an old view to use
        if (rootView == null) {
            // create an new layout and finde the views inside and sett the in the ViewHolder
            LayoutInflater inflater = LayoutInflater.from(context);
            rootView = inflater.inflate(R.layout.friend_layout, parent, false);

            FriendMessagesViewHolder viewHolder = new FriendMessagesViewHolder();
            viewHolder.name = (TextView) rootView.findViewById(R.id.friendNameText);
            viewHolder.lastname = (TextView) rootView.findViewById(R.id.friendLstnameText);
            viewHolder.relativeLayout = (RelativeLayout) rootView.findViewById(R.id.friendRelativelayout);
            rootView.setTag(viewHolder);
        }

        // get the viewHolder to sett the different information
        final FriendMessagesViewHolder holder = (FriendMessagesViewHolder) rootView.getTag();
        final Friend friend = friends.get(position);

        holder.name.setText(friend.name);
        holder.lastname.setText(friend.lastname);

        // sett onCLickListener to change to the message fragment for the friend on current position
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMessagesFriendInteraction(friend);
            }
        });

        return rootView;
    }

    public void setListener(FriendMessagesFragment.OnMessageInteractionListener mListener) {
        this.mListener = mListener;
    }

    public void setFriends(List<Friend> friends) {
        this.friends.clear();
        for (Friend friend : friends) {
            this.friends.add(friend);
        }
        notifyDataSetChanged();
    }

}
