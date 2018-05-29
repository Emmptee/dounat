package com.donut.app.mvp.mine;

import com.android.volley.manager.RequestManager;
import com.donut.app.R;
import com.donut.app.SysApplication;
import com.donut.app.entity.UserInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.SendNetRequestManager;
import com.donut.app.http.message.BaseRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.ShoppingAccountRequest;
import com.donut.app.http.message.ShoppingAccountResponse;
import com.donut.app.http.message.UploadResponse;
import com.donut.app.http.message.UserInfoEditRequest;
import com.donut.app.http.message.UserInfoResponse;
import com.donut.app.utils.JsonUtils;

import java.util.Map;


/**
 * Created by Qi on 2017/3/28.
 * Description : 类为public是必须的,在反射中强转时非public会报错<br>
 */
public class MinePresenter<V extends MineContract.View> extends MineContract.Presenter<V> {

    private static final int INFO_REQUEST = 0, UPLOAD_IMG_REQUEST = 1, INFO_MODIFY = 2, SHOPPING_INFO = 3;

    private String headUrl;

    public void loadData(boolean showLoadingView) {
//        super.loadData(new Object(),
//                HeaderRequest.NEW_HOME,
//                NEW_HOME_CODE,
//                showLoadingView);
    }

    void getShoppingAccountRequest() {
        ShoppingAccountRequest request = new ShoppingAccountRequest();
        request.setRows(0);
        request.setPage(0);
        super.loadData(request,
                HeaderRequest.GET_SHOPPING_ACCOUNT,
                SHOPPING_INFO,
                false);
    }

    void saveUserHeadShot(String path) {
        SendNetRequestManager requestManager = new SendNetRequestManager(requestListener);
        //LoadController loadController =
        requestManager.uploadImg(path, 2, UPLOAD_IMG_REQUEST);
    }

    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        switch (actionId) {
            case INFO_MODIFY:
                BaseResponse headRes = JsonUtils.fromJson(responseJson, BaseResponse.class);
                if (COMMON_SUCCESS.equals(headRes.getCode())) {
                    UserInfo info = SysApplication.getUserInfo();
                    info.setImgUrl(headUrl);
                    SysApplication.setUserInfo(info);
                    mView.showToastMsg(R.string.head_upload_success);
                } else {
                    mView.showToastMsg(headRes.getMsg());
                }
                break;
            case SHOPPING_INFO:
                ShoppingAccountResponse shopRes
                        = JsonUtils.fromJson(responseJson, ShoppingAccountResponse.class);
                if (COMMON_SUCCESS.equals(shopRes.getCode())) {
                    mView.showShopCartAmount(shopRes.getTotalAmount());
                }
                break;
            case INFO_REQUEST:
                UserInfoResponse res = JsonUtils.fromJson(responseJson,
                        UserInfoResponse.class);
                if (COMMON_SUCCESS.equals(res.getCode())) {
                    mView.showStarUserInfo(res);
                }
                break;
        }
    }

    private RequestManager.RequestListener requestListener
            = new RequestManager.RequestListener() {
        @Override
        public void onRequest() {
        }

        @Override
        public void onLoading(long total, long count, String filePath) {
        }

        @Override
        public void onSuccess(String response, Map<String, String> headers,
                              String url, int actionId) {
            UploadResponse res = JsonUtils.fromJson(response, UploadResponse.class);
            if (COMMON_SUCCESS.equals(res.getCode())) {
                switch (actionId) {
                    case UPLOAD_IMG_REQUEST:
                        headUrl = res.getFileUrl();
                        updateUserInfo(headUrl);
                        break;
                }
            } else {
                mView.showToastMsg(res.getMsg());
            }
        }

        @Override
        public void onError(String errorMsg, String url, int actionId) {
        }
    };

    private void updateUserInfo(String imgUrl) {
        UserInfoEditRequest headRequest = new UserInfoEditRequest();
        headRequest.setHeadPic(imgUrl);
        super.loadData(headRequest,
                HeaderRequest.MODIFY_PERSONAL_INFO,
                INFO_MODIFY,
                false);
    }

    @Override
    public void getStarUserInfo() {
        super.loadData(new BaseRequest(), HeaderRequest.GET_PERSONAL_INFO, INFO_REQUEST,
                false);
    }
}
