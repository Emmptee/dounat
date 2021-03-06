package com.donut.app.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.RetrievePasswdRequest;
import com.donut.app.http.message.ValidateRegPhoneRequest;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

public class ForgotPasswordActivity extends BaseActivity {


    @ViewInject(R.id.forgot_account_edit)
    private EditText mAccountEt;
    @ViewInject(R.id.forgot_pwd_edit)
    private EditText mPwdEt;
    @ViewInject(R.id.forgot_code_edit)
    private EditText mCodeEt;


    @ViewInject(R.id.btn_forgot)
    private Button mForgot;
    @ViewInject(R.id.forgot_get_code)
    private TextView mGetCode;

    @ViewInject(R.id.see_pwd)
    private TextView mEyes;


    private CharSequence temp;//监听前的文本
    private int editStart;//光标开始位置
    private int editEnd;//光标结束位置

    private TimeCount timer;

    private static final int RETRIEVE_PASS = 0;
    private static final int PHONE_REQUEST = 1;

    private static final int READ_PHONE_STATE_REQUEST_CODE = 2;

    public static final String PHONE = "PHONE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        updateHeadTitle(getString(R.string.forgot_pass_title), true);

        ViewUtils.inject(this);
        timer = new TimeCount(60000, 1000);
        init();

        initView();
    }

    private void initView(){

        SMSSDK.initSDK(this, Constant.SMSSDK_APPKEY, Constant.SMSSDK_APPSECRET);
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    timer.start();
                    // 验证码已经发送
                    showToast("验证码已经发送");
                }
            } else {
                // 根据服务器返回的网络错误，给toast提示
                try {
                    ((Throwable) data).printStackTrace();
                    Throwable throwable = (Throwable) data;

                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");
                    if (!TextUtils.isEmpty(des)) {
                        showToast(des);
                        return;
                    }
                } catch (Exception e) {
                    SMSLog.getInstance().w(e);
                }
                // 如果木有找到资源，默认提示
                showToast(getString(R.string.connect_failuer_toast));
            }
        }
    };


    private void init() {
        mGetCodeState(false);
        mAccountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (temp.length() > 0) {
                    mGetCodeState(true);
                } else {
                    mGetCodeState(false);
                }
            }
        });
        mPwdEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                /* 得到光标开始和结束位置 ,超过最大数后记录刚超出的数字索引进行控制 */
                editStart = mPwdEt.getSelectionStart();
                editEnd = mPwdEt.getSelectionEnd();
                if (temp.length() > Constant.LENGTH_32)
                {
                    ToastUtil.showShort(ForgotPasswordActivity.this, getString(R.string.password_check_tip));
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    mPwdEt.setText(s);
                    mPwdEt.setSelection(tempSelection);
                }
            }
        });

        if (getIntent() != null && !"".equals(getIntent().getStringExtra(PHONE))) {
            mAccountEt.setText(getIntent().getStringExtra(PHONE));
            mGetCodeState(true);
        } else if (getLastUserAccount() != null) {
            mAccountEt.setText(getLastUserAccount());
            mGetCodeState(true);
        }
    }

    boolean isShow = false;
    @OnClick(value = {R.id.btn_forgot, R.id.forgot_get_code,R.id.see_pwd_linear})
    private void btnClick(View v) {

        switch (v.getId()) {
            case R.id.see_pwd_linear:
                if (isShow)
                {
                    mPwdEt.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
                    mEyes.setBackgroundResource(R.drawable.icon_close);
                    isShow = false;
                }
                else
                {
                    mPwdEt.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                    mEyes.setBackgroundResource(R.drawable.icon_open);
                    isShow = true;
                }
                mPwdEt.setSelection(mPwdEt.getText().toString().length());
                break;
            case R.id.btn_forgot:
                forgotRequest();
                break;
            case R.id.forgot_get_code:
                //先验证手机号
                phoneVertify();
                break;

        }
    }

    String phoneNum;

    String password;

    String code;

    private void phoneVertify() {
        phoneNum = mAccountEt.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)) {
            ToastUtil.showShort(this, getString(R.string.telphone_empty_tip));
            return;
        }
