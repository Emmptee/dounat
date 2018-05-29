package com.donut.app.mvp.channel.list2;

import com.donut.app.http.message.SubjectListResponse;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

/**
 * Created by Qi on 2017/3/21.
 * Description : <br>
 */
public interface ChannelList2Contract {
    interface View extends BaseView {
        void showView(SubjectListResponse response);
    }

    abstract class Presenter extends BasePresenterImpl<View> {

    }
}
