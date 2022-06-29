package com.example.native_view_test;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.camera.view.PreviewView;

import com.example.native_view_test.camera.CameraController;
import com.example.native_view_test.camera.NativeViewFactory;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity {


    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);

        View view = LayoutInflater.from(this).inflate(R.layout.activity_camera,null);
        PreviewView cameraPreview = view.findViewById(R.id.camera_view);
        CameraController cameraController = new CameraController(this,this, cameraPreview);

        flutterEngine
                .getPlatformViewsController()
                .getRegistry()
                .registerViewFactory("<platform-view-type>", new NativeViewFactory(view));

        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), "CameraService").setMethodCallHandler((call, result) -> {
            if(call.method.equals("initialize")) {
                cameraController.startCamera();
            }
            else if(call.method.equals("dispose")) {
                cameraController.stopCamera();
            }
        });
    }
}
