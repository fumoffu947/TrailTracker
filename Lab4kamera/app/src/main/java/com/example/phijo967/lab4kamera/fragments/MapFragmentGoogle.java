package com.example.phijo967.lab4kamera.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phijo967.lab4kamera.R;
import com.example.phijo967.lab4kamera.datastruct.SavedInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Created by fumoffu947 on 2015-05-20.
 */
public class MapFragmentGoogle extends Fragment {

    private List<List<Double>> postitionList;
    private MapFragment mMapFragment;

    public static MapFragmentGoogle getInstance(List<List<Double>> positionList) {
        MapFragmentGoogle mapFragmentGoogle = new MapFragmentGoogle();
        mapFragmentGoogle.postitionList = positionList;
        return mapFragmentGoogle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map_google, container, false);

        // set an googlemap
        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                SavedInfo.fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();


        mMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // to set the camera position after the iteration through the positions
                Double minLat = null;
                Double minLng = null;
                Double maxLat = null;
                Double maxLng = null;
                PolylineOptions polylineOptions = new PolylineOptions();

                for (int index = 0; index < postitionList.size(); index++) {
                    Double latitude = postitionList.get(index).get(0);
                    Double longitude = postitionList.get(index).get(1);

                    // calculates the biggest and smallest lat and lng so set camera position
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

                    // add a marker for current position in list
                    LatLng latLng = new LatLng(latitude, longitude);
                    googleMap.addMarker(new MarkerOptions().position(
                            latLng).
                            title(String.valueOf(index)));

                    polylineOptions.add(latLng);
                }
                googleMap.addPolyline(polylineOptions);

                // change the camera position to be over the markers
                if (minLat != null && minLng != null && maxLat != null && maxLng != null) {
                    LatLngBounds bounds = new LatLngBounds(new LatLng(minLat, minLng),
                            new LatLng(maxLat, maxLng));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                }
            }
        });

        return rootView;
    }
}
