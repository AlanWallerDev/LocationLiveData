package com.example.t00053669.mapstuff;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final static String TAG = "Main Activity";
    private final int PERMISSION_REQ_CODE = 100;

    private MyLocationViewModel myLocationViewModel;
    private static Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        myLocationViewModel = ViewModelProviders.of(this).get(MyLocationViewModel.class);
        checkLocationPermission();
        startObserving();
    }

    private void startObserving(){
        myLocationViewModel.getLocation().observe(this, new Observer<Location>() {
            @Override
            public void onChanged(@Nullable Location location) {

                String lastUpdateTime = DateFormat.getDateTimeInstance().format(new Date());
                updateUI(lastUpdateTime, location);
                /**
                 if(mToast != null){
                 mToast.cancel();
                 }else{
                 mToast = Toast.makeText(this, "Updated at: " + String.format(Locale.ENGLISH, "%s: %s", "Time: ", lastUpdateTime), Toast.LENGTH_SHORT).show();
                 }**/

            }
        });
    }

    private void updateUI(String lastUpdateTime, Location location){
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        Log.d(TAG, "last update" + lastUpdateTime + "location: Lat: " + location.getLatitude() + "Long: " + location.getLongitude());
    }

    private boolean checkLocationPermission(){
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return (permissionState == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case PERMISSION_REQ_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startObserving();
                }else{
                    Log.d(TAG, "Permission Denied");
                }
            }
        }
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
}
