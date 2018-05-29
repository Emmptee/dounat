package com.donut.app.mvp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public abstract class DataBindingActivity<B extends ViewDataBinding> extends BaseActivity {
    public Context mContext;
    public B mViewBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mViewBinding = DataBindingUtil.setContentView(this, this.getLayoutId());
        initTransitionView();
        initPresenter();
        initView();
        initEvent();
        loadData();
    }

    @Override
    protected void updateHeadTitle(String titleContent, boolean ishaveBack) {
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        if (tv_title != null) {
            tv_title.setText(titleContent);
        }
        View backView = findViewById(R.id.back);
        if (ishaveBack && backView != null) {
            backView.setVisibility(View.VISIBLE);
            backView.setOnClickListener(this);
        }
    }

    protected void initPresenter() {}

    protected void initTransitionView() {//在这里给转场view副值
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initEvent();

    protected abstract void loadData();
}
