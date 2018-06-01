package com.donut.app.mvp.shakestar.video.record;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.databinding.ActivityRecordBinding;
import com.donut.app.http.message.shakestar.CommendAllResponse;
import com.donut.app.http.message.shakestar.ParticularsResponse;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.shakestar.select.particulars.ParticularsContract;
import com.donut.app.mvp.shakestar.select.particulars.ParticularsPresenter;
import com.donut.app.mvp.shakestar.video.DonutCameraVideoView;
import com.donut.app.mvp.shakestar.video.camera.CameraInterface;
import com.donut.app.mvp.shakestar.video.camera.JCameraView;
import com.donut.app.mvp.shakestar.video.camera.util.FileUtil;
import com.donut.app.mvp.shakestar.video.camera.util.ScreenUtils;
import com.donut.app.mvp.shakestar.video.constant.CameraContants;
import com.donut.app.utils.status_bar.StatusBarCompat;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.socks.library.KLog;

import java.io.File;
import java.util.List;

import VideoHandle.EpEditor;
import VideoHandle.EpVideo;
import VideoHandle.OnEditorListener;


public class RecordActivity extends MVPBaseActivity<ActivityRecordBinding, ParticularsPresenter> implements ParticularsContract.View {

    private static final String TAG = "RecordActivity";
    public static int OUTPUTROTATEFILE = 0;
    public static int VIDEORECORD = 0;
    private static TextView mBtnNext;
    private RelativeLayout mRecordVideoRight;
    private static DonutCameraVideoView mRecordPlayerRight;
    private JCameraView mJCameraView;
    public static int mUrlVideoDuration;

    /**
     * 开始录制的广播:视频开始播放
     */
    private BroadcastReceiver mRecordStartReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (CameraContants.ACTION_DONUT_RECORD_START.equals(intent.getAction())) {
                KLog.e("收到开始录制的广播");
                mRecordPlayerRight.getStartButton().performClick();
            }
        }
    };

    /**
     * 录制时间过短
     */
    private BroadcastReceiver mRecordShortReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (CameraContants.ACTION_DONUT_RECORD_SHORT.equals(intent.getAction())) {
                KLog.e("收到录制过短广播");
                mRecordPlayerRight.onVideoReset();
            }
        }
    };

    /**
     * 录制结束后的广播:下一步按钮、
     */
    private BroadcastReceiver mRecordEndReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (CameraContants.ACTION_DONUT_RECORD_END.equals(intent.getAction())) {
                mBtnNext.setBackgroundResource(R.drawable.shape_half_rec_main);
                mBtnNext.setEnabled(true);
                KLog.e("收到录像结束的广播");
            }
        }
    };
    private int recordTime;
    private Intent intent;
    private String g03 ;
    private String b02;
    private  int STATUSA=0;//未收藏
    private static final int STATUSB=1;//已收藏
    private int page = 0, rows = 10, sortType = 0;
    private int status;
    private ParticularsResponse shakingStarListBeans;
    List<ParticularsResponse.ShakingStarListBean> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerRecordReceiver();

        /*MediaPlayer meidaPlayer = new MediaPlayer();
        try {
            meidaPlayer.setDataSource("mnt/sdcard/ffmpeg/anyixuan.mp4");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            meidaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

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
                KLog.e("点击了下一步");
                //执行视频合成压缩
                RotateOutputVideo();
            }
        });

        //右侧视频播放
        ViewGroup.LayoutParams frameparams = mViewBinding.recordVideoRight.getLayoutParams();
        frameparams.width = ScreenUtils.getScreenWidth(this) / 2;
        frameparams.height = ScreenUtils.getScreenWidth(this);
        mRecordVideoRight.setLayoutParams(frameparams);
//        mRecordPlayerRight.setUp("mnt/sdcard/ffmpeg/anyixuan.mp4", false, null);

        mUrlVideoDuration = recordTime;
        KLog.e("视频时长是:---------" + mUrlVideoDuration);

        //美颜
        mViewBinding.imageBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.e("点击了美颜");
            }
        });

        //背景音乐
        mViewBinding.imageBgMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.e("点击了背景音乐");

            }
        });
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

    @Override
    protected void loadData() {
        StatusBarCompat.translucentStatusBar(this);
        intent = getIntent();
        g03 = intent.getStringExtra("g03");
        b02 = intent.getStringExtra("b02");
        mPresenter.loadData(true, b02, g03,page,rows);
    }

    @Override
    public void showView(List<ParticularsResponse.ShakingStarListBean> list, ParticularsResponse shakingStarListBeans) {
        KLog.e("展示showView");
        mRecordPlayerRight.setUp( shakingStarListBeans.getMaterialVideoList().get(0).getPlayUrl(),false,null);
        /*MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(shakingStarListBeans.getMaterialVideoList().get(0).getPlayUrl());
        int duration = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        recordTime = duration;*/
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
    }



    public void RotateOutputVideo() {
        EpVideo epVideo = new EpVideo(CameraInterface.videoFileAbsPath);
        epVideo.rotation(0, true);
        final String outPath = FileUtil.choseSavePath() + File.separator + "pandoraoutvideo.mp4";
        EpEditor.exec(epVideo, new EpEditor.OutputOption(outPath), new OnEditorListener() {
            @Override
            public void onSuccess() {
                KLog.e("输出成功");
                OUTPUTROTATEFILE = 1;
            }

            @Override
            public void onFailure() {
                KLog.e("输出失败");
            }

            @Override
            public void onProgress(float v) {
                KLog.e("输出中");
            }
        });
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
    /**
     * 控制下一步按钮的广播
     */
    public static class RecordEndReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {


        }
    }


}
