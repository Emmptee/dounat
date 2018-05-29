package com.donut.app.mvp.home;

import com.donut.app.http.message.SubjectHomePageResponse;
import com.donut.app.http.message.shakestar.Channel;
import com.donut.app.http.message.shakestar.SweetResponse;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public interface HomeContract {
    interface View extends BaseView {
        void showView(SubjectHomePageResponse detailResponse);
        void showList(SweetResponse sweetResponse);
        void gotoChannelDetail(int channelType, String subjectId);
        void showChannelList(Channel channel);

        void onToVideoClick();
    }

    abstract class Presenter<V extends View> extends BasePresenterImpl<V> {

    }
}
