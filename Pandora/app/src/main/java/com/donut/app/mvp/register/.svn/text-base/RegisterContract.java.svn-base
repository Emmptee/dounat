package com.donut.app.mvp.register;

import android.content.Context;

import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public interface RegisterContract {
    interface View extends BaseView {
        void getVerCode();

        void registerClickable(boolean flag);

        void registerSuccess();
    }

    abstract class Presenter extends BasePresenterImpl<View> {

        public abstract boolean register(String phoneNum, String password,
                                         String code, Context context);

        public abstract boolean registerStar(String phoneNum, String password,
                                             String starCode, Context context);
    }
}
