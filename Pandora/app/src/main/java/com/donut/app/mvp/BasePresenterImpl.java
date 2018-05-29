package com.donut.app.mvp;

import android.support.annotation.UiThread;
import android.util.Log;
import android.util.SparseArray;

import com.android.volley.manager.LoadController;
import com.android.volley.manager.RequestManager;
import com.donut.app.R;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.SendNetRequestManager;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.shakestar.MyLikeResponse;
import com.donut.app.http.message.wish.CommendLikeRequest;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.L;

import java.util.Map;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public abstract class BasePresenterImpl<V extends BaseView>
        implements BasePresenter<V>,
        RequestManager.RequestListener {
    private static final String TAG = "BasePresenterImpl";
    protected V mView;

    private boolean showLoadingView;

    private SparseArray<LoadController> mSparseArray
            = new SparseArray<>();

    protected static final String COMMON_SUCCESS = "0000";// 请求成功

    private static final String COMMON_NOT_LOGIN = "0002";// 未登录

    @Override
    public void attachView(V view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    public void loadData(Object request, final String header,
                         final int requestCode, final boolean showLoadingView) {
        this.showLoadingView = showLoadingView;
        SendNetRequestManager requestManager = new SendNetRequestManager(this);
        LoadController loadController = requestManager.sendNetRequest(request,
                header, requestCode);
        mSparseArray.put(requestCode, loadController);
    }

    @Override
    public void cancelLoad() {
        if (mSparseArray.size() > 0) {
            for (int i = 0; i < mSparseArray.size(); i++) {
                LoadController controller = mSparseArray.valueAt(i);
                if (controller != null) {
                    controller.cancel();
                }
            }
        }
    }

    protected void showToast(String message) {
        mView.showToastMsg(message);
    }

    protected void showToast(int messageRes) {
        mView.showToastMsg(messageRes);
    }

    @UiThread
    @Override
    public void onRequest() {
        if (showLoadingView) {
            mView.showLoadingDialog();
            showLoadingView = false;
        }
    }

    @Override
    public void onLoading(long total, long count, String filePath) {
    }

    @Override
    @UiThread
    public void onSuccess(String response, Map<String, String> headers,
                          String url, int actionId) {
        L.i("====", response);
        if (mView == null) {
            return;
        }
        mView.dismissLoadingDialog();
        mSparseArray.delete(actionId);
        BaseResponse res = JsonUtils.fromJson(response, BaseResponse.class);
        if (res != null && COMMON_NOT_LOGIN.equals(res.getCode())) {
            showToast(R.string.login_timeout_msg);
            mView.expiresToken();
            return;
        }
        onSuccess(response, url, actionId);
    }

    @UiThread
    @Override
    public void onError(String errorMsg, String url, int actionId) {
        mView.dismissLoadingDialog();
        mSparseArray.delete(actionId);
        showToast(R.string.connect_failuer_toast);
    }

    public abstract void onSuccess(String responseJson, String url, int actionId);
}