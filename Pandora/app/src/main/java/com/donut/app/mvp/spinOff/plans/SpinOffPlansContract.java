package com.donut.app.mvp.spinOff.plans;

import com.donut.app.http.message.spinOff.ExclusivePlanResponse;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

/**
 * Created by Qi on 2017/6/5.
 * Description : <br>
 */
public interface SpinOffPlansContract {
    interface View extends BaseView {
        void showView(ExclusivePlanResponse detailResponse);
    }

    abstract class Presenter<V extends View> extends BasePresenterImpl<V> {

    }
}
