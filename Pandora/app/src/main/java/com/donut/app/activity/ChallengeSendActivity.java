package com.donut.app.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bis.android.plug.cameralibrary.materialcamera.MaterialCamera;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.customview.SelectPicPopupWindow;
import com.donut.app.entity.UploadInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.ChallengeRequest;
import com.donut.app.http.message.UploadResponse;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.service.UploadService;
import com.donut.app.utils.FileUtils;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.L;
import com.donut.app.utils.NetUtils;
import com.donut.app.utils.PictureUtil;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 发起挑战
 */
public class ChallengeSendActivity extends BaseActivity {

    @ViewInject(R.id.head_right_tv)
    private TextView tvRight;

    @ViewInject(R.id.challenge_send_et_title)
    private EditText etTitle;

    @ViewInject(R.id.challenge_send_et_content)
    private EditText etContent;

    @ViewInject(R.id.challenge_send_detail_nowNum)
    private TextView tvNowNum;

    @ViewInject(R.id.challenge_send_add_video)
    private ImageView ivVideo;

    @ViewInject(R.id.challenge_send_video_mask)
    private View viewMask;

    @ViewInject(R.id.challenge_send_video_pb)
    private ProgressBar videoPb;

    @ViewInject(R.id.challenge_send_cb_approve)
    private CheckBox cbApprove;

    public static final String SUBJECT_ID = "SUBJECT_ID";

    private static final int VIDEO_REQUEST_CODE = 1,
            VIDEO_TAKE_REQUEST_CODE = 2;

    private static final int UPLOAD_VIDEO = 1, UPLOAD_IMG = 2, SAVE_CHALLENGE = 3;

    private boolean isUpLoading = false;

    private int videoTime;

    private String videoPath, videoUrl, imgUrl, subjectId;

