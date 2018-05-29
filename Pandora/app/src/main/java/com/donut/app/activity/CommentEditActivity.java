package com.donut.app.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.Constant;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.CommentSubmitRequest;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class CommentEditActivity extends BaseActivity implements TextView.OnEditorActionListener
{
    @ViewInject(R.id.comment_edit)
    private EditText mCommentEt;

    private CharSequence temp;//监听前的文本
    private int editStart;//光标开始位置
    private int editEnd;//光标结束位置

    private static final int COMMENT_REQUEST = 0;
    public static final String CONTENTID="contentid";

    private String contentId;

    @ViewInject(R.id.head_right_tv)
    private TextView mRight;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_edit);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        contentId=getIntent().getStringExtra(CONTENTID);
        ViewUtils.inject(this);
        updateHeadTitle("发表评论",true);
        mRight.setText("发表");
        initView();
    }

    @OnClick(value = {R.id.menu})
    private void viewOnClick(View v) {
        switch (v.getId()){
            case R.id.menu:
                commentSubmitRequest();
                break;
        }
    }

    private void initView(){
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
                    ToastUtil.showShort(CommentEditActivity.this, getString(R.string.comment_length_tips));
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    mCommentEt.setText(s);
                    mCommentEt.setSelection(tempSelection);
                }
            }
        });
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
            }
            return true;
        }
        return false;
    }

    private long nowTime = 0;
    private void commentSubmitRequest()
    {
        if (TextUtils.isEmpty(mCommentEt.getText().toString().trim()))
        {
            ToastUtil.showShort(CommentEditActivity.this, getString(R.string.comment_empty_tips));
            return;
        }

        long t = System.currentTimeMillis();
        if(t - nowTime <= 1000){
            nowTime = t;
            return;
        }

        CommentSubmitRequest request = new CommentSubmitRequest();
        request.setContentId(contentId);
        request.setOperationType("0");
        request.setContent(mCommentEt.getText().toString());
        sendNetRequest(request, HeaderRequest.SUBJECT_COMMENT_SUBMIT, COMMENT_REQUEST,
                true);
    }

    @Override
    public void onSuccess(String response, String url, int actionId)
    {
        super.onSuccess(response, url, actionId);
        switch (actionId){
            case COMMENT_REQUEST:
                BaseResponse res= JsonUtils.fromJson(response,
                        BaseResponse.class);
                if(COMMON_SUCCESS.equals(res.getCode())){
                    setResult(RESULT_OK);
                    finish();
                }
                break;
        }
    }
}
