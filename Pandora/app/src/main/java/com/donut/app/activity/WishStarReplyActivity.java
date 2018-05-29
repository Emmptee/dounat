package com.donut.app.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bis.android.plug.cameralibrary.materialcamera.MaterialCamera;
import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.Constant;
import com.donut.app.customview.SelectPicPopupWindow;
import com.donut.app.entity.UploadInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.UploadResponse;
import com.donut.app.http.message.wish.AchieveWishVoice;
import com.donut.app.http.message.wish.WishCompletedRequest;
import com.donut.app.model.audio.AudioRecorderButton;
import com.donut.app.model.audio.MediaManager;
import com.donut.app.model.galleypick.GallerySelectorActivity;
import com.donut.app.model.galleypick.entities.MediaEntity;
import com.donut.app.service.UploadService;
import com.donut.app.utils.FileUtils;
import com.donut.app.utils.GlideCircleTransform;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.L;
import com.donut.app.utils.PictureUtil;
import com.donut.app.utils.StatusBarUtil;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WishStarReplyActivity extends BaseActivity {

    @ViewInject(R.id.head_right_tv)
    private TextView tvRight;

    @ViewInject(R.id.wish_reply_user_head_img)
    private ImageView ivUserHead;

    @ViewInject(R.id.wish_reply_user_name)
    private TextView tvUserName;

    @ViewInject(R.id.wish_reply_user_time)
    private TextView tvUserTime;

    @ViewInject(R.id.wish_reply_user_content)
    private TextView tvUserContent;

    @ViewInject(R.id.wish_reply_img)
    private ImageView ivImg;

    @ViewInject(R.id.wish_reply_img_hint)
    private View imgHint;

    @ViewInject(R.id.wish_reply_content)
    private EditText etReplyContent;

    @ViewInject(R.id.wish_reply_content_hint)
    private View replyContentHint;

    @ViewInject(R.id.wish_reply_audio_add_view)
    private View audioAddView;

    @ViewInject(R.id.wish_reply_audio_add)
    private AudioRecorderButton mRecord;

    @ViewInject(R.id.wish_reply_audio_play_view)
    private View audioPlayView;

    @ViewInject(R.id.wish_reply_audio_play_anim)
    private ImageView audioPlayViewAnim;

    @ViewInject(R.id.wish_reply_audio_last_time)
    private TextView tvVoiceLastTime;

    @ViewInject(R.id.wish_reply_uploading_pb)
    private ProgressBar uploadingPb;

    public static final String B02_ID = "B02_ID", G01_ID = "G01_ID",
            NICK_NAME = "NICK_NAME", HEAD_PIC = "HEAD_PIC",
            DESCRIPTION = "DESCRIPTION", CREATE_TIME = "CREATE_TIME";

    private String b02Id, g01Id;

    private MediaEntity entity;

    private static final int WISH_REPLY_REQUEST = 0, UPLOAD_IMG_REQUEST = 1,
            UPLOAD_VIDEO_IMG = 2, UPLOAD_VIDEO = 3, UPLOAD_VOICE = 4;

    private static final int PHOTO_TAKE_REQUEST_CODE = 0, VIDEO_TAKE_REQUEST_CODE = 1,
            GALLERY_REQUEST_CODE = 2;

    private String achievePicUrl, achieveVideoUrl,
            achieveVoiceUrl, voiceCreateTime, voiceFilePath, filePath;

    private float voiceLastTime, upLoadingNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_star_reply);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        updateHeadTitle("达成心愿", true);
        tvRight.setText(R.string.submit);
        initData();
        initEvents();
    }

    private void initData() {
        b02Id = getIntent().getStringExtra(B02_ID);
        g01Id = getIntent().getStringExtra(G01_ID);
        tvUserName.setText(getIntent().getStringExtra(NICK_NAME));
        tvUserTime.setText(getIntent().getStringExtra(CREATE_TIME));
        tvUserContent.setText(getIntent().getStringExtra(DESCRIPTION));
        Glide.with(this)
                .load(getIntent().getStringExtra(HEAD_PIC))
                .centerCrop()
                .error(R.drawable.default_header)
                .transform(new GlideCircleTransform(this))
                .into(ivUserHead);
    }

    private CharSequence contentOldSequence;

    private void initEvents() {

        etReplyContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                contentOldSequence = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    replyContentHint.setVisibility(View.GONE);
                } else {
                    replyContentHint.setVisibility(View.VISIBLE);
                }

                int editStart = etReplyContent.getSelectionStart();
                int editEnd = etReplyContent.getSelectionEnd();
                if (contentOldSequence.length() > 200) {
                    showToast("文字回复限制200字以内");
                    s.delete(editStart - 1, editEnd);
                    etReplyContent.setText(s);
                    if (editStart > 200) {
                        editStart = 200 + 1;
                    }
                    etReplyContent.setSelection(editStart - 1);
                }
            }
        });

        AudioRecorderButton.AudioFinishRecorderListener listener =
                new AudioRecorderButton.AudioFinishRecorderListener() {
                    @Override
                    public void onFinish(float seconds, String filePath) {

                        audioAddView.setVisibility(View.GONE);
                        audioPlayView.setVisibility(View.VISIBLE);
                        voiceLastTime = seconds;
                        voiceFilePath = filePath;
                        tvVoiceLastTime.setText((int) seconds + "”");
                        UpLoadNetRequest(filePath, 5, UPLOAD_VOICE);
                        upLoadingNum++;
                    }
                };
        mRecord.setAudioFinishRecorderListener(listener);

        mRecord.setChangeBackground(false);
    }

    @OnClick({R.id.wish_reply_img, R.id.menu,
            R.id.wish_reply_audio_play,
            R.id.wish_reply_audio_delete})
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.wish_reply_img:
                requestRuntimePermission("为了给您提供更好的服务,甜麦圈需要获取存储器读写权限",
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
            case R.id.menu:
                saveData();
                break;
            case R.id.wish_reply_audio_play:

                audioPlayViewAnim.setBackgroundResource(R.drawable.play_anim);
                final AnimationDrawable animationDrawable = (AnimationDrawable) audioPlayViewAnim.getBackground();
                animationDrawable.start();
                MediaManager.playSound(voiceFilePath, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        animationDrawable.stop();
                        audioPlayViewAnim.setBackgroundResource(R.drawable.voice_gray);
                    }
                });

                break;
            case R.id.wish_reply_audio_delete:
                achieveVoiceUrl = voiceCreateTime = "";
                voiceLastTime = 0;
                audioAddView.setVisibility(View.VISIBLE);
                audioPlayView.setVisibility(View.GONE);
                break;
        }
    }

    private void saveData() {

        if (upLoadingNum > 0) {
            showToast("正在上传,请稍后");
            return;
        }

        String achieveText = etReplyContent.getText().toString().trim();

        if (TextUtils.isEmpty(achievePicUrl)
                && TextUtils.isEmpty(achieveVideoUrl)
                && TextUtils.isEmpty(achieveVoiceUrl)
                && TextUtils.isEmpty(achieveText)) {
            showToast("请回复心愿后再提交");
            return;
        }

        WishCompletedRequest request = new WishCompletedRequest();
        request.setB02Id(b02Id);
        request.setG01Id(g01Id);
        request.setAchievePicUrl(achievePicUrl);
        request.setAchieveVideoUrl(achieveVideoUrl);
        if (!TextUtils.isEmpty(achieveVoiceUrl)) {
            List<AchieveWishVoice> achieveVoiceList = new ArrayList<>();
            AchieveWishVoice voice = new AchieveWishVoice();
            voice.setAchieveVoiceUrl(achieveVoiceUrl);
            voice.setCreateTime(voiceCreateTime);
            voice.setLastTime((long) voiceLastTime * 1000);
            achieveVoiceList.add(voice);
            request.setAchieveVoiceList(achieveVoiceList);
        }
        request.setAchieveText(achieveText);
        if (entity != null && entity.isVideo()) {
            //此时为选择的视频文件!
            //非拍摄视频去处理...
            try {
                UploadService.getUploadManager().addNewUpload(
                        entity.getPath(),
                        getUserInfo().getUserId(),
                        getUserInfo().getToken(),
                        UploadInfo.SaveTypeEnum.STAR_REPLY,
                        "",
                        JsonUtils.toJson(request, request.getClass()));
            } catch (DbException e) {
                e.printStackTrace();
            }
            showToast("视频过大可能导致压缩时间较长哦，请耐心等候！");
            startActivity(new Intent(this, UploadManagerActivity.class));
            finish();
            return;
        }

        sendNetRequest(request, HeaderRequest.WISH_REPLY, WISH_REPLY_REQUEST);
    }

    private SelectPicPopupWindow showWindow;
    private SelectPicPopupWindow.OnClickListenerWithPosition mListener
            = new SelectPicPopupWindow.OnClickListenerWithPosition() {
        @Override
        public void onClick(View v, int actionId) {
            showWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_click_three:
//                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
//                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                    startActivityForResult(intent, GALLERY_REQUEST_CODE);
                    Intent intent = new Intent(WishStarReplyActivity.this,
                            GallerySelectorActivity.class);
                    startActivityForResult(intent, GALLERY_REQUEST_CODE);

                    break;
                case R.id.btn_click_one:
                    MaterialCamera materialCameraToPhoto = new MaterialCamera(WishStarReplyActivity.this)
                            .saveDir(FileUtils.getCachePath(WishStarReplyActivity.this, "img"))
                            .qualityProfile(MaterialCamera.QUALITY_720P)
                            .videoPreferredAspect(16f / 9f)
                            .showPortraitWarning(false)
                            .allowRetry(true);
                    materialCameraToPhoto.stillShot();
                    materialCameraToPhoto.start(PHOTO_TAKE_REQUEST_CODE);
                    break;
                case R.id.btn_click_two:
                    MaterialCamera materialCamera = new MaterialCamera(WishStarReplyActivity.this)
                            .countdownSeconds(180)
                            .videoEncodingBitRate(1200 * 1024)
//                                .videoFrameRate(26)
                            .videoPreferredAspect(16f / 9f)
//                                .qualityProfile(MaterialCamera.QUALITY_720P)
                            .saveDir(FileUtils.getCachePath(WishStarReplyActivity.this, "video"))
                            .showPortraitWarning(false)
                            .allowRetry(true);
                    materialCamera.start(VIDEO_TAKE_REQUEST_CODE);
                    break;
            }
        }

        private void empty() {
        }
    };

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (perms != null && perms.size() == 2) {
            showAddPopupWindow();
        }
    }

    private void showAddPopupWindow() {
        showWindow = new SelectPicPopupWindow(this, mListener, "拍摄", "从相册选择", "录制");
        showWindow.showAtLocation(this.findViewById(R.id.activity_wish_star_reply),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_TAKE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    entity = null;
                    File file = new File(data.getData().getPath());
                    String path = file.getAbsolutePath();
                    this.filePath = path;
                    Glide.with(this).load(path)
                            .centerCrop().into(ivImg);
                    imgHint.setVisibility(View.GONE);
                    UpLoadNetRequest(path, 4, UPLOAD_IMG_REQUEST);
                    upLoadingNum++;
                }
                break;
            case GALLERY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    entity = data.getParcelableExtra("MEDIA_ENTITY");
                    Glide.with(this).load(entity.getPath())
                            .centerCrop().into(ivImg);
                    imgHint.setVisibility(View.GONE);
                    if (entity.isVideo()) {
                        //视频文件上传单独处理
                        uploadVideoInfo(entity.getPath(), false);
                    } else {
                        this.filePath = entity.getPath();
                        UpLoadNetRequest(entity.getPath(), 4, UPLOAD_IMG_REQUEST);
                        upLoadingNum++;
                    }
                }
                break;
            case VIDEO_TAKE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    entity = null;
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(data.getData());
                    this.sendBroadcast(intent);
                    getVideoFromIntent(data, true);
                }
                break;
        }
    }

    private void getVideoFromIntent(Intent data, boolean takeVideo) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();

        int time = 0;
        String title = null, filePath = null, selection = null, mime_type = null;
        Uri selectedVideo = data.getData();
        try {
            filePath = java.net.URLDecoder.decode(selectedVideo.toString(), "utf-8");
            selectedVideo.buildUpon().encodedPath(filePath).build();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (ContentResolver.SCHEME_FILE.equals(selectedVideo.getScheme())) {
            String regEx = "file:(/*)";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(filePath);
            int num = filePath.lastIndexOf(m.replaceAll("").trim()) - 1;
            filePath = filePath.substring(num);

            try {
                mmr.setDataSource(filePath);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            mime_type = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            String strTime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            time = Integer.valueOf(strTime);
            title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

            if (mime_type == null || "".equals(mime_type)) {
                showToast("视频拍摄失败,请重新拍摄!");
                return;
            }
        } else if (ContentResolver.SCHEME_CONTENT.equals(selectedVideo.getScheme())) {
            selectedVideo = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            String regEx = "[^0-9]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(filePath);
            String id = m.replaceAll("").trim();
            selection = MediaStore.MediaColumns._ID + "='" + id + "'";

            Cursor cursor = null;
            try {
                cursor = this.getContentResolver().query(selectedVideo,
                        new String[]{MediaStore.MediaColumns.DATA,
                                MediaStore.Video.Media.MIME_TYPE,
                                MediaStore.Video.Media.DURATION,
                                MediaStore.MediaColumns.TITLE},
                        selection, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                    mime_type = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                    time = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE));
                } else {
                    ToastUtil.showShort(WishStarReplyActivity.this, "请确定读取视频的权限处于启用状态！");
                    return;
                }

            } catch (Exception e) {
                L.e("====", e.getMessage());
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        if (time > 182000) {
            showToast("亲，建议您选择少于3分钟的视频哦！");
            return;
        }
        uploadVideoInfo(filePath, takeVideo);
    }

    private void uploadVideoInfo(String filePath, boolean takeVideo) {
        if (TextUtils.isEmpty(filePath) || filePath.startsWith("file")) {
            showToast("您选择的文件已损坏,请重新选择");
            return;
        }

        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(
                filePath, MediaStore.Video.Thumbnails.MINI_KIND);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth(), bitmap.getHeight(),
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        File file = new File(FileUtils.getCachePath(this, "images"),
                UUID.randomUUID().toString() + ".JPG");
        if (bitmap != null) {
            PictureUtil.compressBmpToFile(bitmap, file);
        }

        Glide.with(this).load(file)
                .centerCrop().into(ivImg);
        imgHint.setVisibility(View.GONE);

        // 上传视频文件缩略图
        UpLoadNetRequest(file.getAbsolutePath(), 4, UPLOAD_VIDEO_IMG);
        upLoadingNum++;
        if (takeVideo) {
            // 上传视频文件
            // 拍摄的视频.
            this.filePath = filePath;
            UpLoadNetRequest(filePath, 1, UPLOAD_VIDEO);
            upLoadingNum++;
        }
    }

    @Override
    public void onSuccess(String response, String url, int actionId) {
        super.onSuccess(response, url, actionId);
        switch (actionId) {
            case UPLOAD_IMG_REQUEST:
            case UPLOAD_VIDEO_IMG:
            case UPLOAD_VIDEO:
            case UPLOAD_VOICE:
                upLoadingNum--;
                UploadResponse res = JsonUtils.fromJson(response, UploadResponse.class);
                if (COMMON_SUCCESS.equals(res.getCode())) {
                    if (actionId == UPLOAD_VIDEO) {
                        achieveVideoUrl = res.getFileUrl();
//                        int time = res.getVideoTime().intValue();
//                        if (time != 0)
//                            videoTime = time;
                    } else if (actionId == UPLOAD_VOICE) {
                        if (audioAddView.getVisibility() == View.VISIBLE) {
                            break;
                        }
                        achieveVoiceUrl = res.getFileUrl();
                        voiceCreateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA)
                                .format(System.currentTimeMillis());

                    } else {
                        achievePicUrl = res.getFileUrl();
                    }

                    if (actionId == UPLOAD_VIDEO || actionId == UPLOAD_IMG_REQUEST) {
                        uploadingPb.setVisibility(View.GONE);
                    }
                } else {
                    showToast(res.getMsg());
                }
                break;
            case WISH_REPLY_REQUEST:
                BaseResponse saveRes = JsonUtils.fromJson(response, BaseResponse.class);
                if (COMMON_SUCCESS.equals(saveRes.getCode())) {
                    showToast("达成心愿成功");
                    setResult(RESULT_OK);
                    finish();
                } else {
                    showToast(saveRes.getMsg());
                }
                break;

        }
    }

    @Override
    public void onError(String errorMsg, String url, int actionId) {
        super.onError(errorMsg, url, actionId);
        switch (actionId) {
            case UPLOAD_IMG_REQUEST:
            case UPLOAD_VIDEO_IMG:
            case UPLOAD_VIDEO:
            case UPLOAD_VOICE:
                upLoadingNum--;
                break;
        }
    }

    @Override
    public void onLoading(long total, long count, String filePath) {
        super.onLoading(total, count, filePath);
        if (filePath.equals(this.filePath)) {
            Message message = new Message();
            message.what = 999;
            message.arg1 = (int) ((float) count / (float) total * 100f);
            handler.sendMessage(message);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 999:
                    uploadingPb.setVisibility(View.VISIBLE);
                    uploadingPb.setProgress(msg.arg1);
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
