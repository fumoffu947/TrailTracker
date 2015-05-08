package com.example.phijo967.lab4kamera;

/**
 * Created by fumoffu947 on 2015-05-08.
 */
public class ProfileInfo {
    private String Name;
    private String Lastname;
    private int numberOfPaths;
    private int numberOfSteps;
    private int lengthWent;

    public ProfileInfo(String name, String lastname, int numberOfPaths, int numberOfSteps, int lengthWent) {
        Name = name;
        Lastname = lastname;
        this.numberOfPaths = numberOfPaths;
        this.numberOfSteps = numberOfSteps;
        this.lengthWent = lengthWent;
    }
}
