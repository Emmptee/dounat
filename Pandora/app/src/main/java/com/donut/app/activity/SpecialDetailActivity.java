package com.donut.app.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bis.android.plug.autolayout.AutoLinearLayout;
import com.bis.android.plug.autolayout.AutoRelativeLayout;
import com.bis.android.plug.imagecyclelibrary.SubjectImageCycleView;
import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseGridLayoutManager;
import com.bis.android.sharelibrary.ShareBuilderCommonUtil;
import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.adapter.InnerCommentAdapter;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.customview.InSideGridView;
import com.donut.app.customview.InSideListView;
import com.donut.app.customview.ScrollAlwaysTextView;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.CollectRequest;
import com.donut.app.http.message.CommentSubmitRequest;
import com.donut.app.http.message.ContentComments;
import com.donut.app.http.message.PraiseRequest;
import com.donut.app.http.message.ShareRequest;
import com.donut.app.http.message.SubjectRequest;
import com.donut.app.http.message.SubjectResponse;
import com.donut.app.model.video.DensityUtil;
import com.donut.app.model.video.FullScreenVideoView;
import com.donut.app.model.video.LightnessController;
import com.donut.app.model.video.VolumnController;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.CommonUtils;
import com.donut.app.utils.GlideCircleTransform;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.NetUtils;
import com.donut.app.utils.StatusBarUtil;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wujiaojiao on 2016/5/25.
 */
public class SpecialDetailActivity extends BaseActivity implements TextView.OnEditorActionListener, View.OnClickListener
{
    @ViewInject(R.id.videoview)
    private FullScreenVideoView mVideo;
    @ViewInject(R.id.top_layout)
    private View mTopView;
    @ViewInject(R.id.bottom_layout)
    private View mBottomView;
    @ViewInject(R.id.seekbar)
    private SeekBar mSeekBar;
    @ViewInject(R.id.play_btn)
    private ImageView mPlay;
    @ViewInject(R.id.play_btn_linear)
    private LinearLayout mPlayLinear;
    @ViewInject(R.id.play_time)
    private TextView mPlayTime;
    @ViewInject(R.id.total_time)
    private TextView mDurationTime;
    @ViewInject(R.id.video_layout)
    private RelativeLayout mlayout;
    @ViewInject(R.id.play_num_txt)
    private TextView mPlayNum;
    @ViewInject(R.id.progress_loading)
    private ProgressBar mLoading;
    @ViewInject(R.id.video_title)
    private ScrollAlwaysTextView mTitle;

    @ViewInject(R.id.header)
    private ImageView mHeader;
    @ViewInject(R.id.header_layout)
    private AutoRelativeLayout mHeaderLayout;

    @ViewInject(R.id.header_v)
    private ImageView mHeaderVip;
    @ViewInject(R.id.user_header_v)
    private ImageView mUserHeaderVip;
    @ViewInject(R.id.bottom_user_header_bg)
    private ImageView mUserHeaderBg;

    @ViewInject(R.id.title)
    private TextView mSubjectTitle;
    @ViewInject(R.id.extra)
    private TextView mExtra;
    @ViewInject(R.id.brief)
    private TextView mBrief;
    @ViewInject(R.id.chellange_num)
    private TextView mChallNum;
    @ViewInject(R.id.chellange_icon)
    private ImageView mChallIcon;
    @ViewInject(R.id.reward_num)
    private TextView mRewardNum;
    @ViewInject(R.id.parise_num)
    private TextView mPariseNum;
    @ViewInject(R.id.share_num)
    private TextView mShareNum;
    @ViewInject(R.id.collect_num)
    private TextView mCollectNum;

    @ViewInject(R.id.cycleView)
    SubjectImageCycleView imageCycleView;

    @ViewInject(R.id.video_detail)
    private AutoRelativeLayout mVideoDetail;


    @ViewInject(R.id.list_area_images_linear)
    private AutoLinearLayout mListLinear;

    @ViewInject(R.id.horScroll)
    private HorizontalScrollView mHorScroll;

    @ViewInject(R.id.comment_total_num)
    private TextView mCommentTotalNum;
    @ViewInject(R.id.comment_btn_send)
    private TextView mCommentSend;

    @ViewInject(R.id.guide_comment_total_num)
    private TextView mGuideCommentTotalNum;

    @ViewInject(R.id.user_header)
    private ImageView mUserHeader;

    @ViewInject(R.id.comment_et)
    private EditText mCommentEt;

    @ViewInject(R.id.parise_txt)
    private TextView mPariseTxt;

    @ViewInject(R.id.collect_txt)
    private TextView mCollectTxt;

    @ViewInject(R.id.screen_img)
    private ImageView mScreen;

    @ViewInject(R.id.current)
    private AutoLinearLayout mCurrentLayout;

    @ViewInject(R.id.history)
    private LinearLayout mHistoryLayout;

    @ViewInject(R.id.inner_listview)
    private InSideListView mInnerListview;

    @ViewInject(R.id.recyclerView)
    private InSideGridView recyclerView;

    @ViewInject(R.id.inner_comment_layout)
    private AutoLinearLayout mInnerLayout;

    @ViewInject(R.id.chellange_area_linear)
    private AutoLinearLayout mChallengeLayout;

    @ViewInject(R.id.list_area_no_data)
    private TextView mListAraeNoData;

    @ViewInject(R.id.challenge_area_no_data)
    private TextView mChallAraeNoData;

    @ViewInject(R.id.comment_area_no_data)
    private TextView mCommentAraeNoData;

    @ViewInject(R.id.final_pk_no_data)
    private TextView mFinalPkNoData;

    @ViewInject(R.id.final_linear)
    private LinearLayout mFinalPkLinear;

    @ViewInject(R.id.chellange_Txt)
    private TextView mChallTxt;

    @ViewInject(R.id.scrollview)
    private ScrollView mScroll;

    @ViewInject(R.id.guide_left_linear)
    private RelativeLayout mLeftLinear;

    @ViewInject(R.id.guide_right_linear)
    private RelativeLayout mRightLinear;

    @ViewInject(R.id.video_mengban)
    private RelativeLayout mVideoMengban;

    @ViewInject(R.id.video_mengban_img)
    private ImageView mVideoMengbanImg;

    @ViewInject(R.id.top_play_num)
    private TextView topPlayNum;

    @ViewInject(R.id.subject_into_goods_tv)
    private TextView tvIntoGoods;

    private InnerCommentAdapter mInnerAdapter;

    private List<ContentComments> innerCmmentList = new ArrayList<ContentComments>();

    // 屏幕宽高
    private float width;
    private float height;
    // 视频播放时间
    private int playTime;
    private String videoUrl, videoName;

    // 自动隐藏顶部和底部View的时间
    private static final int HIDE_TIME = 5000;

    // 声音调节Toast
    private VolumnController volumnController;

    private AudioManager mAudioManager;

    // 原始屏幕亮度
    private int orginalLight;

    public static final String SUBJECTID = "SUBJECTID";

    private boolean flag = false;

    private int curPos;

    private String subjectId;

