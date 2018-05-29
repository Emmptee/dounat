package com.donut.app.activity.base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.manager.LoadController;
import com.android.volley.manager.RequestManager;
import com.donut.app.AppManager;
import com.donut.app.R;
import com.donut.app.SysApplication;
import com.donut.app.activity.LoginActivity;
import com.donut.app.config.Constant;
import com.donut.app.customview.LoadingDialog;
import com.donut.app.entity.UserInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.SendNetRequestManager;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.PushRequest;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.AppConfigUtil;
import com.donut.app.utils.CommonUtils;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.L;
import com.donut.app.utils.ToastUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * BaseActivity
 * Created by Qi on 2016/1/21.
 */
public abstract class BaseActivity extends AppCompatActivity
        implements RequestManager.RequestListener,
        View.OnClickListener,
        EasyPermissions.PermissionCallbacks {

    private LoadingDialog progressDialog;

    protected SharedPreferences sp_Info;

    private LoadController mLoadController = null;

    // 默认请求码
    protected static final int BASEREQUEST = 0;

    // 上传图片请求码
    protected static final int UPLOADIMGREQUEST = 9999;

    private static final int REMOVE_NOTICE_TAG = 3;

    /*
     * 通用响应码
     */
    protected static final String COMMON_SUCCESS = "0000";// 请求成功

//    protected static final String COMMON_ERROR = "0001";// 请求失败，内部错误

    protected static final String COMMON_NOT_LOGIN = "0002";// 未登录

//    protected static final String COMMON_BODY_ERROR = "0003";// 消息体格式错误
//
//    protected static final String COMMON_HEADER_ERROR = "0004";// 消息头格式错误
//
//    protected static final String COMMON_SYSTEM_ERROR = "0005";// 系统内部错误

    private boolean isShowLoading;

    private static final int RC_PERM = 1123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
        initData();
    }

    private void initData() {
        sp_Info = this.getSharedPreferences(Constant.SP_INFO,
                Context.MODE_PRIVATE);
    }

    public UserInfo getUserInfo() {
        return SysApplication.getUserInfo();
    }

    public void setUserInfo(UserInfo userInfo, Boolean isLogin) {
        sp_Info.edit()
                .putBoolean(Constant.IS_LOGIN, isLogin).apply();
        SysApplication.setUserInfo(userInfo);
    }

    /*
     * 在登录或者注册之后保存登录账户
     */
    protected void setLastUser(String account) {
        sp_Info.edit().putString(Constant.USER_ACCOUNT, account).apply();
    }

    /*
     * 保存三方登陆标记，用于在个人信息和个人中心界面隐藏一些字段
     */
    protected void setThirdLoginTag(boolean tag) {
        sp_Info.edit().putBoolean(Constant.THIRD_LOGIN_TAG, tag).apply();
    }

    protected boolean getThirdLoginTag() {
        return sp_Info.getBoolean(Constant.THIRD_LOGIN_TAG, false);
    }

    protected String getLastUserAccount() {
        return sp_Info.getString(Constant.USER_ACCOUNT, null);
    }

    /**
     * 得到登录状态
     */
    public boolean getLoginStatus() {
        return sp_Info.getBoolean(Constant.IS_LOGIN, false);
    }

    /**
     * update标题 titleContent 内容 ishaveBack false不显示返回箭
     */
    protected void updateHeadTitle(String titleContent, boolean ishaveBack) {
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        if (tv_title != null) {
            tv_title.setText(titleContent);
        }
        LinearLayout layout_head_back = (LinearLayout) findViewById(R.id.back);
        if (ishaveBack && layout_head_back != null) {
            layout_head_back.setVisibility(View.VISIBLE);
            layout_head_back.setOnClickListener(this);
        }
    }

    boolean isActive = true;

    @Override
    protected void onStart() {
        //SaveBehaviourDataService.startAction(this, BehaviourEnum.LOGIN.getCode()+"00",null);
        super.onStart();
        if (!isActive) {
            // Log.i("wjj","从后台唤醒===========");
            new AppConfigUtil(this).setStarCode(UUID.randomUUID().toString());
            new AppConfigUtil(this).setStarTime(System.currentTimeMillis());
            SaveBehaviourDataService.startAction(this, AppConfigUtil.getBehaviourHeader() + "99999");
        }
    }

    @Override
    protected void onStop() {
//        if (CommonUtils.isRunningForeground(this)) {
        if (!CommonUtils.isAppOnForeground(this)) {
            // Log.i("wjj","进入后台===========");
            SaveBehaviourDataService.startAction(this, AppConfigUtil.getBehaviourHeader() + "88888");
            isActive = false;
        }
        super.onStop();
    }

    /**
     * 运行时权限申请
     */
    //@AfterPermissionGranted(RC_PERM)
    public void requestRuntimePermission(@NonNull String rationale,
                                         @NonNull String... permission) {
        if (EasyPermissions.hasPermissions(this, permission)) {
            // Have permission, do the thing!
            onPermissionsGranted(RC_PERM, Arrays.asList(permission));
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, rationale,
                    RC_PERM, permission);
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
        }
    }

    /**
     * 发送网络请求的封装
     */
    protected void sendNetRequest(Object obj, final String header,
                                  final boolean isShowLoading) {
        this.sendNetRequest(obj, header, BASEREQUEST, isShowLoading);
    }

    protected void sendNetRequest(Object obj, final String header) {
        this.sendNetRequest(obj, header, BASEREQUEST, true);
    }

    protected void sendNetRequest(Object obj, final String header,
                                  final int requestCode) {
        this.sendNetRequest(obj, header, requestCode, true);
    }

    protected void sendNetRequest(Object obj, final String header,
                                  final int requestCode, final boolean isShowLoading) {
        this.isShowLoading = isShowLoading;

        SendNetRequestManager sendNet = new SendNetRequestManager(this);
        mLoadController = sendNet.sendNetRequest(obj, header, requestCode);
    }

    protected void UpLoadNetRequest(String filePath, int type) {
        this.isShowLoading = false;
        SendNetRequestManager sendNet = new SendNetRequestManager(this);
        mLoadController = sendNet.uploadImg(filePath, type, UPLOADIMGREQUEST);
    }

    protected void UpLoadNetRequest(String filePath, int type, int requestCode) {
        this.isShowLoading = false;
        SendNetRequestManager sendNet = new SendNetRequestManager(this);
        mLoadController = sendNet.uploadImg(filePath, type, requestCode);
    }

    protected void UpLoadNetRequest(String filePath, int type,
                                    int requestCode, boolean isShowLoading) {
        this.isShowLoading = isShowLoading;
        SendNetRequestManager sendNet = new SendNetRequestManager(this);
        mLoadController = sendNet.uploadImg(filePath, type, requestCode);
    }

    protected void LocationNetRequest(String url, int requestCode) {
        this.isShowLoading = false;
        SendNetRequestManager sendNet = new SendNetRequestManager(this);
        mLoadController = sendNet.sendLocationRequest(url, requestCode);
    }

    protected void launchActivity(Class<?> cls) {
        launchActivity(cls, null);
    }

    protected void launchActivity(Class<?> cls, Bundle param) {
        Intent intent = new Intent(this, cls);
        if (param != null) {
            intent.putExtras(param);
        }
        startActivity(intent);
    }

    protected void launchActivityForResult(Class<?> cls, int requestCode) {
        launchActivityForResult(cls, null, requestCode);
    }

    protected void launchActivityForResult(Class<?> cls, Bundle param,
                                           int requestCode) {
        Intent intent = new Intent(this, cls);
        if (param != null) {
            intent.putExtras(param);
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onRequest() {
        if (isShowLoading) {
            jumpDialog();
            isShowLoading = false;
        }
    }

    @Override
    public void onLoading(long total, long count, String filePath) {

    }

    @Override
    public void onSuccess(String response, Map<String, String> headers,
                          String url, int actionId) {
        L.i("====", response);
        BaseResponse res = JsonUtils.fromJson(response, BaseResponse.class);
        if (res != null && COMMON_NOT_LOGIN.equals(res.getCode())) {
            ToastUtil.showShort(this, getString(R.string.login_timeout_msg));
            deleteBaiduPush();
            setUserInfo(null, false);
            finish();
            launchActivity(LoginActivity.class);
            dissDialog();
            return;
        }
        dissDialog();
        onSuccess(response, url, actionId);
    }

    private void deleteBaiduPush() {

        PushRequest request = new PushRequest();
        request.setPushUserid(sp_Info.getString(Constant.PUSH_USER_ID, ""));
        request.setPushChannelid(sp_Info.getString(Constant.PUSH_CHANNEL_ID, ""));
        request.setUserId(SysApplication.getUserInfo().getUserId());
        Integer type = SysApplication.getUserInfo().getUserType();
        if (type == 1) {
            request.setUserType(String.valueOf(0));
        } else {
            request.setUserType(String.valueOf(1));
        }
        request.setOsType("0");
        sendNetRequest(request, HeaderRequest.REMOVE_TAG, REMOVE_NOTICE_TAG, false);

    }

    public void onSuccess(String response, String url, int actionId) {
    }

    @Override
    public void onError(String errorMsg, String url, int actionId) {
        dissDialog();
        showToast(getString(R.string.connect_failuer_toast));
    }

    protected void showToast(String message) {
        ToastUtil.showShort(this, message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (im.isActive() && null != getCurrentFocus()) {
            im.hideSoftInputFromWindow(getCurrentFocus()
                            .getApplicationWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return super.onTouchEvent(event);
    }

    /**
     * 弹起加载框
     */
    public void jumpDialog() {
        if (null == progressDialog) {
            progressDialog = new LoadingDialog(BaseActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
            try {
                progressDialog.show();
            } catch (Exception e) {
                jumpDialog();
            }
        }
    }

    /**
     * 关闭弹出框
     */
    protected void dissDialog() {
        if (null != progressDialog) {
            progressDialog.dismiss();
            progressDialog.cancel();
            progressDialog = null;
        }
    }

    // 定义此标志 避免子类中调用了该方法后父类再次调用
    public boolean commentFlag = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!commentFlag) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideKeyboard(v, ev)) {
                    hideKeyboard(v.getWindowToken());
                }
            }
        }
        boolean flag = false;
        try {
            flag = super.dispatchTouchEvent(ev);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     */
    protected boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                v.clearFocus();
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     */
    protected void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onDestroy() {
        // 结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
        if (mLoadController != null) {
            mLoadController.cancel();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dissDialog();
    }

    public SharedPreferences getSp_Info() {
        return sp_Info;
    }
}
