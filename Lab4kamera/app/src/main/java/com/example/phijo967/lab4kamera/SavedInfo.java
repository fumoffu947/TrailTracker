package com.example.phijo967.lab4kamera;

import com.example.phijo967.lab4kamera.fragments.Friend;
import com.example.phijo967.lab4kamera.fragments.arrayadapterContent.PostItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fumoffu947 on 2015-05-08.
 */
public class SavedInfo {

    public static Integer id_u = null;
    public static String username = null;
    public static String password = null;

    public static ProfileInfo profileInfo;

    public static List<Friend> userFriends = new ArrayList<>();

    public static List<PostItem> adapterList = new ArrayList<>();

    public static List<PostItem> userflow = new ArrayList<>();

    public static List<PostItem> userpost = new ArrayList<>();

    public static List<PostItem> testContentPost = new ArrayList<>();
}
