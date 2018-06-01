package com.donut.app.mvp.spinOff.goods;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseGridLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.donut.app.R;
import com.donut.app.activity.H5WebActivity;
import com.donut.app.config.Constant;
import com.donut.app.databinding.FragmentSpinOffGoodsBinding;
import com.donut.app.http.message.spinOff.GoodsListDetail;
import com.donut.app.http.message.spinOff.GoodsListResponse;
import com.donut.app.mvp.MVPBaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qi on 2017/6/5.
 * Description : 衍生品-商品 <br>
 */
public class SpinOffGoodsFragment extends MVPBaseFragment<FragmentSpinOffGoodsBinding, SpinOffGoodsPresenter>
        implements SpinOffGoodsContract.View, SpinOffGoodsListAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private SpinOffGoodsListAdapter mAdapter;

    private List<GoodsListDetail> goodsList = new ArrayList<>();

    private View footerView;

    private String searchStarName;

    public static SpinOffGoodsFragment newInstance() {
        return new SpinOffGoodsFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_spin_off_goods;
    }

    @Override
    protected void initView() {

        mViewBinding.spinOffGoodsSrl.setColorSchemeResources(R.color.refresh_tiffany);
        mViewBinding.spinOffGoodsSrl.setOnRefreshListener(this);

        footerView = LayoutInflater.from(getContext())
                .inflate(R.layout.recycleview_footer, null, false);
        mAdapter = new SpinOffGoodsListAdapter(goodsList, this, footerView);
        mViewBinding.spinOffGoodsList.setAdapter(mAdapter);
        mViewBinding.spinOffGoodsList.setLayoutManager(getLayoutManager());
    }

    @Override
    protected void initEvent() {
    }

    @Override
    public void loadData() {
        mPresenter.searchStarName = this.searchStarName;
        mPresenter.loadData(true);
    }

    @Override
    public void showView(GoodsListResponse detail) {

        mViewBinding.spinOffGoodsSrl.setRefreshing(false);
        footerView.setVisibility(View.GONE);
        if (mPresenter.page == 0) {
            goodsList.clear();
        }

        if (detail.getGoodsList() != null && detail.getGoodsList().size() > 0) {
            goodsList.addAll(detail.getGoodsList());
        }
        mAdapter.notifyDataSetChanged();

        if (goodsList.size() <= 0) {
            mViewBinding.noData.setVisibility(View.VISIBLE);
        } else {
            mViewBinding.noData.setVisibility(View.GONE);
        }
    }

    @Override
    public void OnItemClick(GoodsListDetail detail) {

        SharedPreferences sp_Info = getContext().getSharedPreferences(Constant.SP_INFO,
                Context.MODE_PRIVATE);
        sp_Info.edit().putString("goodsId", detail.getGoodsId()).apply();

        Bundle bundle = new Bundle();
        String url;
        if (detail.getGoodsKind() == 1) {
            url = "file:///android_asset/www/goods-details_auction.html";
        } else {
            url = "file:///android_asset/www/goods-details.html";
        }
        bundle.putString(H5WebActivity.URL, url);
        launchActivity(H5WebActivity.class, bundle);

    }

    @Override
    public void onRefresh() {
        mPresenter.page = 0;
        mPresenter.loadData(false);
    }

    public void setSearchStarName(String name) {
        this.searchStarName = name;
        if (isResumed()) {
            mPresenter.searchStarName = name;
            onRefresh();
        }
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        ABaseGridLayoutManager layoutManager = new ABaseGridLayoutManager(getContext(), 2);
        layoutManager.setOnRecyclerViewScrollListener(mViewBinding.spinOffGoodsList,
                new OnRecyclerViewScrollLocationListener() {
                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView) {
                    }

                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                        if (goodsList.size() % mPresenter.rows == 0) {
                            footerView.setVisibility(View.VISIBLE);
                            mPresenter.page++;
                            mPresenter.loadData(false);
                        }
                    }
                });
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                if (position == goodsList.size()) {
                    return 2;
                }
                return 1;
            }
        });

        return layoutManager;
    }
}
