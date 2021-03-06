package com.donut.app.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.android.volley.manager.LoadController;
import com.android.volley.manager.RequestManager;
import com.donut.app.SysApplication;
import com.donut.app.config.Constant;
import com.donut.app.entity.ConfigInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.SendNetRequestManager;
import com.donut.app.http.message.ConfigItemResponse;
import com.donut.app.http.message.DeviceRequest;
import com.donut.app.http.message.PresentGetResponse;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.L;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 请求初始化数据.
 */
public class LoadInitDataService extends IntentService implements RequestManager.RequestListener {

    private static final int PRESENT_GET = 1, CONFIG_REQUEST = 2,
            UPLOAD_DEVICE = 5;

    private static final String ACTION_OTHER = "ACTION_OTHER", ACTION_DEVICE = "ACTION_DEVICE";

    private static final String DEVICE_ID = "DEVICE_ID";

    private SharedPreferences sp_Info;

    public LoadInitDataService() {
        super("LoadInitDataService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionOther(Context context) {
        Intent intent = new Intent(context, LoadInitDataService.class);
        intent.setAction(ACTION_OTHER);
        context.startService(intent);
    }

    public static void startActionDevice(Context context, String deviceId) {
        Intent intent = new Intent(context, LoadInitDataService.class);
        intent.setAction(ACTION_DEVICE);
        intent.putExtra(DEVICE_ID, deviceId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            sp_Info = getSharedPreferences(Constant.SP_INFO,
                    Context.MODE_PRIVATE);
            final String action = intent.getAction();
            if (ACTION_OTHER.equals(action)) {
                loadOtherData();
            } else if (ACTION_DEVICE.equals(action)) {
                uploadDevice(intent.getStringExtra(DEVICE_ID));
            }
        }
    }

    public void loadData(Object request, final String header,
                         final int requestCode) {
        SendNetRequestManager requestManager = new SendNetRequestManager(this);
        LoadController loadController = requestManager.sendNetRequest(request,
                header, requestCode);
    }

    void loadOtherData() {
        loadData(new Object(), HeaderRequest.PRESENT_GET, PRESENT_GET);
        loadData(new Object(), HeaderRequest.CONFIG_REQUEST, CONFIG_REQUEST);
    }

    void uploadDevice(String deviceId) {
        DeviceRequest deviceRequest = new DeviceRequest();
        deviceRequest.setDeviceId(deviceId);
        deviceRequest.setPhoneType(android.os.Build.MODEL);
        loadData(deviceRequest, HeaderRequest.UPLOAD_DEVICE, UPLOAD_DEVICE);
    }

    public void savePrice(PresentGetResponse response) {
        sp_Info.edit()
                .putFloat(Constant.COMMENT_PRICE, response.getCommentPrice())
                .putFloat(Constant.RATIO, (float) response.getRatio())
                .putFloat(Constant.RECHARGR_RATIO, response.getRechargeRatio())
                .apply();
    }

    public void hasUploadDevice() {
        sp_Info.edit().putBoolean(Constant.DEVICE, true).apply();
    }

    @Override
    public void onRequest() {
    }

    @Override
    public void onLoading(long total, long count, String filePath) {

    }

    @Override
    public void onSuccess(String responseJson, Map<String, String> headers,
                          String url, int actionId) {
        switch (actionId) {
            case PRESENT_GET:
                PresentGetResponse getResponse
                        = JsonUtils.fromJson(responseJson, PresentGetResponse.class);
                if ("0000".equals(getResponse.getCode())) {
                    savePrice(getResponse);
                }
                break;
            case CONFIG_REQUEST:
                ConfigItemResponse configItemResponse
                        = JsonUtils.fromJson(responseJson, ConfigItemResponse.class);
                if ("0000".equals(configItemResponse.getCode())) {
                    List<ConfigItemResponse.ConfigItem> configList
                            = configItemResponse.getConfigList();
                    if (configList != null && configList.size() > 0) {
                        try {
                            SysApplication.getDb().deleteAll(ConfigInfo.class);
                            List<ConfigInfo> infos = new ArrayList<>();
                            for (ConfigItemResponse.ConfigItem item : configList) {
                                ConfigInfo info = new ConfigInfo();
                                info.setName(item.getName());
                                info.setValue(item.getValue());
                                infos.add(info);
                                if ("UNFINISH_TIPS".equals(item.getName())) {
                                    Constant.DEFAULT_TIPS_MSG = item.getValue();
                                }
                            }
                            SysApplication.getDb().saveAll(infos);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case UPLOAD_DEVICE:
                hasUploadDevice();
                break;
        }
    }

    @Override
    public void onError(String errorMsg, String url, int actionId) {
        L.e("====", "oooo"+errorMsg);
    }
}
