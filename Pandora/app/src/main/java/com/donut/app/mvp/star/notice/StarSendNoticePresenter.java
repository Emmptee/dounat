package com.donut.app.mvp.star.notice;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.SparseArray;

import com.android.volley.manager.LoadController;
import com.android.volley.manager.RequestManager;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.SendNetRequestManager;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.StarNoticeAddRequest;
import com.donut.app.http.message.UploadResponse;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.FileUtils;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.PictureUtil;

import java.io.File;
import java.util.Map;
import java.util.UUID;


/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public class StarSendNoticePresenter extends StarSendNoticeContract.Presenter {

    private static final int STAR_NOTICE_ADD = 1;

    public static final int UPLOAD_IMG_REQUEST = 2,
            UPLOAD_VIDEO_IMG = 3, UPLOAD_VIDEO = 4;

    SparseArray<LoadController> mUploadArray
            = new SparseArray<>();

    public String imgUrl, playUrl, thumbnail;

    long lastTime;

    boolean takeVideo;

    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        switch (actionId) {
            case STAR_NOTICE_ADD:
                BaseResponse response = JsonUtils.fromJson(responseJson, BaseResponse.class);
                if (COMMON_SUCCESS.equals(response.getCode())) {
                    showToast("发布通告成功!");
                    mView.finishView();
                } else {
                    showToast(response.getMsg());
                }
                break;
        }
    }

    public void uploadImg(String path, int type, int requestCode) {

        LoadController controller = mUploadArray.get(requestCode);
        if (controller != null) {
            controller.cancel();
        }
        mUploadArray.delete(requestCode);

        SendNetRequestManager requestManager = new SendNetRequestManager(requestListener);
        LoadController loadController = requestManager.uploadImg(path, type, requestCode);
        mUploadArray.put(requestCode, loadController);

    }

    public void uploadVideo(String filePath, boolean takeVideo) {
        if (TextUtils.isEmpty(filePath) || filePath.startsWith("file")) {
            showToast("您选择的文件已损坏,请重新选择");
            return;
        }
        this.takeVideo = takeVideo;

        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(
                filePath, MediaStore.Video.Thumbnails.MINI_KIND);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth(), bitmap.getHeight(),
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        File file = new File(FileUtils.getCachePath(mView.getContext(), "images"),
                UUID.randomUUID().toString() + ".JPG");
        if (bitmap != null) {
            PictureUtil.compressBmpToFile(bitmap, file);
        }

        // 上传视频文件缩略图
        uploadImg(file.getAbsolutePath(), 4, UPLOAD_VIDEO_IMG);
        if (takeVideo) {
            LoadController controller = mUploadArray.get(UPLOAD_VIDEO);
            if (controller != null) {
                controller.cancel();
            }
            mUploadArray.delete(UPLOAD_VIDEO);
            // 上传拍摄的视频文件
            SendNetRequestManager requestManager = new SendNetRequestManager(requestListener);
            LoadController loadController = requestManager.uploadImg(filePath, 1, UPLOAD_VIDEO);
            mUploadArray.put(UPLOAD_VIDEO, loadController);
        }
    }

    private RequestManager.RequestListener requestListener
            = new RequestManager.RequestListener() {
        @Override
        public void onRequest() {
        }

        @Override
        public void onLoading(long total, long count, String filePath) {
            int progress = (int) ((float) count / (float) total * 100f);
            mView.showUploadingProgress(filePath, progress);
        }

        @Override
        public void onSuccess(String response, Map<String, String> headers,
                              String url, int actionId) {
            mView.dismissUploadingProgress(actionId);
            mUploadArray.remove(actionId);
            UploadResponse res = JsonUtils.fromJson(response, UploadResponse.class);
            if (COMMON_SUCCESS.equals(res.getCode())) {
                switch (actionId) {
                    case UPLOAD_IMG_REQUEST:
                        imgUrl = res.getFileUrl();
                        break;
                    case UPLOAD_VIDEO_IMG:
                        thumbnail = res.getFileUrl();
                        break;
                    case UPLOAD_VIDEO:
                        lastTime = res.getVideoTime();
                        playUrl = res.getFileUrl();
                        break;
                }
            }
        }

        @Override
        public void onError(String errorMsg, String url, int actionId) {
            mView.dismissUploadingProgress(actionId);
            mUploadArray.remove(actionId);
        }
    };

    @Override
    public void saveData(StarNoticeAddRequest request) {

        super.loadData(request, HeaderRequest.STAR_NOTICE_ADD,
                STAR_NOTICE_ADD, true);
    }

    void saveBehaviour(String functionCode) {
        SaveBehaviourDataService.startAction(mView.getContext(),
                BehaviourEnum.STAR_SEND_NOTICE.getCode() + functionCode);
    }

    void saveBehaviour(String functionCode, Object request, String header) {
        SaveBehaviourDataService.startAction(mView.getContext(),
                BehaviourEnum.STAR_SEND_NOTICE.getCode() + functionCode, request, header);
    }

}
