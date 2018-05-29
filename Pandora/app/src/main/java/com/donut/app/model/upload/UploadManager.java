package com.donut.app.model.upload;

import android.database.Cursor;

import com.android.volley.manager.LoadController;
import com.android.volley.manager.RequestManager;
import com.android.volley.model.FormFile;
import com.bis.android.plug.ffmpeg4android.FFmpegProcessed;
import com.donut.app.R;
import com.donut.app.SysApplication;
import com.donut.app.entity.UploadInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.BaseRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.ChallengeRequest;
import com.donut.app.http.message.IpAddRequest;
import com.donut.app.http.message.StarNoticeAddRequest;
import com.donut.app.http.message.UploadResponse;
import com.donut.app.http.message.wish.WishCompletedRequest;
import com.donut.app.utils.FileUtils;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.NetUtils;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.db.converter.ColumnConverter;
import com.lidroid.xutils.db.sqlite.ColumnDbType;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Author: Qi Date: 16-09-20
 */
public class UploadManager {

    private Map<String, UploadInfo> uploadInfoMap = new HashMap<>();

    public UploadManager(String userId, String token) {
//        ColumnConverterFactory.registerColumnConverter(HttpHandler.State.class,
//                new HttpHandlerStateConverter());
        setUploadInfoMap(userId, token);
    }

    public void setUploadInfoMap(String userId, String token) {
        uploadInfoMap.clear();
        if (userId == null || "".equals(userId)) {
            return;
        }

        try {
            List<UploadInfo> uploadInfoList = SysApplication.getDb()
                    .findAll(Selector.from(UploadInfo.class).where("userId", "=", userId));

            if (uploadInfoList != null) {
                for (UploadInfo uploadInfo : uploadInfoList) {
                    uploadInfo.setState(4);
                    uploadInfo.setToken(token);
                    uploadInfoMap
                            .put(uploadInfo.getOutFilePath(), uploadInfo);
                }
                SysApplication.getDb().updateAll(uploadInfoList, "state", "token");
            }

        } catch (DbException e) {
            LogUtils.e(e.getMessage(), e);
        }
    }

    /**
     * @return uploadInfoMap
     */
    public Map<String, UploadInfo> getUploadInfoMap() {
        return uploadInfoMap;
    }

    public int getUploadInfoMapCount() {
        return uploadInfoMap.size();
    }

    public UploadInfo getUploadInfo(String outFilePath) {
        return uploadInfoMap.get(outFilePath);
    }

    public void setUploadInfo(UploadInfo info) {

        UploadInfo uploadInfo = getUploadInfo(info.getOutFilePath());
        if (uploadInfo == null) {
            return;
        }
        uploadInfo.setId(info.getId());
        uploadInfo.setHandler(info.getHandler());
        uploadInfo.setState(info.getState());
        uploadInfo.setFilePath(info.getFilePath());
        uploadInfo.setOutFilePath(info.getOutFilePath());
        uploadInfo.setFileName(info.getFileName());
        uploadInfo.setProgress(info.getProgress());
        uploadInfo.setFileLength(info.getFileLength());
        uploadInfo.setForceUpdateFlag(info.getForceUpdateFlag());
        uploadInfo.setVersionCode(info.getVersionCode());
        uploadInfo.setDescription(info.getDescription());
        uploadInfo.setCreateTime(info.getCreateTime());
        uploadInfo.setUserId(info.getUserId());
        uploadInfo.setToken(info.getToken());
        uploadInfo.setSaveTitle(info.getSaveTitle());
        uploadInfo.setSaveType(info.getSaveType());
        uploadInfo.setSaveData(info.getSaveData());
        uploadInfo.setBaseCallBack(info.getBaseCallBack());
    }

