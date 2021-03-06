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
import com.donut.app.adapter.CommentAboutMeAdapter;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.CommentPraise;
import com.donut.app.http.message.CommentPraiseRequest;
import com.donut.app.http.message.CommentSearchResponse;
import com.donut.app.http.message.CommentSubmitRequest;
import com.donut.app.http.message.ContentComments;
import com.donut.app.http.message.MyCommentsRequest;
import com.donut.app.http.message.SubComments;
import com.donut.app.mvp.subject.challenge.SubjectChallengeActivity;
import com.donut.app.mvp.subject.finalpk.SubjectFinalPkActivity;
import com.donut.app.mvp.subject.notice.SubjectNoticeActivity;
import com.donut.app.mvp.subject.snap.SubjectSnapActivity;
import com.donut.app.mvp.subject.special.SubjectSpecialActivity;
import com.donut.app.mvp.wish.reply.WishReplyActivity;
import com.donut.app.mvp.wish.user.WishUserActivity;
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

public class CommentAboutMeActivity extends BaseActivity implements CommentAboutMeAdapter.OnRecyclerViewListener,
            SwipeRefreshLayout.OnRefreshListener,TextView.OnEditorActionListener
{

    @ViewInject(R.id.comment_me_srl)
    private SwipeRefreshLayout swipeRefreshLayout;

    @ViewInject(R.id.comment_me_list)
    private RecyclerView recyclerView;

    @ViewInject(R.id.comment_reply_bottom_layout)
    private AutoLinearLayout mBottomLayout;

    @ViewInject(R.id.comment_reply_et)
    private EditText mCommentEt;

    @ViewInject(R.id.comment_reply_btn_send)
    private View mReplyBtn;

    @ViewInject(R.id.no_data)
    private TextView mNoData;

    private List<ContentComments> commentList = new ArrayList<>();

    private View footerView;

    private CommentAboutMeAdapter mAdapter;

    private static final int MY_COMMENT_REQUEST = 0;

    private static final int COMMENT_PRAISE_REQUEST = 1;

    private static final int COMMENT_REPLY_REQUEST = 2;

    private static final int LOGIN_REQUEST_CODE=5;

    private int page = 0;

    private int rows = 10;

    private CharSequence temp;//监听前的文本
    private int editStart;//光标开始位置
    private int editEnd;//光标结束位置

    boolean isShow=false;

    private static final int COMMENT_DETAIL_REQUEST_CODE=6;

    private String contentid;

    private String functionCode="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_about_me);
        StatusBarUtil.setColor(CommentAboutMeActivity.this, Constant.default_bar_color);
        footerView = LayoutInflater.from(this)
                .inflate(R.layout.recycleview_footer, null, false);
        ViewUtils.inject(this);
        updateHeadTitle("关于我", true);
        initView();
        requestData();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        SaveBehaviourDataService.startAction(this, BehaviourEnum.COMMENT_ABOUT_ME.getCode()+"00",request,HeaderRequest.COMMENT_MY);
    }

    private void initView()
    {
        if(getUserInfo().getUserId()!=null){
            mAdapter = new CommentAboutMeAdapter(CommentAboutMeActivity.this, commentList, footerView,getUserInfo().getUserId());
        }else{
            mAdapter = new CommentAboutMeAdapter(CommentAboutMeActivity.this, commentList, footerView,null);
        }
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(getLayoutManager());
        swipeRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnRecyclerViewListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_tiffany);
