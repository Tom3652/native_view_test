package com.example.native_view_test.camera;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Map;

import io.flutter.plugin.platform.PlatformView;

class NativeView implements PlatformView {
    private final View view;

    NativeView(@NonNull Context context, int id, @Nullable Map<String, Object> creationParams,View view) {
        this.view = view;
    }

    @NonNull
    @Override
    public View getView() {
        return view;
    }


    @Override
    public void dispose() {
    }


}