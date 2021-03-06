package com.donut.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.fragment.ChallengeItemFragment;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * 挑战区
 */
public class ChallengeActivity extends BaseActivity {

    @ViewInject(R.id.challenge_viewpager)
    private ViewPager viewPager;

    @ViewInject(R.id.challenge_tv_new)
    private TextView tvNew;

    @ViewInject(R.id.challenge_tv_more)
    private TextView tvMore;

    @ViewInject(R.id.challenge_tv_strong)
    private TextView tvStrong;

    @ViewInject(R.id.head_right_iv)
    private ImageView ivRight;

    private MyViewPagerAdapter mAdapter;

    private List<Fragment> fragmentList = new ArrayList<Fragment>();

    private static final int ORDER_TYPE_NEW = 0, ORDER_TYPE_STRONG = 1, ORDER_TYPE_MORE = 2;

    public static final String SUBJECT_ID = "SUBJECT_ID";

    private String subjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        initView();
    }

    private void initView() {
        updateHeadTitle(getString(R.string.challenge_title),true);
        ivRight.setVisibility(View.VISIBLE);
        setViewText(ORDER_TYPE_NEW);

        subjectId = getIntent().getStringExtra(SUBJECT_ID);

        ChallengeItemFragment newFragment
                = ChallengeItemFragment.newInstance(ORDER_TYPE_NEW, subjectId);
        fragmentList.add(newFragment);
        ChallengeItemFragment strongFragment
                = ChallengeItemFragment.newInstance(ORDER_TYPE_STRONG, subjectId);
        fragmentList.add(strongFragment);
        ChallengeItemFragment moreFragment
                = ChallengeItemFragment.newInstance(ORDER_TYPE_MORE, subjectId);
        fragmentList.add(moreFragment);

        mAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new ViewPageChangeListener());

    }

    @OnClick({R.id.challenge_tv_new,
            R.id.challenge_tv_more,
            R.id.challenge_tv_strong,
            R.id.challenge_fab,
            R.id.menu})
    protected void viewOnClick(View v){
        switch (v.getId()){
            case R.id.challenge_tv_new:
                setViewText(ORDER_TYPE_NEW);
                viewPager.setCurrentItem(0);
                break;
            case R.id.challenge_tv_strong:
                setViewText(ORDER_TYPE_STRONG);
                viewPager.setCurrentItem(1);
                break;
            case R.id.challenge_tv_more:
                setViewText(ORDER_TYPE_MORE);
                viewPager.setCurrentItem(2);
                break;
            case R.id.challenge_fab:
                challenge();
                break;
            case R.id.menu:
                Bundle bundle = new Bundle();
                bundle.putString(ChallengeSearchActivity.SUBJECT_ID, subjectId);
                bundle.putInt(ChallengeSearchActivity.ORDER_TYPE, viewPager.getCurrentItem());
                launchActivity(ChallengeSearchActivity.class, bundle);
                break;
        }
    }

    private void challenge() {
        if (getLoginStatus()) {
            Intent intent = new Intent(this, ChallengeSendActivity.class);
            intent.putExtra(ChallengeSendActivity.SUBJECT_ID, subjectId);
            startActivity(intent);
        } else {
            showToast(getString(R.string.no_login_msg));
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private final class ViewPageChangeListener implements ViewPager.OnPageChangeListener
    {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {
        }

        @Override
        public void onPageSelected(int index)
        {
            setViewText(index);
        }

        @Override
        public void onPageScrollStateChanged(int state)
        {
        }
    }

    private void setViewText(int index) {
        switch (index) {
            case ORDER_TYPE_NEW:
                tvNew.setSelected(true);
                tvNew.setTextSize(16f);
                tvMore.setSelected(false);
                tvMore.setTextSize(14f);
                tvStrong.setSelected(false);
                tvStrong.setTextSize(14f);
                break;

            case ORDER_TYPE_STRONG:
                tvNew.setSelected(false);
                tvNew.setTextSize(14f);
                tvMore.setSelected(false);
                tvMore.setTextSize(14f);
                tvStrong.setSelected(true);
                tvStrong.setTextSize(16f);
                break;

            case ORDER_TYPE_MORE:
                tvNew.setSelected(false);
                tvNew.setTextSize(14f);
                tvMore.setSelected(true);
                tvMore.setTextSize(16f);
                tvStrong.setSelected(false);
                tvStrong.setTextSize(14f);
                break;
        }
    }


    public static class MyViewPagerAdapter extends FragmentPagerAdapter
    {

        private List<Fragment> fragmentList;

        public MyViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList)
        {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position)
        {
            return fragmentList.get(position);
        }

        @Override
        public int getCount()
        {
            return fragmentList.size();
        }
    }

    @Override
    public void onBackPressed() {
        saveBehaviour("05");
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
        SaveBehaviourDataService.startAction(this, BehaviourEnum.CHALLENGE.getCode() + functionCode);
    }
}
