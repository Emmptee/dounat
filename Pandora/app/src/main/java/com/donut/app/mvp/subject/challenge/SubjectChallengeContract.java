package com.donut.app.mvp.subject.challenge;

import com.donut.app.http.message.SubjectHistoryPKDetailResponse;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

/**
 * Created by Qi on 2017/4/12.
 * Description : <br>
 */
public interface SubjectChallengeContract {
    interface View extends BaseView {
        void showView(SubjectHistoryPKDetailResponse detailResponse);
        void clearCommentEdit();
        void sendAudioSuccess();
    }

    abstract class Presenter extends BasePresenterImpl<View> {

    }
}
