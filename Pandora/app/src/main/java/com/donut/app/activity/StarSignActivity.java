package com.donut.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.EditUserInfoResponse;
import com.donut.app.http.message.PersonalModifyRequest;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.CommonUtils;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class StarSignActivity extends BaseActivity
{
    @ViewInject(R.id.radiogroup)
    private RadioGroup mRadioGroup;

    @ViewInject(R.id.shuipin_rb)
    private RadioButton mShuipinRb;
    @ViewInject(R.id.shuangyu_rb)
    private RadioButton mShuangyuRb;
    @ViewInject(R.id.baiyang_rb)
    private RadioButton mBaiyangRb;
    @ViewInject(R.id.jinniu_rb)
    private RadioButton mJinniuRb;
    @ViewInject(R.id.shuangzi_rb)
    private RadioButton mShuangziRb;
    @ViewInject(R.id.juxie_rb)
    private RadioButton mJuxieRb;
    @ViewInject(R.id.shizi_rb)
    private RadioButton mShiziRb;
    @ViewInject(R.id.chunv_rb)
    private RadioButton mChunvRb;
    @ViewInject(R.id.tianchen_rb)
    private RadioButton mTianChenRb;
    @ViewInject(R.id.tianxie_rb)
    private RadioButton mTianXieRb;
    @ViewInject(R.id.sheshou_rb)
    private RadioButton mSheshouRb;
    @ViewInject(R.id.mojie_rb)
    private RadioButton mMojieRb;

    @ViewInject(R.id.head_right_tv)
    private TextView mRight;

    public static final String SIGN="sign";

    private int star;

    private static final int PERSONAL_MODIFY_REQUEST=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_sign_modify);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        star=getIntent().getIntExtra(SIGN,1);
        updateHeadTitle(getString(R.string.star_sign),true);
        mRight.setText(getString(R.string.save));
        initData();
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                checkChanged(checkedId);
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        SaveBehaviourDataService.startAction(this, BehaviourEnum.SIGN_MODIFY.getCode()+"00");
    }

    @Override
    protected void onStop() {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.SIGN_MODIFY.getCode() + "xx");
        super.onStop();
    }

    private void initData(){
        String str= CommonUtils.setStar(this,star);
        if(str.equals(mShuipinRb.getText().toString())){
            mShuipinRb.setChecked(true);
        }
        if(str.equals(mShuangyuRb.getText().toString())){
            mShuangyuRb.setChecked(true);
        }
        if(str.equals(mBaiyangRb.getText().toString())){
            mBaiyangRb.setChecked(true);
        }
        if(str.equals(mJinniuRb.getText().toString())){
            mJinniuRb.setChecked(true);
        }
        if(str.equals(mShuangziRb.getText().toString())){
            mShuangziRb.setChecked(true);
        }
        if(str.equals(mJuxieRb.getText().toString())){
            mJuxieRb.setChecked(true);
        }
        if(str.equals(mShiziRb.getText().toString())){
            mShiziRb.setChecked(true);
        }
        if(str.equals(mChunvRb.getText().toString())){
            mChunvRb.setChecked(true);
        }
        if(str.equals(mTianChenRb.getText().toString())){
            mTianChenRb.setChecked(true);
        }
        if(str.equals(mTianXieRb.getText().toString())){
            mTianXieRb.setChecked(true);
        }
        if(str.equals(mSheshouRb.getText().toString())){
            mSheshouRb.setChecked(true);
        }
        if(str.equals(mMojieRb.getText().toString())){
            mMojieRb.setChecked(true);
        }
    }

    private void checkChanged(int checkedId){
        switch (checkedId){
            case R.id.shuipin_rb:
                star=1;
                break;
            case R.id.shuangyu_rb:
                star=2;
                break;
            case R.id.baiyang_rb:
                star=3;
                break;
            case R.id.jinniu_rb:
                star=4;
                break;
            case R.id.shuangzi_rb:
                star=5;
                break;
            case R.id.juxie_rb:
                star=6;
                break;
            case R.id.shizi_rb:
                star=7;
                break;
            case R.id.chunv_rb:
                star=8;
                break;
            case R.id.tianchen_rb:
                star=9;
                break;
            case R.id.tianxie_rb:
                star=10;
                break;
            case R.id.sheshou_rb:
                star=11;
                break;
            case R.id.mojie_rb:
                star=12;
                break;
        }
    }

    @OnClick({R.id.menu})
    private void viewOnClick(View v){
        switch (v.getId()){

            case R.id.menu:
                //保存
                requestData();
                break;
        }
    }

    private void requestData()
    {
        PersonalModifyRequest request=new PersonalModifyRequest();
        request.setStar(star+"");
        sendNetRequest(request, HeaderRequest.MODIFY_PERSONAL_INFO, PERSONAL_MODIFY_REQUEST,
                true);
        SaveBehaviourDataService.startAction(this, BehaviourEnum.SIGN_MODIFY.getCode()+"01",request,HeaderRequest.MODIFY_PERSONAL_INFO);
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
        intent.putExtra(SIGN,star);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.SIGN_MODIFY.getCode() + "02");
        super.onBackPressed();
    }
}
