package com.donut.app.mvp.spinOff;

import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

/**
 * Created by Qi on 2017/6/5.
 * Description : <br>
 */
public interface SpinOffContract {
    interface View extends BaseView {
    }

    abstract class Presenter extends BasePresenterImpl<View> {
    }
}
