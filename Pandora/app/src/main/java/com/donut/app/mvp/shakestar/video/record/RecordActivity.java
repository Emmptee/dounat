package com.donut.app.mvp.shakestar.video.record;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.databinding.ActivityRecordBinding;
import com.donut.app.http.message.shakestar.CommendAllResponse;
import com.donut.app.http.message.shakestar.ParticularsResponse;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.shakestar.select.particulars.ParticularsContract;
import com.donut.app.mvp.shakestar.select.particulars.ParticularsPresenter;
import com.donut.app.mvp.shakestar.video.constant.CameraContants;
import com.donut.app.mvp.shakestar.video.camera.CameraInterface;
import com.donut.app.mvp.shakestar.video.camera.util.FileUtil;
import com.donut.app.mvp.shakestar.video.camera.util.ScreenUtils;
import com.socks.library.KLog;

import java.io.File;
import java.util.List;

import VideoHandle.EpEditor;
import VideoHandle.EpVideo;
import VideoHandle.OnEditorListener;


public class RecordActivity extends MVPBaseActivity<ActivityRecordBinding,ParticularsPresenter> implements ParticularsContract.View{

    private static final String TAG = "RecordActivity";
    public static int OUTPUTROTATEFILE = 0;
    public static int VIDEORECORD = 0;
    public static TextView btnNext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* IntentFilter screenFilter = new IntentFilter();
        screenFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(, screenFilter);*/
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record;
    }

    @Override
    protected void initView() {
        //此界面只是引入自定义view，具体的监听由view来处理
        //返回
        mViewBinding.imgCameraBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //下一步
        btnNext = mViewBinding.btnNext;
        btnNext.setEnabled(false);

        btnNext.setOnClickListener(new View.OnClickListener() {
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
        mViewBinding.recordVideoRight.setLayoutParams(frameparams);
        mViewBinding.recordPlayerRight.setUp("mnt/sdcard/ffmpeg/anyixuan.mp4",false,null);

        //美颜
        mViewBinding.imageBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.e("在AC中点击了美颜");
            }
        });

        //背景音乐
        mViewBinding.imageBgMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.e("在AC中点击了背景音乐");

            }
        });
    }

    /**
     * 控制下一步按钮的广播
     */
    public static class RecordEndReceiver extends BroadcastReceiver{
        public RecordEndReceiver(){

        }
        @Override
        public void onReceive(Context context, Intent intent) {
            if (CameraContants.ACTION_DONUT_RECORD_END.equals(intent.getAction())){
                btnNext.setBackgroundResource(R.drawable.shape_half_rec_main);
                btnNext.setEnabled(true);
                KLog.e("收到改变按钮状态的广播");
            }

        }
    }

    private MediaRecorder mediaRecorder;

    private void stopPlayLeftVideo(){

//        player.stop();

        if (mediaRecorder == null){
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

    }


    @Override
    protected void onStart() {
        super.onStart();

        //全屏显示
//        if (Build.VERSION.SDK_INT >= 19) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        } else {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
//            decorView.setSystemUiVisibility(option);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        jCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        jCameraView.onPause();
    }

    @Override
    public void showView(List<ParticularsResponse.ShakingStarListBean> starCommendResponses, ParticularsResponse particularsResponse) {

    }

    @Override
    public void showCommend(List<CommendAllResponse.CommentsListBean> commendAllResponse, CommendAllResponse Response) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void RotateOutputVideo(){
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
}
