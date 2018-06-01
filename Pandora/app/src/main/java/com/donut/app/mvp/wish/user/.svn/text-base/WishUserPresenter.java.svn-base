package com.donut.app.mvp.wish.user;

import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.CommentSubmitRequest;
import com.donut.app.http.message.PraiseRequest;
import com.donut.app.http.message.ShareRequest;
import com.donut.app.http.message.wish.AddPlayNumRequest;
import com.donut.app.http.message.wish.WishDetailsRequest;
import com.donut.app.http.message.wish.WishDetailsResponse;
import com.donut.app.utils.JsonUtils;


/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public class WishUserPresenter extends WishUserContract.Presenter {

    private static final int WISH_DETAIL_REQUEST = 1, ADD_PLAY_NUM = 2,
            LIKE_REQUEST = 2, COLLECT_REQUEST = 3, COMMENT_REQUEST = 4, SHARE_REQUEST = 5;

    public void loadData(boolean showLoadingView, String uuid) {
        WishDetailsRequest request = new WishDetailsRequest();
        request.setB02Id(uuid);
        super.loadData(request,
                HeaderRequest.WISH_DETAIL,
                WISH_DETAIL_REQUEST,
                showLoadingView);
    }

    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        switch (actionId) {
            case WISH_DETAIL_REQUEST:
                WishDetailsResponse response
                        = JsonUtils.fromJson(responseJson, WishDetailsResponse.class);
                if (COMMON_SUCCESS.equals(response.getCode())) {
                    mView.showView(response);
                } else {
                    showToast(response.getMsg());
                }
                break;
            case COMMENT_REQUEST:
                BaseResponse commentRes = JsonUtils.fromJson(responseJson,
                        BaseResponse.class);
                if (COMMON_SUCCESS.equals(commentRes.getCode())) {
                    mView.clearCommentEdit();
                } else {
                    showToast(commentRes.getMsg());
                }
                break;
        }
    }

    void addPlayNum(WishDetailsResponse model){
        AddPlayNumRequest request = new AddPlayNumRequest();
        request.setMediaId(model.getG01Id());
        request.setIdType(2);
        super.loadData(request,
                HeaderRequest.ADD_PLAY_NUM,
                ADD_PLAY_NUM,
                false);
    }

    void onLike(WishDetailsResponse model, boolean like) {
        PraiseRequest request = new PraiseRequest();
        request.setContentId(model.getB02Id());
        request.setPraiseType(like ? 1 : 2);
        super.loadData(request,
                HeaderRequest.SUBJECT_PRAISE,
                LIKE_REQUEST,
                false);
    }

    void sendComment(WishDetailsResponse model, String msg) {
        CommentSubmitRequest request = new CommentSubmitRequest();
        request.setContentId(model.getB02Id());
        request.setOperationType("0");
        request.setContent(msg);

        super.loadData(request,
                HeaderRequest.SUBJECT_COMMENT_SUBMIT,
                COMMENT_REQUEST,
                false);
    }

    void onToShare(WishDetailsResponse model) {
        ShareRequest request = new ShareRequest();
        request.setContentId(model.getB02Id());
        super.loadData(request,
                HeaderRequest.SHARE,
                SHARE_REQUEST,
                false);
//        saveBehaviour("02", request, HeaderRequest.SHARE);
    }

}
