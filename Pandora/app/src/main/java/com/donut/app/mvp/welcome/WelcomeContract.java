package com.donut.app.mvp.welcome;

import com.donut.app.http.message.PresentGetResponse;
import com.donut.app.http.message.WelcomeADResponse;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

import java.util.List;

/**
 * Created by Qi on 2017/3/21.
 * Description : <br>
 */
public interface WelcomeContract {
    interface View extends BaseView {
        void showAdView(List<WelcomeADResponse.ADItem> startPageList);

        void showWelcomeView(boolean isDelay);

        void exitView();

        void gotoHomeView();
    }

    abstract class Presenter extends BasePresenterImpl<View> {

    }
}
