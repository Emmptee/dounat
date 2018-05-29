package com.donut.app.mvp.spinOff.goods;

import com.donut.app.http.message.spinOff.GoodsListResponse;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

/**
 * Created by Qi on 2017/6/5.
 * Description : <br>
 */
public interface SpinOffGoodsContract {
    interface View extends BaseView {
        void showView(GoodsListResponse detailResponse);
    }

    abstract class Presenter<V extends View> extends BasePresenterImpl<V> {

    }
}
