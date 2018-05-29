package com.donut.app.mvp.subject.finalpk;

import com.donut.app.http.message.SubjectHistoryPKDetailResponse;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

/**
 * Created by Qi on 2017/4/12.
 * Description : <br>
 */
public interface SubjectFinalPkContract {
    interface View extends BaseView {
        void showView(SubjectHistoryPKDetailResponse detailResponse);
        void clearCommentEdit();
    }

    abstract class Presenter extends BasePresenterImpl<View> {

    }
}
