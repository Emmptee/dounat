package com.donut.app.mvp.spinOff.plans;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
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
import com.donut.app.activity.CommentActivity;
import com.donut.app.activity.LoginActivity;
import com.donut.app.activity.StarDetailActivity;
import com.donut.app.databinding.FragmentSpinOffPlansBinding;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.spinOff.ExclusivePlan;
import com.donut.app.http.message.spinOff.ExclusivePlanResponse;
import com.donut.app.model.video.VideoActivity;
import com.donut.app.mvp.MVPBaseFragment;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Qi on 2017/6/5.
 * Description : 衍生品-商品 <br>
 */
public class SpinOffPlansFragment extends MVPBaseFragment<FragmentSpinOffPlansBinding, SpinOffPlansPresenter>
        implements SpinOffPlansContract.View, SpinOffPlansListAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private SpinOffPlansListAdapter mAdapter;

    private List<ExclusivePlan> planList = new ArrayList<>();

    private View footerView;

    public static final int REQUEST_CODE_LOGIN = 1, COMMENT_REQUEST_CODE = 2;

    private String searchStarName;

    public static SpinOffPlansFragment newInstance() {
        return new SpinOffPlansFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_spin_off_plans;
    }

    @Override
    protected void initView() {

        mViewBinding.spinOffPlansSrl.setColorSchemeResources(R.color.refresh_tiffany);
        mViewBinding.spinOffPlansSrl.setOnRefreshListener(this);

        footerView = LayoutInflater.from(getContext())
                .inflate(R.layout.recycleview_footer, null, false);
        mAdapter = new SpinOffPlansListAdapter(planList, this, footerView);
        mViewBinding.spinOffPlansList.setAdapter(mAdapter);
        mViewBinding.spinOffPlansList.setLayoutManager(getLayoutManager());
    }

    @Override
    protected void initEvent() {
    }

    @Override
    public void loadData() {
        mPresenter.searchStarName = this.searchStarName;
        mPresenter.loadData(true);
    }

    public void setSearchStarName(String name) {
        this.searchStarName = name;
        if (isResumed()) {
            mPresenter.searchStarName = name;
            onRefresh();
        }
    }

    @Override
    public void showView(ExclusivePlanResponse detail) {

        mViewBinding.spinOffPlansSrl.setRefreshing(false);
        footerView.setVisibility(View.GONE);
        if (mPresenter.page == 0) {
            planList.clear();
        }

        if (detail.getExclusivePlan() != null && detail.getExclusivePlan().size() > 0) {
            planList.addAll(detail.getExclusivePlan());
        }
        mAdapter.notifyDataSetChanged();

        if (planList.size() <= 0) {
            mViewBinding.noData.setVisibility(View.VISIBLE);
        } else {
            mViewBinding.noData.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.page = 0;
        mPresenter.loadData(false);
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(getContext());
        layoutManager.setOnRecyclerViewScrollLocationListener(mViewBinding.spinOffPlansList,
                new OnRecyclerViewScrollLocationListener() {
                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView) {

                    }

                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                        if (planList.size() % mPresenter.rows == 0) {
                            footerView.setVisibility(View.VISIBLE);
                            mPresenter.page++;
                            mPresenter.loadData(false);
                        }
                    }
                });

        return layoutManager;
    }

    @Override
    public void onStarDetailClick(ExclusivePlan detail) {
        Bundle bundle = new Bundle();
        bundle.putString(StarDetailActivity.STAR_ID, detail.getActorId());
        launchActivity(StarDetailActivity.class, bundle);
    }

    @Override
    public void onPlayClick(ExclusivePlan detail) {
        mPresenter.addPlayNum(detail.getB02Id());
        detail.setBrowseTimes(detail.getBrowseTimes() + 1);

        Bundle bundle = new Bundle();
        bundle.putString(VideoActivity.VIDEOURL, detail.getPlayUrl());
//        bundle.putString(VideoActivity.VIDEONAME, detail.getTitle());
        launchActivity(VideoActivity.class, bundle);
    }

    @Override
    public void onLikeClick(ExclusivePlan model) {
        if (!getLoginStatus()) {
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
            return;
        }
        if (model.getPraiseStatus() == 0) {
            model.setPraiseStatus(1);
            model.setPraiseTimes(model.getPraiseTimes() + 1);
            mPresenter.onLike(model.getB02Id(), true);
        } else {
            model.setPraiseStatus(0);
            model.setPraiseTimes(model.getPraiseTimes() - 1);
            mPresenter.onLike(model.getB02Id(), false);
        }
    }

    @Override
    public void onCommentClick(ExclusivePlan detail) {
        if (!getLoginStatus()) {
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(CommentActivity.CONTENTID, detail.getB02Id());
        launchActivityForResult(CommentActivity.class, bundle, COMMENT_REQUEST_CODE);

    }

    private ExclusivePlan model;

    @Override
    public void onShareClick(ExclusivePlan detail) {
        this.model = detail;

        requestRuntimePermission("为了给您提供更好的服务,甜麦圈需要获取存储器读写权限",
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        String linkurl = RequestUrl.SPIN_OFF_PLANS_SHARE_URL
                + "header=00010324&b02Id="
                + model.getB02Id();
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo);
        ShareBuilderCommonUtil.Builder builder = new ShareBuilderCommonUtil.Builder(getContext());
        builder.setTitle(model.getStarName() + "|" + model.getName());
        builder.setContent("爱豆的独家精彩视频尽在甜麦圈APP，快去围观吧！");
        builder.setLinkUrl(linkurl);
        builder.setBitmap(bmp);
        builder.setShareMedia(new SHARE_MEDIA[]{SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA});

        builder.create();
        mPresenter.shareRequest(model.getB02Id());
        model.setShareTimes(model.getShareTimes() + 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_LOGIN:
            case COMMENT_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    mViewBinding.spinOffPlansSrl.setRefreshing(true);
                    onRefresh();
                }
                break;
        }
    }
}
