package com.donut.app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.Constant;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class StarDetailDescriptionActivity extends BaseActivity {

    public static final String CONTENT = "CONTENT";

    @ViewInject(R.id.description_layout_text)
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.star_detail_description_layout);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        updateHeadTitle("明星简介", true);
        initData();
    }

    private void initData() {
        String content = getIntent().getStringExtra(CONTENT);
        textView.setText(content);
    }
}
