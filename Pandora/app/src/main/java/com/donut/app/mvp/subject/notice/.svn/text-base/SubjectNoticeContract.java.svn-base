package com.donut.app.mvp.subject.notice;

import com.donut.app.http.message.subjectSnap.SubjectSnapDetailResponse;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public interface SubjectNoticeContract {
    interface View extends BaseView {
        void showView(SubjectSnapDetailResponse detailResponse);
        void clearCommentEdit();
    }

    abstract class Presenter extends BasePresenterImpl<View> {

    }
}
