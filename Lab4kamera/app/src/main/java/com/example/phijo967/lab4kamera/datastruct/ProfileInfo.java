package com.example.phijo967.lab4kamera.datastruct;

import android.graphics.Bitmap;

/**
 * Created by fumoffu947 on 2015-05-08.
 */
public class ProfileInfo {
    public String name;
    public String lastname;
    public int numberOfPaths;
    public int lengthWent;
    public Bitmap profilePic;
    public Boolean inFriends = false;

    public ProfileInfo(String name, String lastname, int numberOfPaths, int lengthWent, Bitmap profilePic) {
        this.name = name;
        this.lastname = lastname;
        this.numberOfPaths = numberOfPaths;
        this.lengthWent = lengthWent;
        this.profilePic = profilePic;
    }
}