package com.donut.app.mvp.star.notice;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;

import com.bis.android.plug.cameralibrary.materialcamera.MaterialCamera;
import com.donut.app.R;
import com.donut.app.customview.SelectPicPopupWindow;
import com.donut.app.model.galleypick.GallerySelectorActivity;
import com.donut.app.model.galleypick.entities.MediaEntity;
import com.donut.app.utils.FileUtils;
import com.donut.app.utils.L;
import com.donut.app.utils.ToastUtil;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Qi on 2017/3/22.
 * Description : <br>
 */
public class ShowChooseVideoPopupWindow {

    public static final int VIDEO_TAKE_REQUEST_CODE = 1,
            GALLERY_REQUEST_CODE = 2;

    private Activity activity;

    private OnShowViewListener mShowViewListener;

    public ShowChooseVideoPopupWindow(Activity activity, OnShowViewListener listener) {
        this.activity = activity;
        this.mShowViewListener = listener;
    }

    public void show(View view) {
        showWindow = new SelectPicPopupWindow(activity, mListener, "录制", "从相册选择");
        showWindow.showAtLocation(view,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private SelectPicPopupWindow showWindow;
    private SelectPicPopupWindow.OnClickListenerWithPosition mListener
            = new SelectPicPopupWindow.OnClickListenerWithPosition() {
        @Override
        public void onClick(View v, int actionId) {
            showWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_click_three:
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    intent.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*");
                    activity.startActivityForResult(intent, GALLERY_REQUEST_CODE);
//                    Intent intent = new Intent(activity,
//                            GallerySelectorActivity.class);
//                    activity.startActivityForResult(intent, GALLERY_REQUEST_CODE);

                    break;
                case R.id.btn_click_one:
                    MaterialCamera materialCamera = new MaterialCamera(activity)
                            .countdownSeconds(180)
                            .videoEncodingBitRate(1200 * 1024)
//                                .videoFrameRate(26)
                            .videoPreferredAspect(16f / 9f)
//                                .qualityProfile(MaterialCamera.QUALITY_720P)
                            .saveDir(FileUtils.getCachePath(activity, "video"))
                            .showPortraitWarning(false)
                            .allowRetry(true);
                    materialCamera.start(VIDEO_TAKE_REQUEST_CODE);
                    break;
            }
        }

        private void empty() {
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GALLERY_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    getVideoFromIntent(data, false);
                }
                break;
            case VIDEO_TAKE_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(data.getData());
                    activity.sendBroadcast(intent);
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
                ToastUtil.showShort(activity, "系统异常,请重试!");
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
                cursor = activity.getContentResolver().query(selectedVideo,
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
                    ToastUtil.showShort(activity, "请确定读取视频的权限处于启用状态！");
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
            ToastUtil.showShort(activity, "亲，建议您选择少于3分钟的视频哦！");
            return;
        }
        mShowViewListener.chooseVideoSuccess(takeVideo, filePath);
    }

    interface OnShowViewListener {
        void chooseVideoSuccess(boolean takeVideo, String path);
    }
}
