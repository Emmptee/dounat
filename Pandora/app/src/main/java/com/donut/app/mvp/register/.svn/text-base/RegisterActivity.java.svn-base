package com.donut.app.mvp.register;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.activity.BrowseDetailActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.databinding.RegisterLayoutBinding;
import com.donut.app.http.RequestUrl;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.StatusBarUtil;
import com.donut.app.utils.ToastUtil;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

/**
 */
public class RegisterActivity extends MVPBaseActivity<RegisterLayoutBinding, RegisterPresenter>
        implements RegisterContract.View, TextView.OnEditorActionListener {

    private TimeCount timer;

    private CharSequence temp;
    private int editStart, editEnd;

    private boolean passwordIsShow = false, starIsRegister = false;

    @Override
    protected int getLayoutId() {
        return R.layout.register_layout;
    }

    @Override
    protected void initView() {
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        timer = new TimeCount(mViewBinding.registerGetCode, 60000, 1000);
        mViewBinding.registerRule.setText(Html.fromHtml("<font color='#999999'>注册即同意"
                + " </font><font color='#81D8D0'>《甜麦圈注册协议》</font>"));
        mViewBinding.tvChangeUser.setText(Html.fromHtml("<u>" + getString(R.string.register_star_title) + "</u>"));
    }

    @Override
    protected void initEvent() {
        mViewBinding.setHandler(this);
        mViewBinding.registerGetCode.setClickable(false);
        mViewBinding.registerAccountEdit.addTextChangedListener(new TextWatcher() {
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
                    mViewBinding.registerGetCode.setBackgroundResource(R.drawable.shape_half_round_main);
                    mViewBinding.registerGetCode.setClickable(true);
                } else {
                    mViewBinding.registerGetCode.setBackgroundResource(R.drawable.shape_half_round_gray);
                    mViewBinding.registerGetCode.setClickable(false);
                }
            }
        });
        mViewBinding.registerPwdEdit.addTextChangedListener(new TextWatcher() {
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
                editStart = mViewBinding.registerPwdEdit.getSelectionStart();
                editEnd = mViewBinding.registerPwdEdit.getSelectionEnd();
                if (temp.length() > Constant.LENGTH_32) {
                    ToastUtil.showShort(RegisterActivity.this, getString(R.string.password_check_tip));
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    mViewBinding.registerPwdEdit.setText(s);
                    mViewBinding.registerPwdEdit.setSelection(tempSelection);
                }
            }
        });
    }

    @Override
    public void loadData() {
        SMSSDK.initSDK(this, Constant.SMSSDK_APPKEY, Constant.SMSSDK_APPSECRET);
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                SendCodeHandler handler
                        = new SendCodeHandler(RegisterActivity.this, Looper.getMainLooper());
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }

    public void phoneValidate() {
        String phoneNum = mViewBinding.registerAccountEdit.getText().toString().trim();
        mPresenter.phoneValidate(phoneNum);
    }

    public void showPass() {
        if (passwordIsShow) {
            mViewBinding.registerPwdEdit.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
            Drawable drawable = getResources()
                    .getDrawable(R.drawable.icon_close);
            mViewBinding.seePwd.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    drawable, null);
            passwordIsShow = false;
        } else {
            mViewBinding.registerPwdEdit.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());
            Drawable drawable = getResources().getDrawable(R.drawable.icon_open);
            mViewBinding.seePwd.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    drawable, null);
            passwordIsShow = true;
        }
        mViewBinding.registerPwdEdit.setSelection(mViewBinding.registerPwdEdit.getText().toString().length());
    }

    public void registerRequest() {
        String phoneNum = mViewBinding.registerAccountEdit.getText().toString().trim();
        String password = mViewBinding.registerPwdEdit.getText().toString().trim();
        String code = mViewBinding.registerCodeEdit.getText().toString().trim();
        String starCode = mViewBinding.registerStarCodeEdit.getText().toString().trim();
        boolean flag = false;
        if (starIsRegister) {
            flag = mPresenter.registerStar(phoneNum, password, starCode, this);
        } else {
            flag = mPresenter.register(phoneNum, password, code, this);
        }

        if (flag) {
            registerClickable(false);
        }
    }

    public void readRule() {
        Bundle bundle = new Bundle();
        bundle.putString(BrowseDetailActivity.title, "注册条款");
        bundle.putString(BrowseDetailActivity.webUrl, RequestUrl.REGISTER_URL);
        bundle.putBoolean(BrowseDetailActivity.share, false);

        launchActivity(BrowseDetailActivity.class, bundle);
    }

    public void changeUser() {
        if (starIsRegister) {
            mViewBinding.tvRegisterTitle.setText(R.string.register_title);
            mViewBinding.registerAccountEdit.setHint(R.string.telphone_input_tip);
            mViewBinding.registerAccountEdit.setInputType(InputType.TYPE_CLASS_PHONE);
            mViewBinding.codeLinear.setVisibility(View.VISIBLE);
            mViewBinding.codeStarLinear.setVisibility(View.GONE);
            mViewBinding.tvChangeUser.setText(Html.fromHtml("<u>" + getString(R.string.register_star_title) + "</u>"));
            mViewBinding.tvChangeUser.setTextColor(getResources().getColor(R.color.gold));
        } else {
            mViewBinding.tvRegisterTitle.setText(R.string.register_star_title);
            mViewBinding.registerAccountEdit.setHint(R.string.telephone_star_input_tip);
            mViewBinding.registerAccountEdit.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            mViewBinding.codeLinear.setVisibility(View.GONE);
            mViewBinding.codeStarLinear.setVisibility(View.VISIBLE);
            mViewBinding.tvChangeUser.setText(Html.fromHtml("<u>注册普通账户</u>"));
            mViewBinding.tvChangeUser.setTextColor(getResources().getColor(R.color.text_tiffany));
        }

        starIsRegister = !starIsRegister;
    }

    @Override
    public void getVerCode() {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.REGISTER.getCode() + "01");
        mViewBinding.registerCodeEdit.setFocusable(true);
        mViewBinding.registerCodeEdit.setFocusableInTouchMode(true);
        mViewBinding.registerCodeEdit.requestFocus();
        timer.start();
        String phoneNum = mViewBinding.registerAccountEdit.getText().toString().trim();
        SMSSDK.getVerificationCode("86", phoneNum);
    }

    @Override
    public void registerClickable(boolean flag) {
        mViewBinding.btnRegister.setClickable(flag);
    }

    @Override
    public void registerSuccess() {
        showToast("注册成功");
        onBackPressed();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {

            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        SaveBehaviourDataService.startAction(this, BehaviourEnum.REGISTER.getCode() + "00");
    }

    @Override
    public void onBackPressed() {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.REGISTER.getCode() + "04");
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.REGISTER.getCode() + "xx");
        super.onStop();
    }

    private static class SendCodeHandler extends Handler {
        private Context context;

        private SendCodeHandler(Context context, Looper mainLooper) {
            super(mainLooper);
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    // 验证码已经发送
                    ToastUtil.showShort(context, "验证码已经发送");
                }
            } else {
                // 根据服务器返回的网络错误，给toast提示
                try {
                    ((Throwable) data).printStackTrace();
                    Throwable throwable = (Throwable) data;

                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");
                    if (!TextUtils.isEmpty(des)) {
                        ToastUtil.showShort(context, des);
                        return;
                    }
                } catch (Exception e) {
                    SMSLog.getInstance().w(e);
                }
                // 如果木有找到资源，默认提示
                ToastUtil.showShort(context, R.string.connect_failuer_toast);
            }
        }

    }

    private static class TimeCount extends CountDownTimer {
        private TextView mGetCode;

        private TimeCount(TextView mGetCode, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
            this.mGetCode = mGetCode;
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
            mGetCode.setClickable(true);
            mGetCode.setText(R.string.get_code_again);
        }

        /**
         * 计时过程显示
         */
        @Override
        public void onTick(long millisUntilFinished) {
            mGetCode.setClickable(false);
            mGetCode.setText(mGetCode.getContext().getString(R.string.get_code_again)
                    + millisUntilFinished / 1000 + "秒");
        }
    }
}
