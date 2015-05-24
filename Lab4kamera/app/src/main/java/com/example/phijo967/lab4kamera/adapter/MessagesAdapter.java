package com.example.phijo967.lab4kamera.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.phijo967.lab4kamera.R;
import com.example.phijo967.lab4kamera.datastruct.Message;
import com.example.phijo967.lab4kamera.datastruct.SavedInfo;

import java.util.List;

/**
 * Created by fumoffu947 on 2015-05-20.
 */
public class MessagesAdapter extends ArrayAdapter<Message> {

    private final List<Message> messages;
    private final Context context;

    public MessagesAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
        this.messages = objects;
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // cretes a rootview to find TextView etc. and sets the information necessary
        LayoutInflater inflater = LayoutInflater.from(context);

        View rootView;
        if (messages.get(position).id_u.equals(SavedInfo.id_u)) {
            rootView = inflater.inflate(R.layout.message_right_me, parent, false);
        } else {
            rootView = inflater.inflate(R.layout.message_left_friend, parent, false);
        }


        TextView name = (TextView) rootView.findViewById(R.id.messageName);
        TextView lastname = (TextView) rootView.findViewById(R.id.messageLastname);
        TextView message = (TextView) rootView.findViewById(R.id.messageMessage);

        Message currentMessage = messages.get(position);

        name.setText(currentMessage.name);
        lastname.setText(currentMessage.lastname);
        message.setText(currentMessage.message);

        return rootView;
    }

    public void addMessage(Message message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    public void removeLastMessage() {
        messages.remove(messages.size()-1);
        notifyDataSetChanged();
    }

    public void setMessages(List<Message> messages) {
        this.messages.clear();
        for (Message message : messages) {
            this.messages.add(message);
        }
        notifyDataSetChanged();
    }
}
