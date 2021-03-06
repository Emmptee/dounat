package com.donut.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bis.android.plug.autolayout.AutoLinearLayout;
import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.adapter.CommentAdapter;
import com.donut.app.adapter.HotCommentAdapter;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.customview.InSideListView;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.CommentPraise;
import com.donut.app.http.message.CommentPraiseRequest;
import com.donut.app.http.message.CommentSearchRequest;
import com.donut.app.http.message.CommentSearchResponse;
import com.donut.app.http.message.CommentSubmitRequest;
import com.donut.app.http.message.ContentComments;
import com.donut.app.http.message.SubComments;
import com.donut.app.mvp.subject.special.SubjectSpecialActivity;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.CommonUtils;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends BaseActivity implements CommentAdapter.OnRecyclerViewListener,
        HotCommentAdapter.OnRecyclerViewListener, SwipeRefreshLayout.OnRefreshListener, TextView.OnEditorActionListener {
    @ViewInject(R.id.comment_srl)
    private SwipeRefreshLayout swipeRefreshLayout;

    @ViewInject(R.id.comment_list)
    private RecyclerView recyclerView;

    @ViewInject(R.id.comment_list_new)
    private InSideListView hotRecyclerView;

    @ViewInject(R.id.latest_title)
    private AutoLinearLayout mLatestTitle;

    @ViewInject(R.id.hot_title)
    private AutoLinearLayout mHotTitle;

    @ViewInject(R.id.comment_reply_bottom_layout)
    private AutoLinearLayout mBottomLayout;

    @ViewInject(R.id.comment_reply_btn_send)
    private View mReplyBtn;

    @ViewInject(R.id.comment_reply_et)
    private EditText mCommentEt;

    @ViewInject(R.id.head_right_tv)
    private TextView mRight;

    @ViewInject(R.id.no_data)
    private TextView mNoData;

    private List<ContentComments> commentList = new ArrayList<>();
    private List<ContentComments> hotCommentList = new ArrayList<>();

    private CommentAdapter mAdapter;
    private HotCommentAdapter mHotAdapter;

    private View footerView;

    private View headerView;

    private int page = 0;

    private int rows = 10;

    private static final int COMMENT_REQUEST = 0;

    private static final int COMMENT_PRAISE_REQUEST = 1;

    private static final int COMMENT_REPLY_REQUEST = 2;

    private static final int COMMENT_REQUEST_CODE = 3;

    private static final int EDIT_REQUEST_CODE = 4;

    private static final int LOGIN_REQUEST_CODE = 5;

    boolean isShow, isHotComment = false;

    public static final String CONTENTID = "contentid";

    public static final String SUBJECTID = "subjectid";

    private String contentId, subjectId;

    private CharSequence temp;//监听前的文本
    private int editStart;//光标开始位置
    private int editEnd;//光标结束位置

    private static final int COMMENT_DETAIL_REQUEST_CODE = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        StatusBarUtil.setColor(CommentActivity.this, Constant.default_bar_color);
        footerView = LayoutInflater.from(this)
                .inflate(R.layout.recycleview_footer, null, false);
        headerView = LayoutInflater.from(this)
                .inflate(R.layout.comment_top_layout, null, false);
        ViewUtils.inject(this);
        ViewUtils.inject(this, headerView);
        contentId = getIntent().getStringExtra(CONTENTID);
        subjectId = getIntent().getStringExtra(SUBJECTID);
        updateHeadTitle("全部评论", true);
        mRight.setText("写评论");
        initView();
        requestData(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SaveBehaviourDataService.startAction(this, BehaviourEnum.COMMENT.getCode() + "00", request, HeaderRequest.ALL_COMMENT);
    }

    @Override
    public void onBackPressed() {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.COMMENT.getCode() + "05");
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.COMMENT.getCode() + "xx");
        super.onStop();
    }

    private void initView() {
        mHotAdapter = new HotCommentAdapter(CommentActivity.this, hotCommentList);
        hotRecyclerView.setAdapter(mHotAdapter);
        mHotAdapter.setOnRecyclerViewListener(this);
        mAdapter = new CommentAdapter(CommentActivity.this, commentList, footerView,
                headerView);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(getLayoutManager());
        swipeRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnRecyclerViewListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_tiffany);
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
                    ToastUtil.showShort(CommentActivity.this, getString(R.string.reply_length_tips));
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    mCommentEt.setText(s);
                    mCommentEt.setSelection(tempSelection);
                }
            }
        });
