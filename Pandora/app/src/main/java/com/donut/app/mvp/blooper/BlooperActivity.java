package com.donut.app.mvp.blooper;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseGridLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.donut.app.R;
import com.donut.app.config.Constant;
import com.donut.app.databinding.ActivityBlooperBinding;
import com.donut.app.http.message.StarListDetail;
import com.donut.app.http.message.StarListResponse;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.blooper.detail.BlooperDetailActivity;
import com.donut.app.utils.status_bar.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qi on 2017/3/16.
 * Description : 明星花絮<br>
 */
public class BlooperActivity extends MVPBaseActivity<ActivityBlooperBinding, BlooperPresenter>
        implements BlooperContract.View, BlooperAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, TextView.OnEditorActionListener {

    private List<StarListDetail> starList = new ArrayList<>();

    private BlooperAdapter mAdapter;

    private View footerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_blooper;
    }

    @Override
    protected void initView() {
        updateHeadTitle("明星花絮", true);
        StatusBarCompat.setStatusBarColor(this, Constant.default_bar_color);
        mViewBinding.blooperSrl.setColorSchemeResources(R.color.refresh_tiffany);
        mViewBinding.blooperSrl.setOnRefreshListener(this);
        footerView = LayoutInflater.from(this)
                .inflate(R.layout.recycleview_footer, null, false);
        mAdapter = new BlooperAdapter(starList, this, footerView);
        mViewBinding.blooperRv.setAdapter(mAdapter);
        mViewBinding.blooperRv.setHasFixedSize(true);
        mViewBinding.blooperRv.setLayoutManager(getLayoutManager());
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        ABaseGridLayoutManager layoutManager = new ABaseGridLayoutManager(this, 3);
        layoutManager.setOnRecyclerViewScrollListener(mViewBinding.blooperRv,
                new OnRecyclerViewScrollLocationListener() {
                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView) {
                    }

                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                        if (starList.size() >= mPresenter.rows
                                && starList.size() % mPresenter.rows == 0) {
                            footerView.setVisibility(View.VISIBLE);
                            mPresenter.page++;
                            mPresenter.loadData(false);
                        }
                    }
                });
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                if (position == starList.size()) {
                    return 3;
                }
                return 1;
            }
        });

        return layoutManager;
    }

    @Override
    protected void initEvent() {
        mViewBinding.blooperEtSearch.setOnEditorActionListener(this);
        mViewBinding.blooperSearchIvEtClean.setOnClickListener(this);
        mViewBinding.blooperEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mViewBinding.blooperSearchIvEtClean.setVisibility(View.GONE);
                    mPresenter.searchName = "";
                    mViewBinding.blooperEtSearch.setEnabled(false);

                    mViewBinding.blooperSrl.setRefreshing(true);
                    onRefresh();
                } else {
                    mViewBinding.blooperSearchIvEtClean.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void loadData() {
        footerView.setVisibility(View.VISIBLE);
        mPresenter.loadData(true);
    }

    @Override
    public void showView(final StarListResponse detail) {
        mViewBinding.blooperSrl.setRefreshing(false);
        mViewBinding.blooperEtSearch.setEnabled(true);
        footerView.setVisibility(View.GONE);
        if (mPresenter.page == 0) {
            starList.clear();
        }
        if (detail.getStarList() != null) {
            starList.addAll(detail.getStarList());
        }
        mAdapter.notifyDataSetChanged();
        if (starList.size() <= 0) {
            mViewBinding.blooperTvMsg.setVisibility(View.VISIBLE);
        } else {
            mViewBinding.blooperTvMsg.setVisibility(View.GONE);
        }
    }

    @Override
    public void OnClick(StarListDetail detail) {
        Bundle bundle = new Bundle();
        bundle.putString(BlooperDetailActivity.STAR_ID, detail.getStarId());
        launchActivity(BlooperDetailActivity.class, bundle);
    }

    @Override
    public void onRefresh() {
        mPresenter.page = 0;
        mPresenter.loadData(false);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.blooper_search_iv_etClean:
                mViewBinding.blooperEtSearch.setText("");
                mPresenter.searchName = "";
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            if (im.isActive() && null != getCurrentFocus())
//            {
//                im.hideSoftInputFromWindow(getCurrentFocus()
//                                .getApplicationWindowToken(),
//                        InputMethodManager.HIDE_NOT_ALWAYS);
//            }
            mViewBinding.blooperEtSearch.setEnabled(false);

            mViewBinding.blooperSrl.setRefreshing(true);
            mPresenter.searchName = v.getText().toString();
            onRefresh();
            return true;
        }
        return false;
    }
}
