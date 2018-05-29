package com.donut.app.mvp.subject.challenge;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.manager.RequestManager;
import com.bis.android.sharelibrary.ShareBuilderCommonUtil;
import com.donut.app.R;
import com.donut.app.activity.ChallengeActivity;
import com.donut.app.activity.CommentActivity;
import com.donut.app.activity.LoginActivity;
import com.donut.app.activity.RewardActivity;
import com.donut.app.activity.RuleDetailActivity;
import com.donut.app.activity.StarCommentRadioActivity;
import com.donut.app.activity.StarDetailActivity;
import com.donut.app.adapter.DetailCommentRecyclerViewAdapter;
import com.donut.app.config.Constant;
import com.donut.app.customview.DonutJCVideoPlayer;
import com.donut.app.customview.PayRecoderDialog;
import com.donut.app.databinding.SubjectChallengeLayoutBinding;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.SubjectHistoryPKDetail;
import com.donut.app.http.message.SubjectHistoryPKDetailResponse;
import com.donut.app.http.message.SubjectHistoryPKStarComment;
import com.donut.app.model.audio.AudioRecorderButton;
import com.donut.app.model.audio.MediaManager;
import com.donut.app.model.audio.StorageManager;
import com.donut.app.model.video.DensityUtil;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.subject.special.SubjectSpecialActivity;
import com.donut.app.utils.BindingUtils;
import com.donut.app.utils.CommonUtils;
import com.donut.app.utils.status_bar.StatusBarCompat;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by Qi on 2017/4/12.
 * Description : 挑战详情   <br>
 */
