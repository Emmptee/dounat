package com.donut.app.mvp.shakestar.video.record;

import android.media.MediaRecorder;
import android.view.View;
import android.view.ViewGroup;

import com.donut.app.R;
import com.donut.app.databinding.ActivityRecordBinding;
import com.donut.app.http.message.shakestar.CommendAllResponse;
import com.donut.app.http.message.shakestar.ParticularsResponse;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.shakestar.select.particulars.ParticularsContract;
import com.donut.app.mvp.shakestar.select.particulars.ParticularsPresenter;
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
        mViewBinding.btnNext.setEnabled(false);
//        mViewBinding.btnNext.setBackgroundResource(R.drawable.shape_half_rec_main);

        mViewBinding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.e("点击了下一步");
                RotateOutputVideo();
            }
        });

        //右侧视频播放
        ViewGroup.LayoutParams frameparams = mViewBinding.recordVideoRight.getLayoutParams();
        frameparams.width = ScreenUtils.getScreenWidth(this) / 2;
        frameparams.height = ScreenUtils.getScreenWidth(this);
        mViewBinding.recordVideoRight.setLayoutParams(frameparams);
//        mViewBinding.recordPlayerRight.setUp("mnt/sdcard/ffmpeg/anyixuan.mp4",false,null);

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