//        if (!FormatCheckUtil.isMobileNumber(phoneNum)) {
//            ToastUtil.showShort(this, getString(R.string.telphone_check_tip));
//            return;
//        }
        ValidateRegPhoneRequest request = new ValidateRegPhoneRequest();
        request.setPhone(phoneNum);
        request.setValidateType("1");
        sendNetRequest(request, HeaderRequest.VERIFICATY_PHONE, PHONE_REQUEST);
        saveBehaviour("01", request, HeaderRequest.VERIFICATY_PHONE);
    }

    private void getVerCode() {
        mGetCodeState(false);
        mGetCode.setText(getString(R.string.get_code_again));

        mCodeEt.setFocusable(true);
        mCodeEt.setFocusableInTouchMode(true);
        mCodeEt.requestFocus();
        SMSSDK.getVerificationCode("86", phoneNum);
    }

    private void forgotRequest() {
        phoneNum = mAccountEt.getText().toString().trim();
        password = mPwdEt.getText().toString().trim();
        code = mCodeEt.getText().toString();
        if (TextUtils.isEmpty(phoneNum)) {
            ToastUtil.showShort(this, getString(R.string.telphone_empty_tip));
            return;
        }
//        if (!FormatCheckUtil.isMobileNumber(phoneNum)) {
//            ToastUtil.showShort(this, getString(R.string.telphone_check_tip));
//            return;
//        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showShort(this, getString(R.string.password_empty_tip));
            return;
        }
        if (password.length() < 6 || password.length() > 32)
        {
            ToastUtil.showShort(this, getString(R.string.password_check_tip));
            return;
        }

        if (TextUtils.isEmpty(code)) {

            ToastUtil.showShort(this, getString(R.string.code_input_tip));
            return;
        }
        mForgot.setClickable(false);
        RetrievePasswdRequest request = new RetrievePasswdRequest();
        request.setPhone(phoneNum);
        request.setPassword(password);
        request.setDeviceType("0");
        request.setValidateCode(code);
        sendNetRequest(request, HeaderRequest.RETRIEVE_PASS, RETRIEVE_PASS);
        saveBehaviour("02", request, HeaderRequest.RETRIEVE_PASS);
    }

    @Override
    public void onSuccess(String response, String url, int actionId) {

        switch (actionId) {
            case PHONE_REQUEST:
                // 验证手机号是否注册，再获取验证码
                BaseResponse phoneRes = JsonUtils.fromJson(response,
                        BaseResponse.class);
                if (COMMON_SUCCESS.equals(phoneRes.getCode())) {
                    getVerCode();
                } else {
                    ToastUtil.showShort(this, phoneRes.getMsg());
                }
                break;

            case RETRIEVE_PASS:
                mForgot.setClickable(true);
                BaseResponse res = JsonUtils.fromJson(response,
                        BaseResponse.class);
                if (res != null) {
                    if (COMMON_SUCCESS.equals(res.getCode())) {
                        setLastUser(phoneNum);
                        showToast("找回密码成功");
                        finish();
                    } else {
                        ToastUtil.showShort(this, res.getMsg());
                    }
                }

                break;
            default:
                break;
        }
        super.onSuccess(response, url, actionId);
    }


    @Override
    public void onError(String errorMsg, String url, int actionId) {
        super.onError(errorMsg, url, actionId);
        mForgot.setClickable(true);
    }

    /**
     * 定义一个倒计时的内部类
     */
    private class TimeCount extends CountDownTimer {
        private TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        /**
         * 计时完毕时触发
         * <p/>
         * Title: onFinish
         *
         * @see CountDownTimer#onFinish()
         */
        @Override
        public void onFinish() {
            mGetCodeState(true);
            mGetCode.setText(getString(R.string.get_code_again));
        }

        /**
         * 计时过程显示
         */
        @Override
        public void onTick(long millisUntilFinished) {
            mGetCodeState(false);
            mGetCode.setText(getString(R.string.get_code_again)
                    + millisUntilFinished / 1000 + "秒");
        }
    }

    private void mGetCodeState(boolean flag) {
        mGetCode.setClickable(flag);
        mGetCode.setSelected(flag);
    }

    @Override
    public void onBackPressed() {
        saveBehaviour("03");
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
        SaveBehaviourDataService.startAction(this, BehaviourEnum.FORGOT_PASS.getCode() + functionCode);
    }

    private void saveBehaviour(String functionCode, Object request, String header) {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.FORGOT_PASS.getCode() + functionCode, request, header);
    }
}
