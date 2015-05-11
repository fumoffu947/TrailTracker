package com.example.phijo967.lab4kamera;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phijo967.lab4kamera.fragments.LoginScreen;
import com.example.phijo967.lab4kamera.fragments.PostsFragment;
import com.example.phijo967.lab4kamera.fragments.ProfileScreen;
import com.example.phijo967.lab4kamera.fragments.SignUp;
import com.example.phijo967.lab4kamera.http.HttpPostExecute;
import com.example.phijo967.lab4kamera.http.SendHttpRequestTask;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LoginScreen.OnLoginInteractionListener,
        SignUp.OnSignUpInteractionListener, PostsFragment.OnPostFragmentInteractionListener, ProfileScreen.OnProfileScreenInteractionListener{

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView mImageView;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private TextView locationText;
    private String mCurrentPhotoPath;
    private String troll;
    private ImageView mImageView2;
    private HttpPostExecute httpPostExecute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        getFragmentManager().beginTransaction().add(R.id.main,new LoginScreen(), "login").commit();
        //locationText = (TextView) findViewById(R.id.textView2);
        //mImageView = (ImageView) findViewById(R.id.imageView);
        //this.mImageView2 = (ImageView) findViewById(R.id.imageView2);
        //Button button = (Button) findViewById(R.id.button);
        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePic();
            }
        });*/

        SavedInfo.profileInfo = new ProfileInfo("philip", "Johansson", 0, 0, 0);

        this.httpPostExecute = new HttpPostExecute() {
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) {
                System.out.println(jsonObject.toString());
            }
        };

        SendHttpRequestTask task = new SendHttpRequestTask(httpPostExecute);
        HashMap<String, JSONObject> map = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", "test1");
            jsonObject.put("password","test1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        map.put("login",jsonObject);
        task.execute(map);
    }


    protected synchronized void buildGoogleApiClient() { // starts the googleappClient so that i have access to it so i can look for an location
        System.out.println("googleBuilder");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
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
            image = File.createTempFile(
                    imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("could not make file");
        }
        this.mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {// starts an imagecapture intent so i can take an picture with the camera an use the pic
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            photoFile = createImageFile();
            if (photoFile != null) {
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void setPic() {
        int targetW = mImageView2.getMaxWidth();
        int targetH = mImageView2.getMaxHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth; // get the photo width and height
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.max(photoW / targetW, photoH / targetH);
        if (scaleFactor == 1) {
            if (photoH > targetH || photoW > targetW) {
                scaleFactor = 2; // sets the scaleFactor to 2 if the immage is wider or higer than
                                //(my cellphones max bit image size to set in an imageview)
            }
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions); //returns a scaled copy of the image
         mImageView2.setImageBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            //mImageView.setImageBitmap(imageBitmap);
            if (mCurrentPhotoPath != null) {
                setPic();
            }

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Faild to get Address");
                }
                if (addresses != null) {
                    Address address = addresses.get(0);
                    System.out.println(address.toString());
                    locationText.setText(address.getAddressLine(0) + " : " + address.getLocality());
                }
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
        mGoogleApiClient.reconnect();

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
        //Switch to Fragment profile
        Toast.makeText(this, "Login Succesful with "+username+" "+password, Toast.LENGTH_SHORT).show();
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
    public void onPostFragmentInteraction(String id) {

    }

    @Override
    public void onProfileScreenInteraction(String s) {
        if (s.equals("addPost")) {
            PostsFragment fragment = new PostsFragment();
            Bundle arg = new Bundle();
            arg.putString("datatype","userpost");
            fragment.setArguments(arg);
            getFragmentManager().beginTransaction().add(R.id.profileScreenFragmentContainer,
                    fragment).commit();
        }
    }
}