package com.donut.app.mvp.channel.search;

import com.donut.app.http.message.SubjectListDetail;
import com.donut.app.http.message.SubjectListResponse;
import com.donut.app.mvp.BasePresenterImpl;
import com.donut.app.mvp.BaseView;

/**
 * Created by Qi on 2017/3/21.
 * Description : <br>
 */
public interface ChannelSearchContract {
    interface View extends BaseView {
        void showView(SubjectListResponse response);
        void onItemClick(SubjectListDetail detail);
    }

    abstract class Presenter extends BasePresenterImpl<View> {

    }
}
