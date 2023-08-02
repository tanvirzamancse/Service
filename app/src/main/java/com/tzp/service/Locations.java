package com.tzp.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class Locations {
    private static Locations currentLocations;
    private FusedLocationProviderClient providerClient;
    private static Context context;

    public static Locations getLocation(Context mContext) {
        if (currentLocations == null) {
            context = mContext;
            currentLocations = new Locations();
        }
        return currentLocations;
    }


    @SuppressLint("MissingPermission")
    public void getCurrentLocation(Context context) {

        getProviderClient().getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                       // new myLocation(location);

                        if (location==null){
                            return;
                        }
                        Toast.makeText(context, "My : "+location, Toast.LENGTH_SHORT).show();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(context, "Exception : " + e, Toast.LENGTH_SHORT).show();

                    }
                });

    }

/*
    private LatLng myLocation(android.location.Location location) {
        return new LatLng(location.getLatitude(),location.getLongitude());
    }
*/


    public FusedLocationProviderClient getProviderClient() {
        providerClient = LocationServices.getFusedLocationProviderClient(context);
        return providerClient;
    }


}