    private static final int SUBJECT_DETAIL_REQUEST = 0;
    private static final int SUBJECT_PRAISE_REQUEST = 1;
    private static final int SUBJECT_COMMENT_REQUEST = 2;
    private static final int SUBJECT_COLLECT_REQUEST = 3;
    private static final int SHARE_REQUEST = 4;
    private static final int CHALLENGE_SEND_REQUEST = 5;

    private final static int LOGIN_REQUEST_CODE = 0;
    private final static int REWARD_REQUEST_CODE = 1;
    private final static int COMMENT_REQUEST_CODE = 2;

    private int videoWidth;
    private int videoHeight;

    ArrayList<SubjectResponse.SubjectDetailListDetail> mChallengeList;

    private boolean isCollection, isPraise;

    String headUrl, nickName;

    boolean isLandScape;

    /**
     * 装在数据的集合  文字描述
     */
    ArrayList<String> imageDescList;
    /**
     * 装在数据的集合  图片地址
     */
    ArrayList<String> urlList;

    ArrayList<SubjectResponse.SubjectDetailListDetail> advList;

    private CharSequence temp;//监听前的文本
    private int editStart;//光标开始位置
    private int editEnd;//光标结束位置

    protected GestureDetector mGestureDetector;

    private boolean isLeft=false,isRight=false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_detail_layout);
        ViewUtils.inject(this);
        subjectId = getIntent().getStringExtra(SUBJECTID);
        innerListview();
        initView();
        initVideo();
        isLoginBack = false;
        requestData();
        startGesture();
        flag = true;

        mScrollHandler.sendEmptyMessageDelayed(2, 50);
        registerReceiverForUpdate();
    }

    private void registerReceiverForUpdate() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("send_challenge_success");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if("send_challenge_success".equals(intent.getAction())){
                isLoginBack = true;
                flag = true;
                requestData();
            }
        }
    };

    @Override
    protected void onResume()
    {
        super.onResume();

        mVideoMengban.setVisibility(View.VISIBLE);
        if (!flag)
        {
//            mLoading.setVisibility(View.VISIBLE);
//            mVideo.seekTo(curPos);
        }

        Glide.with(this)
                .load(headUrl)
                .centerCrop()
                .placeholder(R.drawable.default_header)
                .error(R.drawable.default_header)
                .transform(new GlideCircleTransform(this))
                .into(mUserHeader);
        if (getUserInfo().getUserType() == 1) {
            float scale = getResources().getDisplayMetrics().density;
            ViewGroup.LayoutParams layoutParams = mUserHeaderBg.getLayoutParams();
            layoutParams.height = (int) (38 * scale + 0.5f);
            layoutParams.width = (int) (38 * scale + 0.5f);
            mUserHeaderBg.setLayoutParams(layoutParams);
            mUserHeaderBg.setImageDrawable(getResources().getDrawable(R.drawable.icon_star_bg));
        }
        if (getUserInfo().getMemberStatus() == 1) {
            mUserHeaderVip.setVisibility(View.VISIBLE);
        }else{
            mUserHeaderVip.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        SaveBehaviourDataService.startAction(this, BehaviourEnum.SUBJECT_DETAIL.getCode() + "00", request, HeaderRequest.SUBJECT_DETAIL);
    }


    private void initVideo()
    {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        width = DensityUtil.getWidthInPx(this);
        height = DensityUtil.getHeightInPx(this);
        threshold = DensityUtil.dip2px(this, 18);
        orginalLight = LightnessController.getLightness(this);
        mPlayLinear.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mVideo.setOnTouchListener(mTouchListener);
        mlayout.setOnTouchListener(mTouchListener);
        landToPor();
    }

    private boolean isScroll = false;

    private void initView()
    {
        mChallTxt.setText("挑战TA");
        mLeftLinear.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });
        mRightLinear.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });
