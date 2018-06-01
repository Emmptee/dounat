package com.donut.app.mvp.auction;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.donut.app.R;
import com.donut.app.activity.H5WebActivity;
import com.donut.app.config.Constant;
import com.donut.app.customview.MyAuctionSelectTypePopupWindow;
import com.donut.app.databinding.ActivityAuctionListBinding;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.auction.MyAuctionDetail;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.utils.status_bar.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public class MyAuctionActivity extends MVPBaseActivity<ActivityAuctionListBinding, MyAuctionPresenter>
        implements MyAuctionContract.View, SwipeRefreshLayout.OnRefreshListener {

    private final List<MyAuctionDetail> mDetails = new ArrayList<>();

    private MyAuctionAdapter mAdapter;

    private View footerView;

    private int auctionType = 0;

    private MyAuctionSelectTypePopupWindow mPopupWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auction_list;
    }

    @Override
    protected void initView() {
        StatusBarCompat.setStatusBarColor(this, Constant.default_bar_color);
        updateHeadTitle(getString(R.string.my_auction), true);
        mViewBinding.auctionSrl.setColorSchemeResources(R.color.refresh_tiffany);
        mViewBinding.auctionSrl.setOnRefreshListener(this);
        footerView = LayoutInflater.from(this)
                .inflate(R.layout.recycleview_footer, null, false);
        mAdapter = new MyAuctionAdapter(this, mDetails, this, footerView);
        mViewBinding.auctionList.setAdapter(mAdapter);
        mViewBinding.auctionList.setHasFixedSize(true);
        mViewBinding.auctionList.setLayoutManager(getLayoutManager());
    }

    @Override
    protected void initEvent() {
        mViewBinding.auctionTopLayout.setOnClickListener(this);
    }

    @Override
    protected void loadData() {
        footerView.setVisibility(View.VISIBLE);
        mPresenter.loadData(true, auctionType);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.auction_top_layout:
                mPopupWindow = new MyAuctionSelectTypePopupWindow(this, auctionType,
                        new MyAuctionSelectTypePopupWindow.OnClickListenerWithPosition() {
                            @Override
                            public void onItemClick(View v, int selectType) {
                                mPopupWindow.dismiss();
                                MyAuctionActivity.this.auctionType = selectType;
                                switch (selectType) {
                                    case 1:
                                        mViewBinding.auctionTvType.setText(R.string.auction_type_1);
                                        break;
                                    case 2:
                                        mViewBinding.auctionTvType.setText(R.string.auction_type_2);
                                        break;
                                    default:
                                        mViewBinding.auctionTvType.setText(R.string.auction_type_0);
                                        break;
                                }
                                mViewBinding.auctionSrl.setRefreshing(true);
                                onRefresh();
                            }

                            private void e() {
                            }
                        });
                mPopupWindow.showAsDropDown(mViewBinding.auctionTopLine);
                break;
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.page = 0;
        mPresenter.loadData(false, auctionType);
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        final ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(
                getContext());
        layoutManager.setOnRecyclerViewScrollLocationListener(mViewBinding.auctionList,
                new OnRecyclerViewScrollLocationListener() {
                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView) {
                    }

                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                        if (mDetails.size() >= mPresenter.rows) {
                            mPresenter.page++;
                            footerView.setVisibility(View.VISIBLE);
                            mPresenter.loadData(false, auctionType);
                        }
                    }
                });
        return layoutManager;
    }

    @Override
    public void showView(List<MyAuctionDetail> details) {
        mViewBinding.auctionSrl.setRefreshing(false);
        footerView.setVisibility(View.GONE);
        if (mPresenter.page == 0) {
            mDetails.clear();
        }
        if (details != null) {
            mDetails.addAll(details);
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, 1000);
        }
        mAdapter.notifyDataSetChanged();
        if (mDetails.size() <= 0) {
            mViewBinding.auctionTvMsg.setVisibility(View.VISIBLE);
            String strMsg = "您还未参与任何竞拍";
            switch (auctionType) {
                case 1:
                case 2:
                    strMsg = "该状态下您没有任何竞拍记录";
                    break;
            }
            mViewBinding.auctionTvMsg.setText(strMsg);
        } else {
            mViewBinding.auctionTvMsg.setVisibility(View.GONE);
        }
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            for (MyAuctionDetail detail : mDetails) {
                if (detail.getAuctionLogsStatus() != 1) {
                    continue;
                }
                if (detail.getPayCountdown() > 0) {
                    detail.setPayCountdown(detail.getPayCountdown() - 1);
                } else {
                    onRefresh();
                    return;
                }
            }
//            mAdapter.notifyDataSetChanged();
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void OnItemClick(MyAuctionDetail detail) {
        sp_Info.edit().putString("goodsId", detail.getD02Id()).apply();

        Bundle bundle = new Bundle();
        bundle.putString(H5WebActivity.URL, "file:///android_asset/www/goods-details_auction.html");
        launchActivity(H5WebActivity.class, bundle);
        mPresenter.saveBehaviour("01");
    }

    @Override
    public void OnItemCancelClick(MyAuctionDetail detail) {
        mPresenter.cancelAuction(detail.getD10Id());
        mPresenter.saveBehaviour("03");
    }

    @Override
    public void OnItemPayClick(MyAuctionDetail detail) {

        if (detail.getD01ValidStatus() == 1) {
            sp_Info.edit().putString("auctionId", detail.getD10Id()).apply();
            Bundle bundle = new Bundle();
            bundle.putString(H5WebActivity.URL, "file:///android_asset/www/payAuctionGoods.html");
            launchActivity(H5WebActivity.class, bundle);
        } else if (detail.getD01ValidStatus() == 0) {
            mPresenter.payAuction(detail.getD01Id());
        }
        mPresenter.saveBehaviour("02");
    }

    @Override
    public void OnItemDeleteClick(MyAuctionDetail detail) {
        mPresenter.deleteAuction(detail.getD10Id());
        mPresenter.saveBehaviour("04");
    }

    @Override
    public void onRefreshView() {
        mViewBinding.auctionSrl.setRefreshing(true);
        onRefresh();
    }

    @Override
    protected void onStop() {
        mPresenter.saveBehaviour("xx");
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.saveBehaviour("00", mPresenter.requestObject, HeaderRequest.MY_AUCTION);
    }

    @Override
    public void onBackPressed() {
        mPresenter.saveBehaviour("05");
        super.onBackPressed();
    }
}