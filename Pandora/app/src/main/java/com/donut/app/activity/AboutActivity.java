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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.StatusBarUtil;
import com.donut.app.utils.VersionUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;


public class AboutActivity extends BaseActivity
{
    @ViewInject(R.id.version_txt)
    private TextView mVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
        ViewUtils.inject(this);
        StatusBarUtil.setColor(AboutActivity.this, Constant.default_bar_color);
        updateHeadTitle(getString(R.string.about_app), true);
        mVersion.setText("v " + VersionUtil.getVersion(this));
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        SaveBehaviourDataService.startAction(this, BehaviourEnum.ABOUT_APP.getCode()+"00");
    }

    @OnClick(value = { R.id.to_comment})
    private void btnClick(View v)
    {
        switch (v.getId())
        {
            case R.id.to_comment:
                SaveBehaviourDataService.startAction(this, BehaviourEnum.ABOUT_APP.getCode()+"01");
                Uri uri = Uri.parse("market://details?id="+getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        SaveBehaviourDataService.startAction(this, BehaviourEnum.ABOUT_APP.getCode()+"02");
    }

    @Override
    protected void onStop() {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.ABOUT_APP.getCode() + "xx");
        super.onStop();
    }

}
