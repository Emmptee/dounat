package com.donut.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.donut.app.config.Constant;
import com.donut.app.config.DonutPlugin;
import com.donut.app.utils.StatusBarUtil;

import org.apache.cordova.CordovaActivity;

public class H5WebActivity extends CordovaActivity {

    public static final String URL = "URL";

    protected  GestureDetector mGestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        String url = getIntent().getStringExtra(URL);
        if (url == null || "".equals(url)) {
            url = "file:///android_asset/www/more.html";
        }
        super.loadUrl(url);
        //appView.showWebPage(url, true, true, map);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev!=null&&mGestureDetector!=null){
            mGestureDetector.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case DonutPlugin.LOGIN_REQUEST_CODE:
            case DonutPlugin.VIP_REQUEST_CODE:
                this.appView.getPluginManager().getPlugin("DonutPlugin")
                        .onActivityResult(requestCode, resultCode, intent);
                break;
        }
    }
}
