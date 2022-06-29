package com.example.native_view_test.camera;

import android.Manifest;

public class Constants {
    public final static String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
    public final static String[] LOCALISATION_PERMISSION = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    public final static String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
}