//        float scale = getResources().getDisplayMetrics().density;
//        int width1 = (int) (44 * scale + 0.5f);
//        int width2 = (int) (55 * scale + 0.5f);
//        int width3 = (int) (45/3 * scale + 0.5f);
//        RelativeLayout.LayoutParams param2 = new RelativeLayout.LayoutParams(width1, width1);
//        LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(width2, width2);
//        RelativeLayout.LayoutParams param4 = new RelativeLayout.LayoutParams(width3, width3);
//        param2.addRule(RelativeLayout.CENTER_IN_PARENT);
//        param3.gravity = Gravity.CENTER;
//        param4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        param4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        param4.setMargins(0,0,5,5);
//        mHeader.setLayoutParams(param2);
//        mHeaderVip.setLayoutParams(param4);
//        mHeaderLayout.setLayoutParams(param3);
        mHeaderLayout.setBackgroundResource(R.drawable.icon_star_bg);
        headUrl = getUserInfo().getImgUrl();
        nickName = getUserInfo().getNickName();

        advList = new ArrayList<SubjectResponse.SubjectDetailListDetail>();
        mCommentEt.setOnEditorActionListener(this);
        mCommentEt.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                editStart = mCommentEt.getSelectionStart();
                editEnd = mCommentEt.getSelectionEnd();
                if (temp.length() > 512)
                {
                    ToastUtil.showShort(SpecialDetailActivity.this, getString(R.string.comment_length_tips));
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    mCommentEt.setText(s);
                    mCommentEt.setSelection(tempSelection);
                }

                if (temp.length() > 0) {
                    mCommentSend.setVisibility(View.VISIBLE);
                } else {
                    mCommentSend.setVisibility(View.GONE);
                }
            }
        });

        mHorScroll.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (topsize > 3)
                {
                    switch (event.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:
                            isScroll = true;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            isScroll = true;
                            break;
                        case MotionEvent.ACTION_UP:
                            mScrollHandler.sendEmptyMessageDelayed(1, 50);
                            break;
                    }
                }
                return false;
            }
        });
        mChallengeList = new ArrayList<SubjectResponse.SubjectDetailListDetail>();

        tvIntoGoods.setOnTouchListener(new View.OnTouchListener() {

            RelativeLayout.LayoutParams params
                    = (RelativeLayout.LayoutParams) tvIntoGoods.getLayoutParams();
            float scale = getResources().getDisplayMetrics().density;
            int marginLeft = (int) (0 - (40 * scale + 0.5f));

            float x = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int left = (int) (marginLeft - (x - event.getX()) / 5);
                        if (left > 0) {
                            left = 0;
                        }
                        params.setMargins(left, 0, 0, 0);
                        break;
                    case MotionEvent.ACTION_UP:
                        params.setMargins(marginLeft, 0, 0, 0);
                        if (event.getEventTime() - event.getDownTime() < 300
                                && (0 == (x-event.getX()))) {
                            showIndex(true);
                        }
                        break;
                }
                tvIntoGoods.setLayoutParams(params);
                return true;
            }
        });
    }

    private Handler mScrollHandler = new Handler()
    {

        @Override
        public void dispatchMessage(Message msg)
        {
            if (1 == msg.what)
            {
                isScroll = false;
            } else if(2 == msg.what) {
                if(!sp_Info.getBoolean(Constant.SUBJECT_FIRST,false)){
//                    mHightLight = new HighLight(SpecialDetailActivity.this)
//                            .autoRemove(false)
//                            .intercept(true)
//                            .addHighLight(R.id.chellange_linear,
//                                    R.layout.subject_guide_challenge_layout,
//                                    new OnBaseCallback() {
//                                        @Override
//                                        public void getPosition(float rightMargin,
//                                                                float bottomMargin,
//                                                                RectF rectF,
//                                                                HighLight.MarginInfo marginInfo) {
//                                            marginInfo.rightMargin = 0;
//                                            marginInfo.topMargin = rectF.top + rectF.height() + offset;
//                                        }
//                                    },
//                                    new CircleLightShape());
//                    mHightLight.show();
                }else{
                    mLeftLinear.setVisibility(View.GONE);
                    mRightLinear.setVisibility(View.GONE);
                }
            }
        }

    };


    private void innerListview()
    {
        mInnerAdapter = new InnerCommentAdapter(this, innerCmmentList);
        mInnerListview.setAdapter(mInnerAdapter);
        recyclerView.setVisibility(View.GONE);
        mInnerListview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                showCommentList();
            }
        });
    }

    private SubjectRequest request;

    private void requestData()
    {
        request = new SubjectRequest();
        request.setSubjectId(subjectId);
        sendNetRequest(request, HeaderRequest.SUBJECT_DETAIL, SUBJECT_DETAIL_REQUEST,
                true);
    }

    private long lastClick;
    @OnClick(value = {R.id.video_mengban_back,R.id.back, R.id.header, R.id.chellange_linear, R.id.reward_linear, R.id.more_challenge
            , R.id.parise_linear, R.id.share_linear, R.id.collect_linear, R.id.screen_btn, R.id.extra,
            R.id.comment_total_num, R.id.inner_comment_layout,R.id.guide_know,R.id.guide_next,
            R.id.guide_comment_total_num,R.id.video_mengban, R.id.guide_challenge_next, R.id.comment_btn_send})
    public void btnClick(View v)
    {
        if (System.currentTimeMillis() - lastClick <= 500) {
            return;
        }
        lastClick = System.currentTimeMillis();

        switch (v.getId())
        {
            case R.id.back:
                if (isLandScape)
                {
                    landToPor();
                } else
                {
                    onBackPressed();
                }
                break;
            case R.id.video_mengban_back:
                onBackPressed();
                break;
            case R.id.header:
                SaveBehaviourDataService.startAction(this, BehaviourEnum.SUBJECT_DETAIL.getCode() + "10");
                if (starId != null && !"".equals(starId))
                {
                    Bundle bund = new Bundle();
                    bund.putString(StarDetailActivity.STAR_ID, starId);
                    launchActivity(StarDetailActivity.class, bund);
                }
                break;
            case R.id.chellange_linear:
                SaveBehaviourDataService.startAction(this, BehaviourEnum.SUBJECT_DETAIL.getCode() + "01");
                if (getLoginStatus())
                {
                    Bundle bundle_c = new Bundle();
                    bundle_c.putString(ChallengeSendActivity.SUBJECT_ID, subjectId);
                    launchActivityForResult(ChallengeSendActivity.class, bundle_c, CHALLENGE_SEND_REQUEST);
                } else
                {
                    startActivityForResult(new Intent(SpecialDetailActivity.this,
                            LoginActivity.class), LOGIN_REQUEST_CODE);
                }

                break;
            case R.id.reward_linear:
                SaveBehaviourDataService.startAction(this, BehaviourEnum.SUBJECT_DETAIL.getCode() + "02");
                if (getLoginStatus())
                {
                    Bundle reward_bun = new Bundle();
                    reward_bun.putString(RewardActivity.PAYEE_NAME, actorName);
                    reward_bun.putString(RewardActivity.PAYEE_HEADER, actorUrl);
                    reward_bun.putString(RewardActivity.CONTENT_ID, contentId);
                    reward_bun.putBoolean(RewardActivity.IS_STAR, true);
                    launchActivityForResult(RewardActivity.class, reward_bun, REWARD_REQUEST_CODE);
                } else
                {
                    startActivityForResult(new Intent(SpecialDetailActivity.this,
                            LoginActivity.class), LOGIN_REQUEST_CODE);
                }

                break;
            case R.id.share_linear:
                SaveBehaviourDataService.startAction(this, BehaviourEnum.SUBJECT_DETAIL.getCode() + "04");
                requestRuntimePermission("为了给您提供更好的服务,甜麦圈需要获取存储器读写权限", Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
            case R.id.more_challenge:
                SaveBehaviourDataService.startAction(this, BehaviourEnum.SUBJECT_DETAIL.getCode() + "12");
                Bundle bundle = new Bundle();
                bundle.putString(ChallengeActivity.SUBJECT_ID, subjectId);
                launchActivity(ChallengeActivity.class, bundle);
                break;
            case R.id.parise_linear:
                SaveBehaviourDataService.startAction(this, BehaviourEnum.SUBJECT_DETAIL.getCode() + "03");
                if (getLoginStatus())
                {
                    pariseRequest();
                } else
                {
                    startActivityForResult(new Intent(SpecialDetailActivity.this,
                            LoginActivity.class), LOGIN_REQUEST_CODE);
                }
                break;

            case R.id.collect_linear:
                SaveBehaviourDataService.startAction(this, BehaviourEnum.SUBJECT_DETAIL.getCode() + "05");
                if (getLoginStatus())
                {
                    collectRequest();
                } else
                {
                    startActivityForResult(new Intent(SpecialDetailActivity.this,
                            LoginActivity.class), LOGIN_REQUEST_CODE);
                }
                break;

            case R.id.screen_btn:
                if (!isLandScape)
                {
                    this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    StatusBarUtil.removeStatusBarView(this);
                    if (videoWidth > videoHeight)
                    {
                        porToLand();
                    } else
                    {
                        porToLarge();
                    }
                } else
                {
                    landToPor();
                }
                break;
            case R.id.extra:
                //详细规则
                SaveBehaviourDataService.startAction(this, BehaviourEnum.SUBJECT_DETAIL.getCode() + "09");
                Bundle bundle_rule = new Bundle();
                bundle_rule.putString(SubjectDetailRuleActivity.SUBJECT_ID, subjectId);
                launchActivity(SubjectDetailRuleActivity.class, bundle_rule);
                break;
            case R.id.guide_comment_total_num:
            case R.id.inner_comment_layout:
            case R.id.comment_total_num:
                showCommentList();
                break;
            case R.id.guide_know:
                isRight=true;
                mRightLinear.setVisibility(View.GONE);
                if(!sp_Info.getBoolean(Constant.SUBJECT_FIRST,false)){
                    sp_Info.edit().putBoolean(Constant.SUBJECT_FIRST,true).commit();
                }
                break;
            case R.id.guide_next:
                isLeft=true;
                mLeftLinear.setVisibility(View.GONE);
                mRightLinear.setVisibility(View.VISIBLE);

                break;
            case R.id.guide_challenge_next:
//                mHightLight.remove();
                mLeftLinear.setVisibility(View.VISIBLE);
                break;
            case R.id.video_mengban:
                mVideoMengban.setVisibility(View.GONE);
                if (!NetUtils.isWifi(this))
                {
                    Dialog dialog = new AlertDialog.Builder(SpecialDetailActivity.this)
                            .setMessage("您正在使用非wifi网络，播放将产生流量费用。")
                            .setNegativeButton(getString(R.string.cancel), null)
                            .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    playVideo(url);
                                    mVideo.start();
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                }else{
                    mVideo.start();
                    mPlay.setImageResource(R.drawable.video_btn_on);
                }
                break;
            case R.id.comment_btn_send:
                // 提交评论
                if (getLoginStatus()) {
                    commentSubmitRequest();
                } else {
                    startActivityForResult(new Intent(this,
                            LoginActivity.class), LOGIN_REQUEST_CODE);
                }
                break;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
        String linkurl;
        if (status == 1) {
            linkurl = RequestUrl.SUBJECT_DETAIL_SHARE_URL + "header=" + HeaderRequest.SUBJECT_DETAIL + "&subjectId=" + subjectId;
        } else {
            linkurl = RequestUrl.SUBJECT_HISTORY_DETAIL_SHARE_URL + "header=" + HeaderRequest.SUBJECT_DETAIL + "&subjectId=" + subjectId;
        }
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo);
        ShareBuilderCommonUtil.Builder builder = new ShareBuilderCommonUtil.Builder(this);
        builder.setTitle(actorName + ",【小伙伴】已围观了" + browseTimes);
        builder.setContent("【" + name + "】" + description);
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
        shareRequest();
    }

    private void porToLand()
    {
        //竖屏转横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        isLandScape = true;
        android.view.ViewGroup.LayoutParams para = mVideo.getLayoutParams();
        para.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        para.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        mVideo.setVideoWidth(para.width);
        mVideo.setVideoHeight(para.height);
        android.view.ViewGroup.LayoutParams para1 = mlayout.getLayoutParams();
        para1.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        para1.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        mlayout.setLayoutParams(para1);
        mVideoDetail.setVisibility(View.GONE);
    }

    private void porToLarge()
    {
        //竖屏扩展
        isLandScape = true;
        android.view.ViewGroup.LayoutParams para = mVideo.getLayoutParams();
        para.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        para.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        mVideo.setVideoWidth(para.width);
        mVideo.setVideoHeight(para.height);
        android.view.ViewGroup.LayoutParams para1 = mlayout.getLayoutParams();
        para1.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        para1.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        mlayout.setLayoutParams(para1);
        mVideoDetail.setVisibility(View.GONE);
    }

    private void landToPor()
    {
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StatusBarUtil.setColor(this, Constant.default_bar_color);

        // 横屏转竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        isLandScape = false;
        android.view.ViewGroup.LayoutParams para = mVideo.getLayoutParams();
        para.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        //para.height = (int) (height * 19 / 60);
        para.height = (int) width * 9 / 16;
        mVideo.setVideoWidth(videoWidth);
        mVideo.setVideoHeight(videoHeight);
        android.view.ViewGroup.LayoutParams para1 = mlayout.getLayoutParams();
        para1.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        //para1.height = (int) (height * 19 / 60) - 25;
        para1.height = (int) width * 9 / 16;
        mlayout.setLayoutParams(para1);
        mVideoMengban.setLayoutParams(para1);
        mBottomView.setVisibility(View.VISIBLE);
        mVideoDetail.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.play_btn_linear:

                if (mVideo.isPlaying())
                {
                    mVideo.pause();
                    mPlay.setImageResource(R.drawable.video_btn_down);
                } else
                {
                    mVideo.start();
                    mPlay.setImageResource(R.drawable.video_btn_on);
                }
                break;
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        LightnessController.setLightness(this, orginalLight);
        flag = false;
        if (url != null && !"".equals(url))
        {
            //playVideo(url+"?t="+curPos);
        }
        if(mVideo.isPlaying()){
            mVideo.pause();
        }
    }

    @Override
    public void onBackPressed()
    {
        if (isLandScape)
        {
            landToPor();
        } else
        {
            SaveBehaviourDataService.startAction(this, BehaviourEnum.SUBJECT_DETAIL.getCode() + "13");
            Intent intent = new Intent();
            intent.putExtra(Constant.COLLECT_STATUS, isCollection ? 1 : 0);
            setResult(RESULT_OK, intent);
            super.onBackPressed();
        }

    }

    @Override
    protected void onStop()
    {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.SUBJECT_DETAIL.getCode() + "xx");
        super.onStop();
    }

    private void pariseRequest()
    {
        PraiseRequest request = new PraiseRequest();
        request.setContentId(contentId);
        request.setPraiseType(isPraise ? 2 : 1);
        sendNetRequest(request, HeaderRequest.SUBJECT_PRAISE, SUBJECT_PRAISE_REQUEST,
                false);

        isPraise = !isPraise;
        Drawable drawable;
        if (isPraise)
        {
            drawable = getResources().getDrawable(R.drawable.icon_parise_pre);
            mPariseTxt.setText(getString(R.string.favored));
            mPariseNum.setText(Integer.parseInt(mPariseNum.getText().toString()) + 1 + "");
        } else
        {
            drawable = getResources().getDrawable(R.drawable.icon_parise);
            mPariseTxt.setText(getString(R.string.favor));
            mPariseNum.setText(Integer.parseInt(mPariseNum.getText().toString()) - 1 + "");
        }
        mPariseNum.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
    }


    private void commentSubmitRequest()
    {
        if (TextUtils.isEmpty(mCommentEt.getText().toString().trim()))
        {
            ToastUtil.showShort(SpecialDetailActivity.this, getString(R.string.comment_empty_tips));
            return;
        }
        CommentSubmitRequest request = new CommentSubmitRequest();
        request.setContentId(contentId);
        request.setOperationType("0");
        request.setContent(mCommentEt.getText().toString());
        sendNetRequest(request, HeaderRequest.SUBJECT_COMMENT_SUBMIT, SUBJECT_COMMENT_REQUEST,
                true);
        SaveBehaviourDataService.startAction(this, BehaviourEnum.SUBJECT_DETAIL.getCode() + "07", request, HeaderRequest.SUBJECT_COMMENT_SUBMIT);
    }

    private void collectRequest()
    {
        CollectRequest request = new CollectRequest();
        request.setContentId(contentId);
        request.setType(0);
        request.setStatus(isCollection ? 0 : 1);
        sendNetRequest(request, HeaderRequest.SUBJECT_COLLECT, SUBJECT_COLLECT_REQUEST,
                false);

        isCollection = !isCollection;
        Drawable drawable;
        if (isCollection)
        {
            drawable = getResources().getDrawable(R.drawable.icon_collect_pre);
            mCollectTxt.setText(getString(R.string.collected));
            mCollectNum.setText(Integer.parseInt(mCollectNum.getText().toString()) + 1 + "");
        } else
        {
            drawable = getResources().getDrawable(R.drawable.icon_collect);
            mCollectTxt.setText(getString(R.string.collect));
            mCollectNum.setText(Integer.parseInt(mCollectNum.getText().toString()) - 1 + "");
        }
        mCollectNum.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
    }

    public void shareRequest()
    {
        ShareRequest request = new ShareRequest();
        request.setContentId(contentId);
        sendNetRequest(request, HeaderRequest.SHARE, SHARE_REQUEST,
                false);
    }


    private void startGesture()
    {
        mGestureDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener()
                {
                    // 当手指在屏幕上滑动的时候调用的方法
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2,
                                           float velocityX, float velocityY)
                    {
                        float distansLeftX = e1.getX() - e2.getX();
                        float distansY = Math.abs(e2.getY() - e1.getY());
                        float distansRightX = e2.getX() - e1.getX();
                        if (distansLeftX > Constant.FLING_MIN_DISTANCE && distansLeftX / distansY > 1
                                && Math.abs(velocityX) > Constant.FLING_MIN_VELOCITY)
                        {
                            // Fling left
                            //showCommentList();

                        } else if (distansRightX > Constant.FLING_MIN_DISTANCE
                                && distansRightX / distansY > 1
                                && Math.abs(velocityX) > Constant.FLING_MIN_VELOCITY
                                && !isLandScape
                                && e1.getY() > mlayout.getHeight() + 10)
                        {
                            // Fling right
                            showIndex(false);
                        }
                        return false;
                    }

                });
    }

    private void showCommentList()
    {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.SUBJECT_DETAIL.getCode() + "08");
        Bundle com_bun = new Bundle();
        com_bun.putString(CommentActivity.CONTENTID, contentId);
        com_bun.putString(CommentActivity.SUBJECTID, subjectId);
        launchActivityForResult(CommentActivity.class, com_bun,COMMENT_REQUEST_CODE);
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
        if(!sp_Info.getBoolean(Constant.SUBJECT_FIRST,false)){
            sp_Info.edit().putBoolean(Constant.SUBJECT_FIRST,true).apply();
            isRight=true;
            mRightLinear.setVisibility(View.GONE);
        }
    }

    public void showIndex(boolean fromBtn)
    {
        String code = "11";
        if (fromBtn) {
            code = "14";
        }
        SaveBehaviourDataService.startAction(this, BehaviourEnum.SUBJECT_DETAIL.getCode() + code);
        sp_Info.edit().putString("subjectId",subjectId).apply();
        Intent intent_video = new Intent(SpecialDetailActivity.this, H5WebActivity.class);
        intent_video.putExtra(H5WebActivity.URL, "file:///android_asset/www/more.html");
        startActivity(intent_video);
        overridePendingTransition(R.anim.first_left_in, R.anim.first_left_out);

        if(!sp_Info.getBoolean(Constant.SUBJECT_FIRST,false)){
            isLeft=true;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        if (isScroll)
        {
            //mHorScroll.onTouchEvent(ev);
        } else
        {
            if (ev != null && mGestureDetector != null)
            {
                if(!sp_Info.getBoolean(Constant.SUBJECT_FIRST,false)&&isLeft&&!isRight){

                }else{
                    mGestureDetector.onTouchEvent(ev);
                }

            }
        }

        return super.dispatchTouchEvent(ev);
    }


    @Override
    public void onSuccess(String response, String url, int actionId)
    {
        super.onSuccess(response, url, actionId);
        switch (actionId)
        {
            case SUBJECT_DETAIL_REQUEST:
                SubjectResponse res = JsonUtils.fromJson(response,
                        SubjectResponse.class);
                if (COMMON_SUCCESS.equals(res.getCode()))
                {
                    showView(res);
                } else
                {
                    ToastUtil.showShort(this, res.getMsg());
                }
                break;
            case SUBJECT_PRAISE_REQUEST:
                BaseResponse praiseRes = JsonUtils.fromJson(response,
                        BaseResponse.class);
                if (COMMON_SUCCESS.equals(praiseRes.getCode()))
                {

                } else
                {
                    ToastUtil.showShort(this, praiseRes.getMsg());
                }
                break;
            case SUBJECT_COLLECT_REQUEST:
                BaseResponse collectRes = JsonUtils.fromJson(response,
                        BaseResponse.class);
                if (COMMON_SUCCESS.equals(collectRes.getCode()))
                {

                } else
                {
                    ToastUtil.showShort(this, collectRes.getMsg());
                }
                break;
            case SUBJECT_COMMENT_REQUEST:
                BaseResponse commentRes = JsonUtils.fromJson(response,
                        BaseResponse.class);
                if (COMMON_SUCCESS.equals(commentRes.getCode()))
                {
                    isLoginBack = true;
                    commentNum += 1;
                    mCommentTotalNum.setText(commentNum + "条");
                    mCommentEt.setText("");
                    mCommentSend.setVisibility(View.GONE);
                    requestData();
                } else
                {
                    ToastUtil.showShort(SpecialDetailActivity.this, commentRes.getMsg());
                }
                break;
            case SHARE_REQUEST:
                BaseResponse shareRes = JsonUtils.fromJson(response,
                        BaseResponse.class);
                if (COMMON_SUCCESS.equals(shareRes.getCode()))
                {
                    mShareNum.setText(Integer.parseInt(mShareNum.getText().toString()) + 1 + "");
                }
                break;
        }

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
          /*判断是否是“send”键*/
//        if (actionId == EditorInfo.IME_ACTION_SEND)
//        {
        if (actionId == 0 && event.getAction() == 0) {
            /*隐藏软键盘*/
            InputMethodManager imm = (InputMethodManager) v
                    .getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            if (imm.isActive())
            {
                imm.hideSoftInputFromWindow(
                        v.getApplicationWindowToken(), 0);
            }
            // 提交评论
            if (getLoginStatus())
            {
                commentSubmitRequest();
            } else
            {
                startActivityForResult(new Intent(SpecialDetailActivity.this,
                        LoginActivity.class), LOGIN_REQUEST_CODE);
            }
            return true;
        }
        return false;
    }


    String contentId;

    int commentNum, status;
    boolean isLoginBack = false;
    private String starId, actorName, actorUrl,browseTimes,description,name,thumbUrl;

    private void showView(final SubjectResponse res)
    {
        contentId = res.getContentId();
        if (!isLoginBack)
        {
            startPlay(res.getPlayUrl());
        }
        starId = res.getActorId();
        actorUrl = res.getHeadPic();
        thumbUrl=res.getPublicPic();
        Glide.with(this)
                .load(res.getHeadPic())
                .centerCrop()
                .placeholder(R.drawable.default_header)
                .error(R.drawable.default_header)
                .transform(new GlideCircleTransform(this))
                .into(mHeader);
        Glide.with(this)
                .load(res.getPublicPic())
                .placeholder(R.drawable.default_bg)
                .error(R.drawable.default_bg)
                .centerCrop()
                .into(mVideoMengbanImg);
        if(res.getMemberStatus()==1){
           mHeaderVip.setVisibility(View.VISIBLE);
        }else{
           mHeaderVip.setVisibility(View.GONE);
        }
        browseTimes=res.getBrowseTimes() + "次";
        mPlayNum.setText(browseTimes);
        topPlayNum.setText(browseTimes + "播放");
        actorName = CommonUtils.setName(SpecialDetailActivity.this, res.getActorName());
        if (res.getName() != null && !TextUtils.isEmpty(res.getName()))
        {
            name = res.getName();
        } else
        {
            name = "未命名";
        }
        mTitle.setText(name);
        mSubjectTitle.setText(actorName + " | " + name);
        description=res.getDescription();
        mBrief.setText(res.getDescription());
        mExtra.setText("专题规则");
        mExtra.setTextColor(getResources().getColor(R.color.colorPrimary));
        mChallNum.setText(res.getChallengeTimes() < 10000 ? res.getChallengeTimes() + "" : res.getChallengeTimes() / 10000 + "万");
        mRewardNum.setText(res.getRewardTimes() < 10000 ? res.getRewardTimes() + "" : res.getRewardTimes() / 10000 + "万");
        mShareNum.setText(res.getShareTimes() < 10000 ? res.getShareTimes() + "" : res.getShareTimes() / 10000 + "万");
        mCollectNum.setText(res.getCollectTimes() < 10000 ? res.getCollectTimes() + "" : res.getCollectTimes() / 10000 + "万");
        mPariseNum.setText(res.getPraiseTimes() < 10000 ? res.getPraiseTimes() + "" : res.getPraiseTimes() / 10000 + "万");
        Drawable drawable;
        if (res.getCollectionStatus() == 1)
        {
            isCollection = true;
            mCollectTxt.setText(getString(R.string.collected));
            drawable = getResources().getDrawable(R.drawable.icon_collect_pre);
        } else
        {
            isCollection = false;
            drawable = getResources().getDrawable(R.drawable.icon_collect);
            mCollectTxt.setText(getString(R.string.collect));
        }
        mCollectNum.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        if (res.getPraiseStatis() == 1)
        {
            isPraise = true;
            mPariseTxt.setText(getString(R.string.favored));
            drawable = getResources().getDrawable(R.drawable.icon_parise_pre);
        } else
        {
            isPraise = false;
            drawable = getResources().getDrawable(R.drawable.icon_parise);
            mPariseTxt.setText(getString(R.string.favor));
        }
        mPariseNum.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);


            commentNum = (int) res.getCommentTimes();
            mCommentTotalNum.setText(res.getCommentTimes() + "条");
            mGuideCommentTotalNum.setText(res.getCommentTimes() + "条");

        status = res.getStatus();
        if (status == 1)
        {
            //进行中
            mCurrentLayout.setVisibility(View.VISIBLE);
            mHistoryLayout.setVisibility(View.GONE);
            showRanking(res.getRankingList());
        } else
        {
            mCurrentLayout.setVisibility(View.GONE);
            mHistoryLayout.setVisibility(View.VISIBLE);
            showFinalPK(res.getHistoryList());
        }
        mChallengeList.clear();
        if (res.getChallengeList() != null)
        {
            int size = res.getChallengeList().size();
            if (size > 4)
            {
                for (int i = 0; i < 4; i++)
                {
                    mChallengeList.add(res.getChallengeList().get(i));
                }
            } else
            {
                mChallengeList.addAll(res.getChallengeList());
            }
            showChallenge(mChallengeList);
        }
        innerCmmentList.clear();
        if (res.getCurrentComments() != null && res.getCurrentComments().size() > 0)
        {
            mCommentAraeNoData.setVisibility(View.GONE);
            innerCmmentList.addAll(res.getCurrentComments());
        } else
        {
            mCommentAraeNoData.setVisibility(View.VISIBLE);
        }
        mInnerAdapter.notifyDataSetChanged();

        AnimationDrawable animationDrawable
                = (AnimationDrawable) mChallIcon.getDrawable();
        animationDrawable.start();
    }


    private void startPlay(final String url)
    {
        if (!NetUtils.isWifi(this))
        {
            this.url = url;
//            Dialog dialog = new AlertDialog.Builder(SpecialDetailActivity.this)
//                    .setMessage("您正在使用非wifi网络，播放将产生流量费用。")
//                    .setNegativeButton(getString(R.string.cancel), null)
//                    .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener()
//                    {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which)
//                        {
//                            playVideo(url);
//                            dialog.dismiss();
//                        }
//                    }).create();
//            dialog.show();
        } else
        {
            playVideo(url);
        }
    }


    private void showFinalPK(List<SubjectResponse.SubjectDetailListDetail> list)
    {
        advList.clear();
        if (list != null && list.size() > 0) {
            advList.addAll(list);
        }
        imageDescList = new ArrayList<String>();
        urlList = new ArrayList<String>();
        if(advList!=null&&advList.size()>0){
            mFinalPkLinear.setVisibility(View.VISIBLE);
            mFinalPkNoData.setVisibility(View.GONE);
            /*添加数据*/
            for (SubjectResponse.SubjectDetailListDetail adv : advList)
            {
                urlList.add(adv.getImgUrl());
                imageDescList.add(adv.getTitle());
            }
            //请求之后再执行
            initCarsuelView(imageDescList, urlList, null);
        }else{
            mFinalPkLinear.setVisibility(View.GONE);
            mFinalPkNoData.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 初始化轮播图
     */
    public void initCarsuelView(ArrayList<String> imageDescList, ArrayList<String> urlList, ArrayList<Integer> periodList)
    {
        LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        imageCycleView.setLayoutParams(cParams);
        SubjectImageCycleView.ImageCycleViewListener mAdCycleViewListener = new SubjectImageCycleView.ImageCycleViewListener()
        {
            @Override
            public void onImageClick(int position, View imageView)
            {
                /*实现点击事件*/
                Intent intent = new Intent(SpecialDetailActivity.this, FinalPKDetailActivity.class);
                intent.putExtra(FinalPKDetailActivity.CONTENTID, advList.get(position).getContentId());
                intent.putExtra(FinalPKDetailActivity.SUBJECTID, subjectId);
                startActivity(intent);
            }

            @Override
            public void displayImage(String imageURL, ImageView imageView)
            {
                /*在此方法中，显示图片，可以用自己的图片加载库，也可以用Imageloader */
//                ImageLoaderHelper.displayImage(imageURL, ImageLoaderHelper.optionsForCommon, imageView);
                Glide.with(SpecialDetailActivity.this)
                        .load(imageURL)
                        .placeholder(R.drawable.default_bg)
                        .error(R.drawable.default_bg)
                        .centerCrop()
                        .into(imageView);
            }
        };
        /*设置数据*/
        imageCycleView.setImageResources(imageDescList, urlList, mAdCycleViewListener);
        if (urlList.size() > 0)
        {
            imageCycleView.startImageCycle();
        }

    }


    private void showChallenge(final List<SubjectResponse.SubjectDetailListDetail> list)
    {
        float scale = getResources().getDisplayMetrics().density;
        double widt = width / 4 - (10 * scale + 0.5f);
        if (list != null && list.size() > 0)
        {
            mChallengeLayout.removeAllViews();
            mChallAraeNoData.setVisibility(View.GONE);
            for (int i = 0; i < list.size(); i++)
            {
                final int pos = i;
                ImageView img = new ImageView(SpecialDetailActivity.this);
                img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                LinearLayout.LayoutParams param0 = new LinearLayout.LayoutParams((int) widt, (int) widt);
                if (i != 0)
                {
                    param0.leftMargin = 15;
                }
                img.setLayoutParams(param0);
                Glide.with(this)
                        .load(list.get(i).getImgUrl())
                        .placeholder(R.drawable.default_bg)
                        .error(R.drawable.default_bg)
                        .centerCrop()
                        .into(img);
                img.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        //进入挑战详情
                        SaveBehaviourDataService.startAction(SpecialDetailActivity.this, BehaviourEnum.SUBJECT_DETAIL.getCode() + "06");
                        Bundle bundle = new Bundle();
                        bundle.putString(ChallengeDetailActivity.CONTENTID, list.get(pos).getContentId());
                        bundle.putString(ChallengeDetailActivity.SUBJECTID, subjectId);
                        launchActivity(ChallengeDetailActivity.class, bundle);
                    }
                });
                mChallengeLayout.addView(img);
            }
        } else
        {
            RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) widt);
            mChallAraeNoData.setVisibility(View.VISIBLE);
            mChallAraeNoData.setLayoutParams(param1);
            mChallAraeNoData.setText("还没有人挑战TA , 来试试");
        }
    }

    int topsize = 0;

    private void showRanking(final List<SubjectResponse.SubjectDetailListDetail> list)
    {
        mListLinear.removeAllViews();
        double widt = width / 4.3;
        if (list != null && list.size() > 0)
        {
            mListAraeNoData.setVisibility(View.GONE);
            topsize = list.size();
            for (int i = 0; i < list.size(); i++)
            {
                final int pos = i;
                ImageView img = new ImageView(SpecialDetailActivity.this);
                img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                LinearLayout.LayoutParams param0 = new LinearLayout.LayoutParams((int) widt, (int) widt);
                if (i != 0)
                {
                    param0.leftMargin = 20;
                }
                img.setLayoutParams(param0);
                Glide.with(this)
                        .load(list.get(i).getImgUrl())
                        .placeholder(R.drawable.default_bg)
                        .error(R.drawable.default_bg)
                        .centerCrop()
                        .into(img);
                img.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        //进入挑战详情
                        SaveBehaviourDataService.startAction(SpecialDetailActivity.this, BehaviourEnum.SUBJECT_DETAIL.getCode() + "06");
                        Bundle bundle = new Bundle();
                        bundle.putString(ChallengeDetailActivity.CONTENTID, list.get(pos).getContentId());
                        bundle.putString(ChallengeDetailActivity.SUBJECTID, subjectId);
                        launchActivity(ChallengeDetailActivity.class, bundle);
                    }
                });
                mListLinear.addView(img);
            }
        } else
        {
            RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) widt);
            mListAraeNoData.setVisibility(View.VISIBLE);
            mListAraeNoData.setLayoutParams(param1);
        }
    }


    private ABaseGridLayoutManager getGridLayoutManager()
    {
        final ABaseGridLayoutManager layoutManager = new ABaseGridLayoutManager(
                this, 4);
        return layoutManager;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case LOGIN_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK)
                {
                    isLoginBack = true;
                    flag = true;
                    headUrl = getUserInfo().getImgUrl();
                    nickName = getUserInfo().getNickName();
                    requestData();
                }
                break;
            case CHALLENGE_SEND_REQUEST:
            case REWARD_REQUEST_CODE:
            case COMMENT_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK)
                {
                    isLoginBack = true;
                    flag = true;
                    requestData();
                }
                break;
            default:
                UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(String response, Map<String, String> headers, String url, int actionId) {
        super.onSuccess(response, headers, url, actionId);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mHandler.removeMessages(0);
        mHandler.removeCallbacksAndMessages(null);
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
    }

    String url = "";
    boolean isComplete = false;

    private void playVideo(String videoUrl)
    {
        mLoading.setVisibility(View.VISIBLE);
        if (videoUrl == null)
        {
            return;
        }
        Uri uri = Uri.parse(videoUrl);
        mVideo.setVideoURI(uri);
        mVideo.requestFocus();
        mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                mLoading.setVisibility(View.GONE);
                videoWidth = mp.getVideoWidth();
                videoHeight = mp.getVideoHeight();
                if (videoWidth > 0 && videoHeight > 0)
                {
                    mVideo.setVideoWidth(mp.getVideoWidth());
                    mVideo.setVideoHeight(mp.getVideoHeight());
                }
                mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener()
                {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent)
                    {
                        if (mVideo.getCurrentPosition() > 0)
                        {
                            mLoading.setVisibility(View.GONE);
                            mPlayTime.setText(CommonUtils.formatLongToTimeStr(mVideo
                                    .getCurrentPosition()));
                            curPos = mVideo.getCurrentPosition();
                            int progress = mVideo.getCurrentPosition() * 100
                                    / mVideo.getDuration();
                            mSeekBar.setProgress(progress);
                            if (mVideo.getCurrentPosition() > mVideo
                                    .getDuration() - 100)
                            {
                                mPlayTime.setText("00:00:00");
                                mSeekBar.setProgress(0);
                            }
                            mSeekBar.setSecondaryProgress(percent);
                        } else
                        {
                            mPlayTime.setText("00:00:00");
                            mSeekBar.setProgress(0);
                        }
                    }
                });

                //mVideo.start();
                mHandler.removeCallbacks(hideRunnable);
                mHandler.postDelayed(hideRunnable, HIDE_TIME);
                mDurationTime.setText(CommonUtils.formatLongToTimeStr(mVideo.getDuration()));
                Timer timer = new Timer();
                timer.schedule(new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        mHandler.sendEmptyMessage(1);
                    }
                }, 0, 100);
                if (playTime != 0)
                {
                    mVideo.seekTo(playTime);
                }
            }
        });
        mVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                isComplete = true;
                duration = mp.getCurrentPosition();
                mPlay.setImageResource(R.drawable.video_btn_down);
                mPlayTime.setText("00:00:00");
                mSeekBar.setProgress(0);
            }
        });
        mVideo.setPlayPauseListener(new FullScreenVideoView.PlayPauseListener()
        {
            @Override
            public void onPlay()
            {
                mPlay.setImageResource(R.drawable.video_btn_on);
            }

            @Override
            public void onPause()
            {
                mPlay.setImageResource(R.drawable.video_btn_down);
            }
        });
    }


    private long duration;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    try
                    {
                        if (mVideo.getCurrentPosition() > 0)
                        {
                            mLoading.setVisibility(View.GONE);
                            if (!isComplete)
                            {
                                duration = mVideo.getDuration();
                            }
                            mPlayTime.setText(CommonUtils.formatLongToTimeStr(mVideo
                                    .getCurrentPosition()));
                            int progress = mVideo.getCurrentPosition() * 100
                                    / mVideo.getDuration();
                            playTime = mVideo.getCurrentPosition();
                            mSeekBar.setProgress(progress);
                            if (mVideo.getCurrentPosition() > duration - 100)
                            {
                                mPlayTime.setText("00:00:00");
                                mSeekBar.setProgress(0);
                            }
                        } else
                        {
                            mPlayTime.setText("00:00:00");
                            mSeekBar.setProgress(0);
                        }
                    }
                    catch (Exception e)
                    {

                    }
                    break;
                case 2:
                    showOrHide();
                    break;

                default:
                    break;
            }
        }
    };

    private Runnable hideRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            showOrHide();
        }
    };

    private void showOrHide()
    {
        if (mTopView.getVisibility() == View.VISIBLE)
        {
            mTopView.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(this,
                    R.anim.option_leave_from_top);
            animation.setAnimationListener(new AnimationImp()
            {
                @Override
                public void onAnimationEnd(Animation animation)
                {
                    super.onAnimationEnd(animation);
                    mTopView.setVisibility(View.GONE);
                }
            });
            mTopView.startAnimation(animation);

            mBottomView.clearAnimation();
            Animation animation1 = AnimationUtils.loadAnimation(this,
                    R.anim.option_leave_from_bottom);
            animation1.setAnimationListener(new AnimationImp()
            {
                @Override
                public void onAnimationEnd(Animation animation)
                {
                    super.onAnimationEnd(animation);
                    mBottomView.setVisibility(View.GONE);
                }
            });
            mBottomView.startAnimation(animation1);

            mPlayNum.clearAnimation();
            Animation animation2 = AnimationUtils.loadAnimation(this,
                    R.anim.option_leave_from_top);
            animation2.setAnimationListener(new AnimationImp()
            {
                @Override
                public void onAnimationEnd(Animation animation)
                {
                    super.onAnimationEnd(animation);
                    mPlayNum.setVisibility(View.GONE);
                }
            });
            mPlayNum.startAnimation(animation2);
        } else
        {
            mTopView.setVisibility(View.VISIBLE);
            mTopView.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(this,
                    R.anim.option_entry_from_top);
            mTopView.startAnimation(animation);

            mBottomView.setVisibility(View.VISIBLE);
            mBottomView.clearAnimation();
            Animation animation1 = AnimationUtils.loadAnimation(this,
                    R.anim.option_entry_from_bottom);
            mBottomView.startAnimation(animation1);

            mPlayNum.setVisibility(View.VISIBLE);
            mPlayNum.clearAnimation();
            Animation animation2 = AnimationUtils.loadAnimation(this,
                    R.anim.option_entry_from_top);
            mPlayNum.startAnimation(animation2);

            mHandler.removeCallbacks(hideRunnable);
            mHandler.postDelayed(hideRunnable, HIDE_TIME);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            height = DensityUtil.getWidthInPx(this);
            width = DensityUtil.getHeightInPx(this);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            width = DensityUtil.getWidthInPx(this);
            height = DensityUtil.getHeightInPx(this);
        }
        super.onConfigurationChanged(newConfig);
    }

    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener()
    {
        @Override
        public void onStopTrackingTouch(SeekBar seekBar)
        {
            if (mVideo.isPlaying())
            {
                mLoading.setVisibility(View.VISIBLE);
            }
            mHandler.postDelayed(hideRunnable, HIDE_TIME);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar)
        {
            mHandler.removeCallbacks(hideRunnable);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser)
        {
            if (fromUser)
            {
                int time = progress * mVideo.getDuration() / 100;
                playTime = time;
                mVideo.seekTo(time);
                mPlayTime.setText(CommonUtils.formatLongToTimeStr(mVideo
                        .getCurrentPosition()));
            }
        }
    };

    private float mLastMotionX;

    private float mLastMotionY;

    private int startX;

    private int startY;

    private int threshold;

    private boolean isClick = true;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener()
    {

        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            final float x = event.getX();
            final float y = event.getY();

            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    mLastMotionX = x;
                    mLastMotionY = y;
                    startX = (int) x;
                    startY = (int) y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaX = x - mLastMotionX;
                    float deltaY = y - mLastMotionY;
                    float absDeltaX = Math.abs(deltaX);
                    float absDeltaY = Math.abs(deltaY);
                    boolean isAdjustAudio = false;
                    if (absDeltaX > threshold && absDeltaY > threshold)
                    {
                        if (absDeltaX < absDeltaY)
                        {
                            isAdjustAudio = true;
                        } else
                        {
                            isAdjustAudio = false;
                        }
                    } else if (absDeltaX < threshold && absDeltaY > threshold)
                    {
                        isAdjustAudio = true;
                    } else if (absDeltaX > threshold && absDeltaY < threshold)
                    {
                        isAdjustAudio = false;
                    } else
                    {
                        return true;
                    }
                    if (isAdjustAudio)
                    {
                        if (x < width / 2)
                        {
                            if (deltaY > 0)
                            {
                                lightDown(absDeltaY);
                            } else if (deltaY < 0)
                            {
                                lightUp(absDeltaY);
                            }
                        } else
                        {
                            if (deltaY > 0)
                            {
                                volumeDown(absDeltaY);
                            } else if (deltaY < 0)
                            {
                                volumeUp(absDeltaY);
                            }
                        }

                    } else
                    {
                        if (deltaX > 0)
                        {
                            forward(absDeltaX);
                        } else if (deltaX < 0)
                        {
                            backward(absDeltaX);
                        }
                    }
                    mLastMotionX = x;
                    mLastMotionY = y;
                    break;
                case MotionEvent.ACTION_UP:
                    if (Math.abs(x - startX) > threshold
                            || Math.abs(y - startY) > threshold)
                    {
                        isClick = false;
                    }
                    mLastMotionX = 0;
                    mLastMotionY = 0;
                    startX = (int) 0;
                    if (isClick)
                    {
                        showOrHide();
                    }
                    isClick = true;
                    break;

                default:
                    break;
            }
            return true;
        }

    };


    private void backward(float delataX)
    {
        int current = mVideo.getCurrentPosition();
        int backwardTime = (int) (delataX / width * mVideo.getDuration());
        int currentTime = current - backwardTime;
        mVideo.seekTo(currentTime);
        mSeekBar.setProgress(currentTime * 100 / mVideo.getDuration());
        mPlayTime.setText(CommonUtils.formatLongToTimeStr(currentTime));
    }

    private void forward(float delataX)
    {
        int current = mVideo.getCurrentPosition();
        int forwardTime = (int) (delataX / width * mVideo.getDuration());
        int currentTime = current + forwardTime;
        mVideo.seekTo(currentTime);
        mSeekBar.setProgress(currentTime * 100 / mVideo.getDuration());
        mPlayTime.setText(CommonUtils.formatLongToTimeStr(currentTime));
    }

    private void volumeDown(float delatY)
    {
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int down = (int) (delatY / height * max * 3);
        int volume = Math.max(current - down, 0);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        int transformatVolume = volume * 100 / max;
        // volumnController.show(transformatVolume);
    }

    private void volumeUp(float delatY)
    {
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int up = (int) ((delatY / height) * max * 3);
        int volume = Math.min(current + up, max);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        int transformatVolume = volume * 100 / max;
        // volumnController.show(transformatVolume);
    }

    private void lightDown(float delatY)
    {
        int down = (int) ((delatY / height) * 255 * 3);
        int transformatLight = LightnessController.getLightness(this) - down;
        LightnessController.setLightness(this, transformatLight);
    }

    private void lightUp(float delatY)
    {
        int up = (int) ((delatY / height) * 255 * 3);
        int transformatLight = LightnessController.getLightness(this) + up;
        if (transformatLight <= 255)
        {
            LightnessController.setLightness(this, transformatLight);
        }

    }

    private class AnimationImp implements Animation.AnimationListener
    {

        @Override
        public void onAnimationEnd(Animation animation)
        {
        }

        @Override
        public void onAnimationRepeat(Animation animation)
        {
        }

        @Override
        public void onAnimationStart(Animation animation)
        {
        }

    }


}