    private boolean takeVideo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_send);

        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        updateHeadTitle("发起挑战", true);
        tvRight.setText("提交");
        initData();
        initEvents();
    }

    private void initData() {
        subjectId = getIntent().getStringExtra(SUBJECT_ID);
    }

    private void initEvents() {

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvNowNum.setText(String.format(getString(R.string.now_num), String.valueOf(s.length())));
            }
        });

    }

    @OnClick({R.id.challenge_send_add_video,
            R.id.menu, R.id.challenge_send_provisions})
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.challenge_send_add_video:
                requestRuntimePermission("为了给您提供更好的服务,甜麦圈需要获取存储器读写权限",
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
            case R.id.menu:
                saveData();
                break;
            case R.id.challenge_send_provisions:
                startActivity(new Intent(this, WebTermsActivity.class)
                        .putExtra(WebTermsActivity.IS_CHALLENGE, true));
                break;
        }
    }

    private void saveData() {

        if (!cbApprove.isChecked()) {
            showToast("请同意活动条款");
            return;
        }

        String title = etTitle.getText().toString().trim();
        String des = etContent.getText().toString().trim();

        if (isUpLoading && !takeVideo) {
            showToast("视频上传中...");
            return;
        }

        if (TextUtils.isEmpty(title)) {
            showToast("请输入标题");
            return;
        }
        if (TextUtils.isEmpty(videoUrl) && (!takeVideo && !TextUtils.isEmpty(this.videoPath))) {
            showToast("请上传视频");
            return;
        }

        if (TextUtils.isEmpty(imgUrl)) {
            showToast("视频封面上传中...");
            return;
        }

        if (TextUtils.isEmpty(des)) {
            showToast(getString(R.string.challenge_send_des));
            return;
        }

        ChallengeRequest request = new ChallengeRequest();
        request.setSubjectId(subjectId);
        request.setTitle(title);
        request.setContent(des);
        request.setPlayUrl(videoUrl);
        request.setLastTime(videoTime);
        request.setThumbnailUrl(imgUrl);

        saveBehaviour("01", request, HeaderRequest.SAVE_CHALLENGE);
        if (takeVideo) {
            if (!NetUtils.isNetworkConnected(this)) {
                showToast(getString(R.string.connect_no));
                return;
            }
            // 如果非拍摄视频去处理...
            try {
                UploadService.getUploadManager().addNewUpload(
                        this.videoPath,
                        getUserInfo().getUserId(),
                        getUserInfo().getToken(),
                        UploadInfo.SaveTypeEnum.CHALLENGE,
                        title,
                        JsonUtils.toJson(request, request.getClass()));
            } catch (DbException e) {
                e.printStackTrace();
            }

            showToast("视频过大可能导致压缩时间较长哦，请耐心等候！");
            startActivity(new Intent(this, UploadManagerActivity.class));
            finish();
            return;
        }
        sendNetRequest(request, HeaderRequest.SAVE_CHALLENGE, SAVE_CHALLENGE);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (perms != null && perms.size() == 2) {
            showAddPopupWindow();
        }
    }

    private SelectPicPopupWindow showWindow;

    private void showAddPopupWindow() {
        showWindow = new SelectPicPopupWindow(this, new SelectPicPopupWindow.OnClickListenerWithPosition() {
            @Override
            public void onClick(View v, int actionId) {
                showWindow.dismiss();
                switch (v.getId()) {
                    case R.id.btn_click_three:
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        intent.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*");
                        startActivityForResult(intent, VIDEO_REQUEST_CODE);
                        break;
                    case R.id.btn_click_one:
//                        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                        videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//                        videoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 900);//拍摄时长,秒数
////                      videoIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024*1L);//查资料说是设置文件大小,但设置之后无效果???WHY?
//                        startActivityForResult(videoIntent, VIDEO_TAKE_REQUEST_CODE);

                        MaterialCamera materialCamera = new MaterialCamera(ChallengeSendActivity.this)
                                .countdownSeconds(180)
                                .videoEncodingBitRate(1200 * 1024)
//                                .videoFrameRate(26)
                                .videoPreferredAspect(16f / 9f)
//                                .qualityProfile(MaterialCamera.QUALITY_720P)
                                .saveDir(FileUtils.getCachePath(ChallengeSendActivity.this, "video"))
                                .showPortraitWarning(false)
                                .allowRetry(true);
                        materialCamera.start(VIDEO_TAKE_REQUEST_CODE);
                        break;
                }
            }
        }, "录制", "本机");

        showWindow.showAtLocation(this.findViewById(R.id.layout_main),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case VIDEO_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    takeVideo = true;
                    getVideoFromIntent(data, false);
                }
                break;
            case VIDEO_TAKE_REQUEST_CODE:

                if (resultCode == RESULT_OK) {
                    takeVideo = false;

//                    String filePath = data.getStringExtra(CameraAty.VIDEOPATH);
//                    int time = data.getIntExtra(CameraAty.VIDEOTIME, 0);
//                    String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
//                    getVideoInfo(filePath, fileName, time);
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(data.getData());
                    this.sendBroadcast(intent);
                    getVideoFromIntent(data, true);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getVideoFromIntent(Intent data, boolean TAKE_VIDEO) {
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
            } catch (Exception e){
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
//        if (!"video/mp4".equals(mime_type) && !"video/3gpp".equals(mime_type)) {
//            showToast("仅支持MP4/3GP格式视频,请重新选择");
//            return;
//        }
        getVideoInfo(filePath, title, time);
    }

    private void getVideoInfo(String filePath, String title, int time) {
        if (TextUtils.isEmpty(filePath) || filePath.startsWith("file")) {
            showToast("您选择的文件已损坏,请重新选择");
            return;
        }

        int width = getResources().getDisplayMetrics().widthPixels;
        float density = getResources().getDisplayMetrics().density;
        int imgWidth = (int) (width - (density * 20 + 0.5f)) / 3;
        ivVideo.setMaxHeight(imgWidth);
        ivVideo.setMaxWidth(imgWidth);

        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(
                filePath, MediaStore.Video.Thumbnails.MINI_KIND);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth(), bitmap.getHeight(),
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        ivVideo.setImageBitmap(bitmap);

        File file = new File(FileUtils.getCachePath(this, "images"),
                UUID.randomUUID().toString() + ".JPG");

        if (bitmap != null) {
            PictureUtil.compressBmpToFile(bitmap, file);
        }

        this.videoPath = filePath;
        this.videoTime = time;

        // 上传视频文件缩略图
        UpLoadNetRequest(file.getAbsolutePath(), 4, UPLOAD_IMG);
        // 上传视频文件
        if (!takeVideo) {
            // 只有拍摄的视频才直接上传.
            UpLoadNetRequest(filePath, 1, UPLOAD_VIDEO);
        }

        isUpLoading = true;
    }

    @Override
    public void onLoading(long total, long count, String filePath) {
        super.onLoading(total, count, filePath);
        if (filePath.equals(videoPath)) {
            Message message = new Message();
            message.what = 999;
            message.arg1 = (int) ((float) count / (float) total * 100f);
            ChallengeSendActivity.this.handler.sendMessage(message);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 999:
                    videoPb.setVisibility(View.VISIBLE);
                    videoPb.setProgress(msg.arg1);
                    break;
                case 111:
                    Bundle bundle = msg.getData();
                    ChallengeSendActivity.super.onError(bundle.getString("errorMsg"),
                            bundle.getString("url"), bundle.getInt("actionId"));
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onSuccess(String response, String url, int actionId) {
        switch (actionId) {
            case UPLOAD_VIDEO:
            case UPLOAD_IMG:
                UploadResponse res = JsonUtils.fromJson(response, UploadResponse.class);
                if (COMMON_SUCCESS.equals(res.getCode())) {
                    if (actionId == UPLOAD_VIDEO) {
                        isUpLoading = false;

                        viewMask.setVisibility(View.GONE);
                        videoPb.setVisibility(View.GONE);
                        videoUrl = res.getFileUrl();
                        int time = res.getVideoTime().intValue();
                        if (time != 0) {
                            videoTime = time;
                        }
                    } else {
                        imgUrl = res.getFileUrl();
                    }
                } else {
                    showToast(res.getMsg());
                }
                break;
            case SAVE_CHALLENGE:
                BaseResponse saveRes = JsonUtils.fromJson(response, BaseResponse.class);
                if (COMMON_SUCCESS.equals(saveRes.getCode())) {
                    showToast("发起挑战成功");
                    setResult(RESULT_OK);
                    finish();
                } else {
                    showToast(saveRes.getMsg());
                }
                break;
        }

        super.onSuccess(response, url, actionId);
    }

    @Override
    public void onError(String errorMsg, String url, int actionId) {
        Message message = new Message();
        message.what = 111;
        Bundle bundle = new Bundle();
        bundle.putString("errorMsg", errorMsg);
        bundle.putString("url", url);
        bundle.putInt("actionId", actionId);
        message.setData(bundle);
        ChallengeSendActivity.this.handler.sendMessage(message);

        if (actionId == UPLOAD_VIDEO) {
            isUpLoading = false;
        }
    }

    @Override
    public void onBackPressed() {
        String name = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(content)
                && TextUtils.isEmpty(videoUrl) && !isUpLoading) {
            super.onBackPressed();
        } else {
            Dialog dialog = new AlertDialog.Builder(this)
                    .setMessage("信息未保存，确定返回吗？")
                    .setNegativeButton(getString(R.string.cancel), null)
                    .setPositiveButton(getString(R.string.sure),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    saveBehaviour("02");
                                    ChallengeSendActivity.super.onBackPressed();
                                }
                            }).create();
            dialog.show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        saveBehaviour("00");
    }

    @Override
    protected void onStop() {
        saveBehaviour("xx");
        super.onStop();
    }

    private void saveBehaviour(String functionCode) {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.CHALLENGE_SEND.getCode() + functionCode);
    }

    private void saveBehaviour(String functionCode, Object request, String header) {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.CHALLENGE_SEND.getCode() + functionCode, request, header);
    }
}
