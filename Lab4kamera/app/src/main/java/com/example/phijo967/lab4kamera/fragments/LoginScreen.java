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
 * {@link LoginScreen.OnLoginInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginScreen extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnLoginInteractionListener mListener;
    private HttpPostExecute httpPostExecute;

    private String username;
    private String password;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginScreen newInstance(String param1, String param2) {
        LoginScreen fragment = new LoginScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public LoginScreen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.httpPostExecute = new HttpPostExecute() {
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) {
                String result = JsonParse.loginParse(jsonObject);
                switch (result) {
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
                        try {
                            int test = Integer.parseInt(result);
                            onInteraction(username, password);
                        }catch (NumberFormatException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),"Couldn't get the User", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(),"Please try again later", Toast.LENGTH_LONG).show();
                        }
                }
            }
        };
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmentu
        final View rootView = inflater.inflate(R.layout.fragment_login_screen, container, false);

        Button button = (Button) rootView.findViewById(R.id.loginbutton);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("onClick");
                EditText usernameE = (EditText) rootView.findViewById(R.id.signUpUsernameEdit);
                EditText passwordE = (EditText) rootView.findViewById(R.id.signUpPasswordEdit);
                username = usernameE.getText().toString();
                password = passwordE.getText().toString();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", username);
                    jsonObject.put("password", password);
                    HashMap<String,JSONObject> map = new HashMap();
                    map.put("login",jsonObject);
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
    public void onInteraction(String username, String password) {// False is go to Profile
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
    public interface OnLoginInteractionListener {
        // TODO: Update argument type and name
        public void onLoginInteraction(String username, String password);
    }

}
