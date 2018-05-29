package com.donut.app.mvp.home;

import android.content.Intent;
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
import com.donut.app.databinding.FragmentIdolBinding;
import com.donut.app.http.message.StarListDetail;
import com.donut.app.http.message.StarListResponse;
import com.donut.app.mvp.MVPBaseFragment;
import com.donut.app.mvp.blooper.BlooperAdapter;
import com.donut.app.mvp.blooper.BlooperContract;
import com.donut.app.mvp.blooper.IdolBlooperPresenter;
import com.donut.app.mvp.blooper.detail.BlooperDetailActivity;
import com.donut.app.mvp.home.search.SubjectSearchActivity;
import com.donut.app.utils.L;
import com.donut.app.utils.status_bar.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

public class IdolFragment extends MVPBaseFragment<FragmentIdolBinding, IdolBlooperPresenter> implements
        BlooperContract.View,
        BlooperAdapter.OnItemClickListener, 
        SwipeRefreshLayout.OnRefreshListener, 
        TextView.OnEditorActionListener{
    private List<StarListDetail> starList = new ArrayList<>();

    private BlooperAdapter mAdapter;

    private TextWatcher textWatcher;

    View footerView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_idol;
    }

    @Override
    protected void initView() {
        StatusBarCompat.setStatusBarColor(getActivity(), Constant.default_bar_color);
        mViewBinding.blooperSrl.setColorSchemeResources(R.color.refresh_tiffany);
        mViewBinding.blooperSrl.setOnRefreshListener(this);

        footerView = LayoutInflater.from(getContext())
                .inflate(R.layout.recycleview_footer, null, false);
        mAdapter = new BlooperAdapter(starList, this, footerView);
        mViewBinding.blooperStar.setAdapter(mAdapter);
        mViewBinding.blooperStar.setHasFixedSize(true);
        mViewBinding.blooperStar.setLayoutManager(getLayoutManager());
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        ABaseGridLayoutManager layoutManager = new ABaseGridLayoutManager(getActivity(), 3);
        layoutManager.setOnRecyclerViewScrollListener(mViewBinding.blooperStar,
                new OnRecyclerViewScrollLocationListener() {
                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView) {
                    }

                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                        if (starList.size() >= mPresenter.rows
                                && starList.size() % mPresenter.rows == 0) {
                            mViewBinding.getRoot().setVisibility(View.VISIBLE);
                            mPresenter.page++;
                            L.e("==================onBottomWhenScrollIdle");
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
//        mViewBinding.blooperEtSearch.setOnEditorActionListener(this);
//        mViewBinding.blooperSearchIvEtClean.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.blooper_search_iv_etClean:
//                        mViewBinding.blooperEtSearch.setText("");
//                        mPresenter.searchName = "";
//                        break;
//                }
//            }
//        });

        mViewBinding.blooperEtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(SubjectSearchActivity.class);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        textWatcher=new TextWatcher() {
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
                    L.e("=======================afterTextChanged()");
                    onRefresh();
                } else {
                    mViewBinding.blooperSearchIvEtClean.setVisibility(View.VISIBLE);
                }
            }
        };
        mViewBinding.blooperEtSearch.addTextChangedListener(textWatcher);
    }

    @Override
    public void onPause() {
        mViewBinding.blooperEtSearch.removeTextChangedListener(textWatcher);
        super.onPause();
    }

    @Override
    public void loadData() {
        //mViewBinding.getRoot().setVisibility(View.VISIBLE);
        L.e("===============loadData()----");
        mPresenter.loadData(true);
    }

    @Override
    public void showView(final StarListResponse detail) {
        mViewBinding.blooperSrl.setRefreshing(false);
        mViewBinding.blooperEtSearch.setEnabled(true);
//        mViewBinding.getRoot().setVisibility(View.GONE);
        if (mPresenter.page == 0) {
            starList.clear();
        }
        if (detail.getStarList() != null) {
            starList.addAll(detail.getStarList());
        }
        L.e("===================mAdapter");
        mAdapter.notifyDataSetChanged();
        if (starList.size() <= 0) {
            mViewBinding.blooperTvMsg.setVisibility(View.VISIBLE);
        } else {
            mViewBinding.blooperTvMsg.setVisibility(View.GONE);
        }
        footerView.setVisibility(View.GONE);
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
        L.e("================OnRefresh()---");
        mPresenter.loadData(false);
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
            L.e("=======================onEditorAction()");
            onRefresh();
            return true;
        }
        return false;
    }

}
