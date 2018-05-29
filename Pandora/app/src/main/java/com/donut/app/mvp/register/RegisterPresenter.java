package com.donut.app.mvp.register;

import android.content.Context;
import android.text.TextUtils;

import com.donut.app.R;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.RegisterRequest;
import com.donut.app.http.message.ValidateRegPhoneRequest;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public class RegisterPresenter extends RegisterContract.Presenter {

    private static final int REGISTER_REQUEST = 0;
    private static final int PHONE_REQUEST = 1;

    void phoneValidate(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            mView.showToastMsg(R.string.telphone_empty_tip);
            return;
        }
        ValidateRegPhoneRequest request = new ValidateRegPhoneRequest();
        request.setPhone(phoneNum);
        request.setValidateType("0");
        super.loadData(request, HeaderRequest.VERIFICATY_PHONE, PHONE_REQUEST, true);
    }

    @Override
    public boolean register(String phoneNum, String password, String code, Context context) {
        if (TextUtils.isEmpty(phoneNum)) {
            mView.showToastMsg(R.string.telphone_empty_tip);
            return false;
        }
//        if (!FormatCheckUtil.isMobileNumber(phoneNum)) {
//            ToastUtil.showShort(this, getString(R.string.telphone_check_tip));
//            return;
//        }
        if (TextUtils.isEmpty(password)) {
            mView.showToastMsg(R.string.password_empty_tip);
            return false;
        }
        if (password.length() < 6 || password.length() > 32) {
            mView.showToastMsg(R.string.password_check_tip);
            return false;
        }

        if (TextUtils.isEmpty(code)) {
            mView.showToastMsg(R.string.code_input_tip);
            return false;
        }
        RegisterRequest request = new RegisterRequest();
        request.setRegPhone(phoneNum);
        request.setPasswd(password);
        request.setRegPlatform("0");
        request.setValidateCode(code);
        request.setPhoneType("0");

        SaveBehaviourDataService.startAction(context, BehaviourEnum.REGISTER.getCode() + "03",
                request, HeaderRequest.REGISTER);
        super.loadData(request, HeaderRequest.REGISTER, REGISTER_REQUEST,
                true);
        return true;
    }

    @Override
    public boolean registerStar(String phoneNum, String password, String starCode, Context context) {
        if (TextUtils.isEmpty(phoneNum)) {
            mView.showToastMsg(R.string.telephone_star_empty_tip);
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            mView.showToastMsg(R.string.password_empty_tip);
            return false;
        }
        if (password.length() < 6 || password.length() > 32) {
            mView.showToastMsg(R.string.password_check_tip);
            return false;
        }

        if (TextUtils.isEmpty(starCode)) {
            mView.showToastMsg(R.string.code_star_empty_tip);
            return false;
        }
        RegisterRequest request = new RegisterRequest();
        request.setRegPhone(phoneNum);
        request.setPasswd(password);
        request.setRegPlatform("0");
        request.setInvitationCode(starCode);
        request.setPhoneType("0");

        SaveBehaviourDataService.startAction(context, BehaviourEnum.REGISTER.getCode() + "03",
                request, HeaderRequest.REGISTER_STAR);
        super.loadData(request, HeaderRequest.REGISTER_STAR, REGISTER_REQUEST,
                true);
        return true;
    }

    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        switch (actionId) {
            case PHONE_REQUEST:
                // 验证手机号是否注册，再获取验证码
                BaseResponse phoneRes = JsonUtils.fromJson(responseJson,
                        BaseResponse.class);
                if (COMMON_SUCCESS.equals(phoneRes.getCode())) {
                    mView.getVerCode();
                } else {
                    showToast(phoneRes.getMsg());
                }
                break;
            case REGISTER_REQUEST:
                mView.registerClickable(true);
                BaseResponse res = JsonUtils.fromJson(responseJson,
                        BaseResponse.class);

                if (COMMON_SUCCESS.equals(res.getCode())) {
                    mView.registerSuccess();
                } else {
                    mView.showToastMsg(res.getMsg());
                }
                break;
        }
    }

    @Override
    public void onError(String errorMsg, String url, int actionId) {
        super.onError(errorMsg, url, actionId);
        mView.registerClickable(true);
    }
}
