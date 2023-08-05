package com.tzp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class MyService extends Service {

    WorkManager workManager = WorkManager.getInstance(this);
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest
                .Builder(MyWorker.class,
                5,
                TimeUnit.SECONDS)
                .build();

        workManager.enqueue(workRequest);
    }
}