    public void addNewUpload(String filePath, String userId, String token, UploadInfo.SaveTypeEnum saveType,
                             String saveTitle, String saveData) throws DbException {
        String fileName = UUID.randomUUID().toString().replace("-", "") + ".mp4";
        String outFilePath = FileUtils.getCachePath(SysApplication.getInstance(), "video") + File.separator + fileName;

        UploadInfo uploadInfo = new UploadInfo();
        uploadInfo.setFilePath(filePath);
        uploadInfo.setOutFilePath(outFilePath);
        uploadInfo.setFileLength(new File(filePath).length());
        uploadInfo.setFileName(fileName);
        uploadInfo.setUserId(userId);
        uploadInfo.setToken(token);
        uploadInfo.setSaveType(saveType.getType());
        uploadInfo.setSaveTitle(saveTitle);
        uploadInfo.setSaveData(saveData);
        uploadInfo.setState(0);

        uploadInfoMap.put(outFilePath, uploadInfo);
        try {
            SysApplication.getDb().saveBindingId(uploadInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
        makeThread(uploadInfo);
    }

    public void resumeUpload(String filePath) throws DbException {
        if (!NetUtils.isNetworkConnected(SysApplication.getInstance())) {
            ToastUtil.showShort(SysApplication.getInstance(),
                    SysApplication.getInstance().getString(R.string.connect_no));
            return;
        }
        resumeUpload(getUploadInfo(filePath));
    }

    private void resumeUpload(UploadInfo uploadInfo) throws DbException {
        uploadInfo.setState(0);
        SysApplication.getDb().update(uploadInfo);
        makeThread(uploadInfo);
    }

    private Thread thread;

    private Thread getThread() {

        if (thread == null) {
            thread = new Thread(new makeVideoRunnable());
        }

        if (!thread.isAlive()) {
            //Log.e("====","thread:"+thread.getState());
//            if(thread.getState() == Thread.State.TERMINATED){
//                thread.re
//            }
            if(thread.getState() == Thread.State.NEW){
                thread.start();
            } else {
                thread = new Thread(new makeVideoRunnable());
                thread.start();
            }
        }

        return thread;
    }

    private LinkedList<UploadInfo> uploadInfoList = new LinkedList<>();

    private void makeThread(UploadInfo uploadInfo) {
        uploadInfoList.add(uploadInfo);
        getThread();
    }

    private class makeVideoRunnable implements Runnable {

        @Override
        public void run() {

            while (uploadInfoList.size() > 0) {
                try {
                    UploadInfo uploadInfo = uploadInfoList.get(0);
                    String filePath = uploadInfo.getFilePath();
                    String outFilePath = uploadInfo.getOutFilePath();
                    uploadInfo.setState(1);

                    if (uploadInfo.getId() == 0) {
                        long id = SysApplication.getDb().findFirst(
                                Selector.from(uploadInfo.getClass())
                                        .where("outFilePath", "=", outFilePath));
                        uploadInfo.setId(id);
                    }
                    setUploadInfo(uploadInfo);
                    SysApplication.getDb().update(uploadInfo);

                    File inFile = new File(filePath);
                    File outFile = new File(outFilePath);

                    if (!outFile.exists()
                            || outFile.length() <= 100) {
                        FFmpegProcessed.processedVideo(filePath, outFilePath);
                    }
                    if (uploadInfoMap.get(uploadInfo.getOutFilePath()) != null) {
                        if (!outFile.exists()
                                || outFile.length() <= 100
                                || (inFile.getName().endsWith("mp4") && outFile.length() > inFile.length())) {

                            String oldFileName = outFile.getName();
                            String newFileName = oldFileName.substring(0, oldFileName.lastIndexOf("."));
                            String srcFileName = inFile.getName();
                            outFile = new File(outFile.getParent(), newFileName + srcFileName.substring(srcFileName.lastIndexOf(".")));

                            if (!outFile.getName().endsWith("mp4")) {
                                uploadInfo.setState(4);
                                try {
                                    setUploadInfo(uploadInfo);
                                    SysApplication.getDb().update(uploadInfo);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                                return;
                            }

                            FileUtils.copyFile(inFile, outFile);
                            uploadInfo.setOutFilePath(outFile.getAbsolutePath());
                            uploadInfoMap.remove(outFilePath);
                            uploadInfoMap.put(outFile.getAbsolutePath(), uploadInfo);
                        }
                        if (uploadInfoMap.get(uploadInfo.getOutFilePath()) != null) {
                            uploadInfo.setFileLength(outFile.length());
                            LoadController loadController = getLoadController(outFile);
                            uploadInfo.setHandler(loadController);
                            uploadInfo.setState(2);

                            setUploadInfo(uploadInfo);
                            SysApplication.getDb().update(uploadInfo);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                uploadInfoList.remove(0);
                Thread.currentThread().interrupt();
            }

        }
    }

    private LoadController getLoadController(File file) {
        String fileName = file.getName();
        Map<String, String> params = new HashMap<String, String>();
        params.put("uploadType", "1");
        params.put("fileName", fileName);
        FormFile[] files = new FormFile[1];
        files[0] = new FormFile(fileName, file, "file", null);
        return RequestManager.getInstance().upLoadRequest(
                RequestUrl.REQUEST_UPLOAD_URL, params, new UploadRequestListener(file.getAbsolutePath()), files, 7777);
    }


    public void removeUpload(String outFilePath) throws DbException {
        removeUpload(getUploadInfo(outFilePath));
    }

    public void removeUpload(UploadInfo uploadInfo) throws DbException {
        if (uploadInfo != null) {
            LoadController handler = uploadInfo.getHandler();
            if (handler != null) {
                handler.cancel();
            }
            uploadInfoMap.remove(uploadInfo.getOutFilePath());
            SysApplication.getDb().delete(uploadInfo);
            if (uploadInfo.getOutFilePath() != null) {
                File file = new File(uploadInfo.getOutFilePath());
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    public void stopAllUpload() throws DbException {

        List<UploadInfo> uploadInfoList = new ArrayList<UploadInfo>();

        for (UploadInfo uploadInfo : uploadInfoMap.values()) {
            LoadController handler = uploadInfo.getHandler();
            if (handler != null) {
                handler.cancel();
            } else {
                uploadInfo.setState(5);
            }
            uploadInfoList.add(uploadInfo);
        }
        if (uploadInfoList.size() > 0) {
            SysApplication.getDb().saveOrUpdateAll(uploadInfoList);
        }
    }

    public void backupUploadInfoMap() throws DbException {
        List<UploadInfo> uploadInfoList = new ArrayList<UploadInfo>();
        for (UploadInfo uploadInfo : uploadInfoMap.values()) {
            LoadController handler = uploadInfo.getHandler();
//            if (handler != null) {
//                uploadInfo.setState(handler.getState());
//            }
            uploadInfoList.add(uploadInfo);
        }
        if (uploadInfoList.size() > 0) {
            SysApplication.getDb().saveOrUpdateAll(uploadInfoList);
        }
    }

    private class HttpHandlerStateConverter implements
            ColumnConverter<HttpHandler.State> {

        @Override
        public HttpHandler.State getFieldValue(Cursor cursor, int index) {
            return HttpHandler.State.valueOf(cursor.getInt(index));
        }

        @Override
        public HttpHandler.State getFieldValue(String fieldStringValue) {
            if (fieldStringValue == null) {
                return null;
            }
            return HttpHandler.State.valueOf(fieldStringValue);
        }

        @Override
        public Object fieldValue2ColumnValue(HttpHandler.State fieldValue) {
            return fieldValue.value();
        }

        @Override
        public ColumnDbType getColumnDbType() {
            return ColumnDbType.INTEGER;
        }
    }

    private class UploadRequestListener implements RequestManager.RequestListener {

        private String outFilePath;

        UploadRequestListener(String filePath){
            this.outFilePath = filePath;
        }

        @Override
        public void onRequest() {
        }

        int oldProgress = 0;
        long time = 0;

        @Override
        public void onLoading(long total, long count, String filePath) {
            if (System.currentTimeMillis() - time <= 300) {
                return;
            }
            UploadInfo uploadInfo = getUploadInfo(outFilePath);
            if (uploadInfo == null) {
                return;
            }

            int progress = (int) ((float) count / (float) total * 100f);
            if (count != total && count % 10 != 0 && oldProgress == progress) {
                //优化处理
                return;
            }
            oldProgress = progress / 2 + 50;
            if (oldProgress >= 100) {
                oldProgress = 99;
            }
            uploadInfo.setProgress(oldProgress);
            try {
                setUploadInfo(uploadInfo);
                SysApplication.getDb().update(uploadInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (uploadInfo.getBaseCallBack() != null) {
                uploadInfo.getBaseCallBack().onLoading(total, count, filePath);
            }
            time = System.currentTimeMillis();
        }

        @Override
        public void onSuccess(String response, Map<String, String> headers,
                              String url, int actionId) {
            UploadInfo uploadInfo = getUploadInfo(outFilePath);
            if (uploadInfo == null) {
                return;
            }
            UploadResponse res = JsonUtils.fromJson(response, UploadResponse.class);
            if ("0000".equals(res.getCode())) {

                if (!NetUtils.isNetworkConnected(SysApplication.getInstance())) {
                    return;
                }

                BaseRequest baseRequest = new BaseRequest();
                JSONObject jsonObject = null;
                int requestCode = 0;

                if (uploadInfo.getSaveType() == UploadInfo.SaveTypeEnum.IP_SEND.getType()) {
                    IpAddRequest request = JsonUtils.fromJson(uploadInfo.getSaveData(), IpAddRequest.class);
                    request.setPlayUrl(res.getFileUrl());
                    Long time = res.getVideoTime();
                    if (time != null && time != 0) {
                        request.setLastTime(time);
                    }
                    requestCode = 100;

                    baseRequest.setHeader(HeaderRequest.SAVE_IP_COLLECTION_REQUEST);
                    baseRequest.setUserId(uploadInfo.getUserId());
                    baseRequest.setToken(uploadInfo.getToken());
                    baseRequest.setData(JsonUtils.toJson(request, request.getClass()));

                    //SendNetRequestManager sendNet = new SendNetRequestManager(mContext, new PriRequestListener(uploadInfo));
                    //sendNet.sendNetRequest(request, HeaderRequest.SAVE_IP_COLLECTION_REQUEST, 100);
                } else if (uploadInfo.getSaveType() == UploadInfo.SaveTypeEnum.CHALLENGE.getType()) {
                    ChallengeRequest request = JsonUtils.fromJson(uploadInfo.getSaveData(), ChallengeRequest.class);
                    request.setPlayUrl(res.getFileUrl());
                    Long time = res.getVideoTime();
                    if (time != null && time != 0) {
                        request.setLastTime(time.intValue());
                    }
                    requestCode = 200;

                    baseRequest.setHeader(HeaderRequest.SAVE_CHALLENGE);
                    baseRequest.setUserId(uploadInfo.getUserId());
                    baseRequest.setToken(uploadInfo.getToken());
                    baseRequest.setData(JsonUtils.toJson(request, request.getClass()));
//                    SendNetRequestManager sendNet = new SendNetRequestManager(mContext, new PriRequestListener(uploadInfo));
//                    sendNet.sendNetRequest(request, HeaderRequest.SAVE_CHALLENGE, 200);
                } else if (uploadInfo.getSaveType() == UploadInfo.SaveTypeEnum.STAR_REPLY.getType()) {
                    WishCompletedRequest request
                            = JsonUtils.fromJson(uploadInfo.getSaveData(), WishCompletedRequest.class);
                    request.setAchieveVideoUrl(res.getFileUrl());

                    requestCode = 300;

                    baseRequest.setHeader(HeaderRequest.WISH_REPLY);
                    baseRequest.setUserId(uploadInfo.getUserId());
                    baseRequest.setToken(uploadInfo.getToken());
                    baseRequest.setData(JsonUtils.toJson(request, request.getClass()));
                } else if (uploadInfo.getSaveType() == UploadInfo.SaveTypeEnum.STAR_SEND_NOTICE.getType()) {
                    StarNoticeAddRequest request
                            = JsonUtils.fromJson(uploadInfo.getSaveData(), StarNoticeAddRequest.class);
                    request.setPlayUrl(res.getFileUrl());

                    requestCode = 400;

                    baseRequest.setHeader(HeaderRequest.STAR_NOTICE_ADD);
                    baseRequest.setUserId(uploadInfo.getUserId());
                    baseRequest.setToken(uploadInfo.getToken());
                    baseRequest.setData(JsonUtils.toJson(request, request.getClass()));
                }

                try {
                    jsonObject = new JSONObject(JsonUtils.toJson(baseRequest,
                            BaseRequest.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestManager.getInstance().jsonObjectRequest(
                        RequestUrl.REQUEST_URL_W, jsonObject, new PriRequestListener(uploadInfo),
                        true, 10 * 1000, 2, requestCode);

            } else {
                uploadInfo.setState(4);
                try {
                    setUploadInfo(uploadInfo);
                    SysApplication.getDb().saveOrUpdate(uploadInfo);
                } catch (DbException e) {
                    LogUtils.e(e.getMessage(), e);
                }
                if (uploadInfo.getBaseCallBack() != null) {
                    uploadInfo.getBaseCallBack().onError("", url, actionId);
                }
            }
        }

        @Override
        public void onError(String errorMsg, String url, int actionId) {
            LogUtils.e("onError:" + errorMsg);
            UploadInfo uploadInfo = getUploadInfo(outFilePath);
            if (uploadInfo == null) {
                return;
            }
            uploadInfo.setState(4);
            if (uploadInfo.getProgress() == 100) {
                uploadInfo.setProgress(99);
            }
            try {
                setUploadInfo(uploadInfo);
                SysApplication.getDb().update(uploadInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (uploadInfo.getBaseCallBack() != null) {
                uploadInfo.getBaseCallBack().onError("", url, actionId);
            }
        }
    }

    private class PriRequestListener implements RequestManager.RequestListener {
        UploadInfo uploadInfo;

        private PriRequestListener(UploadInfo uploadInfo) {
            this.uploadInfo = uploadInfo;
        }

        @Override
        public void onRequest() {
        }

        @Override
        public void onLoading(long l, long l1, String s) {
        }

        @Override
        public void onSuccess(String response, Map<String, String> headers, String url, int actionId) {
            BaseResponse saveRes = JsonUtils.fromJson(response, BaseResponse.class);
            if ("0000".equals(saveRes.getCode())) {
                // uploadInfo.setState(HttpHandler.State.SUCCESS);
                try {
                    removeUpload(uploadInfo);
                } catch (DbException e) {
                    LogUtils.e(e.getMessage(), e);
                }
                if (uploadInfo.getBaseCallBack() != null) {
                    uploadInfo.getBaseCallBack().onSuccess(response, headers, url, actionId);
                }
            }
        }

        @Override
        public void onError(String s, String s1, int i) {
        }
    }
}
