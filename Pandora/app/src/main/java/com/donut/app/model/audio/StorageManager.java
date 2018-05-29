package com.donut.app.model.audio;

import android.content.Context;

import com.android.volley.manager.LoadController;
import com.android.volley.manager.RequestManager;
import com.android.volley.model.FormFile;
import com.donut.app.SysApplication;
import com.donut.app.entity.AudioInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.SendNetRequestManager;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.StarCommentAddRequest;
import com.donut.app.http.message.UploadResponse;
import com.donut.app.utils.FileUtils;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.L;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Qi on 2016/8/15.
 */
public class StorageManager {

    private Context context;

    private LoadController mLoadController = null;

    private float seconds;

    private String fkB02;

    private RequestManager.RequestListener saveInfoListener, getInfoListener;

    public StorageManager(Context context) {
        this.context = context;
    }

    public void saveInfo(float seconds, String filePath,String fkB02, RequestManager.RequestListener listener) {
        this.seconds = seconds;
        this.fkB02 = fkB02;
        this.saveInfoListener = listener;
        if (listener == null) {
            return;
        }
        listener.onRequest();
        if (filePath == null || "".equals(filePath)) {
            saveInfoListener.onError("文件地址不能为空", "", 400);
            return;
        }
        File file = new File(filePath);

        Map<String, String> params = new HashMap<>();
        params.put("uploadType", "5");
        params.put("fileName", file.getName());

        FormFile[] files = new FormFile[1];
        files[0] = new FormFile(file.getName(), file, "file", null);

        mLoadController = RequestManager.getInstance().upLoadRequest(RequestUrl.REQUEST_UPLOAD_URL,
                params, new RequestListener(filePath), files, 999);
    }

    public void getInfo(String fileUrl, float seconds, RequestManager.RequestListener listener) {
        this.getInfoListener = listener;
        if (listener == null) {
            return;
        }
        listener.onRequest();
        this.seconds = seconds;
        if (fileUrl == null || "".equals(fileUrl)) {
            listener.onError("文件url不能为空", "", 400);
            return;
        } else if (!fileUrl.startsWith("http")) {
            // 非网络地址,直接获取文件地址
            getFilePath(fileUrl, "");
        }
        try {
            AudioInfo audioInfo = SysApplication.getDb().findFirst(
                    Selector.from(AudioInfo.class).expr("fileUrl", "=", fileUrl));

            String filePath = "";
            if (audioInfo != null && audioInfo.getFilePath() != null) {
                //保存过音频文件
                filePath = audioInfo.getFilePath();
            } else {
                // 未保存过音频文件
                filePath = FileUtils.getCachePath(context, "audio")
                        + fileUrl.substring(fileUrl.lastIndexOf(File.separator));
            }
            getFilePath(filePath, fileUrl);

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private String getFilePath(String filePath, String fileUrl) {
        if (getInfoListener == null) {
            return "";
        }

        File file = new File(filePath);
        if (file.exists()) {
            // 文件未被删除
            getInfoListener.onSuccess(file.getAbsolutePath(), null, "", 200);
            return file.getAbsolutePath();
        } else if (fileUrl != null && !"".equals(fileUrl)) {
            //文件已被删除,重新从网络下载
            try {
                SysApplication.getDb().delete(AudioInfo.class, WhereBuilder.b("fileUrl", "=", fileUrl));
            } catch (DbException e) {
                e.printStackTrace();
            }
            //RequestManager.getInstance().get(fileUrl, requestListener, 1);
            new HttpUtils().download(fileUrl, filePath, true,
                    true, new CallBack(filePath, fileUrl));

            return filePath;
        } else {
            return "";
        }
    }

    public void cancel() {
        if (mLoadController != null) {
            mLoadController.cancel();
            mLoadController = null;
        }
    }

    private class CallBack extends RequestCallBack<File> {
        private String filePath, fileUrl;

        public CallBack(String filePath, String fileUrl) {
            this.filePath = filePath;
            this.fileUrl = fileUrl;
        }


        @Override
        public void onSuccess(ResponseInfo<File> responseInfo) {
            getInfoListener.onSuccess(filePath, null, "", 200);
            AudioInfo audioInfo = new AudioInfo();
            audioInfo.setFilePath(filePath);
            audioInfo.setFileUrl(fileUrl);
            audioInfo.setSeconds(seconds);
            try {
                SysApplication.getDb().save(audioInfo);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            L.e("====", "onFailure:" + s);
            getInfoListener.onError(s, "", 400);
        }
    }

    private class RequestListener implements RequestManager.RequestListener {

        private String filePath;

        public RequestListener(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public void onRequest() {
        }

        @Override
        public void onLoading(long total, long count, String filePath) {
        }

        @Override
        public void onSuccess(String response, Map<String, String> headers,
                              String url, int actionId) {
            if (999 == actionId) {
                UploadResponse res = JsonUtils.fromJson(response, UploadResponse.class);
                if ("0000".equals(res.getCode())) {

                    StarCommentAddRequest request = new StarCommentAddRequest();
                    request.setAudioUrl(res.getFileUrl());
                    request.setFkB02(fkB02);
                    request.setLastTime((long) seconds);

                    SendNetRequestManager manager = new SendNetRequestManager(
                            new RequestListener(""));
                    manager.sendNetRequest(request, HeaderRequest.STAR_COMMENT_ADD, 2333);

                    AudioInfo audioInfo = new AudioInfo();
                    audioInfo.setFilePath(filePath);
                    audioInfo.setFileUrl(res.getFileUrl());
                    audioInfo.setSeconds(seconds);
                    try {
                        SysApplication.getDb().save(audioInfo);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            } else if(2333 == actionId) {
                BaseResponse baseResponse = JsonUtils.fromJson(response, BaseResponse.class);
                if("0000".equals(baseResponse.getCode())) {
                    //
                    saveInfoListener.onSuccess("点评成功", null, "", 200);
                } else {
                    saveInfoListener.onError(baseResponse.getMsg(), "", Integer.valueOf(baseResponse.getCode()));
                }
            }
        }

        @Override
        public void onError(String errorMsg, String url, int actionId) {
            if(2333 == actionId) {
                saveInfoListener.onError(errorMsg, url, actionId);
            }
        }
    }

}