//        mBottomLayout.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener(){
//                    @Override
//                    public void onGlobalLayout()
//                    {
//                        Rect r = new Rect();
//                        mBottomLayout.getWindowVisibleDisplayFrame(r);
//
//                        int screenHeight = mBottomLayout.getRootView()
//                                .getHeight();
//                        int heightDifference = screenHeight
//                                - (r.bottom - r.top);
//                        if (heightDifference > 200)
//                        { // 说明键盘是弹出状态
//                            isShow=true;
//                            mBottomLayout.setVisibility(View.VISIBLE);
//                        } else{
//                            isShow=false;
//                            mBottomLayout.setVisibility(View.GONE);
//                        }
//                    }
//                });

    }

    @OnClick(value = {R.id.menu, R.id.comment_reply_btn_send})
    private void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.menu:

                if (getLoginStatus()) {
                    Bundle bundle = new Bundle();
                    bundle.putString(CommentEditActivity.CONTENTID, contentId);
                    launchActivityForResult(CommentEditActivity.class, bundle, EDIT_REQUEST_CODE);
                } else {
                    launchActivityForResult(LoginActivity.class, LOGIN_REQUEST_CODE);
                }

                break;
            case R.id.comment_reply_btn_send:
                InputMethodManager imm = (InputMethodManager) v
                        .getContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(
                            v.getApplicationWindowToken(), 0);
                }
                // 提交评论
                if (getLoginStatus()) {
                    reply(commentId, beReplyId, beReplyContent);
                } else {
                    startActivityForResult(new Intent(CommentActivity.this,
                            LoginActivity.class), LOGIN_REQUEST_CODE);
                }
                break;
        }
    }

    public void showSpecialDetail() {
        Intent intent = new Intent(CommentActivity.this, SubjectSpecialActivity.class);
        intent.putExtra(SubjectSpecialActivity.SUBJECT_ID, subjectId);
        startActivity(intent);
        overridePendingTransition(R.anim.first_left_in, R.anim.first_left_out);
    }

    CommentSearchRequest request;

    private void requestData(boolean isShowDialog) {
        request = new CommentSearchRequest();
        request.setCurrentUserId(getUserInfo().getUserId());
        request.setPage(page);
        request.setRows(rows);
        request.setSubPage(0);
        request.setSubRows(5);
        request.setContentId(contentId);
        sendNetRequest(request, HeaderRequest.ALL_COMMENT, COMMENT_REQUEST, isShowDialog);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            int[] location = new int[2];
            mReplyBtn.getLocationOnScreen(location);
            int x = (int) (ev.getRawX() - location[0]);
            int y = (int) (ev.getRawY() - location[1]);
            boolean isTouchBtn = x > 0 && x < mReplyBtn.getWidth()
                    && y > 0 && y < mReplyBtn.getHeight();

            View v = getCurrentFocus();
            if (!isTouchBtn && isShouldHideKeyboard(v, ev)) {
                isShow = false;
                mBottomLayout.setVisibility(View.GONE);
                HideKeyboard(v);
            }
        }
        commentFlag = true;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onSuccess(String response, String url, int actionId) {
        super.onSuccess(response, url, actionId);
        switch (actionId) {
            case COMMENT_REQUEST:
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
                    praiseSccess();
                } else {
                    ToastUtil.showShort(CommentActivity.this, praRes.getCode());
                }
                break;
            case COMMENT_REPLY_REQUEST:
                BaseResponse replyRes = JsonUtils.fromJson(response,
                        BaseResponse.class);
                if (COMMON_SUCCESS.equals(replyRes.getCode())) {
                    commentReplySuccess();
                } else {
                    ToastUtil.showShort(CommentActivity.this, replyRes.getCode());
                }
                break;
        }
    }


    private void praiseSccess() {

        if (isHotComment) {
//            hotPraiseSuccess();
            page = 0;
            requestData(true);
        } else {
            if (pos < 10) {
                page = 0;
                requestData(true);
            } else {
                latestPraiseSuccess();
            }
        }
    }


    private void commentReplySuccess() {
        SubComments subNew = new SubComments();
        subNew.setBeRepliedUserName(beReplyName);
        subNew.setBeRepliedUserId(beReplyId);
        subNew.setBeRepliedUserType(beType);
        subNew.setRepliedUserName(CommonUtils.setName(this, getUserInfo().getNickName()));
        subNew.setRepliedUserId(getUserInfo().getUserId());
        subNew.setRepliedUserType(getUserInfo().getUserType() + "");
        subNew.setContent(mCommentEt.getText().toString().trim());
        mCommentEt.setText("");

        if (isHotComment) {
            hotCommentList.get(pos).getSubCommentsList().add(subNew);
            hotCommentList.get(pos).setCommentNum(commentList.get(pos).getCommentNum() + 1);
            hotCommentList.get(pos).setIsReplied("0");
            mHotAdapter.notifyDataSetChanged();
        } else {
            commentList.get(pos).getSubCommentsList().add(subNew);
            commentList.get(pos).setCommentNum(commentList.get(pos).getCommentNum() + 1);
            commentList.get(pos).setIsReplied("0");
            mAdapter.notifyDataSetChanged();
        }

    }

    private void hotPraiseSuccess() {

        CommentPraise praNew = new CommentPraise();
        praNew.setPraisedUserName(CommonUtils.setName(this, getUserInfo().getNickName()));
        praNew.setPraisedUserId(getUserInfo().getUserId());
        if (hotCommentList.get(pos).getIsPraised() != null) {
            if (Integer.parseInt(hotCommentList.get(pos).getIsPraised()) == 0) {
                hotCommentList.get(pos).setIsPraised("1");
                String userId = getUserInfo().getUserId();
                for (int i = 0; i < hotCommentList.get(pos).getPraiseList().size(); i++) {
                    if (userId.equals(hotCommentList.get(pos).getPraiseList().get(i).getPraisedUserId())) {
                        hotCommentList.get(pos).getPraiseList().remove(i);
                    }
                }
            } else {
                hotCommentList.get(pos).setIsPraised("0");
                if (hotCommentList.get(pos).getPraiseList() != null && hotCommentList.get(pos).getPraiseList().size() > 0) {
                    hotCommentList.get(pos).getPraiseList().add(0, praNew);
                } else {
                    hotCommentList.get(pos).getPraiseList().add(praNew);
                }
            }
        } else {
            hotCommentList.get(pos).setIsPraised("0");
            if (hotCommentList.get(pos).getPraiseList() != null && hotCommentList.get(pos).getPraiseList().size() > 0) {
                hotCommentList.get(pos).getPraiseList().add(0, praNew);
            } else {
                hotCommentList.get(pos).getPraiseList().add(praNew);
            }
        }
        mHotAdapter.notifyDataSetChanged();
    }

    private void latestPraiseSuccess() {

        CommentPraise praNew = new CommentPraise();
        praNew.setPraisedUserName(CommonUtils.setName(this, getUserInfo().getNickName()));
        praNew.setPraisedUserId(getUserInfo().getUserId());
        if (commentList.get(pos).getIsPraised() != null && !"".equals(commentList.get(pos).getIsPraised())) {
            if (Integer.parseInt(commentList.get(pos).getIsPraised()) == 0) {
                commentList.get(pos).setIsPraised("1");
                String userId = getUserInfo().getUserId();
                for (int i = 0; i < commentList.get(pos).getPraiseList().size(); i++) {
                    if (userId.equals(commentList.get(pos).getPraiseList().get(i).getPraisedUserId())) {
                        commentList.get(pos).getPraiseList().remove(i);
                    }
                }
            } else {
                commentList.get(pos).setIsPraised("0");
                if (commentList.get(pos).getPraiseList() != null && commentList.get(pos).getPraiseList().size() > 0) {
                    commentList.get(pos).getPraiseList().add(0, praNew);
                } else {
                    commentList.get(pos).getPraiseList().add(praNew);
                }
            }
        } else {
            commentList.get(pos).setIsPraised("0");
            if (commentList.get(pos).getPraiseList() != null && commentList.get(pos).getPraiseList().size() > 0) {
                commentList.get(pos).getPraiseList().add(0, praNew);
            } else {
                commentList.get(pos).getPraiseList().add(praNew);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void showView(CommentSearchResponse commentRes) {
        if (page == 0 && commentRes.getHotComments() != null && commentRes.getHotComments().size() > 0) {
            mHotTitle.setVisibility(View.VISIBLE);
            hotCommentList.clear();
            hotCommentList.addAll(commentRes.getHotComments());
            mHotAdapter.notifyDataSetChanged();
            InSideListView.setListViewHeightBasedOnChildren(hotRecyclerView);

        } else {
            if (page == 0) {
                mHotTitle.setVisibility(View.GONE);
            }

        }
        if (commentRes.getCommentsList() != null && commentRes.getCommentsList().size() > 0) {
            mNoData.setVisibility(View.GONE);
            if (page == 0) {
                mLatestTitle.setVisibility(View.VISIBLE);
                footerView.setVisibility(View.GONE);
                commentList.clear();
                swipeRefreshLayout.setRefreshing(false);
            } else {
                if (commentRes.getCommentsList().size() >= rows) {
                    footerView.setVisibility(View.GONE);
                } else {
                    footerView.setVisibility(View.VISIBLE);
                    mAdapter.setNoMoreData(true);
                }
            }
            page++;
            commentList.addAll(commentRes.getCommentsList());
        } else {
            if (page == 0) {
                mLatestTitle.setVisibility(View.GONE);
                mNoData.setVisibility(View.VISIBLE);
                footerView.setVisibility(View.GONE);
            } else {
                mNoData.setVisibility(View.GONE);
                footerView.setVisibility(View.VISIBLE);
                mAdapter.setNoMoreData(true);
            }

        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        HideKeyboard(mCommentEt);
        super.onPause();
    }

    boolean isTop;

    private ABaseLinearLayoutManager getLayoutManager() {
        final ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(
                CommentActivity.this);
        layoutManager.setOnRecyclerViewScrollLocationListener(recyclerView,
                new OnRecyclerViewScrollLocationListener() {
                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView) {
                        isTop = true;
                    }

                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                        if (!isTop) {
                            requestData(false);
                            footerView.setVisibility(View.VISIBLE);
                        }
                        isTop = false;

                    }
                });
        return layoutManager;
    }


    @Override
    public void onItemClick(String uuid) {
        if (isShow) {
            mCommentEt.setFocusable(false);
            HideKeyboard(mCommentEt);
        } else {
            SaveBehaviourDataService.startAction(this, BehaviourEnum.COMMENT.getCode() + "02");
            Bundle bundle = new Bundle();
            bundle.putString(CommentDetailActivity.COMMENTID, uuid);
            bundle.putString(CommentDetailActivity.CONTENTID, contentId);
            launchActivityForResult(CommentDetailActivity.class, bundle, COMMENT_DETAIL_REQUEST_CODE);
        }
    }

    @Override
    public void onFavor(int pos) {
        if (getLoginStatus()) {
            isHotComment = false;
            this.pos = pos;
            CommentPraiseRequest request = new CommentPraiseRequest();
            request.setCommentId(commentList.get(pos).getCommentId());
            //0点赞 1取消
            if (commentList.get(pos).getIsPraised() != null && !"".equals(commentList.get(pos).getIsPraised())) {
                if (Integer.parseInt(commentList.get(pos).getIsPraised()) == 0) {
                    request.setOperationType("1");
                } else {
                    request.setOperationType("0");
                }
            } else {
                request.setOperationType("0");
            }
            sendNetRequest(request, HeaderRequest.COMMENT_PRAISE, COMMENT_PRAISE_REQUEST);
            SaveBehaviourDataService.startAction(this, BehaviourEnum.COMMENT.getCode() + "01", request, HeaderRequest.COMMENT_PRAISE);
        } else {
            startActivityForResult(new Intent(CommentActivity.this,
                    LoginActivity.class), LOGIN_REQUEST_CODE);
        }
    }

    String commentId, beReplyId, beReplyName, beType, beReplyContent;
    int pos, subPos;

    @Override
    public void onComment(int pos, int subPos) {
        isShow = true;
        mBottomLayout.setVisibility(View.VISIBLE);

        isHotComment = false;
        mCommentEt.setFocusable(true);
        mCommentEt.setFocusableInTouchMode(true);
        mCommentEt.requestFocus();
        ShowKeyboard(mCommentEt);
        this.pos = pos;
        this.subPos = subPos;
        commentId = commentList.get(pos).getCommentId();
        if (subPos > -1) {
            this.beReplyId = commentList.get(pos).getSubCommentsList().get(subPos).getRepliedUserId();
            this.beReplyName = CommonUtils.setName(this, commentList.get(pos).getSubCommentsList().get(subPos).getRepliedUserName());
            this.beType = commentList.get(pos).getSubCommentsList().get(subPos).getRepliedUserType();
            this.beReplyContent = commentList.get(pos).getSubCommentsList().get(subPos).getContent();
            mCommentEt.setHint("回复" + beReplyName + ":");
        } else {
            this.beReplyId = null;
            this.beType = commentList.get(pos).getUserType();
            this.beReplyContent = commentList.get(pos).getContent();
            mCommentEt.setHint("回复" + CommonUtils.setName(this, commentList.get(pos).getCommentatorName()) + ":");
        }

    }

    @Override
    public void onHotComment(int pos, int subPos) {
        isShow = true;
        mBottomLayout.setVisibility(View.VISIBLE);

        isHotComment = true;
        mCommentEt.setFocusable(true);
        mCommentEt.setFocusableInTouchMode(true);
        mCommentEt.requestFocus();
        ShowKeyboard(mCommentEt);
        this.pos = pos;
        this.subPos = subPos;
        commentId = hotCommentList.get(pos).getCommentId();
        if (subPos > -1) {
            this.beReplyId = hotCommentList.get(pos).getSubCommentsList().get(subPos).getRepliedUserId();
            this.beReplyName = CommonUtils.setName(this, hotCommentList.get(pos).getSubCommentsList().get(subPos).getRepliedUserName());
            this.beType = hotCommentList.get(pos).getSubCommentsList().get(subPos).getRepliedUserType();
            this.beReplyContent = hotCommentList.get(pos).getSubCommentsList().get(subPos).getContent();
            mCommentEt.setHint("回复" + beReplyName + ":");
        } else {
            this.beReplyId = null;
            this.beType = hotCommentList.get(pos).getUserType();
            this.beReplyContent = hotCommentList.get(pos).getContent();
            mCommentEt.setHint("回复" + CommonUtils.setName(this, hotCommentList.get(pos).getCommentatorName()) + ":");
        }
    }


    @Override
    public void onHotFavor(int pos) {
        if (getLoginStatus()) {
            isHotComment = true;
            this.pos = pos;
            CommentPraiseRequest request = new CommentPraiseRequest();
            request.setCommentId(hotCommentList.get(pos).getCommentId());
            //0点赞 1取消
            if (hotCommentList.get(pos).getIsPraised() != null && !"".equals(hotCommentList.get(pos).getIsPraised())) {
                if (Integer.parseInt(hotCommentList.get(pos).getIsPraised()) == 0) {
                    request.setOperationType("1");
                } else {
                    request.setOperationType("0");
                }
            } else {
                request.setOperationType("0");
            }
            sendNetRequest(request, HeaderRequest.COMMENT_PRAISE, COMMENT_PRAISE_REQUEST);
            SaveBehaviourDataService.startAction(this, BehaviourEnum.COMMENT.getCode() + "01", request, HeaderRequest.COMMENT_PRAISE);
        } else {
            startActivityForResult(new Intent(CommentActivity.this,
                    LoginActivity.class), LOGIN_REQUEST_CODE);
        }

    }

    @Override
    public void onHotItemClick(String uuid) {
        if (isShow) {
            mCommentEt.setFocusable(false);
            HideKeyboard(mCommentEt);
        } else {
            //跳评论详情
            SaveBehaviourDataService.startAction(this, BehaviourEnum.COMMENT.getCode() + "02");
            Bundle bundle = new Bundle();
            bundle.putString(CommentDetailActivity.COMMENTID, uuid);
            bundle.putString(CommentDetailActivity.CONTENTID, contentId);
            launchActivityForResult(CommentDetailActivity.class, bundle, COMMENT_DETAIL_REQUEST_CODE);
        }
    }


    private void reply(String commentId, String beRepliedId, String beReplyContent) {
        if (TextUtils.isEmpty(mCommentEt.getText().toString().trim())) {
            ToastUtil.showShort(CommentActivity.this, getString(R.string.reply_empty_tips));
            return;
        }
        CommentSubmitRequest request = new CommentSubmitRequest();
        request.setUserContent(beReplyContent);
        request.setContentId(contentId);
        request.setCommentId(commentId);
        request.setOperationType("1");
        request.setBeRepliedId(beRepliedId);
        request.setContent(mCommentEt.getText().toString());
        sendNetRequest(request, HeaderRequest.SUBJECT_COMMENT_SUBMIT, COMMENT_REPLY_REQUEST);
        if (beRepliedId != null && !"".equals(beRepliedId.trim())) {
            SaveBehaviourDataService.startAction(this, BehaviourEnum.COMMENT.getCode() + "04", request, HeaderRequest.SUBJECT_COMMENT_SUBMIT);
        } else {
            SaveBehaviourDataService.startAction(this, BehaviourEnum.COMMENT.getCode() + "03", request, HeaderRequest.SUBJECT_COMMENT_SUBMIT);
        }
        mBottomLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
          /*判断是否是“send”键*/
//        if (actionId == EditorInfo.IME_ACTION_SEND)
//        {
        if (actionId == 0 && event.getAction() == 0) {
            /*隐藏软键盘*/
            InputMethodManager imm = (InputMethodManager) v
                    .getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(
                        v.getApplicationWindowToken(), 0);
            }
            // 提交评论
            if (getLoginStatus()) {
                reply(commentId, beReplyId, beReplyContent);
            } else {
                startActivityForResult(new Intent(CommentActivity.this,
                        LoginActivity.class), LOGIN_REQUEST_CODE);
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case COMMENT_DETAIL_REQUEST_CODE:
            case LOGIN_REQUEST_CODE:
            case EDIT_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    page = 0;
                    requestData(true);
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        page = 0;
        requestData(false);
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
