package com.donut.app.mvp.wish.user;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bis.android.sharelibrary.ShareBuilderCommonUtil;
import com.donut.app.R;
import com.donut.app.activity.CommentActivity;
import com.donut.app.activity.FullImageActivity;
import com.donut.app.activity.LoginActivity;
import com.donut.app.adapter.DetailCommentRecyclerViewAdapter;
import com.donut.app.config.Constant;
import com.donut.app.databinding.ActivityWishUserBinding;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.wish.WishDetailsResponse;
import com.donut.app.model.video.VideoActivity;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.utils.status_bar.StatusBarCompat;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public class WishUserActivity extends MVPBaseActivity<ActivityWishUserBinding, WishUserPresenter>
        implements WishUserContract.View, DetailCommentRecyclerViewAdapter.OnItemClickListener {

    public static final String CONTENT_ID = "CONTENT_ID", FULFILL = "FULFILL";

    public static final int COMMENT_REQUEST_CODE = 1;

    private CharSequence temp;

    private int editStart, editEnd;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wish_user;
    }

    @Override
    protected void initView() {
        StatusBarCompat.setStatusBarColor(this, Constant.default_bar_color);
        updateHeadTitle("查看心愿", true);
        mViewBinding.setHandler(this);
        mViewBinding.setUserInfo(getUserInfo());
        mViewBinding.setFulfill(getIntent().getBooleanExtra(FULFILL, false));
    }

    @Override
    protected void initEvent() {
        final TextView mCommentBtnSend = mViewBinding.commentBottomLayout.includeCommentBtnSend;
        mCommentBtnSend.setOnClickListener(this);
        final EditText mCommentEt = mViewBinding.commentBottomLayout.includeCommentEt;
        mCommentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                editStart = mCommentEt.getSelectionStart();
                editEnd = mCommentEt.getSelectionEnd();
                if (temp.length() > 512) {
                    showToast(getString(R.string.comment_length_tips));
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    mCommentEt.setText(s);
                    mCommentEt.setSelection(tempSelection);
                }

                if (temp.length() > 0) {
                    mCommentBtnSend.setText(getString(R.string.send_msg));
                } else {
                    mCommentBtnSend.setText(
                            String.format(getString(R.string.comment_times),
                                    mViewBinding.getWish().getCommentTimes()));
                }
            }
        });
    }

    @Override
    protected void loadData() {
        mPresenter.loadData(true, getIntent().getStringExtra(CONTENT_ID));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.include_comment_btn_send:
                String msg = mViewBinding.commentBottomLayout
                        .includeCommentEt.getText().toString().trim();
                if (TextUtils.isEmpty(msg)) {
                    onToComment();
                    break;
                }
                mPresenter.sendComment(mViewBinding.getWish(), msg);
                break;
        }
    }

    @Override
    public void onLike(WishDetailsResponse model) {
        if (!getLoginStatus()) {
            showToast(getString(R.string.no_login_msg));
            launchActivity(LoginActivity.class);
            return;
        }
        if (model.getPraiseStatus() == 0) {
            model.setPraiseStatus(1);
            model.setPraiseTimes(model.getPraiseTimes() + 1);
            mPresenter.onLike(model, true);
        } else {
            model.setPraiseStatus(0);
            model.setPraiseTimes(model.getPraiseTimes() - 1);
            mPresenter.onLike(model, false);
        }
    }

    @Override
    public void onToComment() {
        Bundle bundle = new Bundle();
        bundle.putString(CommentActivity.CONTENTID, mViewBinding.getWish().getB02Id());
        launchActivityForResult(CommentActivity.class, bundle, COMMENT_REQUEST_CODE);
    }

    @Override
    public void onToShare(WishDetailsResponse model) {
        requestRuntimePermission("为了给您提供更好的服务,甜麦圈需要获取存储器读写权限", Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
        final WishDetailsResponse model = mViewBinding.getWish();
        String url = RequestUrl.WISH_DETAIL_SHARE_URL + "header="
                + HeaderRequest.WISH_DETAIL + "&b02Id=" + model.getB02Id();

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo);
        ShareBuilderCommonUtil.Builder builder = new ShareBuilderCommonUtil.Builder(this);

        String strStarName = model.getStarName();
        if (model.getWishType() != 0) {
            strStarName = getString(R.string.app_name);
        }
        String strPlayTimes = "";
        if (model.getVideoPlayTimes() > 0) {
            strPlayTimes = "|小伙伴已围观了" + model.getVideoPlayTimes() + "次";
        }
        builder.setTitle(strStarName + strPlayTimes);
        builder.setContent("我在甜麦圈发起的心愿已被" + strStarName + "达成，快来围观吧！");
        builder.setLinkUrl(url);
//        builder.setImgUrl(model.get);
        builder.setBitmap(bmp);
        builder.setShareMedia(new SHARE_MEDIA[]{SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA});
//        builder.setListener(new ShareBuilderCommonUtil.OnResponseListener() {
//            @Override
//            public void onSuccessRes() {
//
//            }
//        });
        builder.create();
        mPresenter.onToShare(model);
        model.setShareTimes(model.getShareTimes() + 1);
    }

    @Override
    public void onShowImg(WishDetailsResponse model) {
        Bundle bundle = new Bundle();
        bundle.putString(FullImageActivity.IMAGE_URL, model.getImgUrl());
        launchActivity(FullImageActivity.class, bundle);
    }

    @Override
    public void onPlayVideo(WishDetailsResponse model) {
        Bundle bundle = new Bundle();
        bundle.putString(VideoActivity.VIDEOURL, model.getAchieveVideoUrl());
        launchActivity(VideoActivity.class, bundle);

        mPresenter.addPlayNum(model);
    }

    @Override
    public void showView(WishDetailsResponse response) {
        mViewBinding.setWish(response);

        DetailCommentRecyclerViewAdapter adapter
                = new DetailCommentRecyclerViewAdapter(this, response.getCurrentComments(), this);
        RecyclerView view = mViewBinding.wishUserCommentList;
        view.setAdapter(adapter);
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setNestedScrollingEnabled(false);
        view.setHasFixedSize(false);
    }

    @Override
    public void clearCommentEdit() {
        mViewBinding.commentBottomLayout
                .includeCommentEt.setText("");
        mViewBinding.commentBottomLayout
                .includeCommentBtnSend.setText(
                String.format(getString(R.string.comment_times),
                        mViewBinding.getWish().getCommentTimes()));
        refreshData();
    }

    @Override
    public void refreshData() {
        loadData();
    }

    @Override
    public void OnCommentItemClick() {
        onToComment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case COMMENT_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    loadData();
                }
                break;
        }
    }
}
