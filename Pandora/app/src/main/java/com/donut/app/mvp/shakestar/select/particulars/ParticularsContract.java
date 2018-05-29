package com.donut.app.mvp.shakestar.select.particulars;


import com.donut.app.http.message.shakestar.CommendAllResponse;
import com.donut.app.http.message.shakestar.ParticularsResponse;
import com.donut.app.http.message.shakestar.ShakeStarCommendResponse;
import com.donut.app.http.message.shakestar.ShakeStarSelectResponse;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

import java.util.List;

/**
 * Created by hard on 2018/1/30.
 */

public interface ParticularsContract {
    interface View extends BaseView{
        void showView( List<ParticularsResponse.ShakingStarListBean> starCommendResponses,ParticularsResponse particularsResponse);
        void showCommend(List<CommendAllResponse.CommentsListBean> commendAllResponse, CommendAllResponse Response);

    }

    abstract class Presenter<V extends View> extends BasePresenterImpl<V> {

    }

}
