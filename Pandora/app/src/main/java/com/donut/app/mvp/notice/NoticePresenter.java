package com.donut.app.mvp.notice;

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
public class NoticePresenter extends NoticeContract.Presenter {

    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
    }

    public void saveBehaviour(String functionCode) {
        SaveBehaviourDataService.startAction(mView.getContext(),
                BehaviourEnum.NOTICE_ACTION.getCode() + functionCode);
    }

    public void saveBehaviour(String functionCode, Object request, String header) {
        SaveBehaviourDataService.startAction(mView.getContext(),
                BehaviourEnum.NOTICE_ACTION.getCode() + functionCode, request, header);
    }
}
