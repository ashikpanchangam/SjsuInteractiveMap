package com.example.ashikspc.sjsuinteractivemap;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Jayesh on 10/29/2016.
 */

public class MyLocationListener implements LocationListener {

    private MapActivity mainAct;

    public MyLocationListener(MapActivity ma){
        mainAct = ma;
    }

    @Override
    public void onLocationChanged(Location loc) {
        mainAct.plotUser(loc.getLatitude(), loc.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}
