package com.donut.app.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.SysApplication;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.entity.UserInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.BaseRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.PushRequest;
import com.donut.app.http.message.UpdateInfoRequest;
import com.donut.app.http.message.UpdateInfoResponse;
import com.donut.app.http.message.UserInfoResponse;
import com.donut.app.mvp.auction.MyAuctionActivity;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.AppConfigUtil;
import com.donut.app.utils.CacheUtils;
import com.donut.app.utils.FileUtils;
import com.donut.app.utils.FormatCheckUtil;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.donut.app.utils.ToastUtil;
import com.donut.app.utils.UpdateAppUtil;
import com.donut.app.utils.VersionUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.util.UUID;

/**
 * 设置
 */
public class SettingsActivity extends BaseActivity {

    @ViewInject(R.id.setting_top_layout)
    private LinearLayout topLayout;

    @ViewInject(R.id.setting_tv_message_num)
    private TextView tvNum;

    @ViewInject(R.id.setting_btn_login)
    private Button btnLogin;

    @ViewInject(R.id.dot_txt)
    private ImageView mDot;

    @ViewInject(R.id.setting_message_dot)
    private View mMessageDot;

    @ViewInject(R.id.setting_tv_cache_num)
    private TextView tvCacheNum;

    private static final int LOGIN_CODE = 1, UPDATE_PASS_CODE = 2;

    private static final int PERSONAL_INFO_REQUEST_CODE = 0;

    private static final int PERSONAL_INFO_REQUEST = 0;

    private static final int REMOVE_NOTICE_TAG = 3;

    private static final int UPDATE_RESULT = 0x88;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        updateHeadTitle(getString(R.string.settings_title), true);
        initData();
    }

    private void loadData() {
        BaseRequest request = new BaseRequest();
        sendNetRequest(request, HeaderRequest.GET_PERSONAL_INFO, PERSONAL_INFO_REQUEST,
                false);
    }


    private void initData() {
        if (getLoginStatus()) {
            topLayout.setVisibility(View.VISIBLE);
            btnLogin.setText(R.string.quit);
            loadData();
        } else {
            topLayout.setVisibility(View.GONE);
            btnLogin.setText(R.string.login);
        }

        if (sp_Info.getBoolean(Constant.HAS_NEW_MSG, false)) {
            mMessageDot.setVisibility(View.VISIBLE);
        } else {
            mMessageDot.setVisibility(View.GONE);
        }

        long cacheLength = 0;
        String externalCache = FileUtils.getExternalStorageCachePath(this);
        if (externalCache != null) {
            cacheLength += FileUtils.getDirSize(new File(externalCache));
        }
        String dataCache = FileUtils.getCachePath(this);
        if (dataCache != null) {
            cacheLength += FileUtils.getDirSize(new File(dataCache));
        }
        tvCacheNum.setText(FormatCheckUtil.getDataSize(cacheLength));
    }

    private void deleteBaiduPush() {

        PushRequest request = new PushRequest();
        request.setPushUserid(sp_Info.getString(Constant.PUSH_USER_ID, ""));
        request.setPushChannelid(sp_Info.getString(Constant.PUSH_CHANNEL_ID, ""));
        request.setUserId(getUserInfo().getUserId());
        Integer type = getUserInfo().getUserType();
        if (type == 1) {
            request.setUserType(String.valueOf(0));
        } else {
            request.setUserType(String.valueOf(1));
        }
        request.setOsType("0");
        sendNetRequest(request, HeaderRequest.REMOVE_TAG, REMOVE_NOTICE_TAG, false);
    }

    @OnClick({R.id.setting_tv_personal_info, R.id.setting_tv_update_pass,
            R.id.setting_tv_address, R.id.setting_tv_income, R.id.setting_tv_auction,
            R.id.setting_tv_message, R.id.setting_tv_feedback, R.id.setting_tv_update,
            R.id.setting_tv_law, R.id.setting_tv_about, R.id.setting_tv_recommended,
            R.id.setting_btn_login, R.id.setting_tv_my_attention, R.id.setting_cache})
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.setting_tv_personal_info:
                launchActivityForResult(PersonalInfoActivity.class, PERSONAL_INFO_REQUEST_CODE);
                saveBehaviour("01");
                break;
            case R.id.setting_tv_update_pass:
                startActivityForResult(new Intent(this, UpdatePasswordActivity.class), UPDATE_PASS_CODE);
                saveBehaviour("02");
                break;
            case R.id.setting_tv_address:
                launchActivity(MyAddressActivity.class);
                saveBehaviour("04");
                break;
            case R.id.setting_tv_income:
                launchActivity(RewardIncomeActivity.class);
                saveBehaviour("05");
                break;
            case R.id.setting_tv_auction:
                launchActivity(MyAuctionActivity.class);
                break;
            case R.id.setting_tv_message:
                tvNum.setText("");
                mMessageDot.setVisibility(View.GONE);

                launchActivity(SystemNoticeActivity.class);
                saveBehaviour("06");
                break;
            case R.id.setting_tv_feedback:
                launchActivity(AdviceActivity.class);
                saveBehaviour("07");
                break;
            case R.id.setting_tv_update:
                saveBehaviour("08");
                Update();
                break;
            case R.id.setting_tv_law:
                toLawActivity();
                saveBehaviour("09");
                break;
            case R.id.setting_tv_about:
                launchActivity(AboutActivity.class);
                saveBehaviour("10");
                break;
            case R.id.setting_tv_recommended:
                toRecommendActivity();
                saveBehaviour("11");
                break;
            case R.id.setting_btn_login:
                if (getLoginStatus()) {
                    logout();
                } else {
                    saveBehaviour("14");
                    startActivityForResult(new Intent(this, LoginActivity.class), LOGIN_CODE);
                }
                break;
