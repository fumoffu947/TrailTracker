package com.example.phijo967.lab4kamera.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phijo967.lab4kamera.JsonParse;
import com.example.phijo967.lab4kamera.ProfileInfo;
import com.example.phijo967.lab4kamera.R;
import com.example.phijo967.lab4kamera.SavedInfo;
import com.example.phijo967.lab4kamera.http.HttpPostExecute;
import com.example.phijo967.lab4kamera.http.SendHttpRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.example.phijo967.lab4kamera.fragments.ProfileScreen.OnProfileScreenInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileScreen extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnProfileScreenInteractionListener mListener;
    private HttpPostExecute httpPostExecute;
    private TextView name;
    private TextView lastname;
    private TextView numberOfPaths;
    private TextView numberOfSteps;
    private TextView lengthWent;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileScreen newInstance(String param1, String param2) {
        ProfileScreen fragment = new ProfileScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileScreen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.httpPostExecute = new HttpPostExecute() {
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) {
                Bundle args = JsonParse.profileParse(jsonObject);
                if (args.getString("result").equals("ok")) {
                    SavedInfo.profileInfo = new ProfileInfo(args.getString("name"),
                            args.getString("lastname"), args.getInt("numOfPath"),
                            args.getInt("numberOfSteps"), args.getInt("lenghtWent"));
                }
                else {
                    SavedInfo.profileInfo = new ProfileInfo("Failed to load","",0,0,0);
                    Toast.makeText(getActivity(),"Failed to load user", Toast.LENGTH_SHORT).show();
                }
                name.setText(SavedInfo.profileInfo.name);
                lastname.setText(SavedInfo.profileInfo.lastname);
                numberOfPaths.setText("Number of paths: "+SavedInfo.profileInfo.numberOfPaths);
                numberOfSteps.setText("Number of steps: "+SavedInfo.profileInfo.numberOfSteps);
                lengthWent.setText("Length went: "+SavedInfo.profileInfo.lengthWent);
            }
        };
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecute);
        HashMap<String, JSONObject> map = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", "test1");
            jsonObject.put("password","test1");
            jsonObject.put("id_u",SavedInfo.id_u);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        map.put("getuser",jsonObject);
        task.execute(map);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        onInteraction("addPost");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile_screen, container, false);
        this.name = (TextView) rootView.findViewById(R.id.profileScreenNameText);
        this.lastname = (TextView) rootView.findViewById(R.id.profileScreenLastnameText);
        this.numberOfPaths = (TextView) rootView.findViewById(R.id.profileScreenNumberOfPathsText);
        this.numberOfSteps = (TextView) rootView.findViewById(R.id.profileScreenNumberOfStepsText);
        this.lengthWent = (TextView) rootView.findViewById(R.id.profileScreenLengthWentText);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onInteraction(String s) {
        if (mListener != null) {
            mListener.onProfileScreenInteraction(s);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnProfileScreenInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnProfileScreenInteractionListener");
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
    public interface OnProfileScreenInteractionListener {
        // TODO: Update argument type and name
        public void onProfileScreenInteraction(String s);
    }

}