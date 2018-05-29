package com.donut.app.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bis.android.plug.autolayout.AutoLinearLayout;
import com.bis.android.plug.autolayout.AutoRelativeLayout;
import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.adapter.PraiseListAdapter;
import com.donut.app.adapter.ReplyListAdapter;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.CommentDetailRequest;
import com.donut.app.http.message.CommentPraise;
import com.donut.app.http.message.CommentPraiseRequest;
import com.donut.app.http.message.CommentSearchResponse;
import com.donut.app.http.message.CommentSubmitRequest;
import com.donut.app.http.message.ContentComments;
import com.donut.app.http.message.SubComments;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.CommonUtils;
import com.donut.app.utils.GlideCircleTransform;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

public class CommentDetailActivity extends BaseActivity implements TextView.OnEditorActionListener,
        ReplyListAdapter.OnRecyclerViewListener {
    @ViewInject(R.id.swipe_layout)
    private SwipeRefreshLayout myRefreshView;

    @ViewInject(R.id.recyclerView)
    private RecyclerView mRecycleview;

    @ViewInject(R.id.tab_parise)
    private TextView mPariseTxt;

    @ViewInject(R.id.tab_reply)
    private TextView mReplyTxt;

    @ViewInject(R.id.parise_tri)
    private TextView mPariseTri;

    @ViewInject(R.id.reply_tri)
    private TextView mReplyTri;

    @ViewInject(R.id.comment_detail_header_bg)
    private AutoRelativeLayout mHeaderBg;

    @ViewInject(R.id.comment_detail_header)
    private ImageView mHeader;

    @ViewInject(R.id.comment_detail_header_v)
    private ImageView mHeaderVip;

    @ViewInject(R.id.user_header_v)
    private ImageView mUserHeaderVip;

    @ViewInject(R.id.comment_detail_name)
    private TextView mName;

    @ViewInject(R.id.comment_detail_money)
    private TextView mMoney;

    @ViewInject(R.id.comment_detail_creattime)
    private TextView mTime;

    @ViewInject(R.id.comment_detail_content)
    private TextView mContent;

    @ViewInject(R.id.comment_detail_city)
    private TextView mCity;

    @ViewInject(R.id.comment_detail_favor_num)
    private TextView mFavorNum;

    @ViewInject(R.id.comment_detail_num)
    private TextView mCommentNum;

    @ViewInject(R.id.comment_et)
    private EditText mCommentEt;

    @ViewInject(R.id.comment_btn_send)
    private View mReplyBtn;

    @ViewInject(R.id.comment_layout)
    private AutoLinearLayout mBottomLayout;

    @ViewInject(R.id.comment_total_num)
    private TextView mTotal;

    @ViewInject(R.id.user_header)
    private ImageView mUserHeader;

    @ViewInject(R.id.bottom_user_header_bg)
    private ImageView mUserHeaderBg;

    @ViewInject(R.id.no_data)
    private TextView mNoData;

    public static final String COMMENTID = "commentid";

    public static final String CONTENTID = "contentid";

    public static final String ABOUTME = "aboutme";

    private View mFooterView;

    private View mTopView;

    private PraiseListAdapter mPariseAdapter;

    private ReplyListAdapter mReplyAdapter;

    private List<CommentPraise> praiseList = new ArrayList<CommentPraise>();

    private List<SubComments> replyList = new ArrayList<SubComments>();

    private int praisePage = 0;

    private int praiseRows = 20;

    private int subCommentPage = 0;

    private int subCommentRows = 20;

    private boolean isPraise = true;

    private String commentid, contentid;

    private static final int COMMENT_DETAIL_REQUEST = 0;

    private static final int COMMENT_PRAISE_REQUEST = 1;

    private static final int COMMENT_REPLY_REQUEST = 2;

    private static final int LOGIN_REQUEST_CODE = 5;

    private ContentComments comment;

    private CharSequence temp;//监听前的文本
    private int editStart;//光标开始位置
    private int editEnd;//光标结束位置

    boolean isShow = false;

    boolean isAboutMe = false;

    boolean isPressReply = false;

    private String commentorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_detail_layout);
        StatusBarUtil.setColor(CommentDetailActivity.this, Constant.default_bar_color);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTopView = inflater.inflate(R.layout.activity_comment_detail_top, null);
        mFooterView = inflater.inflate(R.layout.recycleview_footer, null,
                false);
        ViewUtils.inject(this, mTopView);
        ViewUtils.inject(this);
        updateHeadTitle(getString(R.string.comment), true);
        commentid = getIntent().getStringExtra(COMMENTID);
        contentid = getIntent().getStringExtra(CONTENTID);
        isAboutMe = getIntent().getBooleanExtra(ABOUTME, false);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isAboutMe) {
            SaveBehaviourDataService.startAction(this, BehaviourEnum.MY_COMMENT_DETAIL.getCode() + "00");
        } else {
            SaveBehaviourDataService.startAction(this, BehaviourEnum.COMMENT_DETAIL.getCode() + "00");
        }
    }

    private void initView() {
        mTotal.setVisibility(View.GONE);
        mReplyBtn.setVisibility(View.VISIBLE);
        float scale = getResources().getDisplayMetrics().density;
        int width = (int) (28 * scale + 0.5f);
        ViewGroup.LayoutParams param0 = mUserHeader.getLayoutParams();
        param0.height = width + 1 - 1;
        param0.width = width;
        mUserHeader.setLayoutParams(param0);

        if (getUserInfo().getUserType() == 1) {
            ViewGroup.LayoutParams layoutParams = mUserHeaderBg.getLayoutParams();
            layoutParams.height = (int) (38 * scale + 0.5f);
            layoutParams.width = (int) (38 * scale + 0.5f);
            mUserHeaderBg.setLayoutParams(layoutParams);
            mUserHeaderBg.setImageDrawable(getResources().getDrawable(R.drawable.icon_star_bg));
        }

        String headUrl = getUserInfo().getImgUrl();
        Glide.with(this)
                .load(headUrl)
                .centerCrop()
                .placeholder(R.drawable.default_header)
                .error(R.drawable.default_header)
                .transform(new GlideCircleTransform(this))
                .into(mUserHeader);

        if (getUserInfo().getMemberStatus() == 1) {
            mUserHeaderVip.setVisibility(View.VISIBLE);
        } else {
            mUserHeaderVip.setVisibility(View.GONE);
        }
        mBottomLayout.setVisibility(View.VISIBLE);
        myRefreshView.setColorSchemeResources(R.color.refresh_tiffany);
        mRecycleview.setHasFixedSize(true);
        mRecycleview.setLayoutManager(getLayoutManager());
        showReplyList();
        myRefreshView.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (isPraise) {
                            praisePage = 0;
                        } else {
                            subCommentPage = 0;
                        }
                        reuqestData();
                    }
                });

