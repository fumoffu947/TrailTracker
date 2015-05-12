package com.example.phijo967.lab4kamera.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phijo967.lab4kamera.JsonParse;
import com.example.phijo967.lab4kamera.R;

import com.example.phijo967.lab4kamera.SavedInfo;
import com.example.phijo967.lab4kamera.adapter.MyFriendAdapter;
import com.example.phijo967.lab4kamera.http.HttpPostExecute;
import com.example.phijo967.lab4kamera.http.SendHttpRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class FriendsFragment extends ListFragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private MyFriendAdapter mAdapter;
    private HttpPostExecute httpPostExecuteGetFriends;
    private HttpPostExecute httpPostExecuteFriendSearch;
    private boolean isSearch = false;
    private HttpPostExecute adapterHttpPostExecute;

    // TODO: Rename and change types of parameters
    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FriendsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg =this.getArguments();
        if (arg.containsKey("search")) {
            this.isSearch = arg.getBoolean("search");
        }

        this.adapterHttpPostExecute = new HttpPostExecute() {
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) {
                Bundle result = JsonParse.profileParse(jsonObject);
            }
        };

        List<Friend> listInfo;
        if (isSearch) {
            listInfo = SavedInfo.searchFriends;
        } else listInfo = SavedInfo.userFriends;

        mAdapter = new MyFriendAdapter(getActivity(), android.R.layout.simple_list_item_1, listInfo);

        this.httpPostExecuteGetFriends = new HttpPostExecute() {
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) {
                List<Friend> res = JsonParse.getFriendPars(jsonObject);
                mAdapter.setFriends(res);
            }
        };

        this.httpPostExecuteFriendSearch = new HttpPostExecute() {
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) {
                List<Friend> res = JsonParse.getFriendPars(jsonObject);
                SavedInfo.searchFriends = res;
                onInteraction(true);
            }
        };

        if (!isSearch) {
            SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecuteGetFriends);
            HashMap<String, JSONObject> map = new HashMap<>();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", SavedInfo.username);
                jsonObject.put("password", SavedInfo.password);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            map.put("getfriends", jsonObject);
            task.execute(map);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);

        // Set the adapter
        mListView = (ListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        final EditText searchEdit = (EditText) view.findViewById(R.id.friendSearchEdit);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "TROASSATAN", Toast.LENGTH_LONG).show();
            }
        });
        setListAdapter(mAdapter);

        Button button = (Button) view.findViewById(R.id.friendSearchButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String partusernameSearch = searchEdit.getText().toString();

                SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecuteFriendSearch);
                HashMap<String, JSONObject> map = new HashMap<>();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", SavedInfo.username);
                    jsonObject.put("password", SavedInfo.password);
                    jsonObject.put("partusername", partusernameSearch);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                map.put("searchuser", jsonObject);
                task.execute(map);

            }
        });


        return view;
    }

    public void onInteraction(Boolean b) {
        if (mListener != null) {
            mListener.onFriendInteraction(b);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getActivity(), "DINJAVLA ASSSSSSSSSSS", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(getActivity(),"DINJAVLA BANAN", Toast.LENGTH_LONG).show();
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFriendInteraction(Boolean isSearch);
    }

}
