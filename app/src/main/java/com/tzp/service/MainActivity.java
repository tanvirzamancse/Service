package com.tzp.service;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.tzp.service.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding binding;
    ActivityResultLauncher<IntentSenderRequest> resultLauncher;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 111;
    private static final int REQUEST_CHECK_SETTINGS = 11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializations();
        body();
        clickEvent();
    }

    private void initializations() {

    }

    private void body() {
    }

    private void clickEvent() {

        binding.buttonPanel.setOnClickListener(view -> {
            createLocationRequest();
        });
    }


    protected void createLocationRequest() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);


        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(this, locationSettingsResponse -> {

            if (locationSettingsResponse.getLocationSettingsStates().isGpsUsable()) {

               if (RuntimePermission.getInstance(getApplicationContext()).checkRuntimePermission(this)){
                   startBackgroundService();
               }
            }
        });

        task.addOnFailureListener(this, e -> {

            if (e instanceof ResolvableApiException) {

                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException sendEx) {

                }
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){

            if (RuntimePermission.getInstance(getApplicationContext()).checkRuntimePermission(this)){
                startBackgroundService();
            }

        }else {
            Log.d("onTask", "RESULT_FAIL");
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startBackgroundService();

            } else {

                Toast.makeText(getApplicationContext(), "no permission", Toast.LENGTH_SHORT).show();

            }

        }
    }


    private void startBackgroundService() {
        Log.d("TAG", "startBackgroundService: ");
    }

}