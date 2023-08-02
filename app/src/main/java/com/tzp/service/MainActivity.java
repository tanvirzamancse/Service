package com.tzp.service;

import android.annotation.SuppressLint;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.tzp.service.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding binding;
    ActivityResultLauncher<IntentSenderRequest> resultLauncher;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 111;
    private static final int REQUEST_CHECK_SETTINGS = 11;
    public Location mLocation;

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

            if (RuntimePermission.getInstance(getApplicationContext()).checkRuntimePermission(this)) {
                createLocationRequest();

         /*       Locations.getLocation(getApplicationContext()).getProviderClient().getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {

                                if (location != null) {
                                    mLocation = location;
                                    Log.d("permission", "onSuccess" + mLocation.getLatitude());
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Log.d("permission", "onFailure");

                                Toast.makeText(MainActivity.this, "Location null", Toast.LENGTH_SHORT).show();
                            }
                        });*/

            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createLocationRequest();

            /*    Locations.getLocation(getApplicationContext()).getProviderClient().getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {

                                if (location != null) {
                                    mLocation = location;

                                    Log.d("permission", "onRequestPermissionsResult " + mLocation.getLatitude());
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("permission", "onFailure");

                                Toast.makeText(MainActivity.this, "Location null", Toast.LENGTH_SHORT).show();
                            }
                        });*/

            } else {

                Toast.makeText(getApplicationContext(), "no permission", Toast.LENGTH_SHORT).show();

            }

        }
    }


    protected void createLocationRequest() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);


        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response  = task.getResult(ApiException.class);
                } catch (ApiException e) {
                   switch (e.getStatusCode()){

                       case REQUEST_CHECK_SETTINGS:
                           try {
                               // Show the dialog by calling startResolutionForResult(),
                               // and check the result in onActivityResult().
                               ResolvableApiException resolvable = (ResolvableApiException) e;
                               resolvable.startResolutionForResult(MainActivity.this,
                                       REQUEST_CHECK_SETTINGS);
                           } catch (IntentSender.SendIntentException sendEx) {
                               // Ignore the error.
                           }
                   }
                }

            }
        });
     /*   task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                Log.d("TAG", "onSuccess: ");
            }
        });*/

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: ");
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

}