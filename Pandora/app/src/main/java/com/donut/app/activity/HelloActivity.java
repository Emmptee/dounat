/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * @Title: HelloActivity.java
 * @Package com.golawyer.client.activity
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 亓振刚
 * @date 2014-12-2 下午8:01:05
 * @version V1.0
 */
package com.donut.app.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.Constant;
import com.donut.app.utils.VersionUtil;
import com.donut.app.utils.status_bar.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: HelloActivity
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 亓振刚
 * @date 2014-12-2 下午8:01:05
 * @version 1.0
 */
public class HelloActivity extends BaseActivity implements
        OnPageChangeListener, OnTouchListener {

    private ViewPager mPager;// 页卡内容

    private ViewPagerAdapter vpAdapter;

    private View goBtn;

    private List<View> views;

    // 引导图片资源
    private static final int[] pics = {R.drawable.new_guide1, R.drawable.new_guide2, R.drawable.new_guide3, R.drawable.new_guide4};

    private int currentIndex;

    private int lastX = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_activity);

        StatusBarCompat.translucentStatusBar(this);

        goBtn = findViewById(R.id.iv_hello_go_home);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result();
            }
        });

        views = new ArrayList<>();

        ViewGroup.LayoutParams mParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // 初始化引导图片列表
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setImageResource(pics[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            views.add(iv);
        }
        mPager = (ViewPager) findViewById(R.id.hello_vpager);
        // 初始化Adapter
        vpAdapter = new ViewPagerAdapter(views);
        mPager.setAdapter(vpAdapter);
        mPager.setOnPageChangeListener(this);
        mPager.setOnTouchListener(this);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int arg0) {
        currentIndex = arg0;
        if (currentIndex == views.size() - 1) {
            goBtn.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    result();
                }
            }, 5000);
        } else {
            goBtn.setVisibility(View.GONE);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if ((lastX - event.getX()) > 50
                        && (currentIndex == views.size() - 1)) {
                    result();
                }
                break;
            default:
                break;
        }
        return false;
    }

    private void result() {

        if (!isFinishing()) {
            sp_Info.edit().putInt(Constant.NOW_VERSION_CODE, VersionUtil.getVersionCode(this))
                    .apply();
            setResult(RESULT_OK);
            finish();
        }

    }

    public class ViewPagerAdapter extends PagerAdapter {

        // 界面列表
        private List<View> views;

        public ViewPagerAdapter(List<View> views) {
            this.views = views;
        }

        // 销毁arg1位置的界面
        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(views.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        // 获得当前界面数
        @Override
        public int getCount() {
            if (views != null) {
                return views.size();
            }

            return 0;
        }

        // 初始化arg1位置的界面
        @Override
        public Object instantiateItem(View arg0, int arg1) {

            ((ViewPager) arg0).addView(views.get(arg1), 0);

            return views.get(arg1);
        }

        // 判断是否由对象生成界面
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub

        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean flag = false;
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        } else {
            flag = super.onKeyUp(keyCode, event);
        }
        return flag;
    }

}