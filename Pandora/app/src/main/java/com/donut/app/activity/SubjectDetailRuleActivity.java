package com.donut.app.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.Constant;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.SubjectRuleRequest;
import com.donut.app.http.message.SubjectRuleResponse;
import com.donut.app.utils.BindingUtils;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class SubjectDetailRuleActivity extends BaseActivity {

    @ViewInject(R.id.subject_rule_iv_playbill)
    private ImageView ivPlaybill;

    @ViewInject(R.id.subject_rule_tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.subject_rule_tv_content)
    private TextView tvContent;

    @ViewInject(R.id.subject_rule_layout_star1)
    private FrameLayout layoutStar1;

    @ViewInject(R.id.subject_rule_iv_head1)
    private ImageView ivHead1;

    @ViewInject(R.id.subject_rule_tv_name1)
    private TextView tvName1;

    @ViewInject(R.id.subject_rule_tv_endTime)
    private TextView tvEndTime;

    public static final String IS_SUBJECT_NOTICE = "IS_SUBJECT_NOTICE",
            SUBJECT_ID = "SUBJECT_ID", CONTENT_ID = "CONTENT_ID";

    private static final int SUBJECT_RULE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_detail_rule);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        String title = "专题规则";
        if (getIntent().getBooleanExtra(IS_SUBJECT_NOTICE, false)) {
            title = "通告内容";
        }
        updateHeadTitle(title, true);
        loadData();
    }

    private void loadData() {
        String uuid = getIntent().getStringExtra(SUBJECT_ID);
        SubjectRuleRequest request = new SubjectRuleRequest();
        request.setUuid(uuid);
        request.setContentId(getIntent().getStringExtra(CONTENT_ID));
        sendNetRequest(request, HeaderRequest.SUBJECT_RULE_REQUEST, SUBJECT_RULE_REQUEST);
    }

    @Override
    public void onSuccess(String response, String url, int actionId) {
        super.onSuccess(response, url, actionId);
        switch (actionId) {
            case SUBJECT_RULE_REQUEST:

                SubjectRuleResponse ruleResponse = JsonUtils.fromJson(response, SubjectRuleResponse.class);
                if (COMMON_SUCCESS.equals(ruleResponse.getCode())) {
                    showView(ruleResponse);
                } else {
                    showToast(ruleResponse.getMsg());
                }
                break;
        }
    }

    private void showView(SubjectRuleResponse ruleResponse) {
        BindingUtils.loadImg(ivPlaybill, ruleResponse.getPublicPic());
        tvTitle.setText(ruleResponse.getSubjectName());
        tvContent.setText(ruleResponse.getDescription());
        String endTime = ruleResponse.getEndTime();
        if (TextUtils.isEmpty(endTime)) {
            tvEndTime.setVisibility(View.GONE);
        } else {
            tvEndTime.setText("截止日期:" + ruleResponse.getEndTime());
        }

        layoutStar1.setVisibility(View.VISIBLE);
        BindingUtils.loadRoundImg(ivHead1, ruleResponse.getHeadPic());
        tvName1.setText(ruleResponse.getName());
    }
}