public class SubjectChallengeActivity extends MVPBaseActivity<SubjectChallengeLayoutBinding, SubjectChallengePresenter>
        implements SubjectChallengeContract.View, DonutJCVideoPlayer.OnBackBtnClickListener, PayRecoderDialog.onPayListener {

    public static final String SUBJECT_ID = "SUBJECT_ID";

    public static final String CONTENT_ID = "CONTENT_ID";

    public static final String IS_COLLECT = "IS_COLLECT";

    private String contentId, subjectId;

    public static final int REQUEST_CODE_LOGIN = 1, COMMENT_REQUEST_CODE = 2,
            REWARD_REQUEST_CODE = 3, CHALLENGE_SEND_REQUEST = 4, MORE_RECODER_CODE = 5;

    private CharSequence temp;

    private int editStart, editEnd;

    private boolean hasPlayed;

    private AnimationDrawable mAnimationDrawable;

    @Override
    protected int getLayoutId() {
        return R.layout.subject_challenge_layout;
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

        mViewBinding.recordTxt.setAudioFinishRecorderListener(
                new AudioRecorderButton.AudioFinishRecorderListener() {
                    @Override
                    public void onFinish(float seconds, String filePath) {
                        mPresenter.upLoadAudio(seconds, filePath, contentId);
                    }
                });
    }

    @Override
    public void loadData() {
        subjectId = getIntent().getStringExtra(SUBJECT_ID);
        contentId = getIntent().getStringExtra(CONTENT_ID);

        mPresenter.loadData(true, contentId);
    }

    @Override
    public void showView(final SubjectHistoryPKDetailResponse detail) {
        onHideRecordLayout();
        mViewBinding.tvDescription.setMaxLines(2);
        mViewBinding.setDetail(detail);

        BindingUtils.loadImg(mViewBinding.videoCoverImg, detail.getThumbnailUrl());
        BindingUtils.loadRoundImg(mViewBinding.ivChallengeUserHead, detail.getHeadPic());

        showChallenge(detail.getChallengeList());

        showRecorderListenBtn(detail);

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

    private void showRecorderListenBtn(SubjectHistoryPKDetailResponse detail) {
        SubjectHistoryPKStarComment audio = detail.getAudio();
        if (getLoginStatus() && audio != null && audio.getUuid() != null) {
            boolean status = audio.getStatus() != null
                    && Integer.parseInt(audio.getStatus()) == 1;
            if (getUserInfo().getUserId().equals(audio.getFkA01())
                    || status || getUserInfo().getUserType() == 1) {
                mViewBinding.tvRecorderListen.setVisibility(View.GONE);
            } else {
                mViewBinding.tvRecorderListen.setVisibility(View.VISIBLE);
            }
        } else {
            mViewBinding.tvRecorderListen.setVisibility(View.VISIBLE);
        }

    }

    private void showChallenge(final List<SubjectHistoryPKDetail> list) {

        float scale = getResources().getDisplayMetrics().density;
        int width = (int) (DensityUtil.getWidthInPx(this) / 4 - (10 * scale + 0.5f));
        TextView mNoData = (TextView) mViewBinding.challengeAreaLayout
                .findViewById(R.id.challenge_area_no_data);
        LinearLayout mChallengeLayout = (LinearLayout) mViewBinding.challengeAreaLayout
                .findViewById(R.id.chellange_area_linear);
        final List<SubjectHistoryPKDetail> mChallengeList = new ArrayList<>();
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
                    String cId = mChallengeList.get(pos).getContentId();
                    if (contentId.equals(cId)) {
                        return;
                    }
                    //进入挑战详情
                    Bundle bundle = new Bundle();
                    bundle.putString(SubjectChallengeActivity.CONTENT_ID, mChallengeList.get(pos).getContentId());
                    bundle.putString(SubjectChallengeActivity.SUBJECT_ID, subjectId);
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
        mPresenter.loadData(true, contentId);
    }

    @Override
    public void sendAudioSuccess() {
        mPresenter.loadData(true, contentId);
    }

    @Override
    protected void onStop() {
        mPresenter.saveBehaviour("xx");
        MediaManager.release();
        if (mAnimationDrawable != null) {
            mAnimationDrawable.stop();
            mViewBinding.recorderPlayAnim.setBackgroundResource(R.drawable.voice_gray);
        }
        super.onStop();
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
                mPresenter.sendComment(contentId, msg);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        mPresenter.saveBehaviour("11");
        Intent intent = new Intent();
        if (mViewBinding.getDetail() != null) {
            intent.putExtra(Constant.COLLECT_STATUS,
                    mViewBinding.getDetail().getCollectionStatus() == 1);
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
        mViewBinding.detailVideoPlayer.setVisibility(View.VISIBLE);
        DonutJCVideoPlayer videoPlayer = mViewBinding.detailVideoPlayer;
        videoPlayer.setUp(mViewBinding.getDetail().getPlayUrl(),
                DonutJCVideoPlayer.SCREEN_LAYOUT_NORMAL,
                mViewBinding.getDetail().getName(),
                mViewBinding.getDetail().getBrowseTimes(),
                this);
        videoPlayer.startButton.performClick();
//        mPresenter.addPlayNum(contentId);
    }

    public void onBackClick() {
        onBackPressed();
    }

    public void onToRule(SubjectHistoryPKDetailResponse model) {
        Bundle bundle_detail = new Bundle();
        bundle_detail.putString(RuleDetailActivity.DETAIL, model.getDescription());
        bundle_detail.putBoolean(RuleDetailActivity.CHALL_TYPE, true);
        launchActivity(RuleDetailActivity.class, bundle_detail);
    }

    public void onStarSendClick(SubjectHistoryPKDetailResponse model) {
        mPresenter.saveBehaviour("10");
        mViewBinding.recordLayout.setVisibility(View.VISIBLE);
    }

    public void onHideRecordLayout() {
        mViewBinding.recordLayout.setVisibility(View.GONE);
    }

    public void onStarRecommendClick(SubjectHistoryPKDetailResponse model) {
        if (model.getLookGoodOnHim() == 1) {
            model.setLookGoodOnHim(0);
            model.setStarRecommend(0);
            mPresenter.recommendRequest(contentId, true);
        } else {
            model.setLookGoodOnHim(1);
            model.setStarRecommend(1);
            mPresenter.recommendRequest(contentId, false);
        }
    }

    public void onCollectClick(SubjectHistoryPKDetailResponse model) {
        mPresenter.saveBehaviour("08");
        if (!getLoginStatus()) {
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
        } else {
            if (model.getCollectionStatus() == 0) {
                model.setCollectionStatus(1);
                model.setCollectTimes(model.getCollectTimes() + 1);
                mPresenter.onToCollect(contentId, true);
            } else {
                model.setCollectionStatus(0);
                model.setCollectTimes(model.getCollectTimes() - 1);
                mPresenter.onToCollect(contentId, false);
            }
        }
    }

    public void onLikeClick(SubjectHistoryPKDetailResponse model) {
        mPresenter.saveBehaviour("06");
        if (!getLoginStatus()) {
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
            return;
        }
        if (model.getPraiseStatis() == 0) {
            model.setPraiseStatis(1);
            model.setPraiseTimes(model.getPraiseTimes() + 1);
            mPresenter.onLike(contentId, true);
        } else {
            model.setPraiseStatis(0);
            model.setPraiseTimes(model.getPraiseTimes() - 1);
            mPresenter.onLike(contentId, false);
        }
    }

    public void onRewardClick() {
        mPresenter.saveBehaviour("05");
        if (!getLoginStatus()) {
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
            return;
        }
        if (mViewBinding.getDetail() == null) {
            return;
        }
        SubjectHistoryPKDetailResponse model = mViewBinding.getDetail();
        Bundle reward_bun = new Bundle();
        reward_bun.putString(RewardActivity.PAYEE_NAME, model.getActorName());
        reward_bun.putString(RewardActivity.PAYEE_HEADER, model.getHeadPic());
        reward_bun.putString(RewardActivity.CONTENT_ID, contentId);
        reward_bun.putBoolean(RewardActivity.IS_STAR, getUserInfo().getUserType() == 1);
        launchActivityForResult(RewardActivity.class, reward_bun, REWARD_REQUEST_CODE);
    }

    public void onShareClick() {
        mPresenter.saveBehaviour("07");
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
        bundle.putString(CommentActivity.CONTENTID, contentId);
        launchActivityForResult(CommentActivity.class, bundle, COMMENT_REQUEST_CODE);
    }

    public void onToSpecialClick() {
        //参与专题
        mPresenter.saveBehaviour("01");
        Bundle bundle_rule = new Bundle();
        bundle_rule.putString(SubjectSpecialActivity.SUBJECT_ID,
                subjectId);
        launchActivity(SubjectSpecialActivity.class, bundle_rule);
    }

    public void onToAllChallengeClick() {
        mPresenter.saveBehaviour("02");
        Bundle bundle = new Bundle();
        bundle.putString(ChallengeActivity.SUBJECT_ID,
                subjectId);
        launchActivity(ChallengeActivity.class, bundle);
    }

    public void onToAllStarCommentClick(SubjectHistoryPKDetailResponse model) {
        if (getLoginStatus()) {
            Bundle b = new Bundle();
            b.putString(StarCommentRadioActivity.CONTENT_ID, contentId);
            b.putString(StarCommentRadioActivity.CHALLENGE_USERID, model.getActorId());
            b.putString(StarCommentRadioActivity.AUDIO_UUID, model.getAudio().getUuid());
            launchActivityForResult(StarCommentRadioActivity.class, b, MORE_RECODER_CODE);
        } else {
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
        }
    }

    public void onPlayAudioClick(SubjectHistoryPKDetailResponse model) {
        if (getLoginStatus()) {
            if (mViewBinding.tvRecorderListen.getVisibility() == View.VISIBLE) {
                showPayRecoderDialog(model);
            } else {
                mViewBinding.recorderPlayAnim.setBackgroundResource(R.drawable.play_anim);
                mAnimationDrawable = (AnimationDrawable) mViewBinding.recorderPlayAnim.getBackground();
                mAnimationDrawable.start();
                new StorageManager(this).getInfo(model.getAudio().getAudioUrl(), model.getAudio().getLastTime(),
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
                                MediaManager.playSound(response, new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        mAnimationDrawable.stop();
                                        mViewBinding.recorderPlayAnim.setBackgroundResource(R.drawable.voice_gray);
                                    }
                                });
                            }

                            @Override
                            public void onError(String errorMsg, String url, int actionId) {

                            }
                        });
            }
        } else {
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
        }
    }

    private void showPayRecoderDialog(SubjectHistoryPKDetailResponse model) {
        PayRecoderDialog dialog = new PayRecoderDialog(this, this, model.getAudio().getUuid());
        dialog.setContent(CommonUtils.setName(this, getUserInfo().getNickName()),
                getUserInfo().getImgUrl(),
                model.getBalance(),
                model.getVideoPrice());
        dialog.show();
    }

    public void onToStarDetailClick(SubjectHistoryPKDetailResponse model) {
        if (model.getUserType() == 1) {
            Bundle bundle = new Bundle();
            bundle.putString(StarDetailActivity.STAR_ID, model.getActorId());
            bundle.putString(StarDetailActivity.FKA01_ID, model.getActorId());
            launchActivity(StarDetailActivity.class, bundle);
        }
    }

    public void onToStarRecorderDetailClick(SubjectHistoryPKDetailResponse model) {
        Bundle bundle = new Bundle();
        bundle.putString(StarDetailActivity.STAR_ID, model.getAudio().getActorUuid());
        launchActivity(StarDetailActivity.class, bundle);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
        if (mViewBinding.getDetail() == null) {
            return;
        }
        String linkurl = RequestUrl.CHALLENGE_DETAIL_SHARE_URL + "header=" + HeaderRequest.FINAL_PK_DETAIL + "&contentId=" + contentId;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo);
        ShareBuilderCommonUtil.Builder builder = new ShareBuilderCommonUtil.Builder(this);
        builder.setTitle("【小伙伴】已围观了"
                + mViewBinding.getDetail().getBrowseTimes() + "次"
                + mViewBinding.getDetail().getName());
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
        mPresenter.shareRequest(contentId);
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
            case MORE_RECODER_CODE:
                if (resultCode == RESULT_OK) {
                    onPaySuccess();
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

    @Override
    public void onPaySuccess() {
        SubjectHistoryPKDetailResponse detail = mViewBinding.getDetail();
        Integer time = detail.getAudio().getListenTimes();
        if (time != null) {
            time += 1;
        } else {
            time = 1;
        }
        detail.getAudio().setListenTimes(time);
        detail.getAudio().setStatus("1");
        showRecorderListenBtn(detail);
        mViewBinding.recorderPlayTimes.setText(
                String.format(getString(R.string.challenge_audio_times), time));
    }

    @Override
    public void onPayFail(String code, String msg) {
    }
}