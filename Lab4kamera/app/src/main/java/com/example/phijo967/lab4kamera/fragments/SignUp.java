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

        this.httpPostExecute = new HttpPostExecute() {
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) {
                String result = JsonParse.addUserParse(jsonObject);
                switch (result) {
                    case "emailError":
                        Toast.makeText(getActivity(), "Email is occupied", Toast.LENGTH_LONG).show();
                        break;
                    case "usernameExistsError":
                        Toast.makeText(getActivity(), "Username is occupied", Toast.LENGTH_LONG).show();
                        break;
                    case "user added":
                        Toast.makeText(getActivity(), "Sign up successful", Toast.LENGTH_LONG).show();
                        onButtonPressed();
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

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("name",name.getText().toString());
                    jsonObject.put("lastname",lastname.getText().toString());
                    jsonObject.put("email",email.getText().toString());
                    jsonObject.put("username", username.getText().toString());
                    jsonObject.put("password", password.getText().toString());
                    HashMap<String,JSONObject> map = new HashMap();
                    map.put("adduser",jsonObject);
                    SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecute);
                    task.execute(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSignUpInteractionListener {
        // TODO: Update argument type and name
        public void onSigUpInteraction();
    }

}
