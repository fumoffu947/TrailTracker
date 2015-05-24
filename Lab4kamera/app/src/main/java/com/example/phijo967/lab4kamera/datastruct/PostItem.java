package com.example.phijo967.lab4kamera.datastruct;

import android.graphics.Bitmap;

import com.example.phijo967.lab4kamera.datastruct.Comment;

import java.util.List;

/**
 * Created by fumoffu947 on 2015-05-09.
 */
public class PostItem {

    public String userName;
    public String userLastname;
    public String name;
    public String description;
    public Integer id_p;
    public Integer likes;
    public List<Comment> comments;
    public List<List<Double>> positionList;
    public List<Bitmap> photoList;

    public PostItem(String userName, String userLastname, String name, String description,
                    Integer id_p, Integer likes, List<Comment> comments,
                    List<List<Double>> positionList, List<Bitmap> photoList) {
        this.userName = userName;
        this.userLastname = userLastname;
        this.name = name;
        this.description = description;
        this.id_p = id_p;
        this.likes = likes;
        this.comments = comments;
        this.positionList = positionList;
        this.photoList = photoList;
    }
}
