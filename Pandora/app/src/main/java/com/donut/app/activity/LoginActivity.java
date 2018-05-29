package com.donut.app.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.manager.RequestManager;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.customview.LoadingDialog;
import com.donut.app.entity.UserInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.PushRequest;
import com.donut.app.http.message.UserLoginRequest;
import com.donut.app.http.message.UserLoginResponse;
import com.donut.app.http.message.UserThirdLoginRequest;
import com.donut.app.mvp.register.RegisterActivity;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 */
public class LoginActivity extends BaseActivity {
    @ViewInject(R.id.login_account_edit)
    private EditText mAccount;

    @ViewInject(R.id.login_pwd_edit)
    private EditText mPwd;

    @ViewInject(R.id.see_pwd)
    private TextView mEyes;

    private boolean isShow = false, isThirdLogin = false;

    private static final int LOGIN_REQUEST = 0, THIRD_LOGIN_REQUEST = 1, QQ_UNIONID_REQUEST = 2;

    private static final int BANDING_REQ = 0;

    private static final int SYSTEM_NOTICE_TAG = 2;

    private UMShareAPI mShareAPI;

    private UserInfo info = new UserInfo();

    private LoadingDialog thirdLoginDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        if (getLastUserAccount() != null) {
            mAccount.setText(getLastUserAccount());
        }

