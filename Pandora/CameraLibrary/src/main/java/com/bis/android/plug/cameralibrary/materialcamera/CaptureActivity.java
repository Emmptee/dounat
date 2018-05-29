package com.bis.android.plug.cameralibrary.materialcamera;

import android.app.Fragment;
import android.support.annotation.NonNull;

import com.bis.android.plug.cameralibrary.materialcamera.internal.BaseCaptureActivity;
import com.bis.android.plug.cameralibrary.materialcamera.internal.CameraFragment;


public class CaptureActivity extends BaseCaptureActivity {

    @Override
    @NonNull
    public Fragment getFragment() {
        return CameraFragment.newInstance();
    }
}