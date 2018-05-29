package com.donut.app.mvp.notice;

import com.donut.app.http.message.wish.WishDetailsResponse;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public interface NoticeContract {
    interface View extends BaseView {
    }

    abstract class Presenter extends BasePresenterImpl<View> {

    }
}
