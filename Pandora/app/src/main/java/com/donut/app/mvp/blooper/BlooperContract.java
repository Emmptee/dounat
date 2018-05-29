package com.donut.app.mvp.blooper;

import com.donut.app.http.message.StarListResponse;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public interface BlooperContract {
    interface View extends BaseView {
        void showView(StarListResponse detailResponse);
    }

    abstract class Presenter extends BasePresenterImpl<View> {

    }
}
