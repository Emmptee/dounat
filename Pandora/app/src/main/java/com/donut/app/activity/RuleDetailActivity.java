package com.donut.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class RuleDetailActivity extends BaseActivity
{
    @ViewInject(R.id.detail)
    private TextView tvDetail;
    public static final String DETAIL="detail";
    public static final String CHALL_TYPE="chall_type";
    String detail,title;
    boolean isChall;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iprule);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        detail=getIntent().getStringExtra(DETAIL);
        isChall=getIntent().getBooleanExtra(CHALL_TYPE,false);
        ViewUtils.inject(this);
        if(isChall){
            title="挑战描述";
            SaveBehaviourDataService.startAction(this, BehaviourEnum.CHALLENGE_RULE.getCode()+"00");
        }else{
            title="终极PK简介";
        }

        updateHeadTitle(title,true);
        if(detail!=null){
            tvDetail.setText(detail);
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        if(isChall){
            SaveBehaviourDataService.startAction(this, BehaviourEnum.CHALLENGE_RULE.getCode()+"01");
        }
    }
}
