package com.donut.app.mvp.shakestar.video.record;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.databinding.ActivityRecordBinding;
import com.donut.app.http.message.shakestar.CommendAllResponse;
import com.donut.app.http.message.shakestar.ParticularsResponse;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.shakestar.ffmpegdemo.FFmpegFuncion;
import com.donut.app.mvp.shakestar.ffmpegdemo.FFmpegService;
import com.donut.app.mvp.shakestar.ffmpegdemo.MediaInfo;
import com.donut.app.mvp.shakestar.select.particulars.ParticularsContract;
import com.donut.app.mvp.shakestar.select.particulars.ParticularsEvent;
import com.donut.app.mvp.shakestar.select.particulars.ParticularsPresenter;
import com.donut.app.mvp.shakestar.video.DonutCameraVideoView;
import com.donut.app.mvp.shakestar.video.camera.JCameraView;
import com.donut.app.mvp.shakestar.video.camera.util.FileUtil;
import com.donut.app.mvp.shakestar.video.camera.util.ScreenUtils;
import com.donut.app.mvp.shakestar.video.constant.CameraContants;
import com.donut.app.mvp.shakestar.video.record.preview.RecordPreviewActivity;
import com.donut.app.utils.PictureUtil;
import com.donut.app.utils.status_bar.StatusBarCompat;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class RecordActivity extends MVPBaseActivity<ActivityRecordBinding, ParticularsPresenter> implements ParticularsContract.View {

    private static final String TAG = "RecordActivity";
    public static final int REQUEST_FOR_PREVIEW = 100;
    public static int OUTPUTROTATEFILE = 0;
    private TextView mBtnNext;
    private RelativeLayout mRecordVideoRight;
    private DonutCameraVideoView mRecordPlayerRight;
    private JCameraView mJCameraView;
    private Intent intent;
    private String g03 ;
    private String b02;
    private boolean isUpload;

    private int page = 0, rows = 10, sortType = 0;
    public static int VideoDuration;
    public String stringVideoDuration;
    public static int mDuration;

    //视频合成相关
    public enum FunctionState {
        VideoConcat, VideoJoin
    }
    FunctionState currentFuncionState = FunctionState.VideoJoin;
    NumberFormat numberFormat = NumberFormat.getPercentInstance();

    private ArrayList<String>[] FFmpegTasklist;
    private int FFmpegTaskIndex=0;

    MediaInfo sourceInfo;
    MediaInfo recordInfo;
    String tempFile1 = "/mnt/sdcard/ffmpeg/tmp1.mp4";
    String tempFile2 = "/mnt/sdcard/ffmpeg/tmp2.mp4";
    String destFile = FileUtil.choseSavePath() + File.separator + "dest.mp4";
    String concatFile = "/mnt/sdcard/ffmpeg/concat.txt";

    int sourceFrames = -1;
    int recordFrames = -1;
    int temp1Frames = -1;
    int temp2Frames = -1;

    /**
     * 接收开始录制的广播:视频开始播放
     */
    private BroadcastReceiver mRecordStartReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (CameraContants.ACTION_DONUT_RECORD_START.equals(intent.getAction())) {
                KLog.v("收到开始录制的广播");
                mRecordPlayerRight.getStartButton().performClick();
            }
        }
    };

    /**
     * 接收录制时间过短的广播
     */
    private BroadcastReceiver mRecordShortReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (CameraContants.ACTION_DONUT_RECORD_SHORT.equals(intent.getAction())) {
                KLog.v("收到录制过短广播");
                mRecordPlayerRight.onVideoReset();
            }
        }
    };

    /**
     * 接收录制结束后的广播:下一步按钮
     */
    private BroadcastReceiver mRecordEndReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (CameraContants.ACTION_DONUT_RECORD_END.equals(intent.getAction())) {
                mBtnNext.setBackgroundResource(R.drawable.shape_half_rec_main);
                mBtnNext.setEnabled(true);
                KLog.v("收到录像结束的广播");
            }
        }
    };

    private BroadcastReceiver mFFmpegReceiver = new FFmpegReceiver();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerRecordReceiver();
        initViewJoinVideo();
        initFFmpegReceiver();
        KLog.v("主界面");
        numberFormat.setMinimumFractionDigits(0);
        EventBus.getDefault().register(this);//素材视频的时长
    }
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEvent(ParticularsEvent event) {
        String MSG = event.getLastTime();
        KLog.v("eventbus 中的时间长度++++" + MSG);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_record;
    }

    @Override
    protected void initView() {

        //此界面中把和相机相关的控件都集中在JCameraView中
        mJCameraView = mViewBinding.jcameraview;
        mBtnNext = mViewBinding.btnNext;
        mRecordVideoRight = mViewBinding.recordVideoRight;
        mRecordPlayerRight = mViewBinding.recordPlayerRight;
        mRecordPlayerRight.setUp(FileUtil.choseSavePath() + File.separator + "download.mp4",false,null);

        //返回
        mViewBinding.imgCameraBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //下一步
        mBtnNext.setEnabled(false);
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //执行视频合成压缩
                joinVideo();
            }
        });

        //右侧视频播放
        ViewGroup.LayoutParams frameparams = mViewBinding.recordVideoRight.getLayoutParams();
        frameparams.width = ScreenUtils.getScreenWidth(this) / 2;
        frameparams.height = ScreenUtils.getScreenWidth(this);
        mRecordVideoRight.setLayoutParams(frameparams);
