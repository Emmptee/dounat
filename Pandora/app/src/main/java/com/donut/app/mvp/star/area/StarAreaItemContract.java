package com.donut.app.mvp.star.area;

import com.donut.app.http.message.SubjectListDetail;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public interface StarAreaItemContract {
    interface View extends BaseView {
        void showView(SubjectListDetail detail);

        void gotoDetail();
    }

    abstract class Presenter<V extends View> extends BasePresenterImpl<V> {

    }
}
