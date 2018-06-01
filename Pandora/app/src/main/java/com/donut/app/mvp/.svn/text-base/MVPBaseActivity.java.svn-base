package com.donut.app.mvp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.ViewDataBinding;
import android.os.Bundle;

import com.donut.app.SysApplication;
import com.donut.app.activity.LoginActivity;
import com.donut.app.config.Constant;
import com.donut.app.customview.LoadingDialog;
import com.donut.app.utils.ToastUtil;

import java.lang.reflect.ParameterizedType;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public abstract class MVPBaseActivity<B extends ViewDataBinding, T extends BasePresenter>
        extends DataBindingActivity<B> implements BaseView {
    public T mPresenter;

    @Override
    protected void initPresenter() {
        if (this.getClass().getGenericSuperclass() instanceof ParameterizedType &&
                ((ParameterizedType) (this.getClass().getGenericSuperclass())).getActualTypeArguments().length > 0) {
            Class mPresenterClass = (Class) ((ParameterizedType) (this.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[1];
            try {
                mPresenter = (T) mPresenterClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            mPresenter.attachView(this);
        }
    }

    @Override
    protected void initEvent(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.cancelLoad();
            mPresenter.detachView();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    private LoadingDialog progressDialog;

    @Override
    public void showLoadingDialog() {
        progressDialog = new LoadingDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        try {
            progressDialog.show();
        } catch (Exception e) {
            showLoadingDialog();
        }
    }

    @Override
    public void dismissLoadingDialog() {
        if (null != progressDialog) {
            progressDialog.dismiss();
            progressDialog.cancel();
            progressDialog = null;
        }
    }

    @Override
    public void expiresToken() {
        SysApplication.setUserInfo(null);
        finish();
        launchActivity(LoginActivity.class);
        SharedPreferences sp_Info = getSharedPreferences(Constant.SP_INFO,
                Context.MODE_PRIVATE);
        sp_Info.edit()
                .putBoolean(Constant.IS_LOGIN, false).apply();
    }

    @Override
    public void showToastMsg(String message) {
        ToastUtil.showShort(getContext(), message);
    }

    @Override
    public void showToastMsg(int messageRes) {
        ToastUtil.showShort(getContext(), messageRes);
    }

    @Override
    protected void launchActivity(Class<?> cls) {
        launchActivity(cls, null);
    }

    @Override
    protected void launchActivity(Class<?> cls, Bundle param) {
        Intent intent = new Intent(getContext(), cls);
        if (param != null) {
            intent.putExtras(param);
        }
        getContext().startActivity(intent);
    }

    @Override
    protected void launchActivityForResult(Class<?> cls, int requestCode) {
        launchActivityForResult(cls, null, requestCode);
    }

    @Override
    protected void launchActivityForResult(Class<?> cls, Bundle param,
                                           int requestCode) {
        Intent intent = new Intent(getContext(), cls);
        if (param != null) {
            intent.putExtras(param);
        }
        startActivityForResult(intent, requestCode);
    }
}
