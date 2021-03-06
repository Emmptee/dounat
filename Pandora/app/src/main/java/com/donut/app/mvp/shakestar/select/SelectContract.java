package com.donut.app.mvp.shakestar.select;


import com.donut.app.http.message.shakestar.ShakeStarCommendResponse;
import com.donut.app.http.message.shakestar.ShakeStarSelectResponse;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

import java.util.List;

/**
 * Created by hard on 2018/1/30.
 */

public interface SelectContract {
    interface View extends BaseView{
        void showView(List<ShakeStarSelectResponse.MaterialListBean> selectResponse);
    }

    abstract class Presenter<V extends View> extends BasePresenterImpl<V> {

    }

}
