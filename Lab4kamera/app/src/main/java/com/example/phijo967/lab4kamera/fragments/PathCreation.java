package com.example.phijo967.lab4kamera.fragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phijo967.lab4kamera.JsonParse;
import com.example.phijo967.lab4kamera.MainActivity;
import com.example.phijo967.lab4kamera.R;
import com.example.phijo967.lab4kamera.datastruct.SavedInfo;
import com.example.phijo967.lab4kamera.http.HttpPostExecute;
import com.example.phijo967.lab4kamera.http.SendHttpRequestTask;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PathCreation extends Fragment {

    private OnPathCreationInteractionListener mListener;
    private TextView pathCreationLenghtWent;
    private Button startEndTracking;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SavedInfo.httpPostExecute = new HttpPostExecute() {
            @Override
            public void httpOnPostExecute(JSONObject jsonObject) {
                String res = JsonParse.resultParse(jsonObject);
                Toast.makeText(getActivity(), res,Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_path_creation, container, false);




        SavedInfo.activity = getActivity();

        // add a googleMap to the fragmentlayout
        if (SavedInfo.mMapFragment == null) SavedInfo.mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                SavedInfo.fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.pathCreationMap, SavedInfo.mMapFragment);
        fragmentTransaction.commit();

        // finds all relevant views
        SavedInfo.postName = (EditText) rootView.findViewById(R.id.pathCreationpostNameEdit);
        SavedInfo.postDescription = (EditText) rootView.findViewById(R.id.pathCreationDescriptionEdit);

        this.pathCreationLenghtWent = (TextView) rootView.findViewById(R.id.pathCreationLengthWentText);

        SavedInfo.picHolderLayout = (LinearLayout) rootView.findViewById(R.id.pathCreationPicHolderLayout);
        Button addPic = (Button) rootView.findViewById(R.id.pathCreationAddPictureButton);
        this.startEndTracking = (Button) rootView.findViewById(R.id.pathCreationStartPositionTrackingButton);
        Button postPath = (Button) rootView.findViewById(R.id.pathCreationPostPathButton);

        // set onClickListener to post the path
        postPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfOkInput()) return;

                SendHttpRequestTask task = new SendHttpRequestTask(SavedInfo.httpPostExecute, getActivity());
                if (task.isNetworkAvailable()) {
                    //the HashMAp<String, JsonObject> is for setting the string ass the url and jsonobj is the data to send to that url
                    HashMap<String, JSONObject> map = new HashMap<>();
                    JSONObject jsonObject = new JSONObject();
                    String photoString = MainActivity.bitmapToBase64(SavedInfo.picturesList); // make the pictures to strings and
                    try {
                        jsonObject.put("username", SavedInfo.username); // pack all the info about the currentPath
                        jsonObject.put("password", SavedInfo.password); // and send it
                        jsonObject.put("id_u", SavedInfo.id_u);
                        jsonObject.put("photos", photoString);
                        jsonObject.put("position_list", SavedInfo.gPSPositions);
                        jsonObject.put("description", SavedInfo.postDescription.getText().toString());
                        jsonObject.put("name", SavedInfo.postName.getText().toString());
                        jsonObject.put("length_went", SavedInfo.totalDistance.intValue());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    map.put("postpath", jsonObject);
                    task.execute(map);
                } else Toast.makeText(getActivity(), "No internet connection",Toast.LENGTH_SHORT).show();

            }
        });

        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SavedInfo.picturesList.size() < 5){ // so memmory allocation wont me to big
                    takePicture();
                }
                else Toast.makeText(getActivity(), "Reached max pictures", Toast.LENGTH_SHORT).show();

            }
        });



        // start and stop tracking positions with gps and set the text
        startEndTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SavedInfo.isTracking) {
                    // chacks if the gps is active
                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && SavedInfo.mGoogleApiClient.isConnected()) {
                        // starts a lcation updater requester
                        LocationRequest locationRequest = new LocationRequest();
                        locationRequest.setFastestInterval(5000);
                        locationRequest.setInterval(10000);
                        // listener to get the location from locationUpdates
                        SavedInfo.locationListener = new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                System.out.println(String.valueOf(location.getLatitude()) + String.valueOf(location.getLongitude()));
                                if (location != null) {
                                    List<Double> positions = new ArrayList<>();
                                    positions.add(location.getLatitude());
                                    positions.add(location.getLongitude());
                                    SavedInfo.gPSPositions.add(positions);
                                }
                                // sets markers and lines
                                SavedInfo.mMapFragment.getMapAsync(new OnMapReadyCallback() { // has to be called from main thread
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        setGoogleMarkers();
                                    }
                                });
                            }
                        };
                        // starts a location update with evn intervals
                        SavedInfo.locationUpdater = LocationServices.FusedLocationApi.requestLocationUpdates(SavedInfo.mGoogleApiClient, locationRequest, SavedInfo.locationListener);
                        SavedInfo.isTracking = true;
                        startEndTracking.setText("Stop Tracking");
                    }else startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                } else {
                    startEndTracking.setText("Start Tracking");
                    SavedInfo.isTracking = false;
                    // stops the update (removes the updater)
                    LocationServices.FusedLocationApi.removeLocationUpdates(SavedInfo.mGoogleApiClient,SavedInfo.locationListener);
                }
            }
        });

        setGoogleMarkers();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        SavedInfo.activity = getActivity();
        setPictures();
        if (SavedInfo.isTracking) { // if the timer gets the gps positions the change the text on button
            startEndTracking.setText("Stop Tracking");
        }
    }

    private boolean checkIfOkInput() { // checks input of the path Name to se if is to long or to short
        if (SavedInfo.postName.getText().toString().length() < 2) {
            Toast.makeText(getActivity(), "Pleas enter a longer Name", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (SavedInfo.postName.getText().toString().length() > 100) {
            Toast.makeText(getActivity(), "Pleas enter a shorter Name", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void setGoogleMarkers() {
        SavedInfo.mMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.clear();
                // to set the camera position after the iteration through the positions
                Double minLat = null;
                Double minLng = null;
                Double maxLat = null;
                Double maxLng = null;
                PolylineOptions polylineOptions = new PolylineOptions();

                for (int index = 0; index < SavedInfo.gPSPositions.size(); index++) {
                    Double latitude = SavedInfo.gPSPositions.get(index).get(0);
                    Double longitude = SavedInfo.gPSPositions.get(index).get(1);

                    // calculates the biggest and smallest lat and lng
                    if (minLat == null) {
                        minLat = latitude;
                    } else minLat = Math.min(minLat, latitude);
                    if (minLng == null) {
                        minLng = longitude;
                    } else minLng = Math.min(minLng, longitude);

                    if (maxLat == null) {
                        maxLat = latitude;
                    } else maxLat = Math.max(maxLat, latitude);
                    if (maxLng == null) {
                        maxLng = longitude;
                    } else maxLng = Math.max(maxLng, longitude);


                    // add marker on the map
                    LatLng latLng = new LatLng(latitude, longitude);
                    googleMap.addMarker(new MarkerOptions().position(
                            latLng).
                            title(String.valueOf(index)));

                    polylineOptions.add(latLng);
                }
                googleMap.addPolyline(polylineOptions);

                // move the camera so that the markers is in the view
                if (minLat != null && minLng != null && maxLat != null && maxLng != null) {
                    LatLngBounds bounds = new LatLngBounds(new LatLng(minLat, minLng),
                            new LatLng(maxLat, maxLng));
                    try {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                    } catch (Exception e) {
                        e.printStackTrace();
                        // if View is to small
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
                    }
                }

                // calculates the distance between the positions and set it to a TextView
                if (SavedInfo.gPSPositions.size() > 1) {
                    SavedInfo.totalDistance = Float.valueOf(0);
                    for (int index = 1; index < SavedInfo.gPSPositions.size(); index++) {
                        Location locationA = new Location(""); // make locations
                        Location locationB = new Location("");

                        List<Double> pointA = SavedInfo.gPSPositions.get(index - 1);
                        List<Double> pointB = SavedInfo.gPSPositions.get(index);

                        // sets the long and lat for the locations
                        locationA.setLatitude(pointA.get(0));
                        locationA.setLongitude(pointA.get(1));
                        locationB.setLatitude(pointB.get(0));
                        locationB.setLongitude(pointB.get(1));

                        // calculate the distance between the locations
                        Float distance = locationA.distanceTo(locationB);
                        SavedInfo.totalDistance += distance;
                    }
                    pathCreationLenghtWent.setText(String.valueOf(SavedInfo.totalDistance));
                }
            }
        });
    }

    /**
     * setts all the Bitmaps from SavedInfo.picturesList in dynamicly added imageView
     * (already scaled down
     */
    private void setPictures() {
        for (Bitmap bitmap : SavedInfo.picturesList) {
            ImageView currentImageView = new ImageView(getActivity());
            currentImageView.setMaxHeight(SavedInfo.picHolderLayout.getMinimumHeight());
            currentImageView.setMaxWidth(SavedInfo.picHolderLayout.getMinimumWidth());
            SavedInfo.picHolderLayout.addView(currentImageView);
            currentImageView.setImageBitmap(bitmap);
        }
    }

    public void takePicture() {
        if (mListener != null) {
            mListener.onPathCreationInteraction();
        }
    }

    @Override // auto generated
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnPathCreationInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnPathCreationInteractionListener {

        public void onPathCreationInteraction();
    }

}