        mShareAPI = UMShareAPI.get(this);
        setUserInfo(info, false);
    }

    String phoneNum;

    private void loginRequest() {
        phoneNum = mAccount.getText().toString();
        String password = mPwd.getText().toString();

        if (TextUtils.isEmpty(phoneNum)) {
            showToast("请输入手机号");
            return;
        } else if (TextUtils.isEmpty(password)) {
            showToast("请输入密码");
            return;
        }

        UserLoginRequest request = new UserLoginRequest();
        request.setUserName(phoneNum);
        request.setPasswd(password);
        info.setUserName(phoneNum);
        info.setPasswd(password);
        sendNetRequest(request, HeaderRequest.LOGIN, LOGIN_REQUEST);
        saveBehaviour("01", request, HeaderRequest.LOGIN);
    }

    @OnClick({R.id.back, R.id.btn_login, R.id.login_to_register,
            R.id.login_to_forget_pass, R.id.login_third_qq,
            R.id.login_third_weibo, R.id.login_third_weixin,
            R.id.see_pwd_linear})
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.login_to_register:
                startActivity(new Intent(this, RegisterActivity.class));
                saveBehaviour("02");
                break;
            case R.id.login_to_forget_pass:
                startActivity(new Intent(this, ForgotPasswordActivity.class)
                        .putExtra(ForgotPasswordActivity.PHONE, mAccount.getText().toString()));
                saveBehaviour("03");
                break;
            case R.id.btn_login:
                loginRequest();
                break;

            case R.id.login_third_qq:
                if (mShareAPI.isInstall(this, SHARE_MEDIA.QQ)) {
                    login(SHARE_MEDIA.QQ);
                    saveBehaviour("05");
                } else {
                    showToast(getString(R.string.qq_install_login_tips));
                }
                break;
            case R.id.login_third_weibo:
                login(SHARE_MEDIA.SINA);
                saveBehaviour("06");
                break;
            case R.id.login_third_weixin:
                if (mShareAPI.isInstall(this, SHARE_MEDIA.WEIXIN)) {
                    login(SHARE_MEDIA.WEIXIN);
                    saveBehaviour("04");
                } else {
                    showToast(getString(R.string.weixin_install_login_tips));
                }
                break;
            case R.id.see_pwd_linear:
                if (isShow) {
                    mPwd.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
                    Drawable drawable = getResources()
                            .getDrawable(R.drawable.icon_close);
                    mEyes.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            drawable, null);
                    isShow = false;
                } else {
                    mPwd.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                    Drawable drawable = getResources().getDrawable(R.drawable.icon_open);
                    mEyes.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            drawable, null);
                    isShow = true;
                }
                mPwd.setSelection(mPwd.getText().toString().length());
                break;
        }
    }

    private String uid, nickName, headerUrl;

    private void login(final SHARE_MEDIA platform) {
        showThirdLoginDialog();
        this.shareMedia = platform;
        if (platform == SHARE_MEDIA.QQ) {
            mShareAPI.doOauthVerify(LoginActivity.this, platform, umAuthListener);
        } else {
            mShareAPI.getPlatformInfo(LoginActivity.this, platform, umAuthSuccessListener);
        }
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA media) {
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (platform == SHARE_MEDIA.QQ) {
                String url = "https://graph.qq.com/oauth2.0/me?access_token=" + data.get("access_token") + "&unionid=1";
                RequestManager.getInstance().get(
                        url, requestListener, QQ_UNIONID_REQUEST);
            } else {
                mShareAPI.getPlatformInfo(LoginActivity.this, platform, umAuthSuccessListener);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
//            showToast("授权失败");
            showToast("登录失败");
            dismissThirdLoginDialog();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
//            showToast("取消授权");
            showToast("取消登录");
            dismissThirdLoginDialog();
        }
    };

    private UMAuthListener umAuthSuccessListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action,
                               Map<String, String> data) {
            dismissThirdLoginDialog();
            if (data != null && data.size() > 0) {
                if (platform == SHARE_MEDIA.QQ) {
//                    uid = data.get("unionid");
//                    sex = "男".equals(data.get("gender")) ? "0" : "1";
                    nickName = data.get("name");
                    headerUrl = data.get("iconurl");
                }
                if (platform == SHARE_MEDIA.WEIXIN) {
                    uid = data.get("unionid");
//                    sex = "男".equals(data.get("sex")) ? "0" : "1";
                    nickName = data.get("name");
                    headerUrl = data.get("iconurl");
                }
                if (platform == SHARE_MEDIA.SINA) {
                    uid = data.get("id");
                    nickName = data.get("name");
                    headerUrl = data.get("iconurl");
//                    sex = "男".equals(data.get("gender")) ? "0" : "1";
                }
                thirdLogin(platform);
            } else {
                showToast("信息获取失败,请稍后重试");
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            showToast("三方信息获取失败");
            dismissThirdLoginDialog();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            showToast("取消登录");
            dismissThirdLoginDialog();
        }
    };

    private boolean toLogin;
    private SHARE_MEDIA shareMedia;

    @Override
    protected void onResume() {
        if (toLogin && shareMedia == SHARE_MEDIA.WEIXIN) {
            dismissThirdLoginDialog();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        toLogin = true;
        super.onPause();
    }

    private void thirdLogin(SHARE_MEDIA platform) {
        isThirdLogin = true;
        UserThirdLoginRequest request = new UserThirdLoginRequest();
        request.setThirdTag(uid);
        request.setNickName(nickName);
        request.setImgPath(headerUrl);
        request.setPhoneType("0");
        if (platform == SHARE_MEDIA.QQ) {
            request.setRegPlatform("2");
        }
        if (platform == SHARE_MEDIA.WEIXIN) {
            request.setRegPlatform("1");
        }
        if (platform == SHARE_MEDIA.SINA) {
            request.setRegPlatform("3");
        }
        sendNetRequest(request, HeaderRequest.USER_THIRD_LOGIN, THIRD_LOGIN_REQUEST,
                true);
    }


    @Override
    public void onSuccess(String response, String url, int actionId) {
        switch (actionId) {
            case THIRD_LOGIN_REQUEST:
            case LOGIN_REQUEST:
                // 登录返回
                UserLoginResponse resLogin = JsonUtils.fromJson(response,
                        UserLoginResponse.class);
                if (COMMON_SUCCESS.equals(resLogin.getCode())) {
                    settag(resLogin.getUserId(), resLogin.getUserType());
                    info.setUserId(resLogin.getUserId());
                    info.setToken(resLogin.getToken());
                    info.setNickName(resLogin.getNickName());
                    info.setUserType(resLogin.getUserType());
                    info.setmBalance(resLogin.getBalance());
                    info.setThirdTagUid(uid);
                    info.setMemberStatus(resLogin.getMemberStatus());
                    if (isThirdLogin) {
                        info.setThirdTag(1);
                        info.setImgUrl(resLogin.getPicPath());
                    } else {
                        info.setThirdTag(0);
                        info.setImgUrl(resLogin.getImgUrl());
                    }
                    setLastUser(phoneNum);
                    setUserInfo(info, true);
                    if (isThirdLogin && resLogin.getIsFirstLogin() == 1) {
                        startActivityForResult(
                                new Intent(this, BandingPhoneActivity.class)
                                        .putExtra(BandingPhoneActivity.THIRD_TAG, uid)
                                        .putExtra(BandingPhoneActivity.NICK_NAME, nickName)
                                        .putExtra(BandingPhoneActivity.PIC_PATH, headerUrl)
                                , BANDING_REQ);
                        showToast("三方登陆成功,请绑定手机");
                        break;
                    } else {
                        setResult(RESULT_OK);
                        finish();
                    }

                } else {
                    showToast(resLogin.getMsg());
                    setUserInfo(info, false);
                }
                break;
            default:
                break;
        }
        super.onSuccess(response, url, actionId);
    }

    private void settag(String userid, int type) {
        PushRequest request = new PushRequest();
        request.setPushUserid(sp_Info.getString(Constant.PUSH_USER_ID, ""));
        request.setPushChannelid(sp_Info.getString(Constant.PUSH_CHANNEL_ID, ""));
        request.setUserId(userid);
        if (type == 1) {
            request.setUserType(String.valueOf(0));
        } else {
            request.setUserType(String.valueOf(1));
        }
        request.setStatus("1");
        request.setOsType("0");
        sendNetRequest(request, HeaderRequest.SET_TAG, SYSTEM_NOTICE_TAG, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case BANDING_REQ:
                //不管是否绑定成功，都可以登录
                if (resultCode == RESULT_OK) {
                    showToast("绑定成功");
                }
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    private RequestManager.RequestListener requestListener = new RequestManager.RequestListener() {
        @Override
        public void onRequest() {
        }

        @Override
        public void onLoading(long l, long l1, String s) {
        }

        @Override
        public void onSuccess(String response, Map<String, String> headers,
                              String url, int actionId) {
            if (actionId == QQ_UNIONID_REQUEST) {
                String json = response.replace("callback(", "").replace(");", "").trim();
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    uid = jsonObj.getString("unionid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, umAuthSuccessListener);
        }

        @Override
        public void onError(String s, String s1, int i) {

        }
    };

    private void showThirdLoginDialog() {
        if (thirdLoginDialog == null) {
            thirdLoginDialog = new LoadingDialog(this);
            thirdLoginDialog.setCanceledOnTouchOutside(false);
        }
        try {
            thirdLoginDialog.show();
        } catch (Exception e) {
            showThirdLoginDialog();
        }
    }

    private void dismissThirdLoginDialog() {
        if (null != thirdLoginDialog) {
            thirdLoginDialog.dismiss();
            thirdLoginDialog.cancel();
            thirdLoginDialog = null;
        }
    }

    @Override
    public void onBackPressed() {
        saveBehaviour("07");
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
        SaveBehaviourDataService.startAction(this, BehaviourEnum.LOGIN.getCode() + functionCode);
    }

    private void saveBehaviour(String functionCode, Object request, String header) {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.LOGIN.getCode() + functionCode, request, header);
    }
}
