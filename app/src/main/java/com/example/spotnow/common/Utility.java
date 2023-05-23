package com.example.spotnow.common;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.Locale;

public abstract class Utility {
    public static Coordinate GetGPS(Context context) {

        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Utility", "Permision Error");
            return null;
        }
        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        return new Coordinate(loc.getLatitude(),loc.getLongitude());
    }

    public static String GetAddressFromGPS(Context context, Coordinate loc){
        if(loc == null)
            return "";

        Geocoder geocoder = new Geocoder(context, Locale.KOREA);
        String result = "";
        try{
            result = geocoder.getFromLocation(loc._latitude,loc._longitude,1).get(0).getAddressLine(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
