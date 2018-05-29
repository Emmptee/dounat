package com.donut.app.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.donut.app.SysApplication;
import com.donut.app.config.Constant;
import com.donut.app.entity.UserInfo;
import com.donut.app.model.upload.UploadManager;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

import java.util.List;

public class UploadService extends Service {

    private static UploadManager UPLOAD_MANAGER;

    private static String userId;

    public static UploadManager getUploadManager() {
        Context appContext = SysApplication.getInstance();

        UserInfo userInfo = SysApplication.getUserInfo();

        if (!UploadService.isServiceRunning(appContext)) {
            Intent downloadSvr = new Intent("upload.service.action");
            downloadSvr.setPackage(Constant.PACKAGE_NAME);
            appContext.startService(downloadSvr);
        }
        if (UPLOAD_MANAGER == null) {
            UPLOAD_MANAGER = new UploadManager(userInfo.getUserId(), userInfo.getToken());
        }
        if (userId == null) {
            userId = userInfo.getUserId();
        } else if (!userId.equals(userInfo.getUserId())) {
            userId = userInfo.getUserId();
            UPLOAD_MANAGER.setUploadInfoMap(userInfo.getUserId(), userInfo.getToken());
        }
        return UPLOAD_MANAGER;
    }

    @Override
    public void onDestroy() {
        if (UPLOAD_MANAGER != null) {
            try {
                UPLOAD_MANAGER.stopAllUpload();
                UPLOAD_MANAGER.backupUploadInfoMap();
                UPLOAD_MANAGER = null;
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
        }
        super.onDestroy();
    }

    public UploadService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public static boolean isServiceRunning(Context context) {
        boolean isRunning = false;

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(Integer.MAX_VALUE);

        if (serviceList == null || serviceList.size() == 0) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(
                    UploadService.class.getName())) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

}
