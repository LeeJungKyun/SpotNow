package com.example.spotnow.common;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.Locale;

public abstract class Utility {

    // Retrieves the GPS coordinates of the device
    public static Coordinate GetGPS(Context context) {

        // Get the LocationManager instance
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Check if location permissions are granted
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Utility", "Permission Error");
            return null;
        }

        // Retrieve the last known location using the NETWORK_PROVIDER
        Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        // If the location is null, return null
        if (loc == null)
            return null;

        // Create a new Coordinate object using the latitude and longitude values of the location
        return new Coordinate(loc.getLatitude(), loc.getLongitude());
    }

    // Retrieves the address corresponding to the given GPS coordinates
    public static String GetAddressFromGPS(Context context, Coordinate loc) {
        if (loc == null)
            return "";

        // Create a Geocoder instance with the default Locale set to Korea
        Geocoder geocoder = new Geocoder(context, Locale.KOREA);
        String result = "";
        try {
            // Get the address line from the location coordinates
            result = geocoder.getFromLocation(loc._latitude, loc._longitude, 1).get(0).getAddressLine(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
