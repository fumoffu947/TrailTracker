package com.example.phijo967.lab4kamera.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.phijo967.lab4kamera.datastruct.SavedInfo;
import com.example.phijo967.lab4kamera.http.HttpPostExecute;
import com.example.phijo967.lab4kamera.JsonParse;
import com.example.phijo967.lab4kamera.R;
import com.example.phijo967.lab4kamera.http.SendHttpRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginScreen extends Fragment {

    private OnLoginInteractionListener mListener;
    private HttpPostExecute httpPostExecute;

    private String username;
    private String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.httpPostExecute = new HttpPostExecute() {
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) {
                String result = JsonParse.resultParse(jsonObject);
                switch (result) { // depending on the result inform the user about what happens
                    case "usernameError":
                        Toast.makeText(getActivity(),"Wrong Username",Toast.LENGTH_SHORT).show();
                        break;
                    case "passwordError":
                        Toast.makeText(getActivity(),"Wrong Password",Toast.LENGTH_SHORT).show();
                        break;
                    case "failed to parse":
                        Toast.makeText(getActivity(), "Internal Error parse", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "Pleas try again later", Toast.LENGTH_LONG).show();
                    default:
                        try { // switch to ProfileFragment
                            int id_u = Integer.parseInt(result);
                            SavedInfo.id_u = id_u;
                            onInteraction(username, password);
                        }catch (NumberFormatException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),"Couldn't get the User", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(),"Please try again later", Toast.LENGTH_LONG).show();
                        }
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_login_screen, container, false);

        Button button = (Button) rootView.findViewById(R.id.loginbutton);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecute, getActivity());
                if (task.isNetworkAvailable()) {
                    EditText usernameE = (EditText) rootView.findViewById(R.id.signUpUsernameEdit);
                    EditText passwordE = (EditText) rootView.findViewById(R.id.signUpPasswordEdit);
                    username = usernameE.getText().toString();
                    password = passwordE.getText().toString();
                    JSONObject jsonObject = new JSONObject();
                    SavedInfo.username = username; // saves username and password for later use
                    SavedInfo.password = password;
                    try {
                        jsonObject.put("username", username);  // send username and password to check it is ok
                        jsonObject.put("password", password);
                        //the HashMAp<String, JsonObject> is for setting the string ass the url and jsonobj is the data to send to that url
                        HashMap<String, JSONObject> map = new HashMap();
                        map.put("login", jsonObject);
                        task.execute(map);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else Toast.makeText(getActivity(), "No internet connection",Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    public void onInteraction(String username, String password) {
        if (mListener != null) {
            mListener.onLoginInteraction(username, password);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnLoginInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLoginInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnLoginInteractionListener {

        public void onLoginInteraction(String username, String password);
    }

}
