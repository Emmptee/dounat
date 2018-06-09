package com.donut.app.mvp.shakestar.ffmpegdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.donut.app.R;
import com.donut.app.mvp.shakestar.video.camera.util.FileUtil;
import com.socks.library.KLog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class TestActivity extends Activity {

    public static final String TAG = "YY-FFmpeg";

    public enum FunctionState {
        VideoConcat, VideoJoin
    }
    FunctionState currentFuncionState = FunctionState.VideoJoin;
    NumberFormat numberFormat = NumberFormat.getPercentInstance();
    private ImageView coverView;
    private Bitmap coverBitmap;
    private ArrayList<String>[] FFmpegTasklist;
    private int FFmpegTaskIndex=0;

    MediaInfo sourceInfo;
    MediaInfo recordInfo = new MediaInfo(FileUtil.choseSavePath() + File.separator + "pandoravideo.mp4");
    String tempFile1 = "/mnt/sdcard/ffmpeg/tmp1.mp4";
    String tempFile2 = "/mnt/sdcard/ffmpeg/tmp2.mp4";
    String destFile = FileUtil.choseSavePath() + File.separator + "dest.mp4";
    String concatFile = "/mnt/sdcard/ffmpeg/concat.txt";

    int sourceFrames = -1;
    int recordFrames = -1;
    int temp1Frames = -1;
    int temp2Frames = -1;

    ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initFFmpegReceiver();
        initViewJoinVideo();
        KLog.e("主界面");
        numberFormat.setMinimumFractionDigits(0);
    }

    void initViewJoinVideo(){
        currentFuncionState = FunctionState.VideoJoin;
        sourceInfo = new MediaInfo(FileUtil.choseSavePath() + File.separator + "joinsrc.mp4");

        Intent intent = new Intent(this, FFmpegService.class);
        intent.putStringArrayListExtra("param", FFmpegFuncion.getMediaInfo(sourceInfo.getFilePath()));
        startService(intent);
    }

    /**
     * @param v
     * 执行合成操作(同屏合成)
     */
    //合成按钮
    public void compose(View v) {
        Intent intent = new Intent(this, FFmpegService.class);
        KLog.e("启动Service");
        FFmpegTaskCreate(currentFuncionState);
        FFmpegTaskIndex = 0;
        KLog.e();
        intent.putStringArrayListExtra("param", FFmpegTasklist[FFmpegTaskIndex++]);
        startService(intent);
        showProgressDialog();
    }

    void initFFmpegReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.myaya.ffmpegbroadcast");
        registerReceiver(new FFmpegReceiver(), intentFilter);
    }

    public class FFmpegReceiver extends BroadcastReceiver {
        /**
         * @param context
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            String fun = intent.getStringExtra("fun");
            if("onProgress".equals(fun)){
                int frame = intent.getIntExtra("frame", -1);
                Log.d(TAG, "onProgress " + frame);
                FFmpegProgress(frame);
            }else if("onEnd".equals(fun)){
                Log.d(TAG,"onEnd");
                FFmpegEnd();
            }else if("notifyMedia".equals(fun)){
                String fileName = intent.getStringExtra("fileName");
                double fps = intent.getDoubleExtra("fps", -1);
                int duration = intent.getIntExtra("duration", -1);
                Log.d(TAG, "notifyMedia " + fileName + " " + fps + " " + duration);
                FFmpegNotifyMedia(fileName, duration, fps);
            }
        }
    }


    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        KLog.e("正在处理中");
        mProgressDialog.setMessage("正在处理中...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }
    private void calcelProgressDialog() {
        if( mProgressDialog!=null){
            mProgressDialog.cancel();
            mProgressDialog=null;
        }
    }

/*    void setCoverImage(Bitmap bitmap){
        if(coverBitmap != null){
            coverBitmap.recycle();
            coverBitmap = null;
        }
        coverBitmap = bitmap;
        coverView.setBackground(new BitmapDrawable(bitmap));
    }*/


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(new FFmpegReceiver());
    }

    public void FFmpegMakeConcatFile(String[] list) {
        final File concat = new File(concatFile);
        try {
            FileWriter writer = new FileWriter(concat);
            for (String path : list) {
                writer.write("file " + path + "\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void FFmpegTaskCreate(FunctionState state){
        new File(destFile).delete();
        new File(tempFile1).delete();
        new File(tempFile2).delete();
//        new File(concatFile).delete();

        switch (state){
            //同屏拼接
            case VideoJoin:
                if(sourceInfo.getFps().equals(recordInfo.getFps()) &&
                        sourceInfo.getHeight().equals(recordInfo.getHeight()) &&
                        sourceInfo.getWidth().equals(recordInfo.getWidth()) ){  //如果参数一致，直接拼接
                    FFmpegTasklist = new ArrayList[1];
                    FFmpegTasklist[0] = FFmpegFuncion.joinVideo(sourceInfo.getFilePath(), recordInfo.getFilePath(), destFile);
                    Log.e(TAG,"参数一致直接拼接");
                }else{
                FFmpegTasklist = new ArrayList[2];
                FFmpegTasklist[0] = FFmpegFuncion.formatVideo(recordInfo.getFilePath(),
                        sourceInfo.getWidth(), sourceInfo.getHeight(), sourceInfo.getFps(), tempFile1);
                FFmpegTasklist[1] = FFmpegFuncion.joinVideo(sourceInfo.getFilePath(), tempFile1, destFile);
                Log.e(TAG,"参数不一致");
                }
                break;
            //前后拼接
            case VideoConcat:
                FFmpegTasklist = new ArrayList[3];
                FFmpegTasklist[0] = FFmpegFuncion.formatVideo(sourceInfo.getFilePath(), sourceInfo.getWidth(),
                        sourceInfo.getHeight(), sourceInfo.getFps(), tempFile1);
                FFmpegTasklist[1] = FFmpegFuncion.formatVideo(recordInfo.getFilePath(), sourceInfo.getWidth(),
                        sourceInfo.getHeight(), sourceInfo.getFps(), tempFile2);
                FFmpegMakeConcatFile(new String[]{tempFile1, tempFile2});
                FFmpegTasklist[2] = FFmpegFuncion.concatVideo(concatFile, destFile);
                break;
        }
    }

    public void FFmpegEnd(){
        if(null == FFmpegTasklist){
            return;
        }

        if(mProgressDialog!=null && FFmpegTasklist != null && FFmpegTaskIndex >= FFmpegTasklist.length) {
            FFmpegTasklist = null;
            mProgressDialog.setMessage("100%");
            calcelProgressDialog();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    finish();
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 2000);
            return;
        }

        final Intent intent = new Intent(this, FFmpegService.class);
        intent.putStringArrayListExtra("param", FFmpegTasklist[FFmpegTaskIndex++]);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                startService(intent);
            }
        }, 100);
    }

    public void FFmpegProgress(int frame){
        if(mProgressDialog!=null){
            switch (currentFuncionState) {
                case VideoJoin:
                    if (FFmpegTaskIndex == 1) {   //视频格式化1
                        mProgressDialog.setMessage("视频格式化..." + numberFormat.format((float) frame / recordFrames));
                    } else if (FFmpegTaskIndex == 2) {//视频合成中
                        mProgressDialog.setMessage("视频拼接中..." + numberFormat.format((float) frame / (temp1Frames > sourceFrames ? temp1Frames : sourceFrames)));
                    }
                    break;
            }
        }
    }

    public void FFmpegNotifyMedia(String filePath, int duration, double fps){
        int frams = (int)(fps*duration/1000000);
        if(null != recordInfo && filePath.equals(recordInfo.getFilePath())){
            recordInfo.setFps("" + fps);
            recordFrames = frams;
        }
        if(null != sourceInfo && filePath.equals(sourceInfo.getFilePath())){
            sourceInfo.setFps("" + fps);
            sourceFrames = frams;
        }
        if(tempFile1.equals(filePath)){
            temp1Frames = frams;
        }
        if(tempFile2.equals(filePath)){
            temp2Frames = frams;
        }
    }
}