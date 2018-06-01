package com.donut.app.mvp.channel.list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.donut.app.R;
import com.donut.app.config.Constant;
import com.donut.app.config.GotoChannelUtils;
import com.donut.app.databinding.ActivityChannelListLayoutBinding;
import com.donut.app.http.message.SubjectListDetail;
import com.donut.app.http.message.SubjectListResponse;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.channel.search.ChannelSearchActivity;
import com.donut.app.utils.status_bar.StatusBarCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Qi on 2017/3/21.
 * Description : <br>
 */
public class ChannelListActivity extends MVPBaseActivity<ActivityChannelListLayoutBinding, ChannelListPresenter>
        implements ChannelListContract.View,
        ViewPager.OnPageChangeListener,
        ChannelListItemFragment.OnFragmentInteractionListener {

    public static final String CHANNEL_ID = "CHANNEL_ID",
            CHANNEL_TYPE = "CHANNEL_TYPE", CHANNEL_NAME = "CHANNEL_NAME";

    private ChannelItemViewPagerAdapter mAdapter;

    private List<ChannelListItemFragment> fragmentList = new ArrayList<>();

    private List<SubjectListDetail> subjectList = new ArrayList<>();

    private int nowIndex, channelType;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_channel_list_layout;
    }

    @Override
    protected void initView() {
        StatusBarCompat.setStatusBarColor(this, Constant.default_bar_color);
        mAdapter = new ChannelItemViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewBinding.channelListViewPager.setAdapter(mAdapter);
        mViewBinding.channelListViewPager.addOnPageChangeListener(this);
        mViewBinding.setHandler(this);
    }

    @Override
    protected void initEvent() {
    }

    @Override
    public void loadData() {
        channelType = getIntent().getIntExtra(CHANNEL_TYPE, 0);
        mPresenter.loadData(true, getIntent().getStringExtra(CHANNEL_ID));

        String title = getIntent().getStringExtra(CHANNEL_NAME);
        updateHeadTitle(title, true);
    }

    @Override
    public void showView(final SubjectListResponse detail) {
        if (mPresenter.page == 0) {
            fragmentList.clear();
            subjectList.clear();
        }
        List<SubjectListDetail> details = detail.getSubjectList();
        if (details != null && details.size() > 0) {
            for (int i = 0; i < details.size(); i++) {
                SubjectListDetail subjectDetail = details.get(i);
                ChannelListItemFragment fragment
                        = ChannelListItemFragment.newInstance(subjectDetail, channelType);
                fragmentList.add(fragment);
                subjectList.add(subjectDetail);
            }
            mViewBinding.setDetail(subjectList.get(nowIndex));
        }
        mAdapter.notifyDataSetChanged();
    }

    public void gotoSearchAll() {
        Bundle bundle = new Bundle();
        bundle.putString(ChannelSearchActivity.CHANNEL_ID,
                getIntent().getStringExtra(CHANNEL_ID));
        bundle.putString(ChannelSearchActivity.CHANNEL_NAME,
                getIntent().getStringExtra(CHANNEL_NAME));
        bundle.putInt(ChannelSearchActivity.CHANNEL_TYPE, channelType);
        launchActivity(ChannelSearchActivity.class, bundle);
        mPresenter.saveBehaviour("02", channelType);
    }

    public String createTimeFormat(String time) {
        if (time == null) {
            return "";
        }
        String formatTime = time;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(formatTime.substring(0, 10));
            formatTime = new SimpleDateFormat("yyyy MM dd", Locale.CHINA).format(date.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatTime;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        nowIndex = position;
        mViewBinding.setDetail(subjectList.get(nowIndex));

        if (nowIndex == fragmentList.size() - 1
                && (nowIndex + 1) % ChannelListPresenter.rows == 0) {
            mPresenter.page++;
            loadData();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onRefreshData() {
        mPresenter.page = 0;
        loadData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.saveBehaviour("00", channelType);
    }

    @Override
    protected void onStop() {
        mPresenter.saveBehaviour("xx", channelType);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        mPresenter.saveBehaviour("03", channelType);
        super.onBackPressed();
    }

    private class ChannelItemViewPagerAdapter extends FragmentPagerAdapter {

        private final List<ChannelListItemFragment> fragmentList;

        private ChannelItemViewPagerAdapter(FragmentManager fm,
                                            List<ChannelListItemFragment> fragmentList) {
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
