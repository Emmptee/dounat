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
public abstract class MVPBaseFragment<B extends ViewDataBinding, T extends BasePresenter>
        extends DataBindingFragment<B> implements BaseView {
    public T mPresenter;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.cancelLoad();
            mPresenter.detachView();
        }
    }

    @Override
    protected void initPresenter() {
        mPresenter = getInstance(this, 1);
        mPresenter.attachView(this);
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    public T getInstance(Object o, int i) {
        try {
            Class mPresenterClass = (Class) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i];
            return (T) mPresenterClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }
        return null;
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
        getActivity().finish();
        launchActivity(LoginActivity.class);
        SharedPreferences sp_Info = getContext().getSharedPreferences(Constant.SP_INFO,
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

    protected void launchActivity(Class<?> cls) {
        launchActivity(cls, null);
    }

    protected void launchActivity(Class<?> cls, Bundle param) {
        Intent intent = new Intent(getContext(), cls);
        if (param != null) {
            intent.putExtras(param);
        }
        getContext().startActivity(intent);
    }

    protected void launchActivityForResult(Class<?> cls, int requestCode) {
        launchActivityForResult(cls, null, requestCode);
    }

    protected void launchActivityForResult(Class<?> cls, Bundle param,
                                           int requestCode) {
        Intent intent = new Intent(getContext(), cls);
        if (param != null) {
            intent.putExtras(param);
        }
        startActivityForResult(intent, requestCode);
    }
}
