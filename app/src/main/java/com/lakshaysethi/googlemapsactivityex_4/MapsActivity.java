package com.lakshaysethi.googlemapsactivityex_4;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private static final int LOCATION_REQUEST_CODE = 1278;
    FusedLocationProviderClient locationClient;
    double latitude;
    double longitude;


    Button getLocationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        locationClient = new FusedLocationProviderClient(this);
        getLocationBtn = findViewById(R.id.getLocationBtn);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Button onclick listener
        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (permissionGranted()){
                    if (getAndSetLocationCoordinates()){
                        mMap.clear();


                    }else{
                        Toast.makeText(MapsActivity.this, "Please Wait.. getting Location, make sure you have LOCATION \"ON\"", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private boolean getAndSetLocationCoordinates() {
        Task<Location> getLocationTask = locationClient.getLastLocation();
        getLocationTask.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> getLocationTask) {
                if(getLocationTask.getResult() != null){
                    latitude = getLocationTask.getResult().getLatitude();
                    longitude = getLocationTask.getResult().getLongitude();
                    addMarkerToMap(latitude,longitude);
                }else{
                    Toast.makeText(MapsActivity.this, "Please Wait.. getting Location, make sure you have LOCATION \"ON\"", Toast.LENGTH_SHORT).show();
                }

            }
        });
        if (longitude != 0){
            return true;
        }
        return true;
    }


    private void addMarkerToMap(double lat,double lon) {

        LatLng latLng = new LatLng(lat,lon);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
    }



    private boolean permissionGranted() {
        //if not
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "LOCATION PERMISSION NOT GRANTED PLEASE GRANT LOCATION PERMISSION", Toast.LENGTH_SHORT).show();
        //ask for permission
            ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },LOCATION_REQUEST_CODE);
            return false;
        }else{
            return true;
        }


    }

}
