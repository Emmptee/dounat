package com.donut.app.mvp.spinOff.boons.detail;

import com.donut.app.http.message.spinOff.ExpressionPicsDetailResponse;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

/**
 * Created by Qi on 2017/3/27.
 * Description : <br>
 */
public interface SpinOffBoonsDetailContract {
    interface View extends BaseView {
        void showView(ExpressionPicsDetailResponse detailResponse);
    }

    abstract class Presenter extends BasePresenterImpl<View> {

    }
}
