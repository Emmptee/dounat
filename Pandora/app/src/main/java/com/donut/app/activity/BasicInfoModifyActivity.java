package com.donut.app.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.entity.UserInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.EditUserInfoResponse;
import com.donut.app.http.message.PersonalModifyRequest;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.Timer;
import java.util.TimerTask;

public class BasicInfoModifyActivity extends BaseActivity
{
    @ViewInject(R.id.edit_input)
    private EditText mInputEt;

    @ViewInject(R.id.head_right_tv)
    private TextView mRight;

    public static final String CONTENT = "content";

    private String content;

    public static final String TYPE = "type";

    public static final int TYPE_NICKNAME = 0, TYPE_AGE = 1, TYPE_PROFESSIONAL = 3;

    private int type = -1;

    public static final int PERSONAL_MODIFY_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info_modify);
        ViewUtils.inject(this);
        StatusBarUtil.setColor(BasicInfoModifyActivity.this, Constant.default_bar_color);
        showKeyBoard();
        type = getIntent().getIntExtra(TYPE, -1);
        String title = null;
        if (type == TYPE_NICKNAME)
        {
            title = getString(R.string.nickname);
            mInputEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            mInputEt.setHint("请输入昵称");
        } else if (type == TYPE_AGE)
        {
            title = getString(R.string.age);
            mInputEt.setInputType(InputType.TYPE_CLASS_NUMBER);
            mInputEt.setHint("请输入年龄");
        } else if (type == TYPE_PROFESSIONAL)
        {
            title = getString(R.string.professional);
            mInputEt.setHint("请输入职业");
        }
        updateHeadTitle(title, true);
        mRight.setText(getString(R.string.save));
        content = getIntent().getStringExtra(CONTENT);
        mInputEt.setText(content);
        mInputEt.setSelection(mInputEt.getText().length());
    }

    private void showKeyBoard()
    {
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
                       {
                           @Override
                           public void run()
                           {
                               InputMethodManager inputManager =
                                       (InputMethodManager) mInputEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(mInputEt, 0);
                           }

                       },
                500);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if (type == TYPE_NICKNAME)
        {
            SaveBehaviourDataService.startAction(this, BehaviourEnum.NICKNAME_MODIFY.getCode() + "00");
        } else if (type == TYPE_AGE)
        {
            SaveBehaviourDataService.startAction(this, BehaviourEnum.AGE_MODIFY.getCode() + "00");
        } else if (type == TYPE_PROFESSIONAL)
        {
            SaveBehaviourDataService.startAction(this, BehaviourEnum.PROFESSIONAL_MODIFY.getCode() + "00");
        }

    }

    @Override
    protected void onStop()
    {
        if (type == TYPE_NICKNAME)
        {
            SaveBehaviourDataService.startAction(this, BehaviourEnum.NICKNAME_MODIFY.getCode() + "xx");
        } else if (type == TYPE_AGE)
        {
            SaveBehaviourDataService.startAction(this, BehaviourEnum.AGE_MODIFY.getCode() + "xx");
        } else if (type == TYPE_PROFESSIONAL)
        {
            SaveBehaviourDataService.startAction(this, BehaviourEnum.PROFESSIONAL_MODIFY.getCode() + "xx");
        }
        super.onStop();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        if (type == TYPE_NICKNAME)
        {
            SaveBehaviourDataService.startAction(this, BehaviourEnum.NICKNAME_MODIFY.getCode() + "02");
        } else if (type == TYPE_AGE)
        {
            SaveBehaviourDataService.startAction(this, BehaviourEnum.AGE_MODIFY.getCode() + "02");
        } else if (type == TYPE_PROFESSIONAL)
        {
            SaveBehaviourDataService.startAction(this, BehaviourEnum.PROFESSIONAL_MODIFY.getCode() + "02");
        }
    }

    PersonalModifyRequest request;

    private void requestData()
    {
        content = mInputEt.getText().toString();
        request = new PersonalModifyRequest();
        String emptytip = "";
        switch (type)
        {
            case TYPE_NICKNAME:
                request.setNickName(content);
                emptytip = "昵称不能为空";
                break;
            case TYPE_AGE:
                request.setAge(content);
                emptytip = "年龄不能为空";
                break;
            case TYPE_PROFESSIONAL:
                request.setJob(content);
                emptytip = "职业不能为空";
                break;
        }
        if (TextUtils.isEmpty(content))
        {
            ToastUtil.showShort(this, emptytip);
            return;
        }
        sendNetRequest(request, HeaderRequest.MODIFY_PERSONAL_INFO, PERSONAL_MODIFY_REQUEST,
                true);
        saveBehaviour();
    }

    private void saveBehaviour()
    {
        if (type == TYPE_NICKNAME)
        {
            SaveBehaviourDataService.startAction(this, BehaviourEnum.NICKNAME_MODIFY.getCode() + "01", request, HeaderRequest.MODIFY_PERSONAL_INFO);
        } else if (type == TYPE_AGE)
        {
            SaveBehaviourDataService.startAction(this, BehaviourEnum.AGE_MODIFY.getCode() + "01", request, HeaderRequest.MODIFY_PERSONAL_INFO);
        } else if (type == TYPE_PROFESSIONAL)
        {
            SaveBehaviourDataService.startAction(this, BehaviourEnum.PROFESSIONAL_MODIFY.getCode() + "01", request, HeaderRequest.MODIFY_PERSONAL_INFO);
        }
    }

    @OnClick({R.id.menu})
    private void viewOnClick(View v)
    {
        switch (v.getId())
        {
            case R.id.menu:
                //保存
                requestData();
                break;
        }
    }


    @Override
    public void onSuccess(String response, String url, int actionId)
    {
        super.onSuccess(response, url, actionId);
        switch (actionId)
        {
            case PERSONAL_MODIFY_REQUEST:
                EditUserInfoResponse res = JsonUtils.fromJson(response,
                        EditUserInfoResponse.class);
                if (COMMON_SUCCESS.equals(res.getCode()))
                {
                    UserInfo info = getUserInfo();
                    if (type == 0)
                    {
                        info.setNickName(content);
                    }
                    setUserInfo(info, true);
                    if (res.getIsFirst() == 1)
                    {
                        saveFirstComplete();
                    }
                    onBack();
                } else
                {
                    ToastUtil.showShort(this, res.getMsg());
                }
                break;
        }
    }

    private void saveFirstComplete()
    {
        SharedPreferences.Editor editor = sp_Info.edit();
        editor.putLong(Constant.VIP_TIME, System.currentTimeMillis());
        editor.commit();
    }

    public void onBack()
    {
        Intent intent = new Intent();
        intent.putExtra(CONTENT, content);
        setResult(RESULT_OK, intent);
        onBackPressed();
    }
}
