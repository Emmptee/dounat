package com.donut.app.mvp.subject.special;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bis.android.plug.imagecyclelibrary.SubjectImageCycleView;
import com.bis.android.sharelibrary.ShareBuilderCommonUtil;
import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.activity.ChallengeActivity;
import com.donut.app.activity.ChallengeSendActivity;
import com.donut.app.activity.CommentActivity;
import com.donut.app.activity.H5WebActivity;
import com.donut.app.activity.LoginActivity;
import com.donut.app.activity.RewardActivity;
import com.donut.app.activity.StarDetailActivity;
import com.donut.app.activity.SubjectDetailRuleActivity;
import com.donut.app.adapter.DetailCommentRecyclerViewAdapter;
import com.donut.app.config.Constant;
import com.donut.app.customview.DonutJCVideoPlayer;
import com.donut.app.databinding.SubjectSpecialLayoutBinding;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.SubjectResponse;
import com.donut.app.model.video.DensityUtil;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.subject.challenge.SubjectChallengeActivity;
import com.donut.app.mvp.subject.finalpk.SubjectFinalPkActivity;
import com.donut.app.utils.BindingUtils;
import com.donut.app.utils.GlideRoundTransform;
import com.donut.app.utils.status_bar.StatusBarCompat;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by Qi on 2017/4/12.
 * Description : 有本事你来   未调试完成<br>
 */
