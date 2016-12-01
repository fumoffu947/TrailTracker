package com.example.phijo967.lab4kamera.adapter;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phijo967.lab4kamera.JsonParse;
import com.example.phijo967.lab4kamera.R;
import com.example.phijo967.lab4kamera.datastruct.SavedInfo;
import com.example.phijo967.lab4kamera.datastruct.Comment;
import com.example.phijo967.lab4kamera.datastruct.PostItem;
import com.example.phijo967.lab4kamera.fragments.MapFragmentGoogle;
import com.example.phijo967.lab4kamera.http.HttpPostExecute;
import com.example.phijo967.lab4kamera.http.SendHttpRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by fumoffu947 on 2015-05-09.
 */
public class MyListAdapter extends ArrayAdapter<PostItem> {

    private final Context context;
    private List<PostItem> postItems;
    private final HttpPostExecute httpPostExecuteComment;
    private int currentPostItem;

    public MyListAdapter(Context context, int resource, List<PostItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.postItems = objects;
        this.httpPostExecuteComment = new HttpPostExecute() { // the interface to get tha data back from the postExecute from ascyktask
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) {
                String res = JsonParse.resultParse(jsonObject);
                if (res.equals("comment was added to post")) { // if it was a success
                    Toast.makeText(getContext(),"Comment was added", Toast.LENGTH_SHORT).show();
                }
                else { // it went wrong remove it and notify user
                    Toast.makeText(getContext(), "Failed to add comment", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "Pleas try again later", Toast.LENGTH_SHORT).show();
                    postItems.get(currentPostItem).comments.remove(postItems.get(currentPostItem).comments.size()-1);
                    notifyDataSetChanged();
                }
            }
        };
    }

    static class ViewHolder {
        public TextView nameOfPost;
        public TextView descriptionOfPost;
        public TextView userName;
        public TextView userLastname;
        public TextView likes;
        public LinearLayout linearLayoutPic;
        public LinearLayout linearLayoutComment;
        public EditText commentEdit;
        public Button addCommentButton;
        public Button likeButton;
        public HttpPostExecute httpPostExecuteLike;
        public TextView showMapButton;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rootView = convertView; // if an exixsting view is out of sight

        if (convertView == null) { // if no existing view exists inflate a new
            LayoutInflater inflater = LayoutInflater.from(context);
            rootView = inflater.inflate(R.layout.post_layout, parent,false);

            // configure the ViewHolder
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.nameOfPost = (TextView) rootView.findViewById(R.id.postName);
            viewHolder.descriptionOfPost = (TextView) rootView.findViewById(R.id.postDescription);
            viewHolder.userName = (TextView) rootView.findViewById(R.id.postUserName);
            viewHolder.userLastname = (TextView) rootView.findViewById(R.id.postUserLastname);
            viewHolder.likes = (TextView) rootView.findViewById(R.id.postNumberOfLikes);

            viewHolder.linearLayoutPic = (LinearLayout) rootView.findViewById(R.id.postLinearLayoutPic);
            viewHolder.linearLayoutComment = (LinearLayout) rootView.
                    findViewById(R.id.postLinearLayoutComment);
            viewHolder.commentEdit = (EditText) rootView.findViewById(R.id.postAddCommentEdit);

            viewHolder.addCommentButton = (Button) rootView.findViewById(R.id.postPostComment);
            viewHolder.likeButton = (Button) rootView.findViewById(R.id.postLikeButton);
            viewHolder.showMapButton = (TextView) rootView.findViewById(R.id.postMapButton);

            rootView.setTag(viewHolder);
        }

        // insert the data
        final ViewHolder holder = (ViewHolder) rootView.getTag();
        final PostItem postItem = postItems.get(position);

        holder.nameOfPost.setText(postItem.name);
        holder.descriptionOfPost.setText(postItem.description);
        holder.userName.setText(postItem.userName);
        holder.userLastname.setText(postItem.userLastname);
        holder.likes.setText(String.valueOf(postItem.likes));

        List<Comment> comments = postItem.comments;
        final List<List<Double>> postitionList = postItem.positionList;
        List<Bitmap> photoList = postItem.photoList;

        holder.linearLayoutComment.removeAllViews(); // remove the comments from before adding them
        holder.linearLayoutPic.removeAllViews(); // remove the pictures from before adding them

        for (int index = 0; index < comments.size(); index++) { // dynamically add comments in new TextView
            String lineSep = System.getProperty("line.separator");
            TextView comment = new TextView(context);
            Comment subcomment = comments.get(index);
            comment.setText(subcomment.name+" "+subcomment.lastname+lineSep+subcomment.comment+lineSep);
            holder.linearLayoutComment.addView(comment);
        }

        for (int photoindex = 0; photoindex < photoList.size(); photoindex++) { // dynamicly add pictures
            ImageView imageView = new ImageView(getContext());                  // in new ImagesView's
            holder.linearLayoutPic.addView(imageView);
            imageView.setImageBitmap(Bitmap.createScaledBitmap(photoList.get(photoindex),
                    holder.linearLayoutPic.getMinimumHeight(),
                    holder.linearLayoutPic.getMinimumHeight(), false));
        }

        holder.addCommentButton.setOnClickListener(new View.OnClickListener() { // an click listener for add comment
            @Override
            public void onClick(View v) {
                String commentEdit = holder.commentEdit.getText().toString(); // get the info
                Comment comment = new Comment(SavedInfo.profileInfo.name,
                        SavedInfo.profileInfo.lastname, commentEdit); // create a new comment
                postItem.comments.add(comment); // add it to show it until it failed
                notifyDataSetChanged();

                SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecuteComment, getContext()); // post the comment
                if (task.isNetworkAvailable()) {
                    //the HashMAp<String, JsonObject> is for setting the string ass the url and jsonobj is the data to send to that url
                    HashMap<String, JSONObject> map = new HashMap<>();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("username", SavedInfo.username); // pack necessary into to post a comment
                        jsonObject.put("password", SavedInfo.password);
                        jsonObject.put("id_p", postItem.id_p); // id of the current post
                        jsonObject.put("comment", commentEdit);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    map.put("postcomment", jsonObject);
                    task.execute(map);

                    currentPostItem = position; // save the postItems position in the list so that the comment can be removed if it fails

                    holder.commentEdit.setText("");
                }else Toast.makeText(getContext(), "No internet connection",Toast.LENGTH_SHORT).show();

            }
        });

        holder.httpPostExecuteLike = new HttpPostExecute() {
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) { // sets the like info
                String res = JsonParse.resultParse(jsonObject);
                if (res.equals("invalidInput")) {
                    Toast.makeText(getContext(),"Could not like. Try agin later.", Toast.LENGTH_SHORT).show();
                }else if (res.equals("post was liked")) {
                    postItem.likes += 1;
                    holder.likes.setText(String.valueOf(postItem.likes));
                }else {
                    postItem.likes -= 1;
                    holder.likes.setText(String.valueOf(postItem.likes));
                }
            }
        };

        // i dont set the info directly because it goes so fast to like
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendHttpRequestTask task = new SendHttpRequestTask(holder.httpPostExecuteLike, getContext()); // post the like
                if (task.isNetworkAvailable()) {
                    //the HashMAp<String, JsonObject> is for setting the string ass the url and jsonobj is the data to send to that url
                    HashMap<String, JSONObject> map = new HashMap<>();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("username", SavedInfo.username);
                        jsonObject.put("password", SavedInfo.password);
                        jsonObject.put("id_p", postItem.id_p);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    map.put("addremovelike", jsonObject);
                    task.execute(map);
                }else Toast.makeText(getContext(), "No internet connection",Toast.LENGTH_SHORT).show();

            }
        });

        // set an listener to the textView to change to mapp fragment upon click
        // i did the fragment transaction here to not have to call the mainActivity and do extra code
        holder.showMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // switch to map fragment to show GoogleMap with current positionlist
                Fragment mapFragment = MapFragmentGoogle.getInstance(postitionList);
                SavedInfo.fragmentManager.beginTransaction().replace(R.id.main,  mapFragment).commit();
            }
        });

        return rootView;
    }

    public void setPostItems(List<PostItem> postItems) {
        this.postItems.clear();
        for (PostItem postItem : postItems) {
            this.postItems.add(postItem);
        }
        notifyDataSetChanged();

    }
}
