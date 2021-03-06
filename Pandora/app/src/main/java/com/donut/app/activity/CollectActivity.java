package com.donut.app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.fragment.CollectItemFragment;
import com.donut.app.fragment.CollectWishFragment;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的收藏
 */
public class CollectActivity extends BaseActivity {

    @ViewInject(R.id.collect_viewpager)
    private ViewPager viewPager;

    @ViewInject(R.id.collect_tv_subject)
    private TextView tvSubject;

    @ViewInject(R.id.collect_tv_challenge)
    private TextView tvChallenge;

    @ViewInject(R.id.collect_tv_ip)
    private TextView tvIp;

    private MyViewPagerAdapter mAdapter;

    private List<Fragment> fragmentList = new ArrayList<>();

    private static final int ORDER_TYPE_SUBJECT = 0, ORDER_TYPE_CHALLENGE = 1, ORDER_TYPE_IP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SaveBehaviourDataService.startAction(this, BehaviourEnum.MY_COLLECT.getCode() + "00");
    }

    @Override
    protected void onStop() {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.MY_COLLECT.getCode() + "xx");
        super.onStop();
    }

    private void initView() {

        updateHeadTitle("我的收藏", true);
        setViewText(ORDER_TYPE_SUBJECT);
        CollectItemFragment subjectColFragment
                = CollectItemFragment.newInstance(ORDER_TYPE_SUBJECT);
        fragmentList.add(subjectColFragment);
        CollectItemFragment challengeColFragment
                = CollectItemFragment.newInstance(ORDER_TYPE_CHALLENGE);
        fragmentList.add(challengeColFragment);
        CollectWishFragment IPColFragment
                = CollectWishFragment.newInstance();
        fragmentList.add(IPColFragment);

        viewPager.addOnPageChangeListener(new ViewPageChangeListener());
        mAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(mAdapter);
    }

    @OnClick({R.id.collect_tv_subject,
            R.id.collect_tv_challenge,
            R.id.collect_tv_ip})
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.collect_tv_subject:
                setViewText(ORDER_TYPE_SUBJECT);
                break;
            case R.id.collect_tv_challenge:
                setViewText(ORDER_TYPE_CHALLENGE);
                break;
            case R.id.collect_tv_ip:
                setViewText(ORDER_TYPE_IP);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SaveBehaviourDataService.startAction(this, BehaviourEnum.MY_COLLECT.getCode() + "07");
    }

    private final class ViewPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int index) {
            setViewText(index);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    private void setViewText(int index) {
        switch (index) {
            case ORDER_TYPE_SUBJECT:
                tvSubject.setSelected(true);
                tvSubject.setTextSize(16f);
                tvChallenge.setSelected(false);
                tvChallenge.setTextSize(14f);
                tvIp.setSelected(false);
                tvIp.setTextSize(14f);
                viewPager.setCurrentItem(0);
                break;

            case ORDER_TYPE_CHALLENGE:
                tvSubject.setSelected(false);
                tvSubject.setTextSize(14f);
                tvIp.setSelected(false);
                tvIp.setTextSize(14f);
                tvChallenge.setSelected(true);
                tvChallenge.setTextSize(16f);
                viewPager.setCurrentItem(1);
                break;

            case ORDER_TYPE_IP:
                tvSubject.setSelected(false);
                tvSubject.setTextSize(14f);
                tvIp.setSelected(true);
                tvIp.setTextSize(16f);
                tvChallenge.setSelected(false);
                tvChallenge.setTextSize(14f);
                viewPager.setCurrentItem(2);
                break;
        }
    }


    public static class MyViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;

        MyViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
