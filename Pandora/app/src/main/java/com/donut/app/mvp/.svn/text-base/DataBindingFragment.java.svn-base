package com.donut.app.mvp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.donut.app.fragment.base.BaseFragment;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public abstract class DataBindingFragment<B extends ViewDataBinding> extends BaseFragment {
    public Context mContext;
    public B mViewBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        mViewBinding = DataBindingUtil.inflate(inflater, this.getLayoutId(), container, false);
        initTransitionView();
        mContext = this.getContext();
        initPresenter();
        initView();
        initEvent();
        loadData();

        return mViewBinding.getRoot();
    }

    protected void initPresenter() {
    }

    protected void initTransitionView() {//在这里给转场view副值
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initEvent();

    protected abstract void loadData();
}
