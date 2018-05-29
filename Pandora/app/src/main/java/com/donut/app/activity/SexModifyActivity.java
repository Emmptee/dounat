package com.donut.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.EditUserInfoResponse;
import com.donut.app.http.message.PersonalModifyRequest;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class SexModifyActivity extends BaseActivity
{
    @ViewInject(R.id.man_choice)
    private ImageView mManChoice;
    @ViewInject(R.id.woman_choice)
    private ImageView mWomanChoice;

    @ViewInject(R.id.head_right_tv)
    private TextView mRight;


    public static final String SEX="SEX";

    private int sex;

    private static final int PERSONAL_MODIFY_REQUEST=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sex_modify);
        ViewUtils.inject(this);
        sex=getIntent().getIntExtra(SEX,-1);
        if(sex==0){
            mManChoice.setVisibility(View.VISIBLE);
            mWomanChoice.setVisibility(View.GONE);
        }else{
            mManChoice.setVisibility(View.GONE);
            mWomanChoice.setVisibility(View.VISIBLE);
        }
        updateHeadTitle(getString(R.string.sex),true);
        mRight.setText(getString(R.string.save));
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        SaveBehaviourDataService.startAction(this, BehaviourEnum.SEX_MODIFY.getCode()+"00");
    }

    @Override
    protected void onStop() {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.SEX_MODIFY.getCode() + "xx");
        super.onStop();
    }

    @Override
    public void onBackPressed()
    {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.SEX_MODIFY.getCode() + "02");
        super.onBackPressed();
    }

    @OnClick({R.id.man_linear,R.id.woman_linear,R.id.menu})
    private void viewOnClick(View v){
        switch (v.getId()){

            case R.id.man_linear:
                mManChoice.setVisibility(View.VISIBLE);
                mWomanChoice.setVisibility(View.GONE);
                sex=0;
                break;
            case R.id.woman_linear:
                mManChoice.setVisibility(View.GONE);
                mWomanChoice.setVisibility(View.VISIBLE);
                sex=1;
                break;
            case R.id.menu:
                //保存
                requestData();
                break;
        }
    }

    private void requestData()
    {
        PersonalModifyRequest request=new PersonalModifyRequest();
        request.setSex(sex+"");
        sendNetRequest(request, HeaderRequest.MODIFY_PERSONAL_INFO, PERSONAL_MODIFY_REQUEST,
                true);
        SaveBehaviourDataService.startAction(this, BehaviourEnum.SEX_MODIFY.getCode()+"01",request,HeaderRequest.MODIFY_PERSONAL_INFO);
    }

    @Override
    public void onSuccess(String response, String url, int actionId)
    {
        super.onSuccess(response, url, actionId);
        switch (actionId){
            case PERSONAL_MODIFY_REQUEST:
                EditUserInfoResponse res= JsonUtils.fromJson(response,
                        EditUserInfoResponse.class);
                if(COMMON_SUCCESS.equals(res.getCode())){
                    if (res.getIsFirst() == 1)
                    {
                        saveFirstComplete();
                    }
                    onBack();
                }else{
                    ToastUtil.showShort(this,res.getMsg());
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
        Intent intent=new Intent();
        intent.putExtra(SEX,sex);
        setResult(RESULT_OK,intent);
        finish();
    }
}
