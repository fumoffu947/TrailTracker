package com.example.phijo967.lab4kamera.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.phijo967.lab4kamera.JsonParse;
import com.example.phijo967.lab4kamera.MyListAdapter;
import com.example.phijo967.lab4kamera.R;

import com.example.phijo967.lab4kamera.SavedInfo;
import com.example.phijo967.lab4kamera.fragments.arrayadapterContent.Comment;
import com.example.phijo967.lab4kamera.fragments.arrayadapterContent.PostContentHolder;
import com.example.phijo967.lab4kamera.fragments.arrayadapterContent.PostItem;
import com.example.phijo967.lab4kamera.http.HttpPostExecute;
import com.example.phijo967.lab4kamera.http.SendHttpRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link com.example.phijo967.lab4kamera.fragments.PostsFragment.OnPostFragmentInteractionListener}
 * interface.
 */
public class PostsFragment extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnPostFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private MyListAdapter mAdapter;
    private HttpPostExecute httpPostExecute;
    private String dataType;

    // TODO: Rename and change types of parameters
    public static PostsFragment newInstance(String param1, String param2) {
        PostsFragment fragment = new PostsFragment();
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
    public PostsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         Bundle arg =this.getArguments();
        this.dataType = arg.getString("datatype");
        mAdapter = new MyListAdapter(getActivity(),android.R.layout.simple_list_item_1, SavedInfo.userpost);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        this.httpPostExecute = new HttpPostExecute() {
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) {
                List<PostItem> res = JsonParse.postParse(jsonObject);
                mAdapter.setPostItems(res);
                if (dataType.equals("userdata")) {
                    SavedInfo.userpost = res;
                }
                else {
                    SavedInfo.userflow = res;
                }
            }
        };
        if (dataType.equals("userpost")) {

            if(SavedInfo.userpost.isEmpty()) {
                SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecute);
                HashMap<String, JSONObject> map = new HashMap<>();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", SavedInfo.username);
                    jsonObject.put("password",SavedInfo.password);
                    jsonObject.put("id_u",SavedInfo.id_u);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                map.put("getuserpost",jsonObject);
                task.execute(map);
            }
        }else {
            mAdapter = new MyListAdapter(getActivity(),android.R.layout.simple_list_item_1, SavedInfo.userflow);
            if (SavedInfo.userflow.isEmpty()) {
                SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecute);
                HashMap<String, JSONObject> map = new HashMap<>();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", "test1");
                    jsonObject.put("password", "test1");
                    jsonObject.put("id_u", SavedInfo.id_u);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                map.put("getuserflowpost", jsonObject);
                task.execute(map);
            }

        }
        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(new Comment("test2", "test2son", "a comment"));
        List<List<Double>> posL = new ArrayList<>();
        posL.add(new ArrayList<Double>());
        PostItem object = new PostItem("test", "testSon", "a post", "this is a test post", 1, 4, comments, posL, new ArrayList<Bitmap>());
        SavedInfo.testContentPost.add(object);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnPostFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPostFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            //mListener.onPostFragmentInteraction(PostContentHolder.ITEMS.get(position).id);
        }
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
    public interface OnPostFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onPostFragmentInteraction(String id);
    }

}
