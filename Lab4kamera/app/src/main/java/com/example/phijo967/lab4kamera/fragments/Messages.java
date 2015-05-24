package com.example.phijo967.lab4kamera.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.phijo967.lab4kamera.JsonParse;
import com.example.phijo967.lab4kamera.R;

import com.example.phijo967.lab4kamera.adapter.MessagesAdapter;
import com.example.phijo967.lab4kamera.datastruct.Friend;
import com.example.phijo967.lab4kamera.datastruct.Message;
import com.example.phijo967.lab4kamera.datastruct.SavedInfo;
import com.example.phijo967.lab4kamera.http.HttpPostExecute;
import com.example.phijo967.lab4kamera.http.SendHttpRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class Messages extends ListFragment {

    private HttpPostExecute httpPostExecuteAddMessage;
    private Friend friend;
    private HttpPostExecute httpPostExecuteAllMessages;
    private String currentMessage;
    private MessagesAdapter mAdapter;

    public static Messages getInstance(Friend friend) {
        Messages messages = new Messages();
        messages.friend = friend;
        return messages;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // interface to take care of the massage send response
        this.httpPostExecuteAddMessage = new HttpPostExecute() {
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) {
                String res = JsonParse.resultParse(jsonObject);
                if (!res.equals("message added")) { // if message was not added remove it
                    mAdapter.removeLastMessage();
                }
            }
        };

        // interface to take care of all messages
        this.httpPostExecuteAllMessages = new HttpPostExecute() {
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) {
                List<Message> res = JsonParse.messagePars(jsonObject);
                mAdapter.setMessages(res);
            }
        };

        this.mAdapter = new MessagesAdapter(getActivity(),
                android.R.layout.simple_list_item_1, SavedInfo.currentMessages);
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.messages, container, false);

        final EditText message = (EditText) rootView.findViewById(R.id.messagesAddMessageEdit);

        Button addMessage = (Button) rootView.findViewById(R.id.messageAddMessageButton);

        addMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkMessageLenght(message)) return;

                SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecuteAddMessage, getActivity());
                if (task.inNetworkAvailable()) {
                    //the HashMAp<String, JsonObject> is for setting the string ass the url and jsonobj is the data to send to that url
                    HashMap<String, JSONObject> map = new HashMap<>();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("username", SavedInfo.username); // sends message with info
                        jsonObject.put("password", SavedInfo.password); // so it  with the right user
                        jsonObject.put("id_u_to", friend.id_u);
                        jsonObject.put("message", message.getText().toString());
                        currentMessage = message.getText().toString();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    map.put("addmessage", jsonObject);
                    task.execute(map);

                    // add the message to the adapter
                    mAdapter.addMessage(new Message(SavedInfo.id_u, SavedInfo.profileInfo.name, SavedInfo.profileInfo.lastname, currentMessage));
                    message.setText("");
                }else Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        // get all the messages upon fragment creation
        SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecuteAllMessages, getActivity());
        if (task.inNetworkAvailable()) {
            //the HashMAp<String, JsonObject> is for setting the string ass the url and jsonobj is the data to send to that url
            HashMap<String, JSONObject> map = new HashMap<>();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", SavedInfo.username);
                jsonObject.put("password", SavedInfo.password);
                jsonObject.put("id_u_to", friend.id_u);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            map.put("getmessages", jsonObject);
            task.execute(map);
        }else Toast.makeText(getActivity(), "No internet connection",Toast.LENGTH_SHORT).show();

        return rootView;
    }

    private boolean checkMessageLenght(EditText message) {
        if (message.getText().toString().length() < 1) {
            return true;
        }
        return false;
    }

}
