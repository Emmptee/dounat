package com.donut.app.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.fragment.base.BaseFragment;
import com.donut.app.mvp.shakestar.attention.ShakeStarAttentionFragment;
import com.donut.app.mvp.shakestar.commend.ShakeStarCommendFragment;
import com.donut.app.mvp.shakestar.search.ShakeStarSearchFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.umeng.qq.tencent.l;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hard on 2018/1/26.
 */

public class ShakeStarFragment extends BaseFragment implements View.OnClickListener {


    private static final String TAG = "ShakeStarFragment";

    @ViewInject(R.id.shake_viewpager)
    private ViewPager viewpager;
    @ViewInject(R.id.shake_gz)
    private TextView shake_gz;
    @ViewInject(R.id.shake_tj)
    private TextView shake_tj;
    @ViewInject(R.id.shake_ss)
    private TextView shake_ss;
    private List<Fragment> fragments;
    private List<TextView> texts;
    //代表着三个页面的下标
    private static final int CurrentItem_typeA=0;
    private static final int CurrentItem_typeB=1;
    private static final int CurrentItem_typeC=2;
    private int k=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //找到对应的fragment布局
        View mLayoutView = inflater.inflate(R.layout.fragment_shake_star_layout,
                container, false);
        ViewUtils.inject(this, mLayoutView);
        return mLayoutView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEvent();
        loadData();

    }


    private void loadData() {


    }

    private void initEvent() {
        fragments= new ArrayList<>();
        texts = new ArrayList<>();
        shake_gz.setOnClickListener(this);
        shake_tj.setOnClickListener(this);
        shake_ss.setOnClickListener(this);
        fragments.add(new ShakeStarAttentionFragment());
        fragments.add(new ShakeStarCommendFragment());
        fragments.add(new ShakeStarSearchFragment());
        texts.add(shake_gz);
        texts.add(shake_tj);
        texts.add(shake_ss);
        //设置缓存页数
        viewpager.setOffscreenPageLimit(3);
        //fragment+viewpager
        viewpager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        texts.get(CurrentItem_typeB).setTextColor(Color.parseColor("#ffffff"));
        viewpager.setCurrentItem(CurrentItem_typeB);
        k=1;
        //viewpager滑动监听
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < texts.size(); i++) {
                    if (position == i) {
                        texts.get(i).setTextColor(Color.parseColor("#ffffff"));
                    } else {
                        texts.get(i).setTextColor(Color.parseColor("#80ffffff"));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                GSYVideoManager.onPause();
            }
        });

    }


    //点击切换不同的fragment页面
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shake_gz:
                viewpager.setCurrentItem(CurrentItem_typeA);
                break;
            case R.id.shake_tj:
                viewpager.setCurrentItem(CurrentItem_typeB);
                break;
            case R.id.shake_ss:
                viewpager.setCurrentItem(CurrentItem_typeC);
                break;
        }
    }
}
