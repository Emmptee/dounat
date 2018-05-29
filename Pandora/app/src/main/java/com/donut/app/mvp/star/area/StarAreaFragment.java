package com.donut.app.mvp.star.area;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.donut.app.R;
import com.donut.app.SysApplication;
import com.donut.app.activity.LoginActivity;
import com.donut.app.activity.StarChosenActivity;
import com.donut.app.activity.StarDetailActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.databinding.FragmentStarAreaLayoutBinding;
import com.donut.app.http.message.StarSubjectsResponse;
import com.donut.app.http.message.SubjectListDetail;
import com.donut.app.mvp.MVPBaseFragment;
import com.donut.app.mvp.star.notice.StarSendNoticeActivity;
import com.donut.app.service.SaveBehaviourDataService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qi on 2017/3/16.
 * Description : 明星专区 <br>
 */
public class StarAreaFragment extends MVPBaseFragment<FragmentStarAreaLayoutBinding, StarAreaPresenter>
        implements StarAreaContract.View, ViewPager.OnPageChangeListener {

    private StarAreaItemViewPagerAdapter mAdapter;

    private ArrayList<StarAreaItemFragment> fragmentList = new ArrayList<>();

    private List<SubjectListDetail> subjectList = new ArrayList<>();

    private int nowIndex;

    private static final int SEND_NOTICE_CODE = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_star_area_layout;
    }

    @Override
    protected void initView() {
        mViewBinding.setHandler(this);
        mAdapter = new StarAreaItemViewPagerAdapter(getChildFragmentManager(), fragmentList);
        mViewBinding.starAreaViewPager.setAdapter(mAdapter);
        mViewBinding.starAreaViewPager.addOnPageChangeListener(this);

        int w = getResources().getDisplayMetrics().widthPixels;
        ViewGroup.LayoutParams params = mViewBinding.starAreaBottomLayout.getLayoutParams();
        params.height = w * 180 / 750;
        mViewBinding.starAreaBottomLayout.setLayoutParams(params);
    }

    @Override
    protected void initEvent() {
    }

    @Override
    public void loadData() {
        mPresenter.loadData(true);
    }

    public void gotoStarDetail() {
        Bundle bundle = new Bundle();
        bundle.putString(StarDetailActivity.FKA01_ID, SysApplication.getUserInfo().getUserId());
        launchActivity(StarDetailActivity.class, bundle);
    }

    public void gotoStarChosen() {
        SaveBehaviourDataService.startAction(getActivity(), BehaviourEnum.STAR_ZONE.getCode() + "01");

        Bundle bundle = new Bundle();
        bundle.putString(StarChosenActivity.SUBJECT_ID, subjectList.get(nowIndex).getSubjectId());
        launchActivity(StarChosenActivity.class, bundle);
    }

    public void sendSubjectNotice() {
        SaveBehaviourDataService.startAction(getActivity(), BehaviourEnum.STAR_ZONE.getCode() + "04");
        launchActivityForResult(StarSendNoticeActivity.class, SEND_NOTICE_CODE);
    }

    @Override
    public void showView(StarSubjectsResponse detail) {

        ArrayList<StarAreaItemFragment> fragmentList = new ArrayList<>();
        subjectList.clear();
        List<SubjectListDetail> details = detail.getStarSubjects();
        if (details != null && details.size() > 0) {
            for (int i = 0; i < details.size(); i++) {
                SubjectListDetail subjectDetail = details.get(i);
                StarAreaItemFragment fragment = StarAreaItemFragment.newInstance(subjectDetail);
                fragmentList.add(fragment);
                subjectList.add(subjectDetail);
            }
        } else {
            SubjectListDetail subjectDetail = new SubjectListDetail();
            subjectDetail.setEmptyData(true);
            StarAreaItemFragment fragment = StarAreaItemFragment.newInstance(subjectDetail);
            fragmentList.add(fragment);
            subjectList.add(subjectDetail);
        }
        mAdapter.setFragments(fragmentList);
    }

    @Override
    public void expiresToken() {
        SysApplication.setUserInfo(null);
        SharedPreferences sp_Info = getContext().getSharedPreferences(Constant.SP_INFO,
                Context.MODE_PRIVATE);
        sp_Info.edit()
                .putBoolean(Constant.IS_LOGIN, false).apply();
        launchActivityForResult(LoginActivity.class, 999);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999
                && resultCode == Activity.RESULT_OK
                && SysApplication.getUserInfo().getUserType() == 1) {
            loadData();
        } else if (requestCode == SEND_NOTICE_CODE
                && resultCode == Activity.RESULT_OK) {
            loadData();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        nowIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private class StarAreaItemViewPagerAdapter extends FragmentPagerAdapter {

        private final List<StarAreaItemFragment> fragmentList;

        private FragmentManager fm;

        private StarAreaItemViewPagerAdapter(FragmentManager fm,
                                             List<StarAreaItemFragment> fragmentList) {
            super(fm);
            this.fm = fm;
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

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        private void setFragments(List<StarAreaItemFragment> fragments) {
            if (this.fragmentList != null) {
                FragmentTransaction ft = fm.beginTransaction();
                for (Fragment f : this.fragmentList) {
                    ft.remove(f);
                }
                ft.commit();
                ft = null;
                fm.executePendingTransactions();

                this.fragmentList.clear();
                this.fragmentList.addAll(fragments);
            }
            notifyDataSetChanged();
        }
    }

}
