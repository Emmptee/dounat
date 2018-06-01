package com.donut.app.mvp.subject.notice;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bis.android.sharelibrary.ShareBuilderCommonUtil;
import com.donut.app.R;
import com.donut.app.activity.CommentActivity;
import com.donut.app.activity.LoginActivity;
import com.donut.app.activity.RewardActivity;
import com.donut.app.activity.StarDetailActivity;
import com.donut.app.activity.SubjectDetailRuleActivity;
import com.donut.app.adapter.DetailCommentRecyclerViewAdapter;
import com.donut.app.config.Constant;
import com.donut.app.customview.DonutJCVideoPlayer;
import com.donut.app.databinding.ActivitySubjectNoticeBinding;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.subjectSnap.SubjectSnapDetailResponse;
import com.donut.app.model.video.DensityUtil;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.blooper.detail.BlooperDetailActivity;
import com.donut.app.utils.BindingUtils;
import com.donut.app.utils.status_bar.StatusBarCompat;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by Qi on 2017/3/16.
 * Description : 大咖有通告<br>
 */
public class SubjectNoticeActivity extends MVPBaseActivity<ActivitySubjectNoticeBinding, SubjectNoticePresenter>
        implements SubjectNoticeContract.View, DonutJCVideoPlayer.OnBackBtnClickListener {

    public static final String SUBJECT_ID = "SUBJECT_ID";

    public static final int REQUEST_CODE_LOGIN = 1, COMMENT_REQUEST_CODE = 2,
            REWARD_REQUEST_CODE = 3, STAR_DETAIL_CODE = 4;

    private CharSequence temp;

    private int editStart, editEnd;

    private boolean hasPlayed;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_subject_notice;
    }

    @Override
    protected void initView() {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)) {
            RelativeLayout.LayoutParams params =
                    (RelativeLayout.LayoutParams) mViewBinding.videoCoverBack.getLayoutParams();
            params.setMargins(0, getStatusBarHeight(this), 0, 0);
        }
        StatusBarCompat.translucentStatusBar(this, false);
        float videoWidth = DensityUtil.getWidthInPx(this);
        float videoHeight = videoWidth * 9 / 16;
        ViewGroup videoLayout = mViewBinding.videoTopLayout;
        ViewGroup.LayoutParams params = videoLayout.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = (int) videoHeight;
        videoLayout.setLayoutParams(params);

        mViewBinding.setHandler(this);
        mViewBinding.setUserInfo(getUserInfo());
        BindingUtils.loadRoundImg(mViewBinding.commentBottomLayout.includeBottomUserHeader,
                getUserInfo().getImgUrl());
    }

    private static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelOffset(resId);
        }
        return result;
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
                    if (mViewBinding.getDetail() == null) {
                        return;
                    }
                    mCommentBtnSend.setText(
                            String.format(getString(R.string.comment_times),
                                    mViewBinding.getDetail().getCommentTimes()));
                }
            }
        });
    }

    @Override
    public void loadData() {
        mPresenter.loadData(true, getIntent().getStringExtra(SUBJECT_ID));
    }

    @Override
    public void showView(final SubjectSnapDetailResponse detail) {
        mViewBinding.tvDescription.setMaxLines(2);
        mViewBinding.setDetail(detail);

        BindingUtils.loadImg(mViewBinding.videoCoverImg, detail.getPublicPic());
        BindingUtils.loadRoundImg(mViewBinding.ivSnapStarHead, detail.getHeadPic());

        if (detail.getCurrentComments() != null) {
            DetailCommentRecyclerViewAdapter.OnItemClickListener listener
                    = new DetailCommentRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void OnCommentItemClick() {
                    onToCommentClick();
                }
            };
            DetailCommentRecyclerViewAdapter adapter
                    = new DetailCommentRecyclerViewAdapter(this, detail.getCurrentComments(), listener);

            RecyclerView view = mViewBinding.snapCommentList;
            view.setLayoutManager(new LinearLayoutManager(this));
            view.setNestedScrollingEnabled(false);
            view.setHasFixedSize(false);
            view.setAdapter(adapter);
        }

        EditText mCommentEt = mViewBinding.commentBottomLayout.includeCommentEt;
        if (mCommentEt.getText().length() > 0) {
            mCommentEt.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mViewBinding.commentBottomLayout.includeCommentBtnSend.setText(getString(R.string.send_msg));
                }
            }, 100);
        }
    }

    @Override
    public void clearCommentEdit() {
        if (mViewBinding.getDetail() == null) {
            return;
        }
        mViewBinding.commentBottomLayout
                .includeCommentEt.setText("");
        mViewBinding.commentBottomLayout
                .includeCommentBtnSend.setText(
                String.format(getString(R.string.comment_times),
                        mViewBinding.getDetail().getCommentTimes()));
        loadData();
    }

    @Override
    protected void onStop() {
        mPresenter.saveBehaviour("xx");
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.saveBehaviour("00", mPresenter.requestObject, HeaderRequest.SUBJECT_NOTICE_DETAIL);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.include_comment_btn_send:
                String msg = mViewBinding.commentBottomLayout
                        .includeCommentEt.getText().toString().trim();
                if (TextUtils.isEmpty(msg)) {
                    onToCommentClick();
                    break;
                }
                if (!getLoginStatus()) {
                    launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
                    break;
                }
                mPresenter.sendComment(mViewBinding.getDetail(), msg);
                mPresenter.saveBehaviour("04");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        mPresenter.saveBehaviour("05");
        Intent intent = new Intent();
        if (mViewBinding.getDetail() != null) {
            intent.putExtra(Constant.COLLECT_STATUS,
                    mViewBinding.getDetail().getCollectionStatus());
        }
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public void backBtnClick() {
        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mViewBinding.getDetail() != null && hasPlayed) {
            onPlayClick();
        }
    }

    public void onPlayClick() {
        if (mViewBinding.getDetail() == null) {
            return;
        }
        hasPlayed = true;
        mViewBinding.videoCover.setVisibility(View.GONE);
        mViewBinding.snapVideoPlayer.setVisibility(View.VISIBLE);
        DonutJCVideoPlayer videoPlayer = mViewBinding.snapVideoPlayer;
        videoPlayer.setUp(mViewBinding.getDetail().getPlayUrl(),
                DonutJCVideoPlayer.SCREEN_LAYOUT_NORMAL,
                mViewBinding.getDetail().getName(),
                mViewBinding.getDetail().getBrowseTimes(),
                this);
        videoPlayer.startButton.performClick();
//        mPresenter.addPlayNum(mViewBinding.getDetail().getContentId());
    }

    public void onBackClick() {
        onBackPressed();
    }

    public void onStarDetailClick(SubjectSnapDetailResponse model) {
            Bundle bundle = new Bundle();
            bundle.putString(StarDetailActivity.STAR_ID, model.getActorId());
            launchActivityForResult(StarDetailActivity.class, bundle, STAR_DETAIL_CODE);
            mPresenter.saveBehaviour("02");
       /* Bundle bundle = new Bundle();
        bundle.putString(BlooperDetailActivity.STAR_ID, model.getActorId());
        launchActivity(BlooperDetailActivity.class, bundle);*/
    }

    public void onFollowClick(SubjectSnapDetailResponse model) {
        if (!getLoginStatus()) {
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
        } else {
            if (model.getFollowStatus() == 0) {
                model.setFollowStatus(1);
                mPresenter.onToFollow(model, true);
            } else {
                model.setFollowStatus(0);
                mPresenter.onToFollow(model, false);
            }
        }
    }

    public void onCollectClick(SubjectSnapDetailResponse model) {
        if (!getLoginStatus()) {
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
        } else {
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
    }

    public void onLikeClick(SubjectSnapDetailResponse model) {
        if (!getLoginStatus()) {
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
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

    public void onRewardClick() {
        if (!getLoginStatus()) {
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
            return;
        }
        if (mViewBinding.getDetail() == null) {
            return;
        }
        SubjectSnapDetailResponse model = mViewBinding.getDetail();
        Bundle reward_bun = new Bundle();
        reward_bun.putString(RewardActivity.PAYEE_NAME, model.getActorName());
        reward_bun.putString(RewardActivity.PAYEE_HEADER, model.getHeadPic());
        reward_bun.putString(RewardActivity.CONTENT_ID, model.getContentId());
        reward_bun.putBoolean(RewardActivity.IS_STAR, getUserInfo().getUserType() == 1);
        launchActivityForResult(RewardActivity.class, reward_bun, REWARD_REQUEST_CODE);
    }

    public void onShareClick() {
        requestRuntimePermission("为了给您提供更好的服务,甜麦圈需要获取存储器读写权限",
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public void onToCommentClick() {
        if (!getLoginStatus()) {
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
            return;
        }
        if (mViewBinding.getDetail() == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(CommentActivity.CONTENTID, mViewBinding.getDetail().getContentId());
        launchActivityForResult(CommentActivity.class, bundle, COMMENT_REQUEST_CODE);
        mPresenter.saveBehaviour("03");
    }

    public void onToRuleClick() {
        //通告内容
        Bundle bundle_rule = new Bundle();
        bundle_rule.putBoolean(SubjectDetailRuleActivity.IS_SUBJECT_NOTICE,
                true);
        bundle_rule.putString(SubjectDetailRuleActivity.SUBJECT_ID,
                getIntent().getStringExtra(SUBJECT_ID));
        bundle_rule.putString(SubjectDetailRuleActivity.CONTENT_ID,
                mViewBinding.getDetail().getContentId());
        launchActivity(SubjectDetailRuleActivity.class, bundle_rule);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
        if (mViewBinding.getDetail() == null) {
            return;
        }
        String linkurl = RequestUrl.SUBJECT_NOTICE_SHARE_URL
                + "header=" + HeaderRequest.SUBJECT_NOTICE_DETAIL
                + "&subjectId=" + getIntent().getStringExtra(SUBJECT_ID);

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo);
        ShareBuilderCommonUtil.Builder builder = new ShareBuilderCommonUtil.Builder(this);

        builder.setTitle(mViewBinding.getDetail().getActorName()
                + ",【小伙伴】已围观了" + mViewBinding.getDetail().getBrowseTimes() + "次");
        builder.setContent(mViewBinding.getDetail().getDescription());
        builder.setLinkUrl(linkurl);
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
        mPresenter.shareRequest(mViewBinding.getDetail().getContentId());
        mViewBinding.getDetail().setShareTimes(mViewBinding.getDetail().getShareTimes() + 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_LOGIN:
                if (resultCode == RESULT_OK) {
                    refreshView();
                }
                break;
            case REWARD_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        String content = data.getStringExtra(RewardActivity.REWARD_CONTENT);
                        if (!TextUtils.isEmpty(content)) {
                            refreshView();
                            break;
                        }
                        float amount = data.getFloatExtra(RewardActivity.REWARD_AMOUNT, 0f);
                        mViewBinding.getDetail().setRewardTimes(
                                (long) (mViewBinding.getDetail().getRewardTimes() + amount));
                    } else {
                        refreshView();
                    }
                }
                break;
            case STAR_DETAIL_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        int followStatus = data.getIntExtra(StarDetailActivity.FOLLOW_STATUS, 0);
                        mViewBinding.getDetail().setFollowStatus(followStatus);
                    }
                }
                break;
        }
    }

    private void refreshView() {
        JCVideoPlayer.releaseAllVideos();
        mViewBinding.videoCover.setVisibility(View.VISIBLE);
        mViewBinding.snapVideoPlayer.setVisibility(View.GONE);
        loadData();
    }

}