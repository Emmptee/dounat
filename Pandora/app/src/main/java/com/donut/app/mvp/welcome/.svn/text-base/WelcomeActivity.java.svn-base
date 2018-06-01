package com.donut.app.mvp.welcome;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.SysApplication;
import com.donut.app.activity.HelloActivity;
import com.donut.app.activity.HomeActivity;
import com.donut.app.activity.WebAdActivity;
import com.donut.app.config.Constant;
import com.donut.app.databinding.WelcomeLayoutBinding;
import com.donut.app.entity.ConfigInfo;
import com.donut.app.http.message.WelcomeADResponse;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.service.LoadInitDataService;
import com.donut.app.utils.VersionUtil;
import com.donut.app.utils.status_bar.StatusBarCompat;
import com.lidroid.xutils.db.sqlite.Selector;

import java.util.List;

/**
 * Created by Qi on 2017/3/21.
 * Description : <br>
 */
public class WelcomeActivity extends MVPBaseActivity<WelcomeLayoutBinding, WelcomePresenter>
        implements WelcomeContract.View {

    public static final String NOTICE_JSON_CONTENT = "NOTICE_JSON_CONTENT";

    private static final int HELLO_REQUEST_CODE = 0x01;

    private int nowAdIndex = 0;

    private List<WelcomeADResponse.ADItem> adList;

    @Override
    protected int getLayoutId() {
        return R.layout.welcome_layout;
    }

    @Override
    protected void initView() {
        StatusBarCompat.translucentStatusBar(this);

        int w = getResources().getDisplayMetrics().widthPixels;
        ViewGroup.LayoutParams params = mViewBinding.welcomeImgLayout.getLayoutParams();
        params.height = w * 1050 / 750;
        mViewBinding.welcomeImgLayout.setLayoutParams(params);
        mViewBinding.welcomeBottomLayout.setBackgroundResource(R.drawable.iv_welcome_bottom);
        mPresenter.initData();
    }

    @Override
    protected void initEvent() {
        mViewBinding.tvWelcomeGoto.setOnClickListener(this);
        mViewBinding.welcomeImgFlipper.setOnClickListener(this);
    }

    @Override
    protected void loadData() {

        if (sp_Info.getInt(Constant.NOW_VERSION_CODE, 0)
                != VersionUtil.getVersionCode(this)) {
            //向导页面
            launchActivityForResult(HelloActivity.class,
                    HELLO_REQUEST_CODE);
        } else {
            mPresenter.loadUpdateInfo(true);
        }
        if (!sp_Info.getBoolean(Constant.DEVICE, false)) {
            requestRuntimePermission("为了给您提供更好的服务,甜麦圈需要获取本机识别码权限",
                    Manifest.permission.READ_PHONE_STATE);
        }
        LoadInitDataService.startActionOther(this);
    }

    @Override
    public void showAdView(List<WelcomeADResponse.ADItem> startPageList) {
        this.adList = startPageList;
        mViewBinding.tvWelcomeGoto.setVisibility(View.VISIBLE);
        for (WelcomeADResponse.ADItem adItem : startPageList) {
            ImageView iv = new ImageView(this);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mViewBinding.welcomeImgFlipper.addView(iv,
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
            Glide.with(this).load(adItem.getPicUrl()).into(iv);
        }
        int countdown = startPageList.get(nowAdIndex).getCountdown();
        mHandler.postDelayed(mRunnable, countdown * 1000);
    }

    @Override
    public void showWelcomeView(boolean isDelay) {
        if (isDelay) {
            int delayMillis = 3000;
            try {
                ConfigInfo info = SysApplication.getDb().findFirst(Selector.from(ConfigInfo.class)
                        .where("name", "=", "START_PAGE_LOAD_TIME"));
                delayMillis = Integer.valueOf(info.getValue()) * 1000;
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    gotoHomeView();
                }
            }, delayMillis);
        } else {
            gotoHomeView();
        }
    }

    private Handler mHandler = new Handler();

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            nowAdIndex++;
            if (nowAdIndex < adList.size()) {
                mViewBinding.welcomeImgFlipper.showNext();
                int countdown = adList.get(nowAdIndex).getCountdown();
                mHandler.postDelayed(mRunnable, countdown * 1000);
            } else {
                mPresenter.gotoHomePage();
            }
        }
    };

    @Override
    public void exitView() {
        finish();
    }

    @Override
    public void gotoHomeView() {
        mHandler.removeCallbacks(mRunnable);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(NOTICE_JSON_CONTENT, getIntent().getStringExtra(NOTICE_JSON_CONTENT));
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case HELLO_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mPresenter.loadUpdateInfo(false);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_welcome_goto:
                mPresenter.gotoHomePage();
                break;
            case R.id.welcome_img_flipper:
                if (adList == null
                        || adList.size() <= 0
                        || nowAdIndex >= adList.size()) {
                    break;
                }
                mPresenter.gotoHomePage();
                WelcomeADResponse.ADItem adItem = adList.get(nowAdIndex);
                if (TextUtils.isEmpty(adItem.getSkipAddress())) {
                    break;
                }
                Bundle bundle = new Bundle();
                bundle.putString(WebAdActivity.TITLE, adItem.getTitle());
                bundle.putString(WebAdActivity.SKIP_ADDRESS, adItem.getSkipAddress());
                launchActivity(WebAdActivity.class, bundle);
                break;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        String deviceId = "";
        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        try {
            TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                deviceId = tm.getDeviceId();
            }
            if ("000000000000000".equals(deviceId)) {
                deviceId = android_id;
            }
        } catch (Exception e) {
            deviceId = android_id;
        }
        if (deviceId == null || deviceId.length() <= 0) {
            deviceId = android_id;
        }
        LoadInitDataService.startActionDevice(this, deviceId);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        super.onPermissionsDenied(requestCode, perms);
        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        LoadInitDataService.startActionDevice(this, android_id);
    }
}