//        mCommentEt.setOnEditorActionListener(this);
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
                    ToastUtil.showShort(CommentDetailActivity.this, getString(R.string.reply_length_tips));
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    mCommentEt.setText(s);
                    mCommentEt.setSelection(tempSelection);
                }
            }
        });

        mTopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideKeyboard(mCommentEt);
            }
        });

    }

    private void showPraiseList() {
        HideKeyboard(mCommentEt);
        praisePage = 0;
        isPraise = true;
        mPariseTxt.setSelected(true);
        mReplyTxt.setSelected(false);
        mPariseTri.setVisibility(View.VISIBLE);
        mReplyTri.setVisibility(View.GONE);
        mPariseAdapter = new PraiseListAdapter(this, praiseList, mTopView, mFooterView);
        mRecycleview.setAdapter(mPariseAdapter);
        reuqestData();
    }

    private void showReplyList() {
        HideKeyboard(mCommentEt);
        subCommentPage = 0;
        isPraise = false;
        mReplyTxt.setSelected(true);
        mPariseTxt.setSelected(false);
        mReplyTri.setVisibility(View.VISIBLE);
        mPariseTri.setVisibility(View.GONE);
        if (isAboutMe && getUserInfo().getUserId() != null) {
            mReplyAdapter = new ReplyListAdapter(this, replyList, mTopView, mFooterView, getUserInfo().getUserId());
        } else {
            mReplyAdapter = new ReplyListAdapter(this, replyList, mTopView, mFooterView, null);
        }
        mReplyAdapter.setOnRecyclerViewListener(this);
        mRecycleview.setAdapter(mReplyAdapter);
        reuqestData();
    }

    private void reuqestData() {
        CommentDetailRequest request = new CommentDetailRequest();
        request.setCommentId(commentid);
        if (getUserInfo().getUserId() != null) {
            request.setCurrentUserId(getUserInfo().getUserId());
        }
        if (isPraise) {
            request.setPraisePage(praisePage);
            request.setPraiseRows(praiseRows);
        } else {
            request.setSubCommentPage(subCommentPage);
            request.setSubCommentRows(subCommentRows);
        }
        sendNetRequest(request, HeaderRequest.COMMENT_DETAIL, COMMENT_DETAIL_REQUEST);
        if (isPraise) {
            if (isAboutMe) {
                SaveBehaviourDataService.startAction(this, BehaviourEnum.MY_COMMENT_DETAIL.getCode() + "03", request, HeaderRequest.COMMENT_DETAIL);
            } else {
                SaveBehaviourDataService.startAction(this, BehaviourEnum.COMMENT_DETAIL.getCode() + "01", request, HeaderRequest.COMMENT_DETAIL);
            }
        }
        if (isPressReply && !isPraise) {
            if (isAboutMe) {
                SaveBehaviourDataService.startAction(this, BehaviourEnum.MY_COMMENT_DETAIL.getCode() + "04", request, HeaderRequest.COMMENT_DETAIL);
            } else {
                SaveBehaviourDataService.startAction(this, BehaviourEnum.COMMENT_DETAIL.getCode() + "02", request, HeaderRequest.COMMENT_DETAIL);
            }

            isPressReply = false;
        }
    }

    @Override
    public void onItemClick(int position) {
        commentReply(position);
    }

    @OnClick(value = {R.id.tab_parise, R.id.tab_reply, R.id.comment_detail_favor_num,
            R.id.comment_detail_num, R.id.comment_detail_header_bg, R.id.comment_btn_send})
    protected void btnClick(View v) {
        switch (v.getId()) {
            case R.id.tab_parise:
                isPressReply = false;
                showPraiseList();
                break;

            case R.id.tab_reply:
                isPressReply = true;
                showReplyList();
                break;
            case R.id.comment_detail_favor_num:
                praiseRequest();
                break;
            case R.id.comment_detail_num:
                commentReply(-1);
                break;
            case R.id.comment_detail_header_bg:
                if (userType == 1) {
                    Intent intent = new Intent(this, StarDetailActivity.class);
                    intent.putExtra(StarDetailActivity.FKA01_ID, commentorId);
                    startActivity(intent);
                }
                break;
            case R.id.comment_btn_send:
                InputMethodManager imm = (InputMethodManager) v
                        .getContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(
                            v.getApplicationWindowToken(), 0);
                }
                // 提交评论
                if (getLoginStatus()) {
                    reply(commentid, beReplyId, beReplyContent);
                } else {
                    startActivityForResult(new Intent(CommentDetailActivity.this,
                            LoginActivity.class), LOGIN_REQUEST_CODE);
                }
                break;
        }
    }

    String beReplyId, beReplyName, beType, beReplyContent;

    private void commentReply(int position) {
        mCommentEt.setFocusable(true);
        mCommentEt.setFocusableInTouchMode(true);
        mCommentEt.requestFocus();
        ShowKeyboard(mCommentEt);
        if (position > -1) {
            this.beReplyId = replyList.get(position).getRepliedUserId();
            this.beReplyName = CommonUtils.setName(this, replyList.get(position).getRepliedUserName());
            this.beType = replyList.get(position).getRepliedUserType();
            this.beReplyContent = replyList.get(position).getContent();
            mCommentEt.setHint("回复" + beReplyName + ":");
        } else {
            this.beReplyId = null;
            this.beType = comment.getUserType();
            this.beReplyContent = comment.getContent();
            mCommentEt.setHint("回复" + CommonUtils.setName(this, comment.getCommentatorName()) + ":");
        }
    }

    private long lastClick;

    private void praiseRequest() {
        if (System.currentTimeMillis() - lastClick <= 1000) {
            return;
        }
        lastClick = System.currentTimeMillis();
        if (getLoginStatus()) {
            CommentPraiseRequest request = new CommentPraiseRequest();
            request.setCommentId(commentid);
            if (comment == null) {
                return;
            }
            //0点赞 1取消
            if (comment.getIsPraised() != null && !"".equals(comment.getIsPraised())) {
                if (Integer.parseInt(comment.getIsPraised()) == 0) {
                    request.setOperationType("1");
                } else {
                    request.setOperationType("0");
                }
            } else {
                request.setOperationType("0");
            }
            sendNetRequest(request, HeaderRequest.COMMENT_PRAISE, COMMENT_PRAISE_REQUEST, false);

            if (isAboutMe) {
                SaveBehaviourDataService.startAction(this, BehaviourEnum.MY_COMMENT_DETAIL.getCode() + "01", request, HeaderRequest.COMMENT_PRAISE);
            } else {
                SaveBehaviourDataService.startAction(this, BehaviourEnum.COMMENT_DETAIL.getCode() + "04", request, HeaderRequest.COMMENT_PRAISE);
            }

        } else {
            startActivityForResult(new Intent(CommentDetailActivity.this,
                    LoginActivity.class), LOGIN_REQUEST_CODE);
        }
    }

    private void reply(String commentId, String beRepliedId, String beReplyContent) {
        if (TextUtils.isEmpty(mCommentEt.getText().toString().trim())) {
            ToastUtil.showShort(CommentDetailActivity.this, getString(R.string.reply_empty_tips));
            return;
        }
        CommentSubmitRequest request = new CommentSubmitRequest();
        request.setUserContent(beReplyContent);
        request.setContentId(contentid);
        request.setCommentId(commentId);
        request.setOperationType("1");
        request.setBeRepliedId(beRepliedId);
        request.setContent(mCommentEt.getText().toString());
        sendNetRequest(request, HeaderRequest.SUBJECT_COMMENT_SUBMIT, COMMENT_REPLY_REQUEST);
        if (beRepliedId == null || "".equals(beRepliedId.trim())) {
            if (isAboutMe) {
                SaveBehaviourDataService.startAction(this, BehaviourEnum.MY_COMMENT_DETAIL.getCode() + "02", request, HeaderRequest.SUBJECT_COMMENT_SUBMIT);
            } else {
                SaveBehaviourDataService.startAction(this, BehaviourEnum.COMMENT_DETAIL.getCode() + "03", request, HeaderRequest.SUBJECT_COMMENT_SUBMIT);
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
          /*判断是否是“send”键*/
//        if (actionId == EditorInfo.IME_ACTION_SEND) {
            /*隐藏软键盘*/
        if (actionId == 0 && event.getAction() == 0) {
            InputMethodManager imm = (InputMethodManager) v
                    .getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(
                        v.getApplicationWindowToken(), 0);
            }
            // 提交评论
            if (getLoginStatus()) {
                reply(commentid, beReplyId, beReplyContent);
            } else {
                startActivityForResult(new Intent(CommentDetailActivity.this,
                        LoginActivity.class), LOGIN_REQUEST_CODE);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (isAboutMe) {
            SaveBehaviourDataService.startAction(this, BehaviourEnum.MY_COMMENT_DETAIL.getCode() + "05");
        } else {
            SaveBehaviourDataService.startAction(this, BehaviourEnum.COMMENT_DETAIL.getCode() + "05");
        }

        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        if (isAboutMe) {
            SaveBehaviourDataService.startAction(this, BehaviourEnum.MY_COMMENT_DETAIL.getCode() + "xx");
        } else {
            SaveBehaviourDataService.startAction(this, BehaviourEnum.COMMENT_DETAIL.getCode() + "xx");
        }
        super.onStop();
    }

    @Override
    public void onSuccess(String response, String url, int actionId) {
        super.onSuccess(response, url, actionId);
        switch (actionId) {
            case COMMENT_DETAIL_REQUEST:
                CommentSearchResponse commentRes = JsonUtils.fromJson(response,
                        CommentSearchResponse.class);
                if (COMMON_SUCCESS.equals(commentRes.getCode())) {
                    showView(commentRes);
                }
                break;
            case COMMENT_PRAISE_REQUEST:
                BaseResponse praRes = JsonUtils.fromJson(response,
                        BaseResponse.class);
                if (COMMON_SUCCESS.equals(praRes.getCode())) {
                    showPraiseList();
                } else {
                    ToastUtil.showShort(CommentDetailActivity.this, praRes.getCode());
                }
                break;
            case COMMENT_REPLY_REQUEST:
                BaseResponse replyRes = JsonUtils.fromJson(response,
                        BaseResponse.class);
                if (COMMON_SUCCESS.equals(replyRes.getCode())) {
                    mCommentEt.setText("");
                    showReplyList();
                } else {
                    ToastUtil.showShort(CommentDetailActivity.this, replyRes.getCode());
                }
                break;
        }
    }

    private void showView(CommentSearchResponse commentRes) {
        comment = commentRes.getContentCommentsDetail();
        if (comment == null) {
            return;
        }
        showTop(comment);
        if (isPraise) {
            showPraiseView(comment.getPraiseList());
        } else {
            showCommentView(comment.getSubCommentsList());
        }

    }

    private int userType;

    private void showTop(ContentComments comment) {
        commentorId = comment.getCommentatorId();
        mPariseTxt.setText("赞 " + comment.getPraiseNum());
        mReplyTxt.setText("回复 " + comment.getCommentNum());
        Glide.with(this)
                .load(comment.getCommentatorUrl())
                .centerCrop()
                .placeholder(R.drawable.default_header)
                .error(R.drawable.default_header)
                .transform(new GlideCircleTransform(this))
                .into(mHeader);
        mName.setText(CommonUtils.setName(this, comment.getCommentatorName()));
        float scale = getResources().getDisplayMetrics().density;
        int width1 = (int) (37 * scale + 0.5f);
        int width2 = (int) (45 * scale + 0.5f);
        RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(width1, width1);
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(width2, width2);
        param1.addRule(RelativeLayout.CENTER_IN_PARENT);
        param2.gravity = Gravity.CENTER;
        mHeaderBg.setLayoutParams(param2);
        mHeader.setLayoutParams(param1);

        int width3 = (int) (45 / 3 * scale + 0.5f);
        RelativeLayout.LayoutParams param4 = new RelativeLayout.LayoutParams(width3, width3);
        param4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        param4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        param4.setMargins(0, 0, 5, 5);
        mHeaderVip.setLayoutParams(param4);
        if (comment.getIsMember() != null && comment.getIsMember() == 1) {
            mHeaderVip.setVisibility(View.VISIBLE);
        } else {
            mHeaderVip.setVisibility(View.GONE);
        }
        if (comment.getUserType() != null && Integer.parseInt(comment.getUserType()) == 1) {
            userType = Integer.parseInt(comment.getUserType());
            mHeaderBg.setBackgroundResource(R.drawable.icon_star_bg);
            mName.setTextColor(getResources().getColor(R.color.gold));

        } else {
            userType = 0;
            mHeaderBg.setBackgroundResource(R.drawable.icon_head_bg);
            mName.setTextColor(getResources().getColor(R.color.text_gray6));
        }
        if (comment.getRewardNum() != null) {
            mMoney.setVisibility(View.VISIBLE);
            mMoney.setText("x" + comment.getRewardNum());
        } else {
            mMoney.setVisibility(View.GONE);
        }

        mTime.setText(comment.getUpdateTime());
        mContent.setText(comment.getContent());
        beReplyContent = comment.getContent();
        Drawable drawable = null;
        if (comment.getIsPraised() != null) {
            if (Integer.parseInt(comment.getIsPraised()) == 0) {
                //赞过
                drawable = getResources().getDrawable(R.drawable.icon_comment_favor_main);
                mFavorNum.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else {
                drawable = getResources().getDrawable(R.drawable.icon_comment_favor_gray);
                mFavorNum.setTextColor(getResources().getColor(R.color.text_gray6));
            }
        } else {
            drawable = getResources().getDrawable(R.drawable.icon_comment_favor_gray);
            mFavorNum.setTextColor(getResources().getColor(R.color.text_gray6));
        }
        mFavorNum.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        if (comment.getPraiseList() != null && comment.getPraiseList().size() > 0) {
            mFavorNum.setText(comment.getPraiseList().size() + "");
        } else {
            mFavorNum.setText("");
        }
        mCity.setText(comment.getAddress());
        Drawable draw = null;
        if (comment.getIsReplied() != null) {
            if (Integer.parseInt(comment.getIsReplied()) == 0) {
                //回复过
                draw = getResources().getDrawable(R.drawable.icon_comment_green);
                mCommentNum.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else {
                draw = getResources().getDrawable(R.drawable.icon_comment_gray);
                mCommentNum.setTextColor(getResources().getColor(R.color.text_gray6));
            }
        } else {
            draw = getResources().getDrawable(R.drawable.icon_comment_gray);
            mCommentNum.setTextColor(getResources().getColor(R.color.text_gray6));
        }
        mCommentNum.setCompoundDrawablesWithIntrinsicBounds(null, null, draw, null);
        if (comment.getCommentNum() > 0) {
            mCommentNum.setText(comment.getCommentNum() + "");
        } else {
            mCommentNum.setText("");
        }
    }

    private void showPraiseView(List<CommentPraise> list) {

        if (list != null && list.size() > 0) {
            mNoData.setVisibility(View.GONE);
            if (praisePage == 0) {
                mFooterView.setVisibility(View.GONE);
                praiseList.clear();
                myRefreshView.setRefreshing(false);
            } else {
                if (praiseList.size() >= praiseRows) {
                    mFooterView.setVisibility(View.GONE);
                } else {
                    mFooterView.setVisibility(View.VISIBLE);
                    mPariseAdapter.setNoMoreData(true);
                }
            }
            praisePage++;
            praiseList.addAll(list);
        } else {
            if (praisePage == 0) {
                praiseList.clear();
                myRefreshView.setRefreshing(false);
                mNoData.setVisibility(View.VISIBLE);
                mNoData.setText("还没有人点赞，抢个沙发");
                mFooterView.setVisibility(View.GONE);
            } else {
                mNoData.setVisibility(View.GONE);
                mFooterView.setVisibility(View.VISIBLE);
                mPariseAdapter.setNoMoreData(true);
            }
        }
        mPariseAdapter.notifyDataSetChanged();
    }

    private void showCommentView(List<SubComments> list) {
        if (list != null && list.size() > 0) {
            mNoData.setVisibility(View.GONE);
            if (subCommentPage == 0) {
                mFooterView.setVisibility(View.GONE);
                replyList.clear();
                myRefreshView.setRefreshing(false);
            } else {
                if (list.size() >= subCommentRows) {
                    mFooterView.setVisibility(View.GONE);
                } else {
                    mFooterView.setVisibility(View.VISIBLE);
                    mReplyAdapter.setNoMoreData(true);
                }
            }
            subCommentPage++;
            replyList.addAll(list);
        } else {
            if (subCommentPage == 0) {
                myRefreshView.setRefreshing(false);
                mNoData.setVisibility(View.VISIBLE);
                mNoData.setText("还没有人回复，抢个沙发");
                mFooterView.setVisibility(View.GONE);
            } else {
                mNoData.setVisibility(View.GONE);
                mFooterView.setVisibility(View.VISIBLE);
                mReplyAdapter.setNoMoreData(true);
            }
        }
        mReplyAdapter.notifyDataSetChanged();
        if (isAboutMe) {
            mReplyAdapter.setMainCommentorId(commentorId);
        }

    }

    private boolean isTop;

    private ABaseLinearLayoutManager getLayoutManager() {
        final ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(
                this);
        layoutManager.setOnRecyclerViewScrollLocationListener(mRecycleview,
                new OnRecyclerViewScrollLocationListener() {
                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView) {
                        isTop = true;
                    }

                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                        if (!isTop) {
                            reuqestData();
                            mFooterView.setVisibility(View.VISIBLE);
                        }
                        isTop = false;
                    }
                });

        return layoutManager;
    }

    //隐藏虚拟键盘
    public void HideKeyboard(View v) {
        isShow = false;
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    //显示虚拟键盘
    public void ShowKeyboard(View v) {
        isShow = true;
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }
}
