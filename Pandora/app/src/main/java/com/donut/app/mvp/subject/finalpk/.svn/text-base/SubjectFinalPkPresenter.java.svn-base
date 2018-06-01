package com.donut.app.mvp.subject.finalpk;

import com.android.volley.manager.RequestManager;
import com.donut.app.R;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.CollectRequest;
import com.donut.app.http.message.CommentSubmitRequest;
import com.donut.app.http.message.ConcernedOnStarRequest;
import com.donut.app.http.message.PraiseRequest;
import com.donut.app.http.message.RecommandRequest;
import com.donut.app.http.message.ShareRequest;
import com.donut.app.http.message.SubjectHistoryPKDetailRequest;
import com.donut.app.http.message.SubjectHistoryPKDetailResponse;
import com.donut.app.http.message.subjectSnap.SubjectSnapDetailResponse;
import com.donut.app.http.message.wish.AddPlayNumRequest;
import com.donut.app.model.audio.StorageManager;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;

import java.util.Map;


/**
 * Created by Qi on 2017/4/12.
 * Description : <br>
 */
public class SubjectFinalPkPresenter extends SubjectFinalPkContract.Presenter {

    private static final int CHALLENGE_DETAIL = 1,
            LIKE_REQUEST = 2, COLLECT_REQUEST = 3,
            COMMENT_REQUEST = 4, SHARE_REQUEST = 5,
            ADD_PLAY_NUM = 7, CONCERNED_ON_STAR = 8;

    public void loadData(boolean showLoadingView, String contentId) {

        SubjectHistoryPKDetailRequest request = new SubjectHistoryPKDetailRequest();
        request.setContentId(contentId);
        super.loadData(request,
                HeaderRequest.FINAL_PK_DETAIL,
                CHALLENGE_DETAIL,
                showLoadingView);
    }

    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        switch (actionId) {
            case CHALLENGE_DETAIL:
                SubjectHistoryPKDetailResponse response
                        = JsonUtils.fromJson(responseJson, SubjectHistoryPKDetailResponse.class);
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

    void onLike(String contentId, boolean like) {
        PraiseRequest request = new PraiseRequest();
        request.setContentId(contentId);
        request.setPraiseType(like ? 1 : 2);
        super.loadData(request,
                HeaderRequest.SUBJECT_PRAISE,
                LIKE_REQUEST,
                false);
    }

    void onToCollect(String contentId, boolean collect) {
        CollectRequest request = new CollectRequest();
        request.setContentId(contentId);
        request.setType(0);
        request.setStatus(collect ? 1 : 0);
        super.loadData(request,
                HeaderRequest.SUBJECT_COLLECT,
                COLLECT_REQUEST,
                false);
    }

    void sendComment(String contentId, String msg) {
        CommentSubmitRequest request = new CommentSubmitRequest();
        request.setContentId(contentId);
        request.setOperationType("0");
        request.setContent(msg);
        super.loadData(request,
                HeaderRequest.SUBJECT_COMMENT_SUBMIT,
                COMMENT_REQUEST,
                false);
        saveBehaviour("03", request, HeaderRequest.SUBJECT_COMMENT_SUBMIT);
    }

    public void shareRequest(String contentId) {
        ShareRequest request = new ShareRequest();
        request.setContentId(contentId);
        super.loadData(request,
                HeaderRequest.SHARE,
                SHARE_REQUEST,
                false);
    }

    void addPlayNum(String contentId) {
        AddPlayNumRequest request = new AddPlayNumRequest();
        request.setMediaId(contentId);
        request.setIdType(2);
        super.loadData(request,
                HeaderRequest.ADD_PLAY_NUM,
                ADD_PLAY_NUM,
                false);
    }

    void onToFollow(SubjectHistoryPKDetailResponse model, boolean follow) {
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

    public void saveBehaviour(String functionCode) {
        SaveBehaviourDataService.startAction(mView.getContext(),
                BehaviourEnum.FINAL_PK_DETAIL.getCode() + functionCode);
    }

    public void saveBehaviour(String functionCode, Object request, String header) {
        SaveBehaviourDataService.startAction(mView.getContext(),
                BehaviourEnum.FINAL_PK_DETAIL.getCode() + functionCode, request, header);
    }

}