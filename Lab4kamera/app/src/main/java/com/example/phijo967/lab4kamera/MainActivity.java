package com.example.phijo967.lab4kamera;

import android.app.ActivityManager;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.phijo967.lab4kamera.datastruct.Friend;
import com.example.phijo967.lab4kamera.datastruct.SavedInfo;
import com.example.phijo967.lab4kamera.fragments.FriendMessagesFragment;
import com.example.phijo967.lab4kamera.fragments.FriendsFragment;
import com.example.phijo967.lab4kamera.fragments.LoginScreen;
import com.example.phijo967.lab4kamera.fragments.Messages;
import com.example.phijo967.lab4kamera.fragments.Options;
import com.example.phijo967.lab4kamera.fragments.PathCreation;
import com.example.phijo967.lab4kamera.fragments.PostsFragment;
import com.example.phijo967.lab4kamera.fragments.ProfileScreen;
import com.example.phijo967.lab4kamera.fragments.SearchProfile;
import com.example.phijo967.lab4kamera.fragments.SignUp;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LoginScreen.OnLoginInteractionListener,
        SignUp.OnSignUpInteractionListener,
        ProfileScreen.OnProfileScreenInteractionListener, FriendsFragment.OnFragmentInteractionListener,
        PathCreation.OnPathCreationInteractionListener, Options.OnOptionInteractionListener,
        FriendMessagesFragment.OnMessageInteractionListener{

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private LinearLayout mainBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildGoogleApiClient();
        SavedInfo.mGoogleApiClient.connect();
        this.mainBar = (LinearLayout) findViewById(R.id.mainBarHolder);
        if (SavedInfo.username == null) { // if the app has been opend en closed without being killed
            mainBar.setVisibility(View.GONE);
            getFragmentManager().beginTransaction().add(R.id.main, new LoginScreen(), "login").commit();
        }
        // saves the fragmentManager so that you can use it to add GoogleMaps in other fragments so
        // they dont have to call on Mainactivity
        SavedInfo.fragmentManager = getFragmentManager();
    }

     /**
     * Converts Bitmaps to base64 Strings and compress them
     *
     * @param photoBitmaps
     * all the images to conver
     * @return
     * returns a string of base64 String seperated with a ","
     */
    public static String bitmapToBase64(List<Bitmap> photoBitmaps) {
        List<String> res = new ArrayList<>();
        for (Bitmap photoBitmap : photoBitmaps) {

            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            photoBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bOut); // compress bitmap so it wont be so bigg
            byte[] byteMap = bOut.toByteArray();

            // convert to String
            String result = Base64.encodeToString(byteMap, Base64.DEFAULT);

            res.add(result);
        }
        // add all the string together
        String stringRes = "";
        if (res.size() > 0) {
            stringRes = res.remove(0);
            for (String re : res) {
                stringRes += "," + re;
            }
        }
        return stringRes;
    }


    protected synchronized void buildGoogleApiClient() { // starts the googleappClient so that i have access to it so i can look for an location
        System.out.println("googleBuilder");
        SavedInfo.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void takePic() {
        dispatchTakePictureIntent();

    }

    public File createImageFile() {
        //creates a file to store the picture in so i can access the image and send it att a later point
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            // creates the image file
            image = File.createTempFile(
                    imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("could not make file");
        }
        SavedInfo.mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {// starts an imagecapture intent so i can take an picture with the camera an use the pic
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            photoFile = createImageFile();
            if (photoFile != null) { // if the image file was created take the picture
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void saveScaledBitmap() {
        int targetH = SavedInfo.picHolderLayout.getMinimumHeight(); // set är maxHeight to

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(SavedInfo.mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth; // get the photo width and height
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.max(photoW / targetH, photoH / targetH);
        if (scaleFactor == 1) {
            if (photoH > targetH || photoW > targetH) {
                scaleFactor = 2; // sets the scaleFactor to 2 if the immage is wider or higer than
                                //(my cellphones max bit image size to set in an imageview)
            }
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(SavedInfo.mCurrentPhotoPath, bmOptions); //returns a scaled copy of the image
        // saves the scaled down bitmap to use in other fragments
        SavedInfo.picturesList.add(bitmap);
        File file = new File(SavedInfo.mCurrentPhotoPath);
        file.delete();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // when te picture is taken this takes car od the result
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (SavedInfo.mCurrentPhotoPath != null) {
                saveScaledBitmap();
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        System.out.println("connected");

    }


    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("connection suspended");
        SavedInfo.mGoogleApiClient.reconnect();

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("connection faild");
    }

    public void signUpFragment(View view) {
        switchFragment(new SignUp());
    }

    public void backToLogin(View view) {
        switchFragment(new LoginScreen());
    }


    @Override
    public void onLoginInteraction(String username, String password) {
        mainBar.setVisibility(View.VISIBLE);
        switchFragment(new ProfileScreen());

    }

    @Override
    public void onSigUpInteraction() {
        switchFragment(new LoginScreen());
    }

    public void switchFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.main, fragment).commit();
    }

    @Override
    public void onProfileScreenInteraction(String s) {
        // sets so that the PostFragment load int the userPost instead of userflow
        if (s.equals("addPost")) {
            PostsFragment fragment = new PostsFragment();
            Bundle arg = new Bundle();
            arg.putString("datatype","userpost");
            fragment.setArguments(arg);
            getFragmentManager().beginTransaction().add(R.id.profileScreenFragmentContainer,
                    fragment).commit();
        }
    }

    public void toFriends(View view) {
        clearPostInfo();
        FriendsFragment fragment = new FriendsFragment();
        fragment.setArguments(new Bundle());
        switchFragment(fragment);

    }

    public void toOptions(View view) {
        clearPostInfo(); // clears the SavedInfo.pictureList which contains the pictures taken
        switchFragment(new Options());
    }

    public void toFlow(View view) {
        clearPostInfo();
        // sets so that PostFragment load userFlow instead of userPost
        Bundle arg = new Bundle();
        arg.putString("datatype","userflow");
        Fragment fragment = new PostsFragment();
        fragment.setArguments(arg);
        switchFragment(fragment);
    }

    public void toNewPath(View view) {
        clearPostInfo();
        switchFragment(new PathCreation());
    }

    public void toProfile(View view) {
        clearPostInfo();
        switchFragment(new ProfileScreen());
    }

    @Override
    public void onFriendInteraction(Boolean isSearch) {
        // sets so that the adapter loads the adapter loads from SavedInfo.searchFriends instead of userFriends
        Bundle arg = new Bundle();
        arg.putBoolean("search", isSearch);
        Fragment fragment = new FriendsFragment();
        fragment.setArguments(arg);
        switchFragment(fragment);
    }

    @Override
    public void switchToSearchProfile() {
        switchFragment(new SearchProfile());
    }

    @Override
    public void onPathCreationInteraction() {
        takePic();
    }

    public void clearPostInfo() {
        //clears the captures pictures and gps positions
        SavedInfo.picturesList = new ArrayList<>();
        SavedInfo.gPSPositions = new ArrayList<>();
        SavedInfo.mMapFragment = null;
    }

    @Override
    public void onOptionInteraction() {
        switchFragment(new LoginScreen());
    }

    @Override
    public void onOptionTakePic() {
        takePic();
    }

    public void toMessages(View view) {
        clearPostInfo();
        switchFragment(new FriendMessagesFragment());
    }

    @Override
    public void onMessagesFriendInteraction(Friend friend) {
        Messages fragment = Messages.getInstance(friend);
        switchFragment(fragment);

    }
}