public class SubjectSpecialActivity extends MVPBaseActivity<SubjectSpecialLayoutBinding, SubjectSpecialPresenter>
        implements SubjectSpecialContract.View, DonutJCVideoPlayer.OnBackBtnClickListener {

    public static final String SUBJECT_ID = "SUBJECT_ID";

    public static final int REQUEST_CODE_LOGIN = 1, COMMENT_REQUEST_CODE = 2,
            REWARD_REQUEST_CODE = 3, CHALLENGE_SEND_REQUEST = 4, STAR_DETAIL_CODE = 5;

    private CharSequence temp;

    private int editStart, editEnd;

    private boolean hasPlayed;

    @Override
    protected int getLayoutId() {
        return R.layout.subject_special_layout;
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
        mViewBinding.challengeLayout.setVisibility(View.GONE);
        findViewById(R.id.challenge_box).setVisibility(View.GONE);
        mViewBinding.setHandler(this);
        mViewBinding.setUserInfo(getUserInfo());
        BindingUtils.loadRoundImg(mViewBinding.commentBottomLayout.includeBottomUserHeader,
                getUserInfo().getImgUrl());
//        if (!sp_Info.getBoolean(Constant.SUBJECT_FIRST, false)) {
//            showGuide();
//        }
    }

    private void showGuide() {
//        mHighLight = new HighLight(this)
//                .autoRemove(false)
//                .intercept(true)
//                .enableNext()
//                .addHighLight(R.id.into_goods_tv,
//                        R.layout.subject_guide_left_layout,
//                        new OnLeftPosCallback(45),
//                        new RectLightShape())
//                .addHighLight(R.id.chellange_linear,
//                        R.layout.subject_guide_challenge_layout,
//                        new OnBaseCallback() {
//                            @Override
//                            public void getPosition(float rightMargin,
//                                                    float bottomMargin,
//                                                    RectF rectF,
//                                                    HighLight.MarginInfo marginInfo) {
//                                marginInfo.rightMargin = 0;
//                                marginInfo.topMargin = rectF.top + rectF.height() + offset;
//                            }
//                        },
//                        new CircleLightShape())
//                .addHighLight(R.id.include_comment_btn_send,
//                        R.layout.subject_guide_right_layout,
//                        new OnLeftPosCallback(45),
//                        new RectLightShape())
//                .setClickCallback(new HighLight.OnClickCallback() {
//                    @Override
//                    public void onClick() {
//                        mHighLight.next();
//                    }
//                });
//
//        mHighLight.show();
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
        mViewBinding.intoGoodsTv.setOnTouchListener(new View.OnTouchListener() {

            RelativeLayout.LayoutParams params
                    = (RelativeLayout.LayoutParams) mViewBinding.intoGoodsTv.getLayoutParams();
            float scale = getResources().getDisplayMetrics().density;
            int marginLeft = (int) (0 - (40 * scale + 0.5f));

            float x = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int left = (int) (marginLeft - (x - event.getX()) / 5);
                        if (left > 0) {
                            left = 0;
                        }
                        params.setMargins(left, params.topMargin, 0, 0);
                        break;
                    case MotionEvent.ACTION_UP:
                        params.setMargins(marginLeft, params.topMargin, 0, 0);
//                        if (event.getEventTime() - event.getDownTime() < 300
//                                && (0 == (x-event.getX()))) {
//                            //点击事件
//                            showGoodsView();
//                        }
                        showGoodsView();
                        break;
                }
                mViewBinding.intoGoodsTv.setLayoutParams(params);
                return true;
            }
        });
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
                    if (mViewBinding.getDetail() != null) {
                        mCommentBtnSend.setText(
                                String.format(getString(R.string.comment_times),
                                        mViewBinding.getDetail().getCommentTimes()));
                    }
                }
            }
        });
    }

    @Override
    public void loadData() {
        mPresenter.loadData(true, getIntent().getStringExtra(SUBJECT_ID));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void showView(final SubjectResponse detail) {
        mViewBinding.tvDescription.setMaxLines(2);
        mViewBinding.setDetail(detail);

        BindingUtils.loadImg(mViewBinding.videoCoverImg, detail.getPublicPic());
        BindingUtils.loadRoundImg(mViewBinding.ivSpecialStarHead, detail.getHeadPic());

        if (detail.getStatus() == 1) {
            showRanking(detail.getRankingList());
        } else if (detail.getStatus() == 2) {
            showFinalPK(detail.getHistoryList());
        }

        showChallenge(detail.getChallengeList());

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

        AnimationDrawable animationDrawable
                = (AnimationDrawable) mViewBinding.ivChallenge.getDrawable();
        animationDrawable.start();

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

    private void showRanking(final List<SubjectResponse.SubjectDetailListDetail> list) {
        LinearLayout mListLinear = (LinearLayout) mViewBinding.current.findViewById(R.id.list_area_images_linear);
        mListLinear.removeAllViews();
        View mListAreaNoData = mViewBinding.current.findViewById(R.id.list_area_no_data);
        if (list != null && list.size() > 0) {
            mListAreaNoData.setVisibility(View.GONE);
            int width = (int) (DensityUtil.getWidthInPx(this) / 4.3);
            for (int i = 0; i < list.size(); i++) {
                final int pos = i;
                ImageView img = new ImageView(this);
                img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                LinearLayout.LayoutParams param0 = new LinearLayout.LayoutParams(width, width);
                if (i != 0) {
                    param0.leftMargin = 20;
                }
                img.setLayoutParams(param0);
                Glide.with(this)
                        .load(list.get(i).getImgUrl())
                        .transform(new GlideRoundTransform(getContext(), 3))
                        .placeholder(R.drawable.default_bg)
                        .error(R.drawable.default_bg)
                        .into(img);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //进入挑战详情
                        mPresenter.saveBehaviour("06");
                        Bundle bundle = new Bundle();
                        bundle.putString(SubjectChallengeActivity.CONTENT_ID, list.get(pos).getContentId());
                        bundle.putString(SubjectChallengeActivity.SUBJECT_ID, getIntent().getStringExtra(SUBJECT_ID));
                        launchActivity(SubjectChallengeActivity.class, bundle);
                    }
                });
                mListLinear.addView(img);
            }
        } else {
            mListAreaNoData.setVisibility(View.VISIBLE);
        }
    }

    private void showFinalPK(final List<SubjectResponse.SubjectDetailListDetail> list) {
        if (list == null || list.size() <= 0) {
            return;
        }

        ArrayList<String> urlList = new ArrayList<>();
        ArrayList<String> imageDescList = new ArrayList<>();

        for (SubjectResponse.SubjectDetailListDetail adv : list) {
            urlList.add(adv.getImgUrl());
            imageDescList.add(adv.getTitle());
        }

        SubjectImageCycleView.ImageCycleViewListener mAdCycleViewListener
                = new SubjectImageCycleView.ImageCycleViewListener() {
            @Override
            public void onImageClick(int position, View imageView) {
                /*实现点击事件*/
                Bundle bundle = new Bundle();
                bundle.putString(SubjectFinalPkActivity.CONTENT_ID, list.get(position).getContentId());
                bundle.putString(SubjectFinalPkActivity.SUBJECT_ID, getIntent().getStringExtra(SUBJECT_ID));
                launchActivity(SubjectFinalPkActivity.class, bundle);
            }

            @Override
            public void displayImage(String imageURL, ImageView imageView) {
                /*在此方法中，显示图片，可以用自己的图片加载库，也可以用Imageloader */
//                ImageLoaderHelper.displayImage(imageURL, ImageLoaderHelper.optionsForCommon, imageView);
                Glide.with(SubjectSpecialActivity.this)
                        .load(imageURL)
                        .transform(new GlideRoundTransform(getContext(), 3))
                        .placeholder(R.drawable.default_bg)
                        .error(R.drawable.default_bg)
                        .into(imageView);
            }
        };
        /*设置数据*/
        mViewBinding.finalPkCycleView.setImageResources(imageDescList, urlList, mAdCycleViewListener);
//        if (urlList.size() > 0) {
//            mViewBinding.finalPkCycleView.startImageCycle();
//        }
    }

    private void showChallenge(final List<SubjectResponse.SubjectDetailListDetail> list) {

        float scale = getResources().getDisplayMetrics().density;
        int width = (int) (DensityUtil.getWidthInPx(this) / 4 - (10 * scale + 0.5f));
        TextView mNoData = (TextView) mViewBinding.challengeAreaLayout
                .findViewById(R.id.challenge_area_no_data);
        LinearLayout mChallengeLayout = (LinearLayout) mViewBinding.challengeAreaLayout
                .findViewById(R.id.chellange_area_linear);
        final List<SubjectResponse.SubjectDetailListDetail> mChallengeList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            int size = list.size();
            if (size > 4) {
                for (int i = 0; i < 4; i++) {
                    mChallengeList.add(list.get(i));
                }
            } else {
                mChallengeList.addAll(list);
            }
        } else {
            RelativeLayout.LayoutParams param
                    = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, width);
            mNoData.setVisibility(View.VISIBLE);
            mNoData.setLayoutParams(param);
            mNoData.setText("还没有人挑战TA , 来试试");
            return;
        }

        mChallengeLayout.removeAllViews();
        mNoData.setVisibility(View.GONE);
        for (int i = 0; i < mChallengeList.size(); i++) {
            final int pos = i;
            ImageView img = new ImageView(this);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, width);
            if (i != 0) {
                param.leftMargin = 15;
            }
            img.setLayoutParams(param);
            BindingUtils.loadImg(img, mChallengeList.get(i).getImgUrl());
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //进入挑战详情
                    mPresenter.saveBehaviour("06");
                    Bundle bundle = new Bundle();
                    bundle.putString(SubjectChallengeActivity.CONTENT_ID, mChallengeList.get(pos).getContentId());
                    bundle.putString(SubjectChallengeActivity.SUBJECT_ID, getIntent().getStringExtra(SUBJECT_ID));
                    launchActivity(SubjectChallengeActivity.class, bundle);
                }
            });
            mChallengeLayout.addView(img);
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
        super.onStop();
        if (mViewBinding.finalPkCycleView != null) {
            mViewBinding.finalPkCycleView.pushImageCycle();
        }
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
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
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

    private void showGoodsView() {
        sp_Info.edit().putString("subjectId", getIntent().getStringExtra(SUBJECT_ID)).apply();
        Bundle bundle = new Bundle();
        bundle.putString(H5WebActivity.URL, "file:///android_asset/www/more.html");
        launchActivity(H5WebActivity.class, bundle);
        overridePendingTransition(R.anim.first_left_in, R.anim.first_left_out);
    }

    public void onPlayClick() {
        if (mViewBinding.getDetail() == null) {
            return;
        }
        hasPlayed = true;
        mViewBinding.videoCover.setVisibility(View.GONE);
        mViewBinding.detailVideoPlayer.setVisibility(View.VISIBLE);
        DonutJCVideoPlayer videoPlayer = mViewBinding.detailVideoPlayer;
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

    public void onStarDetailClick(SubjectResponse model) {
        Bundle bundle = new Bundle();
        bundle.putString(StarDetailActivity.STAR_ID, model.getActorId());
        launchActivityForResult(StarDetailActivity.class, bundle, STAR_DETAIL_CODE);
    }

    public void onFollowClick(SubjectResponse model) {
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

    public void onCollectClick(SubjectResponse model) {
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

    public void onLikeClick(SubjectResponse model) {
        if (!getLoginStatus()) {
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
            return;
        }
        if (model.getPraiseStatis() == 0) {
            model.setPraiseStatis(1);
            model.setPraiseTimes(model.getPraiseTimes() + 1);
            mPresenter.onLike(model, true);
        } else {
            model.setPraiseStatis(0);
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
        SubjectResponse model = mViewBinding.getDetail();
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
    }

    public void onToRuleClick() {
        //详细规则
        mPresenter.saveBehaviour("09");
        Bundle bundle_rule = new Bundle();
        bundle_rule.putString(SubjectDetailRuleActivity.SUBJECT_ID,
                getIntent().getStringExtra(SUBJECT_ID));
        bundle_rule.putString(SubjectDetailRuleActivity.CONTENT_ID,
                mViewBinding.getDetail().getContentId());
        launchActivity(SubjectDetailRuleActivity.class, bundle_rule);
    }

    public void onChallengeClick() {
        if (!getLoginStatus()) {
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
            return;
        }
        if (mViewBinding.getDetail() == null) {
            return;
        }
        mPresenter.saveBehaviour("01");
        Bundle bundle = new Bundle();
        bundle.putString(ChallengeSendActivity.SUBJECT_ID,
                getIntent().getStringExtra(SUBJECT_ID));
        launchActivityForResult(ChallengeSendActivity.class, bundle, CHALLENGE_SEND_REQUEST);
    }

    public void onToAllChallengeClick() {
        mPresenter.saveBehaviour("12");
        Bundle bundle = new Bundle();
        bundle.putString(ChallengeActivity.SUBJECT_ID,
                getIntent().getStringExtra(SUBJECT_ID));
        launchActivity(ChallengeActivity.class, bundle);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
        if (mViewBinding.getDetail() == null) {
            return;
        }
        String linkurl;
        if (mViewBinding.getDetail().getStatus() == 1) {
            linkurl = RequestUrl.SUBJECT_DETAIL_SHARE_URL + "header="
                    + HeaderRequest.SUBJECT_DETAIL + "&subjectId=" + getIntent().getStringExtra(SUBJECT_ID);
        } else {
            linkurl = RequestUrl.SUBJECT_HISTORY_DETAIL_SHARE_URL + "header="
                    + HeaderRequest.SUBJECT_DETAIL + "&subjectId=" + getIntent().getStringExtra(SUBJECT_ID);
        }
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo);
        ShareBuilderCommonUtil.Builder builder = new ShareBuilderCommonUtil.Builder(this);
        builder.setTitle(mViewBinding.getDetail().getActorName()
                + ",【小伙伴】已围观了" + mViewBinding.getDetail().getBrowseTimes() + "次");
        builder.setContent("【" + mViewBinding.getDetail().getName() + "】"
                + mViewBinding.getDetail().getDescription());
        builder.setLinkUrl(linkurl);
        builder.setBitmap(bmp);
        builder.setShareMedia(new SHARE_MEDIA[]{SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA});
//        builder.setListener(new ShareBuilderCommonUtil.OnResponseListener() {
//            @Override
//            public void onSuccessRes() {
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
            case CHALLENGE_SEND_REQUEST:
                if (resultCode == RESULT_OK) {
                    refreshView();
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
        mViewBinding.detailVideoPlayer.setVisibility(View.GONE);
        loadData();
    }

}