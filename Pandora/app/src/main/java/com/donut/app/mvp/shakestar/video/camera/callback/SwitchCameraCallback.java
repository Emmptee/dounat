package com.donut.app.mvp.shakestar.video.camera.callback;


/**
 * 切换摄像头回调
 *
 */
public interface SwitchCameraCallback
{
    /**
     * 开始切换摄像头之前回调
     */
    public void startSwitch();

    /**
     * 完成切换之后回调
     */
    public void finishSwitch();

    /**
     * 失败的回调
     */
    public void failSwitch();
}
