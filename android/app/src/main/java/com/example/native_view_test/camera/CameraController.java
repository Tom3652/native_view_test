package com.example.native_view_test.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CameraController {

    private final Context context;
    private ImageCapture _imageCapture;
    private final PreviewView cameraPreview;
    private final LifecycleOwner lifecycleOwner;
    private boolean isInitialized = false;

    public CameraController(Context context, LifecycleOwner lifecycleOwner, PreviewView camera) {
        this.cameraPreview = camera;
        this.lifecycleOwner = lifecycleOwner;
        this.context = context;
    }


    /**
     * To start camera preview and capture
     */
    @SuppressLint("MissingPermission")
    public void startCamera() {
        if(!isInitialized) {
            ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(context.getApplicationContext());
            try {
                //Init imageCapture
                _imageCapture = new ImageCapture.Builder().build();
                cameraProviderFuture.addListener(() -> {
                    try {
                        // Used to bind the lifecycle of cameras to the lifecycle owner
                        ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                        Preview preview = new Preview.Builder().build();
                        preview.setSurfaceProvider(cameraPreview.getSurfaceProvider());

                        CameraSelector backCamera = CameraSelector.DEFAULT_BACK_CAMERA;
                        try {
                            // Unbind use cases before rebinding
                            cameraProvider.unbindAll();

                            // Bind use cases to camera
                            cameraProvider.bindToLifecycle(lifecycleOwner, backCamera, preview, _imageCapture);
                            isInitialized = true;
                        } catch (Exception exc) {
                            Log.e("POC_PHOTO", "Use case binding failed", exc);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, ContextCompat.getMainExecutor(context));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * To stop camera preview and capture
     */
    public void stopCamera() {
        if(isInitialized) {
            ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(context.getApplicationContext());
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                cameraProvider.unbindAll();
                isInitialized = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Action to take photo/picture
     */
    public void takePhoto(CaptureListener captureListener) {
        Log.d("TAG", "takePhoto: Image capture : " + _imageCapture);
        if(_imageCapture == null )
            return;

        // Création d'un fichier pour enregistrer la nouvelle photo.
        File photo = new File(getOutputDirectory(context.getExternalMediaDirs(), context),
                new SimpleDateFormat(Constants.FILENAME_FORMAT, Locale.FRANCE).format(System.currentTimeMillis())+".jpg");

        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(photo).build();

        //Action de la capture de la photo
        _imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(context), new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Uri savedUri = Uri.fromFile(photo);
                        Log.d("TAG", "onImageSaved: " + savedUri.getPath());
                        captureListener.onCapture(savedUri.getPath());
                    }
                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        exception.printStackTrace();
                    }
                }
        );
    }


    private File getOutputDirectory(File[] externalMediaDir, Context context) {
        File mediaDir = null;
        if (externalMediaDir != null && externalMediaDir.length > 0)
            mediaDir = externalMediaDir[0];

        if (mediaDir != null) {
            mediaDir = new File(mediaDir, "Reso3D");
            mediaDir.mkdirs();
        }

        if (mediaDir != null && mediaDir.exists())
            return mediaDir;
            //else return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        else {
            //context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            //mediaDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), context.getResources().getString(R.string.app_title));
            mediaDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Reso3D");
            mediaDir.mkdirs();
            if (mediaDir.exists())
                return mediaDir;
            else return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }
    }



}
