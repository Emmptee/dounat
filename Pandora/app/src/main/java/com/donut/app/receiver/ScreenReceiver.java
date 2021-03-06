package com.donut.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class ScreenReceiver {

    private Context mContext;
    private ScreenBroadcastReceiver mScreenReceiver;
    private ScreenStateListener mScreenStateListener;

    class ScreenBroadcastReceiver extends BroadcastReceiver{
        private String action = null;
        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            if(Intent.ACTION_SCREEN_ON.equals(action)){
                mScreenStateListener.onScreenOn();
            }else if(Intent.ACTION_SCREEN_OFF.equals(action)){
                mScreenStateListener.onScreenOff();
            }
        }
    }

    public interface ScreenStateListener {
        void onScreenOn();
        void onScreenOff();
    }

    public ScreenReceiver(Context context) {
        mContext = context;
        mScreenReceiver = new ScreenBroadcastReceiver();
    }

    public void requestScreenStateUpdate(ScreenStateListener listener) {
        mScreenStateListener = listener;
        startScreenBroadcastReceiver();
    }

    private void startScreenBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mContext.registerReceiver(mScreenReceiver, filter);
    }

    public void unregisterScreenStateUpdate(){
        mContext.unregisterReceiver(mScreenReceiver);
    }


}
