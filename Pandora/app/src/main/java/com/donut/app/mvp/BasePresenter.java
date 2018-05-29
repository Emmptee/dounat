package com.donut.app.mvp;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
interface BasePresenter<V extends BaseView> {
    void attachView(V view);

    void detachView();

    void cancelLoad();
}
