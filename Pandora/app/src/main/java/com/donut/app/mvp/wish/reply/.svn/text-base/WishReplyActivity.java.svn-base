package com.donut.app.mvp.wish.reply;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.manager.RequestManager;
import com.bis.android.sharelibrary.ShareBuilderCommonUtil;
import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.activity.CommentActivity;
import com.donut.app.activity.FullImageActivity;
import com.donut.app.activity.LoginActivity;
import com.donut.app.adapter.DetailCommentRecyclerViewAdapter;
import com.donut.app.config.Constant;
import com.donut.app.databinding.ActivityWishReplyBinding;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.wish.AchieveWishVoice;
import com.donut.app.http.message.wish.WishDetailsResponse;
import com.donut.app.model.audio.MediaManager;
import com.donut.app.model.audio.StorageManager;
import com.donut.app.model.video.VideoActivity;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.utils.BindingUtils;
import com.donut.app.utils.GlideCircleTransform;
import com.donut.app.utils.status_bar.StatusBarCompat;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;
import java.util.Map;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public class WishReplyActivity extends MVPBaseActivity<ActivityWishReplyBinding, WishReplyPresenter>
        implements WishReplyContract.View, DetailCommentRecyclerViewAdapter.OnItemClickListener {

    public static final String CONTENT_ID = "CONTENT_ID";

    public static final int COMMENT_REQUEST_CODE = 1;

    private CharSequence temp;

    private int editStart, editEnd;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wish_reply;
    }

    @Override
    protected void initView() {
        StatusBarCompat.setStatusBarColor(this, Constant.default_bar_color);
        updateHeadTitle("心愿详情", true);
        mViewBinding.setHandler(this);
        mViewBinding.setUserInfo(getUserInfo());
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
    public void onCollect(WishDetailsResponse model) {
        if (!getLoginStatus()) {
            showToast(getString(R.string.no_login_msg));
            launchActivity(LoginActivity.class);
            return;
        }

        if (model.getCollectionStatus() == 0) {
            model.setCollectionStatus(1);
            model.setCollectTimes(model.getCollectTimes() + 1);
            mPresenter.onToCollect(model, true);
        } else {
            model.setCollectionStatus(0);
            model.setCollectTimes(model.getCollectTimes() - 1);
            mPresenter.onToCollect(model, false);
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
        mPresenter.saveBehaviour("05");
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
    public void onPlayAudio(View view, WishDetailsResponse model) {

        final View playAnim = mViewBinding.ivWishReplyAudioPlayAnim;

        AchieveWishVoice voice = model.getAchieveVoiceList().get(0);
        new StorageManager(this).getInfo(voice.getAchieveVoiceUrl(), voice.getLastTime(),
                new RequestManager.RequestListener() {
                    @Override
                    public void onRequest() {
                    }

                    @Override
                    public void onLoading(long l, long l1, String s) {
                    }

                    @Override
                    public void onSuccess(String response, Map<String, String> headers,
                                          String url, int actionId) {
                        playAnim.setBackgroundResource(R.drawable.play_anim);
                        final AnimationDrawable animationDrawable
                                = (AnimationDrawable) playAnim.getBackground();
                        animationDrawable.start();
                        MediaManager.playSound(response, new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                animationDrawable.stop();
                                playAnim.setBackgroundResource(R.drawable.voice_gray);
                            }
                        });
                    }

                    @Override
                    public void onError(String errorMsg, String url, int actionId) {
                    }
                });

        voice.setListenTimes(voice.getListenTimes() + 1);
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

        if (response.getWishType() == 0) {
            String showImgUrl = response.getStarHeadPic();
            if (response.getAchievePicUrl() != null
                    && response.getAchievePicUrl().length() > 0) {
                showImgUrl = response.getAchievePicUrl();
            }
            BindingUtils.loadImg(mViewBinding.ivWishReplyShowImg, showImgUrl);
            BindingUtils.loadRoundImg(mViewBinding.ivWishReplyStarHeadImg, response.getStarHeadPic());
            BindingUtils.loadRoundImg(mViewBinding.ivWishReplyStarHeadImg2, response.getStarHeadPic());
        } else {
            Glide.with(this)
                    .load(R.drawable.icon_logo)
                    .centerCrop()
                    .transform(new GlideCircleTransform(this))
                    .into(mViewBinding.ivWishReplyStarHeadImg);
            Glide.with(this)
                    .load(response.getAchievePicUrl())
                    .error(R.drawable.wish_fulfill_top_bg)
                    .centerCrop()
                    .into(mViewBinding.ivWishReplyShowImg);
            Glide.with(this)
                    .load(R.drawable.icon_logo)
                    .centerCrop()
                    .transform(new GlideCircleTransform(this))
                    .into(mViewBinding.ivWishReplyStarHeadImg2);
        }

        DetailCommentRecyclerViewAdapter adapter
                = new DetailCommentRecyclerViewAdapter(this, response.getCurrentComments(), this);
        RecyclerView view = mViewBinding.wishReplyCommentList;
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

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.saveBehaviour("00");
    }

    @Override
    protected void onStop() {
        mPresenter.saveBehaviour("xx");
        super.onStop();
    }
}