//        mRecordPlayerRight.setUp("mnt/sdcard/ffmpeg/anyixuan.mp4", false, null);

        //美颜
        mViewBinding.imageBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                joinVideo();
                showToast("精彩内容,敬请期待!");
            }
        });

        //背景音乐
        mViewBinding.imageBgMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* KLog.v("点击了背景音乐");
                Intent it=new Intent(getApplicationContext(), RecordPreviewActivity.class);
                it.putExtra("g03",g03);
                it.putExtra("b02",b02);
                it.putExtra("isUpload","upload");
                startActivityForResult(it, REQUEST_FOR_PREVIEW);*/
                showToast("精彩内容,敬请期待!");
            }
        });

    }


    @Override
    protected void loadData() {
        StatusBarCompat.translucentStatusBar(this);
        intent = getIntent();
        g03 = intent.getStringExtra("g03");
        b02 = intent.getStringExtra("b02");
        stringVideoDuration = intent.getStringExtra("mylasttime");
        KLog.v("loaddata" + stringVideoDuration);
//        VideoDuration = Integer.parseInt(stringVideoDuration);
        mPresenter.loadData(true, b02, g03,page,rows);
        KLog.v("loadData" + VideoDuration);
    }

    @Override
    public void showView(List<ParticularsResponse.ShakingStarListBean> list, ParticularsResponse shakingStarListBeans) {
        KLog.v("showView");
        ImageView imageView = new ImageView(getContext());
        Glide.with(this).load(shakingStarListBeans.getMaterialVideoList().get(0).getThumbnailUrl()).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mRecordPlayerRight.setThumbImageView(imageView);
//        mRecordPlayerRight.setUp(shakingStarListBeans.getMaterialVideoList().get(0).getPlayUrl(),false,null);
//        VideoDuration = Integer.parseInt(getLastTime(shakingStarListBeans.getMaterialVideoList().get(0).getPlayUrl()));
    }

    @Override
    public void showCommend(List<CommendAllResponse.CommentsListBean> commendAllResponse, CommendAllResponse Response) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mJCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
        mJCameraView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterRecordReceiver();
        unregisterReceiver(mFFmpegReceiver);
        EventBus.getDefault().unregister(this);
    }


    private void registerRecordReceiver() {
        IntentFilter startFilter = new IntentFilter();
        startFilter.addAction(CameraContants.ACTION_DONUT_RECORD_START);
        startFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mRecordStartReceiver, startFilter);

        IntentFilter shortFilter = new IntentFilter();
        shortFilter.addAction(CameraContants.ACTION_DONUT_RECORD_SHORT);
        shortFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mRecordShortReceiver, shortFilter);

        IntentFilter endFilter = new IntentFilter();
        endFilter.addAction(CameraContants.ACTION_DONUT_RECORD_END);
        endFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mRecordEndReceiver, endFilter);
    }

    private void unRegisterRecordReceiver(){
        unregisterReceiver(mRecordStartReceiver);
        unregisterReceiver(mRecordShortReceiver);
        unregisterReceiver(mRecordEndReceiver);

    }

    private MediaRecorder mediaRecorder;

    private void stopPlayLeftVideo() {

//        player.stop();

        if (mediaRecorder == null) {
            return;
        }
        try {
            mediaRecorder.stop();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        mediaRecorder.reset();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    void initViewJoinVideo(){
        currentFuncionState = FunctionState.VideoJoin;
//        recordInfo = new MediaInfo(FileUtil.choseSavePath() + File.separator + "pandoravideo.mp4");
        sourceInfo = new MediaInfo(FileUtil.choseSavePath() + File.separator + "download.mp4");
        Intent intent = new Intent(this, FFmpegService.class);
        intent.putStringArrayListExtra("param", FFmpegFuncion.getMediaInfo(sourceInfo.getFilePath()));
        startService(intent);
    }
    /**
     * 执行合成操作(同屏合成)
     */
    //合成按钮
    public void joinVideo() {
//        initViewJoinVideo();
        recordInfo = new MediaInfo(FileUtil.choseSavePath() + File.separator + "pandoravideo.mp4");
//        sourceInfo = new MediaInfo(FileUtil.choseSavePath() + File.separator + "download.mp4");
        Intent intent = new Intent(this, FFmpegService.class);
        KLog.v("启动Service");
        FFmpegTaskCreate(currentFuncionState);
        FFmpegTaskIndex = 0;
        intent.putStringArrayListExtra("param", FFmpegTasklist[FFmpegTaskIndex++]);
        startService(intent);
        showProgressDialog();
    }
    void initFFmpegReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.myaya.ffmpegbroadcast");
        registerReceiver(mFFmpegReceiver, intentFilter);
    }
    ProgressDialog mProgressDialog;

    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        KLog.v("正在处理中");
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
                    FFmpegTasklist[1] = FFmpegFuncion.joinVideo(tempFile1,sourceInfo.getFilePath(),destFile);
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
            saveThumbnail(FileUtil.choseSavePath() + File.separator + "dest.mp4");
            calcelProgressDialog();

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Intent preIntent =new Intent(getContext(),RecordPreviewActivity.class);
                    preIntent.putExtra("g03",g03);
                    preIntent.putExtra("b02",b02);
                    preIntent.putExtra("isUpload","upload");
                    startActivityForResult(preIntent, REQUEST_FOR_PREVIEW);
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 300);
            return;
        }

        final Intent intent = new Intent(this, FFmpegService.class);
        intent.putStringArrayListExtra("param", FFmpegTasklist[FFmpegTaskIndex++]);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                startService(intent);
            }
        }, 300);
    }
    public static void saveThumbnail(String filePath){
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(
                filePath, MediaStore.Video.Thumbnails.MINI_KIND);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth(), bitmap.getHeight(),
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        File file = new File(FileUtil.choseSavePath(),
                "myThumbnail.jpg");
        if (bitmap != null) {
            PictureUtil.compressBmpToFile(bitmap, file);
        }
        KLog.v("保存封面======");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== REQUEST_FOR_PREVIEW){
            if (resultCode == RESULT_OK){
                KLog.v("返回了");
                setResult(RESULT_OK);
                this.finish();
            }
        }
    }
}
