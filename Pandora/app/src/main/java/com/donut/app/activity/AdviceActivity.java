/**   
 *  Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * @Title: StudyInfoActivity.java 
 * @Package com.bis.sportedu.activity 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Administrator  
 * @date 2015-8-31 下午4:43:12 
 * @version V1.0   
 */
package com.donut.app.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.AdviceRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @ClassName: AdviceActivity
 * @Description:意见建议
 * @author wujiaojiao
 * @date 2016-7-15 下午4:43:12
 * @version 1.0
 */
public class AdviceActivity extends BaseActivity
{
    @ViewInject(R.id.advice_et)
    private EditText mAdviceEt;

    @ViewInject(R.id.connect_et)
    private EditText mConnectEt;

    @ViewInject(R.id.head_right_tv)
    private TextView mRight;

    private CharSequence temp;//监听前的文本
    private int editStart;//光标开始位置
    private int editEnd;//光标结束位置

    private static final int ADVICE_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advice_layout);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        updateHeadTitle(getString(R.string.feedback), true);
        initView();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        SaveBehaviourDataService.startAction(this, BehaviourEnum.ADVICE.getCode()+"00");
    }

    private void initView(){

        mAdviceEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                /** 得到光标开始和结束位置 ,超过最大数后记录刚超出的数字索引进行控制 */
                editStart = mAdviceEt.getSelectionStart();
                editEnd = mAdviceEt.getSelectionEnd();
                if (temp.length() > Constant.LENGTH_512)
                {
                    ToastUtil.showShort(AdviceActivity.this, getString(R.string.feedback_check_tips));
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    mAdviceEt.setText(s);
                    mAdviceEt.setSelection(tempSelection);
                }
            }
        });


        mConnectEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                /** 得到光标开始和结束位置 ,超过最大数后记录刚超出的数字索引进行控制 */
                editStart = mConnectEt.getSelectionStart();
                editEnd = mConnectEt.getSelectionEnd();
                if (temp.length() > Constant.LENGTH_64)
                {
                    ToastUtil.showShort(AdviceActivity.this, getString(R.string.connect_check_tips));
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    mConnectEt.setText(s);
                    mConnectEt.setSelection(tempSelection);
                }
            }
        });
    }

    @OnClick(value = {R.id.btn_submit})
    private void btnClick(View v)
    {
        switch (v.getId())
        {
        case R.id.btn_submit:
            senAdvice();
            break;
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        SaveBehaviourDataService.startAction(this, BehaviourEnum.ADVICE.getCode()+"02");
    }

    @Override
    protected void onStop() {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.ADVICE.getCode() + "xx");
        super.onStop();
    }

    private void senAdvice()
    {
        String content = mAdviceEt.getText().toString();
        String connect = mConnectEt.getText().toString();
        if (TextUtils.isEmpty(content.trim()))
        {
            ToastUtil.showShort(this, getString(R.string.advice_empty_tips));
            return;
        }
        mRight.setClickable(false);
        AdviceRequest request = new AdviceRequest();
        if (getUserInfo().getUserId() != null) {
            request.setUserId(getUserInfo().getUserId());
        } else {
            request.setPushChannelid(sp_Info.getString(Constant.PUSH_CHANNEL_ID, ""));
        }
        request.setContent(content);
        request.setContact(connect);
        sendNetRequest(request, HeaderRequest.ADVICE, ADVICE_REQUEST);
        SaveBehaviourDataService.startAction(this, BehaviourEnum.ADVICE.getCode()+"01",request,HeaderRequest.ADVICE);
    }

    @Override
    public void onSuccess(String response, String url, int actionId)
    {
        super.onSuccess(response, url, actionId);
        switch (actionId)
        {
        case ADVICE_REQUEST:
            BaseResponse res = JsonUtils.fromJson(response, BaseResponse.class);
            mRight.setClickable(true);
            if (COMMON_SUCCESS.equals(res.getCode())) {
                showToast(getString(R.string.feedback_success));
                finish();
            } else {
                showToast(res.getMsg());
            }
            break;
        default:
            break;
        }
    }
}
