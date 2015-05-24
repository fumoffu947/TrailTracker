package com.example.phijo967.lab4kamera.datastruct;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.phijo967.lab4kamera.http.HttpPostExecute;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.MapFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by fumoffu947 on 2015-05-08.
 */
public class SavedInfo {

    public static Integer id_u = null;
    public static String username = null;
    public static String password = null;

    public static ProfileInfo profileInfo;

    public static ProfileInfo searchProfile;
    public static Integer searchId;

    public static List<Friend> userFriends = new ArrayList<>();

    public static List<Friend> searchFriends = new ArrayList<>();

    public static List<PostItem> adapterList = new ArrayList<>();

    public static List<PostItem> userflow = new ArrayList<>();

    public static List<PostItem> userpost = new ArrayList<>();

    public static String mCurrentPhotoPath;
    public static List<Bitmap> picturesList = new ArrayList<>();
    public static LinearLayout picHolderLayout;
    public static boolean startedTraking;
    public static GoogleApiClient mGoogleApiClient;
    public static List<List<Double>> gPSPositions = new ArrayList<>();
    public static Timer getGPSPosTimer;
    public static boolean isTracking;
    public static EditText postName;
    public static EditText postDescription;
    public static HttpPostExecute httpPostExecute;

    public static FragmentManager fragmentManager;
    public static Activity activity;
    public static List<Friend> friendRequests = new ArrayList<>();
    public static List<Message> currentMessages = new ArrayList<>();
    public static int removePositionRequest;
    public static boolean searchFriendInFriends = false;
    public static MapFragment mMapFragment;
    public static Float totalDistance;

    public static void clearSavedInfo() {
        id_u = null;
        username = null;
        password = null;
        profileInfo = null;
        searchProfile = null;
        searchId = null;
        userFriends = new ArrayList<>();
        searchFriends = new ArrayList<>();
        adapterList = new ArrayList<>();
        userflow = new ArrayList<>();
        userpost = new ArrayList<>();
        mCurrentPhotoPath = null;
        picturesList = new ArrayList<>();
        picHolderLayout = null;
        startedTraking = false;
        mGoogleApiClient = null;
        gPSPositions = new ArrayList<>();
        getGPSPosTimer = null;
        isTracking = false;
        postName = null;
        postDescription = null;
        httpPostExecute = null;
        fragmentManager = null;
        activity = null;
        friendRequests = new ArrayList<>();
        mMapFragment = null;

    }
}
