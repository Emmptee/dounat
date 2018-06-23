package com.donut.app.mvp.shakestar.video.record.preview;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.databinding.ActivityRecordPreviewLayoutBinding;
import com.donut.app.entity.UploadInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.shakestar.DonutVideoView;
import com.donut.app.mvp.shakestar.ffmpegdemo.MediaInfo;
import com.donut.app.mvp.shakestar.video.DonutCameraVideoView;
import com.donut.app.mvp.shakestar.video.camera.util.FileUtil;
import com.donut.app.mvp.shakestar.video.camera.util.ScreenUtils;
import com.donut.app.service.UploadService;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.PictureUtil;
import com.donut.app.utils.status_bar.StatusBarCompat;
import com.lidroid.xutils.exception.DbException;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.socks.library.KLog;

import java.io.File;
import java.io.FileInputStream;
import java.util.Timer;
import java.util.TimerTask;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class RecordPreviewActivity extends MVPBaseActivity<ActivityRecordPreviewLayoutBinding, ShakeStarPreviewPresenter>
        implements ShakeStarPreviewContract.View {
    private static final String TAG = "REcordPreview";
    public static int REQUEST_FOR_PREVIEW_FINISH = 101;

    private DonutCameraVideoView mPreviewVideoview;
    private RelativeLayout mPreviewVideoLayout;
    private Button mPreviewBtnPublish;
    private EditText mVideoPreviewSearchEt;
    CharSequence descriptionCharSequence;
    private String videoFilePath = FileUtil.choseSavePath() + File.separator + "dest.mp4";
    //    MediaInfo joinVideoInfo = new MediaInfo(videoFilePath);
    private String g03;
    private String b02;
    private Intent intent;


    public static RecordPreviewActivity instance;
    private String isUpload;
    private long maxFileSize;
    private DonutCameraVideo mVideoviewPre;
    private String des = null;

    public RecordPreviewActivity() {

    }

    public static RecordPreviewActivity getInstance() {
        if (instance == null) {
            synchronized (RecordPreviewActivity.class) {
                if (instance == null) {
                    instance = new RecordPreviewActivity();
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mPopupWindow = new ChooseVideoPopupWindow(this, this);
        intent = getIntent();
        g03 = intent.getStringExtra("g03");
        b02 = intent.getStringExtra("b02");
        isUpload = intent.getStringExtra("isUpload");
        KLog.v("ISUPLOADING" + isUpload);
        KLog.v("g03和b02" + g03 + "====" + b02);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record_preview_layout;
    }

    @Override
    protected void initView() {
        //返回
        mViewBinding.videoPreviewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //合成视频播放
        mPreviewVideoLayout = mViewBinding.previewVideoLayout;
        ViewGroup.LayoutParams params = mPreviewVideoLayout.getLayoutParams();
        params.width = ScreenUtils.getScreenWidth(this);
        params.height = ScreenUtils.getScreenWidth(this);
        mPreviewVideoLayout.setLayoutParams(params);
        mVideoviewPre = mViewBinding.videoviewPre;
        mVideoviewPre.setUp(videoFilePath, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, " ");
//        FileUtil.savePicture(FileUtil.choseSavePath(), joinVideoInfo.getCoverBitmap());
        //设置封面
        File file = new File(FileUtil.choseSavePath() + File.separator + "myThumbnail.jpg");
        if (file.exists()) {
            KLog.v("加载封面" + FileUtil.choseSavePath() + File.separator + "myThumbnail.jpg");
            ImageView imageView = new ImageView(getContext());
            /*Glide.with(this)
                    .load(FileUtil.choseSavePath() + File.separator + "myThumbnail.jpg")
                    .centerCrop()
                    .into(mViewBinding.videoviewPre.thumbImageView);*/
            mViewBinding.videoviewPre.thumbImageView.setImageBitmap(BitmapFactory.decodeFile(
                    FileUtil.choseSavePath() + File.separator + "myThumbnail.jpg"));
        }

        //编辑框
        mVideoPreviewSearchEt = mViewBinding.videoPreviewSearchEt;
        //发布
        mPreviewBtnPublish = mViewBinding.previewBtnPublish;

        mPreviewBtnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.v("点击发布");
                mPresenter.uploadVideo(videoFilePath, true);
                showToast("请稍候片刻,大片正在上传中!");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        saveData();
                    }
                }, 5000);//5秒后执行保存数据
            }
        });

        mVideoPreviewSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                descriptionCharSequence = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int editStart = mVideoPreviewSearchEt.getSelectionStart();
                int editEnd = mVideoPreviewSearchEt.getSelectionEnd();
                if (descriptionCharSequence.length() > 20) {
                    showToast("描述限制20字以内");
                    s.delete(editStart - 1, editEnd);
                    mVideoPreviewSearchEt.setText(s);
                    if (editStart > 20) {
                        editStart = 20 + 1;
                    }
                    mVideoPreviewSearchEt.setSelection(editStart - 1);
                }
            }
        });


    }

    public void saveData() {
        des = mVideoPreviewSearchEt.getText().toString().trim();
        KLog.v("G03ID是" + g03);
        KLog.v(TAG, "描述是" + des);
        KLog.v(TAG, "视频地址是" + mPresenter.playUrl);
        KLog.v(TAG, "缩略图" + mPresenter.videoThumbnail);
        KLog.v("视频时长是" + (int) mPresenter.lastTime);
        if (TextUtils.isEmpty(des)) {
            showToast("请填写描述");
            return;
        }
        if (mPresenter.mUploadArray.size() > 0) {
            showToast("视频正在上传,请稍后");
            return;
        }


        if (TextUtils.isEmpty(mPresenter.playUrl)) {
            showToast("抱歉，视频上传失败，请重新上传");
            return;
        }
        Log.e(TAG, "开始请求");
        ShakeStarPreviewRequest request = new ShakeStarPreviewRequest();
        request.setG03Id(g03);
        request.setContentDesc(des);
        request.setContentType(12);
        request.setLastTime(mPresenter.lastTime);
        request.setVideoThumbnail(mPresenter.videoThumbnail);
        request.setPlayUrl(mPresenter.playUrl);
        mPresenter.saveBehaviour("01", request, HeaderRequest.SHAKESTAR_PREVIEW);
        /*try {
            UploadService.getUploadManager().addNewUpload(
                    videoFilePath,
                    getUserInfo().getUserId(),
                    getUserInfo().getToken(),
                    UploadInfo.SaveTypeEnum.SHAKE_STAR_PREVIEW,
                    "",
                    JsonUtils.toJson(request, request.getClass()));
        } catch (DbException e) {
            e.printStackTrace();
        }*/
        showToast("视频过大可能导致压缩时间较长哦，请耐心等候！");
        mPresenter.saveData(request);
    }

    @Override
    protected void loadData() {
        StatusBarCompat.translucentStatusBar(this);

    }

    @Override
    public void showUploadingProgress(int progress) {

    }

    @Override
    public void dismissUploadingProgress() {
    }

    @Override
    public void finishView() {
        JZVideoPlayer.releaseAllVideos();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtil.deleteFile(FileUtil.choseSavePath() + "/myThumbnail.jpg");
        JZVideoPlayer.releaseAllVideos();
    }
}
