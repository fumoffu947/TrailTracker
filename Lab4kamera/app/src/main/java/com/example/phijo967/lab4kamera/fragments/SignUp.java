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

import com.example.phijo967.lab4kamera.http.HttpPostExecute;
import com.example.phijo967.lab4kamera.JsonParse;
import com.example.phijo967.lab4kamera.R;
import com.example.phijo967.lab4kamera.http.SendHttpRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUp.OnSignUpInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignUp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUp extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnSignUpInteractionListener mListener;
    private HttpPostExecute httpPostExecute;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUp.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUp newInstance(String param1, String param2) {
        SignUp fragment = new SignUp();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SignUp() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //show message ti user if en error is returned els it switches to login fragment
        this.httpPostExecute = new HttpPostExecute() {
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) {
                String result = JsonParse.resultParse(jsonObject);
                switch (result) {
                    case "emailError":
                        Toast.makeText(getActivity(), "Email is occupied", Toast.LENGTH_LONG).show();
                        break;
                    case "usernameExistsError":
                        Toast.makeText(getActivity(), "Username is occupied", Toast.LENGTH_LONG).show();
                        break;
                    case "user added":
                        Toast.makeText(getActivity(), "Sign up successful", Toast.LENGTH_LONG).show();
                        switchToLogin();
                }

            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        Button button = (Button) rootView.findViewById(R.id.signUpButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText) rootView.findViewById(R.id.signUpNameEdit);
                EditText lastname = (EditText) rootView.findViewById(R.id.signUpLastNameEdit);
                EditText email = (EditText) rootView.findViewById(R.id.signUpEmailEdit);
                EditText username = (EditText) rootView.findViewById(R.id.signUpUsernameEdit);
                EditText password = (EditText) rootView.findViewById(R.id.signUpPasswordEdit);
                EditText passwordVerification = (EditText) rootView.findViewById(R.id.signUpPasswordCheckEdit);

                if (checkIfOkInput(name, lastname, email, username, password, passwordVerification)) return;
                SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecute, getActivity());
                if (task.inNetworkAvailable()) {

                    JSONObject jsonObject = new JSONObject();
                    try { // adds a user if input is ok
                        jsonObject.put("name", name.getText().toString());
                        jsonObject.put("lastname", lastname.getText().toString());
                        jsonObject.put("email", email.getText().toString());
                        jsonObject.put("username", username.getText().toString());
                        jsonObject.put("password", password.getText().toString());
                        //the HashMAp<String, JsonObject> is for setting the string ass the url and jsonobj is the data to send to that url
                        HashMap<String, JSONObject> map = new HashMap();
                        map.put("adduser", jsonObject);
                        task.execute(map);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else Toast.makeText(getActivity(), "No internet connection",Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    /**
     * Checks if the input fields are ok length
     * @return
     * returns false if ok true if input is not ok
     */
    private boolean checkIfOkInput(EditText name, EditText lastname, EditText email, EditText username, EditText password, EditText passwordVerification) {
        if (name.getText().toString().length() < 2) {
            Toast.makeText(getActivity(), "Pleas enter a longer name.", Toast.LENGTH_SHORT).show();
            return true;
        }
        if ( lastname.getText().toString().length() < 2) {
            Toast.makeText(getActivity(), "Pleas enter a longer lastname.", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (!email.getText().toString().contains("@")) {
            Toast.makeText(getActivity(), "Pleas enter a correct email.", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (username.getText().toString().length() < 5) {
            Toast.makeText(getActivity(), "Pleas enter a longer username.", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (username.getText().toString().length() > 32) {
            Toast.makeText(getActivity(), "Pleas enter a shorter username.", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (password.getText().toString().length() < 7 ) {
            Toast.makeText(getActivity(), "Pleas enter a longer password.", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (password.getText().toString().length() > 32) {
            Toast.makeText(getActivity(), "Pleas enter a shorter password.", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (!password.getText().toString().equals(passwordVerification.getText().toString())) {
            Toast.makeText(getActivity(), "Password dont match.", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public void switchToLogin() {
        if (mListener != null) {
            mListener.onSigUpInteraction();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSignUpInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSignUpInteractionListener");
        }
    }

    @Override // auto generated
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSignUpInteractionListener {

        public void onSigUpInteraction();
    }

}
