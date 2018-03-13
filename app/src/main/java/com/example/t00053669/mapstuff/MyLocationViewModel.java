package com.example.t00053669.mapstuff;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.location.Location;

/**
 * Created by t00053669 on 3/12/2018.
 */

public class MyLocationViewModel extends AndroidViewModel {

    private LiveData<Location> location;

    MyLocationViewModel(Application app){
        super(app);

        location = MyLocationLiveData.getInstance(app);
    }

    public LiveData<Location> getLocation(){
        return location;
    }
}
