package com.donut.app.mvp.shakestar.mine;


import com.donut.app.http.message.shakestar.MaterialResponse;
import com.donut.app.http.message.shakestar.MyLikeResponse;
import com.donut.app.http.message.shakestar.MyProductionResponse;
import com.donut.app.http.message.shakestar.ShakeStarCommendResponse;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

import java.util.List;

/**
 * Created by hard on 2018/1/30.
 */

public interface MineContract {
    interface View extends BaseView{
        void showView(List<MyProductionResponse.MyShakingStarListBean> myShakingStarListBeans,MyProductionResponse myProductionResponse);
        void showLikeView(List<MyLikeResponse.MyLikeShakingStarListBean> myLikelist);
        void showMaterialView(List<MaterialResponse.MyMaterialListBean> myMaterialListBeans);
    }

    abstract class Presenter<V extends View> extends BasePresenterImpl<V> {

    }

}
