package com.example.phijo967.lab4kamera.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.phijo967.lab4kamera.JsonParse;
import com.example.phijo967.lab4kamera.MainActivity;
import com.example.phijo967.lab4kamera.R;
import com.example.phijo967.lab4kamera.adapter.FriendRequestArrayAdapter;
import com.example.phijo967.lab4kamera.datastruct.Friend;
import com.example.phijo967.lab4kamera.datastruct.SavedInfo;
import com.example.phijo967.lab4kamera.http.HttpPostExecute;
import com.example.phijo967.lab4kamera.http.SendHttpRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;


public class Options extends Fragment {

    private OnOptionInteractionListener mListener;
    private FriendRequestArrayAdapter mAdapter;
    private HttpPostExecute httpPostExecute;
    private AbsListView mListView;
    private HttpPostExecute httpPostExecuteProfileUpdatePic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // take care of the respons on getfrienrequest and sets the requests in the adapter
        this.httpPostExecute = new HttpPostExecute() {
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) {
                List<Friend> res = JsonParse.getFriendPars(jsonObject);
                mAdapter.setFriendsRequests(res);
            }
        };

        this.httpPostExecuteProfileUpdatePic = new HttpPostExecute() {
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) {
                String result = JsonParse.resultParse(jsonObject);
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();

            }
        };

        this.mAdapter = new FriendRequestArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, SavedInfo.friendRequests);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_options, container, false);

        // Set the adapter
        mListView = (ListView) rootView.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        Button logoutButton = (Button) rootView.findViewById(R.id.optionLogoutButton);
        final Button takePictureButton = (Button) rootView.findViewById(R.id.optionTakePictureButton);
        Button updateProfilePicture = (Button) rootView.findViewById(R.id.optionUpdateProfilePicture);
        Button updateFriendRequests = (Button) rootView.findViewById(R.id.optionsUpdateFriendRequestsButton);
        final ImageView profileImg = (ImageView) rootView.findViewById(R.id.optionProfileImg);
        SavedInfo.picHolderLayout = (LinearLayout) rootView.findViewById(R.id.optionsPicHolder);

        if (!SavedInfo.picturesList.isEmpty()) {
            profileImg.setImageBitmap(SavedInfo.picturesList.get(0));
        }

        // takes a picture
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavedInfo.picturesList = new ArrayList<Bitmap>();
                takePic();
            }
        });

        updateProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
                if (!SavedInfo.picturesList.isEmpty()) {
                    bitmaps.add(SavedInfo.picturesList.get(0));
                    String photoString = MainActivity.bitmapToBase64(bitmaps); // make the pictures to strings and

                    SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecuteProfileUpdatePic, getActivity());
                    if (task.inNetworkAvailable()) {
                        //the HashMAp<String, JsonObject> is for setting the string ass the url and jsonobj is the data to send to that url
                        HashMap<String, JSONObject> map = new HashMap<>();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("username", SavedInfo.username);
                            jsonObject.put("password", SavedInfo.password); // updates the profilepic of current user
                            jsonObject.put("profilepic", photoString);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        map.put("updateprofilepic", jsonObject);
                        task.execute(map);
                    }else Toast.makeText(getActivity(), "No internet connection",Toast.LENGTH_SHORT).show();
                } else Toast.makeText(getActivity(), "Pleas take a picture first", Toast.LENGTH_SHORT).show();

            }
        });

        // log out the user and clear the saved info
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavedInfo.clearSavedInfo();
                onLogoutPressed();
            }
        });

        updateFriendRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFriendRequests();
            }
        });

        getFriendRequests();

        return rootView;
    }



    private void getFriendRequests() {
        SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecute, getActivity());
        if (task.inNetworkAvailable()) {
            //the HashMAp<String, JsonObject> is for setting the string ass the url and jsonobj is the data to send to that url
            HashMap<String, JSONObject> map = new HashMap<>();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", SavedInfo.username); // gets the friednrequest for the current user
                jsonObject.put("password", SavedInfo.password);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            map.put("getfriendrequests", jsonObject);
            task.execute(map);
        } else Toast.makeText(getActivity(), "No internet connection",Toast.LENGTH_SHORT).show();
    }

    public void onLogoutPressed() {
        if (mListener != null) {
            mListener.onOptionInteraction();
        }
    }

    public void takePic() {
        if (mListener != null) {
            mListener.onOptionTakePic();
        }
    }

    @Override // auto generated
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnOptionInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override // auto generated
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnOptionInteractionListener {

        public void onOptionInteraction();

        public void onOptionTakePic();
    }

}
