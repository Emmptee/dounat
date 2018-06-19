package com.donut.app.mvp.shakestar.video.record.preview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.donut.app.R;
import com.socks.library.KLog;

import cn.jzvd.JZVideoPlayerStandard;

public class DonutCameraVideo extends JZVideoPlayerStandard {
    public DonutCameraVideo(Context context) {
        super(context);
    }

    public DonutCameraVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        startButton = findViewById(cn.jzvd.R.id.start);
        fullscreenButton = findViewById(cn.jzvd.R.id.fullscreen);
        bottomContainer = findViewById(cn.jzvd.R.id.layout_bottom);
        ImageView startButtonView = startButton;
        ImageView fullScreenButtonView = fullscreenButton;
        startButtonView.setImageResource(R.drawable.start);
        fullScreenButtonView.setVisibility(INVISIBLE);
        bottomContainer.setVisibility(INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == cn.jzvd.R.id.fullscreen) {
            if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                KLog.e("点击全品");
                //click quit fullscreen
            } else {
                KLog.e("没有点击");
                //click goto fullscreen
            }
        }
    }

    @Override
    public int getLayoutId() {
        return cn.jzvd.R.layout.jz_layout_standard;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return super.onTouch(v, event);
    }

    @Override
    public void startVideo() {
        super.startVideo();
    }

    @Override
    public void onStateNormal() {
        super.onStateNormal();
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
    }

    @Override
    public void onStateError() {
        super.onStateError();
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
    }

    @Override
    public void onInfo(int what, int extra) {
        super.onInfo(what, extra);
    }

    @Override
    public void onError(int what, int extra) {
        super.onError(what, extra);
    }

    @Override
    public void startWindowFullscreen() {
        super.startWindowFullscreen();
    }

    @Override
    public void startWindowTiny() {
        super.startWindowTiny();
    }

}
