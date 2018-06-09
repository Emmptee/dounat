package com.donut.app.mvp.shakestar.ffmpegdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import codepig.ffmpegcldemo.FFmpegKit;


public class FFmpegService extends Service {
    private static final String TAG = "FFmpegService";

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends android.os.Binder {
         public FFmpegService getService() {
            return FFmpegService.this;
         }
     }

    String sourceMp4="";
    String recordMp4="";
    String[] commands;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        FFmpegKit.init(this);
        Log.i("66666666666666666", "--------->onStartCommand: ");
        sourceMp4 = intent.getStringExtra("source");
        recordMp4 = intent.getStringExtra("record");
        ArrayList<String> param = intent.getStringArrayListExtra("param");
        commands = new String[param.size()];
        param.toArray(commands);
        new Thread(compoundRun).start();
        return START_NOT_STICKY;
    }

    public Callback callback;

    public interface Callback {
        void onProgress(int frame);
        void onEnd();
    }

    public void setCallBack(Callback call){
        callback = call;
    }

    Runnable compoundRun=new Runnable() {
        @Override
        public void run() {
            new File("/mnt/sdcard/DCIM/mchengdu.mp4").delete();


            FFmpegKit.execute(commands);
        }
    };
}
