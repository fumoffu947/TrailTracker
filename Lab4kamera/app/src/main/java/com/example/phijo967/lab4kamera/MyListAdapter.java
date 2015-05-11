package com.example.phijo967.lab4kamera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.phijo967.lab4kamera.fragments.arrayadapterContent.Comment;
import com.example.phijo967.lab4kamera.fragments.arrayadapterContent.PostItem;
import com.example.phijo967.lab4kamera.http.HttpPostExecute;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by fumoffu947 on 2015-05-09.
 */
public class MyListAdapter extends ArrayAdapter<PostItem> {

    private final Context context;
    private final List<PostItem> postItems;
    private final HttpPostExecute httpPostExecute;

    public MyListAdapter(Context context, int resource, List<PostItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.postItems = objects;
        this.httpPostExecute = new HttpPostExecute() {
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) {

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
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = convertView;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            rootView = inflater.inflate(R.layout.post_layout, parent,false);

            // configure the ViewHolder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.nameOfPost = (TextView) rootView.findViewById(R.id.postName);
            viewHolder.descriptionOfPost = (TextView) rootView.findViewById(R.id.postDescription);
            viewHolder.userName = (TextView) rootView.findViewById(R.id.postUserName);
            viewHolder.userLastname = (TextView) rootView.findViewById(R.id.postUserLastname);
            viewHolder.likes = (TextView) rootView.findViewById(R.id.postLikes);
            viewHolder.linearLayoutPic = (LinearLayout) rootView.findViewById(R.id.postLinearLayoutPic);
            viewHolder.linearLayoutComment = (LinearLayout) rootView.
                    findViewById(R.id.postLinearLayoutComment);
            viewHolder.commentEdit = (EditText) rootView.findViewById(R.id.postAddCommentEdit);
            viewHolder.addCommentButton = (Button) rootView.findViewById(R.id.postPostComment);
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
        List<List<Float>> postitionList = postItem.positionList;
        List<Bitmap> photoList = postItem.photoList;

        for (int index = 0; index < comments.size(); index++) { // dynamically add comments
            String lineSep = System.getProperty("line.separator");
            TextView comment = new TextView(context);
            Comment subcomment = comments.get(index);
            comment.setText(subcomment.name+" "+subcomment.lastname+lineSep+subcomment.comment+lineSep);
            holder.linearLayoutComment.addView(comment);
        }

        holder.addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentEdit = holder.commentEdit.getText().toString();
                Comment comment = new Comment(SavedInfo.profileInfo.name,
                        SavedInfo.profileInfo.lastname, commentEdit);
                postItem.comments.add(comment);
                notifyDataSetChanged();
                holder.commentEdit.setText("");
                //do network call
            }
        });
        return rootView;
    }
}