//            case R.id.setting_tv_my_ip:
//                launchActivity(MyIpActivity.class);
//                break;
            case R.id.setting_tv_my_attention:
                launchActivity(MyAttentionActivity.class);
                break;
            case R.id.setting_cache:
                Dialog dialog = new AlertDialog.Builder(this)
                        .setMessage(R.string.clear_cache_title)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        CacheUtils.clear(SysApplication.getInstance());
                                    }
                                }).start();

                                tvCacheNum.setText("0KB");
                            }
                        }).create();
                dialog.show();
                break;
        }
    }

    private void Update() {
        UpdateInfoRequest infoRequest = new UpdateInfoRequest();
        infoRequest.setVersionCode(VersionUtil.getVersionCode(this) + "");
        sendNetRequest(infoRequest, HeaderRequest.UPDATE, UPDATE_RESULT,
                false);

    }

    private void toLawActivity() {
        Intent intent_law = new Intent(SettingsActivity.this, BrowseDetailActivity.class);
        intent_law.putExtra(BrowseDetailActivity.title,
                getString(R.string.law_terms));
        intent_law
                .putExtra(BrowseDetailActivity.webUrl, RequestUrl.LAW_URL);
        intent_law.putExtra(BrowseDetailActivity.share, false);
        startActivity(intent_law);
    }

    private void toRecommendActivity() {
        Intent intent = new Intent(SettingsActivity.this, BrowseDetailActivity.class);
        intent.putExtra(BrowseDetailActivity.title,
                getString(R.string.recommended));
        intent.putExtra(BrowseDetailActivity.webUrl,
                RequestUrl.RECOMMAND_SHARE_URL);
        intent.putExtra(BrowseDetailActivity.share, true);
        startActivity(intent);
    }


    private void logout() {
        Dialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.logout_title)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteBaiduPush();
                        SettingsActivity.this.setUserInfo(new UserInfo(), false);
                        saveBehaviour("15");
                        new AppConfigUtil(SettingsActivity.this).setStarCode(UUID.randomUUID().toString());
                        initData();
                    }
                }).create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOGIN_CODE:
                initData();

                break;
            case UPDATE_PASS_CODE:
                initData();
                if (!getLoginStatus()) {
                    startActivityForResult(new Intent(this, LoginActivity.class), LOGIN_CODE);
                }
                break;
            case PERSONAL_INFO_REQUEST_CODE:
                //调用接口
                loadData();
                break;
        }
    }

    @Override
    public void onSuccess(String response, String url, int actionId) {
        super.onSuccess(response, url, actionId);
        switch (actionId) {
            case PERSONAL_INFO_REQUEST:
                UserInfoResponse res = JsonUtils.fromJson(response,
                        UserInfoResponse.class);
                if (COMMON_SUCCESS.equals(res.getCode())) {
                    showView(res);
                } else {
                    ToastUtil.showShort(this, res.getMsg());
                }
                break;
            case REMOVE_NOTICE_TAG:
                BaseResponse basRes = JsonUtils.fromJson(response,
                        BaseResponse.class);
                if (COMMON_SUCCESS.equals(basRes.getCode())) {
                    SettingsActivity.this.setUserInfo(new UserInfo(), false);
                    initData();
                } else {
                    ToastUtil.showShort(this, basRes.getMsg());
                }
                break;
            case UPDATE_RESULT:
                UpdateInfoResponse infoRespose = JsonUtils.fromJson(response,
                        UpdateInfoResponse.class);
                if ("0500".equals(infoRespose.getCode())) {
                    //最新版本
                    ToastUtil.showShort(this, "当前版本为最新版本");
                } else if ("0000".equals(infoRespose.getCode())) {
                    String versionName = null;
                    try {
                        String versionCode = infoRespose.getVersionCode();
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
                    new UpdateAppUtil.Builder(this, infoRespose.getFileUrl())
                            .description(infoRespose.getDescription())
                            .fileName(infoRespose.getFileName())
                            .force(infoRespose
                                    .getForceUpdateFlag() == 1)
                            .versionName(versionName)
                            .fileSize(infoRespose.getFileSize())
                            .build()
                            .update();
                } else {
                    //出错
                    ToastUtil.showShort(this, infoRespose.getMsg());
                }
                break;
        }
    }

    private void showView(UserInfoResponse res) {

        if (res.getFlg() != null && res.getFlg() == 1) {
            mDot.setVisibility(View.GONE);
        } else {
            mDot.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onBackPressed() {
        saveBehaviour("16");
        setResult(getLoginStatus() ? RESULT_OK : RESULT_CANCELED);
        super.onBackPressed();
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
        SaveBehaviourDataService.startAction(this, BehaviourEnum.SETTINGS.getCode() + functionCode);
    }
}
