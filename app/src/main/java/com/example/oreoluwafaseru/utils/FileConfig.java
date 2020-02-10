package com.example.oreoluwafaseru.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class FileConfig {
    public static String fileName = "car_ownsers_data.csv";
    public static String dirName = "venten";

    public static final int RE_EXTERNAL_STORAGE = 1234;
    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE

    };

    public static void verifyStoragePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, PERMISSIONS_STORAGE[1]);

        if(permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    RE_EXTERNAL_STORAGE
            );
        }
    }
}
