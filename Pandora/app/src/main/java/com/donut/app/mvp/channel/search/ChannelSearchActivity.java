package com.donut.app.mvp.channel.search;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.donut.app.R;
import com.donut.app.adapter.ChannelSearchAdapter;
import com.donut.app.config.Constant;
import com.donut.app.config.GotoChannelUtils;
import com.donut.app.databinding.ActivityChannelSearchLayoutBinding;
import com.donut.app.http.message.SubjectListDetail;
import com.donut.app.http.message.SubjectListResponse;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.utils.status_bar.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qi on 2017/3/21.
 * Description : <br>
 */
public class ChannelSearchActivity extends MVPBaseActivity<ActivityChannelSearchLayoutBinding, ChannelSearchPresenter>
        implements ChannelSearchContract.View,
        SwipeRefreshLayout.OnRefreshListener,
        TextView.OnEditorActionListener {

    public static final String CHANNEL_ID = "CHANNEL_ID",
            CHANNEL_TYPE = "CHANNEL_TYPE", CHANNEL_NAME = "CHANNEL_NAME";

    private List<SubjectListDetail> subjectList = new ArrayList<>();

    private ChannelSearchAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_channel_search_layout;
    }

    @Override
    protected void initView() {
        StatusBarCompat.setStatusBarColor(this, Constant.default_bar_color);
        mViewBinding.setHandler(this);

        mViewBinding.channelSearchSr.setColorSchemeResources(R.color.refresh_tiffany);
        mViewBinding.channelSearchSr.setOnRefreshListener(this);
        mAdapter = new ChannelSearchAdapter(subjectList, this);
        mViewBinding.channelSearchList.setLayoutManager(getLayoutManager());
        mViewBinding.channelSearchList.setAdapter(mAdapter);

        mPresenter.loadData(false, getIntent().getStringExtra(CHANNEL_ID));
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
                        if (subjectList.size() >= ChannelSearchPresenter.rows) {
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
//        mViewBinding.channelSearchTvInitMsg.setVisibility(View.VISIBLE);

        String title = getIntent().getStringExtra(CHANNEL_NAME);
        updateHeadTitle(title, true);
    }

    @Override
    public void showView(final SubjectListResponse detail) {
        mViewBinding.channelSearchSr.setRefreshing(false);
        mViewBinding.channelSearchEtSearch.setEnabled(true);
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
    public void onItemClick(SubjectListDetail detail) {
        //跳转详情
        GotoChannelUtils.GotoSubjectDetail(this,
                getIntent().getIntExtra(CHANNEL_TYPE, 0), detail.getSubjectId(), 0);
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
