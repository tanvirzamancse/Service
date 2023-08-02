package com.tzp.service;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class RuntimePermission {
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 111;
    private static RuntimePermission permission;
    private static Context rContext;

    public static RuntimePermission getInstance(Context context) {

        if (permission == null) {
            rContext = context;
            permission = new RuntimePermission();
        }
        return permission;

    }

    public boolean isInternetConnected(){

        NetworkInfo activeNetworkInfo = null;
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) rContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception ignored) {
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }


    public boolean checkRuntimePermission(Activity activity) {

        int locationFine = ContextCompat.checkSelfPermission(rContext, Manifest.permission.ACCESS_FINE_LOCATION);
        int locationCoarse = ContextCompat.checkSelfPermission(rContext, Manifest.permission.ACCESS_COARSE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (locationFine != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (locationCoarse != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {

            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[0]), REQUEST_ID_MULTIPLE_PERMISSIONS);

            return false;

        }

        return true;


    }


    public boolean checkStoreRuntimePermission(Activity activity) {

        int cameraPermission = ContextCompat.checkSelfPermission(rContext, Manifest.permission.CAMERA);
        int storagePermision = ContextCompat.checkSelfPermission(rContext, Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (storagePermision != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {

            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[0]), REQUEST_ID_MULTIPLE_PERMISSIONS);

            return false;

        }

        return true;


    }

}
