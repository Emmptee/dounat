package com.donut.app.mvp.shakestar.video.record;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.donut.app.mvp.shakestar.select.particulars.ParticularsPresenter;
import com.donut.app.mvp.shakestar.video.DonutCameraVideoView;
import com.donut.app.mvp.shakestar.video.camera.util.FileUtil;
import com.donut.app.mvp.shakestar.video.camera.util.ScreenUtils;
import com.donut.app.mvp.shakestar.video.record.preview.ShakeStarPreviewContract;
import com.donut.app.mvp.shakestar.video.record.preview.ShakeStarPreviewPresenter;
import com.donut.app.mvp.shakestar.video.record.preview.ShakeStarPreviewRequest;
import com.donut.app.service.UploadService;
import com.donut.app.utils.BindingUtils;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.status_bar.StatusBarCompat;
import com.lidroid.xutils.exception.DbException;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.socks.library.KLog;

import java.io.File;

public class RecordPreviewActivity extends MVPBaseActivity<ActivityRecordPreviewLayoutBinding, ShakeStarPreviewPresenter>
        implements ShakeStarPreviewContract.View {
    private static final String TAG = "WISHHHH";

    private DonutCameraVideoView mPreviewVideoview;
    private RelativeLayout mPreviewVideoLayout;
    private Button mPreviewBtnPublish;
    private EditText mVideoPreviewSearchEt;
    private ImageView coverView;
    private Bitmap coverBitmap;
    CharSequence descriptionCharSequence;
    private String videoFilePath = FileUtil.choseSavePath() + File.separator + "dest.mp4";
    private String mVideoFilePath ;
    MediaInfo joinVideoInfo = new MediaInfo(videoFilePath);


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
                KLog.e("点击了返回");
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
        mPreviewBtnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.uploadVideo(videoFilePath);
                saveData();
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
        Log.e(TAG,"开始保存数据");
        String des = mVideoPreviewSearchEt.getText().toString().trim();
        Log.e(TAG,"描述是" + des);
        if (TextUtils.isEmpty(des)) {
            showToast("请填写描述");
            return;
        }
        if (mPresenter.mUploadArray.size() > 0) {
            showToast("视频正在上传,请稍后");
//            return;
        }
        Log.e(TAG,"视频地址是" + mPresenter.playUrl);

        if (TextUtils.isEmpty(mPresenter.playUrl)) {
            showToast("抱歉，视频上传失败，请重新上传");
            return;
        }
        Log.e(TAG,"开始请求");
        ShakeStarPreviewRequest request = new ShakeStarPreviewRequest();
        request.setDescription(des);
        request.setThumbnail(mPresenter.thumbnail);
        request.setPlayUrl(mPresenter.playUrl);
        mPresenter.saveBehaviour("01", request, HeaderRequest.SHAKESTAR_PREVIEW);
        try {
            UploadService.getUploadManager().addNewUpload(
                    videoFilePath,
                    getUserInfo().getUserId(),
                    getUserInfo().getToken(),
                    UploadInfo.SaveTypeEnum.SHAKE_STAR_PREVIEW,
                    "",
                    JsonUtils.toJson(request, request.getClass()));
            Log.e(TAG,"地址是" + videoFilePath);
        } catch (DbException e) {
            e.printStackTrace();
        }
        showToast("视频过大可能导致压缩时间较长哦，请耐心等候！");
        Log.e(TAG,"快要finish");
        finish();
        mPresenter.saveData(request);
        return;

    }

    @Override
    protected void loadData() {
        StatusBarCompat.translucentStatusBar(this);

    }

    @Override
    public void showUploadingProgress( int progress) {
        if (progress > 0) {
            Message message = new Message();
            message.what = 999;
            message.arg1 = progress;
            handler.sendMessage(message);
        }
    }

    @Override
    public void dismissUploadingProgress(int actionId) {
        mViewBinding.uploadingPb.setVisibility(View.GONE);
    }

    @Override
    public void finishView() {
        setResult(RESULT_OK);
        finish();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 999:
                    mViewBinding.uploadingPb.setProgress(msg.arg1);
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
