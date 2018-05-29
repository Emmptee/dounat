package com.donut.app.mvp.subject.special;

import com.donut.app.http.message.SubjectResponse;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

/**
 * Created by Qi on 2017/4/12.
 * Description : <br>
 */
public interface SubjectSpecialContract {
    interface View extends BaseView {
        void showView(SubjectResponse detailResponse);
        void clearCommentEdit();
    }

    abstract class Presenter extends BasePresenterImpl<View> {

    }
}
