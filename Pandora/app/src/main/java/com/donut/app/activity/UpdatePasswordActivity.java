package com.donut.app.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.entity.UserInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.UserPasswdChangeRequest;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class UpdatePasswordActivity extends BaseActivity {

    @ViewInject(R.id.head_right_tv)
    private TextView tvRight;

    @ViewInject(R.id.update_pass_et_old_pass)
    private EditText etOldPass;

    @ViewInject(R.id.update_pass_et_new_pass)
    private EditText etNewPass;

    @ViewInject(R.id.update_pass_et_new_pass2)
    private EditText etNewPass2;

    private static final int UPDATE_PASS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        updateHeadTitle(getString(R.string.change_password), true);
        tvRight.setText(R.string.save);
    }

    @OnClick({R.id.menu})
    private void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.menu:
                saveInfo();
                break;
        }
    }

    private void saveInfo() {
        if (TextUtils.isEmpty(etOldPass.getText())) {
            showToast("旧密码不能为空");
            return;
        } else if (etOldPass.getText().length() < 6) {
            showToast("密码位数最少6位");
            return;
        } else if (TextUtils.isEmpty(etNewPass.getText())) {
            showToast("新密码不能为空");
            return;
        } else if (etNewPass.getText().length() < 6) {
            showToast("密码位数最少6位");
            return;
        } else if (!etNewPass.getText().toString().equals(etNewPass2.getText().toString())) {
            showToast("两次密码输入不一致");
            return;
        }
        UserPasswdChangeRequest request = new UserPasswdChangeRequest();
        request.setPasswd(etOldPass.getText().toString());
        request.setNewPasswd(etNewPass.getText().toString());
        request.setConfirmPasswd(etNewPass2.getText().toString());
        sendNetRequest(request, HeaderRequest.UPDATE_PASS, UPDATE_PASS);
        SaveBehaviourDataService.startAction(this,
                BehaviourEnum.UPDATE_PASS.getCode() + "01",
                request,
                HeaderRequest.UPDATE_PASS);
    }

    @Override
    public void onSuccess(String response, String url, int actionId) {
        super.onSuccess(response, url, actionId);
        switch (actionId) {
            case UPDATE_PASS:
                BaseResponse baseResponse = JsonUtils.fromJson(response, BaseResponse.class);
                if (COMMON_SUCCESS.equals(baseResponse.getCode())) {
                    showToast("密码修改成功,请重新登录");
                    setUserInfo(new UserInfo(), false);
                    finish();
                } else {
                    showToast(baseResponse.getMsg());
                }
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        saveBehaviour("02");
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
        SaveBehaviourDataService.startAction(this, BehaviourEnum.UPDATE_PASS.getCode() + functionCode);
    }
}
