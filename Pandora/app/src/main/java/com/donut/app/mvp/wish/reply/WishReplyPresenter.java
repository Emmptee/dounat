package com.donut.app.mvp.wish.reply;

import com.donut.app.config.BehaviourEnum;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.CollectRequest;
import com.donut.app.http.message.CommentSubmitRequest;
import com.donut.app.http.message.PraiseRequest;
import com.donut.app.http.message.ShareRequest;
import com.donut.app.http.message.wish.AddPlayNumRequest;
import com.donut.app.http.message.wish.WishDetailsRequest;
import com.donut.app.http.message.wish.WishDetailsResponse;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;


/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public class WishReplyPresenter extends WishReplyContract.Presenter {

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
                BaseResponse baseResponse = JsonUtils.fromJson(responseJson, BaseResponse.class);
                if (COMMON_SUCCESS.equals(baseResponse.getCode())) {
                    mView.clearCommentEdit();
                } else {
                    showToast(baseResponse.getMsg());
                }
                break;
        }
    }

    void addPlayNum(WishDetailsResponse model){
        AddPlayNumRequest request = new AddPlayNumRequest();
        request.setMediaId(model.getG01Id());
        request.setIdType(1);
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
        saveBehaviour("01", request, HeaderRequest.SUBJECT_PRAISE);
    }

    void onToCollect(WishDetailsResponse model, boolean collect) {
        CollectRequest request = new CollectRequest();
        request.setContentId(model.getB02Id());
        request.setType(5);
        request.setStatus(collect ? 1 : 0);
        super.loadData(request,
                HeaderRequest.SUBJECT_COLLECT,
                COLLECT_REQUEST,
                false);
        saveBehaviour("04", request, HeaderRequest.SUBJECT_COLLECT);
    }

    void onToShare(WishDetailsResponse model) {
        ShareRequest request = new ShareRequest();
        request.setContentId(model.getB02Id());
        super.loadData(request,
                HeaderRequest.SHARE,
                SHARE_REQUEST,
                false);
        saveBehaviour("02", request, HeaderRequest.SHARE);
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
        saveBehaviour("03", request, HeaderRequest.SUBJECT_COMMENT_SUBMIT);
    }

    public void saveBehaviour(String functionCode) {
        SaveBehaviourDataService.startAction(mView.getContext(),
                BehaviourEnum.WISH_DETAIL.getCode() + functionCode);
    }

    public void saveBehaviour(String functionCode, Object request, String header) {
        SaveBehaviourDataService.startAction(mView.getContext(),
                BehaviourEnum.WISH_DETAIL.getCode() + functionCode, request, header);
    }
}
