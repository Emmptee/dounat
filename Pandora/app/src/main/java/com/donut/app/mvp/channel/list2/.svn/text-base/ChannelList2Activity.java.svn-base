package com.donut.app.mvp.channel.list2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.donut.app.R;
import com.donut.app.activity.LoginActivity;
import com.donut.app.config.Constant;
import com.donut.app.config.GotoChannelUtils;
import com.donut.app.databinding.ActivityChannelList2LayoutBinding;
import com.donut.app.http.message.SubjectListDetail;
import com.donut.app.http.message.SubjectListResponse;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.channel.search.ChannelSearchActivity;
import com.donut.app.mvp.channel.search.ChannelSearchPresenter;
import com.donut.app.mvp.channel.search2.ChannelSearch2Activity;
import com.donut.app.mvp.subject.notice.SubjectNoticeActivity;
import com.donut.app.utils.status_bar.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qi on 2017/3/21.
 * Description : <br>
 */
public class ChannelList2Activity extends MVPBaseActivity<ActivityChannelList2LayoutBinding, ChannelList2Presenter>
        implements ChannelList2Contract.View, ChannelList2Adapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private final static int REQUEST_CODE_LOGIN = 1, GOTO_DETAIL = 2;

    public static final String CHANNEL_ID = "CHANNEL_ID",
            CHANNEL_TYPE = "CHANNEL_TYPE", CHANNEL_NAME = "CHANNEL_NAME";

    private List<SubjectListDetail> subjectList = new ArrayList<>();

    private SubjectListDetail subjectDetail;

    private ChannelList2Adapter mAdapter;

    private View footerView;

    private int channelType;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_channel_list2_layout;
    }

    @Override
    protected void initView() {
        StatusBarCompat.setStatusBarColor(this, Constant.default_bar_color);
        footerView = LayoutInflater.from(this)
                .inflate(R.layout.recycleview_footer, null, false);
        mViewBinding.channelList2Sr.setColorSchemeResources(R.color.refresh_tiffany);
        mViewBinding.channelList2Sr.setOnRefreshListener(this);
        mAdapter = new ChannelList2Adapter(subjectList, this, footerView);
        mViewBinding.channelList2List.setAdapter(mAdapter);
        mViewBinding.channelList2List.setLayoutManager(getLayoutManager());
        mViewBinding.setHandler(this);

        mViewBinding.getRoot().findViewById(R.id.head_right_iv).setVisibility(View.VISIBLE);
        mViewBinding.getRoot().findViewById(R.id.menu).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.menu:
                gotoSearchAll();
                break;
        }
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        final ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(
                getContext());
        layoutManager.setOnRecyclerViewScrollLocationListener(mViewBinding.channelList2List,
                new OnRecyclerViewScrollLocationListener() {
                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView) {
                    }

                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                        if (subjectList.size() >= ChannelSearchPresenter.rows) {
                            footerView.setVisibility(View.VISIBLE);
                            mPresenter.page++;
                            mViewBinding.channelList2Sr.setRefreshing(true);
                            mPresenter.loadData(false, getIntent().getStringExtra(CHANNEL_ID));
                        }
                    }
                });
        return layoutManager;
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
        mViewBinding.channelList2Sr.setRefreshing(false);
        footerView.setVisibility(View.GONE);
        if (mPresenter.page == 0) {
            subjectList.clear();
        }
        if (detail.getSubjectList() != null
                && detail.getSubjectList().size() > 0) {
            subjectList.addAll(detail.getSubjectList());
            mAdapter.notifyDataSetChanged();
        }
    }

    public void gotoSearchAll() {
        Bundle bundle = new Bundle();
        bundle.putString(ChannelSearch2Activity.CHANNEL_ID,
                getIntent().getStringExtra(CHANNEL_ID));
        bundle.putString(ChannelSearch2Activity.CHANNEL_NAME,
                getIntent().getStringExtra(CHANNEL_NAME));
        bundle.putInt(ChannelSearch2Activity.CHANNEL_TYPE, channelType);
        launchActivity(ChannelSearch2Activity.class, bundle);
        mPresenter.saveBehaviour("02", channelType);
    }

    @Override
    public void OnItemClick(SubjectListDetail detail) {
        detail.setBrowseTimes(detail.getBrowseTimes() + 1);
        subjectDetail = detail;
        //跳转详情
        GotoChannelUtils.GotoSubjectDetail(this, channelType,
                detail.getSubjectId(), GOTO_DETAIL);
        mPresenter.saveBehaviour("01", channelType);
    }

    @Override
    public void OnItemCollectClick(SubjectListDetail detail) {

        if (!sp_Info.getBoolean(Constant.IS_LOGIN, false)) {
            showToast("请先登录");
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
            return;
        }
        mPresenter.onToCollect(detail);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_LOGIN:
                if (resultCode == RESULT_OK) {
                    onRefresh();
                }
                break;
            case GOTO_DETAIL:
                if (resultCode == Activity.RESULT_OK) {
                    int state = data.getIntExtra(Constant.COLLECT_STATUS, 0);
                    subjectDetail.setCollectionStatus(state);
                }
                break;
        }
    }

    private SubjectListDetail mShareDetail;

    @Override
    public void OnItemShareClick(SubjectListDetail detail) {
        mShareDetail = detail;
        requestRuntimePermission("为了给您提供更好的服务,甜麦圈需要获取存储器读写权限",
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);

        mPresenter.onToShare(mShareDetail, channelType);
    }

    @Override
    public void onRefresh() {
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
}
