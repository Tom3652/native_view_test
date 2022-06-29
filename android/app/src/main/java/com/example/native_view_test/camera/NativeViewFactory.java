package com.example.native_view_test.camera;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;
import java.util.Map;

public class NativeViewFactory extends PlatformViewFactory {


    private final View view;

    public NativeViewFactory(View view) {
        super(StandardMessageCodec.INSTANCE);
        this.view = view;
    }

    @NonNull
    @Override
    public PlatformView create(@NonNull Context context, int id, @Nullable Object args) {
        final Map<String, Object> creationParams = (Map<String, Object>) args;
        return new NativeView(context, id, creationParams,view);
    }
}