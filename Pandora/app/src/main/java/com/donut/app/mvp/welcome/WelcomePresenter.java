package com.donut.app.mvp.welcome;

import android.support.annotation.UiThread;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.donut.app.BuildConfig;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.UpdateInfoRequest;
import com.donut.app.http.message.UpdateInfoResponse;
import com.donut.app.http.message.WelcomeADResponse;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.AppConfigUtil;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.UpdateAppUtil;
import com.donut.app.utils.VersionUtil;

import java.util.List;
import java.util.UUID;


/**
 * Created by Qi on 2017/3/21.
 * Description : <br>
 */
public class WelcomePresenter extends WelcomeContract.Presenter implements UpdateAppUtil.OnBtnClickListener {

    private static final int AD_LIST = 3, UPDATE_RESULT = 4;

    private boolean loadAd;

    public void initData() {
        if (mView == null) {
            return;
        }
        PushManager.startWork(mView.getContext(), PushConstants.LOGIN_TYPE_API_KEY,
                BuildConfig.BaiduPushKey);
        //启动用户行为数据收集Service
        new AppConfigUtil(mView.getContext()).setStarCode(UUID.randomUUID().toString());
        new AppConfigUtil(mView.getContext()).setStarTime(System.currentTimeMillis());
        SaveBehaviourDataService.startAction(mView.getContext(),
                AppConfigUtil.getBehaviourHeader() + "99999");
    }

    void loadUpdateInfo(boolean loadAd) {
        this.loadAd = loadAd;
        if (mView == null) {
            return;
        }
        UpdateInfoRequest infoRequest = new UpdateInfoRequest();
        infoRequest.setVersionCode(String.valueOf(VersionUtil.getVersionCode(mView.getContext())));
        super.loadData(infoRequest, HeaderRequest.UPDATE, UPDATE_RESULT, false);
    }

    private void loadAdData() {
        super.loadData(new Object(), HeaderRequest.AD_LIST, AD_LIST, false);
    }

    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        if (mView == null) {
            return;
        }
        switch (actionId) {
            case AD_LIST:
                WelcomeADResponse adResponse
                        = JsonUtils.fromJson(responseJson, WelcomeADResponse.class);
                if (COMMON_SUCCESS.equals(adResponse.getCode())) {
                    List<WelcomeADResponse.ADItem> adList = adResponse.getStartPageList();
                    if (adList != null && adList.size() > 0) {
                        mView.showAdView(adList);
                    } else {
                        mView.showWelcomeView(true);
                    }
                } else {
                    mView.showWelcomeView(true);
                }
                break;
            case UPDATE_RESULT:
                UpdateInfoResponse infoResponse = JsonUtils.fromJson(responseJson,
                        UpdateInfoResponse.class);
                if ("0500".equals(infoResponse.getCode())) {
                    //最新版本
                    if (loadAd) {
                        loadAdData();
                    } else {
                        mView.showWelcomeView(true);
                    }
                } else if ("0000".equals(infoResponse.getCode())) {
                    String versionName = null;
                    try {
                        String versionCode = infoResponse.getVersionCode();
                        String bigVersion = versionCode.substring(4, 6);
                        if (bigVersion.startsWith("0")) {
                            bigVersion = versionCode.substring(5, 6);
                        } else {
                            bigVersion = versionCode.substring(4, 5)
                                    + "."
                                    + versionCode.substring(5, 6);
                        }
                        versionName = bigVersion
                                + "."
                                + versionCode.substring(6, 7)
                                + "."
                                + versionCode.substring(7, 8);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    new UpdateAppUtil.Builder(mView.getContext(), infoResponse.getFileUrl())
                            .description(infoResponse.getDescription())
                            .fileName(infoResponse.getFileName())
                            .force(infoResponse.getForceUpdateFlag() == 1)
                            .versionName(versionName)
                            .fileSize(infoResponse.getFileSize())
                            .setListener(this)
                            .build()
                            .update();
                } else {
                    //出错
                    if (loadAd) {
                        loadAdData();
                    } else {
                        mView.showWelcomeView(true);
                    }
                }
                break;
        }
    }

    @UiThread
    @Override
    public void onError(String errorMsg, String url, int actionId) {
        if (mView == null) {
            return;
        }
        super.onError(errorMsg, url, actionId);
        switch (actionId) {
            case AD_LIST:
                mView.showWelcomeView(false);
                break;
            case UPDATE_RESULT:
                if (loadAd) {
                    loadAdData();
                } else {
                    mView.showWelcomeView(true);
                }
                break;
        }
    }

    @Override
    public void onBtnClick(boolean force, UpdateAppUtil.BtnType type) {
        switch (type) {
            case DOWNLOAD:
                break;
            case DELAY:
                loadAdData();
                break;
            case STORE:
                if (force && mView != null) {
                    mView.exitView();
                } else {
                    loadAdData();
                }
                break;
            case BACKGROUND:
                loadAdData();
                break;
            case CANCEL:
                loadAdData();
                break;
            case INSTALL:
                break;
        }
    }

    void gotoHomePage() {
        if (mView != null) {
            mView.gotoHomeView();
        }
    }
}
