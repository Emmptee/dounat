package com.donut.app.mvp.subject.special;

import com.donut.app.config.BehaviourEnum;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.CollectRequest;
import com.donut.app.http.message.CommentSubmitRequest;
import com.donut.app.http.message.ConcernedOnStarRequest;
import com.donut.app.http.message.PraiseRequest;
import com.donut.app.http.message.ShareRequest;
import com.donut.app.http.message.SubjectRequest;
import com.donut.app.http.message.SubjectResponse;
import com.donut.app.http.message.wish.AddPlayNumRequest;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;


/**
 * Created by Qi on 2017/4/12.
 * Description : <br>
 */
public class SubjectSpecialPresenter extends SubjectSpecialContract.Presenter {

    private static final int SUBJECT_SPECIAL_DETAIL_CODE = 1,
            LIKE_REQUEST = 2, COLLECT_REQUEST = 3,
            COMMENT_REQUEST = 4, SHARE_REQUEST = 5,
            CONCERNED_ON_STAR = 6, ADD_PLAY_NUM = 7;

    public void loadData(boolean showLoadingView, String subjectId) {
        SubjectRequest request = new SubjectRequest();
        request.setSubjectId(subjectId);
        super.loadData(request,
                HeaderRequest.SUBJECT_DETAIL,
                SUBJECT_SPECIAL_DETAIL_CODE,
                showLoadingView);
    }

    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        switch (actionId) {
            case SUBJECT_SPECIAL_DETAIL_CODE:
                SubjectResponse response
                        = JsonUtils.fromJson(responseJson, SubjectResponse.class);
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

    void onLike(SubjectResponse model, boolean like) {
        if (model == null) {
            return;
        }
        PraiseRequest request = new PraiseRequest();
        request.setContentId(model.getContentId());
        request.setPraiseType(like ? 1 : 2);
        super.loadData(request,
                HeaderRequest.SUBJECT_PRAISE,
                LIKE_REQUEST,
                false);
    }

    void onToCollect(SubjectResponse model, boolean collect) {
        if (model == null) {
            return;
        }
        CollectRequest request = new CollectRequest();
        request.setContentId(model.getContentId());
        request.setType(0);
        request.setStatus(collect ? 1 : 0);
        super.loadData(request,
                HeaderRequest.SUBJECT_COLLECT,
                COLLECT_REQUEST,
                false);
    }

    void sendComment(SubjectResponse model, String msg) {
        if (model == null) {
            return;
        }
        CommentSubmitRequest request = new CommentSubmitRequest();
        request.setContentId(model.getContentId());
        request.setOperationType("0");
        request.setContent(msg);
        super.loadData(request,
                HeaderRequest.SUBJECT_COMMENT_SUBMIT,
                COMMENT_REQUEST,
                false);
    }

    public void onToFollow(SubjectResponse model, boolean follow) {
        if (model == null) {
            return;
        }
        ConcernedOnStarRequest request = new ConcernedOnStarRequest();
        request.setStarId(model.getActorId());
        String operation;
        if (follow) {
            operation = "0";
        } else {
            operation = "1";
        }
        request.setOperation(operation);
        super.loadData(request,
                HeaderRequest.CONCERNED_ON_STAR,
                CONCERNED_ON_STAR,
                false);
    }

    public void shareRequest(String contentId) {
        ShareRequest request = new ShareRequest();
        request.setContentId(contentId);
        super.loadData(request,
                HeaderRequest.SHARE,
                SHARE_REQUEST,
                false);
    }

    void addPlayNum(String contentId){
        AddPlayNumRequest request = new AddPlayNumRequest();
        request.setMediaId(contentId);
        request.setIdType(2);
        super.loadData(request,
                HeaderRequest.ADD_PLAY_NUM,
                ADD_PLAY_NUM,
                false);
    }

    public void saveBehaviour(String functionCode) {
        SaveBehaviourDataService.startAction(mView.getContext(),
                BehaviourEnum.SUBJECT_DETAIL.getCode() + functionCode);
    }

    public void saveBehaviour(String functionCode, Object request, String header) {
        SaveBehaviourDataService.startAction(mView.getContext(),
                BehaviourEnum.SUBJECT_DETAIL.getCode() + functionCode, request, header);
    }
}