//        mCommentEt.setOnEditorActionListener(this);
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
                    ToastUtil.showShort(CommentAboutMeActivity.this, getString(R.string.reply_length_tips));
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    mCommentEt.setText(s);
                    mCommentEt.setSelection(tempSelection);
                }
            }
        });
    }

    @OnClick({R.id.comment_reply_btn_send})
    protected void viewOnClick(View v) {
        switch (v.getId()){
            case R.id.comment_reply_btn_send:
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
                    reply(commentId,beReplyId,beReplyContent);
                } else
                {
                    startActivityForResult(new Intent(CommentAboutMeActivity.this,
                            LoginActivity.class), LOGIN_REQUEST_CODE);
                }
                break;
        }
    }

    @Override
    public void onRefresh()
    {
       page=0;
       requestData();
    }
    MyCommentsRequest request;
    private void requestData(){
        request=new MyCommentsRequest();
        request.setPage(page);
        request.setRows(rows);
        request.setSubPage(0);
        request.setSubRows(5);
        sendNetRequest(request, HeaderRequest.COMMENT_MY, MY_COMMENT_REQUEST);
    }

    @Override
    public void onItemClick(String uuid,String contentid)
    {
        if(isShow){
            mBottomLayout.setVisibility(View.GONE);
            mCommentEt.setFocusable(false);
            HideKeyboard(mCommentEt);
        }else{
            SaveBehaviourDataService.startAction(this, BehaviourEnum.COMMENT_ABOUT_ME.getCode()+"01");
            Bundle bundle=new Bundle();
            bundle.putString(CommentDetailActivity.COMMENTID,uuid);
            bundle.putString(CommentDetailActivity.CONTENTID,contentid);
            bundle.putBoolean(CommentDetailActivity.ABOUTME,true);
            launchActivityForResult(CommentDetailActivity.class,bundle,COMMENT_DETAIL_REQUEST_CODE);
        }
    }

    @Override
    public void onBackPressed()
    {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.COMMENT_ABOUT_ME.getCode()+"02");
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.COMMENT_ABOUT_ME.getCode() + "xx");
        super.onStop();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
        {
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
    public void onFavor(int pos)
    {
        if(getLoginStatus()){
            this.pos=pos;
            CommentPraiseRequest request=new CommentPraiseRequest();
            request.setCommentId(commentList.get(pos).getCommentId());
            //0点赞 1取消
            if(commentList.get(pos).getIsPraised()!=null&&!"".equals(commentList.get(pos).getIsPraised())){
                if(Integer.parseInt(commentList.get(pos).getIsPraised())==0){
                    request.setOperationType("1");
                }else{
                    request.setOperationType("0");
                }
            }else{
                request.setOperationType("0");
            }
            sendNetRequest(request, HeaderRequest.COMMENT_PRAISE, COMMENT_PRAISE_REQUEST);
        }else{
            startActivityForResult(new Intent(CommentAboutMeActivity.this,
                    LoginActivity.class), LOGIN_REQUEST_CODE);
        }
    }
    String commentId,beReplyId,beReplyName,beType,beReplyContent;
    int pos,subPos;
    @Override
    public void onComment(int pos, int subPos)
    {
        isShow=true;
        mBottomLayout.setVisibility(View.VISIBLE);

        recyclerView.setFocusable(false);
        mCommentEt.setFocusable(true);
        mCommentEt.setFocusableInTouchMode(true);
        mCommentEt.requestFocus();
        ShowKeyboard(mCommentEt);
        this.pos=pos;
        this.subPos=subPos;
        contentid=commentList.get(pos).getContentId();
        commentId=commentList.get(pos).getCommentId();
        if(subPos>-1){
            this.beReplyId=commentList.get(pos).getSubCommentsList().get(subPos).getRepliedUserId();
            this.beReplyName= CommonUtils.setName(this,commentList.get(pos).getSubCommentsList().get(subPos).getRepliedUserName());
            this.beType=commentList.get(pos).getSubCommentsList().get(subPos).getRepliedUserType();
            this.beReplyContent=commentList.get(pos).getSubCommentsList().get(subPos).getContent();
            mCommentEt.setHint("回复"+beReplyName+":");
        }else{
            this.beReplyId = null;
            this.beType = commentList.get(pos).getUserType();
            this.beReplyContent=commentList.get(pos).getContent();
            mCommentEt.setHint("回复"+CommonUtils.setName(this,commentList.get(pos).getCommentatorName())+":");
        }

    }

    @Override
    public void onRelate(ContentComments comment)
    {
        String type = comment.getType();
        String contentId = comment.getAssociationId();
        String subjectId = comment.getSubjectId();
        //0:专题 1:专题历史 2:挑战 3:IP征集,4心愿，5街拍街拍
        int num=Integer.parseInt(type);
        switch (num){
            case 0:
                Intent intent=new Intent(this, SubjectSpecialActivity.class);
                intent.putExtra(SubjectSpecialActivity.SUBJECT_ID,contentId);
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(this, SubjectFinalPkActivity.class);
                intent1.putExtra(SubjectFinalPkActivity.CONTENT_ID, contentId);
                intent1.putExtra(SubjectFinalPkActivity.SUBJECT_ID, subjectId);
                startActivity(intent1);
                break;
            case 2:
                Bundle bundle = new Bundle();
                bundle.putString(SubjectChallengeActivity.CONTENT_ID, contentId);
                bundle.putString(SubjectChallengeActivity.SUBJECT_ID, subjectId);
                launchActivity(SubjectChallengeActivity.class, bundle);
                break;
            case 3:
                Intent intent3=new Intent(this, IPDetailActivity.class);
                intent3.putExtra(IPDetailActivity.IPID,contentId);
                startActivity(intent3);
                break;
            case 4:
                if (comment.getStatus() == 0) {
                    Intent intent4 = new Intent(this, WishUserActivity.class);
                    intent4.putExtra(WishUserActivity.CONTENT_ID, contentId);
                    startActivity(intent4);
                } else if (comment.getStatus() == 1) {
                    Intent intent4 = new Intent(this, WishReplyActivity.class);
                    intent4.putExtra(WishReplyActivity.CONTENT_ID, contentId);
                    startActivity(intent4);
                }
//                Intent intent4=new Intent(this, WishDetailActivity.class);
//                intent4.putExtra(WishDetailActivity.CONTENT_ID,contentId);
//                startActivity(intent4);
                break;
            case 5:
                Intent intent5=new Intent(this, SubjectSnapActivity.class);
                intent5.putExtra(SubjectSnapActivity.SUBJECT_ID,contentId);
                startActivity(intent5);
                break;
            case 7:
                Intent intent7=new Intent(this, SubjectNoticeActivity.class);
                intent7.putExtra(SubjectNoticeActivity.SUBJECT_ID,contentId);
                startActivity(intent7);
                break;
            case 8:
            case 9:
            case 10:
                // 不做任何处理
                break;
            default:
//                ToastUtil.showShort(this, Constant.DEFAULT_TIPS_MSG);
                break;
        }
    }

    @Override
    public void onSuccess(String response, String url, int actionId)
    {
        super.onSuccess(response, url, actionId);
        switch (actionId)
        {
            case MY_COMMENT_REQUEST:
                CommentSearchResponse commentRes = JsonUtils.fromJson(response,
                        CommentSearchResponse.class);
                if (COMMON_SUCCESS.equals(commentRes.getCode()))
                {
                    showView(commentRes);
                }
                break;
            case COMMENT_PRAISE_REQUEST:
                BaseResponse praRes=JsonUtils.fromJson(response,
                        BaseResponse.class);
                if(COMMON_SUCCESS.equals(praRes.getCode())){
                    praiseSuccess();
                }else{
                    ToastUtil.showShort(CommentAboutMeActivity.this,praRes.getCode());
                }
                break;
            case COMMENT_REPLY_REQUEST:
                BaseResponse replyRes=JsonUtils.fromJson(response,
                        BaseResponse.class);
                if(COMMON_SUCCESS.equals(replyRes.getCode())){
                    commentReplySuccess();
                }else{
                    ToastUtil.showShort(CommentAboutMeActivity.this,replyRes.getCode());
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case COMMENT_DETAIL_REQUEST_CODE:

                if (resultCode == RESULT_OK)
                {
                    page = 0;
                    requestData();
                }
                break;

        }
    }

    private void commentReplySuccess(){

        SubComments subNew=new SubComments();
        subNew.setBeRepliedUserName(beReplyName);
        subNew.setBeRepliedUserId(beReplyId);
        subNew.setBeRepliedUserType(beType);
        subNew.setRepliedUserName(CommonUtils.setName(this,getUserInfo().getNickName()));
        subNew.setRepliedUserId(getUserInfo().getUserId());
        subNew.setRepliedUserType(getUserInfo().getUserType()+"");
        subNew.setContent(mCommentEt.getText().toString().trim());
        mCommentEt.setText("");
        commentList.get(pos).getSubCommentsList().add(subNew);
        commentList.get(pos).setCommentNum(commentList.get(pos).getCommentNum()+1);
        commentList.get(pos).setIsReplied("0");
        mAdapter.notifyDataSetChanged();
    }

    private void praiseSuccess(){

        CommentPraise praNew=new CommentPraise();
        praNew.setPraisedUserName(CommonUtils.setName(this,getUserInfo().getNickName()));
        praNew.setPraisedUserId(getUserInfo().getUserId());
        if(commentList.get(pos).getIsPraised()!=null&&!"".equals(commentList.get(pos).getIsPraised())){
            if(Integer.parseInt(commentList.get(pos).getIsPraised())==0){
                commentList.get(pos).setIsPraised("1");
                String userId=getUserInfo().getUserId();
                for(int i=0;i<commentList.get(pos).getPraiseList().size();i++){
                    if(userId.equals(commentList.get(pos).getPraiseList().get(i).getPraisedUserId())){
                        commentList.get(pos).getPraiseList().remove(i);
                    }
                }
            }else{
                commentList.get(pos).setIsPraised("0");
                if(commentList.get(pos).getPraiseList()!=null&&commentList.get(pos).getPraiseList().size()>0){
                    commentList.get(pos).getPraiseList().add(0,praNew);
                }else{
                    commentList.get(pos).getPraiseList().add(praNew);
                }
            }
        }else{
            commentList.get(pos).setIsPraised("0");
            if(commentList.get(pos).getPraiseList()!=null&&commentList.get(pos).getPraiseList().size()>0){
                commentList.get(pos).getPraiseList().add(0,praNew);
            }else{
                commentList.get(pos).getPraiseList().add(praNew);
            }
        }
        mAdapter.notifyDataSetChanged();
    }


    private void showView(CommentSearchResponse commentRes)
    {

        if (commentRes.getMyComments() != null && commentRes.getMyComments().size() > 0)
        {
            mNoData.setVisibility(View.GONE);
            if (page == 0)
            {
                footerView.setVisibility(View.GONE);
                commentList.clear();
                swipeRefreshLayout.setRefreshing(false);
            } else
            {
                if (commentRes.getMyComments().size() >= rows)
                {
                    footerView.setVisibility(View.GONE);
                } else
                {
                    footerView.setVisibility(View.VISIBLE);
                    mAdapter.setNoMoreData(true);
                }
            }
            page++;
            commentList.addAll(commentRes.getMyComments());
        } else
        {
            if(page==0){
                mNoData.setVisibility(View.VISIBLE);
                footerView.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }else{
                mNoData.setVisibility(View.GONE);
                footerView.setVisibility(View.VISIBLE);
                mAdapter.setNoMoreData(true);
            }

        }
        mAdapter.notifyDataSetChanged();
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
                reply(commentId,beReplyId,beReplyContent);
            } else
            {
                startActivityForResult(new Intent(CommentAboutMeActivity.this,
                        LoginActivity.class), LOGIN_REQUEST_CODE);
            }
            return true;
        }
        return false;
    }

    private void reply(String commentId,String beRepliedId,String beReplyContent){
        if (TextUtils.isEmpty(mCommentEt.getText().toString().trim()))
        {
            ToastUtil.showShort(CommentAboutMeActivity.this, getString(R.string.reply_empty_tips));
            return;
        }
        CommentSubmitRequest request=new CommentSubmitRequest();
        request.setContentId(contentid);
        request.setCommentId(commentId);
        request.setOperationType("1");
        request.setBeRepliedId(beRepliedId);
        request.setContent(mCommentEt.getText().toString());
        request.setUserContent(beReplyContent);
        sendNetRequest(request, HeaderRequest.SUBJECT_COMMENT_SUBMIT, COMMENT_REPLY_REQUEST);
        mBottomLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onPause()
    {
        HideKeyboard(mCommentEt);
        super.onPause();
    }

    boolean isTop;
    private ABaseLinearLayoutManager getLayoutManager()
    {
        final ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(
                CommentAboutMeActivity.this);
        layoutManager.setOnRecyclerViewScrollLocationListener(recyclerView,
                new OnRecyclerViewScrollLocationListener()
                {
                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView)
                    {
                        isTop=true;
                    }
                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView)
                    {
                        if (!isTop){
                            requestData();
                            footerView.setVisibility(View.VISIBLE);
                        }
                        isTop=false;
                    }
                });
        return layoutManager;
    }

    //隐藏虚拟键盘
    public void HideKeyboard(View v)
    {
        isShow=false;
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive())
        {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

        }
    }

    //显示虚拟键盘
    public void ShowKeyboard(View v)
    {
        isShow=true;
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }
}
