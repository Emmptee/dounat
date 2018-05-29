package com.donut.app.mvp.star.area;

/**
 * Created by Qi on 2017/3/16.
 * Description : 类为public是必须的,在反射中强转时非public会报错<br>
 */
public class StarAreaItemPresenter<V extends StarAreaContract.View> extends StarAreaContract.Presenter <V>{


    public void loadData(boolean showLoadingView) {
    }

    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
    }
}
