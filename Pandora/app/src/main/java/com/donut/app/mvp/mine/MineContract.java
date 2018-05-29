package com.donut.app.mvp.mine;

import com.donut.app.http.message.UserInfoResponse;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

/**
 * Created by Qi on 2017/3/28.
 * Description : <br>
 */
public interface MineContract {
    interface View extends BaseView {
        void showView();

        void showShopCartAmount(float totalAmount);

        void showStarUserInfo(UserInfoResponse res);
    }

    abstract class Presenter<V extends View> extends BasePresenterImpl<V> {

        public abstract void getStarUserInfo();
    }
}
