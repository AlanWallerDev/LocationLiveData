package com.example.t00053669.mapstuff;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

/**
 * Created by t00053669 on 3/12/2018.
 */

public class MyLocationLiveData extends LiveData<Location> {

    private static MyLocationLiveData instance = null;
    private final String TAG = "MyLocationLiveData";

    private FusedLocationProviderClient fusedLocationProviderClient;

    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    public static MyLocationLiveData getInstance(Context context){
        if(instance == null){
            instance = new MyLocationLiveData(context);
        }

        return instance;
    }

    private MyLocationLiveData(Context context){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    if(locationResult.getLastLocation() != null) {
                        setValue(locationResult.getLastLocation());
                    }else{
                        Log.d(TAG, "No Last Known Location");
                    }
                }
            }};

        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(1);


    }

    @Override
    protected void onActive() {
        super.onActive();

            startLocationUpdates();
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        if(fusedLocationProviderClient != null){
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    private void startLocationUpdates() {
        try {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    null /* Looper */);
        }catch(SecurityException e){
            e.printStackTrace();
        }
    }

}
