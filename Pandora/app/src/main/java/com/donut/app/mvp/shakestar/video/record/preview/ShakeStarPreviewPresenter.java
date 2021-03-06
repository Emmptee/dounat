package com.donut.app.mvp.shakestar.video.record.preview;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.SparseArray;

import com.android.volley.manager.LoadController;
import com.android.volley.manager.RequestManager;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.SendNetRequestManager;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.UploadResponse;
import com.donut.app.mvp.shakestar.video.camera.util.FileUtil;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.FileUtils;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.PictureUtil;
import com.socks.library.KLog;

import java.io.File;
import java.util.Map;

public class ShakeStarPreviewPresenter extends ShakeStarPreviewContract.Presenter {

    private static final int SHAKE_STAR_PREVIEW = 1;
    public static final int UPLOAD_IMG_REQUEST = 2,
            UPLOAD_VIDEO_IMG = 3, UPLOAD_VIDEO = 4;

    public SparseArray<LoadController> mUploadArray = new SparseArray<>();

    public String imgUrl, playUrl, videoThumbnail;
    public long lastTime;

    public boolean takeVideo, isVideo;

    @Override
    public void saveData(ShakeStarPreviewRequest request) {
        super.loadData(request, HeaderRequest.SHAKESTAR_PREVIEW,
                SHAKE_STAR_PREVIEW, false);
        KLog.v("保存数据");
    }

    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        switch (actionId) {
            case SHAKE_STAR_PREVIEW:
                BaseResponse response = JsonUtils.fromJson(responseJson, BaseResponse.class);
                KLog.v("CODEC----" + response.getCode());
                if (COMMON_SUCCESS.equals(response.getCode())) {
                    showToast("发布成功!");
                    KLog.v("发布成功");
                    mView.finishView();
                } else {
                    KLog.v("发布失败");
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
        KLog.v("上传图片");
        this.isVideo = false;

    }

    public void uploadVideo(String filePath, boolean takeVideo) {
        KLog.v("上传视频");
        this.takeVideo = takeVideo;
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(
                filePath, MediaStore.Video.Thumbnails.MINI_KIND);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth(), bitmap.getHeight(),
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        File file = new File(FileUtil.choseSavePath(), "DonutVideoThumbnail.jpg");
        if (bitmap != null) {
            PictureUtil.compressBmpToFile(bitmap, file);
        }

        // 上传视频文件缩略图
        uploadImg(file.getAbsolutePath(), 4, UPLOAD_VIDEO_IMG);
        LoadController controller = mUploadArray.get(UPLOAD_VIDEO);
        if (controller != null) {
            controller.cancel();
        }
        mUploadArray.delete(UPLOAD_VIDEO);
        SendNetRequestManager requestManager = new SendNetRequestManager(requestListener);
        LoadController loadController = requestManager.uploadImg(filePath, 1, UPLOAD_VIDEO);
        mUploadArray.put(UPLOAD_VIDEO, loadController);
        this.takeVideo = true;
//        RecordPreviewActivity.getInstance().saveData();

    }


    private RequestManager.RequestListener requestListener = new RequestManager.RequestListener() {
        @Override
        public void onRequest() {
            mView.showUploadingProgress(0);
        }

        @Override
        public void onLoading(long total, long count, String filePath) {
            int progress = (int) ((float) count / (float) total * 100f);
        }

        @Override
        public void onSuccess(String response, Map<String, String> headers,
                              String url, int actionId) {
            mView.dismissUploadingProgress();
            mUploadArray.remove(actionId);
            UploadResponse res = JsonUtils.fromJson(response, UploadResponse.class);
            if (COMMON_SUCCESS.equals(res.getCode())) {
                switch (actionId) {
                    case UPLOAD_IMG_REQUEST:
                        videoThumbnail = res.getFileUrl();
                        KLog.v("UPLOAD_IMG_REQUEST" + videoThumbnail);
                        break;
                    case UPLOAD_VIDEO_IMG:

                        videoThumbnail = res.getFileUrl();
                        KLog.v("UPLOAD_VIDEO_IMG" + videoThumbnail);

                        break;
                    case UPLOAD_VIDEO:
                        KLog.v("UPLOAD_VIDEO ====" + playUrl);
                        lastTime = res.getVideoTime();
                        playUrl = res.getFileUrl();
                        break;
                }
            }
        }

        @Override
        public void onError(String s, String s1, int i) {

        }
    };

    public void saveBehaviour(String functionCode) {
        SaveBehaviourDataService.startAction(mView.getContext(),
                BehaviourEnum.SHAKE_STAR_PREVIEW.getCode() + functionCode);
    }

    public void saveBehaviour(String functionCode, Object request, String header) {
        SaveBehaviourDataService.startAction(mView.getContext(),
                BehaviourEnum.SHAKE_STAR_PREVIEW.getCode() + functionCode, request, header);
    }

}
