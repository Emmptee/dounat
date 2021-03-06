package com.donut.app.mvp.blooper.detail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.bis.android.sharelibrary.ShareBuilderCommonUtil;
import com.donut.app.R;
import com.donut.app.config.Constant;
import com.donut.app.databinding.ActivityBlooperDetailBinding;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.StarBlooperDetailResponse;
import com.donut.app.model.video.VideoActivity;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.utils.status_bar.StatusBarCompat;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qi on 2017/3/27.
 * Description : 明星个人花絮页面<br>
 */
public class BlooperDetailActivity extends MVPBaseActivity<ActivityBlooperDetailBinding, BlooperDetailPresenter>
        implements BlooperDetailContract.View, SwipeRefreshLayout.OnRefreshListener,
        BlooperDetailAdapter.OnItemPlayClickListener {

    public static final String STAR_ID = "STAR_ID";

    private List<StarBlooperDetailResponse.BlooperItem> starList = new ArrayList<>();

    private BlooperDetailAdapter mAdapter;

    private View footerView;

    private String starId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_blooper_detail;
    }

    @Override
    protected void initView() {
        StatusBarCompat.setStatusBarColorForCollapsingToolbar(this,
                mViewBinding.blooperDetailAppbarLayout,
                mViewBinding.blooperDetailToolbarLayout,
                mViewBinding.blooperDetailToolbar,
                Constant.default_bar_color);
        mViewBinding.setHandler(this);
        mViewBinding.starBlooperDetailSrl.setColorSchemeResources(R.color.refresh_tiffany);
        mViewBinding.starBlooperDetailSrl.setOnRefreshListener(this);
        footerView = LayoutInflater.from(this)
                .inflate(R.layout.recycleview_footer, null, false);
        mAdapter = new BlooperDetailAdapter(starList, this, footerView);
        mViewBinding.starBlooperDetailList.setAdapter(mAdapter);
        mViewBinding.starBlooperDetailList.setHasFixedSize(true);
        mViewBinding.starBlooperDetailList.setLayoutManager(getLayoutManager());
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        final ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(
                getContext());
        layoutManager.setOnRecyclerViewScrollLocationListener(mViewBinding.starBlooperDetailList,
                new OnRecyclerViewScrollLocationListener() {
                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView) {
                    }

                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                        if (starList.size() >= mPresenter.rows) {
                            footerView.setVisibility(View.VISIBLE);
                            mPresenter.page++;
                            mPresenter.loadData(false, starId);
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
        footerView.setVisibility(View.VISIBLE);
        starId = getIntent().getStringExtra(STAR_ID);
        mPresenter.loadData(true, starId);
    }

    @Override
    public void showView(final StarBlooperDetailResponse detail) {
        footerView.setVisibility(View.GONE);
        mViewBinding.starBlooperDetailSrl.setRefreshing(false);
        if (mPresenter.page == 0) {
            starList.clear();
            mViewBinding.setDetail(detail);
        }

        starList.addAll(detail.getStarTidbitVideos());
        mAdapter.notifyDataSetChanged();

        if (starList.size() > 0) {
            mViewBinding.noData.setVisibility(View.GONE);
        } else {
            mViewBinding.noData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.page = 0;
        mPresenter.loadData(false, starId);
    }

    public void viewBack() {
        super.onBackPressed();
    }

    @Override
    public void OnPlayClick(StarBlooperDetailResponse.BlooperItem detail) {
        Bundle bundle = new Bundle();
        bundle.putString(VideoActivity.VIDEOURL, detail.getPlayUrl());
        bundle.putString(VideoActivity.VIDEONAME, detail.getTitle());
        launchActivity(VideoActivity.class, bundle);

        mPresenter.addPlayNum(detail.getB02Id());
    }

    @Override
    public void OnShareClick(StarBlooperDetailResponse.BlooperItem detail) {
        String linkurl = RequestUrl.BLOOPER_SHARE_URL + "header="
                + HeaderRequest.BLOOPER_SHARE + "&starId=" + starId + "&b02Id=" + detail.getB02Id();

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo);
        ShareBuilderCommonUtil.Builder builder = new ShareBuilderCommonUtil.Builder(this);

        builder.setTitle(detail.getTitle()
                + ",【小伙伴】已围观了" + detail.getBrowseTimes() + "次");
        builder.setContent(detail.getTitle());
        builder.setLinkUrl(linkurl);
        builder.setBitmap(bmp);
        builder.setShareMedia(new SHARE_MEDIA[]{SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA});
        builder.create();
    }
}