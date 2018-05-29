package com.donut.app.mvp.shakestar.video.camera.listener;

/**
 * 按钮布局监听
 */

public interface CaptureListener {
    void takePictures();

    void recordShort(long time);

    void recordStart();

    void recordEnd(long time);

    void recordZoom(float zoom);

    void recordError();
}
