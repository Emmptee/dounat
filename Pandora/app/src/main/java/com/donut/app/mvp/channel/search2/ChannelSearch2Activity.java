package com.donut.app.mvp.channel.search2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.donut.app.R;
import com.donut.app.activity.LoginActivity;
import com.donut.app.adapter.ChannelSearchAdapter;
import com.donut.app.config.Constant;
import com.donut.app.config.GotoChannelUtils;
import com.donut.app.databinding.ActivityChannelSearch2LayoutBinding;
import com.donut.app.http.message.SubjectListDetail;
import com.donut.app.http.message.SubjectListResponse;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.channel.list2.ChannelList2Adapter;
import com.donut.app.utils.status_bar.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qi on 2017/3/21.
 * Description : <br>
 */
public class ChannelSearch2Activity extends MVPBaseActivity<ActivityChannelSearch2LayoutBinding, ChannelSearch2Presenter>
        implements ChannelSearch2Contract.View,
        SwipeRefreshLayout.OnRefreshListener,
        TextView.OnEditorActionListener, ChannelList2Adapter.OnItemClickListener {

    public static final String CHANNEL_ID = "CHANNEL_ID",
            CHANNEL_TYPE = "CHANNEL_TYPE", CHANNEL_NAME = "CHANNEL_NAME";

    private final static int REQUEST_CODE_LOGIN = 1, GOTO_DETAIL = 2;

    private List<SubjectListDetail> subjectList = new ArrayList<>();

    private SubjectListDetail subjectDetail;

    private ChannelList2Adapter mAdapter;

    private View footerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_channel_search2_layout;
    }

    @Override
    protected void initView() {
        StatusBarCompat.setStatusBarColor(this, Constant.default_bar_color);
        mViewBinding.setHandler(this);

        footerView = LayoutInflater.from(this)
                .inflate(R.layout.recycleview_footer, null, false);
        mViewBinding.channelSearchSr.setColorSchemeResources(R.color.refresh_tiffany);
        mViewBinding.channelSearchSr.setOnRefreshListener(this);
        mAdapter = new ChannelList2Adapter(subjectList, this, footerView);
        mViewBinding.channelSearchList.setLayoutManager(getLayoutManager());
        mViewBinding.channelSearchList.setAdapter(mAdapter);

    }

    private RecyclerView.LayoutManager getLayoutManager() {
        final ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(
                getContext());
        layoutManager.setOnRecyclerViewScrollLocationListener(mViewBinding.channelSearchList,
                new OnRecyclerViewScrollLocationListener() {
                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView) {
                    }

                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                        if (subjectList.size() >= ChannelSearch2Presenter.rows) {
                            footerView.setVisibility(View.VISIBLE);
                            mPresenter.page++;
                            mViewBinding.channelSearchSr.setRefreshing(true);
                            mPresenter.loadData(false, getIntent().getStringExtra(CHANNEL_ID));
                        }
                    }
                });
        return layoutManager;
    }

    @Override
    protected void initEvent() {
        mViewBinding.channelSearchEtSearch.setOnEditorActionListener(this);
        mViewBinding.channelSearchEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mViewBinding.channelSearchIvEtClean.setVisibility(View.GONE);
                    mPresenter.searchName = "";
                    onRefresh();
                } else {
                    mViewBinding.channelSearchIvEtClean.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void loadData() {
//        mPresenter.loadData(true, getIntent().getStringExtra(CHANNEL_ID));
//        int channelType = getIntent().getIntExtra(CHANNEL_TYPE, 0);
        footerView.setVisibility(View.GONE);
        mViewBinding.channelSearchTvInitMsg.setVisibility(View.VISIBLE);
        String title = getIntent().getStringExtra(CHANNEL_NAME);
        updateHeadTitle(title, true);
    }

    @Override
    public void showView(final SubjectListResponse detail) {
        mViewBinding.channelSearchSr.setRefreshing(false);
        mViewBinding.channelSearchEtSearch.setEnabled(true);
        footerView.setVisibility(View.GONE);
        mViewBinding.channelSearchTvInitMsg.setVisibility(View.GONE);
        if (mPresenter.page == 0) {
            subjectList.clear();
        }
        if (detail.getSubjectList() != null
                && detail.getSubjectList().size() > 0) {
            subjectList.addAll(detail.getSubjectList());
        }
        mAdapter.notifyDataSetChanged();
        if (subjectList.size() <= 0) {
            mViewBinding.channelSearchTvMsg.setVisibility(View.VISIBLE);
        } else {
            mViewBinding.channelSearchTvMsg.setVisibility(View.GONE);
        }
    }

    @Override
    public void OnItemClick(SubjectListDetail detail) {
        detail.setBrowseTimes(detail.getBrowseTimes() + 1);
        subjectDetail = detail;
        //跳转详情
        GotoChannelUtils.GotoSubjectDetail(this,
                getIntent().getIntExtra(CHANNEL_TYPE, 0),
                detail.getSubjectId(), GOTO_DETAIL);
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

        mPresenter.onToShare(mShareDetail);
    }

    @Override
    public void onRefresh() {
        mPresenter.page = 0;
        mPresenter.loadData(false, getIntent().getStringExtra(CHANNEL_ID));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            mViewBinding.channelSearchEtSearch.setEnabled(false);
            mViewBinding.channelSearchSr.setRefreshing(true);
            mPresenter.searchName = v.getText().toString();
            mPresenter.page = 0;
            mPresenter.loadData(false, getIntent().getStringExtra(CHANNEL_ID));
            return true;
        }
        return false;
    }

    public void onClearEdit() {
        mViewBinding.channelSearchEtSearch.setText("");
        mPresenter.searchName = "";
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!im.isActive()) {
            mViewBinding.channelSearchSr.setRefreshing(true);
            onRefresh();
        }
    }

}