package com.donut.app.mvp.shakestar.video.record.preview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.activity.UploadManagerActivity;
import com.donut.app.databinding.ActivityRecordPreviewLayoutBinding;
import com.donut.app.entity.UploadInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.StarNoticeAddRequest;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.shakestar.ffmpegdemo.MediaInfo;
import com.donut.app.mvp.shakestar.video.DonutCameraVideoView;
import com.donut.app.mvp.shakestar.video.camera.util.FileUtil;
import com.donut.app.mvp.shakestar.video.camera.util.ScreenUtils;
import com.donut.app.mvp.shakestar.video.record.preview.ChooseVideoPopupWindow;
import com.donut.app.mvp.shakestar.video.record.preview.ShakeStarPreviewContract;
import com.donut.app.mvp.shakestar.video.record.preview.ShakeStarPreviewPresenter;
import com.donut.app.mvp.shakestar.video.record.preview.ShakeStarPreviewRequest;
import com.donut.app.mvp.wish.wishing.WishingPresenter;
import com.donut.app.service.UploadService;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.status_bar.StatusBarCompat;
import com.lidroid.xutils.exception.DbException;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.socks.library.KLog;

import java.io.File;

public class RecordPreviewActivity extends MVPBaseActivity<ActivityRecordPreviewLayoutBinding, ShakeStarPreviewPresenter>
        implements ShakeStarPreviewContract.View {
    private static final String TAG = "REcordPreview";

    private DonutCameraVideoView mPreviewVideoview;
    private RelativeLayout mPreviewVideoLayout;
    private Button mPreviewBtnPublish;
    private EditText mVideoPreviewSearchEt;
    CharSequence descriptionCharSequence;
    private String videoFilePath = FileUtil.choseSavePath() + File.separator + "dest.mp4";
    MediaInfo joinVideoInfo = new MediaInfo(videoFilePath);
    private Button previewBtnTest;
    private ChooseVideoPopupWindow mPopupWindow;
    private String g03;
    private String b02;
    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mPopupWindow = new ChooseVideoPopupWindow(this, this);
        intent = getIntent();
        g03 = intent.getStringExtra("g03");
        b02 = intent.getStringExtra("b02");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record_preview_layout;
    }

    @Override
    protected void initView() {
        GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);
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
        mViewBinding.videoviewPre.setUp(videoFilePath, false, null);
        FileUtil.savePicture(FileUtil.choseSavePath(), joinVideoInfo.getCoverBitmap());
        //设置封面
        ImageView imageView = new ImageView(getContext());
        Glide.with(this).load(FileUtil.choseSavePath()).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mViewBinding.videoviewPre.setThumbImageView(imageView);
        //编辑框
        mVideoPreviewSearchEt = mViewBinding.videoPreviewSearchEt;
        //发布
        mPreviewBtnPublish = mViewBinding.previewBtnPublish;
        previewBtnTest = mViewBinding.previewBtnTest;
        mPreviewBtnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
        previewBtnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.e("点击了测试");
                mPresenter.uploadVideo(videoFilePath,true);
//                mPopupWindow.show(mViewBinding.getRoot());
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

    private void saveData() {
        Log.e(TAG, "开始保存数据");
        String des = mVideoPreviewSearchEt.getText().toString().trim();
        Log.e(TAG, "描述是" + des);
        KLog.e(TAG, "视频地址是" + mPresenter.playUrl);
        KLog.e(TAG, "缩略图" + mPresenter.thumbnail);
        if (TextUtils.isEmpty(des)) {
            showToast("请填写描述");
            return;
        }
        if (mPresenter.mUploadArray.size() > 0) {
            showToast("视频正在上传,请稍后");
            KLog.e("数组长度" + mPresenter.mUploadArray.size());
//            return;
        }


        if (TextUtils.isEmpty(mPresenter.playUrl)) {
            showToast("抱歉，视频上传失败，请重新上传");
//            return;
        }
        Log.e(TAG, "开始请求");
        ShakeStarPreviewRequest request = new ShakeStarPreviewRequest();
        request.setG03Id(g03);
        request.setDescription(des);
        request.setContentType(12);
        request.setLastTime(mPresenter.lastTime);
        request.setThumbnail(mPresenter.thumbnail);
        request.setPlayUrl(mPresenter.playUrl);
        mPresenter.saveBehaviour("01", request, HeaderRequest.SHAKESTAR_PREVIEW);
        KLog.e("视频时长是" + (int)mPresenter.lastTime);
        KLog.e("G03ID是" + g03);
        KLog.e("描述是=====" + request.getDescription());
        KLog.e("图片地址=====" + request.getThumbnail());
        /*if (mPresenter.isVideo && !mPresenter.takeVideo) {
            //此时为选择的视频文件!
            //非拍摄视频去处理...*/
            try {
                KLog.e("不是拍摄视频");
                UploadService.getUploadManager().addNewUpload(
                        videoFilePath,
                        getUserInfo().getUserId(),
                        getUserInfo().getToken(),
                        UploadInfo.SaveTypeEnum.SHAKE_STAR_PREVIEW,
                        "",
                        JsonUtils.toJson(request, request.getClass()));
            } catch (DbException e) {
                e.printStackTrace();
            }
            showToast("视频过大可能导致压缩时间较长哦，请耐心等候！");
//            launchActivity(UploadManagerActivity.class);
//            finish();
//            return;
//        }
        mPresenter.saveData(request);
        sendNetRequest(request,HeaderRequest.SHAKESTAR_PREVIEW,ShakeStarPreviewPresenter.UPLOAD_VIDEO);
    }

    @Override
    protected void loadData() {
        StatusBarCompat.translucentStatusBar(this);

    }

    @Override
    public void showUploadingProgress(int progress) {
        /*if (progress > 0) {
            Message message = new Message();
            message.what = 999;
            message.arg1 = progress;
            handler.sendMessage(message);
        }*/
    }

    @Override
    public void dismissUploadingProgress(int actionId) {

    }

    @Override
    public void finishView() {
        setResult(RESULT_OK);
//        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (mPopupWindow != null) {
            mPopupWindow.onActivityResult(requestCode, resultCode, data);
        }*/
    }

/*    @Override
    public void chooseSuccess(int type, boolean isVideo, String path) {
        switch (type) {
            case ChooseVideoPopupWindow.GALLERY_REQUEST_CODE:
                if (isVideo) {
                    this.videoFilePath = path;
                    mPresenter.uploadVideo(path, false);
                } else {
                    mPresenter.uploadImg(path, 4, ShakeStarPreviewPresenter.UPLOAD_IMG_REQUEST);
                }
                break;
            case ChooseVideoPopupWindow.VIDEO_TAKE_REQUEST_CODE:
                mPresenter.uploadVideo(path, true);

                break;
            case ChooseVideoPopupWindow.PHOTO_TAKE_REQUEST_CODE:
                mPresenter.uploadImg(path, 4, ShakeStarPreviewPresenter.UPLOAD_IMG_REQUEST);
                break;
            default:
                break;
        }
    }*/


//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 999:
//                    mViewBinding.uploadingPb.setProgress(msg.arg1);
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    };
}
